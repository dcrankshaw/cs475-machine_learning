package cs475;

import java.util.*;
import java.io.IOException;

public class TestLambdaMeans {

    public static void main(String[] args) throws IOException {

        String path = "/Users/Daniel/cs475-machine_learning/data/nlp.train";
        DataReader data_reader = new DataReader(path, true);
        List<Instance> instances = data_reader.readData();
        data_reader.close();

        FeatureVector aa = new FeatureVector();
        FeatureVector bb = new FeatureVector();

        aa.add(0,4);
        aa.add(3,4);
        bb.add(3,7);
        bb.add(8,7);
        bb.add(11,7);
        aa.add(11,4);
        aa.add(12,4);
        bb.add(14,7);
        bb.add(15,7);
        aa.add(29,4);
        aa.add(67,4);
        bb.add(67,7);
        bb.add(90,7);



        System.out.println("aa to bb dist: " + aa.computeDistance(bb));
        System.out.println("bb to aa dist: " + bb.computeDistance(aa));
        System.out.println("Slow dist: " + slowComputeDistance(aa, bb));

        boolean done = false;
        boolean foundMismatch = false;

        for (Instance a : instances) {
            for (Instance b: instances) {
                double dist = a.getFeatureVector().computeDistance(b.getFeatureVector());
                double slowdist = slowComputeDistance(a.getFeatureVector(), b.getFeatureVector());
                if (dist != slowdist) {
                    done = true;
                    foundMismatch = true;
                    System.out.println("dist: " + dist + " slowdist: " + slowdist);
                    System.out.println("a: " + instances.indexOf(a) + " b: " + instances.indexOf(b));
                    break;
                }
            }
            if (done) {
                break;
            }
        }

        if (!foundMismatch) {
            System.out.println("All distances correct!");
        }

    }

    public static double slowComputeDistance(FeatureVector first, FeatureVector second) {
        int largest_index = first.dimensionality();
        if (second.dimensionality() > largest_index) {
            largest_index = second.dimensionality();
        }
        HashSet<Integer> indices = new HashSet<Integer>(first.getIndices());
        Set<Integer> extraIndices = second.getIndices();
        for (Integer index : extraIndices) {
            indices.add(index);
        }
        double norm = 0;
        for (Integer ind : indices) {
            double diff = first.get(ind) - second.get(ind);
            norm += diff*diff;
        }
        return Math.sqrt(norm);
    }

}
