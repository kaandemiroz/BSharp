package com.okd.bsharp;

import java.util.ArrayList;

/**
 * Created by OKD on 26.5.2016.
 */
public class Tab {

    private String[] strings;
    private ArrayList<String> lines;

    public Tab(){
        strings = new String[6];
        lines = new ArrayList<>();
        initStrings();
    }

    private void initStrings(){
        strings[0] = "E|—";
        strings[1] = "B|—";
        strings[2] = "G|—";
        strings[3] = "D|—";
        strings[4] = "A|—";
        strings[5] = "E|—";
    }

    public void addColumn(int string, int note){
        for(int i=0; i<strings.length; i++){
            if(i==string){
                if(note / 10 > 0)
                    strings[i] += note + " -";
                else
                    strings[i] += note + " —";
            }
            else strings[i] += "——";
        }
        if(strings[0].length() > 250){
            String line = "";
            for(int i=0; i<strings.length; i++){
                line += strings[i] + "\n";
            }
            lines.add(line);
            initStrings();
        }
    }

    public void addColumn(String[] stringArray){
        for(int i=0; i<strings.length; i++){
            strings[i] += stringArray[i] + "\t";
        }
        if(strings[0].length() > 250){
            String line = "";
            for(int i=0; i<strings.length; i++){
                line += strings[i] + "\n";
            }
            lines.add(line);
            initStrings();
        }
    }

    public String toString(){
        String result = "";
        for(int i=0; i<lines.size(); i++){
            result += lines.get(i) + "\n";
        }
        for(int i=0; i<strings.length; i++){
            result += strings[i] + "\n";
        }
        return result;
    }

}
