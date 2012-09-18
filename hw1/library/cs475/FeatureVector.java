package cs475;

import java.io.Serializable;
import java.util.*;

// Sparse vector implented as a Java TreeMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

        private TreeMap<Integer, Feature> vector;
        private int maxIndex_;
        public FeatureVector() {
                vector = new TreeMap<Integer, Feature>();
                maxIndex_ = 0;
        }

        public int dimensionality() {
                return maxIndex_;
        }

        public void add(int index, double value) {
                Feature newFeature = new Feature(index, value);
                vector.put(index, newFeature);
                maxIndex_ = index > maxIndex_ ? index : maxIndex_;
        }

        public double get(int index) {
                return vector.get(index).value_;
        }

        public Iterator<Feature> iterator() {
                return new FeatureVectorIterator();
        }


        private class FeatureVectorIterator implements Iterator<Feature> {
                private Iterator<Feature> internalIterator;

                public FeatureVectorIterator() {
                        internalIterator = vector.values().iterator();
                }

                public boolean hasNext() {
                        return internalIterator.hasNext();
                }

                public Feature next() {
                        return internalIterator.next();
                }

                public void remove() {
                        internalIterator.remove();
                }
        }

}
