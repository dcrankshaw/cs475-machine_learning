package cs475;

import java.io.Serializable;

// Sparse vector implented as a Java TreeMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

        private TreeMap<int, Feature> vector;
        private int maxIndex_;
        public FeatureVector() {
                vector = new TreeMap<int, Feature>();
                maxIndex_ = 0;
        }

        public int dimensionality() {
                return maxIndex_;
        }

        public void add(int index, double value) {
                Feature newFeature = new Feature(index, double);
                vector.put(index, newFeature);
                maxIndex_ = index > maxIndex_ ? index : maxIndex_;
        }

        public double get(int index) {
                return vector.get(index);
        }

        public Iterator<Feature> iterator() {
                return new FeatureVectorIterator();
        }


        private class FeatureVectorIterator implements Iterator<Feature> {
                private Iterator<TreeMap> internalIterator;

                public FeatureVectorIterator() {
                        internalIterator = vector.iterator();
                }

                public boolean hasNext() {
                        return internalIterator.hasNext();
                }

                public Feature next() {
                        return internalIterator.next();
                }

                public void remove() {
                        return internalIterator.remove();
                }
        }

}
