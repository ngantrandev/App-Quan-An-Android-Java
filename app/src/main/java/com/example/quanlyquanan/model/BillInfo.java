package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BillInfo implements Parcelable {
    private String _id;
    private String bill;
    private String food;
    private int quantity, price;

    public BillInfo(String bill, String food, int quantity, int price) {
        this.bill = bill;
        this.food = food;
        this.quantity = quantity;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    // Parcelable constructor
    protected BillInfo(Parcel in) {
        _id = in.readString();
        bill = in.readString();
        food = in.readString();
        quantity = in.readInt();
        price = in.readInt();
    }

    // Creator for Parcelable
    public static final Creator<BillInfo> CREATOR = new Creator<BillInfo>() {
        @Override
        public BillInfo createFromParcel(Parcel in) {
            return new BillInfo(in);
        }

        @Override
        public BillInfo[] newArray(int size) {
            return new BillInfo[size];
        }
    };

    // Write object values to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(bill);
        dest.writeString(food);
        dest.writeInt(quantity);
        dest.writeInt(price);
    }

    // Describe the kinds of special objects contained in this Parcelable instance
    @Override
    public int describeContents() {
        return 0;
    }
}
