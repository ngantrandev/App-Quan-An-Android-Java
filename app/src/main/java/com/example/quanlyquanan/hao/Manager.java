package com.example.quanlyquanan.hao;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private  static List<TableInfo> dsTable = new ArrayList<TableInfo>();
    private static List<FoodInfo> dsFood = new ArrayList<FoodInfo>();


    static void CreateDataTest(){
        CreateDataFood();
        CreateDataTable();
    }
    static void CreateDataTable(){
        for(int i=0;i<5;i++){
            boolean stt = false;
            TableInfo T = new TableInfo(i,stt,"");
            for (FoodInfo f: dsFood){
                T.addFoodSelection(f);
            }
            dsTable.add(T);
        }
    }
    static void CreateDataFood(){
        dsFood.add(new FoodInfo(0 , "Cơm tấm", 27000,"https://upload.wikimedia.org/wikipedia/commons/b/b0/C%C6%A1m_T%E1%BA%A5m%2C_Da_Nang%2C_Vietnam.jpg","Món Khô | Cơm","",10));
        dsFood.add(new FoodInfo(1 , "Cơm gà", 30000,"https://images.foody.vn/res/g108/1077017/prof/s1242x600/foody-upload-api-foody-mobile-comga1-210506181924.jpg","Món khô | Cơm","",20));
        dsFood.add(new FoodInfo(4 , "Bún bò", 35000,"https://cdn.tgdd.vn/Files/2018/04/01/1078873/nau-bun-bo-hue-cuc-de-tai-nha-tu-vien-gia-vi-co-san-202109161718049940.jpg","Món nước","",20));
        dsFood.add(new FoodInfo(5 , "Phở", 30000,"https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-nau-pho-bo-nam-dinh-chuan-vi-thom-ngon-nhu-hang-quan-202201250230038502.jpg","Món nước","",20));
        dsFood.add(new FoodInfo(6 , "Hủ tiếu Nam Vang", 30000,"https://cdn.tgdd.vn/2021/10/CookRecipe/Avatar/2(10).jpg","Món nước | Món khô","",100));
        dsFood.add(new FoodInfo(2 , "Pepsi", 10000,"https://product.hstatic.net/1000141988/product/nuoc_ngot_pepsi_lon_320_ml_415ef91bdb15487ab3079155c3635f66.jpg","Đồ uống","",20));
        dsFood.add(new FoodInfo(3 , "CoCaCola", 10000,"https://tea-3.lozi.vn/v1/ship/resized/losupply-quang-tri-thanh-pho-dong-ha-quang-tri-1648801646214781853-nuoc-ngot-coca-cola-lon-320ml-0-1658895994?w=480&type=o","Đồ uống","",20));
        dsFood.add(new FoodInfo(7 , "Sting (Chai)", 10000,"https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-tang-luc-sting-huong-dau-330ml-2-1630858772?w=480&type=o","Đồ uống","",100));
    }
    static List<FoodInfo> getDsFood() {
        return dsFood;
    }

    static List<TableInfo> getDsTable() {
        return dsTable;
    }
    static int AmountFood(int idFood){
        int Amount=0;
        for (int i=0;i<=dsFood.size();i++) {
            if(dsFood.get(i).getFoodId() == idFood){
                Amount = dsFood.get(i).getFoodAmount();
                return Amount;
            }
        }
        return Amount;
    }
    static int getFoodRemaining(int idFood) {
        int Remains = 0;
        for (int i = 0; i <= dsFood.size(); i++) {
            if (dsFood.get(i).getFoodId() == idFood) {
                Remains = (dsFood.get(i).getFoodAmount() - dsFood.get(i).getFoodSelAmount());
                return Remains;
            }
        }
        return Remains;
    }
    static void AddFood(int idFood,int idtable){
        for (int i = 0; i <= dsFood.size(); i++) {
            if (dsFood.get(i).getFoodId() == idFood) {
                if( dsFood.get(i).getFoodRemaining()>0&&dsFood.get(i).getFoodSelAmount() < dsFood.get(i).getFoodAmount())
                {
                    dsFood.get(i).addFoodSelAmount(1);
                    dsTable.get(idtable).getDsFoodSelection().get(i).addFoodSelAmount(1);
                }
                return;
            }
        }
    }
    static void MinusFood(int idFood,int idtable){
        for (int i = 0; i <= dsFood.size(); i++) {
            if (dsFood.get(i).getFoodId() == idFood) {
                if (dsTable.get(idtable).getDsFoodSelection().get(i).getFoodSelAmount() > 0 && dsFood.get(i).getFoodSelAmount() <= dsFood.get(i).getFoodAmount())
                    {
                        dsFood.get(i).minusFoodSelAmount(1);
                        dsTable.get(idtable).getDsFoodSelection().get(i).minusFoodSelAmount(1);
                    }
                return;
            }
        }
}
    static void ReturnTable(int idtable){
        for(FoodInfo f: dsFood){
            f.setFoodAmount(f.getFoodAmount()-dsTable.get(idtable).getDsFoodSelection().get(FindIdFoodInTable(f.getFoodId())).getFoodSelAmount());
            f.setFoodSelAmount(f.getFoodSelAmount()- dsTable.get(idtable).getDsFoodSelection().get(FindIdFoodInTable(f.getFoodId())).getFoodSelAmount());
        }
        dsTable.get(idtable).setStatusTable(false);
        dsTable.get(idtable).returnTable();
    }
    static  int FindIdFoodInTable(int idFood){
        for (int i = 0; i <= dsFood.size(); i++) {
            if (dsFood.get(i).getFoodId() == idFood) {
                return i;
            }
        }
        return -1;
    }
}
