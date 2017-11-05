package org.usfirst.frc.team5002.io.config.parse;

import java.util.ArrayList;

public class ParameterizedIdentifier {
    private String ident;
    private ArrayList<String> params;

    public ParameterizedIdentifier(
        String identifier, ArrayList<String> parameters
    ) {
        this.ident = identifier;
        this.params = parameters;
    }

    public String identifier() { return ident; }
    public ArrayList<String> parameters() { return params; }

    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(ident+'(');
        for(int i=0;i<params.size(); i++) {
            b.append(params.get(i));
            if(i != params.size()-1) b.append(',');
        }
        b.append(')');

        return b.toString();
    }
}
