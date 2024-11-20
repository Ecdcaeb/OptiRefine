/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.IndexOutOfBoundsException
 *  java.lang.Object
 *  net.minecraft.item.EnumDyeColor
 */
package net.minecraft.block.material;

import net.minecraft.item.EnumDyeColor;

public class MapColor {
    public static final MapColor[] COLORS = new MapColor[64];
    public static final MapColor[] BLOCK_COLORS = new MapColor[16];
    public static final MapColor AIR = new MapColor(0, 0);
    public static final MapColor GRASS = new MapColor(1, 8368696);
    public static final MapColor SAND = new MapColor(2, 16247203);
    public static final MapColor CLOTH = new MapColor(3, 0xC7C7C7);
    public static final MapColor TNT = new MapColor(4, 0xFF0000);
    public static final MapColor ICE = new MapColor(5, 0xA0A0FF);
    public static final MapColor IRON = new MapColor(6, 0xA7A7A7);
    public static final MapColor FOLIAGE = new MapColor(7, 31744);
    public static final MapColor SNOW = new MapColor(8, 0xFFFFFF);
    public static final MapColor CLAY = new MapColor(9, 10791096);
    public static final MapColor DIRT = new MapColor(10, 9923917);
    public static final MapColor STONE = new MapColor(11, 0x707070);
    public static final MapColor WATER = new MapColor(12, 0x4040FF);
    public static final MapColor WOOD = new MapColor(13, 9402184);
    public static final MapColor QUARTZ = new MapColor(14, 0xFFFCF5);
    public static final MapColor ADOBE = new MapColor(15, 14188339);
    public static final MapColor MAGENTA = new MapColor(16, 11685080);
    public static final MapColor LIGHT_BLUE = new MapColor(17, 6724056);
    public static final MapColor YELLOW = new MapColor(18, 0xE5E533);
    public static final MapColor LIME = new MapColor(19, 8375321);
    public static final MapColor PINK = new MapColor(20, 15892389);
    public static final MapColor GRAY = new MapColor(21, 0x4C4C4C);
    public static final MapColor SILVER = new MapColor(22, 0x999999);
    public static final MapColor CYAN = new MapColor(23, 5013401);
    public static final MapColor PURPLE = new MapColor(24, 8339378);
    public static final MapColor BLUE = new MapColor(25, 3361970);
    public static final MapColor BROWN = new MapColor(26, 6704179);
    public static final MapColor GREEN = new MapColor(27, 6717235);
    public static final MapColor RED = new MapColor(28, 0x993333);
    public static final MapColor BLACK = new MapColor(29, 0x191919);
    public static final MapColor GOLD = new MapColor(30, 16445005);
    public static final MapColor DIAMOND = new MapColor(31, 6085589);
    public static final MapColor LAPIS = new MapColor(32, 4882687);
    public static final MapColor EMERALD = new MapColor(33, 55610);
    public static final MapColor OBSIDIAN = new MapColor(34, 8476209);
    public static final MapColor NETHERRACK = new MapColor(35, 0x700200);
    public static final MapColor WHITE_STAINED_HARDENED_CLAY = new MapColor(36, 13742497);
    public static final MapColor ORANGE_STAINED_HARDENED_CLAY = new MapColor(37, 10441252);
    public static final MapColor MAGENTA_STAINED_HARDENED_CLAY = new MapColor(38, 9787244);
    public static final MapColor LIGHT_BLUE_STAINED_HARDENED_CLAY = new MapColor(39, 7367818);
    public static final MapColor YELLOW_STAINED_HARDENED_CLAY = new MapColor(40, 12223780);
    public static final MapColor LIME_STAINED_HARDENED_CLAY = new MapColor(41, 6780213);
    public static final MapColor PINK_STAINED_HARDENED_CLAY = new MapColor(42, 10505550);
    public static final MapColor GRAY_STAINED_HARDENED_CLAY = new MapColor(43, 0x392923);
    public static final MapColor SILVER_STAINED_HARDENED_CLAY = new MapColor(44, 8874850);
    public static final MapColor CYAN_STAINED_HARDENED_CLAY = new MapColor(45, 0x575C5C);
    public static final MapColor PURPLE_STAINED_HARDENED_CLAY = new MapColor(46, 8014168);
    public static final MapColor BLUE_STAINED_HARDENED_CLAY = new MapColor(47, 4996700);
    public static final MapColor BROWN_STAINED_HARDENED_CLAY = new MapColor(48, 4993571);
    public static final MapColor GREEN_STAINED_HARDENED_CLAY = new MapColor(49, 5001770);
    public static final MapColor RED_STAINED_HARDENED_CLAY = new MapColor(50, 9321518);
    public static final MapColor BLACK_STAINED_HARDENED_CLAY = new MapColor(51, 2430480);
    public final int colorValue;
    public final int colorIndex;

    private MapColor(int n, int n2) {
        if (n < 0 || n > 63) {
            throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
        }
        this.colorIndex = n;
        this.colorValue = n2;
        MapColor.COLORS[n] = this;
    }

    public int getMapColor(int n) {
        \u2603 = 220;
        if (n == 3) {
            \u2603 = 135;
        }
        if (n == 2) {
            \u2603 = 255;
        }
        if (n == 1) {
            \u2603 = 220;
        }
        if (n == 0) {
            \u2603 = 180;
        }
        \u2603 = (this.colorValue >> 16 & 0xFF) * \u2603 / 255;
        \u2603 = (this.colorValue >> 8 & 0xFF) * \u2603 / 255;
        \u2603 = (this.colorValue & 0xFF) * \u2603 / 255;
        return 0xFF000000 | \u2603 << 16 | \u2603 << 8 | \u2603;
    }

    public static MapColor getBlockColor(EnumDyeColor enumDyeColor) {
        return BLOCK_COLORS[enumDyeColor.getMetadata()];
    }

    static {
        MapColor.BLOCK_COLORS[EnumDyeColor.WHITE.getMetadata()] = SNOW;
        MapColor.BLOCK_COLORS[EnumDyeColor.ORANGE.getMetadata()] = ADOBE;
        MapColor.BLOCK_COLORS[EnumDyeColor.MAGENTA.getMetadata()] = MAGENTA;
        MapColor.BLOCK_COLORS[EnumDyeColor.LIGHT_BLUE.getMetadata()] = LIGHT_BLUE;
        MapColor.BLOCK_COLORS[EnumDyeColor.YELLOW.getMetadata()] = YELLOW;
        MapColor.BLOCK_COLORS[EnumDyeColor.LIME.getMetadata()] = LIME;
        MapColor.BLOCK_COLORS[EnumDyeColor.PINK.getMetadata()] = PINK;
        MapColor.BLOCK_COLORS[EnumDyeColor.GRAY.getMetadata()] = GRAY;
        MapColor.BLOCK_COLORS[EnumDyeColor.SILVER.getMetadata()] = SILVER;
        MapColor.BLOCK_COLORS[EnumDyeColor.CYAN.getMetadata()] = CYAN;
        MapColor.BLOCK_COLORS[EnumDyeColor.PURPLE.getMetadata()] = PURPLE;
        MapColor.BLOCK_COLORS[EnumDyeColor.BLUE.getMetadata()] = BLUE;
        MapColor.BLOCK_COLORS[EnumDyeColor.BROWN.getMetadata()] = BROWN;
        MapColor.BLOCK_COLORS[EnumDyeColor.GREEN.getMetadata()] = GREEN;
        MapColor.BLOCK_COLORS[EnumDyeColor.RED.getMetadata()] = RED;
        MapColor.BLOCK_COLORS[EnumDyeColor.BLACK.getMetadata()] = BLACK;
    }
}
