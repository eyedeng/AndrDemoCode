package com.example.andrdemocode.architecture.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.andrdemocode.architecture.MyModel;

public class AppViewModel extends ViewModel {

    public MutableLiveData<String> appName = new MutableLiveData<>();

    public void getAppName() {
        String name = getAppModel().getAppName();
        appName.postValue(name);
    }

    private MyModel getAppModel() {
        return new MyModel("AndrDemoCode", 1000, 4);
    }

}
