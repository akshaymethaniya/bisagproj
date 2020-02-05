<%-- 
    Document   : Chart
    Created on : 24 Dec, 2019, 3:28:38 PM
    Author     : FOR ORACLE
--%>

<%@page import="pkg1.CalculateCount"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*,java.sql.*" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<%@page import="pkg1.Properties"%>
<%
    String dataPoints = null;
    boolean displayChart=false;
    if(request.getAttribute("STATE_OSM_ID")!="")
    {
        displayChart=true;
        Gson gsonObj = new Gson();
        List<Map<Object,Object>> counts=(List<Map<Object,Object>>)request.getAttribute("countList");
        dataPoints = gsonObj.toJson(counts);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js" crossorigin=""></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.js" crossorigin=""></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/Leaflet.fullscreen.min.js'></script>
        <link href='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/leaflet.fullscreen.css' rel='stylesheet' />
        <script src="https://rawgit.com/k4r573n/leaflet-control-osm-geocoder/master/Control.OSMGeocoder.js"></script>
	<link rel="stylesheet" href="https://rawgit.com/k4r573n/leaflet-control-osm-geocoder/master/Control.OSMGeocoder.css" />
        <script src="./leaflet-provider.js"></script>
        <script type="text/javascript">
            var map;
            var chart ;
            var dps;
            var QueryResult=[];
            
            var ajax = new XMLHttpRequest();
            ajax.open("GET", "http://localhost:8080/geoserver/bisag/view2019_lines/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities", true);
            ajax.onreadystatechange = function () {
              if (ajax.readyState === 4 && ajax.status === 200)
              {
                var parser = new DOMParser();
                var xmlDoc = parser.parseFromString(ajax.responseText,"text/xml");
                var ats=xmlDoc.getElementsByTagName("LatLonBoundingBox")[0].attributes;
                var x=(parseFloat(ats.minx.value)+parseFloat(ats.maxx.value))/2;
                var y=(parseFloat(ats.miny.value)+parseFloat(ats.maxy.value))/2;
                console.log(y);
                console.log(x);
              }
            };
            ajax.send();
            function applyColors()
            {
                //QueryResult[0]   -- > For 2019
                //dps[0] --> For 2014
                for(var i=0;i<QueryResult.length;i++)
                {
                    var c=document.getElementById('color_'+(2019-i)).value;
                    QueryResult[i].setParams({env: 'color:'+c.substr(1,c.length)});
                    
                }
                for(var i=0;i<QueryResult.length;i++)
                {
                    var c=document.getElementById('color_'+(2014+i)).value ;                
                    dps[i].color=c;
                }
                console.log(dps);
                chart.render();
                
            }
            function applyCQLFilter()
            {
                var cql=document.getElementById('cql_filter');
                for(var i=0;i<QueryResult.length;i++)
                    QueryResult[i].setParams({cql_filter :cql.value});
            }
            window.onload = function() 
            { 
                <% 
                        String []st=new String[6];
                        String []lyr=new String[6];
                        String []COLORS={"ff3300","ff9900","A693BD","000000","00cc33","0099cc"};
                        if (request.getAttribute("ITEM_TO_COUNT").toString().equals("POINTS")){
                          String prefix="bisag:style_point_";
                          String prefix_lyr="bisag:view";
                          String suffix_lyr="_points";
                          int i;
                          for(i=2014;i<=2019;i++)
                          {
                              st[i-2014]=prefix+String.valueOf(i);
                              lyr[i-2014]=prefix_lyr+String.valueOf(i)+suffix_lyr;
                          }
                        }
                        else if (request.getAttribute("ITEM_TO_COUNT").toString().equals("LINES"))
                        {
                            String prefix="bisag:style_line";
                            String prefix_lyr="view";
                            String suffix_lyr="_lines";
                            int i;
                            for(i=2014;i<=2019;i++)
                            {
                                st[i-2014]=prefix;
                                lyr[i-2014]=prefix_lyr+String.valueOf(i)+suffix_lyr;
                            }
                        }
                        else if (request.getAttribute("ITEM_TO_COUNT").toString().equals("POLYGONS"))
                        {
                            String prefix="bisag:style_polygon_";
                            String prefix_lyr="view";
                            String suffix_lyr="_polygons";
                            int i;
                            for(i=2014;i<=2019;i++)
                            {
                                st[i-2014]=prefix+String.valueOf(i);
                                lyr[i-2014]=prefix_lyr+String.valueOf(i)+suffix_lyr;
                            }
                        }
                %>

                <% if(dataPoints != null && displayChart) { %>
                    dps=JSON.parse('<%out.print(dataPoints);%>');
                    console.log(dps);
                    chart = new CanvasJS.Chart("chartContainer", {
                            animationEnabled: true,
                            exportEnabled: true,
                            title: {
                                    text: "NO. OF RESULTS" 
                            },
                            axisX: {						
                                    title: "YEAR"
                            },
                            axisY: {						
                                    title: "COUNT"
                            },
                            data: [{
                                    type: "column",
                                    dataPoints: dps
                            }]
                    });
                    chart.render();

                      
                    // create map and set center and zoom level
                    map = new L.map('map',{
                            crs: L.CRS.EPSG3857,
                            fullscreenControl: true,
                            cursor: true
                    }).setView([22.74312,72],7);
                    
                    
                    
                    var marker=null;
                    var osmGeocoder = new L.Control.OSMGeocoder({
                        collapsed: false,
                        position: 'bottomright',
                        text: 'Find!',
                        placeholder: 'Enter Location',
                        callback: function (results) {
                            if(results && results.length)
                            {
                                console.log(results);
                                var bbox = results[0].boundingbox,
                                        first = new L.LatLng(bbox[0], bbox[2]),
                                        second = new L.LatLng(bbox[1], bbox[3]),
                                        bounds = new L.LatLngBounds([first, second]);
                                var display_name =results[0].display_name;
                                //console.log('Lat: '+first.lat);
                                //console.log('first : '+first+' second :'+second);
                                var lat_m=(first.lat+second.lat)/2;
                                var lng_m=(first.lng+second.lng)/2;
                                if(marker !== null)
                                {
                                    map.removeLayer(marker);
                                }
                                marker= L.marker([lat_m,lng_m],
                                            {title: display_name }
                                            )
                                           .addTo(map);
                                this._map.fitBounds(bounds);
                            }
                            else
                            {
                                alert('No Such Place');
                            }
                        }
                        
                    });
                    map.addControl(osmGeocoder);
        
                    var drawnItems = new L.FeatureGroup();
                    map.addLayer(drawnItems);
                    

                    var drawControl = new L.Control.Draw({
                    draw: {
				polygon: {
					shapeOptions: {
						color: 'purple'
					},
                                        allowIntersection: false,
					drawError: {
						color: 'orange',
						timeout: 1000
					},
					showArea: true,
					metric: false
				},
				polyline: {
					shapeOptions: {
						color: 'red'
					}
				},
				rect: {
					shapeOptions: {
						color: 'green'
					},
                                        showArea: true,
					metric: false
				},
                                circle:false
				/*circle: {
					shapeOptions: {
						color: 'steelblue'
					},
                                        showArea: true,
					metric: false
				}*/
			},
                        edit: {
                            featureGroup: drawnItems
                        }
                    });
                    //console.log(drawnItems.getBounds());
                    map.addControl(drawControl);
                    var layer=null;
                    map.on('draw:deleted',function(e){
                        layer=null;
                        var filterButton=document.getElementById("filterByGeometry");
                        filterButton.disabled=true;
                        var form=document.getElementById("myform");
                        var latlngs=document.getElementById("latlngs");
                        var geometry=document.getElementById("geometry");
                        form.removeChild(latlngs);
                        form.removeChild(geometry);

                    });
                    map.on('draw:created', function (e) {
                        if(layer !== null)
                        {
                            alert('You can draw only one Geometry For Filter');
                            drawnItems.removeLayer(layer);
                        }    
                        var type = e.layerType;
                            layer = e.layer;
                            console.log(layer);
                            console.log(layer.toGeoJSON());
                            if(type === 'polygon' || type === 'rectangle')
                            {
                                // here you got the polygon points
                                //console.log(layer);
                                var points = layer._latlngs;
                                console.log('Points'+points);
                                // here you can get it in geojson format
                                var geojson = layer.toGeoJSON();
                                console.log(JSON.stringify(geojson));
                                var geoJsonLayer = L.geoJson(geojson);
                                //console.log("SOUTH WEST : "+geoJsonLayer.getBounds().getSouthWest());
                                console.log("Bounding Box: " + geoJsonLayer.getBounds().toBBoxString());
                                
                                //Enable The Filter Button On Map
                                var filterButton=document.getElementById("filterByGeometry");
                                filterButton.disabled=false;
                                
                                //Create Hidden Field For Geometry Object
                                var input = document.createElement("input");
                                input.setAttribute("id","geometry");
                                input.setAttribute("type", "hidden");
                                input.setAttribute("name", "geometry");
                                input.setAttribute("value", JSON.stringify(geojson.geometry));
                                
                                var input1 = document.createElement("input");
                                input1.setAttribute("id","latlngs");
                                input1.setAttribute("type", "hidden");
                                input1.setAttribute("name", "latlngs");
                                input1.setAttribute("value", JSON.stringify(layer._latlngs));
                                //Append it to myform
                                document.getElementById("myform").appendChild(input);
                                
                                document.getElementById("myform").appendChild(input1);
                            }
                            else if (type === 'circle') {

                                var theCenterPt = layer.getLatLng();

                                var center = [theCenterPt.lng,theCenterPt.lat]; 
                                console.log(center);

                                var theRadius = layer.getRadius();
                                console.log(theRadius);
                            }
                           
                        drawnItems.addLayer(layer);
                    });
                    
                    /*if('${latlngs}')
                    {
                        var latlngs=JSON.parse('${latlngs}');
                        console.log(latlngs);
                        var polygon = L.polygon(latlngs, {color: 'red'}).addTo(map);
                        map.fitBounds(polygon.getBounds());
                    }*/

                    
                    var basicMap=L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            maxZoom: 19,
                            attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>'
                    }).addTo(map);		
                    
                    var Esri_WorldImagery=L.tileLayer.provider('Esri.WorldImagery');

                    
                    QueryResult[2019-2019]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[5]%>',
                            transparent:true,
                            styles:'<%=st[5]%>',
                            env: 'color:'+'<%=COLORS[5]%>',
                            format:'image/png'
                    });
                    QueryResult[2019-2018]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[4]%>',
                            transparent:true,
                            styles:'<%=st[4]%>',
                            env: 'color:'+'<%=COLORS[4]%>',
                            format:'image/png'
                    });
                    QueryResult[2019-2017]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[3]%>',
                            transparent:true,
                            styles:'<%=st[3]%>',
                            env: 'color:'+'<%=COLORS[3]%>',
                            format:'image/png'
                    });
                    QueryResult[2019-2016]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[2]%>',
                            transparent:true,
                            styles:'<%=st[2]%>',
                            env: 'color:'+'<%=COLORS[2]%>',
                            format:'image/png'
                    });
                    QueryResult[2019-2015]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[1]%>',
                            transparent:true,
                            styles:'<%=st[1]%>',
                            env: 'color:'+'<%=COLORS[1]%>',
                            format:'image/png'
                    });
                    QueryResult[2019-2014]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[0]%>',
                            transparent:true,
                            styles:'<%=st[0]%>',
                            env: 'color:'+'<%=COLORS[0]%>',
                            format:'image/png'
                            
                    }).addTo(map);
                    var overlayLayers={
                            "Query Result-2019":QueryResult[2019-2019],
                            "Query Result-2018":QueryResult[2019-2018],
                            "Query Result-2017":QueryResult[2019-2017],
                            "Query Result-2016":QueryResult[2019-2016],
                            "Query Result-2015":QueryResult[2019-2015],
                            "Query Result-2014":QueryResult[2019-2014]
                        };
                        
                    var control=L.control.layers({
                        "Basic":basicMap,"Esri.WorldImagery":Esri_WorldImagery},
                            overlayLayers
                        ).addTo(map);
                    L.control.scale().addTo(map);
                    
                    map.clicked = 0;                                                                      
                    map.on('click', function(e) {
                        //console.log(chart);
                        map.clicked = map.clicked + 1;
                        setTimeout(function(){
                            if(map.clicked === 1){
                                map.clicked=0;
                            var ls='';
                            var layers = [];
                            map.eachLayer(function(layer) {

                                if(layer instanceof L.TileLayer && layer._wmsVersion )
                                {
                                    layers.push(layer.options.layers);
                                    //console.log(layer);
                                    //layer.setParams({env: 'color:00FF00'});
                                }
                            });
                            if(layers.length === 0)
                                return;
                            //console.log(layers.toString());
                            console.log("Lat, Lon : " + e.latlng.lat + ", " + e.latlng.lng);
                            if(map.isFullscreen())
                            {        
                                alert('Closing The Full Screen Mode To View The Features List');
                            }
                            var popup= new L.Popup({maxWidth: 1000});
                            var latlngStr = '(' + e.latlng.lat + ', ' +         e.latlng.lng + ')';
                            var BBOX =map.getBounds()._southWest.lng+","+map.getBounds()._southWest.lat+","+map.getBounds()._northEast.lng+","+map.getBounds()._northEast.lat;
                            var WIDTH= map.getSize().x;
                            var HEIGHT = map.getSize().y;
                            var X = Math.round(map.layerPointToContainerPoint(e.layerPoint).x);
                            var Y = Math.round(map.layerPointToContainerPoint(e.layerPoint).y);
                            //console.log('X :'+X+" Y: "+Y);
                            //console.log(WIDTH);
                            //console.log(HEIGHT);
                            //console.log(BBOX);
                            var LAYERS='bisag:myview_2014,bisag:myview_2015,bisag:myview_2016,bisag:myview_2017,bisag:myview_2018,bisag:myview_2019';
                            var URL='http://localhost:8080/geoserver/bisag/wms?'+
                                    'SERVICE=WMS&'+
                                    'VERSION=1.1.1&'+
                                    'REQUEST=GetFeatureInfo&'+
                                    'FORMAT=image%2Fpng&'+
                                    'TRANSPARENT=true&'+
                                    'QUERY_LAYERS='+layers.toString()+'&'+
                                    'STYLES&'+
                                    'LAYERS='+layers.toString()+'&'+
                                    'exceptions=application%2Fvnd.ogc.se_inimage&'+
                                    'INFO_FORMAT=text%2Fhtml&'+
                                    'FEATURE_COUNT=100&'+
                                    'X='+X+'&'+
                                    'Y='+Y+'&'+
                                    'SRS=EPSG%3A4326&'+
                                    'WIDTH='+WIDTH+'&'+
                                    'HEIGHT='+HEIGHT+'&'+
                                    'BBOX='+BBOX;
                            /*document.getElementById('info').innerHTML ='<iframe seamless src="' + URL + '"></iframe>';
                            fetch(URL)
                            .then((response) => response.text())
                            .then((html) => {
                              document.getElementById('info').innerHTML = html;
                            });*/
                            //popup.setLatLng(e.latlng);
                            //popup.setContent("<iframe src='"+URL+"' width='500' height='500' frameborder='0'></iframe>");
                            document.getElementById('info').innerHTML = '<iframe seamless src="' + URL + '" style="width:500px;height:auto;"></iframe>';
                            fetch(URL)
                              .then((response) => response.text())
                              .then((html) => {
                                  var el = document.createElement( 'html' );
                                  el.innerHTML=html;
                                  
                                  var header= '<h2>PROPERTIES</h2>';
                                  //var filter='<input type="text" style="width:30%;" placeholder="Enter CQL Filter" class="w3-input w3-border w3-round w3-animate-input" id="cql_filter">';    
                                  //var apply_button='<input type="button" class="w3-btn w3-teal" id="applyCqlFilter" name="applyFilter" onclick="applyCQLFilter()" value="Apply Filter">';
                                  /*if(el.getElementsByTagName('table').length)
                                    document.getElementById('info').innerHTML = header+filter+'<br>'+apply_button+'<br><br>'+ html;
                                  else*/
                                  document.getElementById('info').innerHTML = header+html;

                              });
                            }
                        }, 300);
                        //map.openPopup(popup);
			
                    });
                    map.on('dblclick', function(event){
                        map.clicked = 0;
                        map.zoomIn();
                    });
                    	
                     <% } %> 
                };
        </script>
        <style>
            html, body {
                        height: 100%;
                        margin: 10px;
                }
            #map {       
                 height: 400px;
            }
            input[type="color"] {
                padding:0px;
                border:0px;
            }
        </style>
    </head>
    <body >
        <div style="padding: 20px;border:2px solid;">
            <form action="LoadInitDataForStateWiseAnalysis" method="get" id="myform">
            <div>
                <table class="w3-table w3-border w3-responsive w3-card-4">
                    <tr>
                        <td><input type="color" id="color_2014" value="#ff3300"></td>
                        <td>2014</td>
                        <td><input type="color" id="color_2015" value="#ff9900"></td>                        
                        <td>2015</td>
                        <td><input type="color" id="color_2016"value="#A693BD"></td>                        
                        <td>2016</td>
                        <td><input type="color" id="color_2017" value="#000000"></td>                        
                        <td>2017</td>
                        <td><input type="color" id="color_2018" value="#00cc33"></td>                       
                        <td>2018</td>
                        <td><input type="color" id="color_2019" value="#0099cc"></td>
                        <td>2019</td>
                    </tr>
                    
                </table>
                <br>
                <table>
                    <tr>
                        <td>
                            <input type="button" class="w3-btn w3-teal" id="applyCs" onclick="applyColors()" name="applyCs" value="Apply Colors">
                        </td>
                        <td>
                            <input type="submit" class="w3-btn w3-teal" id="filterByGeometry" name="filterByGeometry" value="Filter" disabled="true">
                        </td>
                    </tr>
                </table>
                <div>
                    <h2 style="text-transform: uppercase;">Analysis Of <b>${STATE_NAME}</b></h2>
                    <h3 style="text-transform: uppercase;"><b>Properties</b> Applied</h3>
                    
                    <table class="w3-table w3-border w3-responsive w3-card-4">
                        <c:forEach items="${propsMapForQuery}" var="prop">
                            <tr>
                                <td style="text-transform: uppercase;"><b>${prop.getKey()}</b></td>
                                <td>:</td>
                                <td style="text-transform: uppercase;">${prop.getValue()[0]}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                    <br>
                    <input type="text" style="width:30%;" placeholder="Enter CQL Filter (Map Only)" class="w3-input w3-border w3-round w3-animate-input" id="cql_filter">
                    <br>
                    <input type="button" class="w3-btn w3-teal" id="applyCqlFilter" name="applyFilter" onclick="applyCQLFilter()" value="Apply Filter">

                
            </div>
            <div style="float:left;width:40%;height:100%;">
                    <h1>Chart</h1>
                    <div id="chartContainer"style="width:100%;height: 100%;" ></div>
                </div>
                <div style="width:60%;height: 70%;float:right;">
                        <input type="hidden" name="state_osm_id" value="${STATE_OSM_ID}">
                        <input type="hidden" name="itemToCount" value="${ITEM_TO_COUNT}">
                        <c:forEach items="${propsMapForQuery}" var="prop">
                            <input type="hidden" name="${prop.key}" value="${prop.value[0]}">
                        </c:forEach>
                        
                        <div>
                            <h1>Map</h1>
                            <div id="map">
                            </div>
                        </div>

                </div>
                <div id="info" style="width:100%;height:auto;overflow-x: scroll;">

                </div>
            
            </form>
        </div>
        <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    </body>
</html>