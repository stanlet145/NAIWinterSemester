package pl.edu.pjatk.service;

import io.vavr.control.Try;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public interface FunctionalBlockService {
    Try<FunctionBlock> tryGetFunctionalBlock(FIS fis);

    void setInputVariablesForFuzzyLogic(FunctionBlock functionBlock);

    void evaluateResultByRules(FunctionBlock functionBlock);
}
