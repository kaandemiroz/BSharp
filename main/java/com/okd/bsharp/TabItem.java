package com.okd.bsharp;

/**
 * Created by OKD on 1.6.2016.
 */
public class TabItem {

    private String string1;
    private String string2;
    private String string3;
    private String string4;
    private String string5;
    private String string6;

    public TabItem(String string1, String string2, String string3,
                   String string4, String string5, String string6){

        this.string1 = string1;
        this.string2 = string2;
        this.string3 = string3;
        this.string4 = string4;
        this.string5 = string5;
        this.string6 = string6;

    }

    public TabItem(String[] strings){
        string1 = strings[0];
        string2 = strings[1];
        string3 = strings[2];
        string4 = strings[3];
        string5 = strings[4];
        string6 = strings[5];
    }

    public TabItem(int string, int position){
        string1 = "—";
        string2 = "—";
        string3 = "—";
        string4 = "—";
        string5 = "—";
        string6 = "—";
        switch (string + 1){
            case 1:
                string1 = position + "";
                break;
            case 2:
                string2 = position + "";
                break;
            case 3:
                string3 = position + "";
                break;
            case 4:
                string4 = position + "";
                break;
            case 5:
                string5 = position + "";
                break;
            case 6:
                string6 = position + "";
                break;
        }
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

    public String getString4() {
        return string4;
    }

    public void setString4(String string4) {
        this.string4 = string4;
    }

    public String getString5() {
        return string5;
    }

    public void setString5(String string5) {
        this.string5 = string5;
    }

    public String getString6() {
        return string6;
    }

    public void setString6(String string6) {
        this.string6 = string6;
    }

    public String[] getStrings(){
        return new String[]{string1, string2, string3, string4, string5, string6};
    }

    public String toString(){
        return string1 + "," + string2 + "," +string3 + "," +string4 + "," +string5 + "," +string6 + ";";
    }
}
