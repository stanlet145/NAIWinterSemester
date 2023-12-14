package pl.edu.pjatk.service.lenet;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

/**
 * Interface for:
 * Revisde Lenet Model approach developed by ramgo2 achieves slightly above random
 * Reference: https://gist.github.com/ramgo2/833f12e92359a2da9e5c2fb6333351c5
 **/
public interface NeuralNetworkConfig {
    MultiLayerNetwork lenetModel();
}
