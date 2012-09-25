package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {

        private int label_;
	public ClassificationLabel(int label) {
                label_ = label;
	}

        public int getLabel() { return label_; }


        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ClassificationLabel)) { return false; }
            ClassificationLabel otherLabel = (ClassificationLabel) other;
            return (otherLabel.getLabel() == label_);
        }

        @Override
        public int hashCode() {
            Integer labelInteger = new Integer(label_);
            return labelInteger.hashCode();
        }

	@Override
	public String toString() {
		return Integer.toString(label_);
	}

}
