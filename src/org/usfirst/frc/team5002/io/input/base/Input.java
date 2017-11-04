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

public class Input<R, P> {

    private final Supplier<? extends R> supplier;
    private Pipeline<R, P> pipeline;

    public Input(Supplier<? extends R> supplier) {
        this.supplier = supplier;
    }

    public R getRawValue() {
        return this.supplier.get();
    }

    public P getValue() {
        return pipeline.run(this.supplier.get());
    }

    public Pipeline<R, P> getPipeline() {
        return pipeline;
    }

    public void attach(Pipeline<R, P> pipeline) {
        this.pipeline = pipeline;
    }

}
