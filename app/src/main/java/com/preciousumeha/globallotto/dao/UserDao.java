package com.preciousumeha.globallotto.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.preciousumeha.globallotto.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Query("SELECT * FROM table_user ORDER BY id ASC")
    LiveData<List<User>> getAllUser();
    @Query("UPDATE table_user SET amt=:amount WHERE wallet_id=:wallet")
    void update(String amount,String wallet);
    @Query("DELETE FROM table_user")
    void deleteAllUser();
}
