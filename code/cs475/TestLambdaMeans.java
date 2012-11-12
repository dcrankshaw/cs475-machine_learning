package cs475;

import java.util.*;

public class TestLambdaMeans {

    public static void main(String[] args) {
        FeatureVector aa = new FeatureVector();
        FeatureVector bb = new FeatureVector();

        aa.add(0,4);
        aa.add(3,4);
        bb.add(3,7);
        bb.add(8,7);
        bb.add(11,7);
        bb.add(14,7);
        bb.add(15,7);
        aa.add(29,4);
        aa.add(67,4);

        double expectedDist = Math.pow((16 + 16 + 16 + 9 + 49 + 49 + 49 + 49), .5);
        System.out.println("Expected Distance: " + expectedDist);


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
