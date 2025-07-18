package net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable {
   private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Random RANDOM = new Random();
   private final byte[] verifyToken = new byte[4];
   private final MinecraftServer server;
   public final NetworkManager networkManager;
   private NetHandlerLoginServer.LoginState currentLoginState = NetHandlerLoginServer.LoginState.HELLO;
   private int connectionTimer;
   private GameProfile loginGameProfile;
   private final String serverId = "";
   private SecretKey secretKey;
   private EntityPlayerMP player;

   public NetHandlerLoginServer(MinecraftServer var1, NetworkManager var2) {
      this.server = ☃;
      this.networkManager = ☃;
      RANDOM.nextBytes(this.verifyToken);
   }

   @Override
   public void update() {
      if (this.currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
         this.tryAcceptPlayer();
      } else if (this.currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT) {
         EntityPlayerMP ☃ = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());
         if (☃ == null) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
            this.server.getPlayerList().initializeConnectionToPlayer(this.networkManager, this.player);
            this.player = null;
         }
      }

      if (this.connectionTimer++ == 600) {
         this.disconnect(new TextComponentTranslation("multiplayer.disconnect.slow_login"));
      }
   }

   public void disconnect(ITextComponent var1) {
      try {
         LOGGER.info("Disconnecting {}: {}", this.getConnectionInfo(), ☃.getUnformattedText());
         this.networkManager.sendPacket(new SPacketDisconnect(☃));
         this.networkManager.closeChannel(☃);
      } catch (Exception var3) {
         LOGGER.error("Error whilst disconnecting player", var3);
      }
   }

   public void tryAcceptPlayer() {
      if (!this.loginGameProfile.isComplete()) {
         this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
      }

      String ☃ = this.server.getPlayerList().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
      if (☃ != null) {
         this.disconnect(new TextComponentTranslation(☃));
      } else {
         this.currentLoginState = NetHandlerLoginServer.LoginState.ACCEPTED;
         if (this.server.getNetworkCompressionThreshold() >= 0 && !this.networkManager.isLocalChannel()) {
            this.networkManager.sendPacket(new SPacketEnableCompression(this.server.getNetworkCompressionThreshold()), new ChannelFutureListener() {
               public void operationComplete(ChannelFuture var1) throws Exception {
                  NetHandlerLoginServer.this.networkManager.setCompressionThreshold(NetHandlerLoginServer.this.server.getNetworkCompressionThreshold());
               }
            });
         }

         this.networkManager.sendPacket(new SPacketLoginSuccess(this.loginGameProfile));
         EntityPlayerMP ☃x = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());
         if (☃x != null) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
            this.player = this.server.getPlayerList().createPlayerForUser(this.loginGameProfile);
         } else {
            this.server
               .getPlayerList()
               .initializeConnectionToPlayer(this.networkManager, this.server.getPlayerList().createPlayerForUser(this.loginGameProfile));
         }
      }
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
      LOGGER.info("{} lost connection: {}", this.getConnectionInfo(), ☃.getUnformattedText());
   }

   public String getConnectionInfo() {
      return this.loginGameProfile != null
         ? this.loginGameProfile + " (" + this.networkManager.getRemoteAddress() + ")"
         : String.valueOf(this.networkManager.getRemoteAddress());
   }

   @Override
   public void processLoginStart(CPacketLoginStart var1) {
      Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet", new Object[0]);
      this.loginGameProfile = ☃.getProfile();
      if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
         this.currentLoginState = NetHandlerLoginServer.LoginState.KEY;
         this.networkManager.sendPacket(new SPacketEncryptionRequest("", this.server.getKeyPair().getPublic(), this.verifyToken));
      } else {
         this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
      }
   }

   @Override
   public void processEncryptionResponse(CPacketEncryptionResponse var1) {
      Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet", new Object[0]);
      PrivateKey ☃ = this.server.getKeyPair().getPrivate();
      if (!Arrays.equals(this.verifyToken, ☃.getVerifyToken(☃))) {
         throw new IllegalStateException("Invalid nonce!");
      } else {
         this.secretKey = ☃.getSecretKey(☃);
         this.currentLoginState = NetHandlerLoginServer.LoginState.AUTHENTICATING;
         this.networkManager.enableEncryption(this.secretKey);
         (new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
               @Override
               public void run() {
                  GameProfile ☃x = NetHandlerLoginServer.this.loginGameProfile;

                  try {
                     String ☃x = new BigInteger(
                           CryptManager.getServerIdHash("", NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)
                        )
                        .toString(16);
                     NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server
                        .getMinecraftSessionService()
                        .hasJoinedServer(new GameProfile(null, ☃x.getName()), ☃x, this.getAddress());
                     if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        NetHandlerLoginServer.LOGGER
                           .info(
                              "UUID of player {} is {}",
                              NetHandlerLoginServer.this.loginGameProfile.getName(),
                              NetHandlerLoginServer.this.loginGameProfile.getId()
                           );
                        NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                     } else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.LOGGER.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(☃x);
                        NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                     } else {
                        NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.unverified_username"));
                        NetHandlerLoginServer.LOGGER.error("Username '{}' tried to join with an invalid session", ☃x.getName());
                     }
                  } catch (AuthenticationUnavailableException var3) {
                     if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(☃x);
                        NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                     } else {
                        NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.authservers_down"));
                        NetHandlerLoginServer.LOGGER.error("Couldn't verify username because servers are unavailable");
                     }
                  }
               }

               @Nullable
               private InetAddress getAddress() {
                  SocketAddress ☃x = NetHandlerLoginServer.this.networkManager.getRemoteAddress();
                  return NetHandlerLoginServer.this.server.getPreventProxyConnections() && ☃x instanceof InetSocketAddress
                     ? ((InetSocketAddress)☃x).getAddress()
                     : null;
               }
            })
            .start();
      }
   }

   protected GameProfile getOfflineProfile(GameProfile var1) {
      UUID ☃ = UUID.nameUUIDFromBytes(("OfflinePlayer:" + ☃.getName()).getBytes(StandardCharsets.UTF_8));
      return new GameProfile(☃, ☃.getName());
   }

   static enum LoginState {
      HELLO,
      KEY,
      AUTHENTICATING,
      READY_TO_ACCEPT,
      DELAY_ACCEPT,
      ACCEPTED;
   }
}
