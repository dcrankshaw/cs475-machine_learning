package cs475;

import java.io.Serializable;

// Sparse vector implented as a Java TreeMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

        TreeMap vector;
        public FeatureVector() {
                vector = new TreeMap();
        }

        public void add(int index, double value) {
                int treeIndex = index - 1;
                if (vector.containsKey(treeIndex)) {
                        // TODO(crankshaw) error handling?
                }
                vector.put(treeIndex, value);
        }

        public double get(int index) {
                int treeIndex = index - 1;
                return vector.get(treeIndex);
        }

        public Iterator<Feature> iterator() {
                return new Iterator<Feature> () {
                        public 

                        public FeatureVector next()
                }
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
