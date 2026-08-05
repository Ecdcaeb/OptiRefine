package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.optifine.DynamicLights;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * OptiFine additions to {@link ItemRenderer}.
 *
 * <p>Only the changes that are NOT already applied by the Cleanroom patches are re-implemented here:</p>
 * <ul>
 *     <li>{@code renderItemSide}: keep depth mask when shaders want it</li>
 *     <li>{@code setLightmap}: dynamic lights support</li>
 *     <li>{@code renderItemInFirstPerson(float)}: {@code Items.BOW == } -&gt; {@code instanceof ItemBow}</li>
 *     <li>{@code renderItemInFirstPerson(AbstractClientPlayer,...)}: skip hand rendering for shaders</li>
 *     <li>{@code renderWaterOverlayTexture}: shaders underwater overlay switch</li>
 *     <li>{@code renderFireInFirstPerson}: sprite tracking for shaders</li>
 *     <li>{@code updateEquippedItem}: notify shaders of equipped item changes</li>
 * </ul>
 *
 * <p>The following OptiFine changes are already present in Cleanroom's patches and thus skipped:
 * {@code ForgeHooksClient.renderSpecificFirstPersonHand} wrappers,
 * {@code instanceof ItemMap} check, {@code ForgeEventFactory} overlay events,
 * {@code ForgeHooksClient.shouldCauseReequipAnimation} logic.</p>
 */
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 2

    @Shadow
// [AUDIT-OK] baseline member mc (SRG field_78455_a)
    private net.minecraft.client.Minecraft mc;
    @Shadow
// [AUDIT-OK] baseline member renderManager (SRG field_178111_g)
    private net.minecraft.client.renderer.entity.RenderManager renderManager;
    @Shadow
// [AUDIT-OK] baseline member itemRenderer (SRG field_178112_h)
    private net.minecraft.client.renderer.RenderItem itemRenderer;
    @Shadow
// [AUDIT-OK] baseline member itemStackMainHand (SRG field_187467_d)
    private ItemStack itemStackMainHand;
    @Shadow
// [AUDIT-OK] baseline member itemStackOffHand (SRG field_187468_e)
    private ItemStack itemStackOffHand;
    @Shadow
// [AUDIT-OK] baseline member equippedProgressMainHand (SRG field_187469_f)
    private float equippedProgressMainHand;
    @Shadow
// [AUDIT-OK] baseline member prevEquippedProgressMainHand (SRG field_187470_g)
    private float prevEquippedProgressMainHand;
    @Shadow
// [AUDIT-OK] baseline member equippedProgressOffHand (SRG field_187471_h)
    private float equippedProgressOffHand;
    @Shadow
// [AUDIT-OK] baseline member prevEquippedProgressOffHand (SRG field_187472_i)
    private float prevEquippedProgressOffHand;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
// [AUDIT-OK] OF member BufferBuilder.setSprite
    private static native void BufferBuilder_setSprite(BufferBuilder bufferBuilder, TextureAtlasSprite sprite);

    /**
     * OptiFine: {@code if (flag && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask))}.
     */
    @Redirect(method = "renderItemSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", ordinal = 0))
    private void optiRefine$renderItemSideDepthMask(boolean flag) {
// [AUDIT-OK] target renderItemSide (SRG func_187462_a) GlStateManager.depthMask(false) ordinal 0 matches baseline (line 72); guard matches OF renderItemKeepDepthMask
        if (!Config.isShaders() || !Shaders.renderItemKeepDepthMask) {
            GlStateManager.depthMask(flag);
        }
    }

    /**
     * OptiFine: {@code if (Config.isDynamicLights()) var2 = DynamicLights.getCombinedLight(...)}.
     */
    @Redirect(method = "setLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getCombinedLight(Lnet/minecraft/util/math/BlockPos;I)I"))
    private int optiRefine$setLightmapDynamicLights(World world, BlockPos pos, int light) {
// [AUDIT-OK] target setLightmap()V (SRG func_187464_b) World.getCombinedLight matches baseline (line 98)
        int combined = world.getCombinedLight(pos, light);
        if (Config.isDynamicLights()) {
            combined = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), combined);
        }
        return combined;
    }

    /**
     * OptiFine: {@code var9.getItem() == Items.BOW} -&gt; {@code var9.getItem() instanceof ItemBow}.
     */
    @Redirect(method = "renderItemInFirstPerson(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item optiRefine$itemBow(ItemStack stack) {
// [AUDIT-OK] target renderItemInFirstPerson(F)V (SRG func_78440_a) ItemStack.getItem matches baseline (line 315, single occurrence); instanceof ItemBow matches OF
        Item item = stack.getItem();
        return item instanceof ItemBow ? Items.BOW : item;
    }

    /**
     * OptiFine: {@code if (!Config.isShaders() || !Shaders.isSkipRenderHand(var4))} wraps the whole method.
     */
    @WrapMethod(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V")
    private void optiRefine$renderItemInFirstPerson(AbstractClientPlayer player, float p2, float p3, EnumHand hand, float p5, ItemStack stack, float p7, Operation<Void> original) {
// [AUDIT-OK] target renderItemInFirstPerson(LAbstractClientPlayer;FFLEnumHand;FLItemStack;F)V (SRG func_187457_a) matches baseline
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(hand)) {
            original.call(player, p2, p3, hand, p5, stack, p7);
        }
    }

    /**
     * OptiFine: {@code if (!Config.isShaders() || Shaders.isUnderwaterOverlay())} wraps the whole method.
     */
    @WrapMethod(method = "renderWaterOverlayTexture")
    private void optiRefine$renderWaterOverlayTexture(float partialTicks, Operation<Void> original) {
// [AUDIT-OK] target renderWaterOverlayTexture(F)V (SRG func_78448_c) matches baseline
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            original.call(partialTicks);
        }
    }

    /**
     * OptiFine: {@code var2.setSprite(var5)} right after {@code var2.begin(...)} in the fire overlay loop.
     */
    @Inject(method = "renderFireInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void optiRefine$setFireSprite(CallbackInfo ci, @Local(ordinal = 0) BufferBuilder bufferBuilder, @Local(ordinal = 0) TextureAtlasSprite sprite) {
// [AUDIT-OK] target renderFireInFirstPerson()V (SRG func_78442_d) begin() ordinal 0 matches baseline (line 564); sprite local assigned before begin (line 551) so @Local valid
        BufferBuilder_setSprite(bufferBuilder, sprite);
    }

    /**
     * OptiFine: notify shaders when the equipped main-hand item changes.
     */
    @Inject(method = "updateEquippedItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/ItemRenderer;itemStackMainHand:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void optiRefine$setItemToRenderMain(CallbackInfo ci) {
// [AUDIT-FIXED] 2026-08-05: updateEquippedItem has exactly ONE PUTFIELD itemStackMainHand
// (forge line 601; cleanroom patch wraps it in a condition, does not add a second store);
// ordinal=1 could not be found -> mixin apply failure. ordinal=0 verified.
        if (Config.isShaders()) {
            Shaders.setItemToRenderMain(this.itemStackMainHand);
        }
    }

    /**
     * OptiFine: notify shaders when the equipped off-hand item changes.
     */
    @Inject(method = "updateEquippedItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/ItemRenderer;itemStackOffHand:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void optiRefine$setItemToRenderOff(CallbackInfo ci) {
// [AUDIT-FIXED] 2026-08-05: single PUTFIELD itemStackOffHand (forge line 606); ordinal=1 not found -> apply failure. ordinal=0 verified.
        if (Config.isShaders()) {
            Shaders.setItemToRenderOff(this.itemStackOffHand);
        }
    }
}
