package com.W9221214.AppLet.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.W9221214.AppLet.async.AsyncDBDelete;
import com.W9221214.AppLet.async.AsyncDBInsert;
import com.W9221214.AppLet.async.AsyncDBUpdate;
import com.W9221214.AppLet.db.AppLetDao;
import com.W9221214.AppLet.db.RoomDB;
import com.W9221214.AppLet.model.AppLet;
import com.W9221214.AppLet.model.FilterParams;

import java.util.List;


public class Repository {
    private AppLetDao dao;

    public Repository(Context context) {
        dao = RoomDB.getInstance(context).getDao();
    }

    public void insertListing(AppLet appLet) {
        new AsyncDBInsert(dao).execute(appLet);
    }

    public void updateListing(AppLet appLet) {
        new AsyncDBUpdate(dao).execute(appLet);
    }

    public void deleteListing(AppLet appLet) {
        new AsyncDBDelete(dao).execute(appLet);
    }

    public LiveData<List<AppLet>> getAllListings() {
        return dao.getAllListings();
    }

    public LiveData<List<AppLet>> filterList(FilterParams filterParamse) {

        return dao.getFilteredListing(
                filterParamse.getMinArea(),
                filterParamse.getMaxArea()
                , filterParamse.getMinNumOfRooms()
                , filterParamse.getMaxNumOfRooms()
                , filterParamse.getMinNumOfBedRooms()
                , filterParamse.getMaxNumOfBedRooms()
        );
    }

    public LiveData<List<AppLet>> geSearchedListings(String term) {
        return dao.getSearchedListing(term);
    }
}
