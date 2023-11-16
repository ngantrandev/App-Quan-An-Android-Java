package com.example.quanlyquanan.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String _id, name;
    private int foodnumber;

    public Category(String _id, String name, int foodnumber) {
        this._id = _id;
        this.name = name;
        this.foodnumber = foodnumber;
    }

    public int getFoodnumber() {
        return foodnumber;
    }

    public void setFoodnumber(int foodnumber) {
        this.foodnumber = foodnumber;
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
}
