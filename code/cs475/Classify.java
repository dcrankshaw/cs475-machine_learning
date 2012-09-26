package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;


import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
    static public LinkedList<Option> options = new LinkedList<Option>();

    public static void main(String[] args) throws IOException {
        // Parse the command line.
        String[] mandatory_args = { "mode"};
        createCommandLineOptions();
        CommandLineUtilities.initCommandLineParameters(args, Classify.options, mandatory_args);

        String mode = CommandLineUtilities.getOptionValue("mode");
        String data = CommandLineUtilities.getOptionValue("data");
        String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
        String algorithm = CommandLineUtilities.getOptionValue("algorithm");
        String model_file = CommandLineUtilities.getOptionValue("model_file");
        int max_decision_tree_depth = 4;
        if (CommandLineUtilities.hasArg("max_decision_tree_depth")) {
            max_decision_tree_depth = CommandLineUtilities.getOptionValueAsInt("max_decision_tree_depth");
        }

        if (mode.equalsIgnoreCase("train")) {
            if (data == null || algorithm == null || model_file == null) {
                System.out.println("Train requires the following arguments: data, algorithm, model_file");
                System.exit(0);
            }
            // Load the training data.
            DataReader data_reader = new DataReader(data, true);
            List<Instance> instances = data_reader.readData();
            data_reader.close();

            // Train the model.
            Predictor predictor = train(instances, algorithm, max_decision_tree_depth);
            saveObject(predictor, model_file);		

        } else if (mode.equalsIgnoreCase("test")) {
            if (data == null || predictions_file == null || model_file == null) {
                System.out.println("Train requires the following arguments: data, predictions_file, model_file");
                System.exit(0);
            }

            // Load the test data.
            DataReader data_reader = new DataReader(data, true);
            List<Instance> instances = data_reader.readData();
            data_reader.close();

            // Load the model.
            Predictor predictor = (Predictor)loadObject(model_file);
            if(predictor == null) {
              System.out.println("Null predictor.");
            }
            evaluateAndSavePredictions(predictor, instances, predictions_file);
        } else {
            System.out.println("Requires mode argument.");
        }
    }

    // for backwards compatibility - shouldn't ever get called
    private static Predictor train(List<Instance> instances, String algorithm) {
        return train(instances, algorithm, 0);
    }


    private static Predictor train(List<Instance> instances,
            String algorithm,
            int max_decision_tree_depth) {
        Predictor predictor;
        //TODO turn this into a case-switch statement
        if (algorithm.equalsIgnoreCase("majority")) {
            predictor = new MajorityPredictor();
        } else if (algorithm.equalsIgnoreCase("even_odd")) {
            predictor = new EvenOddPredictor();
        } else if (algorithm.equalsIgnoreCase("decision_tree")) {
            predictor = new DecisionTreePredictor(max_decision_tree_depth);
        } else {
            System.out.println("No matching algorithm.");
            return null;
        }
        predictor.train(instances);
        if (predictor instanceof DecisionTreePredictor) {
            DecisionTreePredictor printPredictor = (DecisionTreePredictor) predictor;
            printPredictor.printTree();
            System.out.println();
        }
        //Evaluate training data
        double trainEvaluation = AccuracyEvaluator.evaluate(instances, predictor);
        System.out.println("Training Evaluation: " + trainEvaluation);
        return predictor;
    }

    private static void evaluateAndSavePredictions(Predictor predictor,
            List<Instance> instances, String predictions_file) throws IOException {
        PredictionsWriter writer = new PredictionsWriter(predictions_file);
        double testEvaluation = AccuracyEvaluator.evaluate(instances, predictor);
        System.out.println("Testing Evaluation: " + testEvaluation);

        for (Instance instance : instances) {
            Label label = predictor.predict(instance);
            if (label == null) {
              System.out.println("Predicted null label.");
            }
            writer.writePrediction(label);
        }

        writer.close();

    }

    public static void saveObject(Object object, String file_name) {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(new BufferedOutputStream(
                                new FileOutputStream(new File(file_name))));
            oos.writeObject(object);
            oos.close();
        }
        catch (IOException e) {
            System.err.println("Exception writing file " + file_name + ": " + e);
        }
    }

    /**
     * Load a single object from a filename. 
     * @param file_name
     * @return
     */
    public static Object loadObject(String file_name) {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
            Object object = ois.readObject();
            ois.close();
            return object;
        } catch (IOException e) {
            System.err.println("Error loading: " + file_name);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading: " + file_name);
        }
        return null;
    }

    public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
        OptionBuilder.withArgName(arg_name);
        OptionBuilder.hasArg(has_arg);
        OptionBuilder.withDescription(description);
        Option option = OptionBuilder.create(option_name);

        Classify.options.add(option);		
    }

    private static void createCommandLineOptions() {
        registerOption("data", "String", true, "The data to use.");
        registerOption("mode", "String", true, "Operating mode: train or test.");
        registerOption("predictions_file", "String", true, "The predictions file to create.");
        registerOption("algorithm", "String", true, "The name of the algorithm for training.");
        registerOption("model_file", "String", true, "The name of the model file to create/load.");
        registerOption("max_decision_tree_depth", "int", true, "The maximum depth of the decision tree.");

        // Other options will be added here.
    }
}
