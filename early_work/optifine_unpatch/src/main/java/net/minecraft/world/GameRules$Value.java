/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.world.GameRules$ValueType
 */
package net.minecraft.world;

import net.minecraft.world.GameRules;

static class GameRules.Value {
    private String valueString;
    private boolean valueBoolean;
    private int valueInteger;
    private double valueDouble;
    private final GameRules.ValueType type;

    public GameRules.Value(String string, GameRules.ValueType valueType) {
        this.type = valueType;
        this.setValue(string);
    }

    public void setValue(String string) {
        this.valueString = string;
        this.valueBoolean = Boolean.parseBoolean((String)string);
        this.valueInteger = this.valueBoolean ? 1 : 0;
        try {
            this.valueInteger = Integer.parseInt((String)string);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        try {
            this.valueDouble = Double.parseDouble((String)string);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }

    public String getString() {
        return this.valueString;
    }

    public boolean getBoolean() {
        return this.valueBoolean;
    }

    public int getInt() {
        return this.valueInteger;
    }

    public GameRules.ValueType getType() {
        return this.type;
    }
}
