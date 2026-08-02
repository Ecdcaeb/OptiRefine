package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import mods.Hileb.optirefine.library.common.utils.Checked;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.gui.GuiSlot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Checked
@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot{
    @SuppressWarnings("unused")
    @Shadow
    public int width;
    @SuppressWarnings("unused")
    @Shadow
    public int height;
    @Shadow
    public int top;
    @Shadow
    public int bottom;
    @SuppressWarnings("unused")
    @Shadow
    public int right;
    @SuppressWarnings("unused")
    @Shadow
    public int left;
    @Shadow
    @Final
    public int slotHeight;

    @Shadow
    protected abstract void drawSlot(int i1, int i2, int i3, int i4, int i5, int i6, float v);

    
    @WrapOperation(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;drawSlot(IIIIIIF)V"))
    public void injectDrawSelectionBox(GuiSlot instance, int i1, int i2, int i3, int i4, int i5, int i6, float v, Operation<Void> original){
        if (!_is_GuiResourcePackList() || i3 >= this.top - this.slotHeight && i3 <= this.bottom) {
            original.call(instance, i1, i2, i3, i4, i5, i6, v);
        }
    }

    @Unique
    @AccessibleOperation(opcode = Opcodes.INSTANCEOF, desc = "net.minecraft.client.gui.GuiResourcePackList")
    public boolean _is_GuiResourcePackList(){
        throw new AbstractMethodError();
    }
}
