package com.coding.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SummaryInfo {
    private final List<String> summaryInfo;

    SummaryInfo(String... summaries) {
        this.summaryInfo = new ArrayList<>(Arrays.asList(summaries));
    }

    static SummaryInfo fromOrderSummaries(List<String> listOfStrings) {
        final String[] finalOrdersSummaryArray = new String[]{};
        return new SummaryInfo(listOfStrings.toArray(finalOrdersSummaryArray));
    }

    @Override
    public String toString() {
        return String.join("\n", summaryInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryInfo that = (SummaryInfo) o;
        return Objects.equals(summaryInfo, that.summaryInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summaryInfo);
    }
}
