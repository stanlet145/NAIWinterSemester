package pl.edu.pjatk;

import pl.edu.pjatk.flow.FuzzyLogicFlow;
import pl.edu.pjatk.model.FuzzyLogicInputVariables;

/**
 * FuzzyLogicAutoTipper is a system based on fuzzy logic algorithm that automatically proposes tip for restaurant service.
 * AutoTipperApplication.class sets configurable record for set of input variables:
 * <p>
 * 1) service - standing for quality of given service in ranges of poor, good, excellent
 * 2) food - standing for quality of served food in ranges of rancid and delicious
 * 3) price - standing for a fixed pricing rates in given restaurant, from cheap to average to expensive
 * <p>
 * tip_arg_rules.fcl - file containing facade of Fuzzy Logic algorithm.
 * It contains a set of input variables with given mathematical ranges for each of necessary parameters.
 * It also contains output variable declaration (a tip, varying from cheap to average to expensive).
 * <p>
 * Entire algorithm works by given block of rules in ruleset block 'No1' defined in this file.
 * <p>
 * run instruction: Run application using JVM and add jFuzzyLogic.jar as project library
 */
public class AutoTipperApplication {
    /**
     * Set dependencies and run Auto Tipper Application flow
     *
     * @param args console input array of arguments
     */
    public static void main(String[] args) {
        //input parameters for application, configure those to steer output variable value
        var fuzzyLogicInputVariables = new FuzzyLogicInputVariables(0, 9, 125);
        FuzzyLogicFlow.startFuzzyLogicEvaluation(fuzzyLogicInputVariables);
    }

}
