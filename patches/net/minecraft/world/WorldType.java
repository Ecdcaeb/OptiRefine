package net.minecraft.world;

public class WorldType {
   public static final WorldType[] WORLD_TYPES = new WorldType[16];
   public static final WorldType DEFAULT = new WorldType(0, "default", 1).setVersioned();
   public static final WorldType FLAT = new WorldType(1, "flat");
   public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
   public static final WorldType AMPLIFIED = new WorldType(3, "amplified").enableInfoNotice();
   public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
   public static final WorldType DEBUG_ALL_BLOCK_STATES = new WorldType(5, "debug_all_block_states");
   public static final WorldType DEFAULT_1_1 = new WorldType(8, "default_1_1", 0).setCanBeCreated(false);
   private final int id;
   private final String name;
   private final int version;
   private boolean canBeCreated;
   private boolean versioned;
   private boolean hasInfoNotice;

   private WorldType(int var1, String var2) {
      this(☃, ☃, 0);
   }

   private WorldType(int var1, String var2, int var3) {
      this.name = ☃;
      this.version = ☃;
      this.canBeCreated = true;
      this.id = ☃;
      WORLD_TYPES[☃] = this;
   }

   public String getName() {
      return this.name;
   }

   public String getTranslationKey() {
      return "generator." + this.name;
   }

   public String getInfoTranslationKey() {
      return this.getTranslationKey() + ".info";
   }

   public int getVersion() {
      return this.version;
   }

   public WorldType getWorldTypeForGeneratorVersion(int var1) {
      return this == DEFAULT && ☃ == 0 ? DEFAULT_1_1 : this;
   }

   private WorldType setCanBeCreated(boolean var1) {
      this.canBeCreated = ☃;
      return this;
   }

   public boolean canBeCreated() {
      return this.canBeCreated;
   }

   private WorldType setVersioned() {
      this.versioned = true;
      return this;
   }

   public boolean isVersioned() {
      return this.versioned;
   }

   public static WorldType byName(String var0) {
      for (WorldType ☃ : WORLD_TYPES) {
         if (☃ != null && ☃.name.equalsIgnoreCase(☃)) {
            return ☃;
         }
      }

      return null;
   }

   public int getId() {
      return this.id;
   }

   public boolean hasInfoNotice() {
      return this.hasInfoNotice;
   }

   private WorldType enableInfoNotice() {
      this.hasInfoNotice = true;
      return this;
   }
}
