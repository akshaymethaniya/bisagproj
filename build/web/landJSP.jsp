<%-- 
    Document   : landJSP
    Created on : 3 Mar, 2020, 11:22:43 AM
    Author     : FOR ORACLE
--%><%@page import="pkg1.Land"%>
<%@page import="java.util.List"%>


​
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Lands</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"  crossorigin=""></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.3/leaflet.js" crossorigin=""></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.css"/>
        <script src='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/Leaflet.fullscreen.min.js'></script>
        <link href='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/leaflet.fullscreen.css' rel='stylesheet' />

        <style>
            html, body {
                height: 100%;
                margin: 0;
            }
            #map {
                width: 100%;
                height: 90%;
            }
            .w3-myfont {
                font-family: "Comic Sans MS", cursive, sans-serif;
            }
        </style>
        <script type="text/javascript">
            var map;
            var landsData;
            function fitBounds(id) {
                landsData.eachLayer(function (layer) {
                    if (layer.name === id)
                    {
                        map.fitBounds(layer.getBounds());
                    }
                });
            }
            function init() {

                // create map and set center and zoom level
                map = new L.map('map', {
                    crs: L.CRS.EPSG3857,
                    cursor: true,
                    fullscreenControl: true

                }).setView([22.74312, 72], 7);



                landsData = L.featureGroup();
            <c:forEach items="${lands}" var="landObj">
                console.log('${landObj.getName()}');
                var geom = JSON.parse('${landObj.getPolygon_geo()}');
                console.log(geom.coordinates[0]);
                var a = geom.coordinates[0];
                for (var i = 0; i < a.length; i++)
                {
                    var temp = a[i][0];
                    a[i][0] = a[i][1];
                    a[i][1] = temp;
                }
                var poly = L.polygon(a, {color: 'green'});
                poly["name"] = '${landObj.getID()}';
                var geom = JSON.stringify(JSON.parse('${landObj.getPolygon_geo()}'), null, 4);
                var content =
                        `
                                <form action="LandFormHandleServlet" method="post">
                                <h3 class="w3-text-red">Place Details</h3><table>
                                <tr>
                                  <td>Name</td>
                                  <td>  <input type="text" id="name" name="name" placeholder="Name of the place" value='${landObj.getName()}'>
                              </td>
                                </tr>
                                <tr>
                                  <td>Population</td>
                                  <td><input type="number" id="population" min="0" name="population" value="${landObj.getPopulation()}"></td>
                                </tr>
                                <tr>
                                  <td>Famous place</td>
                                  <td><input type="text" id="famous_place" name="famousplace" placeholder="list of famous place" value="${landObj.getFamous_place()}"></td>
                                </tr>
                                <tr>
                                  <td>Area(ha)</td>
                                  <td><input type="number" id="area" name="area" step="any" value='${landObj.getArea()}'></td>
                                </tr>
                              </table>` +
                        `
                                    <h3 class="w3-text-red">Polygon Details</h3>
                                    <table>
                                        <tr>
                                            <td>Geometry</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea rows="5" cols="36" name="polygon_geo" disabled>` + geom + `</textarea>
                                                <input type="hidden" name="polygon_geo" value='` + JSON.stringify(JSON.parse('${landObj.getPolygon_geo()}')) + `'
                                        </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <p>Created : ${landObj.getCreated()}</p>
                                                <input type="hidden" name="created" value='${landObj.getCreated()}' >
​
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="button" class="w3-btn w3-red" name="fit_bounds" value="FIT BOUNDS" onclick="fitBounds('${landObj.getID()}')">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="submit" class="w3-btn w3-red" name="update_poly" value="UPDATE">
                                                <input type="submit" class="w3-btn w3-red" name="remove_poly" value="REMOVE">
                                            </td>
                                        </tr>
                                    </table>
                                    <input type="hidden" name="polygon_id" value="${landObj.getID()}">
                                    </form>    
                                    `;

                poly.bindPopup(content);
                poly.addTo(landsData);
            </c:forEach>
                if (Object.entries(landsData._layers).length !== 0)
                    map.fitBounds(landsData.getBounds());
                landsData.addTo(map);

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

                        polyline: false,
                        rect: {
                            shapeOptions: {
                                color: 'green'
                            },
                            showArea: true,
                            metric: false
                        },
                        circle: false

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
                map.addControl(drawControl);

                var basicMap = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>'
                }).addTo(map);

                var google = L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {
                    attribution: 'google'
                });

                var overlayLayers = {
                    "landsData": landsData
                };

                L.control.layers({
                    "Basic": basicMap,
                    "google": google
                },
                        overlayLayers
                        ).addTo(map);

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
                    if (type === 'polygon' || type === 'rectangle')
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
                        var a = L.GeometryUtil.readableArea(area, true);
                        //console.log("Area : "+a.substr(0,a.length-2));
                        var area = a.substr(0, a.length - 3);
                        var geom = JSON.stringify(geojson.geometry, null, 4);
                        var content =
                                `
                                <form action="LandFormHandleServlet" method="post">
                                <h3>Place Details</h3><table>
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
                                  <td><input type="number" id="area" name="area" step="any" value='` + area + "'" +
                                ` ></td>
                                </tr>
                              </table>` +
                                `
                                    <h3>Polygon Details</h3>
                                    <table>
                                        <tr>
                                            <td>Geometry</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea rows="5" cols="36" name="polygon_geo" disabled>` + geom + `</textarea>
                                                <input type="hidden" name="polygon_geo" value='` + JSON.stringify(geojson.geometry) + `'
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <p>Created : ` + new Date().toLocaleString() + `</p>
                                                <input type="hidden" name="created" value='` + new Date().toLocaleString() + `' >
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="submit" name="save_poly" value="SAVE"></td>
                                        </tr>
                                    </table>
                                    </form>    
                                    `;
                        layer.bindPopup(content);
                    }

                    drawnItems.addLayer(layer);
                });
            }
        </script>
        <script>
            // Script to open and close sidebar
            function w3_open() {
                document.getElementById("mySidebar").style.display = "block";
                document.getElementById("myOverlay").style.display = "block";
            }

            function w3_close() {
                document.getElementById("mySidebar").style.display = "none";
                document.getElementById("myOverlay").style.display = "none";
            }



        </script>
    </head>
    <body onload="init()">
        <!-- Sidebar/menu -->
        <nav class="w3-sidebar w3-red w3-collapse w3-top w3-large w3-padding" style="z-index:3;width:300px;font-weight:bold;" id="mySidebar"><br>
            <a href="javascript:void(0)" onclick="w3_close()" class="w3-button w3-hide-large w3-display-topleft" style="width:100%;font-size:22px">Close Menu</a>
            <div class="w3-container">
                <h3 class="w3-padding-64" class="w3-myfont" style="text-transform:uppercase;"><b>Growth<br>Analyzer</b></h3>
            </div>
            <div class="w3-bar-block">
                <a href="LoadInitDataForStateWiseAnalysis" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-white" style="text-transform:uppercase;">State Wise Analysis</a> 
                <a href="LandFormHandleServlet" onclick="w3_close()" class="w3-bar-item w3-button w3-white" style="text-transform:uppercase;">Lands</a> 
            </div>
        </nav>

        <!-- Top menu on small screens -->
        <header class="w3-container w3-top w3-hide-large w3-red w3-xlarge w3-padding">
            <a href="javascript:void(0)" class="w3-button w3-red w3-margin-right" onclick="w3_open()">☰</a>
            <span class="w3-myfont">Growth Analyzer</span>
        </header>

        <!-- Overlay effect when opening sidebar on small screens -->
        <div class="w3-overlay w3-hide-large" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

        <!-- !PAGE CONTENT! -->
        <div class="w3-main" style="margin-left:340px;margin-right:40px;margin-bottom: 40px;">
            <div class="w3-container" style="margin-top:50px">
                <h1 class="w3-xxlarge w3-text-red" style="text-transform:uppercase;"><b>Manage Lands</b></h1>
                <hr style="width:50px;border:5px solid red" class="w3-round">
                <div id="map" class="w3-card-4 w3-border" style="width:100%;height: 500px;z-index:0;"></div>
            </div>
        </div>
    </body>
</html>
