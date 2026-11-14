package mods.Hileb.optirefine.core.mixin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModMixinPluginTest {
    @Test void integrationPolicySelectsOnlyTheDeclaredModNamespace() {
        assertEquals("example", ModMixinPlugin.requiredMod("mods.Hileb.optirefine.mixin.mods.example.client.MixinScreen"));
        assertEquals("example", ModMixinPlugin.requiredMod("mods.Hileb.optirefine.mixin.mods.example.MixinScreen"));
        assertNull(ModMixinPlugin.requiredMod("mods.Hileb.optirefine.mixin.defaults.example.MixinScreen"));
        assertNull(ModMixinPlugin.requiredMod("another.mods.Hileb.optirefine.mixin.mods.example.MixinScreen"));
        assertNull(ModMixinPlugin.requiredMod("mods.Hileb.optirefine.mixin.mods..MixinScreen"));
    }
}
