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
    tiebreakerLowestIndex = tiebreakerLowest;
    majorityLabel_ = null;
  }

  public Label getMajorityLabel() {
    if (majorityLabel_ == null) {
      System.out.println("Warning, getting null majority label.");
    }
    return majorityLabel_;
  }

  public void train(List<Instance> instances) {
    //        HashMap<Integer, Integer> labelCounts = new HashMap<Integer, Integer>();
    //        // count the number of times each label occurs
    //        for (Instance current : instances) {
    //            ClassificationLabel currentLabel = (ClassificationLabel) current.getLabel();
    //            int labelValue =  currentLabel.getLabel();
    //            if (labelCounts.containsKey(labelValue)) {
    //                labelCounts.put(labelValue, labelCounts.get(labelValue) + 1);
    //            } else {
    //                labelCounts.put(labelValue, 1);
    //            }
    //        }

    HashMap<ClassificationLabel, Integer> countLabels =
      new HashMap<ClassificationLabel, Integer>();
    for (Instance current : instances) {
      ClassificationLabel currentLabel = (ClassificationLabel) current.getLabel();
      if(countLabels.containsKey(currentLabel)) {
        countLabels.put(currentLabel, countLabels.get(currentLabel) + 1);
      } else {
        countLabels.put(currentLabel, 1);
      }
    }


    Vector<ClassificationLabel> maxLabels = new Vector<ClassificationLabel>();
    // find the maximum time a label occurs
    int maxCount = 0;
    for (int count : countLabels.values()) {
      maxCount = count > maxCount ? count : maxCount;
    }

    //find all labels that occur the max number of times
    for (ClassificationLabel label : countLabels.keySet()) {
      if (countLabels.get(label) == maxCount) {
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
        majorityLabel_ = maxLabels.get(labelIndex);
      } else {
        int minLabelValue = Integer.MAX_VALUE;
        for (ClassificationLabel current : maxLabels) {
          if (current.getLabel() < minLabelValue) {
            minLabelValue = current.getLabel();
          }
        }
        majorityLabel_ = new ClassificationLabel(minLabelValue);
      }
    } else {
      majorityLabel_ = maxLabels.firstElement();
    }

  }

  public Label predict(Instance instance) {
    return majorityLabel_;
  }
}
