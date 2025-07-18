package net.minecraft.profiler;

public interface ISnooperInfo {
   void addServerStatsToSnooper(Snooper var1);

   void addServerTypeToSnooper(Snooper var1);

   boolean isSnooperEnabled();
}
