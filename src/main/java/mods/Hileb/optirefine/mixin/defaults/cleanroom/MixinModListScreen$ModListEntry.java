package mods.Hileb.optirefine.mixin.defaults.cleanroom;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Cleanroom ModList 3D item icons are drawn over GuiSlot.drawSelectionBox's opaque
 * black selection fill. Pad an opaque row-colored rect behind the icon so
 * selected/unselected look identical.
 */
@Pseudo
@Mixin(targets = "com.cleanroommc.client.modlist.screen.ModListScreen$ModListEntry", remap = false)
public abstract class MixinModListScreen$ModListEntry {

    @WrapMethod(method = "drawIcon")
    private void optiRefine$safeDrawIcon(int top, int left, Operation<Void> original) {
        Gui.drawRect(left + 4, top + 2, left + 20, top + 18, 0xFF14141A);
        original.call(top, left);
    }
}
