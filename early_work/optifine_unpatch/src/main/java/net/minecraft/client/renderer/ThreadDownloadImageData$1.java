/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.image.BufferedImage
 *  java.io.File
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.net.HttpURLConnection
 *  java.net.URL
 *  javax.imageio.ImageIO
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  org.apache.commons.io.FileUtils
 */
package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.commons.io.FileUtils;

/*
 * Exception performing whole class analysis ignored.
 */
class ThreadDownloadImageData.1
extends Thread {
    ThreadDownloadImageData.1(String string) {
        super(string);
    }

    public void run() {
        HttpURLConnection httpURLConnection = null;
        ThreadDownloadImageData.access$200().debug("Downloading http texture from {} to {}", (Object)ThreadDownloadImageData.access$000((ThreadDownloadImageData)ThreadDownloadImageData.this), (Object)ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this));
        try {
            BufferedImage \u26032;
            httpURLConnection = (HttpURLConnection)new URL(ThreadDownloadImageData.access$000((ThreadDownloadImageData)ThreadDownloadImageData.this)).openConnection(Minecraft.getMinecraft().getProxy());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() / 100 != 2) {
                return;
            }
            if (ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this) != null) {
                FileUtils.copyInputStreamToFile((InputStream)httpURLConnection.getInputStream(), (File)ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this));
                \u26032 = ImageIO.read((File)ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this));
            } else {
                \u26032 = TextureUtil.readBufferedImage((InputStream)httpURLConnection.getInputStream());
            }
            if (ThreadDownloadImageData.access$300((ThreadDownloadImageData)ThreadDownloadImageData.this) != null) {
                \u26032 = ThreadDownloadImageData.access$300((ThreadDownloadImageData)ThreadDownloadImageData.this).parseUserSkin(\u26032);
            }
            ThreadDownloadImageData.this.setBufferedImage(\u26032);
        }
        catch (Exception exception) {
            ThreadDownloadImageData.access$200().error("Couldn't download http texture", (Throwable)exception);
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
