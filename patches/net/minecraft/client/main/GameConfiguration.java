package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Proxy;
import javax.annotation.Nullable;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourceIndexFolder;
import net.minecraft.util.Session;

public class GameConfiguration {
   public final GameConfiguration.UserInformation userInfo;
   public final GameConfiguration.DisplayInformation displayInfo;
   public final GameConfiguration.FolderInformation folderInfo;
   public final GameConfiguration.GameInformation gameInfo;
   public final GameConfiguration.ServerInformation serverInfo;

   public GameConfiguration(
      GameConfiguration.UserInformation var1,
      GameConfiguration.DisplayInformation var2,
      GameConfiguration.FolderInformation var3,
      GameConfiguration.GameInformation var4,
      GameConfiguration.ServerInformation var5
   ) {
      this.userInfo = ☃;
      this.displayInfo = ☃;
      this.folderInfo = ☃;
      this.gameInfo = ☃;
      this.serverInfo = ☃;
   }

   public static class DisplayInformation {
      public final int width;
      public final int height;
      public final boolean fullscreen;
      public final boolean checkGlErrors;

      public DisplayInformation(int var1, int var2, boolean var3, boolean var4) {
         this.width = ☃;
         this.height = ☃;
         this.fullscreen = ☃;
         this.checkGlErrors = ☃;
      }
   }

   public static class FolderInformation {
      public final File gameDir;
      public final File resourcePacksDir;
      public final File assetsDir;
      public final String assetIndex;

      public FolderInformation(File var1, File var2, File var3, @Nullable String var4) {
         this.gameDir = ☃;
         this.resourcePacksDir = ☃;
         this.assetsDir = ☃;
         this.assetIndex = ☃;
      }

      public ResourceIndex getAssetsIndex() {
         return (ResourceIndex)(this.assetIndex == null ? new ResourceIndexFolder(this.assetsDir) : new ResourceIndex(this.assetsDir, this.assetIndex));
      }
   }

   public static class GameInformation {
      public final boolean isDemo;
      public final String version;
      public final String versionType;

      public GameInformation(boolean var1, String var2, String var3) {
         this.isDemo = ☃;
         this.version = ☃;
         this.versionType = ☃;
      }
   }

   public static class ServerInformation {
      public final String serverName;
      public final int serverPort;

      public ServerInformation(String var1, int var2) {
         this.serverName = ☃;
         this.serverPort = ☃;
      }
   }

   public static class UserInformation {
      public final Session session;
      public final PropertyMap userProperties;
      public final PropertyMap profileProperties;
      public final Proxy proxy;

      public UserInformation(Session var1, PropertyMap var2, PropertyMap var3, Proxy var4) {
         this.session = ☃;
         this.userProperties = ☃;
         this.profileProperties = ☃;
         this.proxy = ☃;
      }
   }
}
