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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author FOR ORACLE
 */
public class CalculateCount  {
    public static Map<String,Integer> QueryResult=new HashMap<String,Integer>();
    private static final String DB_PREFIX="osmdata-";
    private static final String USERNAME="postgres";
    private static final String PASSWORD="postgres";
    public CalculateCount()
    {
        
    }
    public static int calculateWithinGeometry(String itemToCount,int year,String STATE_OSM_ID,Map<String, String[]> parameters,String geometry)
    {
        System.out.println("CAlCULATING FOR YEAR STARTED :"+year+"On "+new Date());
        
        String DBNAME=getDBName(year);
        String TABLE_NAME=MapToTable(itemToCount);
        
        int count=0;
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
                " way1 && "+
                "'"+
                "LINESTRING("+
                ParseGeometryObject.getCoordinates(geometry)+
                ")'"+
                "::geometry";
        String sql=SELECT_PART+FROM_PART+WHERE_PART;
        
        
        if(QueryResult.containsKey(sql+";"+year))
        {
            return QueryResult.get(sql+";"+year);
        }
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://localhost:5432/"+DBNAME,USERNAME,PASSWORD);
            
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
            QueryResult.put(sql+";"+year, count);    
            System.out.println("CAlCULATING FOR YEAR ENDED :"+year+" On "+new Date());

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
        return count;
    }
    public static String getDBName(int year)
    {
        return DB_PREFIX+String.valueOf(year);
    }
    public static String MapToTable(String item)
    {
        String TABLE_NAME;
        
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
                TABLE_NAME="planet_osm_line";
        }
        return TABLE_NAME;
            
    }
    public static String generateWherePart(Map<String, String[]> parameters)
    {
        String WHERE_PART="";

        for(Map.Entry<String,String []> entry:parameters.entrySet())
        {
            if(entry.getValue()[0].equals("Anything"))
            {
                WHERE_PART+="P2."+entry.getKey()+" IS NOT NULL AND ";
            }
            else
            {
                WHERE_PART+="P2."+entry.getKey()+" = '"+entry.getValue()[0]+"' AND ";
            }
        }
        return WHERE_PART;
    }
    public static String generateSqlQuery(String TABLE_NAME,String WHERE_PART,String STATE_OSM_ID)
    {
        String SELECT_PART="select COUNT(distinct P2.osm_id) ";
        return SELECT_PART+"FROM planet_osm_polygon P1,"+TABLE_NAME+" P2 "+"WHERE "+WHERE_PART+"P1.osm_id = "+STATE_OSM_ID+" AND ST_within(P2.way, P1.way)";
    }
    public static int calculate(String itemToCount,int year,String STATE_OSM_ID,Map<String, String[]> parameters) throws SQLException
    {
        String DBNAME=getDBName(year);
        String TABLE_NAME=MapToTable(itemToCount);
        String WHERE_PART=generateWherePart(parameters);
        String sql=generateSqlQuery(TABLE_NAME,WHERE_PART,STATE_OSM_ID);

        String tempsql="FROM planet_osm_polygon P1,"+TABLE_NAME+" P2 "+"WHERE "+WHERE_PART+"P1.osm_id = "+STATE_OSM_ID+" AND ST_within(P2.way, P1.way)";
        
        int count=0;
        Connection con=null;
       
        System.out.println("\n\n\n\n\nQuery : "+sql);

        //Check Is Already Calculated
        if(QueryResult.containsKey(sql+";"+year))
        {
            return QueryResult.get(sql+";"+year);
        }
        //If not
        try {
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://localhost:5432/"+DBNAME,USERNAME,PASSWORD);
            
            Statement st=con.createStatement();
            
            

            String dropViewSql="DROP VIEW IF EXISTS myview_"+year;
            String createViewSql="CREATE VIEW myview_"+year+" AS SELECT P2.* "+tempsql;
            st.execute(dropViewSql);
            st.execute(createViewSql);
            
            ResultSet rs=st.executeQuery(sql);
            if(rs.next())
            {
                count=rs.getInt(1);
            }
            QueryResult.put(sql+";"+year, count);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        finally{
            if(con!=null)
                con.close();
        }
        return count;
    }
}
