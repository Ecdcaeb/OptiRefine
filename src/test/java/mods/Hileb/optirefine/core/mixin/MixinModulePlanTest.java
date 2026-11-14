package mods.Hileb.optirefine.core.mixin;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MixinModulePlanTest {
    @Test
    void preservesBlockOrderAndFiltersClientAndServerEntries() {
        MixinModulePlan plan = parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"},{"id":"server-fix","requires":["g5-base"],"side":"server"}],
                 "blocks":[{"module":"g5-base","side":"common","mixins":["Common$Inner"]},{"module":"g5-base","side":"client","mixins":["ClientA"]},{"module":"server-fix","side":"server","mixins":["ServerA"]},{"module":"g5-base","side":"client","mixins":["ClientB"]}]}
                """);
        assertEquals(List.of("Common$Inner", "ClientA", "ClientB"), plan.mixins(true));
        assertEquals(List.of("Common$Inner", "ServerA"), plan.mixins(false));
    }

    @Test
    void commonMixinsPrecedeSideMixinsAcrossModuleBlocks() {
        MixinModulePlan plan = parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"}],
                 "blocks":[{"module":"g5-base","side":"client","mixins":["Client"]},{"module":"g5-base","side":"common","mixins":["Common"]}]}
                """);
        assertEquals(List.of("Common", "Client"), plan.mixins(true));
        assertEquals(List.of("Common"), plan.mixins(false));
    }

    @Test
    void rejectsMissingRequiredBaseAndUnknownModule() {
        assertThrows(IllegalArgumentException.class, () -> parse("""
                {"modules":[],"blocks":[]}
                """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"}],"blocks":[{"module":"unknown","side":"common","mixins":[]}]}
                """));
    }

    @Test
    void rejectsMissingDependenciesCyclesAndDuplicateMixins() {
        assertThrows(IllegalArgumentException.class, () -> parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"},{"id":"broken","requires":["missing"],"side":"both"}],"blocks":[{"module":"g5-base","side":"common","mixins":[]}]}
                """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
                {"modules":[{"id":"g5-base","requires":["loop"],"side":"both"},{"id":"loop","requires":["g5-base"],"side":"both"}],"blocks":[{"module":"g5-base","side":"common","mixins":[]}]}
                """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"}],"blocks":[{"module":"g5-base","side":"common","mixins":["A"]},{"module":"g5-base","side":"client","mixins":["A"]}]}
                """));
    }

    @Test
    void rejectsSelectedFeatureWhenItsProviderIsUnavailableOnThatSide() {
        MixinModulePlan plan = parse("""
                {"modules":[{"id":"g5-base","requires":[],"side":"both"},{"id":"client-provider","requires":["g5-base"],"side":"server"},{"id":"client-feature","requires":["client-provider"],"side":"client"}],
                 "blocks":[{"module":"g5-base","side":"common","mixins":["Base"]},{"module":"client-feature","side":"client","mixins":["Feature"]}]}
                """);
        assertThrows(IllegalArgumentException.class, () -> plan.mixins(true));
        assertEquals(List.of("Base"), plan.mixins(false));
    }

    private static MixinModulePlan parse(String json) {
        return MixinModulePlan.parse(new StringReader(json));
    }
}
