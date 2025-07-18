package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Set<EntityAITasks.EntityAITaskEntry> taskEntries = Sets.newLinkedHashSet();
   private final Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries = Sets.newLinkedHashSet();
   private final Profiler profiler;
   private int tickCount;
   private int tickRate = 3;
   private int disabledControlFlags;

   public EntityAITasks(Profiler var1) {
      this.profiler = ☃;
   }

   public void addTask(int var1, EntityAIBase var2) {
      this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(☃, ☃));
   }

   public void removeTask(EntityAIBase var1) {
      Iterator<EntityAITasks.EntityAITaskEntry> ☃ = this.taskEntries.iterator();

      while (☃.hasNext()) {
         EntityAITasks.EntityAITaskEntry ☃x = ☃.next();
         EntityAIBase ☃xx = ☃x.action;
         if (☃xx == ☃) {
            if (☃x.using) {
               ☃x.using = false;
               ☃x.action.resetTask();
               this.executingTaskEntries.remove(☃x);
            }

            ☃.remove();
            return;
         }
      }
   }

   public void onUpdateTasks() {
      this.profiler.startSection("goalSetup");
      if (this.tickCount++ % this.tickRate == 0) {
         for (EntityAITasks.EntityAITaskEntry ☃ : this.taskEntries) {
            if (☃.using) {
               if (!this.canUse(☃) || !this.canContinue(☃)) {
                  ☃.using = false;
                  ☃.action.resetTask();
                  this.executingTaskEntries.remove(☃);
               }
            } else if (this.canUse(☃) && ☃.action.shouldExecute()) {
               ☃.using = true;
               ☃.action.startExecuting();
               this.executingTaskEntries.add(☃);
            }
         }
      } else {
         Iterator<EntityAITasks.EntityAITaskEntry> ☃x = this.executingTaskEntries.iterator();

         while (☃x.hasNext()) {
            EntityAITasks.EntityAITaskEntry ☃xx = ☃x.next();
            if (!this.canContinue(☃xx)) {
               ☃xx.using = false;
               ☃xx.action.resetTask();
               ☃x.remove();
            }
         }
      }

      this.profiler.endSection();
      if (!this.executingTaskEntries.isEmpty()) {
         this.profiler.startSection("goalTick");

         for (EntityAITasks.EntityAITaskEntry ☃x : this.executingTaskEntries) {
            ☃x.action.updateTask();
         }

         this.profiler.endSection();
      }
   }

   private boolean canContinue(EntityAITasks.EntityAITaskEntry var1) {
      return ☃.action.shouldContinueExecuting();
   }

   private boolean canUse(EntityAITasks.EntityAITaskEntry var1) {
      if (this.executingTaskEntries.isEmpty()) {
         return true;
      } else if (this.isControlFlagDisabled(☃.action.getMutexBits())) {
         return false;
      } else {
         for (EntityAITasks.EntityAITaskEntry ☃ : this.executingTaskEntries) {
            if (☃ != ☃) {
               if (☃.priority >= ☃.priority) {
                  if (!this.areTasksCompatible(☃, ☃)) {
                     return false;
                  }
               } else if (!☃.action.isInterruptible()) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry var1, EntityAITasks.EntityAITaskEntry var2) {
      return (☃.action.getMutexBits() & ☃.action.getMutexBits()) == 0;
   }

   public boolean isControlFlagDisabled(int var1) {
      return (this.disabledControlFlags & ☃) > 0;
   }

   public void disableControlFlag(int var1) {
      this.disabledControlFlags |= ☃;
   }

   public void enableControlFlag(int var1) {
      this.disabledControlFlags &= ~☃;
   }

   public void setControlFlag(int var1, boolean var2) {
      if (☃) {
         this.enableControlFlag(☃);
      } else {
         this.disableControlFlag(☃);
      }
   }

   class EntityAITaskEntry {
      public final EntityAIBase action;
      public final int priority;
      public boolean using;

      public EntityAITaskEntry(int var2, EntityAIBase var3) {
         this.priority = ☃;
         this.action = ☃;
      }

      @Override
      public boolean equals(@Nullable Object var1) {
         if (this == ☃) {
            return true;
         } else {
            return ☃ != null && this.getClass() == ☃.getClass() ? this.action.equals(((EntityAITasks.EntityAITaskEntry)☃).action) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.action.hashCode();
      }
   }
}
