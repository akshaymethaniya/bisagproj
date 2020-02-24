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
import java.util.concurrent.RecursiveTask;
public class CalculateForkJoinTask extends RecursiveTask<Integer>{
    
    
    private final int totalYears;
    public int []COUNTS;
    private final CalculateCount cc;
    public CalculateForkJoinTask(int totalYears,CalculateCount cc) {
        this.totalYears=totalYears;
        this.cc=cc;
        this.COUNTS=new int[totalYears];
    }
    @Override
    protected Integer compute() {
        if(this.totalYears > 0)
        {
            System.out.println("Splitting Task For Each Year");
            CalculateForkJoinTask [] subtasks = new CalculateForkJoinTask[this.totalYears];
            for(int i=0;i<this.totalYears;i++)
            {
                
                if(cc.geometry!=null)
                    subtasks[i]= new CalculateForkJoinTask(0,new CalculateCount(cc.itemToCount,2014+i,cc.STATE_OSM_ID,cc.parameters,cc.geometry));                
                else
                    subtasks[i]= new CalculateForkJoinTask(0,new CalculateCount(cc.itemToCount,2014+i,cc.STATE_OSM_ID,cc.parameters));                
            }
            for(int i=0;i<this.totalYears;i++)
            {
                subtasks[i].fork();
            }
            for(int i=0;i<this.totalYears;i++){
                COUNTS[subtasks[i].cc.year-2014]=subtasks[i].join();
            }
            return 0;
        }
        else
        {
            int count;
            if(cc.geometry!=null)
                count = cc.calculateWithinGeometry();
            else
                count = cc.calculate();
            return count;
        }
    }
    
    
}
