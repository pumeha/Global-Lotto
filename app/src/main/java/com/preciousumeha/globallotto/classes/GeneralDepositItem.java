package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.Deposit;

public class GeneralDepositItem extends ListItem {
    private Deposit deposit;

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
