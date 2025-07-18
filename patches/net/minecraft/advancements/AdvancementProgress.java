package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;

public class AdvancementProgress implements Comparable<AdvancementProgress> {
   private final Map<String, CriterionProgress> criteria = Maps.newHashMap();
   private String[][] requirements = new String[0][];

   public void update(Map<String, Criterion> var1, String[][] var2) {
      Set<String> ☃ = ☃.keySet();
      Iterator<Entry<String, CriterionProgress>> ☃x = this.criteria.entrySet().iterator();

      while (☃x.hasNext()) {
         Entry<String, CriterionProgress> ☃xx = ☃x.next();
         if (!☃.contains(☃xx.getKey())) {
            ☃x.remove();
         }
      }

      for (String ☃xx : ☃) {
         if (!this.criteria.containsKey(☃xx)) {
            this.criteria.put(☃xx, new CriterionProgress(this));
         }
      }

      this.requirements = ☃;
   }

   public boolean isDone() {
      if (this.requirements.length == 0) {
         return false;
      } else {
         for (String[] ☃ : this.requirements) {
            boolean ☃x = false;

            for (String ☃xx : ☃) {
               CriterionProgress ☃xxx = this.getCriterionProgress(☃xx);
               if (☃xxx != null && ☃xxx.isObtained()) {
                  ☃x = true;
                  break;
               }
            }

            if (!☃x) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean hasProgress() {
      for (CriterionProgress ☃ : this.criteria.values()) {
         if (☃.isObtained()) {
            return true;
         }
      }

      return false;
   }

   public boolean grantCriterion(String var1) {
      CriterionProgress ☃ = this.criteria.get(☃);
      if (☃ != null && !☃.isObtained()) {
         ☃.obtain();
         return true;
      } else {
         return false;
      }
   }

   public boolean revokeCriterion(String var1) {
      CriterionProgress ☃ = this.criteria.get(☃);
      if (☃ != null && ☃.isObtained()) {
         ☃.reset();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "AdvancementProgress{criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
   }

   public void serializeToNetwork(PacketBuffer var1) {
      ☃.writeVarInt(this.criteria.size());

      for (Entry<String, CriterionProgress> ☃ : this.criteria.entrySet()) {
         ☃.writeString(☃.getKey());
         ☃.getValue().write(☃);
      }
   }

   public static AdvancementProgress fromNetwork(PacketBuffer var0) {
      AdvancementProgress ☃ = new AdvancementProgress();
      int ☃x = ☃.readVarInt();

      for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
         ☃.criteria.put(☃.readString(32767), CriterionProgress.read(☃, ☃));
      }

      return ☃;
   }

   @Nullable
   public CriterionProgress getCriterionProgress(String var1) {
      return this.criteria.get(☃);
   }

   public float getPercent() {
      if (this.criteria.isEmpty()) {
         return 0.0F;
      } else {
         float ☃ = this.requirements.length;
         float ☃x = this.countCompletedRequirements();
         return ☃x / ☃;
      }
   }

   @Nullable
   public String getProgressText() {
      if (this.criteria.isEmpty()) {
         return null;
      } else {
         int ☃ = this.requirements.length;
         if (☃ <= 1) {
            return null;
         } else {
            int ☃x = this.countCompletedRequirements();
            return ☃x + "/" + ☃;
         }
      }
   }

   private int countCompletedRequirements() {
      int ☃ = 0;

      for (String[] ☃x : this.requirements) {
         boolean ☃xx = false;

         for (String ☃xxx : ☃x) {
            CriterionProgress ☃xxxx = this.getCriterionProgress(☃xxx);
            if (☃xxxx != null && ☃xxxx.isObtained()) {
               ☃xx = true;
               break;
            }
         }

         if (☃xx) {
            ☃++;
         }
      }

      return ☃;
   }

   public Iterable<String> getRemaningCriteria() {
      List<String> ☃ = Lists.newArrayList();

      for (Entry<String, CriterionProgress> ☃x : this.criteria.entrySet()) {
         if (!☃x.getValue().isObtained()) {
            ☃.add(☃x.getKey());
         }
      }

      return ☃;
   }

   public Iterable<String> getCompletedCriteria() {
      List<String> ☃ = Lists.newArrayList();

      for (Entry<String, CriterionProgress> ☃x : this.criteria.entrySet()) {
         if (☃x.getValue().isObtained()) {
            ☃.add(☃x.getKey());
         }
      }

      return ☃;
   }

   @Nullable
   public Date getFirstProgressDate() {
      Date ☃ = null;

      for (CriterionProgress ☃x : this.criteria.values()) {
         if (☃x.isObtained() && (☃ == null || ☃x.getObtained().before(☃))) {
            ☃ = ☃x.getObtained();
         }
      }

      return ☃;
   }

   public int compareTo(AdvancementProgress var1) {
      Date ☃ = this.getFirstProgressDate();
      Date ☃x = ☃.getFirstProgressDate();
      if (☃ == null && ☃x != null) {
         return 1;
      } else if (☃ != null && ☃x == null) {
         return -1;
      } else {
         return ☃ == null && ☃x == null ? 0 : ☃.compareTo(☃x);
      }
   }

   public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {
      public JsonElement serialize(AdvancementProgress var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         JsonObject ☃x = new JsonObject();

         for (Entry<String, CriterionProgress> ☃xx : ☃.criteria.entrySet()) {
            CriterionProgress ☃xxx = ☃xx.getValue();
            if (☃xxx.isObtained()) {
               ☃x.add(☃xx.getKey(), ☃xxx.serialize());
            }
         }

         if (!☃x.entrySet().isEmpty()) {
            ☃.add("criteria", ☃x);
         }

         ☃.addProperty("done", ☃.isDone());
         return ☃;
      }

      public AdvancementProgress deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "advancement");
         JsonObject ☃x = JsonUtils.getJsonObject(☃, "criteria", new JsonObject());
         AdvancementProgress ☃xx = new AdvancementProgress();

         for (Entry<String, JsonElement> ☃xxx : ☃x.entrySet()) {
            String ☃xxxx = ☃xxx.getKey();
            ☃xx.criteria.put(☃xxxx, CriterionProgress.fromDateTime(☃xx, JsonUtils.getString(☃xxx.getValue(), ☃xxxx)));
         }

         return ☃xx;
      }
   }
}
