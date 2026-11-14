package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/** Invokes the Forge API supplied by Cleanroom directly. */
@Mixin(targets = "net.optifine.reflect.ReflectorForge", remap = false)
public abstract class MixinReflectorForge {
    /**
     * @author OptiRefine
     * @reason Report broken textures through the installed Forge client handler.
     */
    @Overwrite(remap = false)
    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation loc, String message) {
        FMLClientHandler.instance().trackBrokenTexture(loc, message);
    }

    /**
     * @author OptiRefine
     * @reason Report missing textures through the installed Forge client handler.
     */
    @Overwrite(remap = false)
    public static void FMLClientHandler_trackMissingTexture(ResourceLocation loc) {
        FMLClientHandler.instance().trackMissingTexture(loc);
    }

    /**
     * @author OptiRefine
     * @reason Publish launch information through the typed Launch blackboard.
     */
    @Overwrite(remap = false)
    public static void putLaunchBlackboard(String key, Object value) {
        Launch.blackboard.put(key, value);
    }

    /**
     * @author OptiRefine
     * @reason Dispatch first-person hand rendering through the Forge client hook.
     */
    @Overwrite(remap = false)
    public static boolean renderFirstPersonHand(RenderGlobal renderGlobal, float partialTicks, int pass) {
        return ForgeHooksClient.renderFirstPersonHand(renderGlobal, partialTicks, pass);
    }

    /**
     * @author OptiRefine
     * @reason Use the existing resource helper while preserving G5's single leading-slash removal.
     */
    @Overwrite(remap = false)
    public static InputStream getOptiFineResourceStream(String path) {
        return OptifineHelper.getOptifineResource(path.startsWith("/") ? path.substring(1) : path);
    }

    /**
     * @author OptiRefine
     * @reason Preserve state-aware block overrides through Forge's virtual method.
     */
    @Overwrite(remap = false)
    public static boolean blockHasTileEntity(IBlockState state) {
        return state.getBlock().hasTileEntity(state);
    }

    /**
     * @author OptiRefine
     * @reason Preserve item-specific durability-bar overrides.
     */
    @Overwrite(remap = false)
    public static boolean isItemDamaged(ItemStack stack) {
        return stack.getItem().showDurabilityBar(stack);
    }

    /**
     * @author OptiRefine
     * @reason Preserve armor-specific overlay overrides.
     */
    @Overwrite(remap = false)
    public static boolean armorHasOverlay(ItemArmor itemArmor, ItemStack itemStack) {
        return itemArmor.hasOverlay(itemStack);
    }

    /**
     * @author OptiRefine
     * @reason Query Forge's world- and position-aware block light value.
     */
    @Overwrite(remap = false)
    public static int getLightValue(IBlockState stateIn, IBlockAccess worldIn, BlockPos posIn) {
        return stateIn.getLightValue(worldIn, posIn);
    }

    /**
     * @author OptiRefine
     * @reason Dispatch map lookup to the stack's actual map item as G5 does with Forge installed.
     */
    @Overwrite(remap = false)
    public static MapData getMapData(ItemMap itemMap, ItemStack stack, World world) {
        return ((ItemMap) stack.getItem()).getMapData(stack, world);
    }

    /**
     * @author OptiRefine
     * @reason Read active mod IDs through Forge's typed loader and container API.
     */
    @Overwrite(remap = false)
    public static String[] getForgeModIds() {
        List<ModContainer> activeMods = Loader.instance().getActiveModList();
        String[] modIds = new String[activeMods.size()];
        int count = 0;
        for (int i = 0; i < activeMods.size(); i++) {
            ModContainer mod = activeMods.get(i);
            if (mod != null) {
                String modId = mod.getModId();
                if (modId != null) {
                    modIds[count++] = modId;
                }
            }
        }
        return count == modIds.length ? modIds : Arrays.copyOf(modIds, count);
    }

    /**
     * @author OptiRefine
     * @reason Preserve ALLOW precedence and apply vanilla spawn checks only for DEFAULT.
     */
    @Overwrite(remap = false)
    public static boolean canEntitySpawn(EntityLiving entityliving, World world, float x, float y, float z) {
        Event.Result result = ForgeEventFactory.canEntitySpawn(entityliving, world, x, y, z, false);
        return result == Event.Result.ALLOW
                || result == Event.Result.DEFAULT && entityliving.getCanSpawnHere() && entityliving.isNotColliding();
    }

    /**
     * @author OptiRefine
     * @reason Invoke Forge's special-spawn hook with primitive coordinates.
     */
    @Overwrite(remap = false)
    public static boolean doSpecialSpawn(EntityLiving entityliving, World world, float x, int y, float z) {
        return ForgeEventFactory.doSpecialSpawn(entityliving, world, x, y, z);
    }

    /**
     * @author OptiRefine
     * @reason Preserve state-aware baked-model ambient-occlusion overrides.
     */
    @Overwrite(remap = false)
    public static boolean isAmbientOcclusion(IBakedModel model, IBlockState state) {
        return model.isAmbientOcclusion(state);
    }

    /**
     * @author OptiRefine
     * @reason Resolve the registry name lazily for crash reporting, retaining the literal null representation.
     */
    @Overwrite(remap = false)
    public static ICrashReportDetail<String> getDetailItemRegistryName(Item item) {
        return () -> String.valueOf(item.getRegistryName());
    }
}
