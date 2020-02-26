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
import static pkg1.PostgreSqlConnectionData.HOST;

/**
 *
 * @author FOR ORACLE
 */
public class Properties {
    public static Map<String,List<String>> table_properties=new HashMap<String,List<String>>();
    public static Map<String,List<PropertyValue>> properties_possible_values=new HashMap<>();
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
    public static List<PropertyValue> getPropertyPossibleValues(String PropertyName)
    {
        if(TABLES.isEmpty())
        {
            initilize();
        }
        if(properties_possible_values.containsKey(PropertyName))
        {
            return properties_possible_values.get(PropertyName);
        }
        else
        {
            loadPropertyPossibleValuesFromDB(PropertyName);
            return properties_possible_values.get(PropertyName);
        }
    }
    
    private static void loadAllPropertyPossibleValuesFromDB()
    {
        if(!properties_possible_values.isEmpty())
        {
            return;
        }
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            
            Class.forName("org.postgresql.Driver");
                        con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/osmdata-2014",USERNAME,PASSWORD);

            
            st=con.createStatement();
            String sql="SELECT * FROM properties";
            rs=st.executeQuery(sql);
            while(rs.next()){
                String key=rs.getString(1);
                String value=rs.getString(2);
                String desc=rs.getString(3);
                if(!properties_possible_values.containsKey(key))
                {
                    properties_possible_values.put(key, new ArrayList<>());
                }
                List<PropertyValue> pvs=properties_possible_values.get(key);
                pvs.add(new PropertyValue(key,value,desc));
            }
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
    private static void loadFromDB(String tableName){
        List<String> properties=new ArrayList<>();
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/osmdata-2014",USERNAME,PASSWORD);

            
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
    public static void main(String[] argv){
       List<String> pro=new ArrayList<>();
       List<String> props=Properties.getProperties("planet_osm_line");
       props.forEach((p) -> {
           if(!pro.contains(p))
           {
               pro.add(p);
           }
        });
       List<String> props1=Properties.getProperties("planet_osm_point");
       props1.forEach((p) -> {
           if(!pro.contains(p))
           {
               pro.add(p);
           }
        });
       List<String> props2=Properties.getProperties("planet_osm_polygon");
       props2.forEach((p) -> {
           if(!pro.contains(p))
           {
               pro.add(p);
           }
        });
       pro.forEach((p) -> {
           System.out.println(p);
       });
       System.out.println(pro.size());
    }
    
    /*TEST METHOD
    public static void main(String []args)
    {
        for(PropertyValue pv:getPropertyPossibleValues("waterway"))
        {
            System.out.println(pv.getPropertyValue());
            System.out.println(pv.getDescription());
        }
    }*/
    private static void loadPropertyPossibleValuesFromDB(String PropertyName) {
        List<PropertyValue> propertyValues=new ArrayList<>();
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://localhost:5432/postgres",USERNAME,PASSWORD);
            
            st=con.createStatement();
            String sql="SELECT prop_value,prop_desc FROM properties WHERE prop_key = '"+PropertyName+"'";
            rs=st.executeQuery(sql);
            while(rs.next()){
                String value=rs.getString(1);
                String desc=rs.getString(2).substring(0, 15);
                propertyValues.add(new PropertyValue(PropertyName,value,desc));
            }
            properties_possible_values.put(PropertyName, propertyValues);
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
}
