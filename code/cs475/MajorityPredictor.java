package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.*;

public class MajorityPredictor extends Predictor implements Serializable {

    Label majorityLabel_ = null;
    boolean tiebreakerLowestIndex;

    private static final long serialVersionUID = 1L;

    public MajorityPredictor() {
        majorityLabel_ = null;
        tiebreakerLowestIndex = false;
    }

    public MajorityPredictor(boolean tiebreakerLowest) {
        tiebreakerLowest = true;
        majorityLabel_ = null;
    }

    public Label getMajorityLabel() {
        return majorityLabel_;
    }

    public void train(List<Instance> instances) {
        HashMap<Integer, Integer> labelCounts = new HashMap<Integer, Integer>();
        // count the number of times each label occurs
        for (Instance current : instances) {
            ClassificationLabel currentLabel = (ClassificationLabel) current.getLabel();
            int labelValue =  currentLabel.getLabel();
            if (labelCounts.containsKey(labelValue)) {
                labelCounts.put(labelValue, labelCounts.get(labelValue) + 1);
            } else {
                labelCounts.put(labelValue, 1);
            }
        }
        Vector<Integer> maxLabels = new Vector<Integer>();
        // find the maximum time a label occurs
        int maxCount = 0;
        for (int count : labelCounts.values()) {
            maxCount = count > maxCount ? count : maxCount;
        }

        //find all labels that occur the max number of times
        for (Integer label : labelCounts.keySet()) {
            if (labelCounts.get(label) == maxCount) {
                maxLabels.add(label);
            }
        }
        int labelIndex = 0;

        // pick a random label from the potential labels
        if (maxLabels.size() > 1) {
            if (!tiebreakerLowestIndex) {
                System.out.println("Breaking tie.");
                Random rand = new Random();
                labelIndex = rand.nextInt(maxLabels.size());
                majorityLabel_ = new ClassificationLabel(maxLabels.get(labelIndex));
            } else {
            int minLabelValue = Integer.MAX_VALUE;
            for (Integer current : maxLabels) {
                if (current < minLabelValue) {
                    minLabelValue = current;
                }
            }
            majorityLabel_ = new ClassificationLabel(minLabelValue);
        }
    }

    }

    public Label predict(Instance instance) {
        return majorityLabel_;
    }
}
