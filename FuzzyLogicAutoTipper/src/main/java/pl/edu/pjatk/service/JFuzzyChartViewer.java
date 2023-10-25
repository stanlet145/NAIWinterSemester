package pl.edu.pjatk.service;

import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.function.Consumer;

/**
 * JFuzzyChart chart viewer
 */
public class JFuzzyChartViewer {
    /**
     * Consumer for viewing entire chart of rules
     * in a form of graphic representations via JFuzzyChart
     */
    public static final Consumer<FunctionBlock> viewAllRulesByFunctionBlock = ruleBlock ->
            JFuzzyChart
                    .get()
                    .chart(ruleBlock);

    /**
     * Consumer for viewing chart of output variable, in our case it should be 'tip'
     * in a form of graphic representation via JFuzzyChart
     */
    public static final Consumer<Variable> viewChartForOutputVariable = variable ->
            JFuzzyChart
                    .get()
                    .chart(variable, variable.getDefuzzifier(), true);
}
