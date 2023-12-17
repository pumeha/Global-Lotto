package com.preciousumeha.globallotto.db_others;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.preciousumeha.globallotto.dao.BankDao;
import com.preciousumeha.globallotto.dao.DepositDao;
import com.preciousumeha.globallotto.dao.NotificationDao;
import com.preciousumeha.globallotto.dao.ResultsDao;
import com.preciousumeha.globallotto.dao.ShareFundHistoryDao;
import com.preciousumeha.globallotto.dao.UserDao;
import com.preciousumeha.globallotto.dao.WithdrawalDao;
import com.preciousumeha.globallotto.database.Database;
import com.preciousumeha.globallotto.entity.Bank;
import com.preciousumeha.globallotto.entity.Deposit;
import com.preciousumeha.globallotto.entity.Notification;
import com.preciousumeha.globallotto.entity.Results;
import com.preciousumeha.globallotto.entity.ShareFundHistory;
import com.preciousumeha.globallotto.entity.User;
import com.preciousumeha.globallotto.entity.Withdrawal;

import java.util.List;

public class Repository {
    private ResultsDao resultsDao;
    private ShareFundHistoryDao shareFundHistoryDao;
    private DepositDao depositDao;
    private NotificationDao notificationDao;
    private BankDao bankDao;
    private WithdrawalDao withdrawalDao;
    private UserDao userDao;
    private LiveData<List<Results>> allResults;
    private LiveData<List<ShareFundHistory>> allShareFundHistory;
    private LiveData<List<Deposit>> allDeposit;
    private LiveData<List<Notification>> allNotification;
    private LiveData<List<Bank>> allBank;
    private LiveData<List<Withdrawal>> allWithdrawal;
    private LiveData<List<User>> allUser;
    private Database database;
    public Repository(Application application){
        database = Database.getDatabase(application);

        resultsDao = database.resultsDao();
        shareFundHistoryDao = database.shareFundHistoryDao();
        depositDao = database.depositDao();
        notificationDao = database.notificationDao();
        bankDao = database.bankDao();
        withdrawalDao = database.withdrawalDao();
        userDao = database.userDao();
        allResults = resultsDao.getAllResults();
        allShareFundHistory = shareFundHistoryDao.getAllShareFundHistory();
        allDeposit = depositDao.getAllDeposit();
        allNotification = notificationDao.getAllNotification();
        allBank = bankDao.getAllBank();
        allUser = userDao.getAllUser();
        allWithdrawal = withdrawalDao.getAllWithdrawal();
    }
    public LiveData<List<User>> getAllUser(){
        return  allUser;
    }

    public LiveData<List<Results>> getAllResults() {
        return allResults;
    }
    public LiveData<List<ShareFundHistory>> getAllShareFundHistory(){return allShareFundHistory;}
    public LiveData<List<Deposit>> getAllDeposit(){return allDeposit;}
    public LiveData<List<Notification>> getAllNotification(){return allNotification;}
    public LiveData<List<Bank>> getAllBank() {
        return allBank;
    }
    public LiveData<List<Withdrawal>> getAllWithdrawal() {
        return allWithdrawal;
    }

    public void update(String amount,String wallet){
        Database.databaseWriteExecutor.execute(() -> userDao.update(amount,wallet));
    }
    public void insert(Results results){
        Database.databaseWriteExecutor.execute(() -> resultsDao.insert(results));
    }
    public void insert(ShareFundHistory shareFundHistory){
        Database.databaseWriteExecutor.execute(() -> shareFundHistoryDao.insert(shareFundHistory));
    }
    public void insert(Deposit deposit){
        Database.databaseWriteExecutor.execute(() -> depositDao.insert(deposit));
    }
    public void insert(Notification notification){
        Database.databaseWriteExecutor.execute(()-> notificationDao.insert(notification));
    }
    public void insert(Bank bank){
        Database.databaseWriteExecutor.execute(() -> bankDao.insert(bank));
    }
    public void insert(Withdrawal withdrawal){
        Database.databaseWriteExecutor.execute(() -> withdrawalDao.insert(withdrawal));
    }
    public void insert(User user){
        Database.databaseWriteExecutor.execute(() -> userDao.insert(user));
    }
    public void deleteAllShareFundHistory(){Database.databaseWriteExecutor.execute(() -> shareFundHistoryDao.deleteAllShareFundHistory());}
    public void deleteAllDeposit(){Database.databaseWriteExecutor.execute(() -> depositDao.deleteAllDeposit());}
    public void deleteAllResults(){Database.databaseWriteExecutor.execute(() -> resultsDao.deleteAllResults());}
    public void deleteAllNotification(){Database.databaseWriteExecutor.execute(() -> notificationDao.deleteAllNotification());}
    public void deleteAllBank(){Database.databaseWriteExecutor.execute(() -> bankDao.deleteAllBank());}
    public void deleteAllWithdrawal(){Database.databaseWriteExecutor.execute(() -> withdrawalDao.deleteAllWithdrawal());}
    public void deleteAllUser(){Database.databaseWriteExecutor.execute(() -> userDao.deleteAllUser());}
}
