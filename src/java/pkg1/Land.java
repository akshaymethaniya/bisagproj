/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;

/**
 *
 * @author FOR ORACLE
 */
public class Land {
    private String name;
    private int population;
    private double area;
    private String polygon_geo;
    private String created;
    private String famous_place;
    private int ID;
    public Land(String name, int population, double area, String polygon_geo, String created,String famous_place) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.polygon_geo = polygon_geo;
        this.created = created;
        this.famous_place=famous_place;
    }

    public Land(String name, int population, double area, String polygon_geo, String created, String famous_place, int ID) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.polygon_geo = polygon_geo;
        this.created = created;
        this.famous_place = famous_place;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    

    public String getFamous_place() {
        return famous_place;
    }

    public void setFamous_place(String famous_place) {
        this.famous_place = famous_place;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public double getArea() {
        return area;
    }

    public String getPolygon_geo() {
        return polygon_geo;
    }

    public String getCreated() {
        return created;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setPolygon_geo(String polygon_geo) {
        this.polygon_geo = polygon_geo;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
}
