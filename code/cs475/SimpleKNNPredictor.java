package cs475;

import java.io.Serializable;
import java.util.*;

public class SimpleKNNPredictor extends KNNPredictor {

    public SimpleKNNPredictor(int numNeighbors) {
        super(numNeigbors);
    }

    public Label predict(Instance instance) {
        List<Neighbor> neighbors = findKNearestNeighbors(instance);
        double neighborSum = 0;
        for (Neighbor neighbor : neighbors) {
            neighborSum += neighbor.getNeighbor().getLabel().getLabel();
        }
        double prediction = (1 / (double) k_) * neighborSum;
        return new RegressionLabel(prediction);
    }

}
