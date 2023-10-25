package pl.edu.pjatk.flow;

import net.sourceforge.jFuzzyLogic.Gpr;
import pl.edu.pjatk.fcl.FisService;
import pl.edu.pjatk.model.FuzzyLogicInputVariables;
import pl.edu.pjatk.service.FunctionalBlockServiceImpl;
import pl.edu.pjatk.service.OutputService;

import static pl.edu.pjatk.service.JFuzzyChartViewer.viewAllRulesByFunctionBlock;
import static pl.edu.pjatk.service.JFuzzyChartViewer.viewChartForOutputVariable;

public class FuzzyLogicFlow {
    private static final String FCL_FILE_NAME = "FuzzyLogicAutoTipper/src/main/resources/tip_arg_rules.fcl";
    private static final String FLOW_END_MESSAGE = "flow successfully ended";

    /**
     * start fuzzy logic evaluation flow for given input variables
     *
     * @param fuzzyLogicInputVariables input variables of service , food quality and pricing
     */
    public static void startFuzzyLogicEvaluation(FuzzyLogicInputVariables fuzzyLogicInputVariables) {
        var fisService = new FisService(FCL_FILE_NAME);
        var functionalBlockService = new FunctionalBlockServiceImpl(fuzzyLogicInputVariables);
        fisService.tryLoadFisFromFclFile()
                .flatMap(functionalBlockService::tryGetFunctionalBlock)
                .andThen(viewAllRulesByFunctionBlock)
                .andThen(functionalBlockService::setInputVariablesForFuzzyLogic)
                .andThen(functionalBlockService::evaluateFunctionalBlock)
                .map(OutputService::getOutputVariable)
                .andThen(viewChartForOutputVariable)
                .onSuccess($ -> {
                    Gpr.debug(FLOW_END_MESSAGE);
                    System.out.println("tip: " + $.getValue());
                })
                .onFailure(throwable -> Gpr.warn(throwable.getMessage()));
    }
}
