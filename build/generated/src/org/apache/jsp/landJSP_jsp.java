package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import pkg1.Land;
import java.util.List;

public final class landJSP_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_c_forEach_var_items;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_c_forEach_var_items = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_c_forEach_var_items.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("​\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <title>Lands</title>\n");
      out.write("        <meta charset=\"UTF-8\">\n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n");
      out.write("        <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.css\">\n");
      out.write("        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"  crossorigin=\"\"></script>\n");
      out.write("        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.js\" crossorigin=\"\"></script>\n");
      out.write("        <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n");
      out.write("        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.js\"></script>\n");
      out.write("        <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.css\"/>\n");
      out.write("        <script src='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/Leaflet.fullscreen.min.js'></script>\n");
      out.write("        <link href='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/leaflet.fullscreen.css' rel='stylesheet' />\n");
      out.write("        \n");
      out.write("        <style>\n");
      out.write("\t\t\thtml, body {\n");
      out.write("\t\t\t\theight: 100%;\n");
      out.write("\t\t\t\tmargin: 0;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t#map {\n");
      out.write("\t\t\t\twidth: 100%;\n");
      out.write("\t\t\t\theight: 90%;\n");
      out.write("\t\t\t}\n");
      out.write("                        .w3-myfont {\n");
      out.write("                            font-family: \"Comic Sans MS\", cursive, sans-serif;\n");
      out.write("                        }\n");
      out.write("        </style>\n");
      out.write("        <script type=\"text/javascript\">\n");
      out.write("\t\tvar map;\n");
      out.write("                var landsData;\n");
      out.write("                function fitBounds(id){\n");
      out.write("                    landsData.eachLayer(function(layer) {\n");
      out.write("                        if(layer.name === id)\n");
      out.write("                        {\n");
      out.write("                            map.fitBounds(layer.getBounds());\n");
      out.write("                        }\n");
      out.write("                    });\n");
      out.write("                }\n");
      out.write("\t\tfunction init() {\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t// create map and set center and zoom level\n");
      out.write("\t\t\tmap = new L.map('map',{\n");
      out.write("\t\t\t\tcrs: L.CRS.EPSG3857,\n");
      out.write("\t\t\t\tcursor: true,\n");
      out.write("                                fullscreenControl: true\n");
      out.write("                                \n");
      out.write("\t\t\t}).setView([22.74312, 72],7);\n");
      out.write("\t\t\t\n");
      out.write("                        \n");
      out.write("\t\t\t\n");
      out.write("                       landsData=L.featureGroup();\n");
      out.write("                        ");
      if (_jspx_meth_c_forEach_0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("                        if(Object.entries(landsData._layers).length !== 0)\n");
      out.write("                            map.fitBounds(landsData.getBounds());\n");
      out.write("                        landsData.addTo(map);\n");
      out.write("\t\t\t\n");
      out.write("                       var drawnItems = new L.FeatureGroup();\n");
      out.write("                       map.addLayer(drawnItems);\n");
      out.write("                       var drawControl = new L.Control.Draw({\n");
      out.write("                            draw: {\n");
      out.write("                                polygon: {\n");
      out.write("                                    shapeOptions: {\n");
      out.write("                                    color: 'purple'\n");
      out.write("                            },\n");
      out.write("                            allowIntersection: false,\n");
      out.write("                        drawError: {\n");
      out.write("                            color: 'orange',\n");
      out.write("                            timeout: 1000\n");
      out.write("                        },\n");
      out.write("                        showArea: true,\n");
      out.write("                        metric: false\n");
      out.write("                        },\n");
      out.write("                        \n");
      out.write("                        polyline:false,\n");
      out.write("                    rect: {\n");
      out.write("                        shapeOptions: {\n");
      out.write("                            color: 'green'\n");
      out.write("                        },\n");
      out.write("                        showArea: true,\n");
      out.write("                        metric: false\n");
      out.write("                    },\n");
      out.write("                    circle: false\n");
      out.write("                    \n");
      out.write("                                               /*circle: {\n");
      out.write("                                                shapeOptions: {\n");
      out.write("                                                color: 'steelblue'\n");
      out.write("                                                },\n");
      out.write("                                                showArea: true,\n");
      out.write("                                                metric: false\n");
      out.write("                                                }*/\n");
      out.write("                    },\n");
      out.write("                    edit: {\n");
      out.write("                           featureGroup: drawnItems\n");
      out.write("                           }\n");
      out.write("                    });\n");
      out.write("\t\tmap.addControl(drawControl);\n");
      out.write("                \n");
      out.write("                var basicMap=L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n");
      out.write("                    maxZoom: 19,\n");
      out.write("                    attribution: '&copy; <a href=\"https://openstreetmap.org/copyright\">OpenStreetMap contributors</a>'\n");
      out.write("                }).addTo(map);\t\t\n");
      out.write("\n");
      out.write("                var google=L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {\n");
      out.write("                    attribution: 'google'\n");
      out.write("                });\n");
      out.write("                \n");
      out.write("                var overlayLayers={\n");
      out.write("                    \"landsData\":landsData\n");
      out.write("                };\n");
      out.write("                \n");
      out.write("                L.control.layers({\n");
      out.write("                    \"Basic\":basicMap,\n");
      out.write("                    \"google\":google\n");
      out.write("                    },\n");
      out.write("                    overlayLayers\n");
      out.write("                ).addTo(map);\n");
      out.write("\n");
      out.write("                map.on('draw:created', function (e) {\n");
      out.write("                        /*if(layer !== null)\n");
      out.write("                        {\n");
      out.write("                            alert('You can draw only one Geometry For Filter');\n");
      out.write("                            drawnItems.removeLayer(layer);\n");
      out.write("                        } */\n");
      out.write("                        var type = e.layerType;\n");
      out.write("                            layer = e.layer;\n");
      out.write("                            //console.log(layer.getBounds());\n");
      out.write("                            //console.log(layer.toGeoJSON());\n");
      out.write("                            if(type === 'polygon' || type === 'rectangle')\n");
      out.write("                            {\n");
      out.write("                                // here you got the polygon points\n");
      out.write("                                //console.log(layer);\n");
      out.write("                                var points = layer._latlngs;\n");
      out.write("                                //console.log('Points'+points);\n");
      out.write("                                // here you can get it in geojson format\n");
      out.write("                                var geojson = layer.toGeoJSON();\n");
      out.write("                                //console.log(JSON.stringify(geojson));\n");
      out.write("                                var geoJsonLayer = L.geoJson(geojson);\n");
      out.write("                                //console.log(\"SOUTH WEST : \"+geoJsonLayer.getBounds().getSouthWest());\n");
      out.write("                                //console.log(\"Bounding Box: \" + geoJsonLayer.getBounds().toBBoxString());\n");
      out.write("                                //Setting Popup Content\n");
      out.write("                                var latlngs = layer._defaultShape ? layer._defaultShape() : layer.getLatLngs(),\n");
      out.write("                                area = L.GeometryUtil.geodesicArea(latlngs);\n");
      out.write("                                var a=L.GeometryUtil.readableArea(area, true);\n");
      out.write("                                //console.log(\"Area : \"+a.substr(0,a.length-2));\n");
      out.write("                                var area=a.substr(0,a.length-3);\n");
      out.write("                                var geom=JSON.stringify(geojson.geometry, null, 4);\n");
      out.write("                                var content=\n");
      out.write("                                `\n");
      out.write("                                <form action=\"LandFormHandleServlet\" method=\"post\">\n");
      out.write("                                <h3>Place Details</h3><table>\n");
      out.write("                                <tr>\n");
      out.write("                                  <td>Name</td>\n");
      out.write("                                  <td>  <input type=\"text\" id=\"name\" name=\"name\" placeholder=\"Name of the place\">\n");
      out.write("                              </td>\n");
      out.write("                                </tr>\n");
      out.write("                                <tr>\n");
      out.write("                                  <td>Population</td>\n");
      out.write("                                  <td><input type=\"number\" id=\"population\" min=\"0\" name=\"population\" value=\"0\"></td>\n");
      out.write("                                </tr>\n");
      out.write("                                <tr>\n");
      out.write("                                  <td>Famous place</td>\n");
      out.write("                                  <td><input type=\"text\" id=\"famous_place\" name=\"famousplace\" placeholder=\"list of famous place\"></td>\n");
      out.write("                                </tr>\n");
      out.write("                                <tr>\n");
      out.write("                                  <td>Area(ha)</td>\n");
      out.write("                                  <td><input type=\"number\" id=\"area\" name=\"area\" step=\"any\" value='`+area+\"'\"+\n");
      out.write("                                ` ></td>\n");
      out.write("                                </tr>\n");
      out.write("                              </table>`+\n");
      out.write("                                `\n");
      out.write("                                    <h3>Polygon Details</h3>\n");
      out.write("                                    <table>\n");
      out.write("                                        <tr>\n");
      out.write("                                            <td>Geometry</td>\n");
      out.write("                                        </tr>\n");
      out.write("                                        <tr>\n");
      out.write("                                            <td>\n");
      out.write("                                                <textarea rows=\"5\" cols=\"36\" name=\"polygon_geo\" disabled>`+geom+`</textarea>\n");
      out.write("                                                <input type=\"hidden\" name=\"polygon_geo\" value='`+JSON.stringify(geojson.geometry)+`'\n");
      out.write("                                            </td>\n");
      out.write("                                        </tr>\n");
      out.write("                                        <tr>\n");
      out.write("                                            <td>\n");
      out.write("                                                <p>Created : `+new Date().toLocaleString()+`</p>\n");
      out.write("                                                <input type=\"hidden\" name=\"created\" value='`+new Date().toLocaleString()+`' >\n");
      out.write("                                            </td>\n");
      out.write("                                        </tr>\n");
      out.write("                                        <tr>\n");
      out.write("                                            <td><input type=\"submit\" name=\"save_poly\" value=\"SAVE\"></td>\n");
      out.write("                                        </tr>\n");
      out.write("                                    </table>\n");
      out.write("                                    </form>    \n");
      out.write("                                    `;\n");
      out.write("                                layer.bindPopup(content);\n");
      out.write("                            } \n");
      out.write("              \n");
      out.write("                             drawnItems.addLayer(layer);\n");
      out.write("                    });\n");
      out.write("\t\t}\n");
      out.write("\t</script>\n");
      out.write("        <script>\n");
      out.write("            // Script to open and close sidebar\n");
      out.write("            function w3_open() {\n");
      out.write("              document.getElementById(\"mySidebar\").style.display = \"block\";\n");
      out.write("              document.getElementById(\"myOverlay\").style.display = \"block\";\n");
      out.write("            }\n");
      out.write("\n");
      out.write("            function w3_close() {\n");
      out.write("              document.getElementById(\"mySidebar\").style.display = \"none\";\n");
      out.write("              document.getElementById(\"myOverlay\").style.display = \"none\";\n");
      out.write("            }\n");
      out.write("\n");
      out.write("           \n");
      out.write("            \n");
      out.write("        </script>\n");
      out.write("    </head>\n");
      out.write("    <body onload=\"init()\">\n");
      out.write("        <!-- Sidebar/menu -->\n");
      out.write("        <nav class=\"w3-sidebar w3-red w3-collapse w3-top w3-large w3-padding\" style=\"z-index:3;width:300px;font-weight:bold;\" id=\"mySidebar\"><br>\n");
      out.write("          <a href=\"javascript:void(0)\" onclick=\"w3_close()\" class=\"w3-button w3-hide-large w3-display-topleft\" style=\"width:100%;font-size:22px\">Close Menu</a>\n");
      out.write("          <div class=\"w3-container\">\n");
      out.write("              <h3 class=\"w3-padding-64\" class=\"w3-myfont\" style=\"text-transform:uppercase;\"><b>Growth<br>Analyzer</b></h3>\n");
      out.write("          </div>\n");
      out.write("          <div class=\"w3-bar-block\">\n");
      out.write("            <a href=\"LoadInitDataForStateWiseAnalysis\" onclick=\"w3_close()\" class=\"w3-bar-item w3-button w3-hover-white\" style=\"text-transform:uppercase;\">State Wise Analysis</a> \n");
      out.write("            <a href=\"LandFormHandleServlet\" onclick=\"w3_close()\" class=\"w3-bar-item w3-button w3-white\" style=\"text-transform:uppercase;\">Lands</a> \n");
      out.write("          </div>\n");
      out.write("        </nav>\n");
      out.write("\n");
      out.write("        <!-- Top menu on small screens -->\n");
      out.write("        <header class=\"w3-container w3-top w3-hide-large w3-red w3-xlarge w3-padding\">\n");
      out.write("          <a href=\"javascript:void(0)\" class=\"w3-button w3-red w3-margin-right\" onclick=\"w3_open()\">☰</a>\n");
      out.write("          <span class=\"w3-myfont\">Growth Analyzer</span>\n");
      out.write("        </header>\n");
      out.write("\n");
      out.write("        <!-- Overlay effect when opening sidebar on small screens -->\n");
      out.write("        <div class=\"w3-overlay w3-hide-large\" onclick=\"w3_close()\" style=\"cursor:pointer\" title=\"close side menu\" id=\"myOverlay\"></div>\n");
      out.write("\n");
      out.write("        <!-- !PAGE CONTENT! -->\n");
      out.write("        <div class=\"w3-main\" style=\"margin-left:340px;margin-right:40px;margin-bottom: 40px;\">\n");
      out.write("            <div class=\"w3-container\" style=\"margin-top:50px\">\n");
      out.write("                <h1 class=\"w3-xxlarge w3-text-red\" style=\"text-transform:uppercase;\"><b>Manage Lands</b></h1>\n");
      out.write("                <hr style=\"width:50px;border:5px solid red\" class=\"w3-round\">\n");
      out.write("                <div id=\"map\" class=\"w3-card-4 w3-border\" style=\"width:100%;height: 500px;z-index:0;\"></div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_forEach_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_forEach_0 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _jspx_tagPool_c_forEach_var_items.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_forEach_0.setPageContext(_jspx_page_context);
    _jspx_th_c_forEach_0.setParent(null);
    _jspx_th_c_forEach_0.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${lands}", java.lang.Object.class, (PageContext)_jspx_page_context, null));
    _jspx_th_c_forEach_0.setVar("landObj");
    int[] _jspx_push_body_count_c_forEach_0 = new int[] { 0 };
    try {
      int _jspx_eval_c_forEach_0 = _jspx_th_c_forEach_0.doStartTag();
      if (_jspx_eval_c_forEach_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\n");
          out.write("                                console.log('");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getName()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("');\n");
          out.write("                                var geom=JSON.parse('");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getPolygon_geo()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("');\n");
          out.write("                                console.log(geom.coordinates[0]);\n");
          out.write("                                var a=geom.coordinates[0];\n");
          out.write("                                for(var i=0;i<a.length;i++)\n");
          out.write("                                {\n");
          out.write("                                    var temp=a[i][0];\n");
          out.write("                                    a[i][0]=a[i][1];\n");
          out.write("                                    a[i][1]=temp;\n");
          out.write("                                }\n");
          out.write("                                var poly=L.polygon(a, {color: 'green'});\n");
          out.write("                                poly[\"name\"]='");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getID()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("';\n");
          out.write("                                var geom=JSON.stringify(JSON.parse('");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getPolygon_geo()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("'), null, 4);\n");
          out.write("                                var content=\n");
          out.write("                                `\n");
          out.write("                                <form action=\"LandFormHandleServlet\" method=\"post\">\n");
          out.write("                                <h3>Place Details</h3><table>\n");
          out.write("                                <tr>\n");
          out.write("                                  <td>Name</td>\n");
          out.write("                                  <td>  <input type=\"text\" id=\"name\" name=\"name\" placeholder=\"Name of the place\" value='");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getName()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("'>\n");
          out.write("                              </td>\n");
          out.write("                                </tr>\n");
          out.write("                                <tr>\n");
          out.write("                                  <td>Population</td>\n");
          out.write("                                  <td><input type=\"number\" id=\"population\" min=\"0\" name=\"population\" value=\"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getPopulation()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("\"></td>\n");
          out.write("                                </tr>\n");
          out.write("                                <tr>\n");
          out.write("                                  <td>Famous place</td>\n");
          out.write("                                  <td><input type=\"text\" id=\"famous_place\" name=\"famousplace\" placeholder=\"list of famous place\" value=\"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getFamous_place()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("\"></td>\n");
          out.write("                                </tr>\n");
          out.write("                                <tr>\n");
          out.write("                                  <td>Area(ha)</td>\n");
          out.write("                                  <td><input type=\"number\" id=\"area\" name=\"area\" step=\"any\" value='");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getArea()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("'></td>\n");
          out.write("                                </tr>\n");
          out.write("                              </table>`+\n");
          out.write("                                `\n");
          out.write("                                    <h3>Polygon Details</h3>\n");
          out.write("                                    <table>\n");
          out.write("                                        <tr>\n");
          out.write("                                            <td>Geometry</td>\n");
          out.write("                                        </tr>\n");
          out.write("                                        <tr>\n");
          out.write("                                            <td>\n");
          out.write("                                                <textarea rows=\"5\" cols=\"36\" name=\"polygon_geo\" disabled>`+geom+`</textarea>\n");
          out.write("                                                <input type=\"hidden\" name=\"polygon_geo\" value='`+JSON.stringify(JSON.parse('");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getPolygon_geo()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("'))+`'\n");
          out.write("                                        </td>\n");
          out.write("                                        </tr>\n");
          out.write("                                        <tr>\n");
          out.write("                                            <td>\n");
          out.write("                                                <p>Created : ");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getCreated()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("</p>\n");
          out.write("                                                <input type=\"hidden\" name=\"created\" value='");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getCreated()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("' >\n");
          out.write("​\n");
          out.write("                                            </td>\n");
          out.write("                                        </tr>\n");
          out.write("                                        <tr>\n");
          out.write("                                            <td>\n");
          out.write("                                                <input type=\"submit\" name=\"update_poly\" value=\"UPDATE\">\n");
          out.write("                                                <input type=\"submit\" name=\"remove_poly\" value=\"REMOVE\">\n");
          out.write("                                                <input type=\"button\" name=\"fit_bounds\" value=\"FIT BOUNDS\" onclick=\"fitBounds('");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getID()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("')\">\n");
          out.write("                                            </td>\n");
          out.write("                                        </tr>\n");
          out.write("                                    </table>\n");
          out.write("                                    <input type=\"hidden\" name=\"polygon_id\" value=\"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${landObj.getID()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("\">\n");
          out.write("                                    </form>    \n");
          out.write("                                    `;\n");
          out.write("                                \n");
          out.write("                                poly.bindPopup(content);\n");
          out.write("                                poly.addTo(landsData);\n");
          out.write("                        ");
          int evalDoAfterBody = _jspx_th_c_forEach_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_forEach_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_forEach_0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_forEach_0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_forEach_0.doFinally();
      _jspx_tagPool_c_forEach_var_items.reuse(_jspx_th_c_forEach_0);
    }
    return false;
  }
}
