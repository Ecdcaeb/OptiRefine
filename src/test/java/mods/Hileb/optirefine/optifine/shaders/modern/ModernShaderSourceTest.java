package mods.Hileb.optirefine.optifine.shaders.modern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModernShaderSourceTest {
    // Use G5's real MacroState and ShaderParser without initializing Shaders or Config.
    // In particular, #ifdef/#ifndef exercise conditionals without the client expression runtime.
    @Test void discoversAliasedImageTypesIncludingIntegerImages() {
        for (String type : new String[]{"image2D", "iimage2D", "uimage2D"}) {
            ModernShaderSource source = new ModernShaderSource();
            source.accept("#define STORAGE_TYPE " + type + "\n"
                    + "#define IMAGE_TYPE STORAGE_TYPE\n"
                    + "layout(rgba8) uniform coherent writeonly highp IMAGE_TYPE colorimg2;");

            assertNull(source.error, type);
            assertTrue(source.images, type);
            assertEquals(1 << 2, source.imageMask, type);
            assertEquals(3, source.colors, type);
        }
    }

    @Test void discoversAliasedMainAndShadowImageNamesWithSeparateMaskBits() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define MAIN_FIRST colorimg0
                #define MAIN_LAST colorimg5
                #define MAIN_ALIAS MAIN_LAST
                #define SHADOW_FIRST shadowcolorimg0
                #define SHADOW_LAST shadowcolorimg1
                layout(rgba8) uniform image2D MAIN_FIRST;
                layout(rgba8) uniform image2D MAIN_ALIAS;
                layout(rgba8) uniform image2D SHADOW_FIRST;
                layout(rgba8) uniform image2D SHADOW_LAST;
                """);

        assertNull(source.error);
        assertTrue(source.images);
        assertEquals((1 << 0) | (1 << 5) | (1 << 6) | (1 << 7), source.imageMask);
        assertEquals(6, source.colors);
        assertEquals(2, source.shadowColors);
        assertEquals(1, source.shadowDepths);
    }

    @Test void discoversAliasesAcrossAcceptedIncludeChunks() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("#define IMAGE_TYPE image2D\n#define OUTPUT_IMAGE colorimg4");
        source.accept("#line 12 1\nlayout(rgba8) uniform readonly IMAGE_TYPE OUTPUT_IMAGE;");

        assertNull(source.error);
        assertEquals(1 << 4, source.imageMask);
        assertEquals(5, source.colors);
    }

    @Test void discoversOnlyActiveConditionalUniformsAndDefinitions() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define ENABLE_IMAGE
                #define IMAGE_TYPE image2D
                #define OUTPUT_IMAGE colorimg1
                #ifdef ENABLE_IMAGE
                layout(rgba8) uniform IMAGE_TYPE OUTPUT_IMAGE;
                #else
                uniform sampler2D colorimg9;
                #endif
                #ifndef ENABLE_IMAGE
                #define OUTPUT_IMAGE colorimg5
                uniform image2D shadowcolorimg1;
                #endif
                uniform IMAGE_TYPE OUTPUT_IMAGE;
                """);

        assertNull(source.error);
        assertEquals(1 << 1, source.imageMask);
        assertEquals(2, source.colors);
        assertEquals(0, source.shadowColors);
        assertEquals(0, source.shadowDepths);
    }

    @Test void undefRemovesAliasesAndRedefinitionChangesSubsequentUniforms() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define IMAGE_TYPE image2D
                #define OUTPUT_IMAGE colorimg5
                #undef OUTPUT_IMAGE
                uniform IMAGE_TYPE OUTPUT_IMAGE;
                #ifdef OUTPUT_IMAGE
                uniform image2D colorimg4;
                #else
                #define OUTPUT_IMAGE shadowcolorimg0
                uniform IMAGE_TYPE OUTPUT_IMAGE;
                #endif
                """);

        assertNull(source.error);
        assertEquals(1 << 6, source.imageMask);
        assertEquals(0, source.colors);
        assertEquals(1, source.shadowColors);
        assertEquals(1, source.shadowDepths);
    }

    @Test void rejectsAliasedUnsupportedImageTypesAndSlots() {
        String[][] declarations = {
                {"sampler2D", "colorimg0"},
                {"image2D", "colorimg6"},
                {"image2D", "shadowcolorimg2"}
        };
        for (String[] declaration : declarations) {
            ModernShaderSource source = new ModernShaderSource();
            source.accept("#define IMAGE_TYPE " + declaration[0] + "\n"
                    + "#define OUTPUT_IMAGE " + declaration[1] + "\n"
                    + "layout(rgba8) uniform IMAGE_TYPE OUTPUT_IMAGE;");

            assertNotNull(source.error, declaration[1]);
            assertTrue(source.error.contains(declaration[0] + " " + declaration[1]));
            assertFalse(source.images);
            assertEquals(0, source.imageMask);
        }
    }

    @Test void preservesSamplerAndCenterDepthDiscoveryThroughAliases() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define MAIN_COLOR gaux3
                #define MAIN_DEPTH depthtex2
                #define SHADOW_COLOR shadowcolor1
                #define SHADOW_DEPTH watershadow
                #define CENTER_DEPTH centerDepthSmooth
                uniform sampler2D MAIN_COLOR;
                uniform sampler2D MAIN_DEPTH;
                uniform sampler2D SHADOW_COLOR;
                uniform sampler2D SHADOW_DEPTH;
                uniform float CENTER_DEPTH;
                """);

        assertNull(source.error);
        assertEquals(7, source.colors);
        assertEquals(3, source.depths);
        assertEquals(2, source.shadowColors);
        assertEquals(2, source.shadowDepths);
        assertTrue(source.centerDepthSmooth);
        assertFalse(source.images);
    }

    @Test void preservesCommentsAndMacroExpandedDispatchMetadata() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define IMAGE_TYPE image2D // selected option
                #define OUTPUT_IMAGE colorimg3
                #define SIZE 8
                #define GROUPS ivec3(2,3,1)
                #define SCALE vec2(0.5F,0.25f)
                /* uniform image2D shadowcolorimg1;
                uniform sampler2D colorimg9; */
                layout(local_size_x=SIZE,local_size_y=SIZE,local_size_z=1) in;
                const ivec3 workGroups=GROUPS;
                const vec2 workGroupsRender=SCALE;
                layout(rgba8) uniform /* storage */ IMAGE_TYPE OUTPUT_IMAGE;
                // uniform image2D colorimg5;
                """);

        assertNull(source.error);
        assertEquals(1 << 3, source.imageMask);
        assertEquals(4, source.colors);
        assertEquals(0, source.shadowColors);
        assertTrue(source.localSize);
        assertEquals(2, source.groupsX);
        assertEquals(3, source.groupsY);
        assertEquals(1, source.groupsZ);
        assertEquals(0.5F, source.relativeX);
        assertEquals(0.25F, source.relativeY);
    }

    @Test void discoversOnlyActiveDrawTargetsAndExtendedSamplers() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("""
                #define AUX colortex15
                #ifdef INACTIVE
                /* RENDERTARGETS: 17 */
                uniform sampler2D colortex16;
                #endif
                /* DRAWBUFFERS:08 */
                /* RENDERTARGETS: 15, N, 2 */
                uniform sampler2D AUX;
                const int colortex15Format = RGBA16F;
                const bool colortex15MipmapEnabled = true;
                """);
        assertNull(source.error);
        assertNull(source.drawTargetsError);
        assertArrayEquals(new int[]{15, -1, 2}, source.drawTargets.buffers());
        assertEquals(1 << 15, source.extendedColors);
        assertEquals(1 << 15, source.mipmaps);
        assertEquals("RGBA16F", source.extendedSettings.getFirst().getValue());
    }

    @Test void separatesLegacyTargetOrderFromInvalidModernOutputs() {
        ModernShaderSource source = new ModernShaderSource();
        source.accept("// DRAWBUFFERS:908N");
        assertArrayEquals(new int[]{9, 0, 8, -1}, source.drawTargets.buffers());
        source.accept("/* RENDERTARGETS: 0,16 */");
        assertTrue(source.drawTargetsError.contains("16"));
        source.accept("/* RENDERTARGETS: 11,11 */");
        assertTrue(source.drawTargetsError.contains("duplicate"));
        source.accept("uniform sampler2D colortex16;");
        assertTrue(source.error.contains("colortex16"));
    }
}
