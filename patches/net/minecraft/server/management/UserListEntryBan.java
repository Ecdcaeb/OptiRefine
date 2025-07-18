package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class UserListEntryBan<T> extends UserListEntry<T> {
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   protected final Date banStartDate;
   protected final String bannedBy;
   protected final Date banEndDate;
   protected final String reason;

   public UserListEntryBan(T var1, Date var2, String var3, Date var4, String var5) {
      super(☃);
      this.banStartDate = ☃ == null ? new Date() : ☃;
      this.bannedBy = ☃ == null ? "(Unknown)" : ☃;
      this.banEndDate = ☃;
      this.reason = ☃ == null ? "Banned by an operator." : ☃;
   }

   protected UserListEntryBan(T var1, JsonObject var2) {
      super(☃, ☃);

      Date ☃;
      try {
         ☃ = ☃.has("created") ? DATE_FORMAT.parse(☃.get("created").getAsString()) : new Date();
      } catch (ParseException var7) {
         ☃ = new Date();
      }

      this.banStartDate = ☃;
      this.bannedBy = ☃.has("source") ? ☃.get("source").getAsString() : "(Unknown)";

      Date ☃x;
      try {
         ☃x = ☃.has("expires") ? DATE_FORMAT.parse(☃.get("expires").getAsString()) : null;
      } catch (ParseException var6) {
         ☃x = null;
      }

      this.banEndDate = ☃x;
      this.reason = ☃.has("reason") ? ☃.get("reason").getAsString() : "Banned by an operator.";
   }

   public Date getBanEndDate() {
      return this.banEndDate;
   }

   public String getBanReason() {
      return this.reason;
   }

   @Override
   boolean hasBanExpired() {
      return this.banEndDate == null ? false : this.banEndDate.before(new Date());
   }

   @Override
   protected void onSerialization(JsonObject var1) {
      ☃.addProperty("created", DATE_FORMAT.format(this.banStartDate));
      ☃.addProperty("source", this.bannedBy);
      ☃.addProperty("expires", this.banEndDate == null ? "forever" : DATE_FORMAT.format(this.banEndDate));
      ☃.addProperty("reason", this.reason);
   }
}
