package com.preciousumeha.globallotto.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.preciousumeha.globallotto.dao.BankDao;
import com.preciousumeha.globallotto.dao.DepositDao;
import com.preciousumeha.globallotto.dao.NotificationDao;
import com.preciousumeha.globallotto.dao.ResultsDao;
import com.preciousumeha.globallotto.dao.ShareFundHistoryDao;
import com.preciousumeha.globallotto.dao.UserDao;
import com.preciousumeha.globallotto.dao.WithdrawalDao;
import com.preciousumeha.globallotto.entity.Bank;
import com.preciousumeha.globallotto.entity.Deposit;
import com.preciousumeha.globallotto.entity.Notification;
import com.preciousumeha.globallotto.entity.Results;
import com.preciousumeha.globallotto.entity.ShareFundHistory;
import com.preciousumeha.globallotto.entity.User;
import com.preciousumeha.globallotto.entity.Withdrawal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Results.class, ShareFundHistory.class, Deposit.class,
        Notification.class, Bank.class, Withdrawal.class, User.class}, version = 7,exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract ResultsDao resultsDao();
    public abstract ShareFundHistoryDao shareFundHistoryDao();
    public abstract DepositDao depositDao();
    public abstract NotificationDao notificationDao();
    public abstract BankDao bankDao();
    public abstract WithdrawalDao withdrawalDao();
    public abstract UserDao userDao();
    private static volatile Database Instance;
    private static final int NUMBER_OF_THREADS = 7;


    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static Database getDatabase(final Context context){
        if (Instance == null){
            synchronized (Database.class){
                if (Instance == null){
                    Instance = Room.databaseBuilder(context.getApplicationContext(),Database.class,"global-lotto_db").build();
                }
            }
        }
        return Instance;
    }
}
