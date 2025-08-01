package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderXPOrb.class)
public class MixinRenderXPOrb {
    //float u = ((float)entity.xpColor + partialTicks) / 2.0F;
    @Expression("@(? / 2.0)")
    @ModifyExpressionValue(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    public float customXPColor(float original, @Share(namespace = "optirefine", value = "color")LocalFloatRef color){
        if (Config.isCustomColors()) {
            color.set(CustomColors.getXpOrbTimer(original));
            return color.get();
        } else {
            color.set(original);
            return original;
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    public void hookBeforeDraw(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci,
                               @Share(namespace = "optirefine", value = "r") LocalIntRef r,
                               @Share(namespace = "optirefine", value = "g") LocalIntRef g,
                               @Share(namespace = "optirefine", value = "b") LocalIntRef b,
                               @Share(namespace = "optirefine", value = "color")LocalFloatRef color
                               ){
        if (Config.isCustomColors()) {
            int col = CustomColors.getXpOrbColor(color.get());
            if (col >= 0) {
                r.set(col >> 16 & 0xFF);
                g.set(col >> 8 & 0xFF);
                b.set(col >> 0 & 0xFF);
            }
        } else {
            r.set(-1);
            g.set(-1);
            b.set(-1);
        }
    }

    @Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;"))
    public BufferBuilder setColor(BufferBuilder instance, int red, int green, int blue, int alpha,
                                  @Share(namespace = "optirefine", value = "r") LocalIntRef r,
                                  @Share(namespace = "optirefine", value = "g") LocalIntRef g,
                                  @Share(namespace = "optirefine", value = "b") LocalIntRef b,
    ){
        return instance.color(r.get() < 0 ? red : r.get(), g.get() < 0 ? green : g.get(), b.get() < 0 ? blue : b.get(), alpha);
    }


}
