package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
    static public LinkedList<Option> options = new LinkedList<Option>();
    public static int max_decision_tree_depth = 4;
    public static double lambda = 1.0;
    public static int online_training_iterations = 1;
    public static double online_learning_rate = 1.0;
    public static double thickness = 0.0;
    public static double gradient_ascent_learning_rate = 0.01;
    public static int gradient_ascent_training_iterations = 5;
    public static double polynomial_kernel_exponent = 2.0;
    public static double gaussian_kernel_sigma = 1.0;
    public static int k_nn = 5;
    public static int k_ensemble = 5;
    public static double ensemble_learning_rate = 0.1;
    public static int ensemble_training_iterations = 5;
    public static double cluster_lambda = 0.0;
    public static int clustering_training_iterations = 10;
    public static String algorithm;

    public static void main(String[] args) throws IOException {
        // Parse the command line.
        String[] mandatory_args = { "mode", "algorithm"};
        createCommandLineOptions();
        CommandLineUtilities.initCommandLineParameters(args, Classify.options, mandatory_args);

        String mode = CommandLineUtilities.getOptionValue("mode");
        String data = CommandLineUtilities.getOptionValue("data");
        String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
        algorithm = CommandLineUtilities.getOptionValue("algorithm");
        String model_file = CommandLineUtilities.getOptionValue("model_file");
        if (CommandLineUtilities.hasArg("max_decision_tree_depth")) {
            max_decision_tree_depth = CommandLineUtilities.getOptionValueAsInt("max_decision_tree_depth");
        }
        if (CommandLineUtilities.hasArg("lambda")) {
            lambda = CommandLineUtilities.getOptionValueAsFloat("lambda");
        }
        if (CommandLineUtilities.hasArg("online_training_iterations")) {
            online_training_iterations = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
        }
        if (CommandLineUtilities.hasArg("online_learning_rate")) {
            online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");
        }
        if (CommandLineUtilities.hasArg("thickness")) {
            thickness = CommandLineUtilities.getOptionValueAsFloat("thickness");
        }
        if (CommandLineUtilities.hasArg("gradient_ascent_training_iterations")) {
            gradient_ascent_training_iterations =
                CommandLineUtilities.getOptionValueAsInt("gradient_ascent_training_iterations");
        }
        if (CommandLineUtilities.hasArg("gradient_ascent_learning_rate")) {
            gradient_ascent_learning_rate =
                CommandLineUtilities.getOptionValueAsFloat("gradient_ascent_learning_rate");
        }
        if (CommandLineUtilities.hasArg("polynomial_kernel_exponent")) {
            polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsFloat("polynomial_kernel_exponent");
        }
        if (CommandLineUtilities.hasArg("gaussian_kernel_sigma")) {
            gaussian_kernel_sigma = CommandLineUtilities.getOptionValueAsFloat("gaussian_kernel_sigma");
        }
        if (CommandLineUtilities.hasArg("k_nn")) {
            k_nn = CommandLineUtilities.getOptionValueAsInt("k_nn");
        }
        if (CommandLineUtilities.hasArg("k_ensemble")) {
            k_ensemble = CommandLineUtilities.getOptionValueAsInt("k_ensemble");
        }
        if (CommandLineUtilities.hasArg("ensemble_learning_rate")) {
            ensemble_learning_rate = CommandLineUtilities.getOptionValueAsFloat("ensemble_learning_rate");
        }
        if (CommandLineUtilities.hasArg("ensemble_training_iterations")) {
            ensemble_training_iterations = CommandLineUtilities.getOptionValueAsInt("ensemble_training_iterations");
        }
        if (CommandLineUtilities.hasArg("cluster_lambda")) {
            cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");
        }
        if (CommandLineUtilities.hasArg("clustering_training_iterations")) {
            clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
        }


        if (mode.equalsIgnoreCase("train")) {
            if (data == null || algorithm == null || model_file == null) {
                System.out.println("Train requires the following arguments: data, algorithm, model_file");
                System.exit(0);
            }
            online_learning_rate = algorithm.equals("winnow") ? 2.0 : 1.0;
            // Load the training data.
            boolean classification = true;
            if (algorithm.equalsIgnoreCase("knn") || algorithm.equalsIgnoreCase("knn_distance")) {
                classification = false;
            }
            DataReader data_reader = new DataReader(data, classification);
            List<Instance> instances = data_reader.readData();
            data_reader.close();

            // Train the model.
            Predictor predictor = train(instances, algorithm);
            saveObject(predictor, model_file);

        } else if (mode.equalsIgnoreCase("test")) {
            if (data == null || predictions_file == null || model_file == null) {
                System.out.println("Train requires the following arguments: data, predictions_file, model_file");
                System.exit(0);
            }

            // Load the test data.
            boolean classification = true;
            if (algorithm.equalsIgnoreCase("knn") || algorithm.equalsIgnoreCase("knn_distance")) {
                classification = false;
            }
            DataReader data_reader = new DataReader(data, classification);
            List<Instance> instances = data_reader.readData();
            data_reader.close();

            // Load the model.
            Predictor predictor = (Predictor)loadObject(model_file);
            if(predictor == null) {
                System.out.println("Null predictor.");
            }
            evaluateAndSavePredictions(predictor, instances, predictions_file, classification);
        } else {
            System.out.println("Requires mode argument.");
        }
    }

    private static Predictor train(List<Instance> instances, String algorithm) {
        Predictor predictor;
        boolean regression = false;

        if (algorithm.equalsIgnoreCase("majority")) {
            predictor = new MajorityPredictor();
        } else if (algorithm.equalsIgnoreCase("even_odd")) {
            predictor = new EvenOddPredictor();
        } else if (algorithm.equalsIgnoreCase("decision_tree")) {
            predictor = new DecisionTreePredictor(max_decision_tree_depth);
        } else if (algorithm.equalsIgnoreCase("naive_bayes")) {
            predictor = new NaiveBayesPredictor(lambda);
        } else if (algorithm.equalsIgnoreCase("winnow")) {
            predictor = new WinnowPredictor(thickness,
                    online_learning_rate,
                    online_training_iterations);
        } else if (algorithm.equalsIgnoreCase("perceptron")) {
            predictor = new PerceptronPredictor(thickness,
                    online_learning_rate,
                    online_training_iterations);
        } else if (algorithm.equalsIgnoreCase("logistic_regression_linear_kernel")) {
            predictor = new LinearKernelLogisticRegressionPredictor(
                    gradient_ascent_training_iterations,
                    gradient_ascent_learning_rate);
        } else if (algorithm.equalsIgnoreCase("logistic_regression_polynomial_kernel")) {
            predictor = new PolynomialKernelLogisticRegressionPredictor(
                    gradient_ascent_training_iterations,
                    gradient_ascent_learning_rate,
                    polynomial_kernel_exponent);
        } else if (algorithm.equalsIgnoreCase("logistic_regression_gaussian_kernel")) {
            predictor = new GaussianKernelLogisticRegressionPredictor(
                    gradient_ascent_training_iterations,
                    gradient_ascent_learning_rate,
                    gaussian_kernel_sigma);
        } else if (algorithm.equalsIgnoreCase("knn")) {
            predictor = new SimpleKNNPredictor(k_nn);
            regression = true;
        } else if (algorithm.equalsIgnoreCase("knn_distance")) {
            predictor = new DistanceWeightedKNNPredictor(k_nn);
            regression = true;
        } else if (algorithm.equalsIgnoreCase("instance_bagging")) {
            predictor = new EnsembleInstanceBaggingPredictor(
                    k_ensemble,
                    ensemble_learning_rate,
                    ensemble_training_iterations);
        } else if (algorithm.equalsIgnoreCase("feature_bagging")) {
            predictor = new EnsembleFeatureBaggingPredictor(
                    k_ensemble,
                    ensemble_learning_rate,
                    ensemble_training_iterations);
        } else if (algorithm.equalsIgnoreCase("lambda_means")) {
            predictor = new LambdaMeansPredictor(
                cluster_lambda,
                clustering_training_iterations);
        } else {
            System.out.println("No matching algorithm.");
            return null;
        }
        predictor.train(instances);
        //Evaluate training data
        if (regression) {
            double trainEvaluation = AccuracyEvaluator.evaluateRegression(instances, predictor);
            System.out.println("Training Evaluation: " + trainEvaluation);
            System.out.println();

        } else if (!algorithm.equalsIgnoreCase("lambda_means")) {
            double trainEvaluation = AccuracyEvaluator.evaluateClassification(instances, predictor);
            System.out.println("Training Evaluation: " + trainEvaluation);
            System.out.println();
        }
        return predictor;
    }

    private static void evaluateAndSavePredictions(Predictor predictor,
            List<Instance> instances, String predictions_file, boolean classification) throws IOException {
        PredictionsWriter writer = new PredictionsWriter(predictions_file);
        double testEvaluation;
        if (!classification) {
            testEvaluation = AccuracyEvaluator.evaluateRegression(instances, predictor);
            System.out.println("Testing Evaluation: " + testEvaluation);
        } else if (!algorithm.equalsIgnoreCase("lambda_means")) {
            testEvaluation = AccuracyEvaluator.evaluateClassification(instances, predictor);
            System.out.println("Testing Evaluation: " + testEvaluation);
        }

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
        registerOption("lambda", "double", true, "The level of smoothing for Naive Bayes.");
        registerOption("thickness", "double", true, "The value of the linear separator thickness.");
        registerOption("online_learning_rate", "double", true, "The LTU learning rate.");
        registerOption("online_training_iterations", "int", true, "The number of training iterations for LTU.");
        registerOption("gradient_ascent_learning_rate", "double", true, "The learning rate for logistic regression.");
        registerOption("gradient_ascent_training_iterations", "int", true, "The number of training iterations.");
        registerOption("polynomial_kernel_exponent", "double", true, "The exponent of the polynomial kernel.");
        registerOption("gaussian_kernel_sigma", "double", true, "The sigma of the Gaussian kernel.");
        registerOption("k_nn", "int", true, "The value of K for KNN regression.");
        registerOption("k_ensemble", "int", true, "The number of classifiers in the ensemble.");
        registerOption("ensemble_learning_rate", "double", true, "The ensemble learning rate.");
        registerOption("ensemble_training_iterations", "int", true, "The number of ensemble training iterations.");
        registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
        registerOption("clustering_training_iterations", "int", true, "The number of lambda-means EM iterations.");

    }
}
