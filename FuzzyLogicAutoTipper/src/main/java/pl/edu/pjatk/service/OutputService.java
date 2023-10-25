package pl.edu.pjatk.service;

import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class OutputService {

    private static final String TIP_VARIABLE_NAME = "tip";

    /**
     * get Variable for given variable name
     *
     * @param functionBlock functional block with rules and input variables
     * @return output variable
     */
    public static Variable getOutputVariable(FunctionBlock functionBlock) {
        return functionBlock.getVariable(TIP_VARIABLE_NAME);
    }
}
