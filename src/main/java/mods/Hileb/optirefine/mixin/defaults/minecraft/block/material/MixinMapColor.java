package mods.Hileb.optirefine.mixin.defaults.minecraft.block.material;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Checked
@Mixin(MapColor.class)
public abstract class MixinMapColor {


    @Shadow
    @Mutable
    public int colorValue;

}
/*
+++ net/minecraft/block/material/MapColor.java	Tue Aug 19 14:59:58 2025
@@ -54,13 +54,13 @@
    public static final MapColor PURPLE_STAINED_HARDENED_CLAY = new MapColor(46, 8014168);
    public static final MapColor BLUE_STAINED_HARDENED_CLAY = new MapColor(47, 4996700);
    public static final MapColor BROWN_STAINED_HARDENED_CLAY = new MapColor(48, 4993571);
    public static final MapColor GREEN_STAINED_HARDENED_CLAY = new MapColor(49, 5001770);
    public static final MapColor RED_STAINED_HARDENED_CLAY = new MapColor(50, 9321518);
    public static final MapColor BLACK_STAINED_HARDENED_CLAY = new MapColor(51, 2430480);
-   public final int colorValue;
+   public int colorValue;
    public final int colorIndex;

    private MapColor(int var1, int var2) {
       if (var1 >= 0 && var1 <= 63) {
          this.colorIndex = var1;
          this.colorValue = var2;

 */
