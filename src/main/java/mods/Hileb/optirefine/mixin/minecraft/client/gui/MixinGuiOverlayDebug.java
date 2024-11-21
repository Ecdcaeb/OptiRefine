package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.renderer.texture.TextureMap;
import net.optifine.SmartAnimations;
import net.optifine.TextureAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug extends Gui {
    @Unique
    @SuppressWarnings("all")
    private String debugOF = null;

    @Unique
    @SuppressWarnings("all")
    private List<String> debugInfoLeft = null;

    @Unique
    @SuppressWarnings("all")
    private List<String> debugInfoRight = null;

    @Unique
    @SuppressWarnings("all")
    private long updateInfoLeftTimeMs = 0L;

    @Unique
    @SuppressWarnings("all")
    private long updateInfoRightTimeMs = 0L;

    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimationsActive ()I")
    private static int _acc_TextureMap_getCountAnimationsActive_(TextureMap instance){
        throw new AbstractMethodError();
    }

    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimations ()I")
    private static int _acc_TextureMap_getCountAnimations_(TextureMap instance){
        throw new AbstractMethodError();
    }

    @Inject(method = "call", at = @At("RETURN"), cancellable = true)
    public void injectCall(CallbackInfoReturnable<List<String>> cir){
        if (Minecraft.getMinecraft().debug.equals(this.debugOF)) {
            StringBuilder sb = new StringBuilder(Minecraft.getMinecraft().debug);
            int fpsMin = Config.getFpsMin();
            int posFps = Minecraft.getMinecraft().debug.indexOf(" fps ");
            if (posFps >= 0) {
                sb.insert(posFps, "/" + fpsMin);
            }

            if (Config.isSmoothFps()) {
                sb.append(" sf");
            }

            if (Config.isFastRender()) {
                sb.append(" fr");
            }

            if (Config.isAnisotropicFiltering()) {
                sb.append(" af");
            }

            if (Config.isAntialiasing()) {
                sb.append(" aa");
            }

            if (Config.isRenderRegions()) {
                sb.append(" reg");
            }

            if (Config.isShaders()) {
                sb.append(" sh");
            }

            Minecraft.getMinecraft().debug = sb.toString();
            this.debugOF = Minecraft.getMinecraft().debug;
        }

        StringBuilder sbx = new StringBuilder();
        TextureMap tm = Config.getTextureMap();
        sbx.append(", A: ");
        if (SmartAnimations.isActive()) {
            sbx.append(_acc_TextureMap_getCountAnimationsActive_(tm) + TextureAnimations.getCountAnimationsActive());
            sbx.append("/");
        }

        sbx.append(_acc_TextureMap_getCountAnimations_(tm) + TextureAnimations.getCountAnimations());
        String ofInfo = sbx.toString();

        cir.setReturnValue(cir.getReturnValue().stream().map((s) -> s.startsWith("P: ") ? s + ofInfo : s).toList());
    }

}
