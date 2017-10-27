package org.usfirst.frc.team5002.io;

/**
 * One stage in the pipeline process. Each stage takes a double value,
 * transforms it somehow, and spits out another one, which flows to the next
 * pipeline stage.
 *
 * @author Brandon Gong
 */
@FunctionalInterface
public interface PipelineStage {
    public double execute(double input);
}
