/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Set
 *  java.util.TreeMap
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.GameRules$Value
 *  net.minecraft.world.GameRules$ValueType
 */
package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;

public class GameRules {
    private final TreeMap<String, Value> rules = new TreeMap();

    public GameRules() {
        this.addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("spectatorsGenerateChunks", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("spawnRadius", "10", ValueType.NUMERICAL_VALUE);
        this.addGameRule("disableElytraMovementCheck", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("maxEntityCramming", "24", ValueType.NUMERICAL_VALUE);
        this.addGameRule("doWeatherCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doLimitedCrafting", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("maxCommandChainLength", "65536", ValueType.NUMERICAL_VALUE);
        this.addGameRule("announceAdvancements", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("gameLoopFunction", "-", ValueType.FUNCTION);
    }

    public void addGameRule(String string, String string2, ValueType valueType) {
        this.rules.put((Object)string, (Object)new Value(string2, valueType));
    }

    public void setOrCreateGameRule(String string, String string2) {
        Value value = (Value)this.rules.get((Object)string);
        if (value != null) {
            value.setValue(string2);
        } else {
            this.addGameRule(string, string2, ValueType.ANY_VALUE);
        }
    }

    public String getString(String string) {
        Value value = (Value)this.rules.get((Object)string);
        if (value != null) {
            return value.getString();
        }
        return "";
    }

    public boolean getBoolean(String string) {
        Value value = (Value)this.rules.get((Object)string);
        if (value != null) {
            return value.getBoolean();
        }
        return false;
    }

    public int getInt(String string) {
        Value value = (Value)this.rules.get((Object)string);
        if (value != null) {
            return value.getInt();
        }
        return 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        for (String string : this.rules.keySet()) {
            Value value = (Value)this.rules.get((Object)string);
            nBTTagCompound.setString(string, value.getString());
        }
        return nBTTagCompound;
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        Set set = nBTTagCompound.getKeySet();
        for (String string : set) {
            this.setOrCreateGameRule(string, nBTTagCompound.getString(string));
        }
    }

    public String[] getRules() {
        Set set = this.rules.keySet();
        return (String[])set.toArray((Object[])new String[set.size()]);
    }

    public boolean hasRule(String string) {
        return this.rules.containsKey((Object)string);
    }

    public boolean areSameType(String string, ValueType valueType) {
        Value value = (Value)this.rules.get((Object)string);
        return value != null && (value.getType() == valueType || valueType == ValueType.ANY_VALUE);
    }
}
