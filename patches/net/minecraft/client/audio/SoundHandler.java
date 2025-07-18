package net.minecraft.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, ITickable {
   public static final Sound MISSING_SOUND = new Sound("meta:missing_sound", 1.0F, 1.0F, 1, Sound.Type.FILE, false);
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON = new GsonBuilder()
      .registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer())
      .registerTypeAdapter(SoundList.class, new SoundListSerializer())
      .create();
   private static final ParameterizedType TYPE = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
         return new Type[]{String.class, SoundList.class};
      }

      @Override
      public Type getRawType() {
         return Map.class;
      }

      @Override
      public Type getOwnerType() {
         return null;
      }
   };
   private final SoundRegistry soundRegistry = new SoundRegistry();
   private final SoundManager sndManager;
   private final IResourceManager resourceManager;

   public SoundHandler(IResourceManager var1, GameSettings var2) {
      this.resourceManager = ☃;
      this.sndManager = new SoundManager(this, ☃);
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      this.soundRegistry.clearMap();

      for (String ☃ : ☃.getResourceDomains()) {
         try {
            for (IResource ☃x : ☃.getAllResources(new ResourceLocation(☃, "sounds.json"))) {
               try {
                  Map<String, SoundList> ☃xx = this.getSoundMap(☃x.getInputStream());

                  for (Entry<String, SoundList> ☃xxx : ☃xx.entrySet()) {
                     this.loadSoundResource(new ResourceLocation(☃, ☃xxx.getKey()), ☃xxx.getValue());
                  }
               } catch (RuntimeException var10) {
                  LOGGER.warn("Invalid sounds.json", var10);
               }
            }
         } catch (IOException var11) {
         }
      }

      for (ResourceLocation ☃ : this.soundRegistry.getKeys()) {
         SoundEventAccessor ☃x = this.soundRegistry.getObject(☃);
         if (☃x.getSubtitle() instanceof TextComponentTranslation) {
            String ☃xx = ((TextComponentTranslation)☃x.getSubtitle()).getKey();
            if (!I18n.hasKey(☃xx)) {
               LOGGER.debug("Missing subtitle {} for event: {}", ☃xx, ☃);
            }
         }
      }

      for (ResourceLocation ☃x : this.soundRegistry.getKeys()) {
         if (SoundEvent.REGISTRY.getObject(☃x) == null) {
            LOGGER.debug("Not having sound event for: {}", ☃x);
         }
      }

      this.sndManager.reloadSoundSystem();
   }

   @Nullable
   protected Map<String, SoundList> getSoundMap(InputStream var1) {
      Map var2;
      try {
         var2 = JsonUtils.fromJson(GSON, new InputStreamReader(☃, StandardCharsets.UTF_8), TYPE);
      } finally {
         IOUtils.closeQuietly(☃);
      }

      return var2;
   }

   private void loadSoundResource(ResourceLocation var1, SoundList var2) {
      SoundEventAccessor ☃ = this.soundRegistry.getObject(☃);
      boolean ☃x = ☃ == null;
      if (☃x || ☃.canReplaceExisting()) {
         if (!☃x) {
            LOGGER.debug("Replaced sound event location {}", ☃);
         }

         ☃ = new SoundEventAccessor(☃, ☃.getSubtitle());
         this.soundRegistry.add(☃);
      }

      for (final Sound ☃xx : ☃.getSounds()) {
         final ResourceLocation ☃xxx = ☃xx.getSoundLocation();
         ISoundEventAccessor<Sound> ☃xxxx;
         switch (☃xx.getType()) {
            case FILE:
               if (!this.validateSoundResource(☃xx, ☃)) {
                  continue;
               }

               ☃xxxx = ☃xx;
               break;
            case SOUND_EVENT:
               ☃xxxx = new ISoundEventAccessor<Sound>() {
                  @Override
                  public int getWeight() {
                     SoundEventAccessor ☃xxxxx = SoundHandler.this.soundRegistry.getObject(☃);
                     return ☃xxxxx == null ? 0 : ☃xxxxx.getWeight();
                  }

                  public Sound cloneEntry() {
                     SoundEventAccessor ☃xxxxx = SoundHandler.this.soundRegistry.getObject(☃);
                     if (☃xxxxx == null) {
                        return SoundHandler.MISSING_SOUND;
                     } else {
                        Sound ☃x = ☃xxxxx.cloneEntry();
                        return new Sound(
                           ☃x.getSoundLocation().toString(),
                           ☃x.getVolume() * ☃.getVolume(),
                           ☃x.getPitch() * ☃.getPitch(),
                           ☃.getWeight(),
                           Sound.Type.FILE,
                           ☃x.isStreaming() || ☃.isStreaming()
                        );
                     }
                  }
               };
               break;
            default:
               throw new IllegalStateException("Unknown SoundEventRegistration type: " + ☃xx.getType());
         }

         ☃.addSound(☃xxxx);
      }
   }

   private boolean validateSoundResource(Sound var1, ResourceLocation var2) {
      ResourceLocation ☃ = ☃.getSoundAsOggLocation();
      IResource ☃x = null;

      boolean var6;
      try {
         ☃x = this.resourceManager.getResource(☃);
         ☃x.getInputStream();
         return true;
      } catch (FileNotFoundException var11) {
         LOGGER.warn("File {} does not exist, cannot add it to event {}", ☃, ☃);
         return false;
      } catch (IOException var12) {
         LOGGER.warn("Could not load sound file {}, cannot add it to event {}", ☃, ☃, var12);
         var6 = false;
      } finally {
         IOUtils.closeQuietly(☃x);
      }

      return var6;
   }

   @Nullable
   public SoundEventAccessor getAccessor(ResourceLocation var1) {
      return this.soundRegistry.getObject(☃);
   }

   public void playSound(ISound var1) {
      this.sndManager.playSound(☃);
   }

   public void playDelayedSound(ISound var1, int var2) {
      this.sndManager.playDelayedSound(☃, ☃);
   }

   public void setListener(EntityPlayer var1, float var2) {
      this.sndManager.setListener(☃, ☃);
   }

   public void pauseSounds() {
      this.sndManager.pauseAllSounds();
   }

   public void stopSounds() {
      this.sndManager.stopAllSounds();
   }

   public void unloadSounds() {
      this.sndManager.unloadSoundSystem();
   }

   @Override
   public void update() {
      this.sndManager.updateAllSounds();
   }

   public void resumeSounds() {
      this.sndManager.resumeAllSounds();
   }

   public void setSoundLevel(SoundCategory var1, float var2) {
      if (☃ == SoundCategory.MASTER && ☃ <= 0.0F) {
         this.stopSounds();
      }

      this.sndManager.setVolume(☃, ☃);
   }

   public void stopSound(ISound var1) {
      this.sndManager.stopSound(☃);
   }

   public boolean isSoundPlaying(ISound var1) {
      return this.sndManager.isSoundPlaying(☃);
   }

   public void addListener(ISoundEventListener var1) {
      this.sndManager.addListener(☃);
   }

   public void removeListener(ISoundEventListener var1) {
      this.sndManager.removeListener(☃);
   }

   public void stop(String var1, SoundCategory var2) {
      this.sndManager.stop(☃, ☃);
   }
}
