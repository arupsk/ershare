package com.Andriod.ER.com;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class UserModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public UserModel() {
        // trigger user load.
    }

    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }

    private class User {
    }
}
