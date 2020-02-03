<%-- 
    Document   : StateWiseAnalysis
    Created on : 16 Jan, 2020, 2:42:41 PM
    Author     : FOR ORACLE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

    <title>State Wise Analysis Form</title>
    <style>
        select{
            display: block;
        }
        td{
            width:50%;
        }
    </style>
    <script>
        function addProperty()
                {
                    var tableRef =document.getElementById('mytable').getElementsByTagName('tbody')[0];

                    var properties_dropdownlist=document.getElementById("properties_dropdownlist");
                    var newRow   = tableRef.insertRow(tableRef.rows.length-1);

                    // Insert a cell in the row at index 0
                    var newCell  = newRow.insertCell(0);
                    var newCell_1 =newRow.insertCell(1);
                    var newCell_2 =newRow.insertCell(2);

                    console.log("hello");
                    // Append a text node to the cell
                    var text=properties_dropdownlist.options[properties_dropdownlist.selectedIndex].value;
                    var newText  = document.createTextNode(text);
                    newCell.appendChild(newText);

                    var input = document.createElement("input");
                    input.type = "text";
                    input.name = text;
                    input.value= "Anything";
                    input.placeholder="Enter Value Of "+text;
                    newCell_1.appendChild(input);

                    newCell_2.innerHTML = '<button type="button" id="'+text+'" onclick="removeProperty(this)">Remove</button>';
                    newRow.id=text;
                }
                function removeProperty(r){
                    var i = r.parentNode.parentNode.rowIndex;
                    document.getElementById("mytable").deleteRow(i);
                }
    </script>
    </head>
    <body style="margin-left:32%;width:500px;margin-top: 10%;">
        <div class="w3-container w3-teal w3-card-4" style="text-align: center;">
            <h2>State Wise Analysis</h2>
        </div>
        <c:set var="b1" value="${STATE_OSM_ID}"></c:set>
        <c:set var="i1" value="${ITEM_TO_COUNT}"></c:set>
        <form action="LoadInitDataForStateWiseAnalysis" style="padding:10px;" method="get" class="w3-container w3-card-4 w3-light-grey">
            <table id="mytable">
                <tr>
                    <td >
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
                        <select name="itemToCount"  class="w3-select w3-border"  id="itemToCount" <c:if test="${not empty propertiesList}"></c:if>>
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
                                <option value="" disabled selected>SELECT PROPERTY</option>
                                <c:forEach items="${propertiesList}" var="prop">
                                        <option value="${prop}">${prop}</option>     
                                </c:forEach>
                            </select>
                        </td>
                        <td><button name="addProp" class="w3-btn w3-teal" style="width: 100%;" type="button"  onclick="addProperty()">ADD</button></td></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" class="w3-btn w3-teal" style="width: 100%;" name="filter" value="View Result">
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td>
                            <input type="submit" class="w3-btn w3-teal" style="width: 100%;" name="loadProperties" value="Load Properties">
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </table>
        </form>
    </body>
</html>


