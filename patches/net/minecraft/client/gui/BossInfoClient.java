package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;

public class BossInfoClient extends BossInfo {
   protected float rawPercent;
   protected long percentSetTime;

   public BossInfoClient(SPacketUpdateBossInfo var1) {
      super(☃.getUniqueId(), ☃.getName(), ☃.getColor(), ☃.getOverlay());
      this.rawPercent = ☃.getPercent();
      this.percent = ☃.getPercent();
      this.percentSetTime = Minecraft.getSystemTime();
      this.setDarkenSky(☃.shouldDarkenSky());
      this.setPlayEndBossMusic(☃.shouldPlayEndBossMusic());
      this.setCreateFog(☃.shouldCreateFog());
   }

   @Override
   public void setPercent(float var1) {
      this.percent = this.getPercent();
      this.rawPercent = ☃;
      this.percentSetTime = Minecraft.getSystemTime();
   }

   @Override
   public float getPercent() {
      long ☃ = Minecraft.getSystemTime() - this.percentSetTime;
      float ☃x = MathHelper.clamp((float)☃ / 100.0F, 0.0F, 1.0F);
      return this.percent + (this.rawPercent - this.percent) * ☃x;
   }

   public void updateFromPacket(SPacketUpdateBossInfo var1) {
      switch (☃.getOperation()) {
         case UPDATE_NAME:
            this.setName(☃.getName());
            break;
         case UPDATE_PCT:
            this.setPercent(☃.getPercent());
            break;
         case UPDATE_STYLE:
            this.setColor(☃.getColor());
            this.setOverlay(☃.getOverlay());
            break;
         case UPDATE_PROPERTIES:
            this.setDarkenSky(☃.shouldDarkenSky());
            this.setPlayEndBossMusic(☃.shouldPlayEndBossMusic());
      }
   }
}
