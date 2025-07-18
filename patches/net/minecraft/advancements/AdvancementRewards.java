package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards {
   public static final AdvancementRewards EMPTY = new AdvancementRewards(
      0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY
   );
   private final int experience;
   private final ResourceLocation[] loot;
   private final ResourceLocation[] recipes;
   private final FunctionObject.CacheableFunction function;

   public AdvancementRewards(int var1, ResourceLocation[] var2, ResourceLocation[] var3, FunctionObject.CacheableFunction var4) {
      this.experience = ☃;
      this.loot = ☃;
      this.recipes = ☃;
      this.function = ☃;
   }

   public void apply(final EntityPlayerMP var1) {
      ☃.addExperience(this.experience);
      LootContext ☃ = new LootContext.Builder(☃.getServerWorld()).withLootedEntity(☃).build();
      boolean ☃x = false;

      for (ResourceLocation ☃xx : this.loot) {
         for (ItemStack ☃xxx : ☃.world.getLootTableManager().getLootTableFromLocation(☃xx).generateLootForPools(☃.getRNG(), ☃)) {
            if (☃.addItemStackToInventory(☃xxx)) {
               ☃.world
                  .playSound(
                     null,
                     ☃.posX,
                     ☃.posY,
                     ☃.posZ,
                     SoundEvents.ENTITY_ITEM_PICKUP,
                     SoundCategory.PLAYERS,
                     0.2F,
                     ((☃.getRNG().nextFloat() - ☃.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F
                  );
               ☃x = true;
            } else {
               EntityItem ☃xxxx = ☃.dropItem(☃xxx, false);
               if (☃xxxx != null) {
                  ☃xxxx.setNoPickupDelay();
                  ☃xxxx.setOwner(☃.getName());
               }
            }
         }
      }

      if (☃x) {
         ☃.inventoryContainer.detectAndSendChanges();
      }

      if (this.recipes.length > 0) {
         ☃.unlockRecipes(this.recipes);
      }

      final MinecraftServer ☃xx = ☃.server;
      FunctionObject ☃xxxx = this.function.get(☃xx.getFunctionManager());
      if (☃xxxx != null) {
         ICommandSender ☃xxxxx = new ICommandSender() {
            @Override
            public String getName() {
               return ☃.getName();
            }

            @Override
            public ITextComponent getDisplayName() {
               return ☃.getDisplayName();
            }

            @Override
            public void sendMessage(ITextComponent var1x) {
            }

            @Override
            public boolean canUseCommand(int var1x, String var2) {
               return ☃ <= 2;
            }

            @Override
            public BlockPos getPosition() {
               return ☃.getPosition();
            }

            @Override
            public Vec3d getPositionVector() {
               return ☃.getPositionVector();
            }

            @Override
            public World getEntityWorld() {
               return ☃.world;
            }

            @Override
            public Entity getCommandSenderEntity() {
               return ☃;
            }

            @Override
            public boolean sendCommandFeedback() {
               return ☃.worlds[0].getGameRules().getBoolean("commandBlockOutput");
            }

            @Override
            public void setCommandStat(CommandResultStats.Type var1x, int var2) {
               ☃.setCommandStat(☃, ☃);
            }

            @Override
            public MinecraftServer getServer() {
               return ☃.getServer();
            }
         };
         ☃xx.getFunctionManager().execute(☃xxxx, ☃xxxxx);
      }
   }

   @Override
   public String toString() {
      return "AdvancementRewards{experience="
         + this.experience
         + ", loot="
         + Arrays.toString((Object[])this.loot)
         + ", recipes="
         + Arrays.toString((Object[])this.recipes)
         + ", function="
         + this.function
         + '}';
   }

   public static class Deserializer implements JsonDeserializer<AdvancementRewards> {
      public AdvancementRewards deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "rewards");
         int ☃x = JsonUtils.getInt(☃, "experience", 0);
         JsonArray ☃xx = JsonUtils.getJsonArray(☃, "loot", new JsonArray());
         ResourceLocation[] ☃xxx = new ResourceLocation[☃xx.size()];

         for (int ☃xxxx = 0; ☃xxxx < ☃xxx.length; ☃xxxx++) {
            ☃xxx[☃xxxx] = new ResourceLocation(JsonUtils.getString(☃xx.get(☃xxxx), "loot[" + ☃xxxx + "]"));
         }

         JsonArray ☃xxxx = JsonUtils.getJsonArray(☃, "recipes", new JsonArray());
         ResourceLocation[] ☃xxxxx = new ResourceLocation[☃xxxx.size()];

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx.length; ☃xxxxxx++) {
            ☃xxxxx[☃xxxxxx] = new ResourceLocation(JsonUtils.getString(☃xxxx.get(☃xxxxxx), "recipes[" + ☃xxxxxx + "]"));
            IRecipe ☃xxxxxxx = CraftingManager.getRecipe(☃xxxxx[☃xxxxxx]);
            if (☃xxxxxxx == null) {
               throw new JsonSyntaxException("Unknown recipe '" + ☃xxxxx[☃xxxxxx] + "'");
            }
         }

         FunctionObject.CacheableFunction ☃xxxxxxx;
         if (☃.has("function")) {
            ☃xxxxxxx = new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(☃, "function")));
         } else {
            ☃xxxxxxx = FunctionObject.CacheableFunction.EMPTY;
         }

         return new AdvancementRewards(☃x, ☃xxx, ☃xxxxx, ☃xxxxxxx);
      }
   }
}
