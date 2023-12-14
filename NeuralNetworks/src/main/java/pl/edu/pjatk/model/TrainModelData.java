package pl.edu.pjatk.model;

import lombok.Builder;
import lombok.Value;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;

@Value
@Builder
public class TrainModelData {
    TrainAndTestSplits trainAndTestSplits;
    ParentPathLabelGenerator labelMaker;
    DataNormalization scaler;
    MultiLayerNetwork network;
    StatsStorage statsStorage;
    ImageRecordReader trainRR;
    ImageTransform transform;
}
