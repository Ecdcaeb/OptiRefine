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

    public void addGameRule(String key, String value, ValueType type) {
        this.rules.put((Object)key, (Object)new Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue) {
        Value gamerules$value = (Value)this.rules.get((Object)key);
        if (gamerules$value != null) {
            gamerules$value.setValue(ruleValue);
        } else {
            this.addGameRule(key, ruleValue, ValueType.ANY_VALUE);
        }
    }

    public String getString(String name) {
        Value gamerules$value = (Value)this.rules.get((Object)name);
        return gamerules$value != null ? gamerules$value.getString() : "";
    }

    public boolean getBoolean(String name) {
        Value gamerules$value = (Value)this.rules.get((Object)name);
        return gamerules$value != null ? gamerules$value.getBoolean() : false;
    }

    public int getInt(String name) {
        Value gamerules$value = (Value)this.rules.get((Object)name);
        return gamerules$value != null ? gamerules$value.getInt() : 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (String s : this.rules.keySet()) {
            Value gamerules$value = (Value)this.rules.get((Object)s);
            nbttagcompound.setString(s, gamerules$value.getString());
        }
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        for (String s : nbt.getKeySet()) {
            this.setOrCreateGameRule(s, nbt.getString(s));
        }
    }

    public String[] getRules() {
        Set set = this.rules.keySet();
        return (String[])set.toArray((Object[])new String[set.size()]);
    }

    public boolean hasRule(String name) {
        return this.rules.containsKey((Object)name);
    }

    public boolean areSameType(String key, ValueType otherValue) {
        Value gamerules$value = (Value)this.rules.get((Object)key);
        return gamerules$value != null && (gamerules$value.getType() == otherValue || otherValue == ValueType.ANY_VALUE);
    }
}
