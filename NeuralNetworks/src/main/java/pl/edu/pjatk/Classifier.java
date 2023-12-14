package pl.edu.pjatk;

import io.vavr.control.Try;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

/**
 * Data Classifier and trainer
 *
 * @author s12901@pjwstk.edu.pl
 */
public class Classifier {
    private static final int NUMBER_OF_LABELS = 2;
    private static final int LABEL_COLUMN_INDEX = 4;
    private static final int BATCH_SIZE = 50;
    private static final double USE_PERCENTAGE_OF_DATA_FOR_TRAINING_NUMBER = 0.65;


    private static final String RESOURCE_DATASET_PATH = "NeuralNetworks/src/main/resources/data_banknote_authentication.csv";

    /**
     * Classify data provided via csv dataset
     * Train and test data
     * normalize given data
     * configure neural network
     * fit and evaluate model for training dataset and testing dataset
     * print results in console
     */
    public void classifyData() {
        Try<SplitTestAndTrain> splitTestAndTrains = trainNeuralNetworkWithCsvData();

        DataSet trainingData = getTrainingData(splitTestAndTrains);
        DataSet testData = getTestingData(splitTestAndTrains);

        normalizeData(trainingData, testData);

        MultiLayerConfiguration conf = neuralNetworkConfiguration();
        MultiLayerNetwork model = initializeMultiLayerNetworkModel(conf);

        model.fit(trainingData);

        evaluateModelOnTestSet(trainingData, model);
    }

    /**
     * Evaluate the model on the test set
     */
    private void evaluateModelOnTestSet(DataSet testData, MultiLayerNetwork model) {
        Evaluation eval = new Evaluation(3);
        INDArray output = model.output(testData.getFeatures());
        eval.eval(testData.getLabels(), output);
        System.out.println(eval.stats());

    }

    /**
     * Create Datasets to train and test neural network
     * splits data with sample size (batch size)
     * label column index determines which column in csv is data label
     * number of possible data labels
     * determine percentage of data used for training
     *
     * @return SplitTestAndTrain dataset tested and trained
     */
    private Try<SplitTestAndTrain> trainNeuralNetworkWithCsvData() {
        return Try.of(CSVRecordReader::new)
                .andThen(this::initializeReaderWithDatasetCsv)
                .map(csvRecordReader -> new RecordReaderDataSetIterator(csvRecordReader, BATCH_SIZE, LABEL_COLUMN_INDEX, NUMBER_OF_LABELS))
                .map(RecordReaderDataSetIterator::next)
                .andThen(DataSet::shuffle)
                .map(dataSet -> dataSet.splitTestAndTrain(USE_PERCENTAGE_OF_DATA_FOR_TRAINING_NUMBER));
    }

    /**
     * get training data by {@link SplitTestAndTrain}
     *
     * @param splitTestAndTrains splitTestAndTrains
     * @return training DataSet
     */
    private DataSet getTrainingData(Try<SplitTestAndTrain> splitTestAndTrains) {
        return splitTestAndTrains
                .map(SplitTestAndTrain::getTrain)
                .getOrElseThrow(throwable -> new RuntimeException(throwable));
    }


    /**
     * get testing data by {@link SplitTestAndTrain}
     *
     * @param splitTestAndTrains splitTestAndTrains
     * @return testing DataSet
     */
    private DataSet getTestingData(Try<SplitTestAndTrain> splitTestAndTrains) {
        return splitTestAndTrains
                .map(SplitTestAndTrain::getTest)
                .getOrElseThrow(throwable -> new RuntimeException(throwable));
    }

    /**
     * Use {@link DataNormalization} for normalizing  training and testing datasets
     *
     * @param trainingData training dataset
     * @param testData     testing dataset
     */
    private void normalizeData(DataSet trainingData, DataSet testData) {
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(trainingData);
        normalizer.transform(trainingData);
        normalizer.transform(testData);
    }

    /**
     * Neural Network Config
     * specification of activation, weight, inputs, outputs and layers
     * algorithms
     * Based on provided dataset library config
     *
     * @return MultiLayerConfiguration configuration
     */
    private MultiLayerConfiguration neuralNetworkConfiguration() {
        final int numInputs = 4; //number of data inputs according to dataset
        int outputNum = 2; //number of outputs
        long seed = 6; //random number used for reproducibility between runs
        return new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(3)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(3)
                        .nOut(3)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(3)
                        .nOut(outputNum)
                        .build())
                .backprop(true)
                .pretrain(false)
                .build();
    }

    /**
     * initialize CsvReader
     *
     * @param csvRecordReader csv reader to be initialized
     */
    private void initializeReaderWithDatasetCsv(CSVRecordReader csvRecordReader) {
        Try.run(() -> csvRecordReader.initialize(getNewFileSplitForDataset()));
    }

    /**
     * get fileSplit
     *
     * @return FileSplit
     */
    private FileSplit getNewFileSplitForDataset() {
        return new FileSplit(new File(RESOURCE_DATASET_PATH));
    }

    /**
     * Simple function initializing model and listeners via library api
     *
     * @param conf neural network config
     * @return MultiLayerNetwork
     */
    private MultiLayerNetwork initializeMultiLayerNetworkModel(MultiLayerConfiguration conf) {
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));
        return model;
    }
}
