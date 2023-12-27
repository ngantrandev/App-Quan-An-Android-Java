package com.example.quanlyquanan.model;

public class TransferTable{
    Table currTable;
    Table targetTable;

    public TransferTable(Table currTable, Table targetTable) {
        this.currTable = currTable;
        this.targetTable = targetTable;
    }

    public Table getCurrTable() {
        return currTable;
    }

    public void setCurrTable(Table currTable) {
        this.currTable = currTable;
    }

    public Table getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(Table targetTable) {
        this.targetTable = targetTable;
    }
}
