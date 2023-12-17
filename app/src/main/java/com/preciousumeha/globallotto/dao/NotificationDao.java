package com.preciousumeha.globallotto.dao;

import com.preciousumeha.globallotto.entity.Notification;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NotificationDao {
    @Insert
    void insert(Notification notification);

    @Query("SELECT * FROM table_notify ORDER BY id ASC")
    LiveData<List<Notification>> getAllNotification();

    @Query("DELETE FROM table_notify")
    void deleteAllNotification();
}
