package eurecom.moveit;

import android.util.Log;

/**
 * Created by omhani on 12/12/2017.
 */

public class Profil {

    private String name;
    private String phone;
    private String height;
    private String width;
    private String weight;
    private String descri;
    private String price;
    private double lati ;
    private double longi ;


    public Profil() {
            Log.w("Profil", "Deprecated class. Use request instead");
    }


    public String getNames() {return name;}
    public double getLati(){return  lati;}
    public double getLongi(){return  longi;}
    public String getPhone() {return phone;}
    public String getHeight() {return height;}
    public String getWidth() {return width;}
    public String getWeight() {return weight;}
    public String getDescri() {return descri;}
    public String getPrice() {return price;}
    public void setPrice(String price) {this.price = price;}
    public void setLati(double latitude){this.lati=latitude;}
    public void setLongi(double longitude){this.longi=longitude;}
    public void setNames(String Name) {this.name = Name;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setHeight(String height) {this.height = height;}
    public void setWidth(String width) {this.width = width;}
    public void setWeight(String weight) {this.weight = weight;}
    public void setDescri(String descri) {this.descri = descri;}





}
