package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlaceGhostRecipe implements Packet<INetHandlerPlayClient> {
   private int field_194314_a;
   private IRecipe field_194315_b;

   public SPacketPlaceGhostRecipe() {
   }

   public SPacketPlaceGhostRecipe(int var1, IRecipe var2) {
      this.field_194314_a = ☃;
      this.field_194315_b = ☃;
   }

   public IRecipe func_194311_a() {
      return this.field_194315_b;
   }

   public int func_194313_b() {
      return this.field_194314_a;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.field_194314_a = ☃.readByte();
      this.field_194315_b = CraftingManager.getRecipeById(☃.readVarInt());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.field_194314_a);
      ☃.writeVarInt(CraftingManager.getIDForRecipe(this.field_194315_b));
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.func_194307_a(this);
   }
}
