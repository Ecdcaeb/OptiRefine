package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import org.objectweb.asm.Opcodes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(ParticleItemPickup.class)
public abstract class MixinParticleItemPickup {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

// [AUDIT-OK] baseline member item exists in target class
    @Shadow
    @Final
    private Entity item;

    @WrapMethod(method = "renderParticle")
    private void optiRefine$renderPickupWithShaderEntityContext(BufferBuilder buffer, Entity camera, float partialTicks,
                                          float rotationX, float rotationZ, float rotationYZ,
                                          float rotationXY, float rotationXZ, Operation<Void> original) {
        if (!Config.isShaders()) {
            original.call(buffer, camera, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            return;
        }
        Program oldProgram = Shaders.activeProgram;
        Entity oldEntity = optiRefine$getRenderedEntity(Config.getRenderGlobal());
        Shaders.nextEntity(this.item);
        try {
            original.call(buffer, camera, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        } finally {
            Shaders.setEntityId(oldEntity);
            Shaders.useProgram(oldProgram);
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native Entity optiRefine$getRenderedEntity(RenderGlobal renderGlobal);

}
