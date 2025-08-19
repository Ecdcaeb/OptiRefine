package mods.Hileb.optirefine.mixin.defaults.minecraft.block.state;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockStateBase.class)
public abstract class MixinBlockStateBase implements IBlockState {

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private int blockId = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private int blockStateId = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private int metadata = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private ResourceLocation blockLocation = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    
    @Public
    public int getBlockId() {
        if (this.blockId < 0) {
             this.blockId = Block.getIdFromBlock(this.getBlock());
         }
         return this.blockId;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    
    @Public
    public int getBlockStateId() {
         if (this.blockStateId < 0) {
             this.blockStateId = Block.getStateId(this);
         }
         return this.blockStateId;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    
    @Public
    public int getMetadata() {
         if (this.metadata < 0) {
             this.metadata = this.getBlock().getMetaFromState(this);
         }
         return this.metadata;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    
    @Public
    public ResourceLocation getBlockLocation() {
        if (this.blockLocation == null) {
            this.blockLocation = Block.REGISTRY.getNameForObject(this.getBlock());
        }
        return this.blockLocation;
    }
}
/*
+++ net/minecraft/block/state/BlockStateBase.java	Tue Aug 19 14:59:58 2025
@@ -1,17 +1,19 @@
 package net.minecraft.block.state;

 import com.google.common.base.Function;
 import com.google.common.base.Joiner;
+import com.google.common.collect.ImmutableTable;
 import com.google.common.collect.Iterables;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.Map.Entry;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
 import net.minecraft.block.properties.IProperty;
+import net.minecraft.util.ResourceLocation;

 public abstract class BlockStateBase implements IBlockState {
    private static final Joiner COMMA_JOINER = Joiner.on(',');
    private static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function<Entry<IProperty<?>, Comparable<?>>, String>() {
       @Nullable
       public String apply(@Nullable Entry<IProperty<?>, Comparable<?>> var1) {
@@ -21,15 +23,55 @@
             IProperty var2 = (IProperty)var1.getKey();
             return var2.getName() + "=" + this.getPropertyName(var2, (Comparable<?>)var1.getValue());
          }
       }

       private <T extends Comparable<T>> String getPropertyName(IProperty<T> var1, Comparable<?> var2) {
-         return var1.getName((T)var2);
+         return var1.getName(var2);
       }
    };
+   private int blockId = -1;
+   private int blockStateId = -1;
+   private int metadata = -1;
+   private ResourceLocation blockLocation = null;
+
+   public int getBlockId() {
+      if (this.blockId < 0) {
+         this.blockId = Block.getIdFromBlock(this.getBlock());
+      }
+
+      return this.blockId;
+   }
+
+   public int getBlockStateId() {
+      if (this.blockStateId < 0) {
+         this.blockStateId = Block.getStateId(this);
+      }
+
+      return this.blockStateId;
+   }
+
+   public int getMetadata() {
+      if (this.metadata < 0) {
+         this.metadata = this.getBlock().getMetaFromState(this);
+      }
+
+      return this.metadata;
+   }
+
+   public ResourceLocation getBlockLocation() {
+      if (this.blockLocation == null) {
+         this.blockLocation = (ResourceLocation)Block.REGISTRY.getNameForObject(this.getBlock());
+      }
+
+      return this.blockLocation;
+   }
+
+   public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
+      return null;
+   }

    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> var1) {
       return this.withProperty(var1, cyclePropertyValue(var1.getAllowedValues(), this.getValue(var1)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> var0, T var1) {
*/
