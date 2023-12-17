package com.preciousumeha.globallotto.dao;

import com.preciousumeha.globallotto.entity.ShareFundHistory;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ShareFundHistoryDao {
    @Insert
    void insert(ShareFundHistory shareFundHistory);

    @Query("SELECT * FROM table_sharefundhistory ORDER BY id ASC")
    LiveData<List<ShareFundHistory>> getAllShareFundHistory();

    @Query("DELETE FROM table_sharefundhistory")
    void deleteAllShareFundHistory();
}
