/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author FOR ORACLE
 */
@MultipartConfig
public class LoadInitDataForStateWiseAnalysis extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String STATE_OSM_ID="";
        String ITEM_TO_COUNT="";
        int fromyear=2014;
        int toyear=2019;

        if(request.getParameterMap().containsKey("fromyear"))
        {
            fromyear=Integer.parseInt(request.getParameter("fromyear"));
        }
        if(request.getParameterMap().containsKey("toyear"))
        {
            toyear=Integer.parseInt(request.getParameter("toyear"));
        }
        
        if(request.getParameterMap().containsKey("state_osm_id"))
        {
            STATE_OSM_ID=(String)request.getParameter("state_osm_id");
        }
        if(request.getParameterMap().containsKey("itemToCount"))
        {
            ITEM_TO_COUNT=(String)request.getParameter("itemToCount");
        }
        
        ArrayList<String> ITEMS_TO_COUNT=new ArrayList<>();
        ITEMS_TO_COUNT.add("POINTS");
        ITEMS_TO_COUNT.add("LINES");
        ITEMS_TO_COUNT.add("POLYGONS");
        
        ServletContext context =request.getServletContext();
        String path_propsVals = context.getRealPath("/WEB-INF/props_vals.json");
        //System.out.println("PATH:"+fullPath);
        
        System.out.println("TOYEAR : "+toyear);
        
        request.setAttribute("STATE_OSM_ID",STATE_OSM_ID);
        request.setAttribute("ITEM_TO_COUNT",ITEM_TO_COUNT);
        request.setAttribute("FROMYEAR",fromyear);
        request.setAttribute("TOYEAR",toyear);
        request.setAttribute("lands", CRUDLand.getLands());

        
        String toForward="StateWiseAnalysis.jsp";

        if(toyear < fromyear)
        {
            request.setAttribute("ITEMS_TO_COUNT",ITEMS_TO_COUNT);
            request.setAttribute("stateMap",States.getStates());
            request.setAttribute("ERROR_MESS","'FROM YEAR' SHOULD BE LESS THAN OR EQUAL TO 'TO YEAR'");
            
            RequestDispatcher rd=request.getRequestDispatcher(toForward); 
            rd.forward(request, response);
        }
        Integer []YEARS=new Integer[toyear-fromyear+1];
        for(int i=0;i<toyear-fromyear+1;i++)
        {
            YEARS[i]=fromyear+i;
        }
        request.setAttribute("YEARS", YEARS);
       
        //Get Properties Button Clicked
        if(request.getParameter("loadProperties")!=null)
        {
            
            request.setAttribute("ITEMS_TO_COUNT",ITEMS_TO_COUNT);
            request.setAttribute("stateMap",States.getStates());
            request.setAttribute("propertiesList",Properties.getProperties(Properties.MapToTable(ITEM_TO_COUNT)));
            toForward="StateWiseAnalysis.jsp";
        }
        else if(request.getParameter("filter")!=null)
        {
            //Removing Other Parameters And Setting Request Attribute
            List<String> allProps=Properties.getProperties(Properties.MapToTable(ITEM_TO_COUNT));
            request.setAttribute("propsMapForQuery",getPropertiesFromParameters(request.getParameterMap(),allProps));
            //System.out.println(getPropertiesFromParameters(request.getParameterMap(),allProps).toString());
            toForward="StateWiseAnalysisServlet";
        }
        else if(request.getParameter("filterByGeometry")!=null)
        {
            //Removing Other Parameters And Setting Request Attribute
            List<String> allProps=Properties.getProperties(Properties.MapToTable(ITEM_TO_COUNT));
            request.setAttribute("propsMapForQuery",getPropertiesFromParameters(request.getParameterMap(),allProps));
            request.setAttribute("geometry",request.getParameterMap().get("geometry"));
            
            toForward="FilterByGeometryOnMapServlet";
        }
        else
        {
            request.setAttribute("ITEMS_TO_COUNT",ITEMS_TO_COUNT);
            request.setAttribute("stateMap",States.getStates());
        }
        RequestDispatcher rd=request.getRequestDispatcher(toForward); 
        rd.forward(request, response);

    }
    protected Map<String, String[]> getPropertiesFromParameters(Map<String, String[]> params,List<String> allProps)
    {
        Map<String, String[]> propsMap=new HashMap<>();
        params.entrySet().stream().filter((param) -> (allProps.contains(param.getKey()))).forEachOrdered((param) -> {
            propsMap.put(param.getKey(), param.getValue());
        });
        return propsMap;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
