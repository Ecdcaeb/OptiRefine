package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityList {
   public static final ResourceLocation LIGHTNING_BOLT = new ResourceLocation("lightning_bolt");
   private static final ResourceLocation PLAYER = new ResourceLocation("player");
   private static final Logger LOGGER = LogManager.getLogger();
   public static final RegistryNamespaced<ResourceLocation, Class<? extends Entity>> REGISTRY = new RegistryNamespaced<>();
   public static final Map<ResourceLocation, EntityList.EntityEggInfo> ENTITY_EGGS = Maps.newLinkedHashMap();
   public static final Set<ResourceLocation> KNOWN_TYPES = Sets.newHashSet();
   private static final List<String> OLD_NAMES = Lists.newArrayList();

   @Nullable
   public static ResourceLocation getKey(Entity var0) {
      return getKey((Class<? extends Entity>)☃.getClass());
   }

   @Nullable
   public static ResourceLocation getKey(Class<? extends Entity> var0) {
      return REGISTRY.getNameForObject(☃);
   }

   @Nullable
   public static String getEntityString(Entity var0) {
      int ☃ = REGISTRY.getIDForObject((Class<? extends Entity>)☃.getClass());
      return ☃ == -1 ? null : OLD_NAMES.get(☃);
   }

   @Nullable
   public static String getTranslationName(@Nullable ResourceLocation var0) {
      int ☃ = REGISTRY.getIDForObject(REGISTRY.getObject(☃));
      return ☃ == -1 ? null : OLD_NAMES.get(☃);
   }

   @Nullable
   public static Class<? extends Entity> getClassFromID(int var0) {
      return REGISTRY.getObjectById(☃);
   }

   @Nullable
   public static Class<? extends Entity> getClassFromName(String var0) {
      return REGISTRY.getObject(new ResourceLocation(☃));
   }

   @Nullable
   public static Entity newEntity(@Nullable Class<? extends Entity> var0, World var1) {
      if (☃ == null) {
         return null;
      } else {
         try {
            return ☃.getConstructor(World.class).newInstance(☃);
         } catch (Exception var3) {
            var3.printStackTrace();
            return null;
         }
      }
   }

   @Nullable
   public static Entity createEntityByID(int var0, World var1) {
      return newEntity(getClassFromID(☃), ☃);
   }

   @Nullable
   public static Entity createEntityByIDFromName(ResourceLocation var0, World var1) {
      return newEntity(REGISTRY.getObject(☃), ☃);
   }

   @Nullable
   public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
      ResourceLocation ☃ = new ResourceLocation(☃.getString("id"));
      Entity ☃x = createEntityByIDFromName(☃, ☃);
      if (☃x == null) {
         LOGGER.warn("Skipping Entity with id {}", ☃);
      } else {
         ☃x.readFromNBT(☃);
      }

      return ☃x;
   }

   public static Set<ResourceLocation> getEntityNameList() {
      return KNOWN_TYPES;
   }

   public static boolean isMatchingName(Entity var0, ResourceLocation var1) {
      ResourceLocation ☃ = getKey((Class<? extends Entity>)☃.getClass());
      if (☃ != null) {
         return ☃.equals(☃);
      } else if (☃ instanceof EntityPlayer) {
         return PLAYER.equals(☃);
      } else {
         return ☃ instanceof EntityLightningBolt ? LIGHTNING_BOLT.equals(☃) : false;
      }
   }

   public static boolean isRegistered(ResourceLocation var0) {
      return PLAYER.equals(☃) || getEntityNameList().contains(☃);
   }

   public static String getValidTypeNames() {
      StringBuilder ☃ = new StringBuilder();

      for (ResourceLocation ☃x : getEntityNameList()) {
         ☃.append(☃x).append(", ");
      }

      ☃.append(PLAYER);
      return ☃.toString();
   }

   public static void init() {
      register(1, "item", EntityItem.class, "Item");
      register(2, "xp_orb", EntityXPOrb.class, "XPOrb");
      register(3, "area_effect_cloud", EntityAreaEffectCloud.class, "AreaEffectCloud");
      register(4, "elder_guardian", EntityElderGuardian.class, "ElderGuardian");
      register(5, "wither_skeleton", EntityWitherSkeleton.class, "WitherSkeleton");
      register(6, "stray", EntityStray.class, "Stray");
      register(7, "egg", EntityEgg.class, "ThrownEgg");
      register(8, "leash_knot", EntityLeashKnot.class, "LeashKnot");
      register(9, "painting", EntityPainting.class, "Painting");
      register(10, "arrow", EntityTippedArrow.class, "Arrow");
      register(11, "snowball", EntitySnowball.class, "Snowball");
      register(12, "fireball", EntityLargeFireball.class, "Fireball");
      register(13, "small_fireball", EntitySmallFireball.class, "SmallFireball");
      register(14, "ender_pearl", EntityEnderPearl.class, "ThrownEnderpearl");
      register(15, "eye_of_ender_signal", EntityEnderEye.class, "EyeOfEnderSignal");
      register(16, "potion", EntityPotion.class, "ThrownPotion");
      register(17, "xp_bottle", EntityExpBottle.class, "ThrownExpBottle");
      register(18, "item_frame", EntityItemFrame.class, "ItemFrame");
      register(19, "wither_skull", EntityWitherSkull.class, "WitherSkull");
      register(20, "tnt", EntityTNTPrimed.class, "PrimedTnt");
      register(21, "falling_block", EntityFallingBlock.class, "FallingSand");
      register(22, "fireworks_rocket", EntityFireworkRocket.class, "FireworksRocketEntity");
      register(23, "husk", EntityHusk.class, "Husk");
      register(24, "spectral_arrow", EntitySpectralArrow.class, "SpectralArrow");
      register(25, "shulker_bullet", EntityShulkerBullet.class, "ShulkerBullet");
      register(26, "dragon_fireball", EntityDragonFireball.class, "DragonFireball");
      register(27, "zombie_villager", EntityZombieVillager.class, "ZombieVillager");
      register(28, "skeleton_horse", EntitySkeletonHorse.class, "SkeletonHorse");
      register(29, "zombie_horse", EntityZombieHorse.class, "ZombieHorse");
      register(30, "armor_stand", EntityArmorStand.class, "ArmorStand");
      register(31, "donkey", EntityDonkey.class, "Donkey");
      register(32, "mule", EntityMule.class, "Mule");
      register(33, "evocation_fangs", EntityEvokerFangs.class, "EvocationFangs");
      register(34, "evocation_illager", EntityEvoker.class, "EvocationIllager");
      register(35, "vex", EntityVex.class, "Vex");
      register(36, "vindication_illager", EntityVindicator.class, "VindicationIllager");
      register(37, "illusion_illager", EntityIllusionIllager.class, "IllusionIllager");
      register(40, "commandblock_minecart", EntityMinecartCommandBlock.class, EntityMinecart.Type.COMMAND_BLOCK.getName());
      register(41, "boat", EntityBoat.class, "Boat");
      register(42, "minecart", EntityMinecartEmpty.class, EntityMinecart.Type.RIDEABLE.getName());
      register(43, "chest_minecart", EntityMinecartChest.class, EntityMinecart.Type.CHEST.getName());
      register(44, "furnace_minecart", EntityMinecartFurnace.class, EntityMinecart.Type.FURNACE.getName());
      register(45, "tnt_minecart", EntityMinecartTNT.class, EntityMinecart.Type.TNT.getName());
      register(46, "hopper_minecart", EntityMinecartHopper.class, EntityMinecart.Type.HOPPER.getName());
      register(47, "spawner_minecart", EntityMinecartMobSpawner.class, EntityMinecart.Type.SPAWNER.getName());
      register(50, "creeper", EntityCreeper.class, "Creeper");
      register(51, "skeleton", EntitySkeleton.class, "Skeleton");
      register(52, "spider", EntitySpider.class, "Spider");
      register(53, "giant", EntityGiantZombie.class, "Giant");
      register(54, "zombie", EntityZombie.class, "Zombie");
      register(55, "slime", EntitySlime.class, "Slime");
      register(56, "ghast", EntityGhast.class, "Ghast");
      register(57, "zombie_pigman", EntityPigZombie.class, "PigZombie");
      register(58, "enderman", EntityEnderman.class, "Enderman");
      register(59, "cave_spider", EntityCaveSpider.class, "CaveSpider");
      register(60, "silverfish", EntitySilverfish.class, "Silverfish");
      register(61, "blaze", EntityBlaze.class, "Blaze");
      register(62, "magma_cube", EntityMagmaCube.class, "LavaSlime");
      register(63, "ender_dragon", EntityDragon.class, "EnderDragon");
      register(64, "wither", EntityWither.class, "WitherBoss");
      register(65, "bat", EntityBat.class, "Bat");
      register(66, "witch", EntityWitch.class, "Witch");
      register(67, "endermite", EntityEndermite.class, "Endermite");
      register(68, "guardian", EntityGuardian.class, "Guardian");
      register(69, "shulker", EntityShulker.class, "Shulker");
      register(90, "pig", EntityPig.class, "Pig");
      register(91, "sheep", EntitySheep.class, "Sheep");
      register(92, "cow", EntityCow.class, "Cow");
      register(93, "chicken", EntityChicken.class, "Chicken");
      register(94, "squid", EntitySquid.class, "Squid");
      register(95, "wolf", EntityWolf.class, "Wolf");
      register(96, "mooshroom", EntityMooshroom.class, "MushroomCow");
      register(97, "snowman", EntitySnowman.class, "SnowMan");
      register(98, "ocelot", EntityOcelot.class, "Ozelot");
      register(99, "villager_golem", EntityIronGolem.class, "VillagerGolem");
      register(100, "horse", EntityHorse.class, "Horse");
      register(101, "rabbit", EntityRabbit.class, "Rabbit");
      register(102, "polar_bear", EntityPolarBear.class, "PolarBear");
      register(103, "llama", EntityLlama.class, "Llama");
      register(104, "llama_spit", EntityLlamaSpit.class, "LlamaSpit");
      register(105, "parrot", EntityParrot.class, "Parrot");
      register(120, "villager", EntityVillager.class, "Villager");
      register(200, "ender_crystal", EntityEnderCrystal.class, "EnderCrystal");
      addSpawnInfo("bat", 4996656, 986895);
      addSpawnInfo("blaze", 16167425, 16775294);
      addSpawnInfo("cave_spider", 803406, 11013646);
      addSpawnInfo("chicken", 10592673, 16711680);
      addSpawnInfo("cow", 4470310, 10592673);
      addSpawnInfo("creeper", 894731, 0);
      addSpawnInfo("donkey", 5457209, 8811878);
      addSpawnInfo("elder_guardian", 13552826, 7632531);
      addSpawnInfo("enderman", 1447446, 0);
      addSpawnInfo("endermite", 1447446, 7237230);
      addSpawnInfo("evocation_illager", 9804699, 1973274);
      addSpawnInfo("ghast", 16382457, 12369084);
      addSpawnInfo("guardian", 5931634, 15826224);
      addSpawnInfo("horse", 12623485, 15656192);
      addSpawnInfo("husk", 7958625, 15125652);
      addSpawnInfo("llama", 12623485, 10051392);
      addSpawnInfo("magma_cube", 3407872, 16579584);
      addSpawnInfo("mooshroom", 10489616, 12040119);
      addSpawnInfo("mule", 1769984, 5321501);
      addSpawnInfo("ocelot", 15720061, 5653556);
      addSpawnInfo("parrot", 894731, 16711680);
      addSpawnInfo("pig", 15771042, 14377823);
      addSpawnInfo("polar_bear", 15921906, 9803152);
      addSpawnInfo("rabbit", 10051392, 7555121);
      addSpawnInfo("sheep", 15198183, 16758197);
      addSpawnInfo("shulker", 9725844, 5060690);
      addSpawnInfo("silverfish", 7237230, 3158064);
      addSpawnInfo("skeleton", 12698049, 4802889);
      addSpawnInfo("skeleton_horse", 6842447, 15066584);
      addSpawnInfo("slime", 5349438, 8306542);
      addSpawnInfo("spider", 3419431, 11013646);
      addSpawnInfo("squid", 2243405, 7375001);
      addSpawnInfo("stray", 6387319, 14543594);
      addSpawnInfo("vex", 8032420, 15265265);
      addSpawnInfo("villager", 5651507, 12422002);
      addSpawnInfo("vindication_illager", 9804699, 2580065);
      addSpawnInfo("witch", 3407872, 5349438);
      addSpawnInfo("wither_skeleton", 1315860, 4672845);
      addSpawnInfo("wolf", 14144467, 13545366);
      addSpawnInfo("zombie", 44975, 7969893);
      addSpawnInfo("zombie_horse", 3232308, 9945732);
      addSpawnInfo("zombie_pigman", 15373203, 5009705);
      addSpawnInfo("zombie_villager", 5651507, 7969893);
      KNOWN_TYPES.add(LIGHTNING_BOLT);
   }

   private static void register(int var0, String var1, Class<? extends Entity> var2, String var3) {
      try {
         ☃.getConstructor(World.class);
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException("Invalid class " + ☃ + " no constructor taking " + World.class.getName());
      }

      if ((☃.getModifiers() & 1024) == 1024) {
         throw new RuntimeException("Invalid abstract class " + ☃);
      } else {
         ResourceLocation ☃ = new ResourceLocation(☃);
         REGISTRY.register(☃, ☃, ☃);
         KNOWN_TYPES.add(☃);

         while (OLD_NAMES.size() <= ☃) {
            OLD_NAMES.add(null);
         }

         OLD_NAMES.set(☃, ☃);
      }
   }

   protected static EntityList.EntityEggInfo addSpawnInfo(String var0, int var1, int var2) {
      ResourceLocation ☃ = new ResourceLocation(☃);
      return ENTITY_EGGS.put(☃, new EntityList.EntityEggInfo(☃, ☃, ☃));
   }

   public static class EntityEggInfo {
      public final ResourceLocation spawnedID;
      public final int primaryColor;
      public final int secondaryColor;
      public final StatBase killEntityStat;
      public final StatBase entityKilledByStat;

      public EntityEggInfo(ResourceLocation var1, int var2, int var3) {
         this.spawnedID = ☃;
         this.primaryColor = ☃;
         this.secondaryColor = ☃;
         this.killEntityStat = StatList.getStatKillEntity(this);
         this.entityKilledByStat = StatList.getStatEntityKilledBy(this);
      }
   }
}
