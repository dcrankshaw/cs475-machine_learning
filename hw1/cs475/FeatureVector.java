package cs475;

import java.io.Serializable;

// Sparse vector implented as a Java TreeMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

        private TreeSet<Feature> vector;
        public FeatureVector() {
                vector = new TreeSet<Feature>();
        }

        public void add(int index, double value) {
                int treeIndex = index - 1;
                Feature newFeature = new Feature(index, double);
                vector.add(newFeature);
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
