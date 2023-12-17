package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.Results;

public class GeneralItem extends ListItem {
    private Results pojoOfJsonArray;

    public Results getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }



    public void setPojoOfJsonArray(Results pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }


}
