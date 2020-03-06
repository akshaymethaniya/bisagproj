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
public class PostgreSqlConnectionData {
    public static final String USERNAME="postgres";
    public static final String PASSWORD="postgres";
    public static final String PORT="5432";
    public static final String HOST="35.229.165.44";
    
    public static String getHost(int year)
    {
        return HOST+":"+PORT;
    }
}
