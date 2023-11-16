FuzzyLogicAutoTipper is a system based on fuzzy logic algorithm by jFuzzyLogic that automatically proposes tip for restaurant service.
AutoTipperApplication.class sets configurable record for set of input variables:

1) service - standing for quality of given service in ranges of poor, good, excellent
2) food - standing for quality of served food in ranges of rancid and delicious
3) price - standing for a fixed pricing rates in given restaurant, from cheap to average to expensive

tip_arg_rules.fcl - file containing facade of Fuzzy Logic algorithm.
It contains a set of input variables with given mathematical ranges for each of necessary parameters.
It also contains output variable declaration (a tip, varying from cheap to average to expensive).

Entire algorithm works by given block of rules in ruleset block 'No1' defined in this file.

run instruction: Run application using JVM and add jFuzzyLogic.jar as project library
