package org.usfirst.frc.team5002.io.config.parse;

public class PipelineSpecification {
    private String device;
    private String input;
    private ParameterizedIdentifier filter;
    private String callback;
    private ArrayList<ParameterizedIdentifier> stages;

    public constructor(
        String device, String input, ParameterizedIdentifier filter)
    {
        this.device = device;
        this.input = input;
        this.filter = filter;
    }

    public void setDevice(String device) { this.device = device; }
    public void setInput(String input) { this.input = input; }

    public void setFilter(ParameterizedIdentifier filter) {
        this.filter = filter;
    }

    public void setCallback(String callback) { this.callback = callback; }
    public void setStages(ArrayList<ParameterizedIdentifier> stages) {
        this.stages = stages;
    }

    public String device() { return this.device; }
    public String input() { return this.input; }
    public ParameterizedIdentifier filter() { return this.filter; }
    public String callback() { return this.callback; }
    public ArrayList<ParameterizedIdentifier> stages() { return this.stages; }
}
