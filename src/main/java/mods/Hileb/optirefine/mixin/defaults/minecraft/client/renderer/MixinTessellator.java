package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.optifine.SmartAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(Tessellator.class)
public abstract class MixinTessellator {

    @Shadow @Final
    private BufferBuilder buffer;

    @Inject(method = "draw", at = @At("HEAD"))
    public void ijdraw(CallbackInfo ci) {
        if (BufferBuilder_animatedSprites_get(this.buffer) != null) {
            SmartAnimations.spritesRendered(BufferBuilder_animatedSprites_get(this.buffer));
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava.util.BitSet;")
    private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder buffer);
}
/*
--- net/minecraft/client/renderer/Tessellator.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/Tessellator.java	Tue Aug 19 14:59:58 2025
@@ -1,8 +1,10 @@
 package net.minecraft.client.renderer;

+import net.optifine.SmartAnimations;
+
 public class Tessellator {
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private static final Tessellator INSTANCE = new Tessellator(2097152);

    public static Tessellator getInstance() {
@@ -11,12 +13,16 @@

    public Tessellator(int var1) {
       this.buffer = new BufferBuilder(var1);
    }

    public void draw() {
+      if (this.buffer.animatedSprites != null) {
+         SmartAnimations.spritesRendered(this.buffer.animatedSprites);
+      }
+
       this.buffer.finishDrawing();
       this.vboUploader.draw(this.buffer);
    }

    public BufferBuilder getBuffer() {
       return this.buffer;
 */
