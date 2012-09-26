package cs475;

import java.util.*;
import java.io.Serializable;


public class DecisionTreeNode implements Serializable {

        private Label label_;
        public boolean isLeaf_;
        private int featureIndex_;
        private double threshholdValue_;
        public DecisionTreeNode leftChild_;
        public DecisionTreeNode rightChild_;
        public String debugInfo_;

        public DecisionTreeNode(Label label) {
            isLeaf_ = true;
            if (label == null) {
              throw new IllegalArgumentException("Cannot create leaf with null labal");
            }
            label_ = label;
        }

        public DecisionTreeNode(int featureIndex, 
                                double threshholdValue,
                                DecisionTreeNode leftChild,
                                DecisionTreeNode rightChild) {
            isLeaf_ = false;
            leftChild_ = leftChild;
            rightChild_ = rightChild;
            featureIndex_ = featureIndex;
            threshholdValue_ = threshholdValue;
        }

        public void addDebugInfo(String info) {
          debugInfo_ = info;
        }

        public DecisionTreeNode getLeftChild() {
            return leftChild_;
        }

        public DecisionTreeNode getRightChild() {
            return rightChild_;
        }

        public String toString() {
          if (isLeaf_) {
            return new String("Label: " + label_);
          } else {
            return new String("featureIndex: " + featureIndex_
                            + " threshold: " + threshholdValue_
                            + " " + debugInfo_);
          }
        }


        /**
          * Takes a FeatureVector and evaluates the feature test
          * on the vector, returning the next node in the path. Returns
          * null if this node is a leaf node.
          * @param vector The FeatureVector to evaluate.
          * @return the result of the test, or null if this is a leaf.
          */
        public DecisionTreeNode getNextNode(FeatureVector vector) {
            if (isLeaf_ ) { return null; }
            if (vector.get(featureIndex_) > threshholdValue_) {
                return rightChild_;
            } else {
                return leftChild_;
            }

        }

        public boolean isLeaf() { return isLeaf_; }

        public Label getLabel() {
            if (isLeaf_) {
                return label_;
            } else {
                return null;
            }
        }
}
