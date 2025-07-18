package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class CPacketEncryptionResponse implements Packet<INetHandlerLoginServer> {
   private byte[] secretKeyEncrypted = new byte[0];
   private byte[] verifyTokenEncrypted = new byte[0];

   public CPacketEncryptionResponse() {
   }

   public CPacketEncryptionResponse(SecretKey var1, PublicKey var2, byte[] var3) {
      this.secretKeyEncrypted = CryptManager.encryptData(☃, ☃.getEncoded());
      this.verifyTokenEncrypted = CryptManager.encryptData(☃, ☃);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.secretKeyEncrypted = ☃.readByteArray();
      this.verifyTokenEncrypted = ☃.readByteArray();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByteArray(this.secretKeyEncrypted);
      ☃.writeByteArray(this.verifyTokenEncrypted);
   }

   public void processPacket(INetHandlerLoginServer var1) {
      ☃.processEncryptionResponse(this);
   }

   public SecretKey getSecretKey(PrivateKey var1) {
      return CryptManager.decryptSharedKey(☃, this.secretKeyEncrypted);
   }

   public byte[] getVerifyToken(PrivateKey var1) {
      return ☃ == null ? this.verifyTokenEncrypted : CryptManager.decryptData(☃, this.verifyTokenEncrypted);
   }
}
