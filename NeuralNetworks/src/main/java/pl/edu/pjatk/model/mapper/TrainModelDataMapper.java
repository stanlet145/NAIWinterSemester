package pl.edu.pjatk.model.mapper;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import pl.edu.pjatk.model.TrainAndTestSplits;
import pl.edu.pjatk.model.TrainModelData;

public class TrainModelDataMapper {
    public static TrainModelData map(TrainAndTestSplits trainAndTestSplits,
                                     ParentPathLabelGenerator labelMaker,
                                     DataNormalization scaler,
                                     MultiLayerNetwork network,
                                     StatsStorage statsStorage,
                                     ImageRecordReader trainRR,
                                     ImageTransform transform) {
        return TrainModelData.builder()
                .trainAndTestSplits(trainAndTestSplits)
                .labelMaker(labelMaker)
                .scaler(scaler)
                .network(network)
                .statsStorage(statsStorage)
                .trainRR(trainRR)
                .transform(transform)
                .build();
    }
}
