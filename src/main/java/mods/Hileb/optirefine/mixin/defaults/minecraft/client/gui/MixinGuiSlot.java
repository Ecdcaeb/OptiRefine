package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;


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
@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot{
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member width exists in target class
    @Shadow
    public int width;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member height exists in target class
    @Shadow
    public int height;
// [AUDIT-OK] baseline member top exists in target class
    @Shadow
    public int top;
// [AUDIT-OK] baseline member bottom exists in target class
    @Shadow
    public int bottom;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member right exists in target class
    @Shadow
    public int right;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member left exists in target class
    @Shadow
    public int left;
// [AUDIT-OK] baseline member slotHeight exists in target class
    @Shadow
    @Final
    public int slotHeight;

// [AUDIT-OK] baseline method drawSlot(IIIIIIF)V declared abstract in GuiSlot
    @Shadow
    protected abstract void drawSlot(int i1, int i2, int i3, int i4, int i5, int i6, float v);

    
// [AUDIT-OK] target drawSelectionBox(IIIIF)V matches baseline; drawSlot invoke matches; handler params match
// [AUDIT-OK] culling guard replicates OF GuiSlot (instanceof GuiResourcePackList skip)
    @WrapOperation(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;drawSlot(IIIIIIF)V"))
    public void injectDrawSelectionBox(GuiSlot instance, int i1, int i2, int i3, int i4, int i5, int i6, float v, Operation<Void> original){
        if (!_is_GuiResourcePackList() || i3 >= this.top - this.slotHeight && i3 <= this.bottom) {
            original.call(instance, i1, i2, i3, i4, i5, i6, v);
        }
    }

// [AUDIT-OK] INSTANCEOF helper, no member lookup needed
    @Unique
    @AccessibleOperation(opcode = Opcodes.INSTANCEOF, desc = "net.minecraft.client.gui.GuiResourcePackList")
    public boolean _is_GuiResourcePackList(){
        throw new AbstractMethodError();
    }
}
