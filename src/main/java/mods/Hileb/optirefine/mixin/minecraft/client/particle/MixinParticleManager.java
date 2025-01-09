package mods.Hileb.optirefine.mixin.minecraft.client.particle;

import com.google.common.collect.Queues;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {
    @Shadow
    @Final
    private Queue<Particle> queue;

    /**
     * @author Hileb
     * @reason TODO
     */
    @Overwrite
    public void addEffect(Particle effect) {
        if (effect != null) {
            if (!(effect instanceof ParticleFirework.Spark) || Config.isFireworkParticles()) {
                this.queue.add(effect);
            }
        }
    }
}
