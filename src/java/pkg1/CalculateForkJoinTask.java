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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
public class CalculateWithinGeometryForkJoinTask extends RecursiveTask<Integer>{
    private static final String DB_PREFIX="osmdata-";
    private static final String USERNAME="postgres";
    private static final String PASSWORD="postgres";
    
    private String itemToCount;
    private int year;
    private String STATE_OSM_ID;
    private Map<String, String[]> parameters;
    private String geometry;
    private int totalYears;
    public int []COUNTS;
    public CalculateWithinGeometryForkJoinTask(int totalYears,String itemToCount, int year, String STATE_OSM_ID, Map<String, String[]> parameters, String geometry) {
        this.itemToCount = itemToCount;
        this.year = year;
        this.STATE_OSM_ID = STATE_OSM_ID;
        this.parameters = parameters;
        this.geometry = geometry;
        this.totalYears=totalYears;
        this.COUNTS=new int[totalYears];
    }
    @Override
    protected Integer compute() {
        if(this.totalYears > 1)
        {
            System.out.println("Splitting Task For Each Year");
            CalculateWithinGeometryForkJoinTask [] subtasks = new CalculateWithinGeometryForkJoinTask[this.totalYears];
            for(int i=0;i<this.totalYears;i++)
            {
                subtasks[i]= new CalculateWithinGeometryForkJoinTask(1,this.itemToCount,2014+i, this.STATE_OSM_ID, this.parameters, this.geometry);
                
            }
            for(int i=0;i<this.totalYears;i++)
            {
                subtasks[i].fork();
            }
            for(int i=0;i<this.totalYears;i++){
                COUNTS[subtasks[i].year-2014]=subtasks[i].join();
            }
            return 0;
        }
        else
        {
            int count=CalculateCount.calculateWithinGeometry(itemToCount, year, STATE_OSM_ID, parameters, geometry);
            return count;
        }
    }
    
    
}
