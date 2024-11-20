/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  java.lang.Deprecated
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemOverride
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ItemOverrideList {
    public static final ItemOverrideList NONE = new ItemOverrideList();
    private final List<ItemOverride> overrides = Lists.newArrayList();

    private ItemOverrideList() {
    }

    public ItemOverrideList(List<ItemOverride> overridesIn) {
        for (int i = overridesIn.size() - 1; i >= 0; --i) {
            this.overrides.add((Object)((ItemOverride)overridesIn.get(i)));
        }
    }

    @Nullable
    @Deprecated
    public ResourceLocation applyOverride(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        if (!this.overrides.isEmpty()) {
            for (ItemOverride itemoverride : this.overrides) {
                if (!itemoverride.matchesItemStack(stack, worldIn, entityIn)) continue;
                return itemoverride.getLocation();
            }
        }
        return null;
    }

    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
        ResourceLocation location;
        if (!stack.isEmpty() && stack.getItem().hasCustomProperties() && (location = this.applyOverride(stack, world, entity)) != null) {
            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(ModelLoader.getInventoryVariant((String)location.toString()));
        }
        return originalModel;
    }

    public ImmutableList<ItemOverride> getOverrides() {
        return ImmutableList.copyOf(this.overrides);
    }
}
