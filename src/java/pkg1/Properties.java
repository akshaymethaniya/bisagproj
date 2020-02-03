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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FOR ORACLE
 */
public class Properties {
    public static Map<String,List<String>> table_properties=new HashMap<String,List<String>>();
    public static List<String> COLUMNS=new ArrayList<String>();
    public static List<String> TABLES=new ArrayList<String>();
    
    private static final String USERNAME="postgres";
    private static final String PASSWORD="postgres";
    
    private static void initilize(){
        TABLES.add("planet_osm_point");
        TABLES.add("planet_osm_line");
        TABLES.add("planet_osm_polygon");
    }
    public static String MapToTable(String item)
    {
        String TABLE_NAME="";
        
        switch (item) {
            case "POINTS":
                TABLE_NAME="planet_osm_point";
                break;
            case "LINES":
                TABLE_NAME="planet_osm_line";
                break;
            case "POLYGONS":
                TABLE_NAME="planet_osm_polygon";
                break;
            default:
                break;
        }
        return TABLE_NAME;
            
    }
    public static Map<String,List<String>> getPropertiesMap(){
        TABLES.stream().filter((tbl) -> (!table_properties.containsKey(tbl))).forEachOrdered((tbl) -> {
            loadFromDB(tbl);
        });
         return table_properties;
    }
    public static List<String> getProperties(String tableName){
        if(TABLES.isEmpty())
        {
            initilize();
        }
        if(table_properties.containsKey(tableName)){
            return table_properties.get(tableName);
        }
        else
        {
            loadFromDB(tableName);
            return table_properties.get(tableName);
        }
    }
    private static void loadFromDB(String tableName){
        List<String> properties=new ArrayList<>();
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://localhost:5432/osmdata-2019",USERNAME,PASSWORD);
            
            st=con.createStatement();
            String sql="SELECT column_name\n" +
                        "FROM information_schema.columns\n" +
                        "WHERE table_name   = '"+tableName+"'";
            rs=st.executeQuery(sql);
            while(rs.next()){
                if(!rs.getString(1).equals("way") && !rs.getString(1).equals("way1"))
                    properties.add(rs.getString(1));
            }
            Collections.sort(properties);
            table_properties.put(tableName, properties);
            
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
    /*public static void main(String[] argv){
       List<String> props=Properties.getProperties("planet_osm_line");
       props.forEach((p) -> {
           System.out.println(p);
        });
    }*/
}
