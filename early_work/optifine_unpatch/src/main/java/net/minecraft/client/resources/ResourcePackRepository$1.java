/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.FileFilter
 *  java.lang.Object
 */
package net.minecraft.client.resources;

import java.io.File;
import java.io.FileFilter;

class ResourcePackRepository.1
implements FileFilter {
    ResourcePackRepository.1() {
    }

    public boolean accept(File p_accept_1_) {
        boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
        boolean flag1 = p_accept_1_.isDirectory() && new File(p_accept_1_, "pack.mcmeta").isFile();
        return flag || flag1;
    }
}
