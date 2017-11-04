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

package org.usfirst.frc.team5002.io.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class Pipeline represents a pipeline-style dataflow structure in which an
 * input undergoes a series of transformations, such as FOC offset, smoothing,
 * deadband, etc.
 *
 * As a major improvement over version 0.0.0.0.0.0.0.0.0.0.0.0.0.0.1, version
 * 1.0 includes support for generic data types, allowing a pipeline to take in
 * different data types and output different data types. Also, it allows
 * intermediate stages to output any data type, and compilation will fail if the
 * stages don't link together (as opposed to a runtime exception). This leads to
 * extremely typesafe yet flexible coding, as a pipeline can now accept a
 * multitude of data types.
 *
 * In order to make compile-time type checking possible, Pipelines are now
 * immutable.
 *
 * @author Brandon Gong
 * @version 1.0
 */
public class Pipeline<I, O> {

    /**
    * This list holds all of the stages in this pipeline.
    */
    private List<PipelineStage<?, ?>> stages;

    /**
     * Creates a new pipeline based off a starting value.
     * TODO I2 and O2 aren't really good names...
     *
     * @param stage the stage to initialize the pipeline with.
     * @return a single-staged pipeline of the given input and output types.
     */
    public static <I2, O2> Pipeline<I2, O2> of(PipelineStage<I2, O2> stage) {
        Pipeline<I2, O2> pipeline = new Pipeline<>();
        pipeline.stages = Collections.singletonList(stage);
        return pipeline;
    }

    /**
     * Adds a stage to the pipeline.
     *
     * @param stage the stage to add on.
     * @return a new pipeline, with the given stage appended.
     */
    public <M> Pipeline<I, M> add(PipelineStage<O, M> stage) {

        // Create a new pipeline with the same input type, but the new output
        // type.
        Pipeline<I, M> pipeline = new Pipeline<>();

        // initialize that pipeline's stages with the stages of this pipeline,
        // then add on the new stage.
        pipeline.stages = new ArrayList<>(this.stages);
        pipeline.stages.add(stage);

        return pipeline;
    }

    /**
     * Run the pipeline with the given value.
     *
     * Although \@SuppressWarnings is usually frowned upon, in this case we can go
     * ahead and do it because we can be sure that the types all match up due to the
     * way we designed the add() method.
     *
     * @param i the input value.
     * @return the result of the pipeline.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public O run(I i) {

        // Variable to hold the result of each stage (which subsequently becomes the
        // input of the next stage)
        Object data = i;

        // Iterate and execute all stages.
        for (PipelineStage p : stages) {
            data = p.execute(data);
        }

        // Have to explicitly cast here to the output value.
        return (O) data;
    }

    /**
     * Explicitly make the constructor private. Allowing the users to directly
     * instantiate through the constructor loses type safety.
     */
    private Pipeline() {}
}
