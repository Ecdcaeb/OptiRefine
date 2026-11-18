package mods.Hileb.optirefine.optifine.resources;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.optifine.reflect.Reflector;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ForgeResourcePacks {
    private ForgeResourcePacks() {
    }

    /** Adds the live Forge packs in Minecraft's low-to-high order, below selected and server packs. */
    public static IResourcePack[] prependActivePacks(IResourcePack[] selected) {
        List<IResourcePack> active = activePacks();
        if (active.isEmpty()) {
            return selected;
        }
        IResourcePack[] added = new IResourcePack[active.size()];
        Set<File> sources = new HashSet<>();
        int first = added.length;
        // Multiple mods can share an archive. Keep the instance with the highest live priority.
        for (int i = active.size() - 1; i >= 0; --i) {
            IResourcePack pack = active.get(i);
            File source = forgePackSource(pack);
            if (source != null && sources.add(source) && !containsPack(selected, pack)) {
                added[--first] = pack;
            }
        }
        if (first == added.length) {
            return selected;
        }
        int addedCount = added.length - first;
        IResourcePack[] packs = new IResourcePack[addedCount + selected.length];
        System.arraycopy(added, first, packs, 0, addedCount);
        System.arraycopy(selected, 0, packs, addedCount, selected.length);
        return packs;
    }

    /** The caller has already checked the selected and server packs. Vanilla default is the fallback. */
    public static IResourcePack findDefiningPack(ResourceLocation location) {
        List<IResourcePack> active = activePacks();
        for (int i = active.size() - 1; i >= 0; --i) {
            IResourcePack pack = active.get(i);
            if (!(pack instanceof DefaultResourcePack) && pack.resourceExists(location)
                    && (forgePackSource(pack) != null || pack.getResourceDomains().contains(location.getNamespace()))) {
                return pack;
            }
        }
        return null;
    }

    private static List<IResourcePack> activePacks() {
        FMLClientHandler handler = FMLClientHandler.instance();
        // Both Cleanroom versions install the list before Forge enters LOADING and registers any packs.
        if (handler.getClient() == null || !Loader.instance().hasReachedState(LoaderState.LOADING)) {
            return List.of();
        }
        return handler.getResourcePackList();
    }

    private static File forgePackSource(IResourcePack pack) {
        Object base = pack instanceof LegacyV2Adapter
                ? Reflector.getFieldValue(pack, Reflector.LegacyV2Adapter_pack)
                : pack;
        if (base instanceof FMLFileResourcePack || base instanceof FMLFolderResourcePack) {
            // Forge builds this pack from ModContainer.getResource(), which can differ from getSource().
            return ((AbstractResourcePack) base).getResourcePackFile();
        }
        return null;
    }

    private static boolean containsPack(IResourcePack[] packs, IResourcePack candidate) {
        for (IResourcePack pack : packs) {
            if (pack == candidate) {
                return true;
            }
        }
        return false;
    }
}
