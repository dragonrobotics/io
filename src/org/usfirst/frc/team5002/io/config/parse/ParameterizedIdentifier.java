package org.usfirst.frc.team5002.io.config.parse;

public class ParameterizedIdentifier {
    private String ident;
    private ArrayList<String> params;

    public constructor(String identifier, ArrayList<String> parameters) {
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public String identifier() { return ident; }
    public ArrayList<String> parameters() { return params; }
}
