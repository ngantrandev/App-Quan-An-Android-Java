package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public class User implements Parcelable {
    private String _id, username, password, sex, imgUrl, role, email, sdt;

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("birthday")
    private String birthDay;

    private int status;

    public User(String _id, String username, String password, String sex, String imgUrl, String email, String sdt, String firstName, String lastName, String birthDay) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.imgUrl = imgUrl;
        this.role = "0";
        this.email = email;
        this.sdt = sdt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.status = 1;
    }

    protected User(Parcel in) {
        _id = in.readString();
        username = in.readString();
        password = in.readString();
        sex = in.readString();
        imgUrl = in.readString();
        role = in.readString();
        email = in.readString();
        sdt = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthDay = in.readString();
        status = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(sex);
        dest.writeString(imgUrl);
        dest.writeString(role);
        dest.writeString(email);
        dest.writeString(sdt);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthDay);
        dest.writeInt(status);
    }
}
