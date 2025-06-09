package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.gui.GuiSlot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot{
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public int top;
    @Shadow
    public int bottom;
    @Shadow
    public int right;
    @Shadow
    public int left;
    @Shadow
    @Final
    public int slotHeight;

    @Shadow
    protected abstract void drawSlot(int i1, int i2, int i3, int i4, int i5, int i6, float v);

    @SuppressWarnings("all")
    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;drawSlot(IIIIIIF)V"))
    public void injectDrawSelectionBox(GuiSlot instance, int i1, int i2, int i3, int i4, int i5, int i6, float v){
        if (!_is_GuiResourcePackList() || i3 >= this.top - this.slotHeight && i3 <= this.bottom) {
            this.drawSlot(i1, i2, i3, i4, i5, i6, v);
        }
    }

    @Unique
    @AccessibleOperation(opcode = Opcodes.INSTANCEOF, desc = "net.minecraft.client.gui.GuiResourcePackList")
    public boolean _is_GuiResourcePackList(){
        throw new AbstractMethodError();
    }



}
