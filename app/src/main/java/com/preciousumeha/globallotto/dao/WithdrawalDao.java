package com.preciousumeha.globallotto.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.preciousumeha.globallotto.entity.Withdrawal;

import java.util.List;

@Dao
public interface WithdrawalDao {
    @Insert
    void insert(Withdrawal withdrawal);
    @Query("SELECT * FROM table_withdrawal ORDER BY id ASC")
    LiveData<List<Withdrawal>> getAllWithdrawal();
    @Query("DELETE FROM table_withdrawal")
    void deleteAllWithdrawal();
}
