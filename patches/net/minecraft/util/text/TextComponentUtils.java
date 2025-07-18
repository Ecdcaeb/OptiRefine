package net.minecraft.util.text;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TextComponentUtils {
   public static ITextComponent processComponent(ICommandSender var0, ITextComponent var1, Entity var2) throws CommandException {
      ITextComponent ☃;
      if (☃ instanceof TextComponentScore) {
         TextComponentScore ☃x = (TextComponentScore)☃;
         String ☃xx = ☃x.getName();
         if (EntitySelector.isSelector(☃xx)) {
            List<Entity> ☃xxx = EntitySelector.matchEntities(☃, ☃xx, Entity.class);
            if (☃xxx.size() != 1) {
               throw new EntityNotFoundException("commands.generic.selector.notFound", ☃xx);
            }

            Entity ☃xxxx = ☃xxx.get(0);
            if (☃xxxx instanceof EntityPlayer) {
               ☃xx = ☃xxxx.getName();
            } else {
               ☃xx = ☃xxxx.getCachedUniqueIdString();
            }
         }

         String ☃xxxx = ☃ != null && ☃xx.equals("*") ? ☃.getName() : ☃xx;
         ☃ = new TextComponentScore(☃xxxx, ☃x.getObjective());
         ((TextComponentScore)☃).setValue(☃x.getUnformattedComponentText());
         ((TextComponentScore)☃).resolve(☃);
      } else if (☃ instanceof TextComponentSelector) {
         String ☃x = ((TextComponentSelector)☃).getSelector();
         ☃ = EntitySelector.matchEntitiesToTextComponent(☃, ☃x);
         if (☃ == null) {
            ☃ = new TextComponentString("");
         }
      } else if (☃ instanceof TextComponentString) {
         ☃ = new TextComponentString(((TextComponentString)☃).getText());
      } else if (☃ instanceof TextComponentKeybind) {
         ☃ = new TextComponentKeybind(((TextComponentKeybind)☃).getKeybind());
      } else {
         if (!(☃ instanceof TextComponentTranslation)) {
            return ☃;
         }

         Object[] ☃x = ((TextComponentTranslation)☃).getFormatArgs();

         for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
            Object ☃xxxx = ☃x[☃xx];
            if (☃xxxx instanceof ITextComponent) {
               ☃x[☃xx] = processComponent(☃, (ITextComponent)☃xxxx, ☃);
            }
         }

         ☃ = new TextComponentTranslation(((TextComponentTranslation)☃).getKey(), ☃x);
      }

      Style ☃x = ☃.getStyle();
      if (☃x != null) {
         ☃.setStyle(☃x.createShallowCopy());
      }

      for (ITextComponent ☃xxxx : ☃.getSiblings()) {
         ☃.appendSibling(processComponent(☃, ☃xxxx, ☃));
      }

      return ☃;
   }
}
