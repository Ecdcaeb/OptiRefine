package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.optifine.IRandomEntity;
import net.optifine.RandomEntity;
import net.optifine.RandomEntityRule;
import net.optifine.RandomTileEntity;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.config.Matches;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Mixin(value = RandomEntityRule.class, remap = false)
public abstract class MixinG5RandomEntityRule {
    @Shadow private EnumDyeColor[] collarColors;

    @Unique private EnumDyeColor[] optiRefine$colors;
    @Unique private RangeListInt optiRefine$sizes;
    @Unique private MatchBlock[] optiRefine$blocks;
    @Unique private NbtTagValue[] optiRefine$nbt;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void optiRefine$parseSelectors(Properties properties, String path, ResourceLocation base, int index,
                                           String textures, ConnectedParser parser, CallbackInfo ci) {
        String colors = properties.getProperty("colors." + index);
        if (colors != null) {
            optiRefine$colors = parser.parseDyeColors(colors.toLowerCase(Locale.ROOT).replace("light_gray", "silver"),
                    "color", ConnectedParser.DYE_COLORS_INVALID);
            collarColors = null;
        }
        String sizes = properties.getProperty("sizes." + index);
        if (sizes != null) {
            optiRefine$sizes = parser.parseRangeListInt(sizes);
            if (optiRefine$sizes == null) optiRefine$sizes = new RangeListInt();
        }
        optiRefine$blocks = parser.parseMatchBlocks(properties.getProperty("blocks." + index));
        String prefix = "nbt." + index + ".";
        List<NbtTagValue> tags = null;
        for (String key : properties.stringPropertyNames()) {
            if (!key.startsWith(prefix) || key.length() == prefix.length()) continue;
            if (tags == null) tags = new ArrayList<>();
            tags.add(new NbtTagValue(key.substring(prefix.length()), properties.getProperty(key)));
        }
        if (tags != null) optiRefine$nbt = tags.toArray(new NbtTagValue[tags.size()]);
    }

    @Inject(method = "matches", at = @At("RETURN"), cancellable = true)
    private void optiRefine$matchSelectors(IRandomEntity source, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() || (optiRefine$colors == null && optiRefine$sizes == null
                && optiRefine$blocks == null && optiRefine$nbt == null)) return;
        Entity entity = source instanceof RandomEntity random ? random.getEntity() : null;
        TileEntity tile = source instanceof RandomTileEntity random ? random.getTileEntity() : null;
        if (optiRefine$colors != null && !optiRefine$matchesColor(optiRefine$getColor(entity, tile))) {
            cir.setReturnValue(false);
            return;
        }
        // The rule uses the zero-based size stored in a slime's NBT. Magma cubes inherit it.
        if (optiRefine$sizes != null && (!(entity instanceof EntitySlime slime)
                || !optiRefine$sizes.isInRange(slime.getSlimeSize() - 1))) {
            cir.setReturnValue(false);
            return;
        }
        if (optiRefine$blocks != null) {
            IBlockState state = optiRefine$getBlockState(entity, tile);
            if (state == null || !Matches.block(Block.getIdFromBlock(state.getBlock()),
                    state.getBlock().getMetaFromState(state), optiRefine$blocks)) {
                cir.setReturnValue(false);
                return;
            }
        }
        if (optiRefine$nbt != null) {
            if (entity == null && tile == null) {
                cir.setReturnValue(false);
                return;
            }
            NBTTagCompound tag = new NBTTagCompound();
            tag = entity != null ? entity.writeToNBT(tag) : tile.writeToNBT(tag);
            for (NbtTagValue match : optiRefine$nbt) {
                if (!match.matches(tag)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }

    @Unique
    private boolean optiRefine$matchesColor(EnumDyeColor color) {
        if (color == null) return false;
        for (EnumDyeColor candidate : optiRefine$colors) {
            if (color == candidate) return true;
        }
        return false;
    }

    @Unique
    private static EnumDyeColor optiRefine$getColor(Entity entity, TileEntity tile) {
        if (entity instanceof EntityWolf wolf && wolf.isTamed()) return wolf.getCollarColor();
        if (entity instanceof EntitySheep sheep) return sheep.getFleeceColor();
        if (entity instanceof EntityLlama llama) return llama.getColor();
        if (entity instanceof EntityShulker shulker) return shulker.getColor();
        if (tile instanceof TileEntityBed bed) return bed.getColor();
        if (tile instanceof TileEntityShulkerBox box) return box.getColor();
        return null;
    }

    @Unique
    private static IBlockState optiRefine$getBlockState(Entity entity, TileEntity tile) {
        if (tile != null && tile.getWorld() != null) return tile.getWorld().getBlockState(tile.getPos());
        if (entity == null || entity.world == null) return null;
        World world = entity.world;
        BlockPos pos = new BlockPos(entity.posX, entity.posY - 0.2D, entity.posZ);
        IBlockState state = world.getBlockState(pos);
        if (state.getMaterial() == Material.AIR) {
            IBlockState below = world.getBlockState(pos.down());
            Block block = below.getBlock();
            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                return below;
            }
        }
        return state;
    }
}
