package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {

        private int label_;
	public ClassificationLabel(int label) {
                label_ = label;
	}

        public int getLabel() { return label_; }

	@Override
	public String toString() {
		return Int.toString(label_);
	}

}
