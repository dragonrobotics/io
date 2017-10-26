package org.usfirst.frc.team5002.io;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    List<PipelineStage> steps = new ArrayList<>();
    public Pipeline add(PipelineStage pipelineStage) {
        steps.add(pipelineStage);
        return this;
    }
    public Pipeline addAll(PipelineStage... pipelineStages) {
        for(PipelineStage pipelineStage : pipelineStages) {
            steps.add(pipelineStage);
        }
        return this;
    }
    public double run(double input) {
        double result = input;
        for(PipelineStage step : steps) result = step.execute(result);
        return result;
    }
}
