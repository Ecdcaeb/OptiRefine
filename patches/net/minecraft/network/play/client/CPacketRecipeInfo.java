package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer> {
   private CPacketRecipeInfo.Purpose purpose;
   private IRecipe recipe;
   private boolean isGuiOpen;
   private boolean filteringCraftable;

   public CPacketRecipeInfo() {
   }

   public CPacketRecipeInfo(IRecipe var1) {
      this.purpose = CPacketRecipeInfo.Purpose.SHOWN;
      this.recipe = ☃;
   }

   public CPacketRecipeInfo(boolean var1, boolean var2) {
      this.purpose = CPacketRecipeInfo.Purpose.SETTINGS;
      this.isGuiOpen = ☃;
      this.filteringCraftable = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.purpose = ☃.readEnumValue(CPacketRecipeInfo.Purpose.class);
      if (this.purpose == CPacketRecipeInfo.Purpose.SHOWN) {
         this.recipe = CraftingManager.getRecipeById(☃.readInt());
      } else if (this.purpose == CPacketRecipeInfo.Purpose.SETTINGS) {
         this.isGuiOpen = ☃.readBoolean();
         this.filteringCraftable = ☃.readBoolean();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.purpose);
      if (this.purpose == CPacketRecipeInfo.Purpose.SHOWN) {
         ☃.writeInt(CraftingManager.getIDForRecipe(this.recipe));
      } else if (this.purpose == CPacketRecipeInfo.Purpose.SETTINGS) {
         ☃.writeBoolean(this.isGuiOpen);
         ☃.writeBoolean(this.filteringCraftable);
      }
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.handleRecipeBookUpdate(this);
   }

   public CPacketRecipeInfo.Purpose getPurpose() {
      return this.purpose;
   }

   public IRecipe getRecipe() {
      return this.recipe;
   }

   public boolean isGuiOpen() {
      return this.isGuiOpen;
   }

   public boolean isFilteringCraftable() {
      return this.filteringCraftable;
   }

   public static enum Purpose {
      SHOWN,
      SETTINGS;
   }
}
