package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.optifine.ConnectedTextures;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.optifine.render.RenderEnv;
import net.optifine.util.ResUtils;

import java.util.Arrays;
import java.util.List;

public final class ForgeModResourceContracts {
    private static final String MOD_ID = "optirefine_client_contracts";
    private static final String CIT_RULE = "mcpatcher/cit/forge_contract.properties";
    private static final String CTM_RULE = "mcpatcher/ctm/forge_contract.properties";

    private ForgeModResourceContracts() {
    }

    /** After the normal resource reload with modern-base.zip selected. */
    public static void verify(Minecraft mc) {
        IResourcePack pack = FMLClientHandler.instance().getResourcePackFor(MOD_ID);
        Object backingPack = pack instanceof LegacyV2Adapter
                ? Reflector.getFieldValue(pack, Reflector.LegacyV2Adapter_pack) : pack;
        require(backingPack instanceof FMLFileResourcePack forgePack && forgePack.getResourcePackFile().isFile()
                        && forgePack.getResourcePackFile().equals(forgePack.getFMLContainer().getResource()),
                "The Forge pack should come from the client contracts mod's resource archive");
        require(Config.getDefiningResourcePack(new ResourceLocation(CIT_RULE)) == pack,
                "Config did not return the exact Forge pack defining the CIT rule");
        require(Config.getDefiningResourcePack(new ResourceLocation(CTM_RULE)) == pack,
                "Config did not resolve the CTM alias to its exact Forge pack");
        require(Config.getDefiningResourcePack(new ResourceLocation(MOD_ID, "mcpatcher/cit/forge_foreign.properties")) == pack,
                "The Forge pack's additional namespace is no longer readable");
        require(Config.getDefiningResourcePack(new ResourceLocation("textures/blocks/gold_block.png"))
                        == Config.getDefaultResourcePack(),
                "The vanilla default pack lost ownership of an unmodified texture");

        List<IResourcePack> packs = Arrays.asList(Config.getResourcePacks());
        require(packs.stream().filter(candidate -> candidate == pack).count() == 1,
                "Config should list the live Forge pack once");
        require(!packs.contains(Config.getDefaultResourcePack()), "Config duplicated its separately handled default pack");
        ResourcePackRepository repository = mc.getResourcePackRepository();
        List<ResourcePackRepository.Entry> selected = repository.getRepositoryEntries();
        for (ResourcePackRepository.Entry entry : repository.getRepositoryEntriesAll()) {
            int index = packs.indexOf(entry.getResourcePack());
            require(selected.contains(entry) ? index > packs.indexOf(pack) : index == -1,
                    "Config changed selection or precedence for " + entry.getResourcePackName());
        }
        ResourceLocation sharedModel = new ResourceLocation("mcpatcher/cit/modern.json");
        require(pack.resourceExists(sharedModel) && selected.size() == 1
                        && selected.getFirst().getResourcePack().resourceExists(sharedModel)
                        && Config.getDefiningResourcePack(sharedModel) == selected.getFirst().getResourcePack(),
                "Config did not resolve the selected pack's model above the Forge pack's legacy copy");
        String[] cit = ResUtils.collectFiles("mcpatcher/cit/", ".properties");
        require(Arrays.asList(cit).contains(CIT_RULE), "ResUtils did not discover the Forge CIT rule");
        require(Arrays.stream(cit).noneMatch(file -> file.endsWith("forge_foreign.properties")),
                "ResUtils treated a mod namespace as minecraft");
        String[] ctm = ResUtils.collectFiles(pack, "mcpatcher/ctm/", ".properties", null);
        require(Arrays.stream(ctm).filter(file -> file.endsWith("forge_contract.properties")).toList()
                        .equals(List.of(CTM_RULE)),
                "ResUtils did not deduplicate the Forge CTM aliases");

        TextureAtlasSprite gold = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/gold_block");
        require(gold != mc.getTextureMapBlocks().getMissingSprite(), "Gold block fixture texture is missing");
        verifyCit(mc, gold);
        verifyCtm(mc, gold);
        verifyStateConnection(mc);
    }

    private static void verifyCit(Minecraft mc, TextureAtlasSprite expected) {
        ItemStack sword = new ItemStack(Items.IRON_SWORD).setStackDisplayName("OptiRefine Forge CIT");
        IBakedModel vanilla = mc.getRenderItem().getItemModelMesher().getItemModel(sword);
        IBakedModel custom = mc.getRenderItem().getItemModelWithOverrides(sword, mc.world, mc.player);
        require(custom != vanilla && custom.getParticleTexture() == expected
                        && custom == CustomItems.getCustomItemModel(sword, vanilla, null, true),
                "Native CIT did not render the baked model from the Forge pack");
        require(mc.getRenderItem().getItemModelWithOverrides(new ItemStack(Items.IRON_SWORD), mc.world, mc.player) == vanilla,
                "Native CIT did not honor the Forge rule's NBT filter");
    }

    private static void verifyCtm(Minecraft mc, TextureAtlasSprite expected) {
        IBlockState state = Blocks.EMERALD_BLOCK.getDefaultState();
        BakedQuad quad = mc.getBlockRendererDispatcher().getModelForState(state)
                .getQuads(state, EnumFacing.NORTH, 0).getFirst();
        BlockPos pos = mc.player.getPosition();
        BakedQuad[] connected = ConnectedTextures.getConnectedTexture(mc.world, state, pos, quad, new RenderEnv(state, pos));
        require(quad.getSprite() != expected && connected.length == 1 && connected[0].getSprite() == expected,
                "Native CTM did not apply the modern Forge rule to the block quad");
    }

    private static void verifyStateConnection(Minecraft mc) {
        IBlockState white = Blocks.WOOL.getStateFromMeta(0);
        IBlockState blue = Blocks.WOOL.getStateFromMeta(11);
        BlockPos pos = mc.player.getPosition().up(3);
        BlockPos east = pos.east();
        BlockPos west = pos.west();
        IBlockState oldEast = mc.world.getBlockState(east);
        IBlockState oldWest = mc.world.getBlockState(west);
        BakedQuad face = mc.getBlockRendererDispatcher().getModelForState(white)
                .getQuads(white, EnumFacing.NORTH, 0).getFirst();
        try {
            mc.world.setBlockState(east, white, 3);
            mc.world.setBlockState(west, white, 3);
            TextureAtlasSprite connected = ConnectedTextures.getConnectedTexture(mc.world, white, pos, face,
                    new RenderEnv(white, pos))[0].getSprite();
            mc.world.setBlockState(east, blue, 3);
            mc.world.setBlockState(west, blue, 3);
            TextureAtlasSprite disconnected = ConnectedTextures.getConnectedTexture(mc.world, white, pos, face,
                    new RenderEnv(white, pos))[0].getSprite();
            require(connected == mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/gold_block")
                            && disconnected == mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/diamond_block"),
                    "CTM connect=state did not distinguish same-block variants");
        } finally {
            mc.world.setBlockState(east, oldEast, 3);
            mc.world.setBlockState(west, oldWest, 3);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
