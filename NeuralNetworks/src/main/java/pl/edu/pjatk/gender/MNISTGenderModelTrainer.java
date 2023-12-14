package pl.edu.pjatk.gender;

import io.vavr.control.Try;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.InputSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.stats.StatsListener;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import pl.edu.pjatk.model.TrainAndTestSplits;
import pl.edu.pjatk.model.TrainModelData;

public class MNISTGenderModelTrainer {
    private static final int NUMBER_OF_UNIQUE_GENDERS = 2;
    private static final int HEIGHT = 100;
    private static final int WIDTH = 100;
    private static final int CHANNELS = 3;
    private static final int BATCH_SIZE = 20;
    private static final int EPOCHS = 100;

    /**
     * Train neural network to distinguish genders
     *
     * set required properties for Deeplearning4j model training
     * Determine Image Reader for images
     * Initialize Record Reader with sample pool of genders for training
     * fit model
     * set library prerequisites
     * train neural network with and without transformation
     *
     * @param trainModelData TrainModelData trainModelData
     */
    public void trainModel(TrainModelData trainModelData) {
        /**
         * instantiate local objects from {@link TrainModelData}
         */
        TrainAndTestSplits trainAndTestSplits = trainModelData.getTrainAndTestSplits();
        InputSplit trainData = trainAndTestSplits.getTrainData();
        InputSplit testData = trainAndTestSplits.getTestData();
        ImageRecordReader trainRR = trainModelData.getTrainRR();
        DataNormalization scaler = trainModelData.getScaler();
        MultiLayerNetwork network = trainModelData.getNetwork();
        StatsStorage statsStorage = trainModelData.getStatsStorage();
        ImageTransform transform = trainModelData.getTransform();
        ParentPathLabelGenerator labelMaker = trainModelData.getLabelMaker();

        /**
         * set required properties for Deeplearning4j model training
         * Determine Image Reader for images
         * Initialize Record Reader with sample pool of genders for training
         * fit model
         * set library prerequisites
         * train neural network with and without transformation
         */
        Try.success(new ImageRecordReader(HEIGHT, WIDTH, CHANNELS, labelMaker))
                .andThen(testRR -> tryInitializeTestRecordReader(testData, testRR))
                .map(imageRecordReader -> new RecordReaderDataSetIterator(imageRecordReader, BATCH_SIZE, 1, NUMBER_OF_UNIQUE_GENDERS))
                .andThen(scaler::fit)
                .andThen(recordReaderDataSetIterator -> recordReaderDataSetIterator.setPreProcessor(scaler))
                .andThen(recordReaderDataSetIterator -> setListeners(recordReaderDataSetIterator, network, statsStorage))
                .andThen(() -> trainWithoutTransformation(scaler, network, trainRR, trainData))
                .andThen(() -> trainWithTransformation(scaler, network, trainRR, trainData, transform));
    }

    private void setListeners(RecordReaderDataSetIterator recordReaderDataSetIterator,
                              MultiLayerNetwork network,
                              StatsStorage statsStorage) {
        network.setListeners(
                new StatsListener(statsStorage),
                new ScoreIterationListener(1),
                new EvaluativeListener(recordReaderDataSetIterator, 1, InvocationType.EPOCH_END));
    }

    /**
     * Try to initialize test record reader
     *
     * @param testData InputSplit
     * @param testRR   ImageRecordReader
     */
    private void tryInitializeTestRecordReader(InputSplit testData, ImageRecordReader testRR) {
        Try.run(() -> testRR.initialize(testData));
    }

    /**
     * Train without transformations
     */
    private void trainWithoutTransformation(DataNormalization scaler,
                                            MultiLayerNetwork network,
                                            ImageRecordReader trainRR,
                                            InputSplit trainData) {
        Try.run(() -> trainRR.initialize(trainData, null))
                .map($ -> new RecordReaderDataSetIterator(trainRR, BATCH_SIZE, 1, NUMBER_OF_UNIQUE_GENDERS))
                .andThen(scaler::fit)
                .andThen(recordReaderDataSetIterator -> recordReaderDataSetIterator.setPreProcessor(scaler))
                .andThen(recordReaderDataSetIterator -> network.fit(recordReaderDataSetIterator, EPOCHS));
    }

    /**
     * Train with transformations
     */
    private void trainWithTransformation(DataNormalization scaler,
                                         MultiLayerNetwork network,
                                         ImageRecordReader trainRR,
                                         InputSplit trainData,
                                         ImageTransform transform) {
        Try.run(() -> trainRR.initialize(trainData, transform))
                .map($ -> new RecordReaderDataSetIterator(trainRR, BATCH_SIZE, 1, NUMBER_OF_UNIQUE_GENDERS))
                .andThen(scaler::fit)
                .andThen(recordReaderDataSetIterator -> recordReaderDataSetIterator.setPreProcessor(scaler))
                .andThen(recordReaderDataSetIterator -> network.fit(recordReaderDataSetIterator, EPOCHS));
    }
}
