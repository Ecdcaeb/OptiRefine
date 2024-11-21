package mods.Hileb.optirefine.library.foundationx.asm;

import top.outlands.foundation.IExplicitTransformer;

/**
 * Many times, we specify the target of the Transformer,
 * perhaps in the class javadoc, as a copy of the list provided when registering.
 * Here, we do it directly, making it more convenient.
 * This class can only be implemented by {@link top.outlands.foundation.IExplicitTransformer}.
 * You must not provide any additional targets when registering, otherwise the return value of this method will be invalid.
 */
public interface ITargetedTransformer extends IExplicitTransformer {
    /**
     * @return the target classes, transformed names
     */
    String[] getTargets();
}
