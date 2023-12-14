package pl.edu.pjatk;

import pl.edu.pjatk.animals.MNISTAnimalsClassifier;
import pl.edu.pjatk.banknotes.BanknoteAuthenticationClassifier;

public class Main {
    /**
     * Classify data from csv, train and test neural network
     * using Deeplearing4j java library
     *
     * @param args args
     * @author s12901@pjwstk.edu.pl
     */
    public static void main(String[] args) {
        new BanknoteAuthenticationClassifier().classifyData();
        new MNISTAnimalsClassifier().classifyAnimals();
    }
}