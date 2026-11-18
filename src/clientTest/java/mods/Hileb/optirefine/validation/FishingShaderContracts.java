package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityFishHook;
import net.optifine.shaders.Program;
import net.optifine.shaders.ProgramStage;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

final class FishingShaderContracts {
    private FishingShaderContracts() {}

    static void verify(Minecraft mc) {
        int lineId = EntityShaderPixels.colorProgram("void main(){gl_FragColor=vec4(0.0,1.0,0.0,1.0);}");
        int entityId = EntityShaderPixels.colorProgram("void main(){gl_FragColor=vec4(1.0,0.0,1.0,1.0);}");
        Program entityProgram = new Program(Shaders.ProgramEntities.getIndex(), "contract_fishing_hook", ProgramStage.GBUFFERS, true);
        entityProgram.setId(entityId);
        int previousBasicId = Shaders.ProgramBasic.getId();
        int previousView = mc.gameSettings.thirdPersonView;
        EntityFishHook previousHook = mc.player.fishEntity;
        RenderManager manager = mc.getRenderManager();
        float previousYaw = manager.playerViewY;
        float previousPitch = manager.playerViewX;
        try {
            Shaders.ProgramBasic.setId(lineId);
            mc.gameSettings.thirdPersonView = 1;
            manager.playerViewY = 180.0F;
            manager.playerViewX = 0.0F;
            EntityFishHook hook = new EntityFishHook(mc.world, mc.player, mc.player.posX + 1.2D, mc.player.posY + 0.2D, mc.player.posZ);
            RenderFish renderer = new RenderFish(manager);
            ByteBuffer pixels = EntityShaderPixels.draw(entityProgram, () -> {
                GL11.glDisable(GL11.GL_CULL_FACE);
                renderer.doRender(hook, 1.2D, 0.2D, 0.0D, 0.0F, 1.0F);
                if (Shaders.activeProgram != entityProgram) throw new AssertionError("Fishing line leaked the basic shader program");
            });
            boolean line = false, bobber = false;
            for (int y = 0; y < EntityShaderPixels.SIZE; y++) for (int x = 0; x < EntityShaderPixels.SIZE; x++) {
                int pixel = EntityShaderPixels.pixel(pixels, x, y);
                line |= pixel == 0x00FF00;
                bobber |= pixel == 0xFF00FF;
            }
            if (!line || !bobber) {
                throw new AssertionError("Missing shader pixels for fishing geometry: line=" + line + ", bobber=" + bobber);
            }
        } finally {
            mc.player.fishEntity = previousHook;
            mc.gameSettings.thirdPersonView = previousView;
            manager.playerViewY = previousYaw;
            manager.playerViewX = previousPitch;
            Shaders.ProgramBasic.setId(previousBasicId);
            GL20.glDeleteProgram(entityId);
            GL20.glDeleteProgram(lineId);
        }
    }
}
