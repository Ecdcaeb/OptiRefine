package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class SPacketEncryptionRequest implements Packet<INetHandlerLoginClient> {
   private String hashedServerId;
   private PublicKey publicKey;
   private byte[] verifyToken;

   public SPacketEncryptionRequest() {
   }

   public SPacketEncryptionRequest(String var1, PublicKey var2, byte[] var3) {
      this.hashedServerId = ☃;
      this.publicKey = ☃;
      this.verifyToken = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.hashedServerId = ☃.readString(20);
      this.publicKey = CryptManager.decodePublicKey(☃.readByteArray());
      this.verifyToken = ☃.readByteArray();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.hashedServerId);
      ☃.writeByteArray(this.publicKey.getEncoded());
      ☃.writeByteArray(this.verifyToken);
   }

   public void processPacket(INetHandlerLoginClient var1) {
      ☃.handleEncryptionRequest(this);
   }

   public String getServerId() {
      return this.hashedServerId;
   }

   public PublicKey getPublicKey() {
      return this.publicKey;
   }

   public byte[] getVerifyToken() {
      return this.verifyToken;
   }
}
