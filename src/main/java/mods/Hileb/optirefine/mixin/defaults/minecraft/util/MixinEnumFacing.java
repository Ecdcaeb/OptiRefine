package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.util.EnumFacing;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;

@Checked
@Mixin(EnumFacing.class)
public abstract class MixinEnumFacing {

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, name = "field_82609_l", deobf = true)
    private static EnumFacing[] _ACC_VALUES;
}
/*
--- net/minecraft/util/EnumFacing.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/EnumFacing.java	Tue Aug 19 14:59:58 2025
@@ -25,13 +25,13 @@
    private final int opposite;
    private final int horizontalIndex;
    private final String name;
    private final EnumFacing.Axis axis;
    private final EnumFacing.AxisDirection axisDirection;
    private final Vec3i directionVec;
-   private static final EnumFacing[] VALUES = new EnumFacing[6];
+   public static final EnumFacing[] VALUES = new EnumFacing[6];
    private static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
    private static final Map<String, EnumFacing> NAME_LOOKUP = Maps.newHashMap();

    private EnumFacing(int var3, int var4, int var5, String var6, EnumFacing.AxisDirection var7, EnumFacing.Axis var8, Vec3i var9) {
       this.index = var3;
       this.horizontalIndex = var5;
@@ -52,13 +52,13 @@

    public EnumFacing.AxisDirection getAxisDirection() {
       return this.axisDirection;
    }

    public EnumFacing getOpposite() {
-      return byIndex(this.opposite);
+      return VALUES[this.opposite];
    }

    public EnumFacing rotateAround(EnumFacing.Axis var1) {
       switch (var1) {
          case X:
             if (this != WEST && this != EAST) {
 */
