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

        public DecisionTreeNode(Label label) {
            isLeaf_ = true;
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

//        public void setLeftChild(DecisionTreeNode child) {
//            if (!isLeaf) {
//                leftChild_ = child;
//            }
//        }
//
//        public void setRightChild(DecisionTreeNode child) {
//            if (!isLeaf) {
//                rightChild_ = child;
//            }
//        }

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


        
        //Figure out how to encapsulate a feature test
        // A feature test is constructed with a feature index, a threshhold value,
        // and two DecisionTreeNodes (the left and right children). It evaluates the
        // test based on being given a FeatureVector and returns the corresponding node.


}
