package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
   public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
   public static final File OLD_OPS_FILE = new File("ops.txt");
   public static final File OLD_WHITELIST_FILE = new File("white-list.txt");

   private static void lookupNames(MinecraftServer var0, Collection<String> var1, ProfileLookupCallback var2) {
      String[] ☃ = (String[])Iterators.toArray(Iterators.filter(☃.iterator(), new Predicate<String>() {
         public boolean apply(@Nullable String var1) {
            return !StringUtils.isNullOrEmpty(☃);
         }
      }), String.class);
      if (☃.isServerInOnlineMode()) {
         ☃.getGameProfileRepository().findProfilesByNames(☃, Agent.MINECRAFT, ☃);
      } else {
         for (String ☃x : ☃) {
            UUID ☃xx = EntityPlayer.getUUID(new GameProfile(null, ☃x));
            GameProfile ☃xxx = new GameProfile(☃xx, ☃x);
            ☃.onProfileLookupSucceeded(☃xxx);
         }
      }
   }

   public static String convertMobOwnerIfNeeded(final MinecraftServer var0, String var1) {
      if (!StringUtils.isNullOrEmpty(☃) && ☃.length() <= 16) {
         GameProfile ☃ = ☃.getPlayerProfileCache().getGameProfileForUsername(☃);
         if (☃ != null && ☃.getId() != null) {
            return ☃.getId().toString();
         } else if (!☃.isSinglePlayer() && ☃.isServerInOnlineMode()) {
            final List<GameProfile> ☃x = Lists.newArrayList();
            ProfileLookupCallback ☃xx = new ProfileLookupCallback() {
               public void onProfileLookupSucceeded(GameProfile var1) {
                  ☃.getPlayerProfileCache().addEntry(☃);
                  ☃.add(☃);
               }

               public void onProfileLookupFailed(GameProfile var1, Exception var2) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", ☃.getName(), ☃);
               }
            };
            lookupNames(☃, Lists.newArrayList(new String[]{☃}), ☃xx);
            return !☃x.isEmpty() && ☃x.get(0).getId() != null ? ☃x.get(0).getId().toString() : "";
         } else {
            return EntityPlayer.getUUID(new GameProfile(null, ☃)).toString();
         }
      } else {
         return ☃;
      }
   }
}
