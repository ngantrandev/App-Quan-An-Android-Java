package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Category implements Parcelable {
    private String _id, name;
    private int foodnumber;

    public Category(String _id, String name, int foodnumber) {
        this._id = _id;
        this.name = name;
        this.foodnumber = foodnumber;
    }

    public int getFoodnumber() {
        return foodnumber;
    }

    public void setFoodnumber(int foodnumber) {
        this.foodnumber = foodnumber;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // Phương thức để đọc dữ liệu từ Parcel
    protected Category(Parcel in) {
        _id = in.readString();
        name = in.readString();
        foodnumber = in.readInt();
    }

    // Phương thức để tạo một đối tượng Parcel từ Category
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    // Phương thức để ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeInt(foodnumber);
    }

    // Phương thức để định nghĩa kích thước của đối tượng Parcel
    @Override
    public int describeContents() {
        return 0;
    }
}
