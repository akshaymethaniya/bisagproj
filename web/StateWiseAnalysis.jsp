<%-- 
    Document   : StateWiseAnalysis
    Created on : 16 Jan, 2020, 2:42:41 PM
    Author     : FOR ORACLE
--%>

<%@page import="pkg1.Properties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>State Wise Analysis Form</title>
        <style>
            /*body{
                background-image: url('./background.png');
                background-repeat: no-repeat;
                background-size:cover;
                    

            }*/
            .tooltip {
            position: relative;
            border-bottom: 1px dotted black;
          }

          .tooltip .tooltiptext {
            visibility: hidden;
            width: 300px;
            background-color: #668DAC;
            color: white;
            text-align: center;
            border-radius: 6px;
            padding: 5px;
            position: absolute;
            z-index: 1;
            top: -5px;
            left: 110%;
          }

          .tooltip .tooltiptext::after {
            content: "";
            position: absolute;
            top: 50%;
            right: 100%;
            margin-top: -5px;
            border-width: 5px;
            border-style: solid;
            border-color: transparent black transparent transparent;
          }
          .tooltip:hover .tooltiptext {
            visibility: visible;
          }
            i:hover{
                    color:grey;
            }
            i{
                color:#668DAC;
            }
            select{
                display: block;
            }
           
            .autocomplete {
                position: relative;
                display: inline-block;
              }
              .autocomplete-items {
                position: absolute;
                border: 1px solid #d4d4d4;
                border-bottom: none;
                border-top: none;
                z-index: 99;
                /*position the autocomplete items to be the same width as the container:*/
                top: 100%;
                left: 0;
                right: 0;
              }

              .autocomplete-items div {
                padding: 10px;
                cursor: pointer;
                background-color: #fff; 
                border-bottom: 1px solid #d4d4d4; 
              }

              /*when hovering an item:*/
              .autocomplete-items div:hover {
                background-color: #e9e9e9; 
              }

              /*when navigating through the items using the arrow keys:*/
              .autocomplete-active {
                background-color: DodgerBlue !important; 
                color: #ffffff; 
              }
        </style>
        <script type="text/javascript" src="./props_vals.js"></script>
        <script>
            console.log(data);
            function autocomplete(inp, arr) {
            /*the autocomplete function takes two arguments,
            the text field element and an array of possible autocompleted values:*/
            var currentFocus;
            var name=inp.name;
            console.log(name);
            /*execute a function when someone writes in the text field:*/
            inp.addEventListener("input", function(e) {

                var a, b, i, val = this.value;
                /*close any already open lists of autocompleted values*/
                closeAllLists();
                if (!val) { return false;}
                currentFocus = -1;
                /*create a DIV element that will contain the items (values):*/
                a = document.createElement("DIV");
                a.setAttribute("id", this.id + "autocomplete-list");
                a.setAttribute("class", "autocomplete-items");
                /*append the DIV element as a child of the autocomplete container:*/
                this.parentNode.appendChild(a);
                /*for each item in the array...*/
                for (i = 0; i < arr.length; i++) {
                  /*check if the item starts with the same letters as the text field value:*/
                  if (arr[i].substr(0, val.length).toUpperCase() === val.toUpperCase()) {
                    /*create a DIV element for each matching element:*/

                    //finding its equivalent description from data
                    var hovertext='';
                    var text=arr[i];
                    console.log(data[name]);
                    if(data[name])
                    {
                        console.log("in if");
                        for(var i=0;i<data[name].length;i++)
                        {

                            if(data[name][i].propertyValue === text)
                            {   console.log("in if2");
                                hovertext=data[name][i].description;
                                console.log(hovertext);
                                break;
                            }
                        }
                    }
                    console.log(arr[i]);
                    b = document.createElement("DIV");
                    b.className="tooltip";
                    /*make the matching letters bold:*/
                    b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                    b.innerHTML += '<span class="tooltiptext">'+hovertext+'</span>';
                    b.innerHTML += arr[i].substr(val.length);
                    /*insert a input field that will hold the current array item's value:*/
                    b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                    /*execute a function when someone clicks on the item value (DIV element):*/
                    b.addEventListener("click", function(e) {
                        /*insert the value for the autocomplete text field:*/
                        inp.value = this.getElementsByTagName("input")[0].value;
                        /*close the list of autocompleted values,
                        (or any other open lists of autocompleted values:*/
                        closeAllLists();
                    });
                    a.appendChild(b);
                  }
                }
            });
            /*execute a function presses a key on the keyboard:*/
            inp.addEventListener("keydown", function(e) {
                var x = document.getElementById(this.id + "autocomplete-list");
                if (x) x = x.getElementsByTagName("div");
                if (e.keyCode === 40) {
                  /*If the arrow DOWN key is pressed,
                  increase the currentFocus variable:*/
                  currentFocus++;
                  /*and and make the current item more visible:*/
                  addActive(x);
                } else if (e.keyCode === 38) { //up
                  /*If the arrow UP key is pressed,
                  decrease the currentFocus variable:*/
                  currentFocus--;
                  /*and and make the current item more visible:*/
                  addActive(x);
                } else if (e.keyCode === 13) {
                  /*If the ENTER key is pressed, prevent the form from being submitted,*/
                  e.preventDefault();
                  if (currentFocus > -1) {
                    /*and simulate a click on the "active" item:*/
                    if (x) x[currentFocus].click();
                  }
                }
            });
            function addActive(x) {
              /*a function to classify an item as "active":*/
              if (!x) return false;
              /*start by removing the "active" class on all items:*/
              removeActive(x);
              if (currentFocus >= x.length) currentFocus = 0;
              if (currentFocus < 0) currentFocus = (x.length - 1);
              /*add class "autocomplete-active":*/
              x[currentFocus].classList.add("autocomplete-active");
            }
            function removeActive(x) {
              /*a function to remove the "active" class from all autocomplete items:*/
              for (var i = 0; i < x.length; i++) {
                x[i].classList.remove("autocomplete-active");
              }
            }
            function closeAllLists(elmnt) {
              /*close all autocomplete lists in the document,
              except the one passed as an argument:*/
              var x = document.getElementsByClassName("autocomplete-items");
              for (var i = 0; i < x.length; i++) {
                if (elmnt !== x[i] && elmnt !== inp) {
                  x[i].parentNode.removeChild(x[i]);
                }
              }
            }
            /*execute a function when someone clicks in the document:*/
            document.addEventListener("click", function (e) {
                closeAllLists(e.target);
            });
          }
            function addProperty()
            {            
                var properties_dropdownlist=document.getElementById("properties_dropdownlist");
                var text=properties_dropdownlist.options[properties_dropdownlist.selectedIndex].value;
                if(!text)
                {
                    return;
                }
                var tableRef =document.getElementById('mytable').getElementsByTagName('tbody')[0];

                var newRow   = tableRef.insertRow(tableRef.rows.length-1);

                // Insert a cell in the row at index 0
                var newCell  = newRow.insertCell(0);
                var newCell_1 =newRow.insertCell(1);
                var newCell_2 =newRow.insertCell(2);

                // Append a text node to the cell
                //console.log(text);
                var newText  = document.createTextNode(text);
                newCell.appendChild(newText);

                //Create AutoComplete DIV
                var d =document.createElement("div");
                d.className="autocomplete";

                //Creating Input Field
                var input = document.createElement("input");
                input.type = "text";
                input.name = text;
                input.value= "*";
                input.setAttribute("class","w3-input w3-border w3-light-grey");
                input.placeholder="Enter Value Of "+text;
                var possibleValues=[];
                //console.log(data[text]);
                if(data[text])
                {
                    for(var i=0;i<data[text].length;i++)
                    {
                        possibleValues[i]=data[text][i].propertyValue;
                        //console.log(data[text][i].propertyValue);
                    }
                    //console.log(possibleValues);
                    autocomplete(input,possibleValues);
                }
                //Append input to div
                d.appendChild(input);

                //append div to table cell
                newCell_1.appendChild(d);
                newCell_2.innerHTML = '<i  id="'+text+'" class="fa fa-trash w3-xlarge" onclick="removeProperty(this)"></i>';
                newRow.id=text;
            }
            function removeProperty(r){
                var i = r.parentNode.parentNode.rowIndex;
                document.getElementById("mytable").deleteRow(i);
            }
            function getDescOfProp(prop){
                return props[prop];
            }
            function validate(){
                    var fromyear=document.getElementById("fromyear").value;
                    var toyear=document.getElementById("toyear").value;
                    var error_mess=document.getElementById("error_mess");

                    console.log('called');
                    console.log('fromyear'+fromyear);
                    
                    if(Number(toyear) < Number(fromyear))
                    {
                        //alert('To Year Should Be Greater Than From Year');
                        error_mess.innerHTML="<i class='fa fa-warning' style='font-size:15px;color:red'></i>&nbsp;'FROM YEAR' SHOULD BE LESS THAN OR EQUAL TO 'TO YEAR'";
                    }
                    else
                    {
                        error_mess.innerHTML="";
                    }
            }
            
            window.onload = function() {
                <% if(request.getParameter("loadProperties")!=null){%>
                var x=document.getElementById("properties_dropdownlist").options;
                for(var i=0;i<x.length;i++){
                    if(props[x[i].text])
                        x[i].title=props[x[i].text];
                }
                <%}%>
            };
            
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
    <body>

         <!-- Sidebar/menu -->
        <nav class="w3-sidebar w3-red w3-collapse w3-top w3-large w3-padding" style="z-index:3;width:300px;font-weight:bold;" id="mySidebar"><br>
          <a href="javascript:void(0)" onclick="w3_close()" class="w3-button w3-hide-large w3-display-topleft" style="width:100%;font-size:22px">Close Menu</a>
          <div class="w3-container">
              <h3 class="w3-padding-64" style="text-transform:uppercase;"><b>Growth<br>Analyzer</b></h3>
          </div>
          <div class="w3-bar-block">
            <a href="LoadInitDataForStateWiseAnalysis" onclick="w3_close()" class="w3-bar-item w3-button w3-white" style="text-transform:uppercase;">State Wise Analysis</a> 
            <a href="LandFormHandleServlet" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-white" style="text-transform:uppercase;">Lands</a> 
          </div>
        </nav>

        <!-- Top menu on small screens -->
        <header class="w3-container w3-top w3-hide-large w3-red w3-xlarge w3-padding">
          <a href="javascript:void(0)" class="w3-button w3-red w3-margin-right" onclick="w3_open()">☰</a>
          <span>Growth Analyzer</span>
        </header>

        <!-- Overlay effect when opening sidebar on small screens -->
        <div class="w3-overlay w3-hide-large" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>
        
        <div class="w3-main" style="margin-left:340px;margin-right:40px">
            <div class="w3-container" style="margin-top:75px">
                <h1 class="w3-xxlarge w3-text-red" style="text-transform:uppercase;"><b>State Wise Analysis</b></h1>
                <hr style="width:50px;border:5px solid red" class="w3-round">
            </div>
            <c:set var="b1" value="${STATE_OSM_ID}"></c:set>
            <c:set var="i1" value="${ITEM_TO_COUNT}"></c:set>
            <div class="w3-container w3-card-4 w3-light-grey ">
                <form action="LoadInitDataForStateWiseAnalysis" style="padding:10px;" method="get" >
                    <table id="mytable" >
                        <tr>
                            <td> 
                                <div class="w3-center w3-text-red"><h3>FROM YEAR</h3></div>
                            </td>
                            <td>
                                <div class="w3-center w3-text-red"><h3>TO YEAR</h3></div>
                            </td>
                        </tr>
                        <tr>            
                            <c:choose>
                                <c:when test="${not empty propertiesList}">
                                    <td>
                                        <input type="number" id="fromyear" class="w3-input w3-border w3-light-grey"  min="2014" max="2019" value="${FROMYEAR}" name="fromyear" disabled="disabled">
                                    </td>
                                    <td>
                                        <input type="number" id="toyear" class="w3-input w3-border w3-light-grey"  min="2014" max="2019" value="${TOYEAR}" name="toyear" disabled="disabled">
                                    </td>
                                    <input type="hidden" name="fromyear" value="${FROMYEAR}">
                                    <input type="hidden" name="toyear" value="${TOYEAR}">
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <input type="number" id="fromyear" class="w3-input w3-border w3-light-grey" onchange="validate()" min="2014" max="2019" value="${FROMYEAR}" name="fromyear">
                                    </td>
                                    <td>
                                        <input type="number" id="toyear" class="w3-input w3-border w3-light-grey" onchange="validate()" min="2014" max="2019" value="${TOYEAR}" name="toyear">
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <td>
                                <select class="w3-select w3-border" name="state_osm_id">
                                    <option value="" disabled selected>Choose State</option>
                                        <c:forEach items="${stateMap}" var="state">
                                            <c:set var="b2" value="${state.key}"></c:set>
                                             <c:choose>
                                                <c:when test="${b1 == b2}">
                                                    <option value="${state.key}" selected>${state.value}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option  value="${state.key}">${state.value}</option>    
                                                </c:otherwise>    
                                            </c:choose>
                                        </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select name="itemToCount"  class="w3-select w3-border"  id="itemToCount">
                                    <option value="" disabled selected>Choose Table</option>
                                    <c:forEach items="${ITEMS_TO_COUNT}" var="item">
                                        <c:set var="i2" value="${item}"></c:set>
                                            <c:choose>
                                            <c:when test="${i1 == i2}">
                                                <option value="${item}" selected>${item}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option  value="${item}">${item}</option>    
                                            </c:otherwise>    
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    <c:choose>
                        <c:when test="${not empty propertiesList}">
                            <tr>
                                <td>
                                    <select name="properties" class="w3-select w3-border" id="properties_dropdownlist" style="width:available;" onsubmit="return false;">
                                        <option  value="" disabled selected>SELECT PROPERTY</option>
                                        <c:forEach items="${propertiesList}" var="prop">
                                            <option value="${prop}">${prop}</option>     
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><button name="addProp" class="w3-btn" style="width: 100%;background-color: #668DAC;color:white;" type="button"  onclick="addProperty()">ADD</button></td></td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" class="w3-btn " style="width: 100%;background-color: #668DAC;color:white;" name="filter" value="View Result">
                                </td>
                            </tr>

                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td>
                                    <input type="submit" class="w3-btn" style="width: 100%;background-color: #668DAC;color:white;" name="loadProperties" value="Load Properties">
                                </td>
                            </tr>                 
                        </c:otherwise>
                    </c:choose>
                    </table>
                    <p class="w3-text-red" id="error_mess">
                        <c:if test="${ not empty ERROR_MESS}">
                            <i class="fa fa-warning" style="font-size:15px;color:red"></i>&nbsp;${ERROR_MESS}
                        </c:if>
                    </p>
                </form>
            </div>
        </div>
        
    </body>
</html>


