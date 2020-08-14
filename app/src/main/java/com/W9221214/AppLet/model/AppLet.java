package com.W9221214.AppLet.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.W9221214.AppLet.utils.ListTypeConverters;

import java.util.List;

@Entity(tableName = "AppLetListings")
@TypeConverters(ListTypeConverters.class)
public class AppLet implements Parcelable {
    @PrimaryKey(autoGenerate = true)

    private int id = 0;
    private String title;
    private String email;
    private int numbOfBedRooms;
    private int Area;
    private int numberOfRooms;
    private String longDescription;
    private List<String> photos = null;
    private String address;
    private String status;
    private long datePutInMarket;
    private int contact;
    private String price;

    public AppLet() {
    }

    protected AppLet(Parcel in) {
        id = in.readInt();
        title = in.readString();
        email= in.readString();
        numbOfBedRooms = in.readInt();
        Area = in.readInt();
        numberOfRooms = in.readInt();
        longDescription = in.readString();
        photos = in.createStringArrayList();
        address = in.readString();
        status = in.readString();
        datePutInMarket = in.readLong();
        contact = in.readInt();
        price = in.readString();
    }

    public static final Creator<AppLet> CREATOR = new Creator<AppLet>() {
        @Override
        public AppLet createFromParcel(Parcel in) {
            return new AppLet(in);
        }

        @Override
        public AppLet[] newArray(int size) {
            return new AppLet[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumbOfBedRooms() {
        return numbOfBedRooms;
    }

    public void setNumbOfBedRooms(int numbOfBedRooms) {
        this.numbOfBedRooms = numbOfBedRooms;
    }

    public int getArea() {
        return Area;
    }

    public void setArea(int Area) {
        this.Area = Area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDatePutInMarket() {
        return datePutInMarket;
    }

    public void setDatePutInMarket(long datePutInMarket) {
        this.datePutInMarket = datePutInMarket;
    }


    public void setContact(int contact) {
        this.contact = contact;
    }

    public int getContact() {
        return contact;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(email);
        dest.writeInt(numbOfBedRooms);
        dest.writeInt(Area);
        dest.writeInt(numberOfRooms);
        dest.writeString(longDescription);
        dest.writeStringList(photos);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeLong(datePutInMarket);
        dest.writeInt(contact);
        dest.writeString(price);
    }
}
