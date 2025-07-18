package net.minecraft.advancements;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

public class AdvancementTreeNode {
   private final Advancement advancement;
   private final AdvancementTreeNode parent;
   private final AdvancementTreeNode sibling;
   private final int index;
   private final List<AdvancementTreeNode> children = Lists.newArrayList();
   private AdvancementTreeNode ancestor;
   private AdvancementTreeNode thread;
   private int x;
   private float y;
   private float mod;
   private float change;
   private float shift;

   public AdvancementTreeNode(Advancement var1, @Nullable AdvancementTreeNode var2, @Nullable AdvancementTreeNode var3, int var4, int var5) {
      if (☃.getDisplay() == null) {
         throw new IllegalArgumentException("Can't position an invisible advancement!");
      } else {
         this.advancement = ☃;
         this.parent = ☃;
         this.sibling = ☃;
         this.index = ☃;
         this.ancestor = this;
         this.x = ☃;
         this.y = -1.0F;
         AdvancementTreeNode ☃ = null;

         for (Advancement ☃x : ☃.getChildren()) {
            ☃ = this.buildSubTree(☃x, ☃);
         }
      }
   }

   @Nullable
   private AdvancementTreeNode buildSubTree(Advancement var1, @Nullable AdvancementTreeNode var2) {
      if (☃.getDisplay() != null) {
         ☃ = new AdvancementTreeNode(☃, this, ☃, this.children.size() + 1, this.x + 1);
         this.children.add(☃);
      } else {
         for (Advancement ☃ : ☃.getChildren()) {
            ☃ = this.buildSubTree(☃, ☃);
         }
      }

      return ☃;
   }

   private void firstWalk() {
      if (this.children.isEmpty()) {
         if (this.sibling != null) {
            this.y = this.sibling.y + 1.0F;
         } else {
            this.y = 0.0F;
         }
      } else {
         AdvancementTreeNode ☃ = null;

         for (AdvancementTreeNode ☃x : this.children) {
            ☃x.firstWalk();
            ☃ = ☃x.apportion(☃ == null ? ☃x : ☃);
         }

         this.executeShifts();
         float ☃x = (this.children.get(0).y + this.children.get(this.children.size() - 1).y) / 2.0F;
         if (this.sibling != null) {
            this.y = this.sibling.y + 1.0F;
            this.mod = this.y - ☃x;
         } else {
            this.y = ☃x;
         }
      }
   }

   private float secondWalk(float var1, int var2, float var3) {
      this.y += ☃;
      this.x = ☃;
      if (this.y < ☃) {
         ☃ = this.y;
      }

      for (AdvancementTreeNode ☃ : this.children) {
         ☃ = ☃.secondWalk(☃ + this.mod, ☃ + 1, ☃);
      }

      return ☃;
   }

   private void thirdWalk(float var1) {
      this.y += ☃;

      for (AdvancementTreeNode ☃ : this.children) {
         ☃.thirdWalk(☃);
      }
   }

   private void executeShifts() {
      float ☃ = 0.0F;
      float ☃x = 0.0F;

      for (int ☃xx = this.children.size() - 1; ☃xx >= 0; ☃xx--) {
         AdvancementTreeNode ☃xxx = this.children.get(☃xx);
         ☃xxx.y += ☃;
         ☃xxx.mod += ☃;
         ☃x += ☃xxx.change;
         ☃ += ☃xxx.shift + ☃x;
      }
   }

   @Nullable
   private AdvancementTreeNode getFirstChild() {
      if (this.thread != null) {
         return this.thread;
      } else {
         return !this.children.isEmpty() ? this.children.get(0) : null;
      }
   }

   @Nullable
   private AdvancementTreeNode getLastChild() {
      if (this.thread != null) {
         return this.thread;
      } else {
         return !this.children.isEmpty() ? this.children.get(this.children.size() - 1) : null;
      }
   }

   private AdvancementTreeNode apportion(AdvancementTreeNode var1) {
      if (this.sibling == null) {
         return ☃;
      } else {
         AdvancementTreeNode ☃ = this;
         AdvancementTreeNode ☃x = this;
         AdvancementTreeNode ☃xx = this.sibling;
         AdvancementTreeNode ☃xxx = this.parent.children.get(0);
         float ☃xxxx = this.mod;
         float ☃xxxxx = this.mod;
         float ☃xxxxxx = ☃xx.mod;

         float ☃xxxxxxx;
         for (☃xxxxxxx = ☃xxx.mod; ☃xx.getLastChild() != null && ☃.getFirstChild() != null; ☃xxxxx += ☃x.mod) {
            ☃xx = ☃xx.getLastChild();
            ☃ = ☃.getFirstChild();
            ☃xxx = ☃xxx.getFirstChild();
            ☃x = ☃x.getLastChild();
            ☃x.ancestor = this;
            float ☃xxxxxxxx = ☃xx.y + ☃xxxxxx - (☃.y + ☃xxxx) + 1.0F;
            if (☃xxxxxxxx > 0.0F) {
               ☃xx.getAncestor(this, ☃).moveSubtree(this, ☃xxxxxxxx);
               ☃xxxx += ☃xxxxxxxx;
               ☃xxxxx += ☃xxxxxxxx;
            }

            ☃xxxxxx += ☃xx.mod;
            ☃xxxx += ☃.mod;
            ☃xxxxxxx += ☃xxx.mod;
         }

         if (☃xx.getLastChild() != null && ☃x.getLastChild() == null) {
            ☃x.thread = ☃xx.getLastChild();
            ☃x.mod += ☃xxxxxx - ☃xxxxx;
         } else {
            if (☃.getFirstChild() != null && ☃xxx.getFirstChild() == null) {
               ☃xxx.thread = ☃.getFirstChild();
               ☃xxx.mod += ☃xxxx - ☃xxxxxxx;
            }

            ☃ = this;
         }

         return ☃;
      }
   }

   private void moveSubtree(AdvancementTreeNode var1, float var2) {
      float ☃ = ☃.index - this.index;
      if (☃ != 0.0F) {
         ☃.change -= ☃ / ☃;
         this.change += ☃ / ☃;
      }

      ☃.shift += ☃;
      ☃.y += ☃;
      ☃.mod += ☃;
   }

   private AdvancementTreeNode getAncestor(AdvancementTreeNode var1, AdvancementTreeNode var2) {
      return this.ancestor != null && ☃.parent.children.contains(this.ancestor) ? this.ancestor : ☃;
   }

   private void updatePosition() {
      if (this.advancement.getDisplay() != null) {
         this.advancement.getDisplay().setPosition(this.x, this.y);
      }

      if (!this.children.isEmpty()) {
         for (AdvancementTreeNode ☃ : this.children) {
            ☃.updatePosition();
         }
      }
   }

   public static void layout(Advancement var0) {
      if (☃.getDisplay() == null) {
         throw new IllegalArgumentException("Can't position children of an invisible root!");
      } else {
         AdvancementTreeNode ☃ = new AdvancementTreeNode(☃, null, null, 1, 0);
         ☃.firstWalk();
         float ☃x = ☃.secondWalk(0.0F, 0, ☃.y);
         if (☃x < 0.0F) {
            ☃.thirdWalk(-☃x);
         }

         ☃.updatePosition();
      }
   }
}
