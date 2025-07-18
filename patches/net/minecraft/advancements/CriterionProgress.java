package net.minecraft.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.network.PacketBuffer;

public class CriterionProgress {
   private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   private final AdvancementProgress advancementProgress;
   private Date obtained;

   public CriterionProgress(AdvancementProgress var1) {
      this.advancementProgress = ☃;
   }

   public boolean isObtained() {
      return this.obtained != null;
   }

   public void obtain() {
      this.obtained = new Date();
   }

   public void reset() {
      this.obtained = null;
   }

   public Date getObtained() {
      return this.obtained;
   }

   @Override
   public String toString() {
      return "CriterionProgress{obtained=" + (this.obtained == null ? "false" : this.obtained) + '}';
   }

   public void write(PacketBuffer var1) {
      ☃.writeBoolean(this.obtained != null);
      if (this.obtained != null) {
         ☃.writeTime(this.obtained);
      }
   }

   public JsonElement serialize() {
      return (JsonElement)(this.obtained != null ? new JsonPrimitive(DATE_TIME_FORMATTER.format(this.obtained)) : JsonNull.INSTANCE);
   }

   public static CriterionProgress read(PacketBuffer var0, AdvancementProgress var1) {
      CriterionProgress ☃ = new CriterionProgress(☃);
      if (☃.readBoolean()) {
         ☃.obtained = ☃.readTime();
      }

      return ☃;
   }

   public static CriterionProgress fromDateTime(AdvancementProgress var0, String var1) {
      CriterionProgress ☃ = new CriterionProgress(☃);

      try {
         ☃.obtained = DATE_TIME_FORMATTER.parse(☃);
         return ☃;
      } catch (ParseException var4) {
         throw new JsonSyntaxException("Invalid datetime: " + ☃, var4);
      }
   }
}
