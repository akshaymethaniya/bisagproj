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

/**
 *
 * @author FOR ORACLE
 */
public class Districts {
    public static Map<String,Map<String,String>> state_dis=new LinkedHashMap<String,Map<String,String>>();
    public static Map<String,String> getDistricts(String STATE_OSM_ID) throws SQLException
    {
        if(state_dis.containsKey(STATE_OSM_ID))
        {
            return state_dis.get(STATE_OSM_ID);
        }
        Connection con=null;
        int flag=0;
        Map<String,String> disMap=new LinkedHashMap<>();
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/osmdata-2018","akshay", "akshay");
           Statement st=con.createStatement();
           String sql="SELECT DISTINCT P2.name,P2.osm_id from planet_osm_polygon P1,planet_osm_polygon P2 \n" +
                        "WHERE P2.admin_level = '5' \n" +
                        "and P1.osm_id='"+STATE_OSM_ID+"' and ST_Intersects(P2.way,P1.way) ORDER BY P2.name;";
           ResultSet rs=st.executeQuery(sql);
           
           while(rs.next())
           {
               String osm_id=rs.getString(2);
               String disname=rs.getString(1);
               disMap.put(osm_id,disname);
           }
        }catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        finally{
            if(con != null)
                con.close();
        }
        state_dis.put(STATE_OSM_ID, disMap);
        return disMap;
    }
}
