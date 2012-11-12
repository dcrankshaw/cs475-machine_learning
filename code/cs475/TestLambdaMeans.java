package cs475;

import java.util.*;

public class TestLambdaMeans {

    public static void main(String[] args) {
        FeatureVector aa = new FeatureVector();
        FeatureVector bb = new FeatureVector();

        for (int i = 0; i < 4; ++i) {
            aa.add(i, 4);
        }
        for (int i = 3; i < 8; ++i) {
            aa.add(i, 7);
        }
        System.out.println("aa to bb dist: " + aa.computeDistance(bb));
        System.out.println("bb to aa dist: " + bb.computeDistance(aa));

        List<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance(aa, new ClassificationLabel(0)));
        instances.add(new Instance(bb, new ClassificationLabel(0)));

        FeatureVector meanVector = LambdaMeansPredictor.computeMeanVector(instances);
        for (Feature f : meanVector) {
            System.out.print(f + " ");
        }
        System.out.println();

    }

}
