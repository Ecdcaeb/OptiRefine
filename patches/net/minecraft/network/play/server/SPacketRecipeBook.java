package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketRecipeBook implements Packet<INetHandlerPlayClient> {
   private SPacketRecipeBook.State state;
   private List<IRecipe> recipes;
   private List<IRecipe> displayedRecipes;
   private boolean guiOpen;
   private boolean filteringCraftable;

   public SPacketRecipeBook() {
   }

   public SPacketRecipeBook(SPacketRecipeBook.State var1, List<IRecipe> var2, List<IRecipe> var3, boolean var4, boolean var5) {
      this.state = ☃;
      this.recipes = ☃;
      this.displayedRecipes = ☃;
      this.guiOpen = ☃;
      this.filteringCraftable = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleRecipeBook(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.state = ☃.readEnumValue(SPacketRecipeBook.State.class);
      this.guiOpen = ☃.readBoolean();
      this.filteringCraftable = ☃.readBoolean();
      int ☃ = ☃.readVarInt();
      this.recipes = Lists.newArrayList();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.recipes.add(CraftingManager.getRecipeById(☃.readVarInt()));
      }

      if (this.state == SPacketRecipeBook.State.INIT) {
         ☃ = ☃.readVarInt();
         this.displayedRecipes = Lists.newArrayList();

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            this.displayedRecipes.add(CraftingManager.getRecipeById(☃.readVarInt()));
         }
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.state);
      ☃.writeBoolean(this.guiOpen);
      ☃.writeBoolean(this.filteringCraftable);
      ☃.writeVarInt(this.recipes.size());

      for (IRecipe ☃ : this.recipes) {
         ☃.writeVarInt(CraftingManager.getIDForRecipe(☃));
      }

      if (this.state == SPacketRecipeBook.State.INIT) {
         ☃.writeVarInt(this.displayedRecipes.size());

         for (IRecipe ☃ : this.displayedRecipes) {
            ☃.writeVarInt(CraftingManager.getIDForRecipe(☃));
         }
      }
   }

   public List<IRecipe> getRecipes() {
      return this.recipes;
   }

   public List<IRecipe> getDisplayedRecipes() {
      return this.displayedRecipes;
   }

   public boolean isGuiOpen() {
      return this.guiOpen;
   }

   public boolean isFilteringCraftable() {
      return this.filteringCraftable;
   }

   public SPacketRecipeBook.State getState() {
      return this.state;
   }

   public static enum State {
      INIT,
      ADD,
      REMOVE;
   }
}
