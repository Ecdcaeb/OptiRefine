/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.ParameterizedType
 *  java.lang.reflect.Type
 *  java.util.List
 */
package net.minecraft.client.settings;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

static final class GameSettings.1
implements ParameterizedType {
    GameSettings.1() {
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{String.class};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
