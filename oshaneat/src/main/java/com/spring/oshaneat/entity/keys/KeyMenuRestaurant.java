package com.spring.oshaneat.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class KeyMenuRestaurant implements Serializable {
    @Column(name = "cateId")
    private int cateID;

    @Column(name = "resId")
    private int resID;

    public KeyMenuRestaurant() {
    }

    public KeyMenuRestaurant(int cateID, int resID) {
        this.cateID = cateID;
        this.resID = resID;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }
}
