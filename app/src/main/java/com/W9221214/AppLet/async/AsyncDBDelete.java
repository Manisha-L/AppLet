package com.W9221214.AppLet.async;

import android.os.AsyncTask;

import com.W9221214.AppLet.db.AppLetDao;
import com.W9221214.AppLet.model.AppLet;

public class AsyncDBDelete extends AsyncTask<AppLet, Void, Void> {

    private AppLetDao dao;

    public AsyncDBDelete(AppLetDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(AppLet... appLets) {
        dao.deleteAppLet(appLets);
        return null;
    }
}
