package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Session;

public class Main {
   public static void main(String[] var0) {
      OptionParser ☃ = new OptionParser();
      ☃.allowsUnrecognizedOptions();
      ☃.accepts("demo");
      ☃.accepts("fullscreen");
      ☃.accepts("checkGlErrors");
      OptionSpec<String> ☃x = ☃.accepts("server").withRequiredArg();
      OptionSpec<Integer> ☃xx = ☃.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565, new Integer[0]);
      OptionSpec<File> ☃xxx = ☃.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
      OptionSpec<File> ☃xxxx = ☃.accepts("assetsDir").withRequiredArg().ofType(File.class);
      OptionSpec<File> ☃xxxxx = ☃.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
      OptionSpec<String> ☃xxxxxx = ☃.accepts("proxyHost").withRequiredArg();
      OptionSpec<Integer> ☃xxxxxxx = ☃.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
      OptionSpec<String> ☃xxxxxxxx = ☃.accepts("proxyUser").withRequiredArg();
      OptionSpec<String> ☃xxxxxxxxx = ☃.accepts("proxyPass").withRequiredArg();
      OptionSpec<String> ☃xxxxxxxxxx = ☃.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
      OptionSpec<String> ☃xxxxxxxxxxx = ☃.accepts("uuid").withRequiredArg();
      OptionSpec<String> ☃xxxxxxxxxxxx = ☃.accepts("accessToken").withRequiredArg().required();
      OptionSpec<String> ☃xxxxxxxxxxxxx = ☃.accepts("version").withRequiredArg().required();
      OptionSpec<Integer> ☃xxxxxxxxxxxxxx = ☃.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854, new Integer[0]);
      OptionSpec<Integer> ☃xxxxxxxxxxxxxxx = ☃.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480, new Integer[0]);
      OptionSpec<String> ☃xxxxxxxxxxxxxxxx = ☃.accepts("userProperties").withRequiredArg().defaultsTo("{}", new String[0]);
      OptionSpec<String> ☃xxxxxxxxxxxxxxxxx = ☃.accepts("profileProperties").withRequiredArg().defaultsTo("{}", new String[0]);
      OptionSpec<String> ☃xxxxxxxxxxxxxxxxxx = ☃.accepts("assetIndex").withRequiredArg();
      OptionSpec<String> ☃xxxxxxxxxxxxxxxxxxx = ☃.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
      OptionSpec<String> ☃xxxxxxxxxxxxxxxxxxxx = ☃.accepts("versionType").withRequiredArg().defaultsTo("release", new String[0]);
      OptionSpec<String> ☃xxxxxxxxxxxxxxxxxxxxx = ☃.nonOptions();
      OptionSet ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.parse(☃);
      List<String> ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.valuesOf(☃xxxxxxxxxxxxxxxxxxxxx);
      if (!☃xxxxxxxxxxxxxxxxxxxxxxx.isEmpty()) {
         System.out.println("Completely ignored arguments: " + ☃xxxxxxxxxxxxxxxxxxxxxxx);
      }

      String ☃xxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxx);
      Proxy ☃xxxxxxxxxxxxxxxxxxxxxxxxx = Proxy.NO_PROXY;
      if (☃xxxxxxxxxxxxxxxxxxxxxxxx != null) {
         try {
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx = new Proxy(
               Type.SOCKS, new InetSocketAddress(☃xxxxxxxxxxxxxxxxxxxxxxxx, (Integer)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxx))
            );
         } catch (Exception var48) {
         }
      }

      final String ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxx);
      final String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxx);
      if (!☃xxxxxxxxxxxxxxxxxxxxxxxxx.equals(Proxy.NO_PROXY) && isNotEmpty(☃xxxxxxxxxxxxxxxxxxxxxxxxxx) && isNotEmpty(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)) {
         Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(☃, ☃.toCharArray());
            }
         });
      }

      int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = (Integer)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxxx);
      int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (Integer)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxxxx);
      boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has("fullscreen");
      boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has("checkGlErrors");
      boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has("demo");
      String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxx);
      Gson ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new GsonBuilder().registerTypeAdapter(PropertyMap.class, new Serializer()).create();
      PropertyMap ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = JsonUtils.gsonDeserialize(
         ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxxxxx), PropertyMap.class
      );
      PropertyMap ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = JsonUtils.gsonDeserialize(
         ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxxxxxx), PropertyMap.class
      );
      String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxxxxxxxxxxxxxxxxx);
      File ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (File)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxx);
      File ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has(☃xxxx)
         ? (File)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxx)
         : new File(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, "assets/");
      File ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has(☃xxxxx)
         ? (File)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xxxxx)
         : new File(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, "resourcepacks/");
      String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has(☃xxxxxxxxxxx)
         ? (String)☃xxxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx)
         : (String)☃xxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx);
      String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.has(☃xxxxxxxxxxxxxxxxxx)
         ? (String)☃xxxxxxxxxxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx)
         : null;
      String ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (String)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃x);
      Integer ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (Integer)☃xxxxxxxxxxxxxxxxxxxxxx.valueOf(☃xx);
      Session ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new Session(
         (String)☃xxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx),
         ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
         (String)☃xxxxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx),
         (String)☃xxxxxxxxxxxxxxxxxxx.value(☃xxxxxxxxxxxxxxxxxxxxxx)
      );
      GameConfiguration ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new GameConfiguration(
         new GameConfiguration.UserInformation(
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx
         ),
         new GameConfiguration.DisplayInformation(
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         ),
         new GameConfiguration.FolderInformation(
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         ),
         new GameConfiguration.GameInformation(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx),
         new GameConfiguration.ServerInformation(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
      );
      Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
         @Override
         public void run() {
            Minecraft.stopIntegratedServer();
         }
      });
      Thread.currentThread().setName("Client thread");
      new Minecraft(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx).run();
   }

   private static boolean isNotEmpty(String var0) {
      return ☃ != null && !☃.isEmpty();
   }
}
