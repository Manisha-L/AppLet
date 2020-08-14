package com.W9221214.AppLet.async;

import android.os.AsyncTask;

import com.W9221214.AppLet.db.AppLetDao;
import com.W9221214.AppLet.model.AppLet;

public class AsyncDBUpdate extends AsyncTask<AppLet, Void, Void> {

    private AppLetDao dao;

    public AsyncDBUpdate(AppLetDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(AppLet... appLets) {
        dao.updateAppLet(appLets);
        return null;
    }
}
