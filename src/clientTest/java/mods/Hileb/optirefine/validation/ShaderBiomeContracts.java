package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.optifine.expr.ConstantFloat;
import net.optifine.expr.IExpressionFloat;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderPackParser;
import net.optifine.shaders.uniform.CustomUniforms;
import net.optifine.shaders.uniform.ShaderExpressionResolver;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Properties;

final class ShaderBiomeContracts {
    private ShaderBiomeContracts() {}

    static void verify(Minecraft mc) throws ReflectiveOperationException {
        verifyExpressions();
        Properties properties = new Properties();
        properties.setProperty("uniform.int.contractCategory", "biome_category");
        properties.setProperty("uniform.int.contractPrecipitation", "biome_precipitation");
        CustomUniforms uniforms = ShaderPackParser.parseCustomUniforms(properties);
        if (uniforms == null) throw new AssertionError("G5 did not parse biome custom uniforms");

        // Sample a different column from the player and exercise both horizontal G5 origin offsets.
        BlockPos playerPos = mc.player.getPosition();
        BlockPos cameraPos = new BlockPos(playerPos.getX() ^ 1, 240, playerPos.getZ() ^ 1);
        byte[] biomes = mc.world.getChunk(cameraPos).getBiomeArray();
        int biomeIndex = (cameraPos.getZ() & 15) * 16 + (cameraPos.getX() & 15);
        byte originalBiome = biomes[biomeIndex];
        String[] names = {"currentWorld", "cameraPositionX", "cameraPositionY", "cameraPositionZ", "cameraOffsetX", "cameraOffsetZ"};
        Object[] values = {mc.world, cameraPos.getX() - 1000 + 0.25, 240.0, cameraPos.getZ() + 1000 + 0.25, 1000, -1000};
        Field[] fields = new Field[names.length];
        Object[] original = new Object[names.length];
        for (int index = 0; index < fields.length; index++) {
            fields[index] = Shaders.class.getDeclaredField(names[index]);
            fields[index].setAccessible(true);
            original[index] = fields[index].get(null);
        }

        int previousProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int program = ShaderLifecycleContracts.linkProgram("#version 130\nvoid main() { gl_Position = gl_Vertex; }", """
                #version 130
                uniform int contractCategory;
                uniform int contractPrecipitation;
                out vec4 color;
                void main() { color = vec4(float(contractCategory), float(contractPrecipitation), 0.0, 1.0); }
                """);
        try {
            GL20.glUseProgram(program);
            uniforms.setProgram(program);
            for (int index = 0; index < fields.length; index++) fields[index].set(null, values[index]);
            int categoryLocation = GL20.glGetUniformLocation(program, "contractCategory");
            int precipitationLocation = GL20.glGetUniformLocation(program, "contractPrecipitation");
            if (categoryLocation < 0 || precipitationLocation < 0) throw new AssertionError("Missing biome GLSL uniforms");
            IntBuffer result = BufferUtils.createIntBuffer(4);
            Sample[] samples = {
                    new Sample(Biomes.PLAINS, 5, 1),
                    new Sample(Biomes.DESERT, 12, 0),
                    new Sample(Biomes.COLD_TAIGA, 1, 2),
                    new Sample(Biomes.FROZEN_RIVER, 13, 2),
                    new Sample(Biomes.EXTREME_HILLS_WITH_TREES, 2, 1),
                    new Sample(Biomes.STONE_BEACH, 0, 1),
                    new Sample(Biomes.MUTATED_MESA, 4, 0)
            };
            for (Sample sample : samples) {
                biomes[biomeIndex] = (byte) Biome.getIdForBiome(sample.biome());
                if (mc.world.getBiome(cameraPos) != sample.biome()) throw new AssertionError("Biome fixture did not reach the camera");
                uniforms.update();
                expectUniform(program, categoryLocation, sample.category(), result, sample.biome().getBiomeName() + " category");
                expectUniform(program, precipitationLocation, sample.precipitation(), result, sample.biome().getBiomeName() + " precipitation");
            }
            fields[0].set(null, null);
            uniforms.update();
            expectUniform(program, categoryLocation, 0, result, "Worldless category");
            expectUniform(program, precipitationLocation, 0, result, "Worldless precipitation");
        } finally {
            biomes[biomeIndex] = originalBiome;
            GL20.glUseProgram(previousProgram);
            GL20.glDeleteProgram(program);
            for (int index = 0; index < fields.length; index++) fields[index].set(null, original[index]);
        }
    }

    private static void verifyExpressions() {
        ConstantFloat override = new ConstantFloat(42);
        ShaderExpressionResolver resolver = new ShaderExpressionResolver(Map.of("biome_category", override));
        if (resolver.getExpression("biome_category") != override) throw new AssertionError("Pack-defined expression was replaced");
        expectConstant(resolver, "BIOME_PLAINS", Biome.getIdForBiome(Biomes.PLAINS));
        String[] categories = {"NONE", "TAIGA", "EXTREME_HILLS", "JUNGLE", "MESA", "PLAINS", "SAVANNA", "ICY",
                "THE_END", "BEACH", "FOREST", "OCEAN", "DESERT", "RIVER", "SWAMP", "MUSHROOM", "NETHER"};
        for (int index = 0; index < categories.length; index++) expectConstant(resolver, "CAT_" + categories[index], index);
        expectConstant(resolver, "PPT_NONE", 0);
        expectConstant(resolver, "PPT_RAIN", 1);
        expectConstant(resolver, "PPT_SNOW", 2);
    }

    private static void expectConstant(ShaderExpressionResolver resolver, String name, int expected) {
        if (!(resolver.getExpression(name) instanceof IExpressionFloat constant) || constant.eval() != expected) {
            throw new AssertionError(name + " did not resolve to " + expected);
        }
    }

    private static void expectUniform(int program, int location, int expected, IntBuffer result, String name) {
        GL20.glGetUniform(program, location, result);
        int actual = result.get(0);
        if (actual != expected) throw new AssertionError(name + " uploaded " + actual + ", expected " + expected);
    }

    private record Sample(Biome biome, int category, int precipitation) {}
}
