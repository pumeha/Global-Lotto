package com.preciousumeha.globallotto.dao;

import com.preciousumeha.globallotto.entity.Results;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ResultsDao {
    @Insert
    void insert(Results results);

    @Query("SELECT * FROM table_results ORDER BY id ASC")
    LiveData<List<Results>> getAllResults();

    @Query("DELETE FROM table_results")
    void deleteAllResults();

}
