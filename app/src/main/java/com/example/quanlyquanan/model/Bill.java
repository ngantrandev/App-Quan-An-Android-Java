package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Bill implements Parcelable {
    private String _id, timeCheckIn, timeCheckOut, note;
    private int tips, status;
    private Table table;
    private Seller seller;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTimeCheckIn() {
        return timeCheckIn;
    }

    public void setTimeCheckIn(String timeCheckIn) {
        this.timeCheckIn = timeCheckIn;
    }

    public String getTimeCheckOut() {
        return timeCheckOut;
    }

    public void setTimeCheckOut(String timeCheckOut) {
        this.timeCheckOut = timeCheckOut;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTips() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(timeCheckIn);
        dest.writeString(timeCheckOut);
        dest.writeString(note);
        dest.writeInt(tips);
        dest.writeInt(status);
        dest.writeParcelable(table, flags);
        dest.writeParcelable(seller, flags);
    }

    // Cần thêm constructor sau đây để đọc dữ liệu từ Parcel
    protected Bill(Parcel in) {
        _id = in.readString();
        timeCheckIn = in.readString();
        timeCheckOut = in.readString();
        note = in.readString();
        tips = in.readInt();
        status = in.readInt();
        table = in.readParcelable(Table.class.getClassLoader());
        seller = in.readParcelable(Seller.class.getClassLoader());
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

}
