package mods.Hileb.optirefine.mixin.defaults.cleanroom;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Cleanroom ModList 3D item icons are drawn over GuiSlot.drawSelectionBox's opaque
 * black selection fill. Pad an opaque row-colored rect behind the icon so
 * selected/unselected look identical. (Experiment: no Shaders/GL program handling —
 * only the drawRect Tessellator pass remains.)
 */
@Mixin(targets = "com.cleanroommc.client.modlist.screen.ModListScreen$ModListEntry")
public abstract class MixinModListScreen$ModListEntry {

    @WrapMethod(method = "drawIcon")
    private void optiRefine$safeDrawIcon(int top, int left, Operation<Void> original) {
        try {
            Gui.drawRect(left + 4, top + 2, left + 20, top + 18, 0xFF14141A);
            original.call(top, left);
        } catch (Throwable t) {
            // Keep the list alive; Cleanroom already has its own catch around the item call.
        }
    }
}
