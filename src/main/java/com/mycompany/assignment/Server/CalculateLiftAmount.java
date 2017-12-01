/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

/**
 *
 * @author chuzhan
 */
public class CalculateLiftAmount {
    public static int verticalCalculation(int liftNumber){
        if(liftNumber>0 && liftNumber < 11)
            return 200;
        else if(liftNumber>10 && liftNumber<21)
            return 300;
        else if(liftNumber>20 && liftNumber<31)
            return 400;
        else if(liftNumber>30 && liftNumber<41)
            return 500;
        else
            return 0;
    }
}
