package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DisplayInfo {
   private final ITextComponent title;
   private final ITextComponent description;
   private final ItemStack icon;
   private final ResourceLocation background;
   private final FrameType frame;
   private final boolean showToast;
   private final boolean announceToChat;
   private final boolean hidden;
   private float x;
   private float y;

   public DisplayInfo(
      ItemStack var1, ITextComponent var2, ITextComponent var3, @Nullable ResourceLocation var4, FrameType var5, boolean var6, boolean var7, boolean var8
   ) {
      this.title = ☃;
      this.description = ☃;
      this.icon = ☃;
      this.background = ☃;
      this.frame = ☃;
      this.showToast = ☃;
      this.announceToChat = ☃;
      this.hidden = ☃;
   }

   public void setPosition(float var1, float var2) {
      this.x = ☃;
      this.y = ☃;
   }

   public ITextComponent getTitle() {
      return this.title;
   }

   public ITextComponent getDescription() {
      return this.description;
   }

   public ItemStack getIcon() {
      return this.icon;
   }

   @Nullable
   public ResourceLocation getBackground() {
      return this.background;
   }

   public FrameType getFrame() {
      return this.frame;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public boolean shouldShowToast() {
      return this.showToast;
   }

   public boolean shouldAnnounceToChat() {
      return this.announceToChat;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public static DisplayInfo deserialize(JsonObject var0, JsonDeserializationContext var1) {
      ITextComponent ☃ = JsonUtils.deserializeClass(☃, "title", ☃, ITextComponent.class);
      ITextComponent ☃x = JsonUtils.deserializeClass(☃, "description", ☃, ITextComponent.class);
      if (☃ != null && ☃x != null) {
         ItemStack ☃xx = deserializeIcon(JsonUtils.getJsonObject(☃, "icon"));
         ResourceLocation ☃xxx = ☃.has("background") ? new ResourceLocation(JsonUtils.getString(☃, "background")) : null;
         FrameType ☃xxxx = ☃.has("frame") ? FrameType.byName(JsonUtils.getString(☃, "frame")) : FrameType.TASK;
         boolean ☃xxxxx = JsonUtils.getBoolean(☃, "show_toast", true);
         boolean ☃xxxxxx = JsonUtils.getBoolean(☃, "announce_to_chat", true);
         boolean ☃xxxxxxx = JsonUtils.getBoolean(☃, "hidden", false);
         return new DisplayInfo(☃xx, ☃, ☃x, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
      } else {
         throw new JsonSyntaxException("Both title and description must be set");
      }
   }

   private static ItemStack deserializeIcon(JsonObject var0) {
      if (!☃.has("item")) {
         throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
      } else {
         Item ☃ = JsonUtils.getItem(☃, "item");
         int ☃x = JsonUtils.getInt(☃, "data", 0);
         return new ItemStack(☃, 1, ☃x);
      }
   }

   public void write(PacketBuffer var1) {
      ☃.writeTextComponent(this.title);
      ☃.writeTextComponent(this.description);
      ☃.writeItemStack(this.icon);
      ☃.writeEnumValue(this.frame);
      int ☃ = 0;
      if (this.background != null) {
         ☃ |= 1;
      }

      if (this.showToast) {
         ☃ |= 2;
      }

      if (this.hidden) {
         ☃ |= 4;
      }

      ☃.writeInt(☃);
      if (this.background != null) {
         ☃.writeResourceLocation(this.background);
      }

      ☃.writeFloat(this.x);
      ☃.writeFloat(this.y);
   }

   public static DisplayInfo read(PacketBuffer var0) {
      ITextComponent ☃ = ☃.readTextComponent();
      ITextComponent ☃x = ☃.readTextComponent();
      ItemStack ☃xx = ☃.readItemStack();
      FrameType ☃xxx = ☃.readEnumValue(FrameType.class);
      int ☃xxxx = ☃.readInt();
      ResourceLocation ☃xxxxx = (☃xxxx & 1) != 0 ? ☃.readResourceLocation() : null;
      boolean ☃xxxxxx = (☃xxxx & 2) != 0;
      boolean ☃xxxxxxx = (☃xxxx & 4) != 0;
      DisplayInfo ☃xxxxxxxx = new DisplayInfo(☃xx, ☃, ☃x, ☃xxxxx, ☃xxx, ☃xxxxxx, false, ☃xxxxxxx);
      ☃xxxxxxxx.setPosition(☃.readFloat(), ☃.readFloat());
      return ☃xxxxxxxx;
   }
}
