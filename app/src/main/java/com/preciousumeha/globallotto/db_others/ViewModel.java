package com.preciousumeha.globallotto.db_others;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.preciousumeha.globallotto.entity.Bank;
import com.preciousumeha.globallotto.entity.Deposit;
import com.preciousumeha.globallotto.entity.Notification;
import com.preciousumeha.globallotto.entity.Results;
import com.preciousumeha.globallotto.entity.ShareFundHistory;
import com.preciousumeha.globallotto.entity.User;
import com.preciousumeha.globallotto.entity.Withdrawal;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private Repository repository;
    LiveData<List<Results>> allResults;
    LiveData<List<ShareFundHistory>> allShareFundHistory;
    LiveData<List<Deposit>> allDeposit;
    LiveData<List<Notification>> allNotification;
    LiveData<List<Bank>> allBank;
    LiveData<List<Withdrawal>> allWithdrawal;
    LiveData<List<User>> allUser;
    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allResults = repository.getAllResults();
        allShareFundHistory = repository.getAllShareFundHistory();
        allDeposit = repository.getAllDeposit();
        allNotification = repository.getAllNotification();
        allBank = repository.getAllBank();
        allWithdrawal = repository.getAllWithdrawal();
        allUser = repository.getAllUser();
    }

    public LiveData<List<Results>> getAllResults() {
        return allResults;
    }
    public LiveData<List<ShareFundHistory>> getAllShareFundHistory(){return allShareFundHistory;}
    public LiveData<List<Deposit>> getAllDeposit() {
        return allDeposit;
    }
    public LiveData<List<Notification>> getAllNotification(){return allNotification;}
    public LiveData<List<Bank>> getAllBank() {
        return allBank;
    }
    public LiveData<List<Withdrawal>> getAllWithdrawal(){return allWithdrawal;}
    public LiveData<List<User>> getAllUser() {
        return allUser;
    }

    public void insert(Results results){
        repository.insert(results);
    }
    public void insert(ShareFundHistory shareFundHistory){repository.insert(shareFundHistory);}
    public void insert(Deposit deposit){repository.insert(deposit);}
    public void insert(Notification notification){repository.insert(notification);}
    public void insert(Bank bank){repository.insert(bank);}
    public void insert(Withdrawal withdrawal){repository.insert(withdrawal);}
    public void insert(User user){
        repository.insert(user);
    }


    public void deleteAllShareFundHistory(){repository.deleteAllShareFundHistory();}
    public void deleteAllDeposit(){repository.deleteAllDeposit();}
    public void deleteAllResults(){repository.deleteAllResults();}
    public void deleteAllNotification(){repository.deleteAllNotification();}
    public void deleteAllBank(){repository.deleteAllBank();}
    public void deleteAllWithdrawal(){repository.deleteAllWithdrawal();}
    public void deleteAllUser(){repository.deleteAllUser();}
    public void updateUserAcc(String amount,String wallet){
        repository.update(amount,wallet);
    }

}
