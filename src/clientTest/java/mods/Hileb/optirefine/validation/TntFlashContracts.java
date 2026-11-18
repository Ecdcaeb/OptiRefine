package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.optifine.shaders.Program;
import net.optifine.shaders.ProgramStage;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL20;

final class TntFlashContracts {
    private TntFlashContracts() {}

    static void verify(Minecraft mc) {
        int id = EntityShaderPixels.colorProgram("uniform vec4 entityColor; void main(){gl_FragColor=vec4(entityColor.rgb*entityColor.a,1.0);}");
        Program program = new Program(Shaders.ProgramEntities.getIndex(), "contract_tnt_flash", ProgramStage.GBUFFERS, true);
        program.setId(id);
        EntityTNTPrimed tnt = new EntityTNTPrimed(mc.world);
        tnt.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        RenderTNTPrimed renderer = new RenderTNTPrimed(mc.getRenderManager());
        try {
            tnt.setFuse(20);
            int flash = render(program, renderer, tnt);
            tnt.setFuse(25);
            int quiet = render(program, renderer, tnt);
            if (!nearGray(flash, Math.round((1.0F - 21.0F / 100.0F) * 0.8F * 255.0F)) || quiet != 0x400000) {
                throw new AssertionError(String.format("TNT entityColor pixels: flash=%06x quiet=%06x", flash, quiet));
            }
        } finally {
            GL20.glDeleteProgram(id);
        }
    }

    private static int render(Program program, RenderTNTPrimed renderer, EntityTNTPrimed tnt) {
        return EntityShaderPixels.pixel(EntityShaderPixels.draw(program, () -> {
            Shaders.setEntityColor(0.5F, 0.0F, 0.0F, 0.5F);
            renderer.doRender(tnt, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            float[] color = Shaders.uniform_entityColor.getValue();
            if (color[0] != 0.5F || color[1] != 0.0F || color[2] != 0.0F || color[3] != 0.5F) {
                throw new AssertionError("TNT flash replaced the enclosing entity color");
            }
        }), EntityShaderPixels.SIZE / 2, EntityShaderPixels.SIZE * 3 / 8);
    }

    private static boolean nearGray(int rgb, int shade) {
        return Math.abs((rgb >> 16) - shade) <= 2 && Math.abs(((rgb >> 8) & 255) - shade) <= 2
                && Math.abs((rgb & 255) - shade) <= 2;
    }
}
