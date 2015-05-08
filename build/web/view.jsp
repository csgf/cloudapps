<%
/**************************************************************************
Copyright (c) 2011-2015:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy
    
See http://www.infn.it and and http://www.consorzio-cometa.it for details 
on the copyright holders.
    
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    
http://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
    
@author <a href="mailto:giuseppe.larocca@ct.infn.it">Giuseppe La Rocca</a>
**************************************************************************/
%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.model.Company" %>
<%@ page import="javax.portlet.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<portlet:defineObjects/>

<%
  Company company = PortalUtil.getCompany(request);
  String gateway = company.getName();
%>

<jsp:useBean id="GPS_table" class="java.lang.String" scope="request"/>
<jsp:useBean id="GPS_queue" class="java.lang.String" scope="request"/>

<jsp:useBean id="bes_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="bes_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="garuda_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="chain_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="chain_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="fedcloud_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="fedcloud_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="eumed_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="gisela_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="sagrid_cloudapps_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_cloudapps_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="cloudapps_ENABLEINFRASTRUCTURE" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="cloudapps_APPID" class="java.lang.String" scope="request"/>
<jsp:useBean id="cloudapps_OUTPUT_PATH" class="java.lang.String" scope="request"/>
<jsp:useBean id="cloudapps_SOFTWARE" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_HOSTNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_USERNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_PASSWORD" class="java.lang.String" scope="request"/>
<jsp:useBean id="SMTP_HOST" class="java.lang.String" scope="request"/>
<jsp:useBean id="SENDER_MAIL" class="java.lang.String" scope="request"/>

<script type="text/javascript">
    
    var latlng2markers = [];
    var icons = [];
    
    icons["plus"] = new google.maps.MarkerImage(
          '<%= renderRequest.getContextPath()%>/images/plus_new.png',
          new google.maps.Size(16,16),
          new google.maps.Point(0,0),
          new google.maps.Point(8,8));
    
    icons["minus"] = new google.maps.MarkerImage(
          '<%= renderRequest.getContextPath()%>/images/minus_new.png',
          new google.maps.Size(16,16),
          new google.maps.Point(0,0),
          new google.maps.Point(8,8));
    
    icons["ce"] = new google.maps.MarkerImage(            
            '<%= renderRequest.getContextPath()%>/images/CloudBlue-1.png',
            new google.maps.Size(16,16),
            new google.maps.Point(0,0),
            new google.maps.Point(8,8));            
    
    function hideMarkers(markersMap,map) 
    {
            for( var k in markersMap) 
            {
                if (markersMap[k].markers.length >1) {
                    var n = markersMap[k].markers.length;
                    var centerMark = new google.maps.Marker({
                        title: "Coordinates:" + markersMap[k].markers[0].getPosition().toString(),
                        position: markersMap[k].markers[0].getPosition(),
                        icon: icons["plus"]
                    });
                    for ( var i=0 ; i<n ; i++ ) 
                        markersMap[k].markers[i].setVisible(false);
                    
                    centerMark.setMap(map);
                    google.maps.event.addListener(centerMark, 'click', function() {
                        var markersMap = latlng2markers;
                        var k = this.getPosition().toString();
                        var visibility = markersMap[k].markers[0].getVisible();
                        if (visibility == false ) {
                            splitMarkersOnCircle(markersMap[k].markers,
                            markersMap[k].connectors,
                            this.getPosition(),
                            map
                        );
                            this.setIcon(icons["minus"]);
                        }
                        else {
                            var n = markersMap[k].markers.length;
                            for ( var i=0 ; i<n ; i++ ) {
                                markersMap[k].markers[i].setVisible(false);
                                markersMap[k].connectors[i].setMap(null);
                            }
                            markersMap[k].connectors = [];
                            this.setIcon(icons["plus"]);
                        }
                    });
                }
            }
    }
    
    function splitMarkersOnCircle(markers, connectors, center, map) 
    {
            var z = map.getZoom();
            var r = 60.0 / (z*Math.exp(z/2));            
            var n = markers.length;
            var dtheta = 2.0 * Math.PI / n;            
            var theta = 0;
            
            for ( var i=0; i<n; i++ ) 
            {
                var X = center.lat() + r*Math.cos(theta);
                var Y = center.lng() + r*Math.sin(theta);
                //console.log(dtheta); console.log(X); console.log(Y);
                markers[i].setPosition(new google.maps.LatLng(X-0.5,Y));
                markers[i].setVisible(true);
                theta += dtheta;
                
                var line = new google.maps.Polyline({
                    path: [center,new google.maps.LatLng(X,Y)],
                    clickable: false,
                    strokeColor: "#0000ff",
                    strokeOpacity: 1,
                    strokeWeight: 2
                });
                
                line.setMap(map);
                connectors.push(line);
            }
    }
    
    function updateAverage(name) {
        
        $.getJSON('<portlet:resourceURL><portlet:param name="action" value="get-ratings"/></portlet:resourceURL>&cloudapps_CR='+name,
        function(data) {                                               
            console.log(data.avg);
            $("#fake-stars-on").width(Math.round(parseFloat(data.avg)*20)); // 20 = 100(px)/5(stars)
            $("#fake-stars-cap").text(new Number(data.avg).toFixed(2) + " (" + data.cnt + ")");
        });                
    }
    
    // Create the Google Map JavaScript APIs V3
    function initialize(lat, lng, zoom) {
        console.log(lat);
        console.log(lng);
        console.log(zoom);
        
        var myOptions = {
            zoom: zoom,
            center: new google.maps.LatLng(lat, lng),
            mapTypeId: google.maps.MapTypeId.TERRAIN
            //mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        
        var map = new google.maps.Map(document.getElementById('map_canvas'), myOptions);  
        var image = new google.maps.MarkerImage('<%= renderRequest.getContextPath() %>/images/CloudBlue-1.png');
        
        var strVar="";
        strVar += "<table>";
        strVar += "<tr>";
        strVar += "<td>";
        strVar += "Vote the resource availability";
        strVar += "<\/td>";
        strVar += "<\/tr>";
        strVar += "<tr><td>\&nbsp;\&nbsp;";
        strVar += "<\/td><\/tr>";
        
        strVar += "<tr>";
        strVar += "<td>";
        strVar += "Rating: <span id=\"stars-cap\"><\/span>";
        strVar += "<div id=\"stars-wrapper2\">";
        strVar += "<select name=\"selrate\">";
        strVar += "<option value=\"1\">Very poor<\/option>";
        strVar += "<option value=\"2\">Not that bad<\/option>";
        strVar += "<option value=\"3\" selected=\"selected\">Average<\/option>";
        strVar += "<option value=\"4\">Good<\/option>";
        strVar += "<option value=\"5\">Perfect<\/option>";
        strVar += "<\/select>";
        strVar += "<\/div>";
        strVar += "<\/td>";
        strVar += "<\/tr>";

        strVar += "<tr>";        
        strVar += "<td>";
        strVar += "Average: <span id=\"fake-stars-cap\"><\/span>";
        strVar += "";
        strVar += "<div id=\"fake-stars-off\" class=\"stars-off\" style=\"width:100px\">";
        strVar += "<div id=\"fake-stars-on\" class=\"stars-on\"><\/div>";
        strVar += "";
        strVar += "<\/div>";
        strVar += "<\/td>";
        strVar += "<\/tr>";
        strVar += "<\/table>";
    
        var data = <%= GPS_table %>;
        var queues = <%= GPS_queue %>;
        
        var infowindow = new google.maps.InfoWindow();
        google.maps.event.addListener(infowindow, 'closeclick', function() {
            this.setContent('');
        });
        
        var infowindowOpts = { 
            maxWidth: 200
        };
               
       infowindow.setOptions(infowindowOpts);       
       
       var markers = [];
       for (var key in data){
                        
            var LatLong = new google.maps.LatLng(parseFloat(data[key]["LAT"]), 
                                                 parseFloat(data[key]["LNG"]));                    
            
            // Identify locations on the map
            var marker = new google.maps.Marker ({
                animation: google.maps.Animation.DROP,
                position: LatLong,
                icon: image,
                title : key
            });    
  
            // Add the market to the map
            marker.setMap(map);
            
            var latlngKey=marker.position.toString();
            if ( latlng2markers[latlngKey] == null )
                latlng2markers[latlngKey] = {markers:[], connectors:[]};
            
            latlng2markers[latlngKey].markers.push(marker);
            markers.push(marker);
        
            google.maps.event.addListener(marker, 'click', function() {
             
            var cr_hostname = this.title;
            
            google.maps.event.addListenerOnce(infowindow, 'domready', function() {
                                        
                    $("#stars-wrapper2").stars({
                        cancelShow: false, 
                        oneVoteOnly: true,
                        inputType: "select",
                        callback: function(ui, type, value)
                        { 
                            $.getJSON('<portlet:resourceURL><portlet:param name="action" value="set-ratings"/></portlet:resourceURL>' +
                                '&cloudapps_CR=' + cr_hostname + 
                                '&vote=' + value);
                            
                            updateAverage(cr_hostname);                      
                        }
                    });
                    
                    updateAverage(cr_hostname);               
                });              
                                                
                infowindow.setContent('<h3>' + cr_hostname + '</h3><br/>' + strVar);
                infowindow.open(map, this);
                                           
                var CE_queue = (queues[cr_hostname]["QUEUE"]);
                $('#cloudapps_CR').val(CE_queue);
                
                marker.setMap(map);
            }); // function                             
        } // for
        
        hideMarkers(latlng2markers, map);
        var markerCluster = new MarkerClusterer(map, markers, {maxZoom: 3, gridSize: 20});
    }
    
</script>

<script type="text/javascript">  
    var EnabledInfrastructure = null;           
    var infrastructures = new Array();  
    var i = 0;    
    
    <c:forEach items="<%= cloudapps_ENABLEINFRASTRUCTURE %>" var="current">
    <c:choose>
    <c:when test="${current!=null}">
        infrastructures[i] = '<c:out value='${current}'/>';       
        i++;  
    </c:when>
    </c:choose>
    </c:forEach>
        
    for (var i = 0; i < infrastructures.length; i++) {
       console.log("Reading array = " + infrastructures[i]);
       if (infrastructures[i]=="bes") EnabledInfrastructure='bes';
       if (infrastructures[i]=="garuda") EnabledInfrastructure='garuda';
       if (infrastructures[i]=="chain") EnabledInfrastructure='chain';
       if (infrastructures[i]=="fedcloud") EnabledInfrastructure='fedcloud';
       if (infrastructures[i]=="eumed")  EnabledInfrastructure='eumed';
       if (infrastructures[i]=="gisela") EnabledInfrastructure='gisela';
       if (infrastructures[i]=="sagrid") EnabledInfrastructure='sagrid';
    }
    
    var NMAX = infrastructures.length;
    //console.log (NMAX);
    
    $(document).ready(function() 
    {           
        // Draw the sortable Tab
        //$( "#tabs" ).tabs({ event: "mouseover" });
        $(function() {
            var tabs = $( "#tabs" ).tabs();
            tabs.find( ".ui-tabs-nav" ).sortable({
                axis: "x",                
                stop: function() {
                    tabs.tabs( "refresh" );
                }
            });
        });
        
        // Add the default message in the Advanced Settings
        $('#cloudapps_textarea_OCTAVE').html("Paste here your macro file or use the default demo");
        $('#cloudapps_textarea_R').html("Paste here your macro file or use the default demo");
        
        // Toggling the hidden div for the first time.        
        if ($('#divToToggle').css('display') == 'block')
            $('#divToToggle').toggle();
        
        var lat; var lng; var zoom;
        var found=0;
        
        if (parseInt(NMAX)>1) { 
            console.log ("More than one cloud infrastructure have been configured!");
            $("#error_infrastructure img:last-child").remove();
            $('#error_infrastructure').append("<img width='70' src='<%= renderRequest.getContextPath()%>/images/world.png' border='0'> More than one cloud infrastructure have been configured!");
            //lat=19;lng=14;zoom=3; 
            //lat=25;lng=20;zoom=3;
            lat=47;lng=12;zoom=3;
            found=1;
        } else if (EnabledInfrastructure=='bes') {
            console.log ("Start up: enabled the BES Cloud Infrastructure!");
            $('#bes_cloudapps_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=30;lng=86;zoom=3;
            found=1;
        } else if (EnabledInfrastructure=='garuda') {
            console.log ("Start up: enabling garuda!");
            $('#garuda_astra_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=29.15;lng=77.41;zoom=4;
            found=1;
        } else if (EnabledInfrastructure=='chain') {        
            console.log ("Start up: enabled the CHAIN-REDS Cloud testbed!");
            $('#chain_cloudapps_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=42;lng=12;zoom=4;
            found=1;            
        } else if (EnabledInfrastructure=='eumed') {       
            console.log ("Start up: enabled the Mediterranen Cloud Infrastructure!");
            $('#eumed_cloudapps_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=34;lng=20;zoom=4;
            found=1;
        } else if (EnabledInfrastructure=='fedcloud') {
            console.log ("Start up: enabled the Indian Cloud Infrastructure!");
            $('#fedcloud_cloudapps_ENABLEINFRASTRUCTURE').attr('checked','checked');            
            lat=47;lng=13;zoom=4;
            found=1;    
        } else if (EnabledInfrastructure=='gisela') {        
            console.log ("Start up: enabled the Latin America Cloud Infrastructure!");
            $('#gisela_cloudapps_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=2;lng=-36;zoom=2;
            found=1;            
        } else if (EnabledInfrastructure=='sagrid') {
            console.log ("Start up: enabled the SAgrid Cloud Infrastructure!");
            $('#sagrid_astra_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=-16;lng=-24;zoom=2;
            found=1;
        }                 
                
        if (found==0) { 
            console.log ("None of the cloud infrastructures have been configured!");
            $("#error_infrastructure img:last-child").remove();
            $('#error_infrastructure').append("<img width='35' src='<%= renderRequest.getContextPath()%>/images/Warning.png' border='0'> None of the available cloud infrastructures have been configured!");
        }                
        
        var accOpts = {
            change: function(e, ui) {                       
                $("<div style='width:650px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;'>").addClass("notify ui-corner-all").text(ui.newHeader.find("a").text() +
                    " was activated... ").appendTo("#error_message").fadeOut(2500, function(){ $(this).remove(); });                
                // Get the active option                
                var active = $("#accordion").accordion("option", "active");
                if (active==1) initialize(lat, lng, zoom);
            },
            autoHeight: false
        };
        
        // Create the accordions
        //$("#accordion").accordion({ autoHeight: false });
        $("#accordion").accordion(accOpts);
                
        // Check file input size with jQuery (Max. 2.5MB)
        $('input[type=file][name=\'cloudapps_file_OCTAVE\']').bind('change', function() {
            if (this.files[0].size/1000 > 25600) {     
                // Remove the img and text (if any)
                $("#error_message img:last-child").remove();
                $("#error_message").empty();
                $('#error_message').append("<img width='35' src='<%= renderRequest.getContextPath()%>/images/Warning.png' border='0'> The user demo file must be less than 2.5MB");
                $("#error_message").css({"color":"red","font-size":"14px"});
                // Removing the input file
                $('input[type=\'file\'][name=\'cloudapps_file_OCTAVE\']').val('');
                return false;
            }           
        });
        
        // Check file input size with jQuery (Max. 2.5MB)
        $('input[type=file][name=\'cloudapps_file_R\']').bind('change', function() {
            if (this.files[0].size/1000 > 25600) {     
                // Remove the img and text (if any)
                $("#error_message img:last-child").remove();
                $("#error_message").empty();
                $('#error_message').append("<img width='35' src='<%= renderRequest.getContextPath()%>/images/Warning.png' border='0'> The user demo file must be less than 2.5MB");
                $("#error_message").css({"color":"red","font-size":"14px"});
                // Removing the input file
                $('input[type=\'file\'][name=\'cloudapps_file_R\']').val('');
                return false;
            }           
        });
        
        $("#commentForm").bind('submit', function() 
        {        
            var flag=true;         
            var extension=true;            
            
            // Remove the img and text error (if any)
            $("#error_message img:last-child").remove();
            $("#error_message").empty();
            
            // Validate input form
            if ($("#cloudapps_vmname").val() == "") flag=false; 
            
            // Check the uploaded ASCII file for OCTAVE.
            if ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() == "cloudapps_ASCII_OCTAVE")
            {
                var ext = ($('input[type=file][name=\'cloudapps_file_OCTAVE\']').val().split('.').pop().toLowerCase());                
                if($.inArray(ext, ['m']) == -1) {                    
                    flag=false;
                    extension=false
                }                
            }
            
            // Check the uploaded ASCII file for R.
            if ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() == "cloudapps_ASCII_R")
            {
                var ext = ($('input[type=file][name=\'cloudapps_file_R\']').val().split('.').pop().toLowerCase());                
                if($.inArray(ext, ['r']) == -1) {                    
                    flag=false;
                    extension=false
                }                
            }
                      
            // Check if the input settings are valid before to
            // display the warning message.
            if ($("select option:selected").val()=="octave") 
            {
                console.log("Selected OCTAVE!");
                if (
                 (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() == "cloudapps_ASCII_OCTAVE") &&
                  ($('input[type=file][name=cloudapps_file_OCTAVE]').val() == "")) ||
                
                  (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() == "cloudapps_textarea_OCTAVE") &&
                  (($('#cloudapps_textarea_OCTAVE').val() == "Paste here your macro file or use the default demo") ||
                  ($('#cloudapps_textarea_OCTAVE').val() == ""))) ||
                  
                 ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() == "") ||
                
                 (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() != "cloudapps_ASCII_OCTAVE") &&
                  ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']').val() != "cloudapps_textarea_OCTAVE"))
                ) 
                    {                        
                        console.log("Missing input settings for OCTAVE");
                        flag=false;
                    }
            }                                                
            
            if ($("select option:selected").val()=="r") 
            {
                console.log("Selected R!");
                if (
                 (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() == "cloudapps_ASCII_R") &&
                  ($('input[type=file][name=cloudapps_file_R]').val() == "")) ||
                
                 (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() == "cloudapps_textarea_R") &&
                  (($('#cloudapps_textarea_R').val() == "Paste here your macro file or use the default demo") ||
                  ($('#cloudapps_textarea_R').val() == ""))) ||
                  
                 ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() == "") ||
                
                 (($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() != "cloudapps_ASCII_R") &&
                  ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']').val() != "cloudapps_textarea_R"))
                ) 
                    {                       
                        console.log("Missing input settings for R");
                        flag=false;
                    }
            }
            
            // CHECK SETTINGS: SUMMING UP ...
            if ((!extension) && (!flag)) {
                $('#error_message').append("<img width='35' src='<%= renderRequest.getContextPath()%>/images/Warning.png' border='0'> Invalid ASCII file format");
                $("#error_message").css({"color":"red","font-size":"14px"});
                //return false;
            }
            
            if ((extension) && (!flag)) {
                // Display the warning message  
                $('#error_message').append("<img width='35' src='<%= renderRequest.getContextPath()%>/images/Warning.png' border='0'> You missed many settings! They have been highlighted below.");
                $("#error_message").css({"color":"red","font-size":"14px"});
                //return false;
            }
            
            if (flag) {
                $("#dialog-message").append("<p>Thanks for submitting a new request! <br/><br/>\n\
                Your request has been successfully submitted by the Science Gateway.\n\
                Have a look on MyJobs area to get more information about all your submitted VMs.</p>");
            
                $("#dialog-message").dialog({
                modal: true,
                title: "CLOUD APPS Notification Message",
                height: 200,
                width: 350
                //buttons: { Ok: function() { $( this ).dialog("close"); } }
                });
                
                $("#error_message").css({"color":"red","font-size":"14px", "font-family": "Tahoma,Verdana,sans-serif,Arial"});                
                $('#error_message').append("<img width='30' src='<%= renderRequest.getContextPath()%>/images/button_ok.png' border='0'> Submission in progress...")(30000, function(){ $(this).remove(); });
                
                // Removes the accordion functionality completely. 
                // This will return the element back to its pre-init state.
                $("#accordion").accordion();
            } else return false;            
        });
                   
        // Roller
        $('#cloudapps_footer').rollchildren({
            delay_time         : 3000,
            loop               : true,
            pause_on_mouseover : true,
            roll_up_old_item   : true,
            speed              : 'slow'   
        });
        
        $("#stars-wrapper1").stars({
            cancelShow: false,
            captionEl: $("#stars-cap"),
            callback: function(ui, type, value)
            {
                $.getJSON("ratings.php", {rate: value}, function(json)
                {                                        
                    $("#fake-stars-on").width(Math.round( $("#fake-stars-off").width()/ui.options.items*parseFloat(json.avg) ));
                    $("#fake-stars-cap").text(json.avg + " (" + json.votes + ")");
                });
            }
         });                  
         
    });

    function enable_Demo_OCTAVE(f) 
    {        
        var _macro="";
        _macro += "# [ Octave demo ]\n";
        _macro += "# This is a macro demo that produces\n";
        _macro += "# a simple bidimentional graph of the\n";
        _macro += "# function: r=sqrt(x^2+y^2); sin(r)/r\n"; 
        _macro += "# The output will be stored into an eps\n";
        _macro += "# image file format.\n";
        _macro += "#\n";
        _macro += "# The image file will be available inside\n";
        _macro += "# the compressedi job ouput file (tar.gz)\n"; 
        _macro += "#\n"; 
        _macro += "tx = ty = linspace (-8, 8, 41)';\n"; 
        _macro += "[xx, yy] = meshgrid (tx, ty);\n"; 
        _macro += "r = sqrt (xx .^ 2 + yy .^ 2) + eps;\n"; 
        _macro += "tz = sin (r) ./ r;\n"; 
        _macro += "mesh (tx, ty, tz);\n"; 
        _macro += "print -deps demo_output.eps\n";
        
        if ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_OCTAVE\']',f).val() == "cloudapps_ASCII_OCTAVE") {
            // Enabling the uploading of the user ASCII file
            $('input[type=\'file\'][name=\'cloudapps_file_OCTAVE\']').removeAttr('disabled');
            // Disabling the specification of the cloudapps text via textarea            
            $('#cloudapps_textarea_OCTAVE').html("Paste here your macro file or use the default demo");
            $('#cloudapps_textarea_OCTAVE').attr('disabled','disabled');
        } else {
            // Disabling the uploading of the user file            
            $('input[type=\'file\'][name=\'cloudapps_file_OCTAVE\']').attr('disabled','disabled');
            // Enabling the specification of the cloudapps text via textarea            
            $('#cloudapps_textarea_OCTAVE').val(_macro);
            $('#cloudapps_textarea_OCTAVE').removeAttr('disabled');
                        
            $("#cloudapps_demo_R").removeAttr("checked");            
            $('#cloudapps_textarea_R').html("Paste here your macro file or use the default demo");
            $('#cloudapps_textarea_R').attr('disabled','disabled');
        }                
    }
    
    function enable_Demo_R(f) 
    {        
        var _macro="";
        _macro += "## This is a simple R macro\n";
        _macro += "## Create a bivariate normal distribution\n\n";
        _macro += "x <- rnorm( n = 10000, mean = 10, sd = 4 )\n"; 
        _macro += "y <- rnorm( n = 10000, mean = 10, sd = 4 )\n"; 
        _macro += "## The z value is the density of values in the x-y plane.\n\n"; 
        _macro += "z <- matrix( data = 0, nrow = 21, ncol = 21 )\n"; 
        _macro += "for ( i in seq( from = 1, to = length( x ), by = 1 ) )\n"; 
        _macro += "{\n"; 
        _macro += "if ( ( x[ i ] >= 0 ) && ( x[ i ] <= 20 ) &&\n"; 
        _macro += "( y[ i ] >= 0 ) && ( y[ i ] <= 20 ) )\n"; 
        _macro += "{\n"; 
        _macro += "xx <- round( x[ i ] )\n"; 
        _macro += "yy <- round( y[ i ] )\n"; 
        _macro += "z[ xx + 1, yy + 1 ] = z[ xx + 1, yy + 1 ] + 1\n"; 
        _macro += "}\n"; 
        _macro += "}\n\n"; 
        _macro += "## Display the density of values with a perspective plot.\n";
        _macro += "persp(\n";
        _macro += "x=0:20,\n";
        _macro += "y=0:20,\n";
        _macro += "z=z,\n";
        _macro += "theta=i,\n";
        _macro += "phi=30,\n";
        _macro += "xlab=\"x\",\n";
        _macro += "ylab=\"y\",\n";
        _macro += "col=\"lightblue\",\n";
        _macro += "ltheta=-135,\n";
        _macro += "shade=0.5,\n";
        _macro += "ticktype=\"detailed\" )\n";
  
        if ($('input:checked[type=\'radio\'][name=\'cloudapps_demo_R\']',f).val() == "cloudapps_ASCII_R") {
            // Enabling the uploading of the user ASCII file
            $('input[type=\'file\'][name=\'cloudapps_file_R\']').removeAttr('disabled');
            // Disabling the specification of the cloudapps text via textarea
            $('#cloudapps_textarea_R').html("Paste here your macro file or use the default demo");
            $('#cloudapps_textarea_R').attr('disabled','disabled');            
        } else {        
            // Disabling the uploading of the user file
            $('input[type=\'file\'][name=\'cloudapps_file_R\']').attr('disabled','disabled');
            // Enabling the specification of the cloudapps text via textarea            
            $('#cloudapps_textarea_R').val(_macro);
            $('#cloudapps_textarea_R').removeAttr('disabled');
            
            $("#cloudapps_demo_OCTAVE").removeAttr("checked");
            $('#cloudapps_textarea_OCTAVE').html("Paste here your macro file or use the default demo");
            $('#cloudapps_textarea_OCTAVE').attr('disabled','disabled');
        }
    }
    
    function DisableElement() {
        if ($('#divToToggle').css('display') == 'block')
            $('#divToToggle').toggle();
    }
    
    function toggleAndChangeText() {        
        
        if ($('#divToToggle').css('display') == 'none') {
            // Collapse the div
            $('#aTag').html('Advanced Settings &#9658');
            if ($("select option:selected").val()=="generic_vm") {
                console.log("Configuring advanced settings for the algorithm generic_VM");                
                $('#generic_VM_Toggle').show();
                $('#generic_OCTAVE_Toggle').hide();
                $('#generic_R_Toggle').hide();                             
            }
            if ($("select option:selected").val()=="octave") {
                console.log("Configuring advanced settings for the algorithm generic_OCTAVE");
                $('#generic_OCTAVE_Toggle').show();                 
                $('#generic_R_Toggle').hide();
                $('#generic_VM_Toggle').hide();                                
            }
            if ($("select option:selected").val()=="r") {                
                console.log("Configuring advanced settings for the algorithm generic_R");
                $('#generic_R_Toggle').show();
                $('#generic_OCTAVE_Toggle').hide();
                $('#generic_VM_Toggle').hide();                
            }
        } else 
            // Expand the div
            $('#aTag').html('Advanced Settings &#9660');
        
        $('#divToToggle').toggle();
}  
</script>

<br/>
<div id="dialog-message" title="Notification"></div>

<form enctype="multipart/form-data" 
      id="commentForm" 
      action="<portlet:actionURL><portlet:param name="ActionEvent" 
      value="SUBMIT_CLOUDAPPS_PORTLET"/></portlet:actionURL>"      
      method="POST">

<fieldset>
<legend>[ CloudApps ]</legend>
<div style="margin-left:15px" id="error_message"></div>

<!-- Accordions -->
<div id="accordion" style="width:650px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
<h3><a href="#">
    <img width="32" align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_1.png" />
    <b>Portlet Settings</b>
    <img width="45" align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/info_image.png"/>
    </a>
</h3>
<div> <!-- Inizio primo accordion -->
<p>The current portlet has been configured to access </p>
<table id="results" border="0" width="600">
    
<!-- CNGRID -->
<tr></tr>
<tr>
    <td>  
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='bes'}">
                <c:set var="results_bes" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_bes=='true'}">        
            <input type="checkbox" 
                   id="bes_cloudapps_ENABLEINFRASTRUCTURE"
                   name="bes_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;"
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The Chinese Cloud Infrastructure
            
            <a href="http://www.cngrid.org/">
                <img width="120" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/cngrid_logo.png" 
                 title="The China National Grid HomePage"/>
                </a>
            </c:when>
        </c:choose>
    </td>
</tr>

<!-- GARUDA -->
<tr></tr>
<tr>
    <td>      
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='garuda'}">
                <c:set var="results_garuda" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_garuda=='true'}">        
            <input type="checkbox" 
                   id="garuda_cloudapps_ENABLEINFRASTRUCTURE"
                   name="garuda_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;" 
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The GARUDA Infrastructure
            </c:when>
        </c:choose>       
    </td>
</tr>

<!-- CHAIN-REDS -->
<tr></tr>
<tr>
    <td>      
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='chain'}">
                <c:set var="results_chain" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_chain=='true'}">        
            <input type="checkbox" 
                   id="chain_cloudapps_ENABLEINFRASTRUCTURE"
                   name="chain_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;" 
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The CHAIN-REDS Cloud testbed
            
                <a href="http://www.chain-project.eu/">
                <img width="140" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/chain-reds.png" 
                 title="The CHAIN-REDS project"/>
                </a>
            </c:when>
        </c:choose>       
    </td>
</tr>

<!-- FEDCLOUD -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='fedcloud'}">
                <c:set var="results_fedcloud" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_fedcloud=='true'}">            
            <input type="checkbox" 
                   id="fedcloud_cloudapps_ENABLEINFRASTRUCTURE"
                   name="fedcloud_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;"
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The EGI Federated Cloud
            
            <a href="https://wiki.egi.eu/wiki/Fedcloud-tf:Testbed">
            <img width="90" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/EGI_Logo_RGB_315x250px.png" 
                 title="The European Grid Infrastructure (EGI)"/>
            </a>
            </c:when>
        </c:choose>
    </td>
</tr>

<!-- EUMED -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='eumed'}">
                <c:set var="results_eumed" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_eumed=='true'}">        
            <input type="checkbox" 
                   id="eumed_cloudapps_ENABLEINFRASTRUCTURE"
                   name="eumed_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;"
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The Mediterranean Grid Infrastructure
            </c:when>
        </c:choose>
    </td>
</tr>

<!-- GISELA -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='gisela'}">
                <c:set var="results_gisela" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_gisela=='true'}">        
            <input type="checkbox" 
                   id="gisela_cloudapps_ENABLEINFRASTRUCTURE"
                   name="gisela_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;"
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The Latin America Grid Infrastructure                    
            </c:when>
        </c:choose>                
    </td>
</tr>

<!-- SAGRID -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= cloudapps_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='sagrid'}">
                <c:set var="results_sagrid" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${results_sagrid=='true'}">        
            <input type="checkbox" 
                   id="sagrid_cloudapps_ENABLEINFRASTRUCTURE"
                   name="sagrid_cloudapps_ENABLEINFRASTRUCTURE"
                   size="55px;"
                   checked="checked"
                   class="textfield ui-widget ui-widget-content required"
                   disabled="disabled"/> The South African Grid Infrastructure
            </c:when>
        </c:choose>                
    </td>
</tr>

</table>

<div style="margin-left:15px" 
     id="error_infrastructure" 
     style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px; display:none;">    
</div>
<br/>

<p align="center">
<img width="120" src="<%=renderRequest.getContextPath()%>/images/separatore.gif"/>
</p>

<!--p align="justify"-->
<u>Instructions for users:</u><br/>
~ With this service it is possible to execute scientific applications on Virtual Machines (VMs) 
  deployed on standard-based federated cloud.<br/><br/>
~ The present service is based on the following standards and software frameworks: <br/><br/>
<p align="center">
<a href="http://grid.in2p3.fr/jsaga/">
<img width="200" src="<%=renderRequest.getContextPath()%>/images/logo-jsaga.png"/></a>

<a href="http://occi-wg.org/">
<img width="200" src="<%=renderRequest.getContextPath()%>/images/OCCI-logo.png"/></a>

<a href="http://www.catania-science-gateways.it">
<img width="100" src="<%=renderRequest.getContextPath()%>/images/CataniaScienceGateways.png"/></a>
</p>

<br/>
~ The following applications are currently available for testing:
<br/><br/>

<fieldset style="width: 567px; border: 1px solid green;">
<div id="tabs">
<ul>
<li><a href="#fragment-1">
<span>
<img width="30" src="<%=renderRequest.getContextPath()%>/images/logo_octave.png" /> The GNU Octave v3.6.4
</span>
</a>    
</li>

<li><a href="#fragment-2">
<span>
<img width="39" src="<%=renderRequest.getContextPath()%>/images/Rlogo.png"/> GNU R v2.15.3
</span>
</a>
</li>

<li><a href="#fragment-3">
<span>
<img width="30" src="<%=renderRequest.getContextPath()%>/images/helloworld.png"/> Generic VM
</span>
</a>
</li>
</ul>
    
<div id="fragment-1">
<table id="results" border="0">               
<tr>
<td>
    <p align="justify">
    <u>GNU Octave-3.6.4</u> is a high-level interpreted language, primarily intended 
    for numerical computations. 
    It provides capabilities for the numerical solution of linear and nonlinear problems, and for 
    performing other numerical experiments. It also provides extensive graphics capabilities for 
    data visualization and manipulation.
    </p>
</td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td width="100">
<a href="http://www.gnu.org/software/octave/">
<img width="85" src="<%=renderRequest.getContextPath()%>/images/logo_octave.png"/>
</a>
</td>
</tr>
<tr>
<td colspan="3">
Octave is distributed under the terms of the  
<a href="https://www.gnu.org/software/octave/license.html">GNU General Public License</a>.    
</td>
</tr>
</table>
</div>
    
<div id="fragment-2">
<table id="results" border="0">               
<tr>
<td>
    <p align="justify">
    <u>R-2.15.3</u> is a free software environment for statistical computing and graphics. 
    R provides a wide variety of statistical (linear and nonlinear modeling, 
    classical statistical tests, time-series analysis, classification, clustering,
    etc) and graphical techniques, and is highly extensible.
    It compiles and runs on a wide variety of UNIX platforms, Windows and MacOS.    
    </p>
</td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td width="100">
<a href="http://www.r-project.org/">
<img width="85" src="<%=renderRequest.getContextPath()%>/images/Rlogo.png"/>
</a>
</td>
</tr>
<tr>
<td colspan="3">
R is available as Free Software under the terms of the <a href="http://www.gnu.org/">Free Software Foundation</a>'s 
<a href="http://www.r-project.org/COPYING">GNU General Public License</a> in source code form.    
</td>
</tr>
</table>
</div>

<div id="fragment-3">
<table id="results" border="0">               
<tr>
<td>
    <p align="justify">
    The <u>Sequential "Hello World!"</u> application just outputs the name of the 
    Virtual Machine where the job has been executed.
    </p>
</td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td width="100">
<a href="#">
<img width="85" src="<%=renderRequest.getContextPath()%>/images/helloworld.png"/>
</a>
</td>
</tr>
</table>    
</div>
</div>
</fieldset>
<br/>

<img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png" title="Read the Help"/>
For further details, please click
<a href="<portlet:renderURL portletMode='HELP'><portlet:param name='action' value='./help.jsp' />
         </portlet:renderURL>" >here</a>
<br/><br/>

<p>
<img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png" title="Read the Help"/>
If you need to change some preferences, please contact the
<a href="mailto:credentials-admin@ct.infn.it?Subject=Request for Technical Support [<%=gateway%> Science Gateway]&Body=Describe Your Problems&CC=sg-licence@ct.infn.it"> administrator</a>
</p>

<liferay-ui:ratings
    className="<%= it.infn.ct.cloudapps.Cloudapps.class.getName()%>"
    classPK="<%= request.getAttribute(WebKeys.RENDER_PORTLET).hashCode()%>" />
    
</div> <!-- Fine Primo Accordion -->

<h3><a href="#">
    <img width="32" align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_2.png" />
    <b>The Cloud Testbed</b>
    <img width="40" align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/clouds.png"/>
    </a>
</h3> 
    
<div> 
    <a href="https://developers.google.com/maps/documentation/javascript/tutorial?hl=it/">
    <img width="150" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/developers-logo.svg" 
             border="0" title="Google Maps JavaScript API v3"/>
    </a>
             
    <!-- Inizio Secondo accordion -->                
    <p>See the Cloud Resource(s) where you can deploy the selected VM</p>
    
    <table border="0">
        <tr>
            <td><legend>Legend</legend></td>
            <td>&nbsp;<img src="<%=renderRequest.getContextPath()%>/images/plus_new.png"/></td>
            <td>&nbsp;Split close sites&nbsp;</td>
        
            <td><img src="<%=renderRequest.getContextPath()%>/images/minus_new.png"/></td>
            <td>&nbsp;Unsplit close sites&nbsp;</td>
                        
            <td><img src="<%=renderRequest.getContextPath()%>/images/CloudBlue-1.png"/></td>
            <td>&nbsp;Cloud Resource&nbsp;</td>
        </tr>    
        <tr><td>&nbsp;</td></tr>
    </table>

    <legend>
        <div id="map_canvas" style="width:570px; height:600px;"></div>
    </legend>

    <input type="hidden" 
           name="cloudapps_CR" 
           id="cloudapps_CR"
           size="25px;" 
           class="textfield ui-widget ui-widget-content"
           value=""/>                  
</div> <!-- Fine Secondo Accordion -->        

<h3><a href="#">
    <img width="32" align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_3.png"/>
    <b>Specify the VM Settings</b>
    <img width="40" align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/icon_small_settings.png"/>
    </a>
</h3>
    
<div> <!-- Inizio Terzo accordion -->
<p align="justify">
<img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png" title="Read the Help"/>
Please, use the drop-down list to choose the VM profile you wish to deploy
</p>

<fieldset style="width:570px; border: 1px solid green;">
<legend>[ VM Settings ]</legend>
<table border="0" width="500">  
<tr>
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Please, select the generic VM to be launched"/>
        
    <label for="cloudappstype">Virtual Server </label><em>*</em>            
    </td>
        
    <td width="260">            
    <select name="cloudappstype" 
            style="height:25px; padding-left: 1px; border-style: solid; 
                   border-color: grey; border-width: 1px; padding-left: 1px;
                   width: 200px;"
            onChange="DisableElement();">                                
                    
     <!--option value="generic_vm">Sequential Hello World</option-->
     <option value="generic_vm">Generic VM</option>
     <option value="octave">OCTAVE-3.6.4</option>
     <option value="r">R-2.15.3</option>                
     </select>
     </td>                
</tr>
    
<tr>
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Please, select the VM flavor"/>
        
    <label for="cloudappsvmflavor">VM Flavor</label><em>*</em>
    </td>
        
    <td width="260">            
    <select name="cloudappsvmflavor" 
            style="height:25px; 
                   padding-left: 1px; 
                   border-style: solid; 
                   border-color: grey; 
                   border-width: 1px; 
                   padding-left: 1px;
                   width: 200px;">                                
                    
    <option value="small">small</option>
    <option value="medium">medium</option>
    <option value="large">large</option>
    <!--option value="larger">larger</option-->
    </select>
    </td>
</tr>
   
<tr>
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Choose a label for your server "/>
        
    <label for="cloudapps_vmname">VM Name <em>*</em></label>
    </td>
        
    <td width="260">         
    <input type="text"                
           id="cloudapps_vmname"
           name="cloudapps_vmname"
           style="padding-left: 1px; border-style: solid; 
                  border-color: grey; border-width: 1px; 
                  padding-left: 1px;"
           value="cloudapps_server"
           class="required"
           size="40" />
    </td>           
</tr>    
    
<tr>
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Choose a description for your run "/>
        
     <label for="cloudapps_desc">Description</label>
     </td>
             
     <td width="260">
     <input type="text"                
            id="cloudapps_desc"
            name="cloudapps_desc"
            style="padding-left: 1px; border-style: solid; 
                   border-color: grey; border-width: 1px; 
                   padding-left: 1px;"
            value="Please, insert here your description"
            size="40" />
     </td>
</tr>
    
<tr><td><br/></td></tr>            
    
<tr>
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Enable email notification to the user"/>
        
    <c:set var="enabled_SMTP" value="<%= SMTP_HOST %>" />
    <c:set var="enabled_SENDER" value="<%= SENDER_MAIL %>" />
    <c:choose>
    <c:when test="${empty enabled_SMTP || empty enabled_SENDER}">
        <input type="checkbox" 
               name="EnableNotification"
               disabled="disable"
               value="yes" /> Notification &nbsp;&nbsp;
    </c:when>
    <c:otherwise>
    <input type="checkbox" 
           name="EnableNotification"               
           value="yes" /> Notification &nbsp;&nbsp;&nbsp;
    </c:otherwise>
    </c:choose>
    </td>
        
    <td width="260">
    <img width="70"
         id="EnableNotificationid"             
         src="<%= renderRequest.getContextPath()%>/images/mailing2.png" 
         border="0"/>
    </td>
</tr>
</table>
    
<table border="0" width="500">
<tr>
    <td width="200">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Customize some VM settings"/>
        
    <a id="aTag" href="javascript:toggleAndChangeText();">
    Advanced Settings &#9660;
    </a>
    <div id="divToToggle" display="none">
            
    <!-- ------------------------------- -->
    <!-- generic_OCTAVE Advanced Settings -->
    <!-- ------------------------------- -->          
    <br/>
    <img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png" title="Read the Help"/>
    Configuring advanced settings (if any) for the selected VM<br/><br/>
    <table id="generic_OCTAVE_Toggle" border="0">
    <tr> 
    <td width="160">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" title="Upload your data to be processed as ASCII file"/>

        <input type="radio" 
               name="cloudapps_demo_OCTAVE"
               id="cloudapps_demo_OCTAVE"
               value="cloudapps_ASCII_OCTAVE"
               class="required"
               onchange="enable_Demo_OCTAVE(this.form);"/>Upload your macro ASCII file (*.m) for OCTAVE (Max 2,5MB) <em>*</em>
            
        <input type="file" name="cloudapps_file_OCTAVE" width="400" class="required" 
               style="padding-left: 1px; border-style: solid; 
                      border-color: gray; border-width: 1px; padding-left: 1px;"
               disabled="disabled"/>
    </td>
    </tr>
        
    <tr>
    <td width="160">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png" 
         border="0" title="Use the texta-area to insert the data to be processed"/>
        
     <input type="radio" 
            name="cloudapps_demo_OCTAVE" 
            id="cloudapps_demo_OCTAVE"
            value="cloudapps_textarea_OCTAVE" 
            class="required"
            onchange="enable_Demo_OCTAVE(this.form);"/>Insert here the macro to be processed <em>*</em>
     </td>
     </tr>
        
     <tr>
     <td>            
     <textarea id="cloudapps_textarea_OCTAVE" 
               name="cloudapps_textarea_OCTAVE"
               style="padding-left: 1px; border-style: solid; width: 113%;
                      border-color: grey; border-width: 1px; padding-left: 1px;"
               class="required"
               disabled="disabled"
               rows="5" cols="69">                
     </textarea>            
     </td>
     </tr>
     </table>
     <!-- ------------------------------- -->
     <!-- generic_OCTAVE Advanced Settings -->
     <!-- ------------------------------- -->   
        
     <!-- ------------------------------- -->
     <!--  generic_R Advanced Settings   -->
     <!-- ------------------------------- -->
     <table id="generic_R_Toggle" border="0">        
     <tr> 
     <td width="160">
     <img width="30" 
          align="absmiddle"
          src="<%= renderRequest.getContextPath()%>/images/question.png" 
          border="0" title="Upload your data to be processed as ASCII file"/>

      <input type="radio" 
             name="cloudapps_demo_R"
             id="cloudapps_demo_R"
             value="cloudapps_ASCII_R"
             class="required"
             onchange="enable_Demo_R(this.form);"/>Upload your macro ASCII file (*.r) for R (Max 2,5MB) <em>*</em>
            
      <input type="file" name="cloudapps_file_R" width="500" class="required" 
             style="padding-left: 1px; border-style: solid; 
                    border-color: gray; border-width: 1px; padding-left: 1px;"
             disabled="disabled"/>
      </td>
      </tr>
        
      <tr>
      <td width="160">
      <img width="30" 
           align="absmiddle"
           src="<%= renderRequest.getContextPath()%>/images/question.png" 
           border="0" title="Use the texta-area to insert the data to be processed"/>
        
      <input type="radio" 
             name="cloudapps_demo_R" 
             id="cloudapps_demo_R"
             value="cloudapps_textarea_R" 
             class="required"
             onchange="enable_Demo_R(this.form);"/>Insert here the macro to be processed <em>*</em>
      </td>
      </tr>
        
      <tr>
      <td>
      <textarea id="cloudapps_textarea_R" 
                name="cloudapps_textarea_R"
                style="padding-left: 1px; border-style: solid; width: 113%;
                       border-color: grey; border-width: 1px; padding-left: 1px;"
                class="required"
                disabled="disabled"
                rows="5" cols="69">                
      </textarea>                 
      </td>
      </tr>
        
      <tr><td></td></tr>
      </table>        
      <!-- ------------------------------- -->
      <!--   generic_R Advanced Settings   -->
      <!-- ------------------------------- -->
      </div>
      </td>
      </tr>

      <tr><td><br/></td></tr>    

      <tr>                    
      <td align="left">
      <input type="image" 
             src="<%= renderRequest.getContextPath()%>/images/start-icon.png"
             width="60"                   
             name="submit"
             id ="submit" 
             title="Run your Simulation!" />                    
      </td>
      </tr>                                            
</table>
</fieldset>
</div>	<!-- Fine Terzo Accordion -->
</div> <!-- Fine Accordions -->
</fieldset>    
</form>                                                                         

<div id="cloudapps_footer" style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
    <div>CloudApps portlet ver. 1.1.0</div>
    <div>The Italian National Institute of Nuclear Physics (INFN), division of Catania, Italy</div>
    <div>Copyright © 2014 - 2015. All rights reserved</div>
</div>