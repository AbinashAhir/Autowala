package com.example.autowala.ui.insurance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InsuranceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InsuranceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is insurance fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}