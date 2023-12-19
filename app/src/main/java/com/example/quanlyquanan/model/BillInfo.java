package com.example.quanlyquanan.model;

public class BillInfo {
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
}
