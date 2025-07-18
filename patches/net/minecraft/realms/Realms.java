package net.minecraft.realms;

import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Session;
import net.minecraft.world.GameType;

public class Realms {
   public static boolean isTouchScreen() {
      return Minecraft.getMinecraft().gameSettings.touchscreen;
   }

   public static Proxy getProxy() {
      return Minecraft.getMinecraft().getProxy();
   }

   public static String sessionId() {
      Session ☃ = Minecraft.getMinecraft().getSession();
      return ☃ == null ? null : ☃.getSessionID();
   }

   public static String userName() {
      Session ☃ = Minecraft.getMinecraft().getSession();
      return ☃ == null ? null : ☃.getUsername();
   }

   public static long currentTimeMillis() {
      return Minecraft.getSystemTime();
   }

   public static String getSessionId() {
      return Minecraft.getMinecraft().getSession().getSessionID();
   }

   public static String getUUID() {
      return Minecraft.getMinecraft().getSession().getPlayerID();
   }

   public static String getName() {
      return Minecraft.getMinecraft().getSession().getUsername();
   }

   public static String uuidToName(String var0) {
      return Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(☃), null), false).getName();
   }

   public static void setScreen(RealmsScreen var0) {
      Minecraft.getMinecraft().displayGuiScreen(☃.getProxy());
   }

   public static String getGameDirectoryPath() {
      return Minecraft.getMinecraft().gameDir.getAbsolutePath();
   }

   public static int survivalId() {
      return GameType.SURVIVAL.getID();
   }

   public static int creativeId() {
      return GameType.CREATIVE.getID();
   }

   public static int adventureId() {
      return GameType.ADVENTURE.getID();
   }

   public static int spectatorId() {
      return GameType.SPECTATOR.getID();
   }

   public static void setConnectedToRealms(boolean var0) {
      Minecraft.getMinecraft().setConnectedToRealms(☃);
   }

   public static ListenableFuture<Object> downloadResourcePack(String var0, String var1) {
      return Minecraft.getMinecraft().getResourcePackRepository().downloadResourcePack(☃, ☃);
   }

   public static void clearResourcePack() {
      Minecraft.getMinecraft().getResourcePackRepository().clearResourcePack();
   }

   public static boolean getRealmsNotificationsEnabled() {
      return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS);
   }

   public static boolean inTitleScreen() {
      return Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu;
   }

   public static void deletePlayerTag(File var0) {
      if (☃.exists()) {
         try {
            NBTTagCompound ☃ = CompressedStreamTools.readCompressed(new FileInputStream(☃));
            NBTTagCompound ☃x = ☃.getCompoundTag("Data");
            ☃x.removeTag("Player");
            CompressedStreamTools.writeCompressed(☃, new FileOutputStream(☃));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }
}
