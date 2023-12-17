package com.preciousumeha.globallotto.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.preciousumeha.globallotto.entity.Deposit;

import java.util.List;

    @Dao
    public interface DepositDao {
        @Insert
        void insert(Deposit deposit);

        @Query("SELECT * FROM table_deposit ORDER BY id ASC")
        LiveData<List<Deposit>> getAllDeposit();

        @Query("DELETE FROM table_deposit")
        void deleteAllDeposit();
    }
