package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.Results;

public class GeneralResultItem extends ListItem {
    private Results results;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
