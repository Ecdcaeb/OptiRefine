package mods.Hileb.optirefine.validation;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.optifine.CustomItems;
import net.optifine.RandomEntities;
import net.optifine.config.NbtTagValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class PackSelectorContracts {
    private static final String LOWER = "pack-selectors-lower";
    private static final String UPPER = "pack-selectors-upper";
    private static final String RANDOM = "optifine/random/entity/";
    private static final String SHEEP = "sheep/sheep";
    private static final String WOLF = "wolf/wolf";
    private static final String SLIME = "slime/slime";
    private static final String BED = "bed/red";
    private static final String DOTTED = "optirefine/selector_2";
    private static final String EXPLICIT = "optirefine/explicit_2";
    private static final String CIT = "optifine/cit/selector/";

    private PackSelectorContracts() {}

    static void verify(Minecraft mc) throws Exception {
        ResourcePackRepository repository = mc.getResourcePackRepository();
        List<ResourcePackRepository.Entry> originalPacks = repository.getRepositoryEntries();
        Path root = repository.getDirResourcepacks().toPath();
        Field random = mc.gameSettings.getClass().getField("ofRandomEntities");
        Field cit = mc.gameSettings.getClass().getField("ofCustomItems");
        Field rendered = RenderGlobal.class.getField("renderedEntity");
        Field renderedTile = TileEntityRendererDispatcher.class.getField("tileEntityRendered");
        TileEntityRendererDispatcher dispatcher = TileEntityRendererDispatcher.instance;
        boolean originalRandom = random.getBoolean(mc.gameSettings);
        boolean originalCit = cit.getBoolean(mc.gameSettings);
        Object originalEntity = rendered.get(mc.renderGlobal);
        Object originalTile = renderedTile.get(dispatcher);
        try {
            createPacks(root);
            repository.updateRepositoryEntriesAll();
            ResourcePackRepository.Entry lower = pack(repository, LOWER);
            ResourcePackRepository.Entry upper = pack(repository, UPPER);
            List<ResourcePackRepository.Entry> selected = new ArrayList<>(originalPacks);
            selected.add(lower);
            selected.add(upper);
            random.setBoolean(mc.gameSettings, true);
            cit.setBoolean(mc.gameSettings, true);
            repository.setRepositories(selected);
            mc.refreshResources();

            verifyCit(mc);
            verifyRandom(mc, rendered, renderedTile);
            EntitySheep sample = new EntitySheep(mc.world);
            sample.setUniqueId(new UUID(0, 1));
            rendered.set(mc.renderGlobal, sample);
            expectTexture(DOTTED, RANDOM + DOTTED + "2.png");
            expectTexture(EXPLICIT, RANDOM + EXPLICIT + "2.png");

            selected.set(selected.size() - 2, upper);
            selected.set(selected.size() - 1, lower);
            repository.setRepositories(selected);
            mc.refreshResources();
            expectTexture(DOTTED, RANDOM + DOTTED + ".2.png");
            expectTexture(SHEEP, RANDOM + SHEEP + "3.png");
        } finally {
            rendered.set(mc.renderGlobal, originalEntity);
            renderedTile.set(dispatcher, originalTile);
            random.setBoolean(mc.gameSettings, originalRandom);
            cit.setBoolean(mc.gameSettings, originalCit);
            repository.setRepositories(originalPacks);
            mc.refreshResources();
        }
    }

    private static void verifyCit(Minecraft mc) {
        ItemStack sword = new ItemStack(Items.GOLDEN_SWORD).setStackDisplayName("Selector Sword");
        sword.addEnchantment(Enchantments.SHARPNESS, 3);
        NBTTagCompound data = sword.getOrCreateSubCompound("Selector");
        data.setInteger("Age", -25);
        data.setInteger("Limit", Integer.MAX_VALUE);
        data.setInteger("Color", 0xff99cc);
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString("Old spelling"));
        lore.appendTag(new NBTTagString("Other line"));
        data.setTag("Lore", lore);
        NBTTagCompound raw = new NBTTagCompound();
        raw.setString("Marker", "client data");
        data.setTag("Raw", raw);

        expectCit(mc, sword, "range");
        data.setInteger("Age", -26);
        expectCit(mc, sword, "legacy");
        data.setInteger("Age", -5);
        expectCit(mc, sword, "range");
        data.setString("Age", "-5.0");
        expectCit(mc, sword, "legacy");
        data.setInteger("Age", -5);
        NBTTagCompound parent = new NBTTagCompound();
        parent.setBoolean("Child", true);
        data.setTag("Missing", parent);
        expectCit(mc, sword, "legacy");
        data.removeTag("Missing");
        data.setTag("Raw", new NBTTagCompound());
        expectCit(mc, sword, "legacy");
        data.setTag("Raw", raw);
        lore.appendTag(new NBTTagString("Unexpected line"));
        expectCit(mc, sword, "legacy");
        lore.removeTag(2);

        sword.getTagCompound().removeTag("ench");
        sword.addEnchantment(Enchantments.SMITE, 4);
        expectCit(mc, sword, "range");
        sword.getTagCompound().removeTag("ench");
        sword.addEnchantment(Enchantments.UNBREAKING, 4);
        expectCit(mc, sword, null);
        require(new NbtTagValue("Missing.Child", "exists:false").matches(null), "An absent item tag must match exists:false");
        require(!new NbtTagValue("Missing.Child", "exists:true").matches(null), "An absent item tag must fail exists:true");
        require(new NbtTagValue("*", "exists:false").matches(new NBTTagCompound()), "An empty compound has no wildcard matches");
    }

    private static void verifyRandom(Minecraft mc, Field rendered, Field renderedTile) throws Exception {
        BlockPos support = mc.player.getPosition().east(4).up(6);
        IBlockState originalBlock = mc.world.getBlockState(support);
        try {
            mc.world.setBlockState(support, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE), 2);
            EntitySheep sheep = new EntitySheep(mc.world);
            position(sheep, support);
            sheep.setFleeceColor(EnumDyeColor.SILVER);
            sheep.setCustomNameTag("Selector sheep");
            rendered.set(mc.renderGlobal, sheep);
            expectTexture(SHEEP, RANDOM + SHEEP + "2.png");
            sheep.setSheared(true);
            expectTexture(SHEEP, RANDOM + SHEEP + "3.png");
            sheep.setSheared(false);
            sheep.setFleeceColor(EnumDyeColor.WHITE);
            expectTexture(SHEEP, RANDOM + SHEEP + "3.png");
            sheep.setFleeceColor(EnumDyeColor.SILVER);
            mc.world.setBlockState(support, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED), 2);
            expectTexture(SHEEP, RANDOM + SHEEP + "3.png");

            EntityWolf wolf = new EntityWolf(mc.world);
            wolf.setTamed(true);
            wolf.setCollarColor(EnumDyeColor.BLUE);
            rendered.set(mc.renderGlobal, wolf);
            expectTexture(WOLF, RANDOM + WOLF + "2.png");
            wolf.setTamed(false);
            expectTexture(WOLF, base(WOLF).getPath());

            EntitySlime slime = new EntitySlime(mc.world);
            rendered.set(mc.renderGlobal, slime);
            NBTTagCompound slimeTag = new NBTTagCompound();
            slimeTag.setInteger("Size", 3);
            slime.readEntityFromNBT(slimeTag);
            expectTexture(SLIME, RANDOM + SLIME + "2.png");
            slimeTag.setInteger("Size", 2);
            slime.readEntityFromNBT(slimeTag);
            expectTexture(SLIME, base(SLIME).getPath());

            mc.world.setBlockState(support, Blocks.BED.getDefaultState(), 2);
            TileEntity tile = mc.world.getTileEntity(support);
            require(tile instanceof TileEntityBed, "Client bed fixture did not create its block entity");
            TileEntityBed bed = (TileEntityBed) tile;
            bed.setColor(EnumDyeColor.RED);
            rendered.set(mc.renderGlobal, null);
            renderedTile.set(TileEntityRendererDispatcher.instance, bed);
            expectTexture(BED, RANDOM + BED + "2.png");
            bed.setColor(EnumDyeColor.BLUE);
            expectTexture(BED, base(BED).getPath());
        } finally {
            renderedTile.set(TileEntityRendererDispatcher.instance, null);
            mc.world.setBlockState(support, originalBlock, 2);
        }
    }

    private static void createPacks(Path root) throws Exception {
        Path lower = startPack(root, LOWER);
        Path upper = startPack(root, UPPER);
        properties(lower, RANDOM + SHEEP, "textures.1=3\n");
        properties(upper, RANDOM + SHEEP, """
                textures.1=2
                colors.1=light_gray
                nbt.1.CustomName=raw:"Selector sheep"
                nbt.1.Sheared=0
                blocks.1=minecraft:wool:color=blue
                textures.2=3
                """);
        properties(upper, RANDOM + WOLF, "skins.1=2\ncollarColors.1=blue\n");
        properties(upper, RANDOM + SLIME, "textures.1=2\nsizes.1=0-1 3\n");
        properties(upper, RANDOM + BED, "textures.1=2\ncolors.1=red\nblocks.1=minecraft:bed\n");
        for (String name : List.of(SHEEP + "2", SHEEP + "3", WOLF + "2", SLIME + "2", BED + "2", EXPLICIT + "2")) {
            image(lower, RANDOM + name, 0xFFFF8855);
        }
        properties(lower, RANDOM + EXPLICIT, "textures.1=2\n");
        image(lower, base(EXPLICIT).getPath().replace(".png", ""), 0xFFFF8855);
        image(lower, base(DOTTED).getPath().replace(".png", ""), 0xFFFF8855);
        image(lower, RANDOM + DOTTED + ".2", 0xFF55CC88);
        image(upper, RANDOM + DOTTED + "2", 0xFF6688FF);

        properties(upper, CIT + "range", """
                type=item
                items=minecraft:golden_sword
                texture=range
                weight=10
                enchantments=minecraft:sharpness smite
                enchantmentIDs=33
                enchantmentLevels=3-
                nbt.display.Name=raw:iregex:"selector sword"
                nbt.Selector.Age=range:(-25) (-10)-10 15 17
                nbt.Selector.Limit=range:2147483647
                nbt.Selector.Color=#ff99cc
                nbt.Selector.Lore.count=range:2
                nbt.Selector.Lore.2=exists:false
                nbt.Selector.Lore.0=!exists:false
                nbt.Selector.Missing.Child=exists:false
                nbt.Selector.Raw=raw:pattern:{Marker:*}
                """);
        properties(upper, CIT + "legacy", """
                type=item
                items=minecraft:golden_sword
                texture=legacy
                enchantmentIDs=16 minecraft:smite
                nbt.display.Name=pattern:Selector *
                nbt.Selector.Lore.*=ipattern:old *
                """);
        image(upper, CIT + "range", 0xFF00AA66);
        image(upper, CIT + "legacy", 0xFFAA0066);
    }

    private static Path startPack(Path root, String name) throws Exception {
        Path pack = root.resolve(name);
        Files.createDirectories(pack);
        Files.writeString(pack.resolve("pack.mcmeta"), "{\"pack\":{\"pack_format\":3,\"description\":\"Pack selector contract\"}}");
        return pack.resolve("assets/minecraft");
    }

    private static ResourcePackRepository.Entry pack(ResourcePackRepository repository, String name) {
        return repository.getRepositoryEntriesAll().stream().filter(entry -> name.equals(entry.getResourcePackName()))
                .findFirst().orElseThrow();
    }

    private static void properties(Path assets, String name, String contents) throws Exception {
        Path path = assets.resolve(name + ".properties");
        Files.createDirectories(path.getParent());
        Files.writeString(path, contents);
    }

    private static void image(Path assets, String name, int color) throws Exception {
        Path path = assets.resolve(name + ".png");
        Files.createDirectories(path.getParent());
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) image.setRGB(x, y, color);
        }
        ImageIO.write(image, "png", path.toFile());
    }

    private static void position(Entity entity, BlockPos support) {
        entity.setPosition(support.getX() + 0.5, support.getY() + 1, support.getZ() + 0.5);
    }

    private static ResourceLocation base(String entityTexture) {
        return new ResourceLocation("textures/entity/" + entityTexture + ".png");
    }

    private static void expectTexture(String entityTexture, String expected) {
        ResourceLocation actual = RandomEntities.getTextureLocation(base(entityTexture));
        require(actual.equals(new ResourceLocation(expected)), "Expected random texture " + expected + ", got " + actual);
    }

    private static void expectCit(Minecraft mc, ItemStack stack, String sprite) {
        IBakedModel vanilla = mc.getRenderItem().getItemModelMesher().getItemModel(stack);
        IBakedModel resolved = mc.getRenderItem().getItemModelWithOverrides(stack, mc.world, mc.player);
        IBakedModel actual = CustomItems.getCustomItemModel(stack, resolved, null, false);
        if (sprite == null) {
            require(actual == vanilla, "Unexpected CIT sprite " + actual.getParticleTexture().getIconName());
        } else {
            String name = actual.getParticleTexture().getIconName();
            require(actual != vanilla && name.endsWith("/selector/" + sprite), "Expected CIT sprite " + sprite + ", got " + name);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
