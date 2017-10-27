package org.usfirst.frc.team5002.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Pipeline represents a pipeline-style dataflow structure in which an
 * input undergoes a series of transformations, such as FOC offset, smoothing,
 * deadband, etc.
 *
 * TODO maybe add remove(), add(PipelineStage, index), clone() methods. We'll
 *      see.
 *
 * @author Brandon Gong
 * @version 0.0.0.0.0.0.0.0.0.0.0.0.0.0.1
 */
public class Pipeline {

    /**
     * This ArrayList holds all of the steps of this particular pipeline
     * instance.
     */
    private List<PipelineStage> steps = new ArrayList<>();

    /**
     * Allows for literate, readable, clean code. Constructs a new
     * pipeline from the given steps. This replaces having a constructor.
     *
     * @param pipelineStages the stages to create the new pipeline with.
     */
    public static Pipeline of(PipelineStage... pipelineStages) {
        return new Pipeline().addAll(pipelineStages);
    }

    /**
     * Adds a PipelineStage to the list of steps.
     * Returns this instance so if you want to be a cool kid and chain method
     * calls like this:
     * Pipeline p = new Pipeline().add(step1).add(step2);
     *
     * @param pipelineStage the stage to add on to the pipeline.
     */
    public Pipeline add(PipelineStage pipelineStage) {
        steps.add(pipelineStage);
        return this;
    }

    /**
     * However if you ever get tired of being a cool kid you can just call
     * addAll() and add everything at once.
     *
     * @param pipelineStages the stages to add on to the end of the pipeline.
     */
    public Pipeline addAll(PipelineStage... pipelineStages) {
        for(PipelineStage pipelineStage : pipelineStages) {
            steps.add(pipelineStage);
        }
        return this;
    }

    /**
     * Run the pipeline with the specified input.
     * @param input the input.
     */
    public double run(double input) {
        double result = input;
        for(PipelineStage step : steps) result = step.run(result);
        return result;
    }

    /**
     * Simple method overload converts boolean to double before calling
     * the real run() method. So we don't have to have ternaries sprinkled
     * all over our code to convert from button inputs to double values.
     */
    public double run(boolean input) {
        return this.run(input ? 1.0 : 0.0);
    }
}
