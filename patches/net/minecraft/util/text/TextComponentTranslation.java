package net.minecraft.util.text;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.translation.I18n;

public class TextComponentTranslation extends TextComponentBase {
   private final String key;
   private final Object[] formatArgs;
   private final Object syncLock = new Object();
   private long lastTranslationUpdateTimeInMilliseconds = -1L;
   @VisibleForTesting
   List<ITextComponent> children = Lists.newArrayList();
   public static final Pattern STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

   public TextComponentTranslation(String var1, Object... var2) {
      this.key = ☃;
      this.formatArgs = ☃;

      for (Object ☃ : ☃) {
         if (☃ instanceof ITextComponent) {
            ((ITextComponent)☃).getStyle().setParentStyle(this.getStyle());
         }
      }
   }

   @VisibleForTesting
   synchronized void ensureInitialized() {
      synchronized (this.syncLock) {
         long ☃ = I18n.getLastTranslationUpdateTimeInMilliseconds();
         if (☃ == this.lastTranslationUpdateTimeInMilliseconds) {
            return;
         }

         this.lastTranslationUpdateTimeInMilliseconds = ☃;
         this.children.clear();
      }

      try {
         this.initializeFromFormat(I18n.translateToLocal(this.key));
      } catch (TextComponentTranslationFormatException var6) {
         this.children.clear();

         try {
            this.initializeFromFormat(I18n.translateToFallback(this.key));
         } catch (TextComponentTranslationFormatException var5) {
            throw var6;
         }
      }
   }

   protected void initializeFromFormat(String var1) {
      boolean ☃ = false;
      Matcher ☃x = STRING_VARIABLE_PATTERN.matcher(☃);
      int ☃xx = 0;
      int ☃xxx = 0;

      try {
         while (☃x.find(☃xxx)) {
            int ☃xxxx = ☃x.start();
            int ☃xxxxx = ☃x.end();
            if (☃xxxx > ☃xxx) {
               TextComponentString ☃xxxxxx = new TextComponentString(String.format(☃.substring(☃xxx, ☃xxxx)));
               ☃xxxxxx.getStyle().setParentStyle(this.getStyle());
               this.children.add(☃xxxxxx);
            }

            String ☃xxxxxx = ☃x.group(2);
            String ☃xxxxxxx = ☃.substring(☃xxxx, ☃xxxxx);
            if ("%".equals(☃xxxxxx) && "%%".equals(☃xxxxxxx)) {
               TextComponentString ☃xxxxxxxx = new TextComponentString("%");
               ☃xxxxxxxx.getStyle().setParentStyle(this.getStyle());
               this.children.add(☃xxxxxxxx);
            } else {
               if (!"s".equals(☃xxxxxx)) {
                  throw new TextComponentTranslationFormatException(this, "Unsupported format: '" + ☃xxxxxxx + "'");
               }

               String ☃xxxxxxxx = ☃x.group(1);
               int ☃xxxxxxxxx = ☃xxxxxxxx != null ? Integer.parseInt(☃xxxxxxxx) - 1 : ☃xx++;
               if (☃xxxxxxxxx < this.formatArgs.length) {
                  this.children.add(this.getFormatArgumentAsComponent(☃xxxxxxxxx));
               }
            }

            ☃xxx = ☃xxxxx;
         }

         if (☃xxx < ☃.length()) {
            TextComponentString ☃xxxxxx = new TextComponentString(String.format(☃.substring(☃xxx)));
            ☃xxxxxx.getStyle().setParentStyle(this.getStyle());
            this.children.add(☃xxxxxx);
         }
      } catch (IllegalFormatException var12) {
         throw new TextComponentTranslationFormatException(this, var12);
      }
   }

   private ITextComponent getFormatArgumentAsComponent(int var1) {
      if (☃ >= this.formatArgs.length) {
         throw new TextComponentTranslationFormatException(this, ☃);
      } else {
         Object ☃ = this.formatArgs[☃];
         ITextComponent ☃x;
         if (☃ instanceof ITextComponent) {
            ☃x = (ITextComponent)☃;
         } else {
            ☃x = new TextComponentString(☃ == null ? "null" : ☃.toString());
            ☃x.getStyle().setParentStyle(this.getStyle());
         }

         return ☃x;
      }
   }

   @Override
   public ITextComponent setStyle(Style var1) {
      super.setStyle(☃);

      for (Object ☃ : this.formatArgs) {
         if (☃ instanceof ITextComponent) {
            ((ITextComponent)☃).getStyle().setParentStyle(this.getStyle());
         }
      }

      if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
         for (ITextComponent ☃x : this.children) {
            ☃x.getStyle().setParentStyle(☃);
         }
      }

      return this;
   }

   @Override
   public Iterator<ITextComponent> iterator() {
      this.ensureInitialized();
      return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
   }

   @Override
   public String getUnformattedComponentText() {
      this.ensureInitialized();
      StringBuilder ☃ = new StringBuilder();

      for (ITextComponent ☃x : this.children) {
         ☃.append(☃x.getUnformattedComponentText());
      }

      return ☃.toString();
   }

   public TextComponentTranslation createCopy() {
      Object[] ☃ = new Object[this.formatArgs.length];

      for (int ☃x = 0; ☃x < this.formatArgs.length; ☃x++) {
         if (this.formatArgs[☃x] instanceof ITextComponent) {
            ☃[☃x] = ((ITextComponent)this.formatArgs[☃x]).createCopy();
         } else {
            ☃[☃x] = this.formatArgs[☃x];
         }
      }

      TextComponentTranslation ☃xx = new TextComponentTranslation(this.key, ☃);
      ☃xx.setStyle(this.getStyle().createShallowCopy());

      for (ITextComponent ☃xxx : this.getSiblings()) {
         ☃xx.appendSibling(☃xxx.createCopy());
      }

      return ☃xx;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof TextComponentTranslation)) {
         return false;
      } else {
         TextComponentTranslation ☃ = (TextComponentTranslation)☃;
         return Arrays.equals(this.formatArgs, ☃.formatArgs) && this.key.equals(☃.key) && super.equals(☃);
      }
   }

   @Override
   public int hashCode() {
      int ☃ = super.hashCode();
      ☃ = 31 * ☃ + this.key.hashCode();
      return 31 * ☃ + Arrays.hashCode(this.formatArgs);
   }

   @Override
   public String toString() {
      return "TranslatableComponent{key='"
         + this.key
         + '\''
         + ", args="
         + Arrays.toString(this.formatArgs)
         + ", siblings="
         + this.siblings
         + ", style="
         + this.getStyle()
         + '}';
   }

   public String getKey() {
      return this.key;
   }

   public Object[] getFormatArgs() {
      return this.formatArgs;
   }
}
