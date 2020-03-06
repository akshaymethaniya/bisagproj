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
import java.util.concurrent.ForkJoinPool;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author FOR ORACLE
 */
public class StateWiseAnalysisServlet extends HttpServlet {

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
        String STATE_OSM_ID=(String)request.getAttribute("STATE_OSM_ID");
        String ITEM_TO_COUNT=(String)request.getAttribute("ITEM_TO_COUNT");
        Map<String,String[]> parameters=(Map<String,String[]>)request.getAttribute("propsMapForQuery");
        Integer []YEARS=(Integer [])request.getAttribute("YEARS");
        
        List<Map<Object,Object>> list = new ArrayList<>();
        
        //FORK JOIN METHOD
       // int []YEARS={2014,2015,2016,2017,2018,2019};
        String []COLORS={"#ff3300","#ff9900","#A693BD","#000000","#00cc33","#0099cc"};

        System.out.println("POOL SIZE :"+String.valueOf(Runtime.getRuntime().availableProcessors() - 1));
        ForkJoinPool fjp = new ForkJoinPool();
        CalculateForkJoinTask task=new CalculateForkJoinTask(YEARS[0],YEARS.length,new CalculateCount(ITEM_TO_COUNT,2014, STATE_OSM_ID, parameters));
        fjp.invoke(task);
        for(int i=0;i<YEARS.length;i++)
        {
           System.out.println("YEAR : "+YEARS[i]+"\t Count :"+task.COUNTS[i]);
           Map<Object,Object> map = new HashMap<>();
           map.put("x", YEARS[i]);
           map.put("y", task.COUNTS[i]);
           map.put("color",COLORS[i]);
           //System.out.println(task.getX_bbox()[1]+"---"+task.getY_bbox()[1]);
           list.add(map);
        }
        
        /* BRUTE FORCE METHOD
        //Running Query For Different Years For Plotting Chart
        List<Map<Object,Object>> list = new ArrayList<>();
        try{
            int []YEARS={2014,2015,2016,2017,2018,2019};
            String []COLORS={"#ff3300","#ff9900","#A693BD","#000000","#00cc33","#0099cc"};
            for(int i=0;i<YEARS.length;i++)
            {
                Map<Object,Object> map = new HashMap<>();

                int count=new CalculateCount(ITEM_TO_COUNT,YEARS[i],STATE_OSM_ID,parameters).calculate();
                //out.println("YEAR : "+YEARS[i]);
                //out.println("\t"+ITEM_TO_COUNT+" : "+count);
                //out.println("<br>");
                map.put("x", YEARS[i]); 
                map.put("y", count); 
                map.put("color",COLORS[i]);
                list.add(map);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        */
        
        //Setting CountList To Request Attribute
        request.setAttribute("countList",list);
        
        //Setting BBox point 
        request.setAttribute("X_BBOX",task.getX_bbox()[1]);
        request.setAttribute("Y_BBOX",task.getY_bbox()[1]);
        //request.setAttribute("YEARS", YEARS);
        request.setAttribute("STATE_NAME",States.getStateName(STATE_OSM_ID));

        RequestDispatcher rd=request.getRequestDispatcher("StateWiseAnalysisResult.jsp"); 
        rd.forward(request, response);

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
