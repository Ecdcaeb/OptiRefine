package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.ArrayUtils;

public class Advancement {
   private final Advancement parent;
   private final DisplayInfo display;
   private final AdvancementRewards rewards;
   private final ResourceLocation id;
   private final Map<String, Criterion> criteria;
   private final String[][] requirements;
   private final Set<Advancement> children = Sets.newLinkedHashSet();
   private final ITextComponent displayText;

   public Advancement(
      ResourceLocation var1, @Nullable Advancement var2, @Nullable DisplayInfo var3, AdvancementRewards var4, Map<String, Criterion> var5, String[][] var6
   ) {
      this.id = ☃;
      this.display = ☃;
      this.criteria = ImmutableMap.copyOf(☃);
      this.parent = ☃;
      this.rewards = ☃;
      this.requirements = ☃;
      if (☃ != null) {
         ☃.addChild(this);
      }

      if (☃ == null) {
         this.displayText = new TextComponentString(☃.toString());
      } else {
         this.displayText = new TextComponentString("[");
         this.displayText.getStyle().setColor(☃.getFrame().getFormat());
         ITextComponent ☃ = ☃.getTitle().createCopy();
         ITextComponent ☃x = new TextComponentString("");
         ITextComponent ☃xx = ☃.createCopy();
         ☃xx.getStyle().setColor(☃.getFrame().getFormat());
         ☃x.appendSibling(☃xx);
         ☃x.appendText("\n");
         ☃x.appendSibling(☃.getDescription());
         ☃.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ☃x));
         this.displayText.appendSibling(☃);
         this.displayText.appendText("]");
      }
   }

   public Advancement.Builder copy() {
      return new Advancement.Builder(this.parent == null ? null : this.parent.getId(), this.display, this.rewards, this.criteria, this.requirements);
   }

   @Nullable
   public Advancement getParent() {
      return this.parent;
   }

   @Nullable
   public DisplayInfo getDisplay() {
      return this.display;
   }

   public AdvancementRewards getRewards() {
      return this.rewards;
   }

   @Override
   public String toString() {
      return "SimpleAdvancement{id="
         + this.getId()
         + ", parent="
         + (this.parent == null ? "null" : this.parent.getId())
         + ", display="
         + this.display
         + ", rewards="
         + this.rewards
         + ", criteria="
         + this.criteria
         + ", requirements="
         + Arrays.deepToString(this.requirements)
         + '}';
   }

   public Iterable<Advancement> getChildren() {
      return this.children;
   }

   public Map<String, Criterion> getCriteria() {
      return this.criteria;
   }

   public int getRequirementCount() {
      return this.requirements.length;
   }

   public void addChild(Advancement var1) {
      this.children.add(☃);
   }

   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof Advancement)) {
         return false;
      } else {
         Advancement ☃ = (Advancement)☃;
         return this.id.equals(☃.id);
      }
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   public String[][] getRequirements() {
      return this.requirements;
   }

   public ITextComponent getDisplayText() {
      return this.displayText;
   }

   public static class Builder {
      private final ResourceLocation parentId;
      private Advancement parent;
      private final DisplayInfo display;
      private final AdvancementRewards rewards;
      private final Map<String, Criterion> criteria;
      private final String[][] requirements;

      Builder(@Nullable ResourceLocation var1, @Nullable DisplayInfo var2, AdvancementRewards var3, Map<String, Criterion> var4, String[][] var5) {
         this.parentId = ☃;
         this.display = ☃;
         this.rewards = ☃;
         this.criteria = ☃;
         this.requirements = ☃;
      }

      public boolean resolveParent(Function<ResourceLocation, Advancement> var1) {
         if (this.parentId == null) {
            return true;
         } else {
            this.parent = ☃.apply(this.parentId);
            return this.parent != null;
         }
      }

      public Advancement build(ResourceLocation var1) {
         return new Advancement(☃, this.parent, this.display, this.rewards, this.criteria, this.requirements);
      }

      public void writeTo(PacketBuffer var1) {
         if (this.parentId == null) {
            ☃.writeBoolean(false);
         } else {
            ☃.writeBoolean(true);
            ☃.writeResourceLocation(this.parentId);
         }

         if (this.display == null) {
            ☃.writeBoolean(false);
         } else {
            ☃.writeBoolean(true);
            this.display.write(☃);
         }

         Criterion.serializeToNetwork(this.criteria, ☃);
         ☃.writeVarInt(this.requirements.length);

         for (String[] ☃ : this.requirements) {
            ☃.writeVarInt(☃.length);

            for (String ☃x : ☃) {
               ☃.writeString(☃x);
            }
         }
      }

      @Override
      public String toString() {
         return "Task Advancement{parentId="
            + this.parentId
            + ", display="
            + this.display
            + ", rewards="
            + this.rewards
            + ", criteria="
            + this.criteria
            + ", requirements="
            + Arrays.deepToString(this.requirements)
            + '}';
      }

      public static Advancement.Builder deserialize(JsonObject var0, JsonDeserializationContext var1) {
         ResourceLocation ☃ = ☃.has("parent") ? new ResourceLocation(JsonUtils.getString(☃, "parent")) : null;
         DisplayInfo ☃x = ☃.has("display") ? DisplayInfo.deserialize(JsonUtils.getJsonObject(☃, "display"), ☃) : null;
         AdvancementRewards ☃xx = JsonUtils.deserializeClass(☃, "rewards", AdvancementRewards.EMPTY, ☃, AdvancementRewards.class);
         Map<String, Criterion> ☃xxx = Criterion.criteriaFromJson(JsonUtils.getJsonObject(☃, "criteria"), ☃);
         if (☃xxx.isEmpty()) {
            throw new JsonSyntaxException("Advancement criteria cannot be empty");
         } else {
            JsonArray ☃xxxx = JsonUtils.getJsonArray(☃, "requirements", new JsonArray());
            String[][] ☃xxxxx = new String[☃xxxx.size()][];

            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxx.size(); ☃xxxxxx++) {
               JsonArray ☃xxxxxxx = JsonUtils.getJsonArray(☃xxxx.get(☃xxxxxx), "requirements[" + ☃xxxxxx + "]");
               ☃xxxxx[☃xxxxxx] = new String[☃xxxxxxx.size()];

               for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxxxx.size(); ☃xxxxxxxx++) {
                  ☃xxxxx[☃xxxxxx][☃xxxxxxxx] = JsonUtils.getString(☃xxxxxxx.get(☃xxxxxxxx), "requirements[" + ☃xxxxxx + "][" + ☃xxxxxxxx + "]");
               }
            }

            if (☃xxxxx.length == 0) {
               ☃xxxxx = new String[☃xxx.size()][];
               int ☃xxxxxx = 0;

               for (String ☃xxxxxxx : ☃xxx.keySet()) {
                  ☃xxxxx[☃xxxxxx++] = new String[]{☃xxxxxxx};
               }
            }

            for (String[] ☃xxxxxx : ☃xxxxx) {
               if (☃xxxxxx.length == 0 && ☃xxx.isEmpty()) {
                  throw new JsonSyntaxException("Requirement entry cannot be empty");
               }

               for (String ☃xxxxxxx : ☃xxxxxx) {
                  if (!☃xxx.containsKey(☃xxxxxxx)) {
                     throw new JsonSyntaxException("Unknown required criterion '" + ☃xxxxxxx + "'");
                  }
               }
            }

            for (String ☃xxxxxx : ☃xxx.keySet()) {
               boolean ☃xxxxxxxx = false;

               for (String[] ☃xxxxxxxxx : ☃xxxxx) {
                  if (ArrayUtils.contains(☃xxxxxxxxx, ☃xxxxxx)) {
                     ☃xxxxxxxx = true;
                     break;
                  }
               }

               if (!☃xxxxxxxx) {
                  throw new JsonSyntaxException(
                     "Criterion '" + ☃xxxxxx + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required."
                  );
               }
            }

            return new Advancement.Builder(☃, ☃x, ☃xx, ☃xxx, ☃xxxxx);
         }
      }

      public static Advancement.Builder readFrom(PacketBuffer var0) {
         ResourceLocation ☃ = ☃.readBoolean() ? ☃.readResourceLocation() : null;
         DisplayInfo ☃x = ☃.readBoolean() ? DisplayInfo.read(☃) : null;
         Map<String, Criterion> ☃xx = Criterion.criteriaFromNetwork(☃);
         String[][] ☃xxx = new String[☃.readVarInt()][];

         for (int ☃xxxx = 0; ☃xxxx < ☃xxx.length; ☃xxxx++) {
            ☃xxx[☃xxxx] = new String[☃.readVarInt()];

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxx[☃xxxx].length; ☃xxxxx++) {
               ☃xxx[☃xxxx][☃xxxxx] = ☃.readString(32767);
            }
         }

         return new Advancement.Builder(☃, ☃x, AdvancementRewards.EMPTY, ☃xx, ☃xxx);
      }
   }
}
