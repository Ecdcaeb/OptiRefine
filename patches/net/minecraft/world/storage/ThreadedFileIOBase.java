package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
   private static final ThreadedFileIOBase INSTANCE = new ThreadedFileIOBase();
   private final List<IThreadedFileIO> threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
   private volatile long writeQueuedCounter;
   private volatile long savedIOCounter;
   private volatile boolean isThreadWaiting;

   private ThreadedFileIOBase() {
      Thread ☃ = new Thread(this, "File IO Thread");
      ☃.setPriority(1);
      ☃.start();
   }

   public static ThreadedFileIOBase getThreadedIOInstance() {
      return INSTANCE;
   }

   @Override
   public void run() {
      while (true) {
         this.processQueue();
      }
   }

   private void processQueue() {
      for (int ☃ = 0; ☃ < this.threadedIOQueue.size(); ☃++) {
         IThreadedFileIO ☃x = this.threadedIOQueue.get(☃);
         boolean ☃xx = ☃x.writeNextIO();
         if (!☃xx) {
            this.threadedIOQueue.remove(☃--);
            this.savedIOCounter++;
         }

         try {
            Thread.sleep(this.isThreadWaiting ? 0L : 10L);
         } catch (InterruptedException var6) {
            var6.printStackTrace();
         }
      }

      if (this.threadedIOQueue.isEmpty()) {
         try {
            Thread.sleep(25L);
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }
      }
   }

   public void queueIO(IThreadedFileIO var1) {
      if (!this.threadedIOQueue.contains(☃)) {
         this.writeQueuedCounter++;
         this.threadedIOQueue.add(☃);
      }
   }

   public void waitForFinish() throws InterruptedException {
      this.isThreadWaiting = true;

      while (this.writeQueuedCounter != this.savedIOCounter) {
         Thread.sleep(10L);
      }

      this.isThreadWaiting = false;
   }
}
