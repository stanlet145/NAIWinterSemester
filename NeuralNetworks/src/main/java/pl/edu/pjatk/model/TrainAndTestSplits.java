package pl.edu.pjatk.model;

import lombok.Value;
import org.datavec.api.split.InputSplit;

@Value
public class TrainAndTestSplits {
    InputSplit trainData;
    InputSplit testData;
}
