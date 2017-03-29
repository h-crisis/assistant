package org.geotools.Shelter;

/**
 * Created by jiao.xue on 2017/02/08.
 */
public class shelters {
    private String Id;
    private String Name;
    private double Lon;
    private double Lat;

    shelters(String Id, String Name, double Lon, double Lat){
        this.Id=Id;
        this.Name=Name;
        this.Lon=Lon;
        this.Lat=Lat;

    }

    public String getId(){
        return Id;
    }

    public void setId(String Id){
        this.Id=Id;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name){
        this.Name=Name;
    }

    public double getLat(){
        return Lat;
    }

    public void setLat(double Lat){
        this.Lat=Lat;
    }

    public double getLon(){
        return Lon;
    }

    public void setLon(double Lon){
        this.Lon=Lon;
    }

}
