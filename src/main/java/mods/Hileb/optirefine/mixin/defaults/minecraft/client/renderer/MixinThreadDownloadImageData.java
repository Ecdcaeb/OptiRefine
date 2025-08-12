package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.optifine.http.HttpPipeline;
import net.optifine.http.HttpRequest;
import net.optifine.http.HttpResponse;
import net.optifine.player.CapeImageBuffer;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.Proxy;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData extends SimpleTexture{
    @Unique
    public Boolean imageFound = null;
    @Unique
    public boolean pipeline = false;

    @Shadow
    private BufferedImage bufferedImage;

    public MixinThreadDownloadImageData(ResourceLocation p_i1275_1) {
        super(p_i1275_1);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEINTERFACE, desc = "net.minecraft.client.renderer.texture.ITextureObject getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    private native static MultiTexID getMultiTexID(ITextureObject iTextureObject);

    @WrapOperation(method = "checkTextureUploaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int blockTextureUploadForConfig(int textureId, BufferedImage texture, Operation<Integer> original){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, Config.getResourceManager(), this.textureLocation, getMultiTexID((ThreadDownloadImageData)(Object)this));
        } else {
            return TextureUtil.uploadTextureImage(textureId, texture);
        }
    }

    @Inject(method = "setBufferedImage", at = @At("TAIL"))
    public void afterImageSetted(BufferedImage bufferedImageIn, CallbackInfo ci){
        this.imageFound = this.bufferedImage != null;
    }

    @WrapOperation(method = "loadTextureFromServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;setDaemon(Z)V"))
    public void setPipelineForLoadTextureFromServer(Thread value, boolean on, Operation<Void> original){
        if (this.optiRefine$shouldPipeline()) {
            original.call(this.imageThread = new Thread(this::optiRefine$loadPipelined, value.getName()), on);
        } else {
            original.call(value, on);
        }
    }


    @Unique
    private boolean optiRefine$shouldPipeline() {
        if (!this.pipeline) {
            return false;
        } else {
            var proxy = Minecraft.getMinecraft().getProxy();
            return (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) && this.imageUrl.startsWith("http://");
        }
    }
    @Shadow @Final
    private String imageUrl;

    @Unique
    private void optiRefine$loadPipelined() {
        try {
            HttpRequest req = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
            HttpResponse resp = HttpPipeline.executeRequest(req);
            if (resp.getStatus() / 100 == 2) {
                byte[] body = resp.getBody();
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                BufferedImage var2;
                if (this.cacheFile != null) {
                    FileUtils.copyInputStreamToFile(bais, this.cacheFile);
                    var2 = ImageIO.read(this.cacheFile);
                } else {
                    var2 = TextureUtil.readBufferedImage(bais);
                }

                if (this.imageBuffer != null) {
                    var2 = this.imageBuffer.parseUserSkin(var2);
                }

                this.setBufferedImage(var2);
            }
        } catch (Exception var9) {
            LOGGER.error("Couldn't download http texture: " + var9.getClass().getName() + ": " + var9.getMessage());
        } finally {
            this.optiRefine$loadingFinished();
        }
    }

    @Shadow @Final
    private File cacheFile;

    @Shadow @Final
    private static Logger LOGGER;

    @Shadow @Final
    private IImageBuffer imageBuffer;

    @Shadow
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    @Shadow @Nullable private Thread imageThread;

    @Unique
    private void optiRefine$loadingFinished() {
        this.imageFound = this.bufferedImage != null;
        if (this.imageBuffer instanceof CapeImageBuffer) {
            CapeImageBuffer cib = (CapeImageBuffer)this.imageBuffer;
            cib.cleanup();
        }
    }

    @Unique
    public IImageBuffer getImageBuffer() {
        return this.imageBuffer;
    }
}
