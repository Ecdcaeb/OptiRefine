package mods.Hileb.optirefine.validation;

import com.mojang.authlib.GameProfile;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.optifine.RandomEntity;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class ShoulderTextureContracts {
    private static final String PACK_NAME = "shoulder-texture-contract";
    private static final String RANDOM_PATH = "optifine/random/entity/parrot/parrot_red_blue";
    private static final ResourceLocation BASE = new ResourceLocation("textures/entity/parrot/parrot_red_blue.png");
    private static final ResourceLocation LEFT = new ResourceLocation(RANDOM_PATH + "3.png");
    private static final ResourceLocation RIGHT = new ResourceLocation(RANDOM_PATH + "4.png");
    private static final UUID LEFT_ID = new UUID(0, 101);
    private static final UUID RIGHT_ID = new UUID(0, 102);
    private static final int PLAYER_ID = Integer.MIN_VALUE + 7686;
    private static final int PARROT_ID = PLAYER_ID + 1;

    private ShoulderTextureContracts() {}

    static void verify(Minecraft mc) throws Exception {
        ResourcePackRepository repository = mc.getResourcePackRepository();
        List<ResourcePackRepository.Entry> initialPacks = repository.getRepositoryEntries();
        Path pack = repository.getDirResourcepacks().toPath().resolve(PACK_NAME);
        Field randomOption = mc.gameSettings.getClass().getField("ofRandomEntities");
        Field renderedEntity = RenderGlobal.class.getField("renderedEntity");
        Field leftShoulder = mc.player.getClass().getField("entityShoulderLeft");
        Field rightShoulder = mc.player.getClass().getField("entityShoulderRight");
        Field leftModel = field(LayerEntityOnShoulder.class, "leftModel", "field_192868_d");
        Field rightModel = field(LayerEntityOnShoulder.class, "rightModel", "field_192872_h");
        Method setLeft = setter("setLeftShoulderEntity", "func_192029_h");
        Method setRight = setter("setRightShoulderEntity", "func_192031_i");
        Method boundTexture = mc.getTextureManager().getClass().getMethod("getBoundTextureLocation");
        Entity previousEntity = (Entity) renderedEntity.get(mc.renderGlobal);
        boolean randomEnabled = randomOption.getBoolean(mc.gameSettings);
        boolean rescale = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        FloatBuffer previousColor = BufferUtils.createFloatBuffer(4);
        GL11C.glGetFloatv(GL11.GL_CURRENT_COLOR, previousColor);
        EntityOtherPlayerMP player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.randomUUID(), "ShoulderProbe"));
        player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        LayerEntityOnShoulder layer = new LayerEntityOnShoulder(mc.getRenderManager());
        try {
            createPack(pack);
            repository.updateRepositoryEntriesAll();
            List<ResourcePackRepository.Entry> enabled = new ArrayList<>(initialPacks);
            enabled.add(repository.getRepositoryEntriesAll().stream()
                    .filter(entry -> PACK_NAME.equals(entry.getResourcePackName())).findFirst().orElseThrow());
            repository.setRepositories(enabled);
            randomOption.setBoolean(mc.gameSettings, true);
            mc.refreshResources();

            EntityParrot tracked = parrot(mc, LEFT_ID, "ShoulderLeft", 91);
            NBTTagCompound leftTag = shoulderTag(tracked);
            NBTTagCompound rightTag = shoulderTag(parrot(mc, RIGHT_ID, "ShoulderRight", 94));
            // The right shoulder arrives with the player's first metadata. The left tracked entity disappears first.
            setRight.invoke(player, rightTag);
            mc.world.addEntityToWorld(PLAYER_ID, player);
            mc.world.addEntityToWorld(PARROT_ID, tracked);
            BlockPos spawn = tracked.getPosition();
            RandomEntity random = new RandomEntity();
            random.setEntity(tracked);
            Biome biome = random.getSpawnBiome();
            tracked.setPosition(player.posX, 108, player.posZ);
            mc.world.removeEntityFromWorld(PARROT_ID);
            mc.world.removeEntityDangerously(tracked);
            setLeft.invoke(player, leftTag);
            EntityParrot left = (EntityParrot) leftShoulder.get(player);
            EntityParrot right = (EntityParrot) rightShoulder.get(player);
            require(left == tracked && right != null && RIGHT_ID.equals(right.getUniqueID()), "Shoulder UUID handoff failed");
            random.setEntity(right);
            require("ShoulderRight".equals(random.getName()) && random.getSpawnPosition().getY() == 94,
                    "Already seated parrot lost its serialized name or position");

            renderedEntity.set(mc.renderGlobal, mc.player);
            CemRenderContext.push(mc.player, mc.world, 0.25F);
            try {
                render(layer, player);
                assertBound(mc, boundTexture, RIGHT);
                Probe leftProbe = new Probe(mc, renderedEntity, left);
                Probe rightProbe = new Probe(mc, renderedEntity, right);
                leftModel.set(layer, leftProbe);
                rightModel.set(layer, rightProbe);
                leftProbe.nested = () -> render(layer, player);
                GlStateManager.enableRescaleNormal();
                int matrixDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
                render(layer, player);
                require(leftProbe.renders == 2 && rightProbe.renders == 2, "Both shoulders did not render in nested calls");
                require(leftProbe.texture.equals(LEFT) && rightProbe.texture.equals(RIGHT), "Wrong G5 shoulder texture selected");
                assertContext(mc, renderedEntity, matrixDepth);
                RuntimeException fault = new IllegalStateException("Intentional shoulder model failure");
                rightProbe.fault = fault;
                try {
                    render(layer, player);
                    throw new AssertionError("Faulting shoulder model was not rendered");
                } catch (RuntimeException error) {
                    if (error != fault) throw error;
                }
                rightProbe.fault = null;
                assertContext(mc, renderedEntity, matrixDepth);

                setLeft.invoke(player, new NBTTagCompound());
                render(layer, player);
                assertBound(mc, boundTexture, RIGHT);
                setRight.invoke(player, leftTag);
                require(leftShoulder.get(player) == null && rightShoulder.get(player) == tracked,
                        "Parrot identity did not follow the new shoulder slot");
                render(layer, player);
                assertBound(mc, boundTexture, LEFT);

                setRight.invoke(player, new NBTTagCompound());
                EntityParrot released = parrot(mc, LEFT_ID, "ShoulderLeft", 108);
                mc.world.addEntityToWorld(PARROT_ID, released);
                random.setEntity(released);
                require(spawn.equals(random.getSpawnPosition()) && biome == random.getSpawnBiome(),
                        "Released parrot lost the tracked spawn conditions");
                setLeft.invoke(player, leftTag);
                mc.world.removeEntityFromWorld(PARROT_ID);
                mc.world.removeEntityDangerously(released);
                require(leftShoulder.get(player) == released, "Metadata-first seating lost the released entity");
                leftModel.set(layer, new Probe(mc, renderedEntity, released));
                render(layer, player);
                assertBound(mc, boundTexture, LEFT);

                repository.setRepositories(initialPacks);
                mc.refreshResources();
                render(layer, player);
                assertBound(mc, boundTexture, BASE);
                require(leftShoulder.get(player) == released, "Pack reload replaced the shoulder entity");
            } finally {
                CemRenderContext.pop();
            }
        } finally {
            mc.world.removeEntityFromWorld(PARROT_ID);
            mc.world.removeEntityFromWorld(PLAYER_ID);
            renderedEntity.set(mc.renderGlobal, previousEntity);
            if (rescale) GlStateManager.enableRescaleNormal();
            else GlStateManager.disableRescaleNormal();
            randomOption.setBoolean(mc.gameSettings, randomEnabled);
            repository.setRepositories(initialPacks);
            mc.refreshResources();
            GlStateManager.color(previousColor.get(0), previousColor.get(1), previousColor.get(2), previousColor.get(3));
        }
    }

    private static void createPack(Path pack) throws Exception {
        Path assets = pack.resolve("assets/minecraft");
        Files.createDirectories(assets.resolve(RANDOM_PATH).getParent());
        Files.writeString(pack.resolve("pack.mcmeta"), "{\"pack\":{\"pack_format\":3,\"description\":\"Shoulder contract\"}}");
        Files.writeString(assets.resolve(RANDOM_PATH + ".properties"), """
                textures.1=2 3
                name.1=ShoulderLeft
                heights.1=91
                textures.2=4
                name.2=ShoulderRight
                heights.2=94
                """);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        for (int variant = 2; variant <= 4; ++variant) {
            int color = 0xFF000000 | 0x550000 * (variant - 1);
            for (int y = 0; y < image.getHeight(); ++y) {
                for (int x = 0; x < image.getWidth(); ++x) image.setRGB(x, y, color);
            }
            ImageIO.write(image, "png", assets.resolve(RANDOM_PATH + variant + ".png").toFile());
        }
    }

    private static EntityParrot parrot(Minecraft mc, UUID id, String name, int y) {
        EntityParrot parrot = new EntityParrot(mc.world);
        parrot.setUniqueId(id);
        parrot.setCustomNameTag(name);
        parrot.setPosition(mc.player.posX, y, mc.player.posZ);
        return parrot;
    }

    private static NBTTagCompound shoulderTag(EntityParrot parrot) {
        NBTTagCompound tag = parrot.writeToNBT(new NBTTagCompound());
        tag.setString("id", "minecraft:parrot");
        return tag;
    }

    private static void render(LayerEntityOnShoulder layer, EntityPlayer player) {
        layer.doRenderLayer(player, 0, 0, 0.25F, 0, 0, 0, 0.0625F);
    }

    private static void assertContext(Minecraft mc, Field renderedEntity, int matrixDepth) throws Exception {
        require(renderedEntity.get(mc.renderGlobal) == mc.player && CemRenderContext.entity() == mc.player
                        && CemRenderContext.partialTicks() == 0.25F
                        && GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL)
                        && GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH) == matrixDepth,
                "Shoulder rendering leaked random entity, CEM or GL context");
    }

    private static void assertBound(Minecraft mc, Method boundTexture, ResourceLocation expected) throws Exception {
        require(expected.equals(boundTexture.invoke(mc.getTextureManager())), "Expected shoulder texture " + expected);
        ITextureObject texture = mc.getTextureManager().getTexture(expected);
        require(texture != null && texture.getGlTextureId() == GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D),
                "Expected the native GL binding for " + expected);
    }

    private static Field field(Class<?> owner, String mcp, String srg) throws Exception {
        Field field;
        try { field = owner.getDeclaredField(mcp); }
        catch (NoSuchFieldException missing) { field = owner.getDeclaredField(srg); }
        field.setAccessible(true);
        return field;
    }

    private static Method setter(String mcp, String srg) throws Exception {
        Method method;
        try { method = EntityPlayer.class.getDeclaredMethod(mcp, NBTTagCompound.class); }
        catch (NoSuchMethodException missing) { method = EntityPlayer.class.getDeclaredMethod(srg, NBTTagCompound.class); }
        method.setAccessible(true);
        return method;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static final class Probe extends ModelParrot {
        private final Minecraft mc;
        private final Field renderedEntity;
        private final EntityParrot parrot;
        private ResourceLocation texture;
        private Runnable nested;
        private RuntimeException fault;
        private int renders;

        private Probe(Minecraft mc, Field renderedEntity, EntityParrot parrot) {
            this.mc = mc;
            this.renderedEntity = renderedEntity;
            this.parrot = parrot;
        }

        @Override
        public void render(Entity player, float swing, float speed, float age, float headYaw, float headPitch, float scale) {
            ++renders;
            try {
                require(renderedEntity.get(mc.renderGlobal) == parrot && CemRenderContext.entity() == parrot,
                        "Shoulder model did not see the same parrot as RandomEntities");
                texture = (ResourceLocation) mc.getTextureManager().getClass().getMethod("getBoundTextureLocation")
                        .invoke(mc.getTextureManager());
                if (nested != null) {
                    Runnable action = nested;
                    nested = null;
                    action.run();
                    require(renderedEntity.get(mc.renderGlobal) == parrot && CemRenderContext.entity() == parrot,
                            "Nested shoulder render did not restore its caller's entity");
                    mc.getTextureManager().bindTexture(BASE);
                }
            } catch (ReflectiveOperationException error) {
                throw new AssertionError(error);
            }
            if (fault != null) throw fault;
            super.render(player, swing, speed, age, headYaw, headPitch, scale);
        }
    }
}
