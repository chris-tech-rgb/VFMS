package com.example.vfms.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class GalleryViewModel extends ViewModel {

    @SuppressWarnings("FieldMayBeFinal")
    private MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(Calendar.getInstance().getTime().toString());
    }

    public LiveData<String> getText() {
        return mText;
    }
}