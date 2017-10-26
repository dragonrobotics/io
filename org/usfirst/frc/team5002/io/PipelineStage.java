package org.usfirst.frc.team5002.io;

@FunctionalInterface
public interface PipelineStage {
    public double execute(double input);
    public default double execute(boolean input) {
        return this.execute(input ? 1.0 : 0.0);
    }
}
