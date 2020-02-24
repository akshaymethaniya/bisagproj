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
import java.util.LinkedHashMap;
import java.util.Map;
import static pkg1.PostgreSqlConnectionData.HOST;
import static pkg1.PostgreSqlConnectionData.PASSWORD;
import static pkg1.PostgreSqlConnectionData.USERNAME;

/**
 *
 * @author FOR ORACLE
 */
public class States {
    public static Map<String,String> stateMap=new LinkedHashMap<String,String>();
    public static String getStateName(String STATE_OSM_ID)
    {
        if(stateMap.isEmpty())
        {
            getStates();
        }
        return stateMap.get(STATE_OSM_ID);
    }
    public static Map<String,String> getStates() 
    {
        if(!stateMap.isEmpty())
        {
            return stateMap;
        }
        Connection con=null;
        int flag=0;
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/osmdata-2014",USERNAME,PASSWORD);
           Statement st=con.createStatement();
           String sql="SELECT DISTINCT name,osm_id FROM planet_osm_polygon WHERE admin_level ='4' and name IS NOT NULL ORDER BY name";
           ResultSet rs=st.executeQuery(sql);           
           while(rs.next())
           {
               String osm_id=rs.getString(2);
               String statename=rs.getString(1);
               stateMap.put(osm_id,statename);
           }
        }catch(ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        finally{
            if(con != null){
                try
                {
                    con.close();
                }catch(SQLException se)
                {
                    se.printStackTrace();
                    System.err.println(se.getClass().getName()+": "+se.getMessage());
                }
            }
        }
        /*stateMap.put("-2022095","Andhra Pradesh");
        stateMap.put("-2025886","Assam");
        stateMap.put("-1958982","Bihar");
        stateMap.put("-1942809","Chandigarh");
        stateMap.put("-1949080", "Gujarat");
        stateMap.put("-1972004","Chhattisgarh");
        stateMap.put("-1952530","Dadra and Nagar Haveli");
        stateMap.put("-1942586","Delhi");
        stateMap.put("-1997192","Goa");
        stateMap.put("-1942601","Haryana");
        stateMap.put("-364186","Himachal Pradesh");*/
        
        return stateMap;
    }
    
}
