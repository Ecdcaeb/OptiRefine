package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;


import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.ItemOverrideCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
@Mixin(ItemOverrideList.class)
public abstract class MixinItemOverrideList {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @Shadow @Final
    // [AUDIT-OK] baseline member overrides declared in ItemOverrideList (deobf:17); shadow initializer is ignored by mixin (nit)
    @Unique
    private List<ItemOverride> overrides = Lists.newArrayList();
    @Unique
    // [AUDIT-OK] OF-added field (OF ItemOverrideList:18), not in baseline
    private ItemOverrideCache itemOverrideCache;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    // [AUDIT-OK] target <init>(Ljava/util/List;)V baseline; cache creation matches OF:28-29
    public void init(List<ItemOverride> overrides, CallbackInfo ci){
        if (this.overrides.size() > 65) {
            this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
        }
    }

    @WrapMethod(method = "applyOverride")
    // [AUDIT-OK] target applyOverride(ItemStack,World,EntityLivingBase) baseline (deobf:32); cache logic mirrors OF:34-55
    public ResourceLocation applyCache(ItemStack stack, World worldIn, EntityLivingBase entityIn, Operation<ResourceLocation> original){
        if (!this.overrides.isEmpty()) {
            if (this.itemOverrideCache != null) {
                ResourceLocation locationCached = this.itemOverrideCache.getModelLocation(stack, worldIn, entityIn);
                if (locationCached != null) {
                    return locationCached == ItemOverrideCache.LOCATION_NULL ? null : locationCached;
                }
            }

            ResourceLocation resourceLocation = original.call(stack, worldIn, entityIn);
            if (resourceLocation != null) {
                if (this.itemOverrideCache != null) {
                    this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, resourceLocation);
                }

                return resourceLocation;
            }

            if (this.itemOverrideCache != null) {
                this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, ItemOverrideCache.LOCATION_NULL);
            }
        }

        return null;
    }


}
