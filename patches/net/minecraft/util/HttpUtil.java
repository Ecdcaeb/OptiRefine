package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
   public static final ListeningExecutorService DOWNLOADER_EXECUTOR = MoreExecutors.listeningDecorator(
      Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build())
   );
   private static final AtomicInteger DOWNLOAD_THREADS_STARTED = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();

   public static String buildPostString(Map<String, Object> var0) {
      StringBuilder ☃ = new StringBuilder();

      for (Entry<String, Object> ☃x : ☃.entrySet()) {
         if (☃.length() > 0) {
            ☃.append('&');
         }

         try {
            ☃.append(URLEncoder.encode(☃x.getKey(), "UTF-8"));
         } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
         }

         if (☃x.getValue() != null) {
            ☃.append('=');

            try {
               ☃.append(URLEncoder.encode(☃x.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException var5) {
               var5.printStackTrace();
            }
         }
      }

      return ☃.toString();
   }

   public static String postMap(URL var0, Map<String, Object> var1, boolean var2, @Nullable Proxy var3) {
      return post(☃, buildPostString(☃), ☃, ☃);
   }

   private static String post(URL var0, String var1, boolean var2, @Nullable Proxy var3) {
      try {
         if (☃ == null) {
            ☃ = Proxy.NO_PROXY;
         }

         HttpURLConnection ☃ = (HttpURLConnection)☃.openConnection(☃);
         ☃.setRequestMethod("POST");
         ☃.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         ☃.setRequestProperty("Content-Length", "" + ☃.getBytes().length);
         ☃.setRequestProperty("Content-Language", "en-US");
         ☃.setUseCaches(false);
         ☃.setDoInput(true);
         ☃.setDoOutput(true);
         DataOutputStream ☃x = new DataOutputStream(☃.getOutputStream());
         ☃x.writeBytes(☃);
         ☃x.flush();
         ☃x.close();
         BufferedReader ☃xx = new BufferedReader(new InputStreamReader(☃.getInputStream()));
         StringBuffer ☃xxx = new StringBuffer();

         String ☃xxxx;
         while ((☃xxxx = ☃xx.readLine()) != null) {
            ☃xxx.append(☃xxxx);
            ☃xxx.append('\r');
         }

         ☃xx.close();
         return ☃xxx.toString();
      } catch (Exception var9) {
         if (!☃) {
            LOGGER.error("Could not post to {}", ☃, var9);
         }

         return "";
      }
   }

   public static ListenableFuture<Object> downloadResourcePack(
      final File var0, final String var1, final Map<String, String> var2, final int var3, @Nullable final IProgressUpdate var4, final Proxy var5
   ) {
      return DOWNLOADER_EXECUTOR.submit(new Runnable() {
         @Override
         public void run() {
            HttpURLConnection ☃ = null;
            InputStream ☃x = null;
            OutputStream ☃xx = null;
            if (☃ != null) {
               ☃.resetProgressAndMessage(I18n.translateToLocal("resourcepack.downloading"));
               ☃.displayLoadingString(I18n.translateToLocal("resourcepack.requesting"));
            }

            try {
               byte[] ☃xxx = new byte[4096];
               URL ☃xxxx = new URL(☃);
               ☃ = (HttpURLConnection)☃xxxx.openConnection(☃);
               ☃.setInstanceFollowRedirects(true);
               float ☃xxxxx = 0.0F;
               float ☃xxxxxx = ☃.entrySet().size();

               for (Entry<String, String> ☃xxxxxxx : ☃.entrySet()) {
                  ☃.setRequestProperty(☃xxxxxxx.getKey(), ☃xxxxxxx.getValue());
                  if (☃ != null) {
                     ☃.setLoadingProgress((int)(++☃xxxxx / ☃xxxxxx * 100.0F));
                  }
               }

               ☃x = ☃.getInputStream();
               ☃xxxxxx = ☃.getContentLength();
               int ☃xxxxxxxx = ☃.getContentLength();
               if (☃ != null) {
                  ☃.displayLoadingString(I18n.translateToLocalFormatted("resourcepack.progress", String.format("%.2f", ☃xxxxxx / 1000.0F / 1000.0F)));
               }

               if (☃.exists()) {
                  long ☃xxxxxxxxx = ☃.length();
                  if (☃xxxxxxxxx == ☃xxxxxxxx) {
                     if (☃ != null) {
                        ☃.setDoneWorking();
                     }

                     return;
                  }

                  HttpUtil.LOGGER.warn("Deleting {} as it does not match what we currently have ({} vs our {}).", ☃, ☃xxxxxxxx, ☃xxxxxxxxx);
                  FileUtils.deleteQuietly(☃);
               } else if (☃.getParentFile() != null) {
                  ☃.getParentFile().mkdirs();
               }

               ☃xx = new DataOutputStream(new FileOutputStream(☃));
               if (☃ > 0 && ☃xxxxxx > ☃) {
                  if (☃ != null) {
                     ☃.setDoneWorking();
                  }

                  throw new IOException("Filesize is bigger than maximum allowed (file is " + ☃xxxxx + ", limit is " + ☃ + ")");
               }

               while (true) {
                  int ☃xxxxxxxxx;
                  if ((☃xxxxxxxxx = ☃x.read(☃xxx)) < 0) {
                     if (☃ != null) {
                        ☃.setDoneWorking();
                     }

                     return;
                  }

                  ☃xxxxx += ☃xxxxxxxxx;
                  if (☃ != null) {
                     ☃.setLoadingProgress((int)(☃xxxxx / ☃xxxxxx * 100.0F));
                  }

                  if (☃ > 0 && ☃xxxxx > ☃) {
                     if (☃ != null) {
                        ☃.setDoneWorking();
                     }

                     throw new IOException("Filesize was bigger than maximum allowed (got >= " + ☃xxxxx + ", limit was " + ☃ + ")");
                  }

                  if (Thread.interrupted()) {
                     HttpUtil.LOGGER.error("INTERRUPTED");
                     if (☃ != null) {
                        ☃.setDoneWorking();
                     }
                     break;
                  }

                  ☃xx.write(☃xxx, 0, ☃xxxxxxxxx);
               }
            } catch (Throwable var16) {
               var16.printStackTrace();
               if (☃ != null) {
                  InputStream ☃xxx = ☃.getErrorStream();

                  try {
                     HttpUtil.LOGGER.error(IOUtils.toString(☃xxx));
                  } catch (IOException var15) {
                     var15.printStackTrace();
                  }
               }

               if (☃ != null) {
                  ☃.setDoneWorking();
               }

               return;
            } finally {
               IOUtils.closeQuietly(☃x);
               IOUtils.closeQuietly(☃xx);
            }
         }
      });
   }

   public static int getSuitableLanPort() throws IOException {
      ServerSocket ☃ = null;
      int ☃x = -1;

      try {
         ☃ = new ServerSocket(0);
         ☃x = ☃.getLocalPort();
      } finally {
         try {
            if (☃ != null) {
               ☃.close();
            }
         } catch (IOException var8) {
         }
      }

      return ☃x;
   }
}
