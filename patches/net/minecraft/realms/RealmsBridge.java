package net.minecraft.realms;

import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsBridge extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private GuiScreen previousScreen;

   public void switchToRealms(GuiScreen var1) {
      this.previousScreen = ☃;

      try {
         Class<?> ☃ = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
         Constructor<?> ☃x = ☃.getDeclaredConstructor(RealmsScreen.class);
         ☃x.setAccessible(true);
         Object ☃xx = ☃x.newInstance(this);
         Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen)☃xx).getProxy());
      } catch (ClassNotFoundException var5) {
         LOGGER.error("Realms module missing");
      } catch (Exception var6) {
         LOGGER.error("Failed to load Realms module", var6);
      }
   }

   public GuiScreenRealmsProxy getNotificationScreen(GuiScreen var1) {
      try {
         this.previousScreen = ☃;
         Class<?> ☃ = Class.forName("com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen");
         Constructor<?> ☃x = ☃.getDeclaredConstructor(RealmsScreen.class);
         ☃x.setAccessible(true);
         Object ☃xx = ☃x.newInstance(this);
         return ((RealmsScreen)☃xx).getProxy();
      } catch (ClassNotFoundException var5) {
         LOGGER.error("Realms module missing");
      } catch (Exception var6) {
         LOGGER.error("Failed to load Realms module", var6);
      }

      return null;
   }

   @Override
   public void init() {
      Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
   }
}
