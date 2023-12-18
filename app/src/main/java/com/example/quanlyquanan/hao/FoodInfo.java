package com.example.quanlyquanan.hao;

import java.io.Serializable;

public class FoodInfo implements Serializable {
    private int foodId;//id của món ăn
    private int foodPrice; // Giá tiền
    private String foodName; //Tên món
    private String foodThumb; // Hình ảnh của món ăn
    private String foodtype;
    private int foodOldPrice;
    private  String foodNote; //Ghi chú trên món ăn này
    private  int foodSelAmount;
    private  int foodAmount;//số lượng tổng



    public FoodInfo(int foodId,String foodName,int foodPrice, String foodThumb, String foodtype, String foodNote, int foodAmount) {
        this.foodId = foodId;
        this.foodPrice = foodPrice;
        this.foodName = foodName;
        this.foodThumb = foodThumb;
        this.foodNote = foodNote;
        this.foodAmount = foodAmount;
        this.foodSelAmount = 0;
        this.foodtype = foodtype;
        this.foodOldPrice = this.foodPrice;
        clearFoodSelAmount();
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodThumb() {
        return  foodThumb;
    }
    public void setFoodThumb(String foodThumb) {
        this.foodThumb = foodThumb;
    }

    public String getFoodtype() {return foodtype;}

    public void setFoodtype(String foodtype) {this.foodtype = foodtype;}

    public int getFoodRemaining() {
        return (this.foodAmount - this.foodSelAmount);}

    public String getFoodNote() {
        return foodNote;
    }

    public void setFoodNote(String foodNote) {
        this.foodNote = foodNote;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public int getFoodOldPrice() {return foodOldPrice;}

    public void setFoodOldPrice(int foodOldPrice) {this.foodOldPrice = foodOldPrice;}

    public int getFoodSelAmount() {
        return foodSelAmount;
    }

    public void setFoodSelAmount(int foodSelAmount) {
        this.foodSelAmount = foodSelAmount;
    }
    public  void addFoodSelAmount(int Amount){
        this.foodSelAmount +=Amount;
    }
    public  void minusFoodSelAmount(int Amount){
        this.foodSelAmount -=Amount;
    }
    public void clearFoodSelAmount(){
        this.foodSelAmount = 0;
    }
    public void resetMenu(){
        this.foodNote = "";
        this.foodSelAmount = 0;
    }
    public void resetMenuAmount(){
        this.foodAmount-=this.foodSelAmount;
    }
}
