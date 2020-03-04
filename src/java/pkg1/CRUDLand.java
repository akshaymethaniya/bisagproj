/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static pkg1.PostgreSqlConnectionData.HOST;
import static pkg1.PostgreSqlConnectionData.PASSWORD;
import static pkg1.PostgreSqlConnectionData.USERNAME;
/**
 *
 * @author FOR ORACLE
 */
public class CRUDLand {

    public static void addLand(Land l){
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/lands",USERNAME,PASSWORD);
            
            PreparedStatement stmt=con.prepareStatement("insert into land(name,population,area,created,polygon_geo,famous_place) values(?,?,?,?,?,?)");  
            stmt.setString(1,l.getName());
            stmt.setInt(2,l.getPopulation());
            stmt.setDouble(3,l.getArea());
            stmt.setString(4, l.getCreated());
            stmt.setString(5,l.getPolygon_geo());
            stmt.setString(6, l.getFamous_place());
            int i=stmt.executeUpdate();  
            
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
    
    public static List<Land> getLands(){
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        List<Land> lands=new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/lands",USERNAME,PASSWORD);
            
            st = con.createStatement();
            rs = st.executeQuery("select * from land");
            
            while(rs.next())
            {
                int ID=Integer.parseInt(rs.getString("id"));
                String name=rs.getString("name");
                int population=rs.getInt("population");
                double area=rs.getDouble("area");
                String created=rs.getString("created");
                String polygon_geo=rs.getString("polygon_geo");
                String famous_place=rs.getString("famous_place");
                lands.add(new Land(name,population,area,polygon_geo,created,famous_place,ID));
            }
            return lands;
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
        return lands;
    }
    
    
    public static void removeLand(int id){
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/lands",USERNAME,PASSWORD);
            
            PreparedStatement stmt=con.prepareStatement(" delete from land where id=?");  
            stmt.setInt(1, id);
            int i=stmt.executeUpdate();  
            
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
    
    public static void updateLand(Land l){
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/lands",USERNAME,PASSWORD);
            
            PreparedStatement stmt=con.prepareStatement("Update land set name=?,population=?,area=?,created=?,polygon_geo=?,famous_place=? where id=?");  
            stmt.setString(1,l.getName());
            stmt.setInt(2,l.getPopulation());
            stmt.setDouble(3,l.getArea());
            stmt.setString(4, l.getCreated());
            stmt.setString(5,l.getPolygon_geo());
            stmt.setString(6, l.getFamous_place());
            stmt.setInt(7,l.getID());
            int i=stmt.executeUpdate();  
            
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
