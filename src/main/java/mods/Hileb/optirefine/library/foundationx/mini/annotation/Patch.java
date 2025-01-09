/*
 * Mini - an ASM-based class transformer reminiscent of MalisisCore and Mixin
 * 
 * The MIT License
 *
 * Copyright (c) 2017-2021 Una Thompson (unascribed) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mods.Hileb.optirefine.library.foundationx.mini.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This class is an annotation so it will appear in IDE autocomplete. Patch on its own is not
 * useful; it's a holder for other annotations.
 */
@SuppressWarnings("unused")
@Retention(SOURCE)
@Target({TYPE, METHOD})
public @interface Patch {

	/**
	 * Flags a MiniTransformer as affecting the given class.
	 */
	@Documented
	@Retention(RUNTIME)
	@Target(TYPE)
    @interface Class {
		String value();
	}

	/**
	 * Flags a method in a MiniTransformer as being a patcher for a given
	 * method. Can be specified multiple times to patch multiple methods in the
	 * same way. The method must take a PatchContext as its sole argument.
	 */
	@Documented
	@Retention(RUNTIME)
	@Target({METHOD, FIELD})
	@Repeatable(Methods.class)
    @interface Method {
		/**
		 * @return the runtime name and signature of the affected method, e.g. func_99999_d()V
		 */
		String value();

		/**
		 * Flags a method in a MiniTransformer as being an optional patcher. Normally, a patcher
		 * that does not match a method will cause an error to be thrown.
		 */
		@Documented
		@Retention(RUNTIME)
		@Target({METHOD, FIELD})
        @interface Optional {}
		/**
		 * Flags a method in a MiniTransformer as affecting control flow, and therefore requiring
		 * frames to be recomputed.
		 */
		@Documented
		@Retention(RUNTIME)
		@Target({METHOD, FIELD})
        @interface AffectsControlFlow {}
	}
	
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
    @interface Methods {
		Method[] value();
	}
	
}
