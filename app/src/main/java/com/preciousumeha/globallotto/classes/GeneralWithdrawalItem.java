package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.Withdrawal;

public class GeneralWithdrawalItem extends ListItem {
    private Withdrawal withdrawal;

    public Withdrawal getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Withdrawal withdrawal) {
        this.withdrawal = withdrawal;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
