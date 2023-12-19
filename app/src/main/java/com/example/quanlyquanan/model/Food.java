package com.example.quanlyquanan.model;

import java.io.Serializable;

public class Food implements Serializable {
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

}
