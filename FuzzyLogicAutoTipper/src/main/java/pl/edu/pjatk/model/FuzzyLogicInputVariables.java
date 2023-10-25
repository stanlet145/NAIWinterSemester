package pl.edu.pjatk.model;

/**
 * Record for Fuzzy Logic Input Variables
 * @param service Quality of service
 * @param food Quality of food
 * @param price Restaurant pricing rate
 */
public record FuzzyLogicInputVariables(double service, double food, double price) {
}
