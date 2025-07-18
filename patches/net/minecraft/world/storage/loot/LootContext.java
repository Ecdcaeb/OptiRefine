package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class LootContext {
   private final float luck;
   private final WorldServer world;
   private final LootTableManager lootTableManager;
   @Nullable
   private final Entity lootedEntity;
   @Nullable
   private final EntityPlayer player;
   @Nullable
   private final DamageSource damageSource;
   private final Set<LootTable> lootTables = Sets.newLinkedHashSet();

   public LootContext(float var1, WorldServer var2, LootTableManager var3, @Nullable Entity var4, @Nullable EntityPlayer var5, @Nullable DamageSource var6) {
      this.luck = ☃;
      this.world = ☃;
      this.lootTableManager = ☃;
      this.lootedEntity = ☃;
      this.player = ☃;
      this.damageSource = ☃;
   }

   @Nullable
   public Entity getLootedEntity() {
      return this.lootedEntity;
   }

   @Nullable
   public Entity getKillerPlayer() {
      return this.player;
   }

   @Nullable
   public Entity getKiller() {
      return this.damageSource == null ? null : this.damageSource.getTrueSource();
   }

   public boolean addLootTable(LootTable var1) {
      return this.lootTables.add(☃);
   }

   public void removeLootTable(LootTable var1) {
      this.lootTables.remove(☃);
   }

   public LootTableManager getLootTableManager() {
      return this.lootTableManager;
   }

   public float getLuck() {
      return this.luck;
   }

   @Nullable
   public Entity getEntity(LootContext.EntityTarget var1) {
      switch (☃) {
         case THIS:
            return this.getLootedEntity();
         case KILLER:
            return this.getKiller();
         case KILLER_PLAYER:
            return this.getKillerPlayer();
         default:
            return null;
      }
   }

   public static class Builder {
      private final WorldServer world;
      private float luck;
      private Entity lootedEntity;
      private EntityPlayer player;
      private DamageSource damageSource;

      public Builder(WorldServer var1) {
         this.world = ☃;
      }

      public LootContext.Builder withLuck(float var1) {
         this.luck = ☃;
         return this;
      }

      public LootContext.Builder withLootedEntity(Entity var1) {
         this.lootedEntity = ☃;
         return this;
      }

      public LootContext.Builder withPlayer(EntityPlayer var1) {
         this.player = ☃;
         return this;
      }

      public LootContext.Builder withDamageSource(DamageSource var1) {
         this.damageSource = ☃;
         return this;
      }

      public LootContext build() {
         return new LootContext(this.luck, this.world, this.world.getLootTableManager(), this.lootedEntity, this.player, this.damageSource);
      }
   }

   public static enum EntityTarget {
      THIS("this"),
      KILLER("killer"),
      KILLER_PLAYER("killer_player");

      private final String targetType;

      private EntityTarget(String var3) {
         this.targetType = ☃;
      }

      public static LootContext.EntityTarget fromString(String var0) {
         for (LootContext.EntityTarget ☃ : values()) {
            if (☃.targetType.equals(☃)) {
               return ☃;
            }
         }

         throw new IllegalArgumentException("Invalid entity target " + ☃);
      }

      public static class Serializer extends TypeAdapter<LootContext.EntityTarget> {
         public void write(JsonWriter var1, LootContext.EntityTarget var2) throws IOException {
            ☃.value(☃.targetType);
         }

         public LootContext.EntityTarget read(JsonReader var1) throws IOException {
            return LootContext.EntityTarget.fromString(☃.nextString());
         }
      }
   }
}
