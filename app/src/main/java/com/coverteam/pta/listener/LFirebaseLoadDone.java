package com.coverteam.pta.listener;

import com.coverteam.pta.model.DataPanduan;
import java.util.List;

public interface LFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<DataPanduan> dataPanduans);

    void onFirebaseLoadFailed(String message);
}
