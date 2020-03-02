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
    //int  []YEARS={2014,2015,2016,2017,2018,2019}; 
    Integer []YEARS=(Integer [])request.getAttribute("YEARS");
    int fromyear=Integer.parseInt(request.getAttribute("FROMYEAR").toString());
    String [] geometries=(String []) request.getAttribute("geometry");
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
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        <script src="https://npmcdn.com/leaflet-geometryutil"></script>
        <script type="text/javascript">
            var map;
            var chart ;
            var dps;
            var QueryResult=[];
           var custom_polygon;
            var layer=null;
            
            function applyColors()
            {
                //QueryResult[0]   -- > For 2014 OR FROMYEAR
                //dps[0] --> For FROMYEAR
                for(var i=0;i<QueryResult.length;i++)
                {
                    var c=document.getElementById('color_'+(<%=fromyear%>+i)).value;
                    QueryResult[i].setParams({env: 'color:'+c.substr(1,c.length)});
                    
                }
                for(var i=0;i<QueryResult.length;i++)
                {
                    var c=document.getElementById('color_'+(<%=fromyear%>+i)).value ;                
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
                        String []st=new String[YEARS.length];
                        String []lyr=new String[YEARS.length];
                        String []COLORS={"ff3300","ff9900","A693BD","000000","00cc33","0099cc"};
                            
                        if (request.getAttribute("ITEM_TO_COUNT").toString().equals("POINTS")){
                          String prefix="bisag:style_point";
                          String prefix_lyr="bisag:view";
                          String suffix_lyr="_points";
                          int i;
                          for(int year:YEARS)
                          {
                              st[year-fromyear]=prefix;
                              lyr[year-fromyear]=prefix_lyr+String.valueOf(year)+suffix_lyr;
                          }
                        }
                        else if (request.getAttribute("ITEM_TO_COUNT").toString().equals("LINES"))
                        {
                            String prefix="bisag:style_line";
                            String prefix_lyr="view";
                            String suffix_lyr="_lines";
                            int i;
                            Map<String,String[]> propsMapForQuery=(Map<String,String[]>) request.getAttribute("propsMapForQuery");
                            if(!propsMapForQuery.containsKey("highway"))
                            {
                                prefix="bisag:style_line1";
                            }
                          for(int year:YEARS)
                            {
                                st[year-fromyear]=prefix;
                                lyr[year-fromyear]=prefix_lyr+String.valueOf(year)+suffix_lyr;
                            }
                        }
                        else if (request.getAttribute("ITEM_TO_COUNT").toString().equals("POLYGONS"))
                        {
                            String prefix="bisag:style_polygon";
                            String prefix_lyr="view";
                            String suffix_lyr="_polygons";
                            int i;
                            for(int year:YEARS)
                            {
                                st[year-fromyear]=prefix;
                                lyr[year-fromyear]=prefix_lyr+String.valueOf(year)+suffix_lyr;
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
                                marker.on('click',function(e){
                                map.removeLayer(marker); 
                                });
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
                    
                    /* Draw Control START */
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
                    /* Draw Control END */

                    //console.log(drawnItems.getBounds());
                    
                   //Polygons On Which We Have Just Applied FilterByGeometry
                    var polygons_filtered=L.featureGroup();
                    //L.polygon(coords, {color: 'red'});
                    <c:forEach items="${geometry}" var="geo">
                        var geometry_filtered=JSON.parse('${geo}');
                        console.log(geometry_filtered.coordinates[0]);
                        var a=geometry_filtered.coordinates[0];
                        for(var i=0;i<a.length;i++)
                        {
                            var temp=a[i][0];
                            a[i][0]=a[i][1];
                            a[i][1]=temp;
                        }
                        var poly=L.polygon(a, {color: 'red'});
                        poly.bindPopup('You Have Just Applied Filter Here.');
                        poly.addTo(polygons_filtered);
                        
                        //map.fitBounds(poly.getBounds());
                    </c:forEach>
                    polygons_filtered.addTo(map);
                    console.log(Object.entries(polygons_filtered._layers).length);
                    if(Object.entries(polygons_filtered._layers).length !== 0)
                    {
                        map.fitBounds(polygons_filtered.getBounds());
                    }
                    map.addControl(drawControl);
                    var polygons_created=L.featureGroup();
                    var ind=0;
                    map.on('draw:deleted',function(e){
                        var layers = e.layers;
                        var layer_name="";
                        
                        layers.eachLayer(function(layer) {
                               /*How to get layer type here? */
                               //console.log(layer.name);
                               layer_name=layer.name;
                               polygons_created.removeLayer(layer);
                        });
                        if(Object.entries(polygons_created._layers).length !== 0)
                            map.fitBounds(polygons_created.getBounds());
                        //layer=null;
                        if(Object.entries(polygons_created._layers).length === 0){
                            //Disable Filter Button
                            var filterButton=document.getElementById("filterByGeometry");
                            filterButton.disabled=true;
                        }
                        //Delete Hidden Parameters
                        var form=document.getElementById("myform");
                        var latlngs=document.getElementById("latlngs-"+layer_name);
                        var geometry=document.getElementById("geometry-"+layer_name);
                        form.removeChild(latlngs);
                        form.removeChild(geometry);
                       
                    });
                    map.on('draw:created', function (e) {
                        /*if(layer !== null)
                        {
                            alert('You can draw only one Geometry For Filter');
                            drawnItems.removeLayer(layer);
                        } */   
                        var type = e.layerType;
                            layer = e.layer;
                            //console.log(layer.getBounds());
                            
                            //console.log(layer.toGeoJSON());
                            if(type === 'polygon' || type === 'rectangle')
                            {
                                // here you got the polygon points
                                //console.log(layer);
                                var points = layer._latlngs;
                                //console.log('Points'+points);
                                // here you can get it in geojson format
                                var geojson = layer.toGeoJSON();
                                //console.log(JSON.stringify(geojson));
                                var geoJsonLayer = L.geoJson(geojson);
                                //console.log("SOUTH WEST : "+geoJsonLayer.getBounds().getSouthWest());
                                //console.log("Bounding Box: " + geoJsonLayer.getBounds().toBBoxString());
                                
                                //Setting Popup Content
                                var latlngs = layer._defaultShape ? layer._defaultShape() : layer.getLatLngs(),
                                area = L.GeometryUtil.geodesicArea(latlngs);
                                var a=L.GeometryUtil.readableArea(area, true);
                                //console.log("Area : "+a.substr(0,a.length-2));
                                var area=a.substr(0,a.length-3);
                                var geom=JSON.stringify(geojson.geometry, null, 4);
                                var content=
                                `<h3>Place Details</h3><table>
                                <tr>
                                  <td>Name</td>
                                  <td>  <input type="text" id="name" name="name" placeholder="Name of the place">
                              </td>
                                </tr>
                                <tr>
                                  <td>Population</td>
                                  <td><input type="number" id="population" min="0" name="population" value="0"></td>
                                </tr>
                                <tr>
                                  <td>Famous place</td>
                                  <td><input type="text" id="famous_place" name="famousplace" placeholder="list of famous place"></td>
                                </tr>
                                <tr>
                                  <td>Area(ha)</td>
                                  <td><input type="number" id="area" name="area" step="any" value='`+area+"'"+
                                ` ></td>
                                </tr>
                                
                              </table>`+
                                `
                                    <h3>Polygon Details</h3>
                                    <table>
                                        <tr>
                                            <td>Geometry</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea rows="5" cols="36" name="polygon_geo" disabled>`+geom+`</textarea>
                                                <input type="hidden" name="polygon_geo" value='`+JSON.stringify(geojson.geometry)+`'
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <p>Created : `+new Date().toLocaleString()+`</p>
                                                <input type="hidden" name="created" value='`+new Date().toLocaleString()+`' >
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="submit" name="save_poly" value="SAVE"></td>
                                        </tr>
                                    </table>
                                    `;
                                layer.bindPopup(content);
                                
                                
                                //Enable The Filter Button On Map
                                var filterButton=document.getElementById("filterByGeometry");
                                filterButton.disabled=false;
                                layer.name="layer"+(++ind);
                                console.log(layer);
                                //Create Hidden Field For Geometry Object
                                var input = document.createElement("input");
                                input.setAttribute("id","geometry-"+layer.name);
                                input.setAttribute("type", "hidden");
                                input.setAttribute("name", "geometry");
                                input.setAttribute("value", JSON.stringify(geojson.geometry));
                                console.log(JSON.stringify(geojson.geometry));
                                var input1 = document.createElement("input");
                                input1.setAttribute("id","latlngs-"+layer.name);
                                input1.setAttribute("type", "hidden");
                                input1.setAttribute("name", "latlngs");
                                input1.setAttribute("value", JSON.stringify(layer._latlngs));
                                
                                 //console.log(JSON.stringify(layer._latlngs));
                                 //Append it to myform
                                 document.getElementById("myform").appendChild(input);
                                
                                 document.getElementById("myform").appendChild(input1);
                                 
                                //Add Layer To polygons_created FeatureGroup
                                layer.addTo(polygons_created);
                                map.fitBounds(polygons_created.getBounds());
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
                    
                    

                    
                    var basicMap=L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            maxZoom: 19,
                            attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>'
                    }).addTo(map);		
                    
                    var Esri_WorldImagery=L.tileLayer.provider('Esri.WorldImagery');
                    var overlayLayers={
                            
                        };
                    <%for(int year:YEARS){%>
                    QueryResult[<%=year%>-<%=fromyear%>]=L.tileLayer.wms("http://localhost:8080/geoserver/wms", {
                            layers: '<%=lyr[year-fromyear]%>',
                            transparent:true,
                            styles:'<%=st[year-fromyear]%>',
                            env: 'color:'+'<%=COLORS[year-fromyear]%>',
                            format:'image/png',
                            tiled:true
                    });
                    overlayLayers["QueryResult-"+<%=year%>]=QueryResult[<%=year%>-<%=fromyear%>];
                    <%}%>
                    QueryResult[0].addTo(map);
                    
                    var ajax = new XMLHttpRequest();
                    ajax.open("GET", "http://localhost:8080/geoserver/bisag/view2014_lines/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities", true);
                    ajax.onreadystatechange = function () {
                      if (ajax.readyState === 4 && ajax.status === 200)
                      {
                        var parser = new DOMParser();
                        var xmlDoc = parser.parseFromString(ajax.responseText,"text/xml");
                       // console.log(xmlDoc);
                        var ats=xmlDoc.getElementsByTagName("LatLonBoundingBox")[0].attributes;
                        var maxx=parseFloat(ats.maxx.value);
                        var maxy=parseFloat(ats.maxy.value);
                        var ats=xmlDoc.getElementsByTagName("BoundingBox")[0].attributes;
                        var minx=parseFloat(ats.minx.value);
                        var miny=parseFloat(ats.miny.value);
                        console.log('minx :'+minx);
                        console.log('miny : '+miny);
                        console.log('maxx : '+maxx);
                        console.log('maxy : '+maxy);
                        console.log((minx+maxx)/2);
                        console.log((miny+maxy)/2);
                        map.setView([(miny+maxy)/2,(minx+maxx)/2],7);
                        
                      }
                    };
                    ajax.send();
                    console.log(map.getBounds());
                    var google=L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {
                        attribution: 'google'
                    });
                    var control=L.control.layers({
                        "Basic":basicMap,
                        "Esri.WorldImagery":Esri_WorldImagery,
                        "google":google
                        },
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
                            //var LAYERS='bisag:myview_2014,bisag:myview_2015,bisag:myview_2016,bisag:myview_2017,bisag:myview_2018,bisag:myview_2019';
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
                        },300);
                        //map.openPopup(popup);
			
                    });
                    map.on('dblclick', function(event){
                        map.clicked = 0;
                        map.zoomIn();
                    });
                    	
                     <% } %> 
                };
                
                function drawPolygon() {
                    if(layer !== null)
                    {
                        alert('One Polygon Already Exists On The Map. Try Again After Removing That.');
                        return;
                    }
                    //var coords =  [[48,-3],[50,5],[44,11],[48,-3]] ;          
                   var coords = document.getElementById("coordPolygon").value;

                   var a = JSON.parse(coords);
                   //console.log(a);
                   //Converting corrdinates to LatLngs
                   for(var i=0;i<a.length;i++)
                   {
                       var temp=a[i][0];
                       a[i][0]=a[i][1];
                       a[i][1]=temp;
                   }
                   //console.log(a);
                   if(custom_polygon)
                   {
                       map.removeLayer(custom_polygon);
                   }
                   custom_polygon = L.polygon(a, {color: 'red'});
                   custom_polygon.addTo(map);
                   console.log(custom_polygon);
                   custom_polygon.on('click',function(e)
                    {
                         map.removeLayer(custom_polygon);
                         var filterButton=document.getElementById("filterByGeometry");
                         filterButton.disabled=true;
                         var form=document.getElementById("myform");
                         var geometry=document.getElementById("geometry");
                         form.removeChild(geometry);
                    });
                   var filterButton=document.getElementById("filterByGeometry");
                   filterButton.disabled=false;
                   var geom='{"type":"Polygon","coordinates":['+coords+']}';
                   console.log(geom);
                   //Create Hidden Field For Geometry Object
                   var input = document.createElement("input");
                   input.setAttribute("id","geometry");
                   input.setAttribute("type", "hidden");
                   input.setAttribute("name", "geometry");
                   input.setAttribute("value", geom);
                   document.getElementById("myform").appendChild(input);
                   map.fitBounds(custom_polygon.getBounds());
                }
                
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
            .grid-container {
                display: grid;
                grid-template-columns: auto 100px;
                grid-gap: 10px;
              }
        </style>
    </head>
    <body>
        <div style="padding: 20px;border:2px solid;">
            <form action="LoadInitDataForStateWiseAnalysis"  method="get" id="myform">
            <div>
                <table class="w3-table w3-border w3-responsive w3-card-4">
                    <tr>
                        <% for(int year:YEARS){%>
                            <td><input type="color" id="color_<%=year%>" value="#<%=COLORS[year-fromyear]%>"></td>
                            <td><%=year%></td>
                        <%}%>
                        
                    </tr>
                    
                </table>
                <br>
                <table>
                    <tr>
                        <td>
                            <input type="button" class="w3-btn" style='background-color: #668DAC;color:white;' id="applyCs" onclick="applyColors()" name="applyCs" value="Apply Colors">
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

            </div>
                <div style="float:left;width:40%;height:100%;" >
                <br>
                <input type="text" style="width:50%;" placeholder="Enter CQL Filter (Map Only)" class="w3-input w3-border w3-round w3-animate-input" id="cql_filter">
                <br>
                <input type="button" class="w3-btn" style="background-color: #668DAC;color:white;" id="applyCqlFilter" name="applyFilter" onclick="applyCQLFilter()" value="Apply Filter">
                
                    <h1>Chart</h1>
                    <div id="chartContainer"style="width:100%;height: 100%;" ></div>
                </div>
                <div style="width:60%;height: 70%;float:right;">
                    <br>    
                    <input type="text" style="width:50%;" placeholder="Enter Coordinates To Draw Polygon  e.g. [[48,-3],[50,5],[44,11],[48,-3]]" class="w3-input w3-border w3-round w3-animate-input"  name="coordPolygon" id="coordPolygon"/><br>
                    <div class="grid-container">
                        <div><input type="button" onclick="drawPolygon()" class="w3-btn" style="background-color: #668DAC;color:white;" value="Draw"/><br></div> 
                        <div><input type="submit" class="w3-btn" style="background-color: #668DAC;color:white;" id="filterByGeometry" name="filterByGeometry" value="Filter" disabled="true"></div>
                    </div>
                    
                    <!--HIDDEN DATA-->
                    <input type="hidden" name="state_osm_id" value="${STATE_OSM_ID}">
                    <input type="hidden" name="itemToCount" value="${ITEM_TO_COUNT}">
                    <input type="hidden" name="fromyear" value="${FROMYEAR}">
                    <input type="hidden" name="toyear" value="${TOYEAR}">
                    <!-- HIDDEN DATA ENDS-->
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