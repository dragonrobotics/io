package org.usfirst.frc.team5002.io;

/**
 * Temporary test class to demonstrate the new pipeline.
 * @author Brandon Gong
 */
public class Test {

    public static void main(String[] args) {

        /*
         * Method 1: creating instances separately, then passing them in.
         * This works, but now we have all these unwanted reference variables
         * floating around that we really don't want to have anything to do
         * with.
         */
        PipelineStage<String, Integer> stage1 = new PipelineStage<String, Integer>() {
            @Override
            public Integer execute(String input) {
                return Integer.valueOf(input);
            }
        };

        PipelineStage<Integer, Boolean> stage2 = new PipelineStage<Integer, Boolean>() {
            @Override
            public Boolean execute(Integer input) {
                return input == 12;
            }
        };

        PipelineStage<Boolean, Double> stage3 = new PipelineStage<Boolean, Double>() {
            @Override
            public Double execute(Boolean input) {
                return input ? 3.1415926535 : 2.7182818284;
            }
        };

        Pipeline<String, Double> pipeline = Pipeline.of(stage1).add(stage2).add(stage3);
        System.out.println(pipeline.run("12") + " " + pipeline.run("134355351"));

        /*
         * Method 2: creating instances separately, then passing them in.
         * This works, but is a little bit messy.
         */
        Pipeline<String, Double> pipeline2 =
            Pipeline.of(
                new PipelineStage<String, Integer>() {
                    @Override
                    public Integer execute(String input) {
                        return Integer.valueOf(input);
                    }
                }
            ).add(
                new PipelineStage<Integer, Boolean>() {
                    @Override
                    public Boolean execute(Integer input) {
                        return input == 12;
                    }
                }
            ).add(
                new PipelineStage<Boolean, Double>() {
                    @Override
                    public Double execute(Boolean input) {
                        return input ? 3.1415926535 : 2.7182818284;
                    }
                }
            );
        System.out.println("\n" + pipeline2.run("12") + " " +  pipeline2.run("134355351"));

        /*
         * Method 3: lambda expressions. Probably the most recommended way to
         * do things. Clean and concise.
         */
        Pipeline<String, Double> pipeline3 =
                             Pipeline.of((String input) -> Integer.valueOf(input))
                                     .add(input -> input == 12)
                                     .add(input -> input ? 3.1415926535 : 2.7182818284);
        System.out.println("\n" + pipeline3.run("12") + " " +  pipeline3.run("134355351"));
    }
}
