package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.vertex;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.optifine.render.VboRange;
import net.optifine.render.VboRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.ByteBuffer;

@Mixin(VertexBuffer.class)
public abstract class MixinVertexBuffer {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private VboRegion vboRegion;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private VboRange vboRange;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int drawMode;

    @WrapMethod(method = "bufferData")
    public void wrapbufferData(ByteBuffer p_181722_1_, Operation<Void> original){
        if (this.vboRegion != null) {
            this.vboRegion.bufferData(p_181722_1_, this.vboRange);
        } else {
            original.call(p_181722_1_);
        }
    }

    @WrapMethod(method = "drawArrays")
    public void wrapdrawArrays(int mode, Operation<Void> original){
        if (this.drawMode > 0) {
            mode = this.drawMode;
        }

        if (this.vboRegion != null) {
            this.vboRegion.drawArrays(mode, this.vboRange);
        } else {
            original.call(mode);
        }
    }

    @Shadow
    public abstract void deleteGlBuffers();

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setVboRegion(VboRegion vboRegion) {
        if (vboRegion != null) {
            this.deleteGlBuffers();
            this.vboRegion = vboRegion;
            this.vboRange = new VboRange();
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public VboRegion getVboRegion() {
        return this.vboRegion;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public VboRange getVboRange() {
        return this.vboRange;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public int getDrawMode() {
        return this.drawMode;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

}
/*
--- net/minecraft/client/renderer/vertex/VertexBuffer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/vertex/VertexBuffer.java	Tue Aug 19 14:59:58 2025
@@ -1,42 +1,83 @@
 package net.minecraft.client.renderer.vertex;

 import java.nio.ByteBuffer;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
+import net.optifine.render.VboRange;
+import net.optifine.render.VboRegion;

 public class VertexBuffer {
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;
+   private VboRegion vboRegion;
+   private VboRange vboRange;
+   private int drawMode;

    public VertexBuffer(VertexFormat var1) {
       this.vertexFormat = var1;
       this.glBufferId = OpenGlHelper.glGenBuffers();
    }

    public void bindBuffer() {
       OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.glBufferId);
    }

    public void bufferData(ByteBuffer var1) {
-      this.bindBuffer();
-      OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, var1, 35044);
-      this.unbindBuffer();
-      this.count = var1.limit() / this.vertexFormat.getSize();
+      if (this.vboRegion != null) {
+         this.vboRegion.bufferData(var1, this.vboRange);
+      } else {
+         this.bindBuffer();
+         OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, var1, 35044);
+         this.unbindBuffer();
+         this.count = var1.limit() / this.vertexFormat.getSize();
+      }
    }

    public void drawArrays(int var1) {
-      GlStateManager.glDrawArrays(var1, 0, this.count);
+      if (this.drawMode > 0) {
+         var1 = this.drawMode;
+      }
+
+      if (this.vboRegion != null) {
+         this.vboRegion.drawArrays(var1, this.vboRange);
+      } else {
+         GlStateManager.glDrawArrays(var1, 0, this.count);
+      }
    }

    public void unbindBuffer() {
       OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
    }

    public void deleteGlBuffers() {
       if (this.glBufferId >= 0) {
          OpenGlHelper.glDeleteBuffers(this.glBufferId);
          this.glBufferId = -1;
       }
+   }
+
+   public void setVboRegion(VboRegion var1) {
+      if (var1 != null) {
+         this.deleteGlBuffers();
+         this.vboRegion = var1;
+         this.vboRange = new VboRange();
+      }
+   }
+
+   public VboRegion getVboRegion() {
+      return this.vboRegion;
+   }
+
+   public VboRange getVboRange() {
+      return this.vboRange;
+   }
+
+   public int getDrawMode() {
+      return this.drawMode;
+   }
+
+   public void setDrawMode(int var1) {
+      this.drawMode = var1;
    }
 }
 */
