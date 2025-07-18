package net.minecraft.network.play.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient> {
   private SPacketPlayerListItem.Action action;
   private final List<SPacketPlayerListItem.AddPlayerData> players = Lists.newArrayList();

   public SPacketPlayerListItem() {
   }

   public SPacketPlayerListItem(SPacketPlayerListItem.Action var1, EntityPlayerMP... var2) {
      this.action = ☃;

      for (EntityPlayerMP ☃ : ☃) {
         this.players.add(new SPacketPlayerListItem.AddPlayerData(☃.getGameProfile(), ☃.ping, ☃.interactionManager.getGameType(), ☃.getTabListDisplayName()));
      }
   }

   public SPacketPlayerListItem(SPacketPlayerListItem.Action var1, Iterable<EntityPlayerMP> var2) {
      this.action = ☃;

      for (EntityPlayerMP ☃ : ☃) {
         this.players.add(new SPacketPlayerListItem.AddPlayerData(☃.getGameProfile(), ☃.ping, ☃.interactionManager.getGameType(), ☃.getTabListDisplayName()));
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = ☃.readEnumValue(SPacketPlayerListItem.Action.class);
      int ☃ = ☃.readVarInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         GameProfile ☃xx = null;
         int ☃xxx = 0;
         GameType ☃xxxx = null;
         ITextComponent ☃xxxxx = null;
         switch (this.action) {
            case ADD_PLAYER:
               ☃xx = new GameProfile(☃.readUniqueId(), ☃.readString(16));
               int ☃xxxxxx = ☃.readVarInt();
               int ☃xxxxxxx = 0;

               for (; ☃xxxxxxx < ☃xxxxxx; ☃xxxxxxx++) {
                  String ☃xxxxxxxx = ☃.readString(32767);
                  String ☃xxxxxxxxx = ☃.readString(32767);
                  if (☃.readBoolean()) {
                     ☃xx.getProperties().put(☃xxxxxxxx, new Property(☃xxxxxxxx, ☃xxxxxxxxx, ☃.readString(32767)));
                  } else {
                     ☃xx.getProperties().put(☃xxxxxxxx, new Property(☃xxxxxxxx, ☃xxxxxxxxx));
                  }
               }

               ☃xxxx = GameType.getByID(☃.readVarInt());
               ☃xxx = ☃.readVarInt();
               if (☃.readBoolean()) {
                  ☃xxxxx = ☃.readTextComponent();
               }
               break;
            case UPDATE_GAME_MODE:
               ☃xx = new GameProfile(☃.readUniqueId(), null);
               ☃xxxx = GameType.getByID(☃.readVarInt());
               break;
            case UPDATE_LATENCY:
               ☃xx = new GameProfile(☃.readUniqueId(), null);
               ☃xxx = ☃.readVarInt();
               break;
            case UPDATE_DISPLAY_NAME:
               ☃xx = new GameProfile(☃.readUniqueId(), null);
               if (☃.readBoolean()) {
                  ☃xxxxx = ☃.readTextComponent();
               }
               break;
            case REMOVE_PLAYER:
               ☃xx = new GameProfile(☃.readUniqueId(), null);
         }

         this.players.add(new SPacketPlayerListItem.AddPlayerData(☃xx, ☃xxx, ☃xxxx, ☃xxxxx));
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.action);
      ☃.writeVarInt(this.players.size());

      for (SPacketPlayerListItem.AddPlayerData ☃ : this.players) {
         switch (this.action) {
            case ADD_PLAYER:
               ☃.writeUniqueId(☃.getProfile().getId());
               ☃.writeString(☃.getProfile().getName());
               ☃.writeVarInt(☃.getProfile().getProperties().size());

               for (Property ☃x : ☃.getProfile().getProperties().values()) {
                  ☃.writeString(☃x.getName());
                  ☃.writeString(☃x.getValue());
                  if (☃x.hasSignature()) {
                     ☃.writeBoolean(true);
                     ☃.writeString(☃x.getSignature());
                  } else {
                     ☃.writeBoolean(false);
                  }
               }

               ☃.writeVarInt(☃.getGameMode().getID());
               ☃.writeVarInt(☃.getPing());
               if (☃.getDisplayName() == null) {
                  ☃.writeBoolean(false);
               } else {
                  ☃.writeBoolean(true);
                  ☃.writeTextComponent(☃.getDisplayName());
               }
               break;
            case UPDATE_GAME_MODE:
               ☃.writeUniqueId(☃.getProfile().getId());
               ☃.writeVarInt(☃.getGameMode().getID());
               break;
            case UPDATE_LATENCY:
               ☃.writeUniqueId(☃.getProfile().getId());
               ☃.writeVarInt(☃.getPing());
               break;
            case UPDATE_DISPLAY_NAME:
               ☃.writeUniqueId(☃.getProfile().getId());
               if (☃.getDisplayName() == null) {
                  ☃.writeBoolean(false);
               } else {
                  ☃.writeBoolean(true);
                  ☃.writeTextComponent(☃.getDisplayName());
               }
               break;
            case REMOVE_PLAYER:
               ☃.writeUniqueId(☃.getProfile().getId());
         }
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handlePlayerListItem(this);
   }

   public List<SPacketPlayerListItem.AddPlayerData> getEntries() {
      return this.players;
   }

   public SPacketPlayerListItem.Action getAction() {
      return this.action;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
   }

   public static enum Action {
      ADD_PLAYER,
      UPDATE_GAME_MODE,
      UPDATE_LATENCY,
      UPDATE_DISPLAY_NAME,
      REMOVE_PLAYER;
   }

   public class AddPlayerData {
      private final int ping;
      private final GameType gamemode;
      private final GameProfile profile;
      private final ITextComponent displayName;

      public AddPlayerData(GameProfile var2, int var3, GameType var4, @Nullable ITextComponent var5) {
         this.profile = ☃;
         this.ping = ☃;
         this.gamemode = ☃;
         this.displayName = ☃;
      }

      public GameProfile getProfile() {
         return this.profile;
      }

      public int getPing() {
         return this.ping;
      }

      public GameType getGameMode() {
         return this.gamemode;
      }

      @Nullable
      public ITextComponent getDisplayName() {
         return this.displayName;
      }

      @Override
      public String toString() {
         return MoreObjects.toStringHelper(this)
            .add("latency", this.ping)
            .add("gameMode", this.gamemode)
            .add("profile", this.profile)
            .add("displayName", this.displayName == null ? null : ITextComponent.Serializer.componentToJson(this.displayName))
            .toString();
      }
   }
}
