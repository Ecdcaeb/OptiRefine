package mods.Hileb.optirefine.mixin.defaults.cleanroom;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Cleanroom ModList 3D item icons are drawn over GuiSlot.drawSelectionBox's opaque
 * black selection fill (0,0,0,255). The 3D icon (e.g. command block) has transparent
 * corners, so the selected row shows pure black behind the icon. Pad an opaque
 * row-colored rect behind the icon so selected/unselected look identical, and
 * force fixed-function state around the call so shaders cannot interfere.
 */
@Mixin(targets = "com.cleanroommc.client.modlist.screen.ModListScreen$ModListEntry")
public abstract class MixinModListScreen$ModListEntry {

    @WrapMethod(method = "drawIcon")
    private void optiRefine$safeDrawIcon(int top, int left, Operation<Void> original) {
        boolean shaders;
        try {
            shaders = Shaders.shaderPackLoaded;
        } catch (Throwable t) {
            shaders = false;
        }
        if (shaders) {
            GL20.glUseProgram(0);
        }
        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        try {
            // Pad an opaque row-colored rect behind the icon: the selection box's
            // black fill (0,0,0,255) otherwise shows through the 3D icon's corners.
            Gui.drawRect(left + 4, top + 2, left + 20, top + 18, 0xFF14141A);
            original.call(top, left);
        } catch (Throwable t) {
            // Keep the list alive; Cleanroom already has its own catch around the item call.
        } finally {
            GlStateManager.enableTexture2D();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (shaders) {
                GL20.glUseProgram(0);
            }
        }
    }
}
