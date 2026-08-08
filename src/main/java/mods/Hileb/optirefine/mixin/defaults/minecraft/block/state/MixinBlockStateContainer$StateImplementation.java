package mods.Hileb.optirefine.mixin.defaults.minecraft.block.state;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.model.BlockModelUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(targets = "net.minecraft.block.state.BlockStateContainer$StateImplementation")
@SuppressWarnings("unused")
public abstract class MixinBlockStateContainer$StateImplementation {
// [AUDIT] 2026-08-09 - three-way audit: OF StateImplementation.getBoundingBox offsets the AABB for blocks with a non-NONE offset type (except BlockFlower)

    @Shadow @Final
// [AUDIT-OK] baseline member block exists in target class (StateImplementation private final Block block)
    private Block block;

    @WrapMethod(method = "getBoundingBox(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;")
// [AUDIT-FIXED] OF:411-419 getBoundingBox: if block.getOffsetType() != NONE && !(block instanceof BlockFlower) -> BlockModelUtils.getOffsetBoundingBox(block.getBoundingBox(this, worldIn, pos), offsetType, pos); else vanilla AABB (original)
    private AxisAlignedBB optiRefine$getBoundingBox(IBlockAccess worldIn, BlockPos pos, Operation<AxisAlignedBB> original) {
        Block.EnumOffsetType offsetType = this.block.getOffsetType();
        if (offsetType != Block.EnumOffsetType.NONE && !(this.block instanceof BlockFlower)) {
            AxisAlignedBB aabb = original.call(worldIn, pos);
            return BlockModelUtils.getOffsetBoundingBox(aabb, offsetType, pos);
        }
        return original.call(worldIn, pos);
    }
}
