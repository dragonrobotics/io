package org.usfirst.frc.team5002.io;

/**
 * One stage in the pipeline process.
 * Now supports generic types for more flexible inputs and outputs.
 *
 * @author Brandon Gong
 * @version 1.0
 */
@FunctionalInterface
public interface PipelineStage<I, O> {
    public O execute(I i);
}
