package net.minecraft.client.tutorial;

import java.util.function.Function;

public enum TutorialSteps {
   MOVEMENT("movement", MovementStep::new),
   FIND_TREE("find_tree", FindTreeStep::new),
   PUNCH_TREE("punch_tree", PunchTreeStep::new),
   OPEN_INVENTORY("open_inventory", OpenInventoryStep::new),
   CRAFT_PLANKS("craft_planks", CraftPlanksStep::new),
   NONE("none", CompletedTutorialStep::new);

   private final String name;
   private final Function<Tutorial, ? extends ITutorialStep> tutorial;

   private <T extends ITutorialStep> TutorialSteps(String var3, Function<Tutorial, T> var4) {
      this.name = ☃;
      this.tutorial = ☃;
   }

   public ITutorialStep create(Tutorial var1) {
      return this.tutorial.apply(☃);
   }

   public String getName() {
      return this.name;
   }

   public static TutorialSteps getTutorial(String var0) {
      for (TutorialSteps ☃ : values()) {
         if (☃.name.equals(☃)) {
            return ☃;
         }
      }

      return NONE;
   }
}
