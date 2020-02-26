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
    public static final String HOST="34.80.94.206"+":"+PORT;
    
    public static String getHost(int year)
    {
        if(year == 2018)
        {
            return "35.228.180.77"+":"+PORT;
        }
        return "34.80.94.206"+":"+PORT;
    }
}
