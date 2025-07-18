package net.minecraft.network.datasync;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.ITextComponent;

public class DataSerializers {
   private static final IntIdentityHashBiMap<DataSerializer<?>> REGISTRY = new IntIdentityHashBiMap<>(16);
   public static final DataSerializer<Byte> BYTE = new DataSerializer<Byte>() {
      public void write(PacketBuffer var1, Byte var2) {
         ☃.writeByte(☃);
      }

      public Byte read(PacketBuffer var1) {
         return ☃.readByte();
      }

      @Override
      public DataParameter<Byte> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Byte copyValue(Byte var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Integer> VARINT = new DataSerializer<Integer>() {
      public void write(PacketBuffer var1, Integer var2) {
         ☃.writeVarInt(☃);
      }

      public Integer read(PacketBuffer var1) {
         return ☃.readVarInt();
      }

      @Override
      public DataParameter<Integer> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Integer copyValue(Integer var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Float> FLOAT = new DataSerializer<Float>() {
      public void write(PacketBuffer var1, Float var2) {
         ☃.writeFloat(☃);
      }

      public Float read(PacketBuffer var1) {
         return ☃.readFloat();
      }

      @Override
      public DataParameter<Float> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Float copyValue(Float var1) {
         return ☃;
      }
   };
   public static final DataSerializer<String> STRING = new DataSerializer<String>() {
      public void write(PacketBuffer var1, String var2) {
         ☃.writeString(☃);
      }

      public String read(PacketBuffer var1) {
         return ☃.readString(32767);
      }

      @Override
      public DataParameter<String> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public String copyValue(String var1) {
         return ☃;
      }
   };
   public static final DataSerializer<ITextComponent> TEXT_COMPONENT = new DataSerializer<ITextComponent>() {
      public void write(PacketBuffer var1, ITextComponent var2) {
         ☃.writeTextComponent(☃);
      }

      public ITextComponent read(PacketBuffer var1) {
         return ☃.readTextComponent();
      }

      @Override
      public DataParameter<ITextComponent> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public ITextComponent copyValue(ITextComponent var1) {
         return ☃.createCopy();
      }
   };
   public static final DataSerializer<ItemStack> ITEM_STACK = new DataSerializer<ItemStack>() {
      public void write(PacketBuffer var1, ItemStack var2) {
         ☃.writeItemStack(☃);
      }

      public ItemStack read(PacketBuffer var1) {
         return ☃.readItemStack();
      }

      @Override
      public DataParameter<ItemStack> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public ItemStack copyValue(ItemStack var1) {
         return ☃.copy();
      }
   };
   public static final DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE = new DataSerializer<Optional<IBlockState>>() {
      public void write(PacketBuffer var1, Optional<IBlockState> var2) {
         if (☃.isPresent()) {
            ☃.writeVarInt(Block.getStateId((IBlockState)☃.get()));
         } else {
            ☃.writeVarInt(0);
         }
      }

      public Optional<IBlockState> read(PacketBuffer var1) {
         int ☃ = ☃.readVarInt();
         return ☃ == 0 ? Optional.absent() : Optional.of(Block.getStateById(☃));
      }

      @Override
      public DataParameter<Optional<IBlockState>> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Optional<IBlockState> copyValue(Optional<IBlockState> var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Boolean> BOOLEAN = new DataSerializer<Boolean>() {
      public void write(PacketBuffer var1, Boolean var2) {
         ☃.writeBoolean(☃);
      }

      public Boolean read(PacketBuffer var1) {
         return ☃.readBoolean();
      }

      @Override
      public DataParameter<Boolean> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Boolean copyValue(Boolean var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Rotations> ROTATIONS = new DataSerializer<Rotations>() {
      public void write(PacketBuffer var1, Rotations var2) {
         ☃.writeFloat(☃.getX());
         ☃.writeFloat(☃.getY());
         ☃.writeFloat(☃.getZ());
      }

      public Rotations read(PacketBuffer var1) {
         return new Rotations(☃.readFloat(), ☃.readFloat(), ☃.readFloat());
      }

      @Override
      public DataParameter<Rotations> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Rotations copyValue(Rotations var1) {
         return ☃;
      }
   };
   public static final DataSerializer<BlockPos> BLOCK_POS = new DataSerializer<BlockPos>() {
      public void write(PacketBuffer var1, BlockPos var2) {
         ☃.writeBlockPos(☃);
      }

      public BlockPos read(PacketBuffer var1) {
         return ☃.readBlockPos();
      }

      @Override
      public DataParameter<BlockPos> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public BlockPos copyValue(BlockPos var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new DataSerializer<Optional<BlockPos>>() {
      public void write(PacketBuffer var1, Optional<BlockPos> var2) {
         ☃.writeBoolean(☃.isPresent());
         if (☃.isPresent()) {
            ☃.writeBlockPos((BlockPos)☃.get());
         }
      }

      public Optional<BlockPos> read(PacketBuffer var1) {
         return !☃.readBoolean() ? Optional.absent() : Optional.of(☃.readBlockPos());
      }

      @Override
      public DataParameter<Optional<BlockPos>> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Optional<BlockPos> copyValue(Optional<BlockPos> var1) {
         return ☃;
      }
   };
   public static final DataSerializer<EnumFacing> FACING = new DataSerializer<EnumFacing>() {
      public void write(PacketBuffer var1, EnumFacing var2) {
         ☃.writeEnumValue(☃);
      }

      public EnumFacing read(PacketBuffer var1) {
         return ☃.readEnumValue(EnumFacing.class);
      }

      @Override
      public DataParameter<EnumFacing> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public EnumFacing copyValue(EnumFacing var1) {
         return ☃;
      }
   };
   public static final DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID = new DataSerializer<Optional<UUID>>() {
      public void write(PacketBuffer var1, Optional<UUID> var2) {
         ☃.writeBoolean(☃.isPresent());
         if (☃.isPresent()) {
            ☃.writeUniqueId((UUID)☃.get());
         }
      }

      public Optional<UUID> read(PacketBuffer var1) {
         return !☃.readBoolean() ? Optional.absent() : Optional.of(☃.readUniqueId());
      }

      @Override
      public DataParameter<Optional<UUID>> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public Optional<UUID> copyValue(Optional<UUID> var1) {
         return ☃;
      }
   };
   public static final DataSerializer<NBTTagCompound> COMPOUND_TAG = new DataSerializer<NBTTagCompound>() {
      public void write(PacketBuffer var1, NBTTagCompound var2) {
         ☃.writeCompoundTag(☃);
      }

      public NBTTagCompound read(PacketBuffer var1) {
         return ☃.readCompoundTag();
      }

      @Override
      public DataParameter<NBTTagCompound> createKey(int var1) {
         return new DataParameter<>(☃, this);
      }

      public NBTTagCompound copyValue(NBTTagCompound var1) {
         return ☃.copy();
      }
   };

   public static void registerSerializer(DataSerializer<?> var0) {
      REGISTRY.add(☃);
   }

   @Nullable
   public static DataSerializer<?> getSerializer(int var0) {
      return REGISTRY.get(☃);
   }

   public static int getSerializerId(DataSerializer<?> var0) {
      return REGISTRY.getId(☃);
   }

   static {
      registerSerializer(BYTE);
      registerSerializer(VARINT);
      registerSerializer(FLOAT);
      registerSerializer(STRING);
      registerSerializer(TEXT_COMPONENT);
      registerSerializer(ITEM_STACK);
      registerSerializer(BOOLEAN);
      registerSerializer(ROTATIONS);
      registerSerializer(BLOCK_POS);
      registerSerializer(OPTIONAL_BLOCK_POS);
      registerSerializer(FACING);
      registerSerializer(OPTIONAL_UNIQUE_ID);
      registerSerializer(OPTIONAL_BLOCK_STATE);
      registerSerializer(COMPOUND_TAG);
   }
}
