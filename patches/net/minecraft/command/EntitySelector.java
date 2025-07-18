package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class EntitySelector {
   private static final Pattern TOKEN_PATTERN = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
   private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings();
   private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);
   private static final Set<String> VALID_ARGUMENTS = Sets.newHashSet();
   private static final String ARGUMENT_RANGE_MAX = addArgument("r");
   private static final String ARGUMENT_RANGE_MIN = addArgument("rm");
   private static final String ARGUMENT_LEVEL_MAX = addArgument("l");
   private static final String ARGUMENT_LEVEL_MIN = addArgument("lm");
   private static final String ARGUMENT_COORDINATE_X = addArgument("x");
   private static final String ARGUMENT_COORDINATE_Y = addArgument("y");
   private static final String ARGUMENT_COORDINATE_Z = addArgument("z");
   private static final String ARGUMENT_DELTA_X = addArgument("dx");
   private static final String ARGUMENT_DELTA_Y = addArgument("dy");
   private static final String ARGUMENT_DELTA_Z = addArgument("dz");
   private static final String ARGUMENT_ROTX_MAX = addArgument("rx");
   private static final String ARGUMENT_ROTX_MIN = addArgument("rxm");
   private static final String ARGUMENT_ROTY_MAX = addArgument("ry");
   private static final String ARGUMENT_ROTY_MIN = addArgument("rym");
   private static final String ARGUMENT_COUNT = addArgument("c");
   private static final String ARGUMENT_MODE = addArgument("m");
   private static final String ARGUMENT_TEAM_NAME = addArgument("team");
   private static final String ARGUMENT_PLAYER_NAME = addArgument("name");
   private static final String ARGUMENT_ENTITY_TYPE = addArgument("type");
   private static final String ARGUMENT_ENTITY_TAG = addArgument("tag");
   private static final Predicate<String> IS_VALID_ARGUMENT = new Predicate<String>() {
      public boolean apply(@Nullable String var1) {
         return ☃ != null && (EntitySelector.VALID_ARGUMENTS.contains(☃) || ☃.length() > "score_".length() && ☃.startsWith("score_"));
      }
   };
   private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(
      new String[]{
         ARGUMENT_COORDINATE_X,
         ARGUMENT_COORDINATE_Y,
         ARGUMENT_COORDINATE_Z,
         ARGUMENT_DELTA_X,
         ARGUMENT_DELTA_Y,
         ARGUMENT_DELTA_Z,
         ARGUMENT_RANGE_MIN,
         ARGUMENT_RANGE_MAX
      }
   );

   private static String addArgument(String var0) {
      VALID_ARGUMENTS.add(☃);
      return ☃;
   }

   @Nullable
   public static EntityPlayerMP matchOnePlayer(ICommandSender var0, String var1) throws CommandException {
      return matchOneEntity(☃, ☃, EntityPlayerMP.class);
   }

   public static List<EntityPlayerMP> getPlayers(ICommandSender var0, String var1) throws CommandException {
      return matchEntities(☃, ☃, EntityPlayerMP.class);
   }

   @Nullable
   public static <T extends Entity> T matchOneEntity(ICommandSender var0, String var1, Class<? extends T> var2) throws CommandException {
      List<T> ☃ = matchEntities(☃, ☃, ☃);
      return ☃.size() == 1 ? ☃.get(0) : null;
   }

   @Nullable
   public static ITextComponent matchEntitiesToTextComponent(ICommandSender var0, String var1) throws CommandException {
      List<Entity> ☃ = matchEntities(☃, ☃, Entity.class);
      if (☃.isEmpty()) {
         return null;
      } else {
         List<ITextComponent> ☃x = Lists.newArrayList();

         for (Entity ☃xx : ☃) {
            ☃x.add(☃xx.getDisplayName());
         }

         return CommandBase.join(☃x);
      }
   }

   public static <T extends Entity> List<T> matchEntities(ICommandSender var0, String var1, Class<? extends T> var2) throws CommandException {
      Matcher ☃ = TOKEN_PATTERN.matcher(☃);
      if (☃.matches() && ☃.canUseCommand(1, "@")) {
         Map<String, String> ☃x = getArgumentMap(☃.group(2));
         if (!isEntityTypeValid(☃, ☃x)) {
            return Collections.emptyList();
         } else {
            String ☃xx = ☃.group(1);
            BlockPos ☃xxx = getBlockPosFromArguments(☃x, ☃.getPosition());
            Vec3d ☃xxxx = getPosFromArguments(☃x, ☃.getPositionVector());
            List<World> ☃xxxxx = getWorlds(☃, ☃x);
            List<T> ☃xxxxxx = Lists.newArrayList();

            for (World ☃xxxxxxx : ☃xxxxx) {
               if (☃xxxxxxx != null) {
                  List<Predicate<Entity>> ☃xxxxxxxx = Lists.newArrayList();
                  ☃xxxxxxxx.addAll(getTypePredicates(☃x, ☃xx));
                  ☃xxxxxxxx.addAll(getXpLevelPredicates(☃x));
                  ☃xxxxxxxx.addAll(getGamemodePredicates(☃x));
                  ☃xxxxxxxx.addAll(getTeamPredicates(☃x));
                  ☃xxxxxxxx.addAll(getScorePredicates(☃, ☃x));
                  ☃xxxxxxxx.addAll(getNamePredicates(☃x));
                  ☃xxxxxxxx.addAll(getTagPredicates(☃x));
                  ☃xxxxxxxx.addAll(getRadiusPredicates(☃x, ☃xxxx));
                  ☃xxxxxxxx.addAll(getRotationsPredicates(☃x));
                  if ("s".equalsIgnoreCase(☃xx)) {
                     Entity ☃xxxxxxxxx = ☃.getCommandSenderEntity();
                     if (☃xxxxxxxxx != null && ☃.isAssignableFrom(☃xxxxxxxxx.getClass())) {
                        if (☃x.containsKey(ARGUMENT_DELTA_X) || ☃x.containsKey(ARGUMENT_DELTA_Y) || ☃x.containsKey(ARGUMENT_DELTA_Z)) {
                           int ☃xxxxxxxxxx = getInt(☃x, ARGUMENT_DELTA_X, 0);
                           int ☃xxxxxxxxxxx = getInt(☃x, ARGUMENT_DELTA_Y, 0);
                           int ☃xxxxxxxxxxxx = getInt(☃x, ARGUMENT_DELTA_Z, 0);
                           AxisAlignedBB ☃xxxxxxxxxxxxx = getAABB(☃xxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                           if (!☃xxxxxxxxxxxxx.intersects(☃xxxxxxxxx.getEntityBoundingBox())) {
                              return Collections.emptyList();
                           }
                        }

                        for (Predicate<Entity> ☃xxxxxxxxxx : ☃xxxxxxxx) {
                           if (!☃xxxxxxxxxx.apply(☃xxxxxxxxx)) {
                              return Collections.emptyList();
                           }
                        }

                        return Lists.newArrayList(new Entity[]{☃xxxxxxxxx});
                     }

                     return Collections.emptyList();
                  }

                  ☃xxxxxx.addAll(filterResults(☃x, ☃, ☃xxxxxxxx, ☃xx, ☃xxxxxxx, ☃xxx));
               }
            }

            return getEntitiesFromPredicates(☃xxxxxx, ☃x, ☃, ☃, ☃xx, ☃xxxx);
         }
      } else {
         return Collections.emptyList();
      }
   }

   private static List<World> getWorlds(ICommandSender var0, Map<String, String> var1) {
      List<World> ☃ = Lists.newArrayList();
      if (hasArgument(☃)) {
         ☃.add(☃.getEntityWorld());
      } else {
         Collections.addAll(☃, ☃.getServer().worlds);
      }

      return ☃;
   }

   private static <T extends Entity> boolean isEntityTypeValid(ICommandSender var0, Map<String, String> var1) {
      String ☃ = getArgument(☃, ARGUMENT_ENTITY_TYPE);
      if (☃ == null) {
         return true;
      } else {
         ResourceLocation ☃x = new ResourceLocation(☃.startsWith("!") ? ☃.substring(1) : ☃);
         if (EntityList.isRegistered(☃x)) {
            return true;
         } else {
            TextComponentTranslation ☃xx = new TextComponentTranslation("commands.generic.entity.invalidType", ☃x);
            ☃xx.getStyle().setColor(TextFormatting.RED);
            ☃.sendMessage(☃xx);
            return false;
         }
      }
   }

   private static List<Predicate<Entity>> getTypePredicates(Map<String, String> var0, String var1) {
      String ☃ = getArgument(☃, ARGUMENT_ENTITY_TYPE);
      if (☃ == null || !☃.equals("e") && !☃.equals("r") && !☃.equals("s")) {
         return !☃.equals("e") && !☃.equals("s") ? Collections.singletonList(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               return ☃ instanceof EntityPlayer;
            }
         }) : Collections.emptyList();
      } else {
         final boolean ☃x = ☃.startsWith("!");
         final ResourceLocation ☃xx = new ResourceLocation(☃x ? ☃.substring(1) : ☃);
         return Collections.singletonList(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               return EntityList.isMatchingName(☃, ☃) != ☃;
            }
         });
      }
   }

   private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      final int ☃x = getInt(☃, ARGUMENT_LEVEL_MIN, -1);
      final int ☃xx = getInt(☃, ARGUMENT_LEVEL_MAX, -1);
      if (☃x > -1 || ☃xx > -1) {
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (!(☃ instanceof EntityPlayerMP)) {
                  return false;
               } else {
                  EntityPlayerMP ☃xxx = (EntityPlayerMP)☃;
                  return (☃ <= -1 || ☃xxx.experienceLevel >= ☃) && (☃ <= -1 || ☃xxx.experienceLevel <= ☃);
               }
            }
         });
      }

      return ☃;
   }

   private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      String ☃x = getArgument(☃, ARGUMENT_MODE);
      if (☃x == null) {
         return ☃;
      } else {
         final boolean ☃xx = ☃x.startsWith("!");
         if (☃xx) {
            ☃x = ☃x.substring(1);
         }

         GameType ☃xxx;
         try {
            int ☃xxxx = Integer.parseInt(☃x);
            ☃xxx = GameType.parseGameTypeWithDefault(☃xxxx, GameType.NOT_SET);
         } catch (Throwable var6) {
            ☃xxx = GameType.parseGameTypeWithDefault(☃x, GameType.NOT_SET);
         }

         final GameType ☃xxxx = ☃xxx;
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (!(☃ instanceof EntityPlayerMP)) {
                  return false;
               } else {
                  EntityPlayerMP ☃xxxxx = (EntityPlayerMP)☃;
                  GameType ☃x = ☃xxxxx.interactionManager.getGameType();
                  return ☃ ? ☃x != ☃ : ☃x == ☃;
               }
            }
         });
         return ☃;
      }
   }

   private static List<Predicate<Entity>> getTeamPredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      final String ☃x = getArgument(☃, ARGUMENT_TEAM_NAME);
      final boolean ☃xx = ☃x != null && ☃x.startsWith("!");
      if (☃xx) {
         ☃x = ☃x.substring(1);
      }

      if (☃x != null) {
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (!(☃ instanceof EntityLivingBase)) {
                  return false;
               } else {
                  EntityLivingBase ☃xxx = (EntityLivingBase)☃;
                  Team ☃x = ☃xxx.getTeam();
                  String ☃xx = ☃x == null ? "" : ☃x.getName();
                  return ☃xx.equals(☃) != ☃;
               }
            }
         });
      }

      return ☃;
   }

   private static List<Predicate<Entity>> getScorePredicates(final ICommandSender var0, Map<String, String> var1) {
      final Map<String, Integer> ☃ = getScoreMap(☃);
      return (List<Predicate<Entity>>)(☃.isEmpty() ? Collections.emptyList() : Lists.newArrayList(new Predicate[]{new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            if (☃ == null) {
               return false;
            } else {
               Scoreboard ☃x = ☃.getServer().getWorld(0).getScoreboard();

               for (Entry<String, Integer> ☃x : ☃.entrySet()) {
                  String ☃xx = ☃x.getKey();
                  boolean ☃xxx = false;
                  if (☃xx.endsWith("_min") && ☃xx.length() > 4) {
                     ☃xxx = true;
                     ☃xx = ☃xx.substring(0, ☃xx.length() - 4);
                  }

                  ScoreObjective ☃xxxx = ☃x.getObjective(☃xx);
                  if (☃xxxx == null) {
                     return false;
                  }

                  String ☃xxxxx = ☃ instanceof EntityPlayerMP ? ☃.getName() : ☃.getCachedUniqueIdString();
                  if (!☃x.entityHasObjective(☃xxxxx, ☃xxxx)) {
                     return false;
                  }

                  Score ☃xxxxxx = ☃x.getOrCreateScore(☃xxxxx, ☃xxxx);
                  int ☃xxxxxxx = ☃xxxxxx.getScorePoints();
                  if (☃xxxxxxx < ☃x.getValue() && ☃xxx) {
                     return false;
                  }

                  if (☃xxxxxxx > ☃x.getValue() && !☃xxx) {
                     return false;
                  }
               }

               return true;
            }
         }
      }}));
   }

   private static List<Predicate<Entity>> getNamePredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      final String ☃x = getArgument(☃, ARGUMENT_PLAYER_NAME);
      final boolean ☃xx = ☃x != null && ☃x.startsWith("!");
      if (☃xx) {
         ☃x = ☃x.substring(1);
      }

      if (☃x != null) {
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               return ☃ != null && ☃.getName().equals(☃) != ☃;
            }
         });
      }

      return ☃;
   }

   private static List<Predicate<Entity>> getTagPredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      String ☃x = getArgument(☃, ARGUMENT_ENTITY_TAG);
      final boolean ☃xx = ☃x != null && ☃x.startsWith("!");
      if (☃xx) {
         ☃x = ☃x.substring(1);
      }

      if (☃x != null) {
         final String ☃xxx = ☃x;
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (☃ == null) {
                  return false;
               } else {
                  return "".equals(☃) ? ☃.getTags().isEmpty() != ☃ : ☃.getTags().contains(☃) != ☃;
               }
            }
         });
      }

      return ☃;
   }

   private static List<Predicate<Entity>> getRadiusPredicates(Map<String, String> var0, final Vec3d var1) {
      double ☃ = getInt(☃, ARGUMENT_RANGE_MIN, -1);
      double ☃x = getInt(☃, ARGUMENT_RANGE_MAX, -1);
      final boolean ☃xx = ☃ < -0.5;
      final boolean ☃xxx = ☃x < -0.5;
      if (☃xx && ☃xxx) {
         return Collections.emptyList();
      } else {
         double ☃xxxx = Math.max(☃, 1.0E-4);
         final double ☃xxxxx = ☃xxxx * ☃xxxx;
         double ☃xxxxxx = Math.max(☃x, 1.0E-4);
         final double ☃xxxxxxx = ☃xxxxxx * ☃xxxxxx;
         return Lists.newArrayList(new Predicate[]{new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1x) {
               if (☃ == null) {
                  return false;
               } else {
                  double ☃xxxxxxxx = ☃.squareDistanceTo(☃.posX, ☃.posY, ☃.posZ);
                  return (☃ || ☃xxxxxxxx >= ☃) && (☃ || ☃xxxxxxxx <= ☃);
               }
            }
         }});
      }
   }

   private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> var0) {
      List<Predicate<Entity>> ☃ = Lists.newArrayList();
      if (☃.containsKey(ARGUMENT_ROTY_MIN) || ☃.containsKey(ARGUMENT_ROTY_MAX)) {
         final int ☃x = MathHelper.wrapDegrees(getInt(☃, ARGUMENT_ROTY_MIN, 0));
         final int ☃xx = MathHelper.wrapDegrees(getInt(☃, ARGUMENT_ROTY_MAX, 359));
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (☃ == null) {
                  return false;
               } else {
                  int ☃xxx = MathHelper.wrapDegrees(MathHelper.floor(☃.rotationYaw));
                  return ☃ > ☃ ? ☃xxx >= ☃ || ☃xxx <= ☃ : ☃xxx >= ☃ && ☃xxx <= ☃;
               }
            }
         });
      }

      if (☃.containsKey(ARGUMENT_ROTX_MIN) || ☃.containsKey(ARGUMENT_ROTX_MAX)) {
         final int ☃x = MathHelper.wrapDegrees(getInt(☃, ARGUMENT_ROTX_MIN, 0));
         final int ☃xx = MathHelper.wrapDegrees(getInt(☃, ARGUMENT_ROTX_MAX, 359));
         ☃.add(new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               if (☃ == null) {
                  return false;
               } else {
                  int ☃ = MathHelper.wrapDegrees(MathHelper.floor(☃.rotationPitch));
                  return ☃ > ☃ ? ☃ >= ☃ || ☃ <= ☃ : ☃ >= ☃ && ☃ <= ☃;
               }
            }
         });
      }

      return ☃;
   }

   private static <T extends Entity> List<T> filterResults(
      Map<String, String> var0, Class<? extends T> var1, List<Predicate<Entity>> var2, String var3, World var4, BlockPos var5
   ) {
      List<T> ☃ = Lists.newArrayList();
      String ☃x = getArgument(☃, ARGUMENT_ENTITY_TYPE);
      ☃x = ☃x != null && ☃x.startsWith("!") ? ☃x.substring(1) : ☃x;
      boolean ☃xx = !☃.equals("e");
      boolean ☃xxx = ☃.equals("r") && ☃x != null;
      int ☃xxxx = getInt(☃, ARGUMENT_DELTA_X, 0);
      int ☃xxxxx = getInt(☃, ARGUMENT_DELTA_Y, 0);
      int ☃xxxxxx = getInt(☃, ARGUMENT_DELTA_Z, 0);
      int ☃xxxxxxx = getInt(☃, ARGUMENT_RANGE_MAX, -1);
      Predicate<Entity> ☃xxxxxxxx = Predicates.and(☃);
      Predicate<Entity> ☃xxxxxxxxx = Predicates.and(EntitySelectors.IS_ALIVE, ☃xxxxxxxx);
      if (☃.containsKey(ARGUMENT_DELTA_X) || ☃.containsKey(ARGUMENT_DELTA_Y) || ☃.containsKey(ARGUMENT_DELTA_Z)) {
         final AxisAlignedBB ☃xxxxxxxxxx = getAABB(☃, ☃xxxx, ☃xxxxx, ☃xxxxxx);
         if (☃xx && !☃xxx) {
            Predicate<Entity> ☃xxxxxxxxxxx = new Predicate<Entity>() {
               public boolean apply(@Nullable Entity var1) {
                  return ☃ != null && ☃.intersects(☃.getEntityBoundingBox());
               }
            };
            ☃.addAll(☃.getPlayers(☃, Predicates.and(☃xxxxxxxxx, ☃xxxxxxxxxxx)));
         } else {
            ☃.addAll(☃.getEntitiesWithinAABB(☃, ☃xxxxxxxxxx, ☃xxxxxxxxx));
         }
      } else if (☃xxxxxxx >= 0) {
         AxisAlignedBB ☃xxxxxxxxxx = new AxisAlignedBB(
            ☃.getX() - ☃xxxxxxx, ☃.getY() - ☃xxxxxxx, ☃.getZ() - ☃xxxxxxx, ☃.getX() + ☃xxxxxxx + 1, ☃.getY() + ☃xxxxxxx + 1, ☃.getZ() + ☃xxxxxxx + 1
         );
         if (☃xx && !☃xxx) {
            ☃.addAll(☃.getPlayers(☃, ☃xxxxxxxxx));
         } else {
            ☃.addAll(☃.getEntitiesWithinAABB(☃, ☃xxxxxxxxxx, ☃xxxxxxxxx));
         }
      } else if (☃.equals("a")) {
         ☃.addAll(☃.getPlayers(☃, ☃xxxxxxxx));
      } else if (!☃.equals("p") && (!☃.equals("r") || ☃xxx)) {
         ☃.addAll(☃.getEntities(☃, ☃xxxxxxxxx));
      } else {
         ☃.addAll(☃.getPlayers(☃, ☃xxxxxxxxx));
      }

      return ☃;
   }

   private static <T extends Entity> List<T> getEntitiesFromPredicates(
      List<T> var0, Map<String, String> var1, ICommandSender var2, Class<? extends T> var3, String var4, final Vec3d var5
   ) {
      int ☃ = getInt(☃, ARGUMENT_COUNT, !☃.equals("a") && !☃.equals("e") ? 1 : 0);
      if (☃.equals("p") || ☃.equals("a") || ☃.equals("e")) {
         Collections.sort(☃, new Comparator<Entity>() {
            public int compare(Entity var1, Entity var2x) {
               return ComparisonChain.start().compare(☃.getDistanceSq(☃.x, ☃.y, ☃.z), var2x.getDistanceSq(☃.x, ☃.y, ☃.z)).result();
            }
         });
      } else if (☃.equals("r")) {
         Collections.shuffle(☃);
      }

      Entity ☃x = ☃.getCommandSenderEntity();
      if (☃x != null && ☃.isAssignableFrom(☃x.getClass()) && ☃ == 1 && ☃.contains(☃x) && !"r".equals(☃)) {
         ☃ = Lists.newArrayList(new Entity[]{☃x});
      }

      if (☃ != 0) {
         if (☃ < 0) {
            Collections.reverse(☃);
         }

         ☃ = ☃.subList(0, Math.min(Math.abs(☃), ☃.size()));
      }

      return ☃;
   }

   private static AxisAlignedBB getAABB(BlockPos var0, int var1, int var2, int var3) {
      boolean ☃ = ☃ < 0;
      boolean ☃x = ☃ < 0;
      boolean ☃xx = ☃ < 0;
      int ☃xxx = ☃.getX() + (☃ ? ☃ : 0);
      int ☃xxxx = ☃.getY() + (☃x ? ☃ : 0);
      int ☃xxxxx = ☃.getZ() + (☃xx ? ☃ : 0);
      int ☃xxxxxx = ☃.getX() + (☃ ? 0 : ☃) + 1;
      int ☃xxxxxxx = ☃.getY() + (☃x ? 0 : ☃) + 1;
      int ☃xxxxxxxx = ☃.getZ() + (☃xx ? 0 : ☃) + 1;
      return new AxisAlignedBB(☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
   }

   private static BlockPos getBlockPosFromArguments(Map<String, String> var0, BlockPos var1) {
      return new BlockPos(getInt(☃, ARGUMENT_COORDINATE_X, ☃.getX()), getInt(☃, ARGUMENT_COORDINATE_Y, ☃.getY()), getInt(☃, ARGUMENT_COORDINATE_Z, ☃.getZ()));
   }

   private static Vec3d getPosFromArguments(Map<String, String> var0, Vec3d var1) {
      return new Vec3d(
         getCoordinate(☃, ARGUMENT_COORDINATE_X, ☃.x, true),
         getCoordinate(☃, ARGUMENT_COORDINATE_Y, ☃.y, false),
         getCoordinate(☃, ARGUMENT_COORDINATE_Z, ☃.z, true)
      );
   }

   private static double getCoordinate(Map<String, String> var0, String var1, double var2, boolean var4) {
      return ☃.containsKey(☃) ? MathHelper.getInt(☃.get(☃), MathHelper.floor(☃)) + (☃ ? 0.5 : 0.0) : ☃;
   }

   private static boolean hasArgument(Map<String, String> var0) {
      for (String ☃ : WORLD_BINDING_ARGS) {
         if (☃.containsKey(☃)) {
            return true;
         }
      }

      return false;
   }

   private static int getInt(Map<String, String> var0, String var1, int var2) {
      return ☃.containsKey(☃) ? MathHelper.getInt(☃.get(☃), ☃) : ☃;
   }

   @Nullable
   private static String getArgument(Map<String, String> var0, String var1) {
      return ☃.get(☃);
   }

   public static Map<String, Integer> getScoreMap(Map<String, String> var0) {
      Map<String, Integer> ☃ = Maps.newHashMap();

      for (String ☃x : ☃.keySet()) {
         if (☃x.startsWith("score_") && ☃x.length() > "score_".length()) {
            ☃.put(☃x.substring("score_".length()), MathHelper.getInt(☃.get(☃x), 1));
         }
      }

      return ☃;
   }

   public static boolean matchesMultiplePlayers(String var0) throws CommandException {
      Matcher ☃ = TOKEN_PATTERN.matcher(☃);
      if (!☃.matches()) {
         return false;
      } else {
         Map<String, String> ☃x = getArgumentMap(☃.group(2));
         String ☃xx = ☃.group(1);
         int ☃xxx = !"a".equals(☃xx) && !"e".equals(☃xx) ? 1 : 0;
         return getInt(☃x, ARGUMENT_COUNT, ☃xxx) != 1;
      }
   }

   public static boolean isSelector(String var0) {
      return TOKEN_PATTERN.matcher(☃).matches();
   }

   private static Map<String, String> getArgumentMap(@Nullable String var0) throws CommandException {
      Map<String, String> ☃ = Maps.newHashMap();
      if (☃ == null) {
         return ☃;
      } else {
         for (String ☃x : COMMA_SPLITTER.split(☃)) {
            Iterator<String> ☃xx = EQUAL_SPLITTER.split(☃x).iterator();
            String ☃xxx = ☃xx.next();
            if (!IS_VALID_ARGUMENT.apply(☃xxx)) {
               throw new CommandException("commands.generic.selector_argument", ☃x);
            }

            ☃.put(☃xxx, ☃xx.hasNext() ? ☃xx.next() : "");
         }

         return ☃;
      }
   }
}
