/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  java.nio.ByteBuffer
 *  java.util.List
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.WorldVertexBufferUploader$1
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.SVertexBuilder
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.SVertexBuilder;

public class WorldVertexBufferUploader {
    public void draw(BufferBuilder vertexBufferIn) {
        if (vertexBufferIn.getVertexCount() > 0) {
            if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
                vertexBufferIn.quadsToTriangles();
            }
            VertexFormat vertexformat = vertexBufferIn.getVertexFormat();
            int i = vertexformat.getSize();
            ByteBuffer bytebuffer = vertexBufferIn.getByteBuffer();
            List list = vertexformat.getElements();
            boolean forgePreDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            boolean forgePostDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            block12: for (int j = 0; j < list.size(); ++j) {
                VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                if (forgePreDrawExists) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage, (ReflectorMethod)Reflector.ForgeVertexFormatElementEnumUseage_preDraw, (Object[])new Object[]{vertexformat, j, i, bytebuffer});
                    continue;
                }
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.getOffset(j));
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage.ordinal()]) {
                    case 1: {
                        GlStateManager.glVertexPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GlStateManager.glEnableClientState((int)32884);
                        continue block12;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture((int)(OpenGlHelper.defaultTexUnit + l));
                        GlStateManager.glTexCoordPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GlStateManager.glEnableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
                        continue block12;
                    }
                    case 3: {
                        GlStateManager.glColorPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GlStateManager.glEnableClientState((int)32886);
                        continue block12;
                    }
                    case 4: {
                        GlStateManager.glNormalPointer((int)k, (int)i, (ByteBuffer)bytebuffer);
                        GlStateManager.glEnableClientState((int)32885);
                    }
                }
            }
            BufferBuilder wr = vertexBufferIn;
            if (wr.isMultiTexture()) {
                wr.drawMultiTexture();
            } else if (Config.isShaders()) {
                SVertexBuilder.drawArrays((int)vertexBufferIn.getDrawMode(), (int)0, (int)vertexBufferIn.getVertexCount(), (BufferBuilder)vertexBufferIn);
            } else {
                GlStateManager.glDrawArrays((int)vertexBufferIn.getDrawMode(), (int)0, (int)vertexBufferIn.getVertexCount());
            }
            int j1 = list.size();
            block13: for (int i1 = 0; i1 < j1; ++i1) {
                VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                if (forgePostDrawExists) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage1, (ReflectorMethod)Reflector.ForgeVertexFormatElementEnumUseage_postDraw, (Object[])new Object[]{vertexformat, i1, i, bytebuffer});
                    continue;
                }
                int k1 = vertexformatelement1.getIndex();
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage1.ordinal()]) {
                    case 1: {
                        GlStateManager.glDisableClientState((int)32884);
                        continue block13;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture((int)(OpenGlHelper.defaultTexUnit + k1));
                        GlStateManager.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
                        continue block13;
                    }
                    case 3: {
                        GlStateManager.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                        continue block13;
                    }
                    case 4: {
                        GlStateManager.glDisableClientState((int)32885);
                    }
                }
            }
        }
        vertexBufferIn.reset();
    }
}
