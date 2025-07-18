package net.minecraft.command;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.gson.JsonParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.exception.ExceptionUtils;

public abstract class CommandBase implements ICommand {
   private static ICommandListener commandListener;
   private static final Splitter COMMA_SPLITTER = Splitter.on(',');
   private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

   protected static SyntaxErrorException toSyntaxException(JsonParseException var0) {
      Throwable ☃ = ExceptionUtils.getRootCause(☃);
      String ☃x = "";
      if (☃ != null) {
         ☃x = ☃.getMessage();
         if (☃x.contains("setLenient")) {
            ☃x = ☃x.substring(☃x.indexOf("to accept ") + 10);
         }
      }

      return new SyntaxErrorException("commands.tellraw.jsonException", ☃x);
   }

   public static NBTTagCompound entityToNBT(Entity var0) {
      NBTTagCompound ☃ = ☃.writeToNBT(new NBTTagCompound());
      if (☃ instanceof EntityPlayer) {
         ItemStack ☃x = ((EntityPlayer)☃).inventory.getCurrentItem();
         if (!☃x.isEmpty()) {
            ☃.setTag("SelectedItem", ☃x.writeToNBT(new NBTTagCompound()));
         }
      }

      return ☃;
   }

   public int getRequiredPermissionLevel() {
      return 4;
   }

   @Override
   public List<String> getAliases() {
      return Collections.emptyList();
   }

   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return ☃.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return Collections.emptyList();
   }

   public static int parseInt(String var0) throws NumberInvalidException {
      try {
         return Integer.parseInt(☃);
      } catch (NumberFormatException var2) {
         throw new NumberInvalidException("commands.generic.num.invalid", ☃);
      }
   }

   public static int parseInt(String var0, int var1) throws NumberInvalidException {
      return parseInt(☃, ☃, Integer.MAX_VALUE);
   }

   public static int parseInt(String var0, int var1, int var2) throws NumberInvalidException {
      int ☃ = parseInt(☃);
      if (☃ < ☃) {
         throw new NumberInvalidException("commands.generic.num.tooSmall", ☃, ☃);
      } else if (☃ > ☃) {
         throw new NumberInvalidException("commands.generic.num.tooBig", ☃, ☃);
      } else {
         return ☃;
      }
   }

   public static long parseLong(String var0) throws NumberInvalidException {
      try {
         return Long.parseLong(☃);
      } catch (NumberFormatException var2) {
         throw new NumberInvalidException("commands.generic.num.invalid", ☃);
      }
   }

   public static long parseLong(String var0, long var1, long var3) throws NumberInvalidException {
      long ☃ = parseLong(☃);
      if (☃ < ☃) {
         throw new NumberInvalidException("commands.generic.num.tooSmall", ☃, ☃);
      } else if (☃ > ☃) {
         throw new NumberInvalidException("commands.generic.num.tooBig", ☃, ☃);
      } else {
         return ☃;
      }
   }

   public static BlockPos parseBlockPos(ICommandSender var0, String[] var1, int var2, boolean var3) throws NumberInvalidException {
      BlockPos ☃ = ☃.getPosition();
      return new BlockPos(
         parseDouble(☃.getX(), ☃[☃], -30000000, 30000000, ☃),
         parseDouble(☃.getY(), ☃[☃ + 1], 0, 256, false),
         parseDouble(☃.getZ(), ☃[☃ + 2], -30000000, 30000000, ☃)
      );
   }

   public static double parseDouble(String var0) throws NumberInvalidException {
      try {
         double ☃ = Double.parseDouble(☃);
         if (!Doubles.isFinite(☃)) {
            throw new NumberInvalidException("commands.generic.num.invalid", ☃);
         } else {
            return ☃;
         }
      } catch (NumberFormatException var3) {
         throw new NumberInvalidException("commands.generic.num.invalid", ☃);
      }
   }

   public static double parseDouble(String var0, double var1) throws NumberInvalidException {
      return parseDouble(☃, ☃, Double.MAX_VALUE);
   }

   public static double parseDouble(String var0, double var1, double var3) throws NumberInvalidException {
      double ☃ = parseDouble(☃);
      if (☃ < ☃) {
         throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", ☃), String.format("%.2f", ☃));
      } else if (☃ > ☃) {
         throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", ☃), String.format("%.2f", ☃));
      } else {
         return ☃;
      }
   }

   public static boolean parseBoolean(String var0) throws CommandException {
      if ("true".equals(☃) || "1".equals(☃)) {
         return true;
      } else if (!"false".equals(☃) && !"0".equals(☃)) {
         throw new CommandException("commands.generic.boolean.invalid", ☃);
      } else {
         return false;
      }
   }

   public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender var0) throws PlayerNotFoundException {
      if (☃ instanceof EntityPlayerMP) {
         return (EntityPlayerMP)☃;
      } else {
         throw new PlayerNotFoundException("commands.generic.player.unspecified");
      }
   }

   public static List<EntityPlayerMP> getPlayers(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      List<EntityPlayerMP> ☃ = EntitySelector.getPlayers(☃, ☃);
      return (List<EntityPlayerMP>)(☃.isEmpty() ? Lists.newArrayList(new EntityPlayerMP[]{getPlayer(☃, null, ☃)}) : ☃);
   }

   public static EntityPlayerMP getPlayer(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      return getPlayer(☃, EntitySelector.matchOnePlayer(☃, ☃), ☃);
   }

   private static EntityPlayerMP getPlayer(MinecraftServer var0, @Nullable EntityPlayerMP var1, String var2) throws CommandException {
      if (☃ == null) {
         try {
            ☃ = ☃.getPlayerList().getPlayerByUUID(UUID.fromString(☃));
         } catch (IllegalArgumentException var4) {
         }
      }

      if (☃ == null) {
         ☃ = ☃.getPlayerList().getPlayerByUsername(☃);
      }

      if (☃ == null) {
         throw new PlayerNotFoundException("commands.generic.player.notFound", ☃);
      } else {
         return ☃;
      }
   }

   public static Entity getEntity(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      return getEntity(☃, ☃, ☃, Entity.class);
   }

   public static <T extends Entity> T getEntity(MinecraftServer var0, ICommandSender var1, String var2, Class<? extends T> var3) throws CommandException {
      Entity ☃ = EntitySelector.matchOneEntity(☃, ☃, ☃);
      if (☃ == null) {
         ☃ = ☃.getPlayerList().getPlayerByUsername(☃);
      }

      if (☃ == null) {
         try {
            UUID ☃x = UUID.fromString(☃);
            ☃ = ☃.getEntityFromUuid(☃x);
            if (☃ == null) {
               ☃ = ☃.getPlayerList().getPlayerByUUID(☃x);
            }
         } catch (IllegalArgumentException var6) {
            if (☃.split("-").length == 5) {
               throw new EntityNotFoundException("commands.generic.entity.invalidUuid", ☃);
            }
         }
      }

      if (☃ != null && ☃.isAssignableFrom(☃.getClass())) {
         return (T)☃;
      } else {
         throw new EntityNotFoundException(☃);
      }
   }

   public static List<Entity> getEntityList(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      return (List<Entity>)(EntitySelector.isSelector(☃)
         ? EntitySelector.matchEntities(☃, ☃, Entity.class)
         : Lists.newArrayList(new Entity[]{getEntity(☃, ☃, ☃)}));
   }

   public static String getPlayerName(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      try {
         return getPlayer(☃, ☃, ☃).getName();
      } catch (CommandException var4) {
         if (EntitySelector.isSelector(☃)) {
            throw var4;
         } else {
            return ☃;
         }
      }
   }

   public static String getEntityName(MinecraftServer var0, ICommandSender var1, String var2) throws CommandException {
      try {
         return getPlayer(☃, ☃, ☃).getName();
      } catch (PlayerNotFoundException var6) {
         try {
            return getEntity(☃, ☃, ☃).getCachedUniqueIdString();
         } catch (EntityNotFoundException var5) {
            if (EntitySelector.isSelector(☃)) {
               throw var5;
            } else {
               return ☃;
            }
         }
      }
   }

   public static ITextComponent getChatComponentFromNthArg(ICommandSender var0, String[] var1, int var2) throws CommandException {
      return getChatComponentFromNthArg(☃, ☃, ☃, false);
   }

   public static ITextComponent getChatComponentFromNthArg(ICommandSender var0, String[] var1, int var2, boolean var3) throws CommandException {
      ITextComponent ☃ = new TextComponentString("");

      for (int ☃x = ☃; ☃x < ☃.length; ☃x++) {
         if (☃x > ☃) {
            ☃.appendText(" ");
         }

         ITextComponent ☃xx = new TextComponentString(☃[☃x]);
         if (☃) {
            ITextComponent ☃xxx = EntitySelector.matchEntitiesToTextComponent(☃, ☃[☃x]);
            if (☃xxx == null) {
               if (EntitySelector.isSelector(☃[☃x])) {
                  throw new PlayerNotFoundException("commands.generic.selector.notFound", ☃[☃x]);
               }
            } else {
               ☃xx = ☃xxx;
            }
         }

         ☃.appendSibling(☃xx);
      }

      return ☃;
   }

   public static String buildString(String[] var0, int var1) {
      StringBuilder ☃ = new StringBuilder();

      for (int ☃x = ☃; ☃x < ☃.length; ☃x++) {
         if (☃x > ☃) {
            ☃.append(" ");
         }

         String ☃xx = ☃[☃x];
         ☃.append(☃xx);
      }

      return ☃.toString();
   }

   public static CommandBase.CoordinateArg parseCoordinate(double var0, String var2, boolean var3) throws NumberInvalidException {
      return parseCoordinate(☃, ☃, -30000000, 30000000, ☃);
   }

   public static CommandBase.CoordinateArg parseCoordinate(double var0, String var2, int var3, int var4, boolean var5) throws NumberInvalidException {
      boolean ☃ = ☃.startsWith("~");
      if (☃ && Double.isNaN(☃)) {
         throw new NumberInvalidException("commands.generic.num.invalid", ☃);
      } else {
         double ☃x = 0.0;
         if (!☃ || ☃.length() > 1) {
            boolean ☃xx = ☃.contains(".");
            if (☃) {
               ☃ = ☃.substring(1);
            }

            ☃x += parseDouble(☃);
            if (!☃xx && !☃ && ☃) {
               ☃x += 0.5;
            }
         }

         double ☃xxx = ☃x + (☃ ? ☃ : 0.0);
         if (☃ != 0 || ☃ != 0) {
            if (☃xxx < ☃) {
               throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", ☃xxx), ☃);
            }

            if (☃xxx > ☃) {
               throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", ☃xxx), ☃);
            }
         }

         return new CommandBase.CoordinateArg(☃xxx, ☃x, ☃);
      }
   }

   public static double parseDouble(double var0, String var2, boolean var3) throws NumberInvalidException {
      return parseDouble(☃, ☃, -30000000, 30000000, ☃);
   }

   public static double parseDouble(double var0, String var2, int var3, int var4, boolean var5) throws NumberInvalidException {
      boolean ☃ = ☃.startsWith("~");
      if (☃ && Double.isNaN(☃)) {
         throw new NumberInvalidException("commands.generic.num.invalid", ☃);
      } else {
         double ☃x = ☃ ? ☃ : 0.0;
         if (!☃ || ☃.length() > 1) {
            boolean ☃xx = ☃.contains(".");
            if (☃) {
               ☃ = ☃.substring(1);
            }

            ☃x += parseDouble(☃);
            if (!☃xx && !☃ && ☃) {
               ☃x += 0.5;
            }
         }

         if (☃ != 0 || ☃ != 0) {
            if (☃x < ☃) {
               throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", ☃x), ☃);
            }

            if (☃x > ☃) {
               throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", ☃x), ☃);
            }
         }

         return ☃x;
      }
   }

   public static Item getItemByText(ICommandSender var0, String var1) throws NumberInvalidException {
      ResourceLocation ☃ = new ResourceLocation(☃);
      Item ☃x = Item.REGISTRY.getObject(☃);
      if (☃x == null) {
         throw new NumberInvalidException("commands.give.item.notFound", ☃);
      } else {
         return ☃x;
      }
   }

   public static Block getBlockByText(ICommandSender var0, String var1) throws NumberInvalidException {
      ResourceLocation ☃ = new ResourceLocation(☃);
      if (!Block.REGISTRY.containsKey(☃)) {
         throw new NumberInvalidException("commands.give.block.notFound", ☃);
      } else {
         return Block.REGISTRY.getObject(☃);
      }
   }

   public static IBlockState convertArgToBlockState(Block var0, String var1) throws NumberInvalidException, InvalidBlockStateException {
      try {
         int ☃ = Integer.parseInt(☃);
         if (☃ < 0) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", ☃, 0);
         } else if (☃ > 15) {
            throw new NumberInvalidException("commands.generic.num.tooBig", ☃, 15);
         } else {
            return ☃.getStateFromMeta(Integer.parseInt(☃));
         }
      } catch (RuntimeException var7) {
         try {
            Map<IProperty<?>, Comparable<?>> ☃x = getBlockStatePropertyValueMap(☃, ☃);
            IBlockState ☃xx = ☃.getDefaultState();

            for (Entry<IProperty<?>, Comparable<?>> ☃xxx : ☃x.entrySet()) {
               ☃xx = getBlockState(☃xx, ☃xxx.getKey(), ☃xxx.getValue());
            }

            return ☃xx;
         } catch (RuntimeException var6) {
            throw new InvalidBlockStateException("commands.generic.blockstate.invalid", ☃, Block.REGISTRY.getNameForObject(☃));
         }
      }
   }

   private static <T extends Comparable<T>> IBlockState getBlockState(IBlockState var0, IProperty<T> var1, Comparable<?> var2) {
      return ☃.withProperty(☃, ☃);
   }

   public static Predicate<IBlockState> convertArgToBlockStatePredicate(final Block var0, String var1) throws InvalidBlockStateException {
      if (!"*".equals(☃) && !"-1".equals(☃)) {
         try {
            final int ☃ = Integer.parseInt(☃);
            return new Predicate<IBlockState>() {
               public boolean apply(@Nullable IBlockState var1) {
                  return ☃ == ☃.getBlock().getMetaFromState(☃);
               }
            };
         } catch (RuntimeException var3) {
            final Map<IProperty<?>, Comparable<?>> ☃x = getBlockStatePropertyValueMap(☃, ☃);
            return new Predicate<IBlockState>() {
               public boolean apply(@Nullable IBlockState var1) {
                  if (☃ != null && ☃ == ☃.getBlock()) {
                     for (Entry<IProperty<?>, Comparable<?>> ☃ : ☃.entrySet()) {
                        if (!☃.getValue(☃.getKey()).equals(☃.getValue())) {
                           return false;
                        }
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            };
         }
      } else {
         return Predicates.alwaysTrue();
      }
   }

   private static Map<IProperty<?>, Comparable<?>> getBlockStatePropertyValueMap(Block var0, String var1) throws InvalidBlockStateException {
      Map<IProperty<?>, Comparable<?>> ☃ = Maps.newHashMap();
      if ("default".equals(☃)) {
         return ☃.getDefaultState().getProperties();
      } else {
         BlockStateContainer ☃x = ☃.getBlockState();
         Iterator var4 = COMMA_SPLITTER.split(☃).iterator();

         while (true) {
            if (!var4.hasNext()) {
               return ☃;
            }

            String ☃xx = (String)var4.next();
            Iterator<String> ☃xxx = EQUAL_SPLITTER.split(☃xx).iterator();
            if (!☃xxx.hasNext()) {
               break;
            }

            IProperty<?> ☃xxxx = ☃x.getProperty(☃xxx.next());
            if (☃xxxx == null || !☃xxx.hasNext()) {
               break;
            }

            Comparable<?> ☃xxxxx = getValueHelper((IProperty<Comparable<?>>)☃xxxx, ☃xxx.next());
            if (☃xxxxx == null) {
               break;
            }

            ☃.put(☃xxxx, ☃xxxxx);
         }

         throw new InvalidBlockStateException("commands.generic.blockstate.invalid", ☃, Block.REGISTRY.getNameForObject(☃));
      }
   }

   @Nullable
   private static <T extends Comparable<T>> T getValueHelper(IProperty<T> var0, String var1) {
      return (T)☃.parseValue(☃).orNull();
   }

   public static String joinNiceString(Object[] var0) {
      StringBuilder ☃ = new StringBuilder();

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         String ☃xx = ☃[☃x].toString();
         if (☃x > 0) {
            if (☃x == ☃.length - 1) {
               ☃.append(" and ");
            } else {
               ☃.append(", ");
            }
         }

         ☃.append(☃xx);
      }

      return ☃.toString();
   }

   public static ITextComponent join(List<ITextComponent> var0) {
      ITextComponent ☃ = new TextComponentString("");

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         if (☃x > 0) {
            if (☃x == ☃.size() - 1) {
               ☃.appendText(" and ");
            } else if (☃x > 0) {
               ☃.appendText(", ");
            }
         }

         ☃.appendSibling(☃.get(☃x));
      }

      return ☃;
   }

   public static String joinNiceStringFromCollection(Collection<String> var0) {
      return joinNiceString(☃.toArray(new String[☃.size()]));
   }

   public static List<String> getTabCompletionCoordinate(String[] var0, int var1, @Nullable BlockPos var2) {
      if (☃ == null) {
         return Lists.newArrayList(new String[]{"~"});
      } else {
         int ☃ = ☃.length - 1;
         String ☃x;
         if (☃ == ☃) {
            ☃x = Integer.toString(☃.getX());
         } else if (☃ == ☃ + 1) {
            ☃x = Integer.toString(☃.getY());
         } else {
            if (☃ != ☃ + 2) {
               return Collections.emptyList();
            }

            ☃x = Integer.toString(☃.getZ());
         }

         return Lists.newArrayList(new String[]{☃x});
      }
   }

   public static List<String> getTabCompletionCoordinateXZ(String[] var0, int var1, @Nullable BlockPos var2) {
      if (☃ == null) {
         return Lists.newArrayList(new String[]{"~"});
      } else {
         int ☃ = ☃.length - 1;
         String ☃x;
         if (☃ == ☃) {
            ☃x = Integer.toString(☃.getX());
         } else {
            if (☃ != ☃ + 1) {
               return Collections.emptyList();
            }

            ☃x = Integer.toString(☃.getZ());
         }

         return Lists.newArrayList(new String[]{☃x});
      }
   }

   public static boolean doesStringStartWith(String var0, String var1) {
      return ☃.regionMatches(true, 0, ☃, 0, ☃.length());
   }

   public static List<String> getListOfStringsMatchingLastWord(String[] var0, String... var1) {
      return getListOfStringsMatchingLastWord(☃, Arrays.asList(☃));
   }

   public static List<String> getListOfStringsMatchingLastWord(String[] var0, Collection<?> var1) {
      String ☃ = ☃[☃.length - 1];
      List<String> ☃x = Lists.newArrayList();
      if (!☃.isEmpty()) {
         for (String ☃xx : Iterables.transform(☃, Functions.toStringFunction())) {
            if (doesStringStartWith(☃, ☃xx)) {
               ☃x.add(☃xx);
            }
         }

         if (☃x.isEmpty()) {
            for (Object ☃xxx : ☃) {
               if (☃xxx instanceof ResourceLocation && doesStringStartWith(☃, ((ResourceLocation)☃xxx).getPath())) {
                  ☃x.add(String.valueOf(☃xxx));
               }
            }
         }
      }

      return ☃x;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return false;
   }

   public static void notifyCommandListener(ICommandSender var0, ICommand var1, String var2, Object... var3) {
      notifyCommandListener(☃, ☃, 0, ☃, ☃);
   }

   public static void notifyCommandListener(ICommandSender var0, ICommand var1, int var2, String var3, Object... var4) {
      if (commandListener != null) {
         commandListener.notifyListener(☃, ☃, ☃, ☃, ☃);
      }
   }

   public static void setCommandListener(ICommandListener var0) {
      commandListener = ☃;
   }

   public int compareTo(ICommand var1) {
      return this.getName().compareTo(☃.getName());
   }

   public static class CoordinateArg {
      private final double result;
      private final double amount;
      private final boolean isRelative;

      protected CoordinateArg(double var1, double var3, boolean var5) {
         this.result = ☃;
         this.amount = ☃;
         this.isRelative = ☃;
      }

      public double getResult() {
         return this.result;
      }

      public double getAmount() {
         return this.amount;
      }

      public boolean isRelative() {
         return this.isRelative;
      }
   }
}
