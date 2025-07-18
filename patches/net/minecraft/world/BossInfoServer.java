package net.minecraft.world;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class BossInfoServer extends BossInfo {
   private final Set<EntityPlayerMP> players = Sets.newHashSet();
   private final Set<EntityPlayerMP> readOnlyPlayers = Collections.unmodifiableSet(this.players);
   private boolean visible = true;

   public BossInfoServer(ITextComponent var1, BossInfo.Color var2, BossInfo.Overlay var3) {
      super(MathHelper.getRandomUUID(), ☃, ☃, ☃);
   }

   @Override
   public void setPercent(float var1) {
      if (☃ != this.percent) {
         super.setPercent(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
      }
   }

   @Override
   public void setColor(BossInfo.Color var1) {
      if (☃ != this.color) {
         super.setColor(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
      }
   }

   @Override
   public void setOverlay(BossInfo.Overlay var1) {
      if (☃ != this.overlay) {
         super.setOverlay(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
      }
   }

   @Override
   public BossInfo setDarkenSky(boolean var1) {
      if (☃ != this.darkenSky) {
         super.setDarkenSky(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
      }

      return this;
   }

   @Override
   public BossInfo setPlayEndBossMusic(boolean var1) {
      if (☃ != this.playEndBossMusic) {
         super.setPlayEndBossMusic(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
      }

      return this;
   }

   @Override
   public BossInfo setCreateFog(boolean var1) {
      if (☃ != this.createFog) {
         super.setCreateFog(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
      }

      return this;
   }

   @Override
   public void setName(ITextComponent var1) {
      if (!Objects.equal(☃, this.name)) {
         super.setName(☃);
         this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
      }
   }

   private void sendUpdate(SPacketUpdateBossInfo.Operation var1) {
      if (this.visible) {
         SPacketUpdateBossInfo ☃ = new SPacketUpdateBossInfo(☃, this);

         for (EntityPlayerMP ☃x : this.players) {
            ☃x.connection.sendPacket(☃);
         }
      }
   }

   public void addPlayer(EntityPlayerMP var1) {
      if (this.players.add(☃) && this.visible) {
         ☃.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.ADD, this));
      }
   }

   public void removePlayer(EntityPlayerMP var1) {
      if (this.players.remove(☃) && this.visible) {
         ☃.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.REMOVE, this));
      }
   }

   public void setVisible(boolean var1) {
      if (☃ != this.visible) {
         this.visible = ☃;

         for (EntityPlayerMP ☃ : this.players) {
            ☃.connection.sendPacket(new SPacketUpdateBossInfo(☃ ? SPacketUpdateBossInfo.Operation.ADD : SPacketUpdateBossInfo.Operation.REMOVE, this));
         }
      }
   }

   public Collection<EntityPlayerMP> getPlayers() {
      return this.readOnlyPlayers;
   }
}
