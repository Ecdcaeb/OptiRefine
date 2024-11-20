/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.inventory.EntityEquipmentSlot$Type
 */
package net.minecraft.entity;

import net.minecraft.inventory.EntityEquipmentSlot;

static class EntityLiving.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type;
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot;

    static {
        $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot = new int[EntityEquipmentSlot.values().length];
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.HEAD.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.CHEST.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.LEGS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.FEET.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type = new int[EntityEquipmentSlot.Type.values().length];
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[EntityEquipmentSlot.Type.HAND.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EntityLiving.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[EntityEquipmentSlot.Type.ARMOR.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
