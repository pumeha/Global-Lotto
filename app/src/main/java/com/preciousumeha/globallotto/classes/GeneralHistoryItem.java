package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.ShareFundHistory;

public class GeneralHistoryItem extends ListItem {
    private ShareFundHistory shareFundHistory;

    public ShareFundHistory getShareFundHistory() {
        return shareFundHistory;
    }



    public void setShareFundHistory(ShareFundHistory shareFundHistory) {
        this.shareFundHistory = shareFundHistory;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
