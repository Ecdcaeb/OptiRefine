package mods.Hileb.optirefine.optifine.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.optifine.RandomEntities;

import java.util.LinkedHashMap;
import java.util.UUID;

public final class G5ShoulderParrots {
    private static final int MAX_RECENT_PARROTS = 128;
    // Entity tracking and player metadata can be delivered in either order.
    private static final LinkedHashMap<UUID, RecentParrot> recent = new LinkedHashMap<>();
    private static World clientWorld;

    private G5ShoulderParrots() {}

    public interface Player {
        EntityShoulderRiding optiRefine$getShoulderEntity(boolean left);
    }

    public static EntityShoulderRiding getShoulder(EntityPlayer player, boolean left) {
        return player instanceof Player tracked ? tracked.optiRefine$getShoulderEntity(left) : null;
    }

    public static void worldChanged(World world) {
        if (clientWorld != world) {
            recent.clear();
            clientWorld = world;
        }
    }

    public static EntityShoulderRiding updateShoulder(AbstractClientPlayer player, EntityShoulderRiding previous,
                                                     NBTTagCompound tag) {
        World world = player.world;
        worldChanged(world);
        UUID id = tag.hasUniqueId("UUID") && EntityList.getClassFromName(tag.getString("id")) == EntityParrot.class
                ? tag.getUniqueId("UUID") : null;
        if (previous instanceof EntityParrot oldParrot) {
            if (oldParrot.getUniqueID().equals(id)) return applyName(oldParrot, tag);
            remember(oldParrot, true);
        }
        if (id == null) return null;

        EntityParrot parrot = findSeated(world, id);
        if (parrot != null) return applyName(parrot, tag);
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityParrot tracked && id.equals(tracked.getUniqueID())) return applyName(tracked, tag);
        }
        RecentParrot detached = recent.remove(id);
        if (detached != null) return applyName(detached.entity(), tag);

        // The client only has the serialized entity when the player arrives with a seated parrot.
        parrot = new EntityParrot(world);
        parrot.readFromNBT(tag);
        if (!tag.hasKey("Pos", 9)) parrot.setPosition(player.posX, player.posY, player.posZ);
        RandomEntities.entityLoaded(parrot, world);
        return parrot;
    }

    private static EntityParrot applyName(EntityParrot parrot, NBTTagCompound tag) {
        parrot.setCustomNameTag(tag.getString("CustomName"));
        return parrot;
    }

    public static void untrack(EntityParrot parrot) {
        World world = parrot.world;
        worldChanged(world);
        if (findSeated(world, parrot.getUniqueID()) != parrot) remember(parrot, false);
    }

    public static EntityParrot getSpawnSource(EntityParrot parrot) {
        World world = parrot.world;
        worldChanged(world);

        UUID id = parrot.getUniqueID();
        EntityParrot seated = findSeated(world, id);
        RecentParrot detached = recent.remove(id);
        return seated != null ? seated : detached != null && detached.seated() ? detached.entity() : null;
    }

    private static EntityParrot findSeated(World world, UUID id) {
        for (EntityPlayer player : world.playerEntities) {
            if (getShoulder(player, true) instanceof EntityParrot left && id.equals(left.getUniqueID())) return left;
            if (getShoulder(player, false) instanceof EntityParrot right && id.equals(right.getUniqueID())) return right;
        }
        return null;
    }

    private static void remember(EntityParrot parrot, boolean seated) {
        UUID id = parrot.getUniqueID();
        recent.remove(id);
        recent.put(id, new RecentParrot(parrot, seated));
        if (recent.size() > MAX_RECENT_PARROTS) recent.pollFirstEntry();
    }

    private record RecentParrot(EntityParrot entity, boolean seated) {}
}
