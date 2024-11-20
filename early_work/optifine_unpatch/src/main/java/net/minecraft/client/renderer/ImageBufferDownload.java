/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.Color
 *  java.awt.Graphics
 *  java.awt.Image
 *  java.awt.image.BufferedImage
 *  java.awt.image.DataBufferInt
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.IImageBuffer
 */
package net.minecraft.client.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.IImageBuffer;

public class ImageBufferDownload
implements IImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    @Nullable
    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        \u2603 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = \u2603.getGraphics();
        graphics.drawImage((Image)bufferedImage, 0, 0, null);
        boolean bl = \u2603 = bufferedImage.getHeight() == 32;
        if (\u2603) {
            graphics.setColor(new Color(0, 0, 0, 0));
            graphics.fillRect(0, 32, 64, 32);
            graphics.drawImage((Image)\u2603, 24, 48, 20, 52, 4, 16, 8, 20, null);
            graphics.drawImage((Image)\u2603, 28, 48, 24, 52, 8, 16, 12, 20, null);
            graphics.drawImage((Image)\u2603, 20, 52, 16, 64, 8, 20, 12, 32, null);
            graphics.drawImage((Image)\u2603, 24, 52, 20, 64, 4, 20, 8, 32, null);
            graphics.drawImage((Image)\u2603, 28, 52, 24, 64, 0, 20, 4, 32, null);
            graphics.drawImage((Image)\u2603, 32, 52, 28, 64, 12, 20, 16, 32, null);
            graphics.drawImage((Image)\u2603, 40, 48, 36, 52, 44, 16, 48, 20, null);
            graphics.drawImage((Image)\u2603, 44, 48, 40, 52, 48, 16, 52, 20, null);
            graphics.drawImage((Image)\u2603, 36, 52, 32, 64, 48, 20, 52, 32, null);
            graphics.drawImage((Image)\u2603, 40, 52, 36, 64, 44, 20, 48, 32, null);
            graphics.drawImage((Image)\u2603, 44, 52, 40, 64, 40, 20, 44, 32, null);
            graphics.drawImage((Image)\u2603, 48, 52, 44, 64, 52, 20, 56, 32, null);
        }
        graphics.dispose();
        this.imageData = ((DataBufferInt)\u2603.getRaster().getDataBuffer()).getData();
        this.setAreaOpaque(0, 0, 32, 16);
        if (\u2603) {
            this.setAreaTransparent(32, 0, 64, 32);
        }
        this.setAreaOpaque(0, 16, 64, 32);
        this.setAreaOpaque(16, 48, 48, 64);
        return \u2603;
    }

    public void skinAvailable() {
    }

    private void setAreaTransparent(int n, int n2, int n3, int n4) {
        for (\u2603 = n; \u2603 < n3; ++\u2603) {
            for (\u2603 = n2; \u2603 < n4; ++\u2603) {
                \u2603 = this.imageData[\u2603 + \u2603 * this.imageWidth];
                if ((\u2603 >> 24 & 0xFF) >= 128) continue;
                return;
            }
        }
        for (\u2603 = n; \u2603 < n3; ++\u2603) {
            for (\u2603 = n2; \u2603 < n4; ++\u2603) {
                int n5 = \u2603 + \u2603 * this.imageWidth;
                this.imageData[n5] = this.imageData[n5] & 0xFFFFFF;
            }
        }
    }

    private void setAreaOpaque(int n, int n2, int n3, int n4) {
        for (\u2603 = n; \u2603 < n3; ++\u2603) {
            for (\u2603 = n2; \u2603 < n4; ++\u2603) {
                int n5 = \u2603 + \u2603 * this.imageWidth;
                this.imageData[n5] = this.imageData[n5] | 0xFF000000;
            }
        }
    }
}
