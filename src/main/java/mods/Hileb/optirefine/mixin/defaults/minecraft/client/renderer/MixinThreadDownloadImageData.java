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
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field (OF: public Boolean imageFound = null), not in baseline
    @Unique
    public Boolean imageFound;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field (OF: public boolean pipeline = false), not in baseline
    @Unique
    public boolean pipeline;

    @Shadow
// [AUDIT-OK] baseline member bufferedImage (SRG field_110560_d)
    private BufferedImage bufferedImage;

    public MixinThreadDownloadImageData(ResourceLocation p_i1275_1) {
        super(p_i1275_1);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @AccessibleOperation(opcode = Opcodes.INVOKEINTERFACE, desc = "net.minecraft.client.renderer.texture.ITextureObject getMultiTexID ()Lnet.optifine.shaders.MultiTexID;", itf = true)
// [AUDIT-OK] OF member ITextureObject.getMultiTexID (MixinITextureObject/MixinAbstractTexture provide), not in baseline
    private native static MultiTexID getMultiTexID(ITextureObject iTextureObject);

    @WrapOperation(method = "checkTextureUploaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int blockTextureUploadForConfig(int textureId, BufferedImage texture, Operation<Integer> original){
// [AUDIT-OK] target checkTextureUploaded()V (SRG func_147640_e) TextureUtil.uploadTextureImage matches baseline; shaders branch matches OF
// [AUDIT-FIXED] 2026-08-05: textureUploaded=true set here, BEFORE any upload — OF:52-53 sets it as the
// first statement inside the guard (re-entry from ShadersTex.getMultiTexID -> loadTexture ->
// checkTextureUploaded while still false would recurse). The previous method-HEAD @Inject made the
// vanilla guard if(!textureUploaded) always false, so deleteGlTexture + this upload never ran and
// skins/capes never reached GL.
        this.textureUploaded = true;
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, Config.getResourceManager(), this.textureLocation, getMultiTexID((ThreadDownloadImageData)(Object)this));
        } else {
            return TextureUtil.uploadTextureImage(textureId, texture);
        }
    }

    @Inject(method = "setBufferedImage", at = @At("TAIL"))
    public void afterImageSetted(BufferedImage bufferedImageIn, CallbackInfo ci){
// [AUDIT-OK] target setBufferedImage(Ljava/awt/image/BufferedImage;)V (SRG func_147641_a) matches baseline
        this.imageFound = this.bufferedImage != null;
    }

    @WrapOperation(method = "loadTextureFromServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;setDaemon(Z)V"))
    public void setPipelineForLoadTextureFromServer(Thread value, boolean on, Operation<Void> original){
// [AUDIT-OK] target loadTextureFromServer()V (SRG func_152433_a) Thread.setDaemon matches baseline; reassigning this.imageThread lets vanilla start() launch the pipeline thread (matches OF shouldPipeline/loadPipelined)
        if (this.optiRefine$shouldPipeline()) {
            original.call(this.imageThread = new Thread(this::optiRefine$loadPipelined, value.getName()), on);
        } else {
            original.call(value, on);
        }
    }


    private boolean optiRefine$shouldPipeline() {
        if (!this.pipeline) {
            return false;
        } else {
            var proxy = Minecraft.getMinecraft().getProxy();
            return (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) && this.imageUrl.startsWith("http://");
        }
    }
    @Shadow @Final
// [AUDIT-OK] baseline member imageUrl (SRG field_110562_b)
    private String imageUrl;

    @Shadow
// [AUDIT-OK] baseline member textureUploaded (SRG field_110559_g)
    private boolean textureUploaded;

    @Unique
// [AUDIT-OK] OF-added method (OF: loadPipelined private), not in baseline
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
// [AUDIT-OK] baseline member cacheFile (SRG field_152434_e)
    private File cacheFile;

    @Shadow @Final
// [AUDIT-OK] baseline member LOGGER (SRG field_147644_c)
    private static Logger LOGGER;

    @Shadow @Final
// [AUDIT-OK] baseline member imageBuffer (SRG field_110563_c)
    private IImageBuffer imageBuffer;

    @Shadow
// [AUDIT-OK] baseline member setBufferedImage (SRG func_147641_a)
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

// [AUDIT-OK] baseline member imageThread (SRG field_110561_e)
    @Shadow @Nullable private Thread imageThread;

// [AUDIT-OK] OF-added method (OF: private loadingFinished), not in baseline
    private void optiRefine$loadingFinished() {
        this.imageFound = this.bufferedImage != null;
        if (this.imageBuffer instanceof CapeImageBuffer) {
            CapeImageBuffer cib = (CapeImageBuffer)this.imageBuffer;
            cib.cleanup();
        }
    }

    @Unique
// [AUDIT-OK] OF-added method (OF: public getImageBuffer), not in baseline
    public IImageBuffer getImageBuffer() {
        return this.imageBuffer;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.imageFound = null;
        this.pipeline = false;
    }
}
