package com.W9221214.AppLet.db;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.W9221214.AppLet.model.AppLet;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
@Dao
public interface AppLetDao {


    @Insert
    long[] insertAppLet(AppLet... appLets);


    @Update
    int updateAppLet(AppLet... appLets);


    @Delete
    int deleteAppLet(AppLet... appLets);

    @Query("SELECT * FROM AppLetListings")
    LiveData<List<AppLet>> getAllListings();


    @Query("SELECT * FROM AppLetListings WHERE Area BETWEEN :minArea  AND :maxArea"
            + " AND numberOfRooms BETWEEN  :minNumOfRooms AND :maxNumOfRooms "
            + "AND numbOfBedRooms BETWEEN :minNumOfBedrooms AND :maxNumOfBedrooms "

    )
    LiveData<List<AppLet>> getFilteredListing(
            String minArea, String maxArea
            , String minNumOfRooms, String maxNumOfRooms
            , String minNumOfBedrooms, String maxNumOfBedrooms
    );

    @Query("SELECT * FROM AppLetListings WHERE (address LIKE '%' || :term || '%' " +
            "OR longDescription LIKE '%' || :term || '%' " +
            "OR title LIKE '%' || :term || '%')")
    LiveData<List<AppLet>> getSearchedListing(
            String term
    );

    @Query("SELECT * FROM AppLetListings")
    Cursor getAppLetWithCursor();
}
