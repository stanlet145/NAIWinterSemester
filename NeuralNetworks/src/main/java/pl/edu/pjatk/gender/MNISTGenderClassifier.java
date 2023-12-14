package pl.edu.pjatk.gender;

import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.PipelineImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.storage.FileStatsStorage;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.primitives.Pair;
import pl.edu.pjatk.model.SplitAndFilter;
import pl.edu.pjatk.model.TrainAndTestSplits;
import pl.edu.pjatk.model.TrainModelData;
import pl.edu.pjatk.model.mapper.TrainModelDataMapper;
import pl.edu.pjatk.service.lenet.NeuralNetworkConfig;
import pl.edu.pjatk.service.lenet.NeuralNetworkConfigImpl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.toIntExact;

public class MNISTGenderClassifier {
    private static final long SEED = 42;
    private static final Random RANDOM_NUMBER_BY_SEED = new Random(SEED);
    private static final int NUMBER_OF_UNIQUE_DATA_BY_LABEL = 3;
    private static final double SPLIT_TRAIN_TEST_RATIO = 0.8;
    private static final int NUMBER_OF_UNIQUE_GENDERS = 2;
    private static final int HEIGHT = 100;
    private static final int WIDTH = 100;
    private static final int CHANNELS = 3;

    private final NeuralNetworkConfig neuralNetworkConfig = new NeuralNetworkConfigImpl();

    /**
     * Main Gender Classifier method
     * Start Gender data classification and neural network training
     */
    public void classifyGender() {
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        SplitAndFilter splitAndFilter = categorizeGenderImages(labelMaker);
        TrainAndTestSplits trainAndTestSplits = prepareTrainAndTestDataSets(splitAndFilter);
        ImageTransform transform = randomizeFlipAndTransformationFormat();

        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        MultiLayerNetwork network = neuralNetworkConfig.lenetModel();
        network.init();

        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new FileStatsStorage(new File(System.getProperty("java.io.tmpdir"), "ui-stats.dl4j"));
        uiServer.attach(statsStorage);

        ImageRecordReader trainRR = new ImageRecordReader(HEIGHT, WIDTH, CHANNELS, labelMaker);

        MNISTGenderModelTrainer mnistGenderModelTrainer = new MNISTGenderModelTrainer();

        TrainModelData trainModelData = TrainModelDataMapper.map(trainAndTestSplits, labelMaker, scaler, network, statsStorage, trainRR, transform);

        mnistGenderModelTrainer.trainModel(trainModelData);
    }

    /**
     * Scan resources for pictures of unique Genders and their labels
     * (unique genders -  male, female)
     * <p>
     * Split and surface normalize data
     * prepare for batching
     *
     * @return SplitAndFilter split and filtered picture data
     */
    private SplitAndFilter categorizeGenderImages(ParentPathLabelGenerator labelMaker) {
        File GenderLabels = new File("NeuralNetworks/src/main/resources/gender");
        FileSplit fileSplit = new FileSplit(GenderLabels, NativeImageLoader.ALLOWED_FORMATS, RANDOM_NUMBER_BY_SEED);
        int numExamples = toIntExact(fileSplit.length());
        BalancedPathFilter pathFilter = new BalancedPathFilter(RANDOM_NUMBER_BY_SEED, labelMaker, numExamples, NUMBER_OF_UNIQUE_GENDERS, NUMBER_OF_UNIQUE_DATA_BY_LABEL);

        return new SplitAndFilter(fileSplit, pathFilter);
    }

    /**
     * Data Setup
     * define train and test split
     **/
    private TrainAndTestSplits prepareTrainAndTestDataSets(SplitAndFilter splitAndFilter) {
        InputSplit[] inputSplit = splitAndFilter.getFileSplit().sample(splitAndFilter.getPathFilter(), SPLIT_TRAIN_TEST_RATIO, 1 - SPLIT_TRAIN_TEST_RATIO);
        InputSplit trainData = inputSplit[0];
        InputSplit testData = inputSplit[1];
        return new TrainAndTestSplits(trainData, testData);
    }

    /**
     * Based on Deeplearning4j documentation recommendations
     * randomized flip and transformation format for future picture processing
     *
     * @return {@link ImageTransform} image transformation
     */
    private ImageTransform randomizeFlipAndTransformationFormat() {
        ImageTransform flipTransform1 = new FlipImageTransform(RANDOM_NUMBER_BY_SEED);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
        ImageTransform warpTransform = new WarpImageTransform(RANDOM_NUMBER_BY_SEED, 42);
        boolean shuffle = false;
        List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(new Pair<>(flipTransform1, 0.9),
                new Pair<>(flipTransform2, 0.8),
                new Pair<>(warpTransform, 0.5));

        return new PipelineImageTransform(pipeline, shuffle);
    }
}
