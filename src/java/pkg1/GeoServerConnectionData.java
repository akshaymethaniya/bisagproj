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
public class GeoServerConnectionData {
    private static final String HOST="localhost";
    private static final String PORT="8080";
    
    public static String getHostWithPort()
    {
        return HOST+":"+PORT;
    }
    
}
