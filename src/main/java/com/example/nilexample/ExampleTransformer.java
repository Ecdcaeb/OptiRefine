package com.example.nilexample;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.Minecraft")
public class ExampleTransformer extends MiniTransformer {

	// Mini is the transformer framework bundled with NilLoader. It's pretty low level, but tries
	// to file off a lot of the sharp edges from doing ASM patches. This is a really minimal example
	// of a patch to just print out something when the Minecraft class static-inits. This is chosen
	// as the example as it works on multiple versions.
	
	// NilLoader will automatically reobfuscate references to classes, fields, and methods in your
	// patches based on your currently selected mapping. This patch carefully avoids obfuscated
	// things to provide a semi-version-agnostic example.
	
	@Patch.Method("<clinit>()V")
	public void patchClinit(PatchContext ctx) {
		ctx.jumpToLastReturn(); // Equivalent to "TAIL" in Mixin
		
		ctx.add(
			// This is the recommended way to do ASM hooks in NilLoader - invoke a helper defined
			// in an inner class for your transformer.
			INVOKESTATIC("com/example/nilexample/ExampleTransformer$Hooks", "onClinit", "()V")
		);
		
		// And, for the sake of illustration, let's also inject bytecode to invoke an entrypoint.
		// Mini provides a convenience method for this (which you must use; the bytecode it
		// generates calls a method that is not part of the API and may change.)
		ctx.addFireEntrypoint("example");
		
		// During execution of this method at this point, any nilmods that define an "example"
		// entrypoint will be invoked.
	}
	
	public static class Hooks {
		
		public static void onClinit() {
			NilExamplePremain.log.info("This message is printed by an example patch!");
		}
		
	}
	
}
