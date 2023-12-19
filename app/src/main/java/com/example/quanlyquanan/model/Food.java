package com.example.quanlyquanan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Food implements Parcelable {
    String _id, name, description, imgUrl;
    int price, soLuongTon, status;
    float discount;
    Category category;

//    public Food(String name, String description, String imgUrl, int price, int soLuongTon, Category category) {
//        this._id = "";
//        this.name = name;
//        this.description = description;
//        this.imgUrl = imgUrl;
//        this.price = price;
//        this.soLuongTon = soLuongTon;
//        this.status = 1;
//        this.discount = 0;
//        this.category = category;
//    }

    public Food(String _id, String name, String description, String imgUrl, int price, int soLuongTon, int status, float discount, Category category) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.soLuongTon = soLuongTon;
        this.status = status;
        this.discount = discount;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getNewPrice() {

        if (this.discount == 0) return this.price;

        else {
            return (int) (price * (1 - discount / 100));
        }

    }

    // loc danh sach mon an trong ds billinfo
    public static List<Food> filterListFoodInListBillInfo(List<BillInfo> billInfoList, List<Food> foodList) {
        List<Food> filterFoodList = new ArrayList<>();

        for (BillInfo billinfo : billInfoList) {
            for (Food food : foodList) {
                if (billinfo.getFood().equals(food.get_id())) {
                    filterFoodList.add(food);
                }
            }
        }

        return filterFoodList;
    }

    public static Food findFoodById(String foodId, List<Food> foodList){
        for (Food food: foodList) {
            if(food.get_id().equals(foodId))
                return food;
        }

        return null;
    }


    // Phương thức để đọc dữ liệu từ Parcel
    protected Food(Parcel in) {
        _id = in.readString();
        name = in.readString();
        description = in.readString();
        imgUrl = in.readString();
        price = in.readInt();
        soLuongTon = in.readInt();
        status = in.readInt();
        discount = in.readFloat();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    // Phương thức để tạo một đối tượng Parcel từ Food
    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    // Phương thức để ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imgUrl);
        dest.writeInt(price);
        dest.writeInt(soLuongTon);
        dest.writeInt(status);
        dest.writeFloat(discount);
        dest.writeParcelable(category, flags);
    }

    // Phương thức để định nghĩa kích thước của đối tượng Parcel
    @Override
    public int describeContents() {
        return 0;
    }

}
