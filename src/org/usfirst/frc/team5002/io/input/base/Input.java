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
