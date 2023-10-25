package pl.edu.pjatk.service;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import pl.edu.pjatk.model.FuzzyLogicInputVariables;

@AllArgsConstructor
public class FunctionalBlockServiceImpl implements FunctionalBlockService {
    private final FuzzyLogicInputVariables fuzzyLogicInputVariables;

    /**
     * Try to get Functional block from fcl file that represents
     * arguments and rules for given FIS
     *
     * @param fis given FIS
     * @return Try of FunctionBlock on success state or Try of Throwable on failure
     */
    @Override
    public Try<FunctionBlock> tryGetFunctionalBlock(FIS fis) {
        return Try.of(() -> fis.getFunctionBlock(null))
                .onFailure(throwable -> Gpr.debug(throwable.getMessage()));
    }

    /**
     * Set variable keys and values for functional block definition
     *
     * @param functionBlock Functional block for FIS
     */
    @Override
    public void setInputVariablesForFuzzyLogic(FunctionBlock functionBlock) {
        functionBlock.setVariable("service", fuzzyLogicInputVariables.service());
        functionBlock.setVariable("food", fuzzyLogicInputVariables.food());
        functionBlock.setVariable("price", fuzzyLogicInputVariables.price());
    }

    /**
     * Evaluate fuzzy rules in this function block
     *
     * @param functionBlock function block with given rules
     */
    @Override
    public void evaluateFunctionalBlock(FunctionBlock functionBlock) {
        functionBlock.evaluate();
    }
}
