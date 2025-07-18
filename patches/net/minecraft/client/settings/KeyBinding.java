package net.minecraft.client.settings;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;
import org.lwjgl.input.Keyboard;

public class KeyBinding implements Comparable<KeyBinding> {
   private static final Map<String, KeyBinding> KEYBIND_ARRAY = Maps.newHashMap();
   private static final IntHashMap<KeyBinding> HASH = new IntHashMap<>();
   private static final Set<String> KEYBIND_SET = Sets.newHashSet();
   private static final Map<String, Integer> CATEGORY_ORDER = Maps.newHashMap();
   private final String keyDescription;
   private final int keyCodeDefault;
   private final String keyCategory;
   private int keyCode;
   private boolean pressed;
   private int pressTime;

   public static void onTick(int var0) {
      if (☃ != 0) {
         KeyBinding ☃ = HASH.lookup(☃);
         if (☃ != null) {
            ☃.pressTime++;
         }
      }
   }

   public static void setKeyBindState(int var0, boolean var1) {
      if (☃ != 0) {
         KeyBinding ☃ = HASH.lookup(☃);
         if (☃ != null) {
            ☃.pressed = ☃;
         }
      }
   }

   public static void updateKeyBindState() {
      for (KeyBinding ☃ : KEYBIND_ARRAY.values()) {
         try {
            setKeyBindState(☃.keyCode, ☃.keyCode < 256 && Keyboard.isKeyDown(☃.keyCode));
         } catch (IndexOutOfBoundsException var3) {
         }
      }
   }

   public static void unPressAllKeys() {
      for (KeyBinding ☃ : KEYBIND_ARRAY.values()) {
         ☃.unpressKey();
      }
   }

   public static void resetKeyBindingArrayAndHash() {
      HASH.clearMap();

      for (KeyBinding ☃ : KEYBIND_ARRAY.values()) {
         HASH.addKey(☃.keyCode, ☃);
      }
   }

   public static Set<String> getKeybinds() {
      return KEYBIND_SET;
   }

   public KeyBinding(String var1, int var2, String var3) {
      this.keyDescription = ☃;
      this.keyCode = ☃;
      this.keyCodeDefault = ☃;
      this.keyCategory = ☃;
      KEYBIND_ARRAY.put(☃, this);
      HASH.addKey(☃, this);
      KEYBIND_SET.add(☃);
   }

   public boolean isKeyDown() {
      return this.pressed;
   }

   public String getKeyCategory() {
      return this.keyCategory;
   }

   public boolean isPressed() {
      if (this.pressTime == 0) {
         return false;
      } else {
         this.pressTime--;
         return true;
      }
   }

   private void unpressKey() {
      this.pressTime = 0;
      this.pressed = false;
   }

   public String getKeyDescription() {
      return this.keyDescription;
   }

   public int getKeyCodeDefault() {
      return this.keyCodeDefault;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public void setKeyCode(int var1) {
      this.keyCode = ☃;
   }

   public int compareTo(KeyBinding var1) {
      return this.keyCategory.equals(☃.keyCategory)
         ? I18n.format(this.keyDescription).compareTo(I18n.format(☃.keyDescription))
         : CATEGORY_ORDER.get(this.keyCategory).compareTo(CATEGORY_ORDER.get(☃.keyCategory));
   }

   public static Supplier<String> getDisplayString(String var0) {
      KeyBinding ☃ = KEYBIND_ARRAY.get(☃);
      return ☃ == null ? () -> ☃ : () -> GameSettings.getKeyDisplayString(☃.getKeyCode());
   }

   static {
      CATEGORY_ORDER.put("key.categories.movement", 1);
      CATEGORY_ORDER.put("key.categories.gameplay", 2);
      CATEGORY_ORDER.put("key.categories.inventory", 3);
      CATEGORY_ORDER.put("key.categories.creative", 4);
      CATEGORY_ORDER.put("key.categories.multiplayer", 5);
      CATEGORY_ORDER.put("key.categories.ui", 6);
      CATEGORY_ORDER.put("key.categories.misc", 7);
   }
}
