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
import java.util.logging.Level;
import java.util.logging.Logger;
import static pkg1.PostgreSqlConnectionData.HOST;
import static pkg1.PostgreSqlConnectionData.PASSWORD;
import static pkg1.PostgreSqlConnectionData.USERNAME;
import static pkg1.PostgreSqlConnectionData.getHost;

/**
 *
 * @author FOR ORACLE
 */
public class CalculateCount  {
    public static Map<String,Integer> QueryResult=new HashMap<String,Integer>();
    private static final String DB_PREFIX="osmdata-";
    
    String itemToCount;
    int year;
    String STATE_OSM_ID;
    Map<String, String[]> parameters;
    String []geometry;
    public double x_bbox;
    public double y_bbox;

    public double getX_bbox() {
        return x_bbox;
    }

    public double getY_bbox() {
        return y_bbox;
    }
    public CalculateCount(String itemToCount, int year, String STATE_OSM_ID, Map<String, String[]> parameters, String []geometry) {
        this.itemToCount = itemToCount;
        this.year = year;
        this.STATE_OSM_ID = STATE_OSM_ID;
        this.parameters = parameters;
        this.geometry = geometry;
    }
    public CalculateCount(String itemToCount, int year, String STATE_OSM_ID, Map<String, String[]> parameters) {
        this.itemToCount = itemToCount;
        this.year = year;
        this.STATE_OSM_ID = STATE_OSM_ID;
        this.parameters = parameters;
    }

    public int calculateWithinGeometry()
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
            if(entry.getValue()[0].equals("*"))
            {
                WHERE_PART+=entry.getKey()+" IS NOT NULL AND ";
            }
            else
            {
                WHERE_PART+=entry.getKey()+" = '"+entry.getValue()[0]+"' AND ";
            }
        }
        WHERE_PART+="( ";
        for(int i=0;i<geometry.length;i++)
        {
            WHERE_PART+=
                "ST_Transform(way,4326) && "+
                "'"+
                "LINESTRING("+
                ParseGeometryObject.getCoordinates(geometry[i])+
                ")'"+
                "::geometry ";
            if(i!=geometry.length-1)
                WHERE_PART+="OR ";
            if(i==geometry.length-1)
                WHERE_PART+=")";
        }
        
        String sql=SELECT_PART+FROM_PART+WHERE_PART;
        System.out.println(sql);
        
       
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://"+HOST+"/"+DBNAME,USERNAME,PASSWORD);
            
            st=con.createStatement();
            
            String dropViewSql="DROP VIEW IF EXISTS view"+year+"_"+itemToCount;
            String createViewSql="CREATE VIEW view"+year+"_"+itemToCount+" AS SELECT * "+FROM_PART+WHERE_PART;
            st.execute(dropViewSql);
            st.execute(createViewSql);
            
            String cal_bboxSql="SELECT (st_xmin(ST_Extent(ST_Transform(way,4326)))+st_xmax(ST_Extent(ST_Transform(way,4326))))/2 as x,"+
                    "(st_ymin(ST_Extent(ST_Transform(way,4326)))+st_ymax(ST_Extent(ST_Transform(way,4326))))/2 as y "+FROM_PART+WHERE_PART;
            //System.out.println(cal_bboxSql);
            ResultSet rs1=st.executeQuery(cal_bboxSql);
            if(rs1.next())
            {
               double x=rs1.getDouble(1);
               double y=rs1.getDouble(2);
               x_bbox=x;
               y_bbox=y;
               System.out.println(x+" : "+y);
            }
            //Check Is Already Calculated
            if(QueryResult.containsKey(sql+";"+year))
            {
                System.out.println("\t\tCAlCULATING FOR YEAR ENDED :"+year+" On "+new Date());
                return QueryResult.get(sql+";"+year);
            }
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
            if(entry.getValue()[0].equals("*"))
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
    public int calculate() 
    {
        System.out.println(" 2.CAlCULATING FOR YEAR STARTED :"+year+"On "+new Date());
        //System.out.println(parameters.toString());
        String DBNAME=getDBName(year);
        String TABLE_NAME=MapToTable(itemToCount);
        String WHERE_PART=generateWherePart(parameters);
        String sql=generateSqlQuery(TABLE_NAME,WHERE_PART,STATE_OSM_ID);

        String tempsql="FROM planet_osm_polygon P1,"+TABLE_NAME+" P2 "+"WHERE "+WHERE_PART+"P1.osm_id = "+STATE_OSM_ID+" AND ST_within(P2.way, P1.way)";
        
        int count=0;
        Connection con=null;
       
        System.out.println("\n\n\n\n\nQuery : "+sql);

        
        //If not
        try {
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager
               .getConnection("jdbc:postgresql://"+getHost(year)+"/"+DBNAME,USERNAME,PASSWORD);
            //Connection con1=DriverManager
            //   .getConnection("jdbc:postgresql://localhost:5432"+"/"+DBNAME,USERNAME,PASSWORD);
            Statement st=con.createStatement();
            //Statement st1=con1.createStatement();

            String dropViewSql="DROP VIEW IF EXISTS view"+year+"_"+itemToCount;
            String createViewSql="CREATE VIEW view"+year+"_"+itemToCount+" AS SELECT P2.* "+tempsql;
            //System.out.println(createViewSql);
            st.execute(dropViewSql);
            st.execute(createViewSql);
            
            String cal_bboxSql="SELECT (st_xmin(ST_Extent(ST_Transform(P2.way,4326)))+st_xmax(ST_Extent(ST_Transform(P2.way,4326))))/2 as x,"+
                    "(st_ymin(ST_Extent(ST_Transform(P2.way,4326)))+st_ymax(ST_Extent(ST_Transform(P2.way,4326))))/2 as y "+tempsql;
            //System.out.println(cal_bboxSql);
            ResultSet rs1=st.executeQuery(cal_bboxSql);
            if(rs1.next())
            {
               double x=rs1.getDouble(1);
               double y=rs1.getDouble(2);
               x_bbox=x;
               y_bbox=y;
               System.out.println(x+" : "+y);
            }
            //Check Is Already Calculated
            if(QueryResult.containsKey(sql+";"+year))
            {
                System.out.println("\t\tOUTPUT FROM STATIC VAR : CAlCULATING FOR YEAR ENDED :"+year+" On "+new Date());
                return QueryResult.get(sql+";"+year);
            }
            ResultSet rs=st.executeQuery(sql);
            if(rs.next())
            {
                count=rs.getInt(1);
            }
            QueryResult.put(sql+";"+year, count);
            System.out.println("\t\tCAlCULATING FOR YEAR ENDED :"+year+" On "+new Date());

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        finally{
            if(con!=null)
                try {
                    con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalculateCount.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return count;
    }
}
