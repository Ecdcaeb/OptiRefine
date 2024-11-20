/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.settings;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public static enum GameSettings.Options {
    INVERT_MOUSE("options.invertMouse", false, true),
    SENSITIVITY("options.sensitivity", true, false),
    FOV("options.fov", true, false, 30.0f, 110.0f, 1.0f),
    GAMMA("options.gamma", true, false),
    SATURATION("options.saturation", true, false),
    RENDER_DISTANCE("options.renderDistance", true, false, 2.0f, 16.0f, 1.0f),
    VIEW_BOBBING("options.viewBobbing", false, true),
    ANAGLYPH("options.anaglyph", false, true),
    FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0f, 260.0f, 10.0f),
    FBO_ENABLE("options.fboEnable", false, true),
    RENDER_CLOUDS("options.renderClouds", false, false),
    GRAPHICS("options.graphics", false, false),
    AMBIENT_OCCLUSION("options.ao", false, false),
    GUI_SCALE("options.guiScale", false, false),
    PARTICLES("options.particles", false, false),
    CHAT_VISIBILITY("options.chat.visibility", false, false),
    CHAT_COLOR("options.chat.color", false, true),
    CHAT_LINKS("options.chat.links", false, true),
    CHAT_OPACITY("options.chat.opacity", true, false),
    CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
    SNOOPER_ENABLED("options.snooper", false, true),
    USE_FULLSCREEN("options.fullscreen", false, true),
    ENABLE_VSYNC("options.vsync", false, true),
    USE_VBO("options.vbo", false, true),
    TOUCHSCREEN("options.touchscreen", false, true),
    CHAT_SCALE("options.chat.scale", true, false),
    CHAT_WIDTH("options.chat.width", true, false),
    CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
    CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
    MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f),
    FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
    REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
    ENTITY_SHADOWS("options.entityShadows", false, true),
    MAIN_HAND("options.mainHand", false, false),
    ATTACK_INDICATOR("options.attackIndicator", false, false),
    ENABLE_WEAK_ATTACKS("options.enableWeakAttacks", false, true),
    SHOW_SUBTITLES("options.showSubtitles", false, true),
    REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
    AUTO_JUMP("options.autoJump", false, true),
    NARRATOR("options.narrator", false, false);

    private final boolean isFloat;
    private final boolean isBoolean;
    private final String translation;
    private final float valueStep;
    private float valueMin;
    private float valueMax;

    public static GameSettings.Options byOrdinal(int ordinal) {
        for (GameSettings.Options gamesettings$options : GameSettings.Options.values()) {
            if (gamesettings$options.getOrdinal() != ordinal) continue;
            return gamesettings$options;
        }
        return null;
    }

    private GameSettings.Options(String translation, boolean isFloat, boolean isBoolean) {
        this(translation, isFloat, isBoolean, 0.0f, 1.0f, 0.0f);
    }

    private GameSettings.Options(String translation, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
        this.translation = translation;
        this.isFloat = isFloat;
        this.isBoolean = isBoolean;
        this.valueMin = valMin;
        this.valueMax = valMax;
        this.valueStep = valStep;
    }

    public boolean isFloat() {
        return this.isFloat;
    }

    public boolean isBoolean() {
        return this.isBoolean;
    }

    public int getOrdinal() {
        return this.ordinal();
    }

    public String getTranslation() {
        return this.translation;
    }

    public float getValueMin() {
        return this.valueMin;
    }

    public float getValueMax() {
        return this.valueMax;
    }

    public void setValueMax(float value) {
        this.valueMax = value;
    }

    public float normalizeValue(float value) {
        return MathHelper.clamp((float)((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin)), (float)0.0f, (float)1.0f);
    }

    public float denormalizeValue(float value) {
        return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp((float)value, (float)0.0f, (float)1.0f));
    }

    public float snapToStepClamp(float value) {
        value = this.snapToStep(value);
        return MathHelper.clamp((float)value, (float)this.valueMin, (float)this.valueMax);
    }

    private float snapToStep(float value) {
        if (this.valueStep > 0.0f) {
            value = this.valueStep * (float)Math.round((float)(value / this.valueStep));
        }
        return value;
    }
}
