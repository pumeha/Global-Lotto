package com.preciousumeha.globallotto.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.preciousumeha.globallotto.entity.Bank;

import java.util.List;

@Dao
public interface BankDao {
    @Insert
    void insert(Bank bank);
    @Query("SELECT * FROM table_mybank ORDER BY id ASC")
    LiveData<List<Bank>> getAllBank();
    @Query("DELETE FROM table_mybank")
    void deleteAllBank();
}
