package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.security.PublicKey;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.util.CryptManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginClient implements INetHandlerLoginClient {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   @Nullable
   private final GuiScreen previousGuiScreen;
   private final NetworkManager networkManager;
   private GameProfile gameProfile;

   public NetHandlerLoginClient(NetworkManager var1, Minecraft var2, @Nullable GuiScreen var3) {
      this.networkManager = ☃;
      this.mc = ☃;
      this.previousGuiScreen = ☃;
   }

   @Override
   public void handleEncryptionRequest(SPacketEncryptionRequest var1) {
      final SecretKey ☃ = CryptManager.createNewSharedKey();
      String ☃x = ☃.getServerId();
      PublicKey ☃xx = ☃.getPublicKey();
      String ☃xxx = new BigInteger(CryptManager.getServerIdHash(☃x, ☃xx, ☃)).toString(16);
      if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().isOnLAN()) {
         try {
            this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), ☃xxx);
         } catch (AuthenticationException var10) {
            LOGGER.warn("Couldn't connect to auth servers but will continue to join LAN");
         }
      } else {
         try {
            this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), ☃xxx);
         } catch (AuthenticationUnavailableException var7) {
            this.networkManager
               .closeChannel(
                  new TextComponentTranslation("disconnect.loginFailedInfo", new TextComponentTranslation("disconnect.loginFailedInfo.serversUnavailable"))
               );
            return;
         } catch (InvalidCredentialsException var8) {
            this.networkManager
               .closeChannel(
                  new TextComponentTranslation("disconnect.loginFailedInfo", new TextComponentTranslation("disconnect.loginFailedInfo.invalidSession"))
               );
            return;
         } catch (AuthenticationException var9) {
            this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo", var9.getMessage()));
            return;
         }
      }

      this.networkManager.sendPacket(new CPacketEncryptionResponse(☃, ☃xx, ☃.getVerifyToken()), new GenericFutureListener<Future<? super Void>>() {
         public void operationComplete(Future<? super Void> var1) throws Exception {
            NetHandlerLoginClient.this.networkManager.enableEncryption(☃);
         }
      });
   }

   private MinecraftSessionService getSessionService() {
      return this.mc.getSessionService();
   }

   @Override
   public void handleLoginSuccess(SPacketLoginSuccess var1) {
      this.gameProfile = ☃.getProfile();
      this.networkManager.setConnectionState(EnumConnectionState.PLAY);
      this.networkManager.setNetHandler(new NetHandlerPlayClient(this.mc, this.previousGuiScreen, this.networkManager, this.gameProfile));
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
      if (this.previousGuiScreen != null && this.previousGuiScreen instanceof GuiScreenRealmsProxy) {
         this.mc.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.previousGuiScreen).getProxy(), "connect.failed", ☃).getProxy());
      } else {
         this.mc.displayGuiScreen(new GuiDisconnected(this.previousGuiScreen, "connect.failed", ☃));
      }
   }

   @Override
   public void handleDisconnect(SPacketDisconnect var1) {
      this.networkManager.closeChannel(☃.getReason());
   }

   @Override
   public void handleEnableCompression(SPacketEnableCompression var1) {
      if (!this.networkManager.isLocalChannel()) {
         this.networkManager.setCompressionThreshold(☃.getCompressionThreshold());
      }
   }
}
