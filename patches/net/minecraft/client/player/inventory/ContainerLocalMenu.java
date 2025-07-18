package net.minecraft.client.player.inventory;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class ContainerLocalMenu extends InventoryBasic implements ILockableContainer {
   private final String guiID;
   private final Map<Integer, Integer> dataValues = Maps.newHashMap();

   public ContainerLocalMenu(String var1, ITextComponent var2, int var3) {
      super(☃, ☃);
      this.guiID = ☃;
   }

   @Override
   public int getField(int var1) {
      return this.dataValues.containsKey(☃) ? this.dataValues.get(☃) : 0;
   }

   @Override
   public void setField(int var1, int var2) {
      this.dataValues.put(☃, ☃);
   }

   @Override
   public int getFieldCount() {
      return this.dataValues.size();
   }

   @Override
   public boolean isLocked() {
      return false;
   }

   @Override
   public void setLockCode(LockCode var1) {
   }

   @Override
   public LockCode getLockCode() {
      return LockCode.EMPTY_CODE;
   }

   @Override
   public String getGuiID() {
      return this.guiID;
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      throw new UnsupportedOperationException();
   }
}
