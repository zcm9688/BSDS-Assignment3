/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

/**
 *
 * @author chuzhan
 */
public class PersonalVertical {
    private int skierID;
    private int dayNum;
    private int lifts;
    private int vertical;
    
    public PersonalVertical(){       
    }
    
    public PersonalVertical(int skierID, int dayNum, int lifts, int vertical) {
        this.skierID = skierID;
        this.dayNum = dayNum;
        this.lifts = lifts;
        this.vertical = vertical;
    }
    
    public int getSkierID() {
        return skierID;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }
    
    public int getLifts() {
        return lifts;
    }

    public void setLifts(int lifts) {
        this.lifts = lifts;
    }
    
    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    } 
}
