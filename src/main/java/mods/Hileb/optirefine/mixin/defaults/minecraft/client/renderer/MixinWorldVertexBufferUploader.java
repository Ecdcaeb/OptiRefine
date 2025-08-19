package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.optifine.shaders.SVertexBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldVertexBufferUploader.class)
public abstract class MixinWorldVertexBufferUploader {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;getVertexFormat()Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public void beforeDraw(BufferBuilder vertexBufferIn, CallbackInfo ci){
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
        }
    }

    @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glDrawArrays(III)V"))
    public void openShader(int mode, int first, int count, Operation<Void> original, @Local(argsOnly = true) BufferBuilder vertexBufferIn){
        if (BufferBuilder_isMultiTexture(vertexBufferIn)) {
            BufferBuilder_drawMultiTexture(vertexBufferIn);
        } else if (Config.isShaders()) {
            SVertexBuilder.drawArrays(vertexBufferIn.getDrawMode(), 0, vertexBufferIn.getVertexCount(), vertexBufferIn);
        } else {
            GlStateManager.glDrawArrays(vertexBufferIn.getDrawMode(), 0, vertexBufferIn.getVertexCount());
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder quadsToTriangles ()V")
    private static native void BufferBuilder_quadsToTriangles(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder isMultiTexture ()Z")
    private static native boolean BufferBuilder_isMultiTexture(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder drawMultiTexture ()V")
    private static native void BufferBuilder_drawMultiTexture(BufferBuilder builder) ;
}
/*

--- net/minecraft/client/renderer/WorldVertexBufferUploader.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/WorldVertexBufferUploader.java	Tue Aug 19 14:59:58 2025
@@ -2,70 +2,94 @@

 import java.nio.Buffer;
 import java.nio.ByteBuffer;
 import java.util.List;
 import net.minecraft.client.renderer.vertex.VertexFormat;
 import net.minecraft.client.renderer.vertex.VertexFormatElement;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.SVertexBuilder;

 public class WorldVertexBufferUploader {
    public void draw(BufferBuilder var1) {
       if (var1.getVertexCount() > 0) {
+         if (var1.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
+            var1.quadsToTriangles();
+         }
+
          VertexFormat var2 = var1.getVertexFormat();
          int var3 = var2.getSize();
          ByteBuffer var4 = var1.getByteBuffer();
          List var5 = var2.getElements();
+         boolean var6 = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
+         boolean var7 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();

-         for (int var6 = 0; var6 < var5.size(); var6++) {
-            VertexFormatElement var7 = (VertexFormatElement)var5.get(var6);
-            VertexFormatElement.EnumUsage var8 = var7.getUsage();
-            int var9 = var7.getType().getGlConstant();
-            int var10 = var7.getIndex();
-            ((Buffer)var4).position(var2.getOffset(var6));
-            switch (var8) {
-               case POSITION:
-                  GlStateManager.glVertexPointer(var7.getElementCount(), var9, var3, var4);
-                  GlStateManager.glEnableClientState(32884);
-                  break;
-               case UV:
-                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var10);
-                  GlStateManager.glTexCoordPointer(var7.getElementCount(), var9, var3, var4);
-                  GlStateManager.glEnableClientState(32888);
-                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
-                  break;
-               case COLOR:
-                  GlStateManager.glColorPointer(var7.getElementCount(), var9, var3, var4);
-                  GlStateManager.glEnableClientState(32886);
-                  break;
-               case NORMAL:
-                  GlStateManager.glNormalPointer(var9, var3, var4);
-                  GlStateManager.glEnableClientState(32885);
+         for (int var8 = 0; var8 < var5.size(); var8++) {
+            VertexFormatElement var9 = (VertexFormatElement)var5.get(var8);
+            EnumUsage var10 = var9.getUsage();
+            if (var6) {
+               Reflector.callVoid(var10, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, new Object[]{var2, var8, var3, var4});
+            } else {
+               int var11 = var9.getType().getGlConstant();
+               int var12 = var9.getIndex();
+               ((Buffer)var4).position(var2.getOffset(var8));
+               switch (var10) {
+                  case POSITION:
+                     GlStateManager.glVertexPointer(var9.getElementCount(), var11, var3, var4);
+                     GlStateManager.glEnableClientState(32884);
+                     break;
+                  case UV:
+                     OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var12);
+                     GlStateManager.glTexCoordPointer(var9.getElementCount(), var11, var3, var4);
+                     GlStateManager.glEnableClientState(32888);
+                     OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
+                     break;
+                  case COLOR:
+                     GlStateManager.glColorPointer(var9.getElementCount(), var11, var3, var4);
+                     GlStateManager.glEnableClientState(32886);
+                     break;
+                  case NORMAL:
+                     GlStateManager.glNormalPointer(var11, var3, var4);
+                     GlStateManager.glEnableClientState(32885);
+               }
             }
          }

-         GlStateManager.glDrawArrays(var1.getDrawMode(), 0, var1.getVertexCount());
-         int var11 = 0;
+         if (var1.isMultiTexture()) {
+            var1.drawMultiTexture();
+         } else if (Config.isShaders()) {
+            SVertexBuilder.drawArrays(var1.getDrawMode(), 0, var1.getVertexCount(), var1);
+         } else {
+            GlStateManager.glDrawArrays(var1.getDrawMode(), 0, var1.getVertexCount());
+         }
+
+         int var14 = 0;

-         for (int var12 = var5.size(); var11 < var12; var11++) {
-            VertexFormatElement var13 = (VertexFormatElement)var5.get(var11);
-            VertexFormatElement.EnumUsage var14 = var13.getUsage();
-            int var15 = var13.getIndex();
-            switch (var14) {
-               case POSITION:
-                  GlStateManager.glDisableClientState(32884);
-                  break;
-               case UV:
-                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var15);
-                  GlStateManager.glDisableClientState(32888);
-                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
-                  break;
-               case COLOR:
-                  GlStateManager.glDisableClientState(32886);
-                  GlStateManager.resetColor();
-                  break;
-               case NORMAL:
-                  GlStateManager.glDisableClientState(32885);
+         for (int var15 = var5.size(); var14 < var15; var14++) {
+            VertexFormatElement var16 = (VertexFormatElement)var5.get(var14);
+            EnumUsage var17 = var16.getUsage();
+            if (var7) {
+               Reflector.callVoid(var17, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, new Object[]{var2, var14, var3, var4});
+            } else {
+               int var13 = var16.getIndex();
+               switch (var17) {
+                  case POSITION:
+                     GlStateManager.glDisableClientState(32884);
+                     break;
+                  case UV:
+                     OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var13);
+                     GlStateManager.glDisableClientState(32888);
+                     OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
+                     break;
+                  case COLOR:
+                     GlStateManager.glDisableClientState(32886);
+                     GlStateManager.resetColor();
+                     break;
+                  case NORMAL:
+                     GlStateManager.glDisableClientState(32885);
+               }
             }
          }
       }

       var1.reset();
    }
 */
