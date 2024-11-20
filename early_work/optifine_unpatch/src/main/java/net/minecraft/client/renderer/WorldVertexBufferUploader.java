/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.nio.ByteBuffer
 *  java.util.List
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class WorldVertexBufferUploader {
    public void draw(BufferBuilder bufferBuilderIn) {
        if (bufferBuilderIn.getVertexCount() > 0) {
            VertexFormat vertexformat = bufferBuilderIn.getVertexFormat();
            int i = vertexformat.getSize();
            ByteBuffer bytebuffer = bufferBuilderIn.getByteBuffer();
            List list = vertexformat.getElements();
            for (int j = 0; j < list.size(); ++j) {
                VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.getOffset(j));
                vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
            }
            GlStateManager.glDrawArrays((int)bufferBuilderIn.getDrawMode(), (int)0, (int)bufferBuilderIn.getVertexCount());
            int j1 = list.size();
            for (int i1 = 0; i1 < j1; ++i1) {
                VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                int k1 = vertexformatelement1.getIndex();
                vertexformatelement1.getUsage().postDraw(vertexformat, i1, i, bytebuffer);
            }
        }
        bufferBuilderIn.reset();
    }
}
