package pl.edu.pjatk.model;

import lombok.Value;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.split.FileSplit;

@Value
public class SplitAndFilter {
    FileSplit fileSplit;
    BalancedPathFilter pathFilter;
}
