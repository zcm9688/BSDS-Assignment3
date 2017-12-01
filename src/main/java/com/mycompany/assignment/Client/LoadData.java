/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chuzhan
 */
public class LoadData {
    public static List<RFIDLiftData> loadResortData(){
        //RFIDLiftData[] skiData = new RFIDLiftData[800000];
        List<RFIDLiftData> skiData = new ArrayList<>();
        try {    
//            BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day1.csv")); 
//            BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day2.csv")); 
//            BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day999.csv")); 
//            BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day3.csv"));
//              BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day4.csv"));
              BufferedReader reader = new BufferedReader(new FileReader("/Users/chuzhan/Downloads/BSDSAssignment2Day5.csv"));

//            reader.readLine();   
            String line = null;
            int i=0;
            while((line=reader.readLine())!=null){    
                String item[] = line.split(",");
                skiData.add(new RFIDLiftData(Integer.valueOf(item[0]), 
                        Integer.valueOf(item[1]),
                        Integer.valueOf(item[2]),
                        Integer.valueOf(item[3]),
                        Integer.valueOf(item[4])));
                i++;
            }
        } catch (Exception e) {    
            e.printStackTrace();    
        }
        System.out.println(skiData.size());
        return skiData;
    }
}
