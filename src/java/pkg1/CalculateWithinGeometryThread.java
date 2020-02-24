/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import static pkg1.CalculateCount.MapToTable;
import static pkg1.CalculateCount.getDBName;
import static pkg1.PostgreSqlConnectionData.HOST;
import static pkg1.PostgreSqlConnectionData.PASSWORD;
import static pkg1.PostgreSqlConnectionData.USERNAME;

/**
 *
 * @author FOR ORACLE
 */
public class CalculateWithinGeometryThread  extends Thread{
    private static final String DB_PREFIX="osmdata-";
    
    String itemToCount;
    int year;
    String STATE_OSM_ID;
    Map<String, String[]> parameters;
    String geometry;
    int count;
    public CalculateWithinGeometryThread(String itemToCount, int year, String STATE_OSM_ID, Map<String, String[]> parameters, String geometry) {
        this.itemToCount = itemToCount;
        this.year = year;
        this.STATE_OSM_ID = STATE_OSM_ID;
        this.parameters = parameters;
        this.geometry = geometry;
    }
    
    @Override
    public void run() {
         //To change body of generated methods, choose Tools | Templates.
         System.out.println("CAlCULATING FOR YEAR :"+year);
        
        String DBNAME=getDBName(year);
        String TABLE_NAME=MapToTable(itemToCount);
        
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        
       
        //STILL NEED TO ADD PROPERTIES IN WHERE PART
        String SELECT_PART="SELECT COUNT(distinct osm_id) ";
        String FROM_PART="FROM "+TABLE_NAME+" ";
        String WHERE_PART="WHERE ";
        for(Map.Entry<String,String []> entry:parameters.entrySet())
        {
            if(entry.getValue()[0].equals("Anything"))
            {
                WHERE_PART+=entry.getKey()+" IS NOT NULL AND ";
            }
            else
            {
                WHERE_PART+=entry.getKey()+" = '"+entry.getValue()[0]+"' AND ";
            }
        }
        WHERE_PART+=
                " ST_Transform(way,4326) && "+
                "'"+
                "LINESTRING("+
                ParseGeometryObject.getCoordinates(geometry)+
                ")'"+
                "::geometry";
        
        
        String sql=SELECT_PART+FROM_PART+WHERE_PART;
        System.out.println(sql);
        
        
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://"+HOST+"/"+DBNAME,USERNAME,PASSWORD);
            
            st=con.createStatement();
            
            String dropViewSql="DROP VIEW IF EXISTS myview_"+year;
            String createViewSql="CREATE VIEW myview_"+year+" AS SELECT * "+FROM_PART+WHERE_PART;
            st.execute(dropViewSql);
            st.execute(createViewSql);
            
            rs=st.executeQuery(sql);
            if(rs.next())
            {
                count=rs.getInt(1);
            }
            System.out.println("CAlCULATING FOR YEAR ENDED :"+year);

        }catch(ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (con!= null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }
    public int getCount()
    {
        return count;
    }
    
}
