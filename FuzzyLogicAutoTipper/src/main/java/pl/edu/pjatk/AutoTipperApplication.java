package pl.edu.pjatk;

import pl.edu.pjatk.flow.FuzzyLogicFlow;
import pl.edu.pjatk.model.FuzzyLogicInputVariables;

public class AutoTipperApplication {
    /**
     * Set dependencies and run Auto Tipper Application flow
     *
     * @param args console input array of arguments
     */
    public static void main(String[] args) {
        //input parameters for application, configure those to steer output variable value
        var fuzzyLogicInputVariables = new FuzzyLogicInputVariables(5, 8, 75);
        FuzzyLogicFlow.startFuzzyLogicEvaluation(fuzzyLogicInputVariables);
    }

}
