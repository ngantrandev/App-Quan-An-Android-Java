package com.example.quanlyquanan.hao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableInfo implements Serializable  {
    private int idTable;
    private boolean statusTable;
    private String NoteTable;

    private List<FoodInfo> dsFoodSelection;
    private  List<BillInfo_hao> Bills;

    public TableInfo(int idTable, boolean statusTable, String noteTable) {
        this.idTable = idTable;
        this.statusTable = statusTable;
        this.NoteTable = noteTable;
        this.dsFoodSelection=new ArrayList<>();
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public boolean isStatusTable() {
        return statusTable;
    }

    public void setStatusTable(boolean statusTable) {
        this.statusTable = statusTable;
    }

    public String getNoteTable() {
        return NoteTable;
    }

    public void setNoteTable(String noteTable) {
        NoteTable = noteTable;
    }

    public List<FoodInfo> getDsFoodSelection() {
        return dsFoodSelection;
    }

    public void setDsFoodSelection(List<FoodInfo> dsFoodSelection) {
            //this.dsFoodSelection = new ArrayList<>(dsFoodSelection);
        this.dsFoodSelection = new ArrayList<FoodInfo>();
        this.dsFoodSelection.addAll(dsFoodSelection);
    }

    public void addFoodSelection(FoodInfo Food) {
        this.dsFoodSelection.add(new FoodInfo(Food.getFoodId() , Food.getFoodName(), Food.getFoodPrice(),Food.getFoodThumb(),Food.getFoodtype(),Food.getFoodNote(), Food.getFoodAmount()));
    }
    public void clearfoodselection(){
        this.dsFoodSelection.clear();
    }
    public void returnTable(){
        setNoteTable("");
        for(FoodInfo f:dsFoodSelection){
            f.resetMenu();
        }
    }
}
