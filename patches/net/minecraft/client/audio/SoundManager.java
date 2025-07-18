package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
   private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.newHashSet();
   private final SoundHandler sndHandler;
   private final GameSettings options;
   private SoundManager.SoundSystemStarterThread sndSystem;
   private boolean loaded;
   private int playTime;
   private final Map<String, ISound> playingSounds = HashBiMap.create();
   private final Map<ISound, String> invPlayingSounds = ((BiMap)this.playingSounds).inverse();
   private final Multimap<SoundCategory, String> categorySounds = HashMultimap.create();
   private final List<ITickableSound> tickableSounds = Lists.newArrayList();
   private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
   private final Map<String, Integer> playingSoundsStopTime = Maps.newHashMap();
   private final List<ISoundEventListener> listeners = Lists.newArrayList();
   private final List<String> pausedChannels = Lists.newArrayList();

   public SoundManager(SoundHandler var1, GameSettings var2) {
      this.sndHandler = ☃;
      this.options = ☃;

      try {
         SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
         SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
      } catch (SoundSystemException var4) {
         LOGGER.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", var4);
      }
   }

   public void reloadSoundSystem() {
      UNABLE_TO_PLAY.clear();

      for (SoundEvent ☃ : SoundEvent.REGISTRY) {
         ResourceLocation ☃x = ☃.getSoundName();
         if (this.sndHandler.getAccessor(☃x) == null) {
            LOGGER.warn("Missing sound for event: {}", SoundEvent.REGISTRY.getNameForObject(☃));
            UNABLE_TO_PLAY.add(☃x);
         }
      }

      this.unloadSoundSystem();
      this.loadSoundSystem();
   }

   private synchronized void loadSoundSystem() {
      if (!this.loaded) {
         try {
            new Thread(new Runnable() {
               @Override
               public void run() {
                  SoundSystemConfig.setLogger(new SoundSystemLogger() {
                     public void message(String var1, int var2x) {
                        if (!☃.isEmpty()) {
                           SoundManager.LOGGER.info(☃);
                        }
                     }

                     public void importantMessage(String var1, int var2x) {
                        if (!☃.isEmpty()) {
                           SoundManager.LOGGER.warn(☃);
                        }
                     }

                     public void errorMessage(String var1, String var2x, int var3) {
                        if (!var2x.isEmpty()) {
                           SoundManager.LOGGER.error("Error in class '{}'", ☃);
                           SoundManager.LOGGER.error(var2x);
                        }
                     }
                  });
                  SoundManager.this.sndSystem = SoundManager.this.new SoundSystemStarterThread();
                  SoundManager.this.loaded = true;
                  SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                  SoundManager.LOGGER.info(SoundManager.LOG_MARKER, "Sound engine started");
               }
            }, "Sound Library Loader").start();
         } catch (RuntimeException var2) {
            LOGGER.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", var2);
            this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
            this.options.saveOptions();
         }
      }
   }

   private float getVolume(SoundCategory var1) {
      return ☃ != null && ☃ != SoundCategory.MASTER ? this.options.getSoundLevel(☃) : 1.0F;
   }

   public void setVolume(SoundCategory var1, float var2) {
      if (this.loaded) {
         if (☃ == SoundCategory.MASTER) {
            this.sndSystem.setMasterVolume(☃);
         } else {
            for (String ☃ : this.categorySounds.get(☃)) {
               ISound ☃x = this.playingSounds.get(☃);
               float ☃xx = this.getClampedVolume(☃x);
               if (☃xx <= 0.0F) {
                  this.stopSound(☃x);
               } else {
                  this.sndSystem.setVolume(☃, ☃xx);
               }
            }
         }
      }
   }

   public void unloadSoundSystem() {
      if (this.loaded) {
         this.stopAllSounds();
         this.sndSystem.cleanup();
         this.loaded = false;
      }
   }

   public void stopAllSounds() {
      if (this.loaded) {
         for (String ☃ : this.playingSounds.keySet()) {
            this.sndSystem.stop(☃);
         }

         this.playingSounds.clear();
         this.delayedSounds.clear();
         this.tickableSounds.clear();
         this.categorySounds.clear();
         this.playingSoundsStopTime.clear();
      }
   }

   public void addListener(ISoundEventListener var1) {
      this.listeners.add(☃);
   }

   public void removeListener(ISoundEventListener var1) {
      this.listeners.remove(☃);
   }

   public void updateAllSounds() {
      this.playTime++;

      for (ITickableSound ☃ : this.tickableSounds) {
         ☃.update();
         if (☃.isDonePlaying()) {
            this.stopSound(☃);
         } else {
            String ☃x = this.invPlayingSounds.get(☃);
            this.sndSystem.setVolume(☃x, this.getClampedVolume(☃));
            this.sndSystem.setPitch(☃x, this.getClampedPitch(☃));
            this.sndSystem.setPosition(☃x, ☃.getXPosF(), ☃.getYPosF(), ☃.getZPosF());
         }
      }

      Iterator<Entry<String, ISound>> ☃x = this.playingSounds.entrySet().iterator();

      while (☃x.hasNext()) {
         Entry<String, ISound> ☃xx = ☃x.next();
         String ☃xxx = ☃xx.getKey();
         ISound ☃xxxx = ☃xx.getValue();
         if (!this.sndSystem.playing(☃xxx)) {
            int ☃xxxxx = this.playingSoundsStopTime.get(☃xxx);
            if (☃xxxxx <= this.playTime) {
               int ☃xxxxxx = ☃xxxx.getRepeatDelay();
               if (☃xxxx.canRepeat() && ☃xxxxxx > 0) {
                  this.delayedSounds.put(☃xxxx, this.playTime + ☃xxxxxx);
               }

               ☃x.remove();
               LOGGER.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", ☃xxx);
               this.sndSystem.removeSource(☃xxx);
               this.playingSoundsStopTime.remove(☃xxx);

               try {
                  this.categorySounds.remove(☃xxxx.getCategory(), ☃xxx);
               } catch (RuntimeException var8) {
               }

               if (☃xxxx instanceof ITickableSound) {
                  this.tickableSounds.remove(☃xxxx);
               }
            }
         }
      }

      Iterator<Entry<ISound, Integer>> ☃xx = this.delayedSounds.entrySet().iterator();

      while (☃xx.hasNext()) {
         Entry<ISound, Integer> ☃xxx = ☃xx.next();
         if (this.playTime >= ☃xxx.getValue()) {
            ISound ☃xxxx = ☃xxx.getKey();
            if (☃xxxx instanceof ITickableSound) {
               ((ITickableSound)☃xxxx).update();
            }

            this.playSound(☃xxxx);
            ☃xx.remove();
         }
      }
   }

   public boolean isSoundPlaying(ISound var1) {
      if (!this.loaded) {
         return false;
      } else {
         String ☃ = this.invPlayingSounds.get(☃);
         return ☃ == null
            ? false
            : this.sndSystem.playing(☃) || this.playingSoundsStopTime.containsKey(☃) && this.playingSoundsStopTime.get(☃) <= this.playTime;
      }
   }

   public void stopSound(ISound var1) {
      if (this.loaded) {
         String ☃ = this.invPlayingSounds.get(☃);
         if (☃ != null) {
            this.sndSystem.stop(☃);
         }
      }
   }

   public void playSound(ISound var1) {
      if (this.loaded) {
         SoundEventAccessor ☃ = ☃.createAccessor(this.sndHandler);
         ResourceLocation ☃x = ☃.getSoundLocation();
         if (☃ == null) {
            if (UNABLE_TO_PLAY.add(☃x)) {
               LOGGER.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", ☃x);
            }
         } else {
            if (!this.listeners.isEmpty()) {
               for (ISoundEventListener ☃xx : this.listeners) {
                  ☃xx.soundPlay(☃, ☃);
               }
            }

            if (this.sndSystem.getMasterVolume() <= 0.0F) {
               LOGGER.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", ☃x);
            } else {
               Sound ☃xx = ☃.getSound();
               if (☃xx == SoundHandler.MISSING_SOUND) {
                  if (UNABLE_TO_PLAY.add(☃x)) {
                     LOGGER.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", ☃x);
                  }
               } else {
                  float ☃xxx = ☃.getVolume();
                  float ☃xxxx = 16.0F;
                  if (☃xxx > 1.0F) {
                     ☃xxxx *= ☃xxx;
                  }

                  SoundCategory ☃xxxxx = ☃.getCategory();
                  float ☃xxxxxx = this.getClampedVolume(☃);
                  float ☃xxxxxxx = this.getClampedPitch(☃);
                  if (☃xxxxxx == 0.0F) {
                     LOGGER.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", ☃xx.getSoundLocation());
                  } else {
                     boolean ☃xxxxxxxx = ☃.canRepeat() && ☃.getRepeatDelay() == 0;
                     String ☃xxxxxxxxx = MathHelper.getRandomUUID(ThreadLocalRandom.current()).toString();
                     ResourceLocation ☃xxxxxxxxxx = ☃xx.getSoundAsOggLocation();
                     if (☃xx.isStreaming()) {
                        this.sndSystem
                           .newStreamingSource(
                              false,
                              ☃xxxxxxxxx,
                              getURLForSoundResource(☃xxxxxxxxxx),
                              ☃xxxxxxxxxx.toString(),
                              ☃xxxxxxxx,
                              ☃.getXPosF(),
                              ☃.getYPosF(),
                              ☃.getZPosF(),
                              ☃.getAttenuationType().getTypeInt(),
                              ☃xxxx
                           );
                     } else {
                        this.sndSystem
                           .newSource(
                              false,
                              ☃xxxxxxxxx,
                              getURLForSoundResource(☃xxxxxxxxxx),
                              ☃xxxxxxxxxx.toString(),
                              ☃xxxxxxxx,
                              ☃.getXPosF(),
                              ☃.getYPosF(),
                              ☃.getZPosF(),
                              ☃.getAttenuationType().getTypeInt(),
                              ☃xxxx
                           );
                     }

                     LOGGER.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", ☃xx.getSoundLocation(), ☃x, ☃xxxxxxxxx);
                     this.sndSystem.setPitch(☃xxxxxxxxx, ☃xxxxxxx);
                     this.sndSystem.setVolume(☃xxxxxxxxx, ☃xxxxxx);
                     this.sndSystem.play(☃xxxxxxxxx);
                     this.playingSoundsStopTime.put(☃xxxxxxxxx, this.playTime + 20);
                     this.playingSounds.put(☃xxxxxxxxx, ☃);
                     this.categorySounds.put(☃xxxxx, ☃xxxxxxxxx);
                     if (☃ instanceof ITickableSound) {
                        this.tickableSounds.add((ITickableSound)☃);
                     }
                  }
               }
            }
         }
      }
   }

   private float getClampedPitch(ISound var1) {
      return MathHelper.clamp(☃.getPitch(), 0.5F, 2.0F);
   }

   private float getClampedVolume(ISound var1) {
      return MathHelper.clamp(☃.getVolume() * this.getVolume(☃.getCategory()), 0.0F, 1.0F);
   }

   public void pauseAllSounds() {
      for (Entry<String, ISound> ☃ : this.playingSounds.entrySet()) {
         String ☃x = ☃.getKey();
         boolean ☃xx = this.isSoundPlaying(☃.getValue());
         if (☃xx) {
            LOGGER.debug(LOG_MARKER, "Pausing channel {}", ☃x);
            this.sndSystem.pause(☃x);
            this.pausedChannels.add(☃x);
         }
      }
   }

   public void resumeAllSounds() {
      for (String ☃ : this.pausedChannels) {
         LOGGER.debug(LOG_MARKER, "Resuming channel {}", ☃);
         this.sndSystem.play(☃);
      }

      this.pausedChannels.clear();
   }

   public void playDelayedSound(ISound var1, int var2) {
      this.delayedSounds.put(☃, this.playTime + ☃);
   }

   private static URL getURLForSoundResource(final ResourceLocation var0) {
      String ☃ = String.format("%s:%s:%s", "mcsounddomain", ☃.getNamespace(), ☃.getPath());
      URLStreamHandler ☃x = new URLStreamHandler() {
         @Override
         protected URLConnection openConnection(URL var1) {
            return new URLConnection(☃) {
               @Override
               public void connect() {
               }

               @Override
               public InputStream getInputStream() throws IOException {
                  return Minecraft.getMinecraft().getResourceManager().getResource(☃).getInputStream();
               }
            };
         }
      };

      try {
         return new URL(null, ☃, ☃x);
      } catch (MalformedURLException var4) {
         throw new Error("TODO: Sanely handle url exception! :D");
      }
   }

   public void setListener(EntityPlayer var1, float var2) {
      if (this.loaded && ☃ != null) {
         float ☃ = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
         float ☃x = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
         double ☃xx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
         double ☃xxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃ + ☃.getEyeHeight();
         double ☃xxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
         float ☃xxxxx = MathHelper.cos((☃x + 90.0F) * (float) (Math.PI / 180.0));
         float ☃xxxxxx = MathHelper.sin((☃x + 90.0F) * (float) (Math.PI / 180.0));
         float ☃xxxxxxx = MathHelper.cos(-☃ * (float) (Math.PI / 180.0));
         float ☃xxxxxxxx = MathHelper.sin(-☃ * (float) (Math.PI / 180.0));
         float ☃xxxxxxxxx = MathHelper.cos((-☃ + 90.0F) * (float) (Math.PI / 180.0));
         float ☃xxxxxxxxxx = MathHelper.sin((-☃ + 90.0F) * (float) (Math.PI / 180.0));
         float ☃xxxxxxxxxxx = ☃xxxxx * ☃xxxxxxx;
         float ☃xxxxxxxxxxxx = ☃xxxxxx * ☃xxxxxxx;
         float ☃xxxxxxxxxxxxx = ☃xxxxx * ☃xxxxxxxxx;
         float ☃xxxxxxxxxxxxxx = ☃xxxxxx * ☃xxxxxxxxx;
         this.sndSystem.setListenerPosition((float)☃xx, (float)☃xxx, (float)☃xxxx);
         this.sndSystem.setListenerOrientation(☃xxxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx);
      }
   }

   public void stop(String var1, SoundCategory var2) {
      if (☃ != null) {
         for (String ☃ : this.categorySounds.get(☃)) {
            ISound ☃x = this.playingSounds.get(☃);
            if (☃.isEmpty()) {
               this.stopSound(☃x);
            } else if (☃x.getSoundLocation().equals(new ResourceLocation(☃))) {
               this.stopSound(☃x);
            }
         }
      } else if (☃.isEmpty()) {
         this.stopAllSounds();
      } else {
         for (ISound ☃x : this.playingSounds.values()) {
            if (☃x.getSoundLocation().equals(new ResourceLocation(☃))) {
               this.stopSound(☃x);
            }
         }
      }
   }

   class SoundSystemStarterThread extends SoundSystem {
      private SoundSystemStarterThread() {
      }

      public boolean playing(String var1) {
         synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary == null) {
               return false;
            } else {
               Source ☃ = (Source)this.soundLibrary.getSources().get(☃);
               return ☃ == null ? false : ☃.playing() || ☃.paused() || ☃.preLoad;
            }
         }
      }
   }
}
