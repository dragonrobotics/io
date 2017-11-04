/*
 * Copyright (c) 2017 Brandon Gong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.usfirst.frc.team5002.io.input.base;

import java.util.function.Supplier;
import org.usfirst.frc.team5002.io.pipeline.Pipeline;

/**
 * Input.java : defines the base input type.
 *
 * Each input has an instance of pipeline attached to it, and also methods
 * to get both raw and processed values.
 * We make use of the Supplier class in order to be able to use lambda
 * expressions without having a functional interface.
 *
 * TODO: Can Input be defined without knowing the end value of the pipeline?
 * this is strangely limiting.
 *
 * @author Brandon Gong
 * @version 1.0.0
 * Date: 11.3.17
 */
public class Input<R, P> {

    /**
     * Supplier that is called to actually get the value.
     */
    private final Supplier<? extends R> supplier;

    /**
     * Internal pipeline instance.
     */
    private Pipeline<R, P> pipeline;

    /**
     * Construct a new Input.
     * @param supplier the Supplier to use to get the value of this input.
     */
    public Input(Supplier<? extends R> supplier) {
        this.supplier = supplier;
    }

    /**
     * Get the raw value (that has not been passed through the pipeline).
     * Used for polling, when all we care about is whether or not the input
     * changed.
     */
    public R getRawValue() {
        return this.supplier.get();
    }

    /**
     * Get the processed, pipelined value.  Used in most other cases.
     */
    public P getValue() {
        return pipeline.run(this.supplier.get());
    }

    /**
     * Get the pipeline instance.
     */
    public Pipeline<R, P> getPipeline() {
        return pipeline;
    }

    /**
     * Set the pipeline instance.
     * TODO perhaps use setPipeline() for a more uniform naming style.
     */
    public void attach(Pipeline<R, P> pipeline) {
        this.pipeline = pipeline;
    }

}
