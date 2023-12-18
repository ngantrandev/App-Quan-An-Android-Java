package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table implements Parcelable {
    private String _id, note;

    @SerializedName("tablename")
    private String tableName;
    int status;

    private String bill;

    public Table(String _id, String note, String tableName, int status) {
        this._id = _id;
        this.note = note;
        this.tableName = tableName;
        this.status = status;
    }

    public String getBill() {
        return bill;
    }

    public void resetBill(){
        setBill("");
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Table{" +
                "note='" + note + '\'' +
                ", tableName='" + tableName + '\'' +
                ", status=" + status +
                ", bill='" + bill + '\'' +
                '}';
    }

    // Parcelable implementation

    protected Table(Parcel in) {
        _id = in.readString();
        note = in.readString();
        tableName = in.readString();
        status = in.readInt();
        bill = in.readString();
    }

    public static final Creator<Table> CREATOR = new Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(note);
        dest.writeString(tableName);
        dest.writeInt(status);
        dest.writeString(bill);
    }
}
