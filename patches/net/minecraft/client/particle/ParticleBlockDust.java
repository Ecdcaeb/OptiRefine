package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class ParticleBlockDust extends ParticleDigging {
   protected ParticleBlockDust(World var1, double var2, double var4, double var6, double var8, double var10, double var12, IBlockState var14) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
   }

   public static class Factory implements IParticleFactory {
      @Nullable
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         IBlockState ☃ = Block.getStateById(☃[0]);
         return ☃.getRenderType() == EnumBlockRenderType.INVISIBLE ? null : new ParticleBlockDust(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).init();
      }
   }
}
