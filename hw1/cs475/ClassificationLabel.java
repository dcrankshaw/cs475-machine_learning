package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {

        int label_;
	public ClassificationLabel(int label) {
                label_ = label;
	}

	@Override
	public String toString() {
		return Int.toString(label_);
	}

}
