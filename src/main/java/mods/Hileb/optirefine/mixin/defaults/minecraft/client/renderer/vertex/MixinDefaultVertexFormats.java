package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.vertex;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.Attributes;
import net.optifine.shaders.SVertexFormat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;

@Mixin(DefaultVertexFormats.class)
public abstract class MixinDefaultVertexFormats {
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public static VertexFormat BLOCK;

    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public static VertexFormat ITEM;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_176600_a", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static VertexFormat ACC_BLOCK;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_176599_b", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static VertexFormat ACC_ITEM;

    @Unique
    private static final VertexFormat BLOCK_VANILLA = DefaultVertexFormats.BLOCK;
    @Unique
    private static final VertexFormat ITEM_VANILLA = DefaultVertexFormats.ITEM;
    @Unique
    private static final VertexFormat FORGE_BAKED = Attributes.DEFAULT_BAKED_FORMAT;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static void updateVertexFormats() {
        if (Config.isShaders()) {
            BLOCK = SVertexFormat.makeDefVertexFormatBlock();
            ITEM = SVertexFormat.makeDefVertexFormatItem();
            SVertexFormat.setDefBakedFormat(Attributes.DEFAULT_BAKED_FORMAT);
        } else {
            BLOCK = BLOCK_VANILLA;
            ITEM = ITEM_VANILLA;
            SVertexFormat.copy(FORGE_BAKED, Attributes.DEFAULT_BAKED_FORMAT);
        }
    }
}
/*
+++ net/minecraft/client/renderer/vertex/DefaultVertexFormats.java	Tue Aug 19 14:59:58 2025
@@ -1,29 +1,65 @@
 package net.minecraft.client.renderer.vertex;

+import java.lang.reflect.Field;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
+import net.optifine.reflect.ReflectorClass;
+import net.optifine.reflect.ReflectorField;
+import net.optifine.shaders.SVertexFormat;
+
 public class DefaultVertexFormats {
-   public static final VertexFormat BLOCK = new VertexFormat();
-   public static final VertexFormat ITEM = new VertexFormat();
+   public static VertexFormat BLOCK = new VertexFormat();
+   public static VertexFormat ITEM = new VertexFormat();
+   private static final VertexFormat BLOCK_VANILLA = BLOCK;
+   private static final VertexFormat ITEM_VANILLA = ITEM;
+   public static ReflectorClass Attributes = new ReflectorClass("net.minecraftforge.client.model.Attributes");
+   public static ReflectorField Attributes_DEFAULT_BAKED_FORMAT = new ReflectorField(Attributes, "DEFAULT_BAKED_FORMAT");
+   private static final VertexFormat FORGE_BAKED = SVertexFormat.duplicate((VertexFormat)getFieldValue(Attributes_DEFAULT_BAKED_FORMAT));
    public static final VertexFormat OLDMODEL_POSITION_TEX_NORMAL = new VertexFormat();
    public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
    public static final VertexFormat POSITION = new VertexFormat();
    public static final VertexFormat POSITION_COLOR = new VertexFormat();
    public static final VertexFormat POSITION_TEX = new VertexFormat();
    public static final VertexFormat POSITION_NORMAL = new VertexFormat();
    public static final VertexFormat POSITION_TEX_COLOR = new VertexFormat();
    public static final VertexFormat POSITION_TEX_NORMAL = new VertexFormat();
    public static final VertexFormat POSITION_TEX_LMAP_COLOR = new VertexFormat();
    public static final VertexFormat POSITION_TEX_COLOR_NORMAL = new VertexFormat();
-   public static final VertexFormatElement POSITION_3F = new VertexFormatElement(
-      0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3
-   );
-   public static final VertexFormatElement COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
-   public static final VertexFormatElement TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
-   public static final VertexFormatElement TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
-   public static final VertexFormatElement NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
-   public static final VertexFormatElement PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);
+   public static final VertexFormatElement POSITION_3F = new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.POSITION, 3);
+   public static final VertexFormatElement COLOR_4UB = new VertexFormatElement(0, EnumType.UBYTE, EnumUsage.COLOR, 4);
+   public static final VertexFormatElement TEX_2F = new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.UV, 2);
+   public static final VertexFormatElement TEX_2S = new VertexFormatElement(1, EnumType.SHORT, EnumUsage.UV, 2);
+   public static final VertexFormatElement NORMAL_3B = new VertexFormatElement(0, EnumType.BYTE, EnumUsage.NORMAL, 3);
+   public static final VertexFormatElement PADDING_1B = new VertexFormatElement(0, EnumType.BYTE, EnumUsage.PADDING, 1);
+
+   public static void updateVertexFormats() {
+      if (Config.isShaders()) {
+         BLOCK = SVertexFormat.makeDefVertexFormatBlock();
+         ITEM = SVertexFormat.makeDefVertexFormatItem();
+         if (Attributes_DEFAULT_BAKED_FORMAT.exists()) {
+            SVertexFormat.setDefBakedFormat((VertexFormat)Attributes_DEFAULT_BAKED_FORMAT.getValue());
+         }
+      } else {
+         BLOCK = BLOCK_VANILLA;
+         ITEM = ITEM_VANILLA;
+         if (Attributes_DEFAULT_BAKED_FORMAT.exists()) {
+            SVertexFormat.copy(FORGE_BAKED, (VertexFormat)Attributes_DEFAULT_BAKED_FORMAT.getValue());
+         }
+      }
+   }
+
+   public static Object getFieldValue(ReflectorField var0) {
+      try {
+         Field var1 = var0.getTargetField();
+         return var1 == null ? null : var1.get(null);
+      } catch (Throwable var3) {
+         var3.printStackTrace();
+         return null;
+      }
+   }

    static {
       BLOCK.addElement(POSITION_3F);
       BLOCK.addElement(COLOR_4UB);
       BLOCK.addElement(TEX_2F);
       BLOCK.addElement(TEX_2S);
 */
