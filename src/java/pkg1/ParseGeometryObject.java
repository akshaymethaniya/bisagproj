/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author FOR ORACLE
 */
public class ParseGeometryObject {
    public static String getCoordinates(String geometry)
    {
        String coodsString="";
        JSONParser jsonParser;
        JSONObject geo ; 
        jsonParser=new JSONParser();
        try {
            geo = (JSONObject) jsonParser.parse(geometry);
            String type=(String)geo.get("type");
            //System.out.println(type);
            JSONArray coordinates=(JSONArray)geo.get("coordinates");
            JSONArray coords=(JSONArray)coordinates.get(0);
            for(Object coord:coords)
            {
                JSONArray coord1=(JSONArray)coord;
                //System.out.print(coord1.get(0));
                //System.out.println(coord1.get(1));
                coodsString+=coord1.get(0)+" "+coord1.get(1);
                coodsString+=" ,";
            }
            //Removing Last ,
            coodsString=coodsString.substring(0, coodsString.length()-1);
        } catch (ParseException ex) {
           System.out.println(ex.getMessage());
        }
        return coodsString;
    }
    
}
