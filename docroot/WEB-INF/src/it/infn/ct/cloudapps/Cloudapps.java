/*
 *************************************************************************
Copyright (c) 2011-2015:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on
the copyright holders.

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
 ***************************************************************************
 */
package it.infn.ct.cloudapps;

// import liferay libraries
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

// import DataEngine libraries
import com.liferay.portal.util.PortalUtil;
import it.infn.ct.GridEngine.InformationSystem.BDII;
import it.infn.ct.GridEngine.Job.*;

// import generic Java libraries
import it.infn.ct.GridEngine.UsersTracking.UsersTrackingDBInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URI;

// import portlet libraries
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

// Importing Apache libraries
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Cloudapps extends GenericPortlet {

    private static Log log = LogFactory.getLog(Cloudapps.class);

    @Override
    protected void doEdit(RenderRequest request,
            RenderResponse response)
            throws PortletException, IOException
    {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");
        
        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the BES VO
        String bes_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("bes_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the BES VO
        String bes_cloudapps_VONAME = portletPreferences.getValue("bes_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the BES VO
        String bes_cloudapps_TOPBDII = portletPreferences.getValue("bes_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the BES VO
        String[] bes_cloudapps_WMS = portletPreferences.getValues("bes_cloudapps_WMS", new String[5]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the BES VO
        String bes_cloudapps_ETOKENSERVER = portletPreferences.getValue("bes_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the BES VO
        String bes_cloudapps_MYPROXYSERVER = portletPreferences.getValue("bes_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the BES VO
        String bes_cloudapps_PORT = portletPreferences.getValue("bes_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the BES VO
        String bes_cloudapps_ROBOTID = portletPreferences.getValue("bes_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the BES VO
        String bes_cloudapps_ROLE = portletPreferences.getValue("bes_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the BES VO
        String bes_cloudapps_RENEWAL = portletPreferences.getValue("bes_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the BES VO
        String bes_cloudapps_DISABLEVOMS = portletPreferences.getValue("bes_cloudapps_DISABLEVOMS", "unchecked");
        
        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("garuda_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_VONAME = portletPreferences.getValue("garuda_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPBDII from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_TOPBDII = portletPreferences.getValue("garuda_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the GARUDA VO
        String[] garuda_cloudapps_WMS = portletPreferences.getValues("garuda_cloudapps_WMS", new String[5]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_ETOKENSERVER = portletPreferences.getValue("garuda_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_MYPROXYSERVER = portletPreferences.getValue("garuda_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_PORT = portletPreferences.getValue("garuda_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_ROBOTID = portletPreferences.getValue("garuda_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_ROLE = portletPreferences.getValue("garuda_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_RENEWAL = portletPreferences.getValue("garuda_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the GARUDA VO
        String garuda_cloudapps_DISABLEVOMS = portletPreferences.getValue("garuda_cloudapps_DISABLEVOMS", "unchecked");

        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the CHAIN VO
        String chain_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("chain_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the CHAIN VO
        String chain_cloudapps_VONAME = portletPreferences.getValue("chain_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the CHAIN VO
        String chain_cloudapps_TOPBDII = portletPreferences.getValue("chain_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the CHAIN VO
        String[] chain_cloudapps_WMS = portletPreferences.getValues("chain_cloudapps_WMS", new String[10]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the CHAIN VO
        String chain_cloudapps_ETOKENSERVER = portletPreferences.getValue("chain_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the CHAIN VO
        String chain_cloudapps_MYPROXYSERVER = portletPreferences.getValue("chain_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the CHAIN VO
        String chain_cloudapps_PORT = portletPreferences.getValue("chain_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the CHAIN VO
        String chain_cloudapps_ROBOTID = portletPreferences.getValue("chain_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the CHAIN VO
        String chain_cloudapps_ROLE = portletPreferences.getValue("chain_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the CHAIN VO
        String chain_cloudapps_RENEWAL = portletPreferences.getValue("chain_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the CHAIN VO
        String chain_cloudapps_DISABLEVOMS = portletPreferences.getValue("chain_cloudapps_DISABLEVOMS", "unchecked");
        
        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("fedcloud_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_VONAME = portletPreferences.getValue("fedcloud_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_TOPBDII = portletPreferences.getValue("fedcloud_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the FEDCLOUD VO
        String[] fedcloud_cloudapps_WMS = portletPreferences.getValues("fedcloud_cloudapps_WMS", new String[10]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_ETOKENSERVER = portletPreferences.getValue("fedcloud_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_MYPROXYSERVER = portletPreferences.getValue("fedcloud_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_PORT = portletPreferences.getValue("fedcloud_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_ROBOTID = portletPreferences.getValue("fedcloud_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_ROLE = portletPreferences.getValue("fedcloud_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_RENEWAL = portletPreferences.getValue("fedcloud_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the FEDCLOUD VO
        String fedcloud_cloudapps_DISABLEVOMS = portletPreferences.getValue("fedcloud_cloudapps_DISABLEVOMS", "unchecked");

        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the EUMED VO
        String eumed_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("eumed_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the EUMED VO
        String eumed_cloudapps_VONAME = portletPreferences.getValue("eumed_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the EUMED VO
        String eumed_cloudapps_TOPBDII = portletPreferences.getValue("eumed_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the EUMED VO
        String[] eumed_cloudapps_WMS = portletPreferences.getValues("eumed_cloudapps_WMS", new String[5]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the EUMED VO
        String eumed_cloudapps_ETOKENSERVER = portletPreferences.getValue("eumed_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the EUMED VO
        String eumed_cloudapps_MYPROXYSERVER = portletPreferences.getValue("eumed_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the EUMED VO
        String eumed_cloudapps_PORT = portletPreferences.getValue("eumed_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the EUMED VO
        String eumed_cloudapps_ROBOTID = portletPreferences.getValue("eumed_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the EUMED VO
        String eumed_cloudapps_ROLE = portletPreferences.getValue("eumed_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the EUMED VO
        String eumed_cloudapps_RENEWAL = portletPreferences.getValue("eumed_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the EUMED VO
        String eumed_cloudapps_DISABLEVOMS = portletPreferences.getValue("eumed_cloudapps_DISABLEVOMS", "unchecked");

        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the GISELA VO
        String gisela_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("gisela_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the GISELA VO
        String gisela_cloudapps_VONAME = portletPreferences.getValue("gisela_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the GISELA VO
        String gisela_cloudapps_TOPBDII = portletPreferences.getValue("gisela_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the GISELA VO
        String[] gisela_cloudapps_WMS = portletPreferences.getValues("gisela_cloudapps_WMS", new String[5]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the GISELA VO
        String gisela_cloudapps_ETOKENSERVER = portletPreferences.getValue("gisela_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the GISELA VO
        String gisela_cloudapps_MYPROXYSERVER = portletPreferences.getValue("gisela_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the GISELA VO
        String gisela_cloudapps_PORT = portletPreferences.getValue("gisela_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the GISELA VO
        String gisela_cloudapps_ROBOTID = portletPreferences.getValue("gisela_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the GISELA VO
        String gisela_cloudapps_ROLE = portletPreferences.getValue("gisela_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the GISELA VO
        String gisela_cloudapps_RENEWAL = portletPreferences.getValue("gisela_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the GISELA VO
        String gisela_cloudapps_DISABLEVOMS = portletPreferences.getValue("gisela_cloudapps_DISABLEVOMS", "unchecked");
        
        // Get the CLOUD INFRASTRUCTURE from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("sagrid_cloudapps_INFRASTRUCTURE", "N/A");
        // Get the CLOUD VONAME from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_VONAME = portletPreferences.getValue("sagrid_cloudapps_VONAME", "N/A");
        // Get the CLOUD TOPPBDII from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_TOPBDII = portletPreferences.getValue("sagrid_cloudapps_TOPBDII", "N/A");
        // Get the CLOUD WMS from the portlet preferences for the SAGRID VO
        String[] sagrid_cloudapps_WMS = portletPreferences.getValues("sagrid_cloudapps_WMS", new String[5]);
        // Get the CLOUD ETOKENSERVER from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_ETOKENSERVER = portletPreferences.getValue("sagrid_cloudapps_ETOKENSERVER", "N/A");
        // Get the CLOUD MYPROXYSERVER from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_MYPROXYSERVER = portletPreferences.getValue("sagrid_cloudapps_MYPROXYSERVER", "N/A");
        // Get the CLOUD PORT from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_PORT = portletPreferences.getValue("sagrid_cloudapps_PORT", "N/A");
        // Get the CLOUD ROBOTID from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_ROBOTID = portletPreferences.getValue("sagrid_cloudapps_ROBOTID", "N/A");
        // Get the CLOUD ROLE from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_ROLE = portletPreferences.getValue("sagrid_cloudapps_ROLE", "N/A");
        // Get the CLOUD RENEWAL from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_RENEWAL = portletPreferences.getValue("sagrid_cloudapps_RENEWAL", "checked");
        // Get the CLOUD DISABLEVOMS from the portlet preferences for the SAGRID VO
        String sagrid_cloudapps_DISABLEVOMS = portletPreferences.getValue("sagrid_cloudapps_DISABLEVOMS", "unchecked");

        // Get the CLOUD APPID from the portlet preferences
        String cloudapps_APPID = portletPreferences.getValue("cloudapps_APPID", "N/A");
        // Get the CLOUD LOG LEVEL from the portlet preferences
        String cloudapps_LOGLEVEL = portletPreferences.getValue("cloudapps_LOGLEVEL", "INFO");
        // Get the CLOUD OUTPUT from the portlet preferences
        String cloudapps_OUTPUT_PATH = portletPreferences.getValue("cloudapps_OUTPUT_PATH", "/tmp");
        // Get the CLOUD SOFTWARE from the portlet preferences
        String cloudapps_SOFTWARE = portletPreferences.getValue("cloudapps_SOFTWARE", "N/A");
        // Get the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Get the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Get the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Get the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Get the SENDER MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        // Get the list of enabled Infrastructures
        String[] infras = portletPreferences.getValues("cloudapps_ENABLEINFRASTRUCTURE", new String[7]);

        // Set the default portlet preferences
        request.setAttribute("chain_cloudapps_INFRASTRUCTURE", chain_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("chain_cloudapps_VONAME", chain_cloudapps_VONAME.trim());
        request.setAttribute("chain_cloudapps_TOPBDII", chain_cloudapps_TOPBDII.trim());
        request.setAttribute("chain_cloudapps_WMS", chain_cloudapps_WMS);
        request.setAttribute("chain_cloudapps_ETOKENSERVER", chain_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("chain_cloudapps_MYPROXYSERVER", chain_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("chain_cloudapps_PORT", chain_cloudapps_PORT.trim());
        request.setAttribute("chain_cloudapps_ROBOTID", chain_cloudapps_ROBOTID.trim());
        request.setAttribute("chain_cloudapps_ROLE", chain_cloudapps_ROLE.trim());
        request.setAttribute("chain_cloudapps_RENEWAL", chain_cloudapps_RENEWAL);
        request.setAttribute("chain_cloudapps_DISABLEVOMS", chain_cloudapps_DISABLEVOMS);
        
        request.setAttribute("garuda_cloudapps_INFRASTRUCTURE", garuda_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("garuda_cloudapps_VONAME", garuda_cloudapps_VONAME.trim());
        request.setAttribute("garuda_cloudapps_TOPBDII", garuda_cloudapps_TOPBDII.trim());
        request.setAttribute("garuda_cloudapps_WMS", garuda_cloudapps_WMS);
        request.setAttribute("garuda_cloudapps_ETOKENSERVER", garuda_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("garuda_cloudapps_MYPROXYSERVER", garuda_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("garuda_cloudapps_PORT", garuda_cloudapps_PORT.trim());
        request.setAttribute("garuda_cloudapps_ROBOTID", garuda_cloudapps_ROBOTID.trim());
        request.setAttribute("garuda_cloudapps_ROLE", garuda_cloudapps_ROLE.trim());
        request.setAttribute("garuda_cloudapps_RENEWAL", garuda_cloudapps_RENEWAL);
        request.setAttribute("garuda_cloudapps_DISABLEVOMS", garuda_cloudapps_DISABLEVOMS);
        
        request.setAttribute("bes_cloudapps_INFRASTRUCTURE", bes_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("bes_cloudapps_VONAME", bes_cloudapps_VONAME.trim());
        request.setAttribute("bes_cloudapps_TOPBDII", bes_cloudapps_TOPBDII.trim());
        request.setAttribute("bes_cloudapps_WMS", bes_cloudapps_WMS);
        request.setAttribute("bes_cloudapps_ETOKENSERVER", bes_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("bes_cloudapps_MYPROXYSERVER", bes_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("bes_cloudapps_PORT", bes_cloudapps_PORT.trim());
        request.setAttribute("bes_cloudapps_ROBOTID", bes_cloudapps_ROBOTID.trim());
        request.setAttribute("bes_cloudapps_ROLE", bes_cloudapps_ROLE.trim());
        request.setAttribute("bes_cloudapps_RENEWAL", bes_cloudapps_RENEWAL);
        request.setAttribute("bes_cloudapps_DISABLEVOMS", bes_cloudapps_DISABLEVOMS);
        
        request.setAttribute("fedcloud_cloudapps_INFRASTRUCTURE", fedcloud_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("fedcloud_cloudapps_VONAME", fedcloud_cloudapps_VONAME.trim());
        request.setAttribute("fedcloud_cloudapps_TOPBDII", fedcloud_cloudapps_TOPBDII.trim());
        request.setAttribute("fedcloud_cloudapps_WMS", fedcloud_cloudapps_WMS);
        request.setAttribute("fedcloud_cloudapps_ETOKENSERVER", fedcloud_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("fedcloud_cloudapps_MYPROXYSERVER", fedcloud_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("fedcloud_cloudapps_PORT", fedcloud_cloudapps_PORT.trim());
        request.setAttribute("fedcloud_cloudapps_ROBOTID", fedcloud_cloudapps_ROBOTID.trim());
        request.setAttribute("fedcloud_cloudapps_ROLE", fedcloud_cloudapps_ROLE.trim());
        request.setAttribute("fedcloud_cloudapps_RENEWAL", fedcloud_cloudapps_RENEWAL);
        request.setAttribute("fedcloud_cloudapps_DISABLEVOMS", fedcloud_cloudapps_DISABLEVOMS);

        request.setAttribute("eumed_cloudapps_INFRASTRUCTURE", eumed_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("eumed_cloudapps_VONAME", eumed_cloudapps_VONAME.trim());
        request.setAttribute("eumed_cloudapps_TOPBDII", eumed_cloudapps_TOPBDII.trim());
        request.setAttribute("eumed_cloudapps_WMS", eumed_cloudapps_WMS);
        request.setAttribute("eumed_cloudapps_ETOKENSERVER", eumed_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("eumed_cloudapps_MYPROXYSERVER", eumed_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("eumed_cloudapps_PORT", eumed_cloudapps_PORT.trim());
        request.setAttribute("eumed_cloudapps_ROBOTID", eumed_cloudapps_ROBOTID.trim());
        request.setAttribute("eumed_cloudapps_ROLE", eumed_cloudapps_ROLE.trim());
        request.setAttribute("eumed_cloudapps_RENEWAL", eumed_cloudapps_RENEWAL);
        request.setAttribute("eumed_cloudapps_DISABLEVOMS", eumed_cloudapps_DISABLEVOMS);                

        request.setAttribute("gisela_cloudapps_INFRASTRUCTURE", gisela_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("gisela_cloudapps_VONAME", gisela_cloudapps_VONAME.trim());
        request.setAttribute("gisela_cloudapps_TOPBDII", gisela_cloudapps_TOPBDII.trim());
        request.setAttribute("gisela_cloudapps_WMS", gisela_cloudapps_WMS);
        request.setAttribute("gisela_cloudapps_ETOKENSERVER", gisela_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("gisela_cloudapps_MYPROXYSERVER", gisela_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("gisela_cloudapps_PORT", gisela_cloudapps_PORT.trim());
        request.setAttribute("gisela_cloudapps_ROBOTID", gisela_cloudapps_ROBOTID.trim());
        request.setAttribute("gisela_cloudapps_ROLE", gisela_cloudapps_ROLE.trim());
        request.setAttribute("gisela_cloudapps_RENEWAL", gisela_cloudapps_RENEWAL);
        request.setAttribute("gisela_cloudapps_DISABLEVOMS", gisela_cloudapps_DISABLEVOMS);
        
        request.setAttribute("sagrid_cloudapps_INFRASTRUCTURE", sagrid_cloudapps_INFRASTRUCTURE.trim());
        request.setAttribute("sagrid_cloudapps_VONAME", sagrid_cloudapps_VONAME.trim());
        request.setAttribute("sagrid_cloudapps_TOPBDII", sagrid_cloudapps_TOPBDII.trim());
        request.setAttribute("sagrid_cloudapps_WMS", sagrid_cloudapps_WMS);
        request.setAttribute("sagrid_cloudapps_ETOKENSERVER", sagrid_cloudapps_ETOKENSERVER.trim());
        request.setAttribute("sagrid_cloudapps_MYPROXYSERVER", sagrid_cloudapps_MYPROXYSERVER.trim());
        request.setAttribute("sagrid_cloudapps_PORT", sagrid_cloudapps_PORT.trim());
        request.setAttribute("sagrid_cloudapps_ROBOTID", sagrid_cloudapps_ROBOTID.trim());
        request.setAttribute("sagrid_cloudapps_ROLE", sagrid_cloudapps_ROLE.trim());
        request.setAttribute("sagrid_cloudapps_RENEWAL", sagrid_cloudapps_RENEWAL);
        request.setAttribute("sagrid_cloudapps_DISABLEVOMS", sagrid_cloudapps_DISABLEVOMS);

        request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
        request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
        request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
        request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
        request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
        request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
        request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
        request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
        request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
        request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());        

        if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
        log.info("\nStarting the EDIT mode...with this settings"
        + "\nbes_cloudapps_INFRASTRUCTURE: " + bes_cloudapps_INFRASTRUCTURE
        + "\nbes_cloudapps_VONAME: " + bes_cloudapps_VONAME
        + "\nbes_cloudapps_TOPBDII: " + bes_cloudapps_TOPBDII                    
        + "\nbes_cloudapps_ETOKENSERVER: " + bes_cloudapps_ETOKENSERVER
        + "\nbes_cloudapps_MYPROXYSERVER: " + bes_cloudapps_MYPROXYSERVER
        + "\nbes_cloudapps_PORT: " + bes_cloudapps_PORT
        + "\nbes_cloudapps_ROBOTID: " + bes_cloudapps_ROBOTID
        + "\nbes_cloudapps_ROLE: " + bes_cloudapps_ROLE
        + "\nbes_cloudapps_RENEWAL: " + bes_cloudapps_RENEWAL
        + "\nbes_cloudapps_DISABLEVOMS: " + bes_cloudapps_DISABLEVOMS
                
        + "\n\ngaruda_cloudapps_INFRASTRUCTURE: " + garuda_cloudapps_INFRASTRUCTURE
        + "\ngaruda_cloudapps_VONAME: " + garuda_cloudapps_VONAME
        + "\ngaruda_cloudapps_TOPBDII: " + garuda_cloudapps_TOPBDII                    
        + "\ngaruda_cloudapps_ETOKENSERVER: " + garuda_cloudapps_ETOKENSERVER
        + "\ngaruda_cloudapps_MYPROXYSERVER: " + garuda_cloudapps_MYPROXYSERVER
        + "\ngaruda_cloudapps_PORT: " + garuda_cloudapps_PORT
        + "\ngaruda_cloudapps_ROBOTID: " + garuda_cloudapps_ROBOTID
        + "\ngaruda_cloudapps_ROLE: " + garuda_cloudapps_ROLE
        + "\ngaruda_cloudapps_RENEWAL: " + garuda_cloudapps_RENEWAL
        + "\ngaruda_cloudapps_DISABLEVOMS: " + garuda_cloudapps_DISABLEVOMS 
                
        + "\n\nchain_cloudapps_INFRASTRUCTURE: " + chain_cloudapps_INFRASTRUCTURE
        + "\nchain_cloudapps_VONAME: " + chain_cloudapps_VONAME
        + "\nchain_cloudapps_TOPBDII: " + chain_cloudapps_TOPBDII                    
        + "\nchain_cloudapps_ETOKENSERVER: " + chain_cloudapps_ETOKENSERVER
        + "\nchain_cloudapps_MYPROXYSERVER: " + chain_cloudapps_MYPROXYSERVER
        + "\nchain_cloudapps_PORT: " + chain_cloudapps_PORT
        + "\nchain_cloudapps_ROBOTID: " + chain_cloudapps_ROBOTID
        + "\nchain_cloudapps_ROLE: " + chain_cloudapps_ROLE
        + "\nchain_cloudapps_RENEWAL: " + chain_cloudapps_RENEWAL
        + "\nchain_cloudapps_DISABLEVOMS: " + chain_cloudapps_DISABLEVOMS
                
        + "\n\nfedcloud_cloudapps_INFRASTRUCTURE: " + fedcloud_cloudapps_INFRASTRUCTURE
        + "\nfedcloud_cloudapps_VONAME: " + fedcloud_cloudapps_VONAME
        + "\nfedcloud_cloudapps_TOPBDII: " + fedcloud_cloudapps_TOPBDII                    
        + "\nfedcloud_cloudapps_ETOKENSERVER: " + fedcloud_cloudapps_ETOKENSERVER
        + "\nfedcloud_cloudapps_MYPROXYSERVER: " + fedcloud_cloudapps_MYPROXYSERVER
        + "\nfedcloud_cloudapps_PORT: " + fedcloud_cloudapps_PORT
        + "\nfedcloud_cloudapps_ROBOTID: " + fedcloud_cloudapps_ROBOTID
        + "\nfedcloud_cloudapps_ROLE: " + fedcloud_cloudapps_ROLE
        + "\nfedcloud_cloudapps_RENEWAL: " + fedcloud_cloudapps_RENEWAL
        + "\nfedcloud_cloudapps_DISABLEVOMS: " + fedcloud_cloudapps_DISABLEVOMS

        + "\n\neumed_cloudapps_INFRASTRUCTURE: " + eumed_cloudapps_INFRASTRUCTURE
        + "\neumed_cloudapps_VONAME: " + eumed_cloudapps_VONAME
        + "\neumed_cloudapps_TOPBDII: " + eumed_cloudapps_TOPBDII                    
        + "\neumed_cloudapps_ETOKENSERVER: " + eumed_cloudapps_ETOKENSERVER
        + "\neumed_cloudapps_MYPROXYSERVER: " + eumed_cloudapps_MYPROXYSERVER
        + "\neumed_cloudapps_PORT: " + eumed_cloudapps_PORT
        + "\neumed_cloudapps_ROBOTID: " + eumed_cloudapps_ROBOTID
        + "\neumed_cloudapps_ROLE: " + eumed_cloudapps_ROLE
        + "\neumed_cloudapps_RENEWAL: " + eumed_cloudapps_RENEWAL
        + "\neumed_cloudapps_DISABLEVOMS: " + eumed_cloudapps_DISABLEVOMS

        + "\n\ngisela_cloudapps_INFRASTRUCTURE: " + gisela_cloudapps_INFRASTRUCTURE
        + "\ngisela_cloudapps_VONAME: " + gisela_cloudapps_VONAME
        + "\ngisela_cloudapps_TOPBDII: " + gisela_cloudapps_TOPBDII                   
        + "\ngisela_cloudapps_ETOKENSERVER: " + gisela_cloudapps_ETOKENSERVER
        + "\ngisela_cloudapps_MYPROXYSERVER: " + gisela_cloudapps_MYPROXYSERVER
        + "\ngisela_cloudapps_PORT: " + gisela_cloudapps_PORT
        + "\ngisela_cloudapps_ROBOTID: " + gisela_cloudapps_ROBOTID
        + "\ngisela_cloudapps_ROLE: " + gisela_cloudapps_ROLE
        + "\ngisela_cloudapps_RENEWAL: " + gisela_cloudapps_RENEWAL
        + "\ngisela_cloudapps_DISABLEVOMS: " + gisela_cloudapps_DISABLEVOMS
                
        + "\n\nsagrid_cloudapps_INFRASTRUCTURE: " + sagrid_cloudapps_INFRASTRUCTURE
        + "\nsagrid_cloudapps_VONAME: " + sagrid_cloudapps_VONAME
        + "\nsagrid_cloudapps_TOPBDII: " + sagrid_cloudapps_TOPBDII                   
        + "\nsagrid_cloudapps_ETOKENSERVER: " + sagrid_cloudapps_ETOKENSERVER
        + "\nsagrid_cloudapps_MYPROXYSERVER: " + sagrid_cloudapps_MYPROXYSERVER
        + "\nsagrid_cloudapps_PORT: " + sagrid_cloudapps_PORT
        + "\nsagrid_cloudapps_ROBOTID: " + sagrid_cloudapps_ROBOTID
        + "\nsagrid_cloudapps_ROLE: " + sagrid_cloudapps_ROLE
        + "\nsagrid_cloudapps_RENEWAL: " + sagrid_cloudapps_RENEWAL
        + "\nsagrid_cloudapps_DISABLEVOMS: " + sagrid_cloudapps_DISABLEVOMS
                 
        + "\ncloudapps_APPID: " + cloudapps_APPID
        + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
        + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
        + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
        + "\nSMTP Server: " + SMTP_HOST
        + "\nSender: " + SENDER_MAIL);
        }

        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/edit.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        //super.doHelp(request, response);

        response.setContentType("text/html");

        log.info("\nStarting the HELP mode...");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/help.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");

        //java.util.Enumeration listPreferences = portletPreferences.getNames();
        PortletRequestDispatcher dispatcher = null;
        
        String bes_cloudapps_TOPBDII = "";
        String bes_cloudapps_VONAME = "";
        String garuda_cloudapps_TOPBDII = "";
        String garuda_cloudapps_VONAME = "";
        String chain_cloudapps_TOPBDII = "";
        String chain_cloudapps_VONAME = "";
        String fedcloud_cloudapps_TOPBDII = "";
        String fedcloud_cloudapps_VONAME = "";
        String eumed_cloudapps_TOPBDII = "";
        String eumed_cloudapps_VONAME = "";
        String gisela_cloudapps_TOPBDII = "";
        String gisela_cloudapps_VONAME = "";
        String sagrid_cloudapps_TOPBDII = "";
        String sagrid_cloudapps_VONAME = "";
        
        String bes_cloudapps_ENABLEINFRASTRUCTURE="";
        String garuda_cloudapps_ENABLEINFRASTRUCTURE="";
        String chain_cloudapps_ENABLEINFRASTRUCTURE="";
        String fedcloud_cloudapps_ENABLEINFRASTRUCTURE="";
        String eumed_cloudapps_ENABLEINFRASTRUCTURE="";
        String gisela_cloudapps_ENABLEINFRASTRUCTURE="";
        String sagrid_cloudapps_ENABLEINFRASTRUCTURE="";
        String[] infras = new String[7];
                
        String[] cloudapps_INFRASTRUCTURES = 
                portletPreferences.getValues("cloudapps_ENABLEINFRASTRUCTURE", new String[7]);
        
        for (int i=0; i<cloudapps_INFRASTRUCTURES.length; i++) {            
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("bes")) 
                { bes_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n BES!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("garuda")) 
                { garuda_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GARUDA!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("chain")) 
                { chain_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n CHAIN!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("fedcloud")) 
                { fedcloud_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n FEDCLOUD!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("eumed")) 
                { eumed_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n EUMED!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("gisela")) 
                { gisela_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GISELA!"); }
            if (cloudapps_INFRASTRUCTURES[i]!=null && cloudapps_INFRASTRUCTURES[i].equals("sagrid")) 
                { sagrid_cloudapps_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n SAGRID!"); }
        }
                
        // Get the APPID from the portlet preferences
        String cloudapps_APPID = portletPreferences.getValue("cloudapps_APPID", "N/A");
        // Get the LOGLEVEL from the portlet preferences
        String cloudapps_LOGLEVEL = portletPreferences.getValue("cloudapps_LOGLEVEL", "INFO");
        // Get the  APPID from the portlet preferences
        String cloudapps_OUTPUT_PATH = portletPreferences.getValue("cloudapps_OUTPUT_PATH", "N/A");
        // Get the  SOFTWARE from the portlet preferences
        String cloudapps_SOFTWARE = portletPreferences.getValue("cloudapps_SOFTWARE", "N/A");
        // Get the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Get the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Get the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Get the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Get the SENDER_MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        
        if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[0]="bes";
            // Get the  INFRASTRUCTURE from the portlet preferences for the BES VO
            String bes_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("bes_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the BES VO
            bes_cloudapps_VONAME = portletPreferences.getValue("bes_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the BES VO
            bes_cloudapps_TOPBDII = portletPreferences.getValue("bes_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the BES VO
            String[] bes_cloudapps_WMS = portletPreferences.getValues("bes_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the BES VO
            String bes_cloudapps_ETOKENSERVER = portletPreferences.getValue("bes_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the BES VO
            String bes_cloudapps_MYPROXYSERVER = portletPreferences.getValue("bes_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the BES VO
            String bes_cloudapps_PORT = portletPreferences.getValue("bes_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the BES VO
            String bes_cloudapps_ROBOTID = portletPreferences.getValue("chain_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the BES VO
            String bes_cloudapps_ROLE = portletPreferences.getValue("bes_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the BES VO
            String bes_cloudapps_RENEWAL = portletPreferences.getValue("bes_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the BES VO
            String bes_cloudapps_DISABLEVOMS = portletPreferences.getValue("bes_cloudapps_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the BES VO
            String bes_WMS = "";
            if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {
                if (bes_cloudapps_WMS!=null) {
                    //log.info("length="+bes_cloudapps_WMS.length);
                    for (int i = 0; i < bes_cloudapps_WMS.length; i++)
                        if (!(bes_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            bes_WMS += bes_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for BES!"); bes_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("bes_cloudapps_INFRASTRUCTURE", bes_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("bes_cloudapps_VONAME", bes_cloudapps_VONAME.trim());
            request.setAttribute("bes_cloudapps_TOPBDII", bes_cloudapps_TOPBDII.trim());
            request.setAttribute("bes_cloudapps_WMS", bes_WMS);
            request.setAttribute("bes_cloudapps_ETOKENSERVER", bes_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("bes_cloudapps_MYPROXYSERVER", bes_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("bes_cloudapps_PORT", bes_cloudapps_PORT.trim());
            request.setAttribute("bes_cloudapps_ROBOTID", bes_cloudapps_ROBOTID.trim());
            request.setAttribute("bes_cloudapps_ROLE", bes_cloudapps_ROLE.trim());
            request.setAttribute("bes_cloudapps_RENEWAL", bes_cloudapps_RENEWAL);
            request.setAttribute("bes_cloudapps_DISABLEVOMS", bes_cloudapps_DISABLEVOMS);
            
            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[1]="garuda";
            // Get the  INFRASTRUCTURE from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("garuda_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the GARUDA VO
            garuda_cloudapps_VONAME = portletPreferences.getValue("garuda_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the GARUDA VO
            garuda_cloudapps_TOPBDII = portletPreferences.getValue("garuda_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the GARUDA VO
            String[] garuda_cloudapps_WMS = portletPreferences.getValues("garuda_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_ETOKENSERVER = portletPreferences.getValue("garuda_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_MYPROXYSERVER = portletPreferences.getValue("garuda_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_PORT = portletPreferences.getValue("garuda_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_ROBOTID = portletPreferences.getValue("garuda_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_ROLE = portletPreferences.getValue("garuda_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_RENEWAL = portletPreferences.getValue("garuda_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the GARUDA VO
            String garuda_cloudapps_DISABLEVOMS = portletPreferences.getValue("garuda_cloudapps_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the GARUDA VO
            String garuda_WMS = "";
            if (garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (garuda_cloudapps_WMS!=null) {
                    //log.info("length="+garuda_cloudapps_WMS.length);
                    for (int i = 0; i < garuda_cloudapps_WMS.length; i++)
                        if (!(garuda_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            garuda_WMS += garuda_cloudapps_WMS[i] + " ";                        
                } else { log.info("WSGRAM not set for GARUDA!"); garuda_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("garuda_cloudapps_INFRASTRUCTURE", garuda_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("garuda_cloudapps_VONAME", garuda_cloudapps_VONAME.trim());
            request.setAttribute("garuda_cloudapps_TOPBDII", garuda_cloudapps_TOPBDII.trim());
            request.setAttribute("garuda_cloudapps_WMS", garuda_WMS);
            request.setAttribute("garuda_cloudapps_ETOKENSERVER", garuda_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("garuda_cloudapps_MYPROXYSERVER", garuda_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("garuda_cloudapps_PORT", garuda_cloudapps_PORT.trim());
            request.setAttribute("garuda_cloudapps_ROBOTID", garuda_cloudapps_ROBOTID.trim());
            request.setAttribute("garuda_cloudapps_ROLE", garuda_cloudapps_ROLE.trim());
            request.setAttribute("garuda_cloudapps_RENEWAL", garuda_cloudapps_RENEWAL);
            request.setAttribute("garuda_cloudapps_DISABLEVOMS", garuda_cloudapps_DISABLEVOMS);
            
            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[2]="chain";
            // Get the  INFRASTRUCTURE from the portlet preferences for the CHAIN VO
            String chain_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("chain_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the CHAIN VO
            chain_cloudapps_VONAME = portletPreferences.getValue("chain_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the CHAIN VO
            chain_cloudapps_TOPBDII = portletPreferences.getValue("chain_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the CHAIN VO
            String[] chain_cloudapps_WMS = portletPreferences.getValues("chain_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the CHAIN VO
            String chain_cloudapps_ETOKENSERVER = portletPreferences.getValue("chain_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the CHAIN VO
            String chain_cloudapps_MYPROXYSERVER = portletPreferences.getValue("chain_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the CHAIN VO
            String chain_cloudapps_PORT = portletPreferences.getValue("chain_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the CHAIN VO
            String chain_cloudapps_ROBOTID = portletPreferences.getValue("chain_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the CHAIN VO
            String chain_cloudapps_ROLE = portletPreferences.getValue("chain_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the CHAIN VO
            String chain_cloudapps_RENEWAL = portletPreferences.getValue("chain_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the CHAIN VO
            String chain_cloudapps_DISABLEVOMS = portletPreferences.getValue("chain_cloudapps_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the CHAIN VO
            String chain_WMS = "";
            if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (chain_cloudapps_WMS!=null) {
                    //log.info("length="+chain_cloudapps_WMS.length);
                    for (int i = 0; i < chain_cloudapps_WMS.length; i++)
                        if (!(chain_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            chain_WMS += chain_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for CHAIN!"); chain_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("chain_cloudapps_INFRASTRUCTURE", chain_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("chain_cloudapps_VONAME", chain_cloudapps_VONAME.trim());
            request.setAttribute("chain_cloudapps_TOPBDII", chain_cloudapps_TOPBDII.trim());
            request.setAttribute("chain_cloudapps_WMS", chain_WMS);
            request.setAttribute("chain_cloudapps_ETOKENSERVER", chain_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("chain_cloudapps_MYPROXYSERVER", chain_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("chain_cloudapps_PORT", chain_cloudapps_PORT.trim());
            request.setAttribute("chain_cloudapps_ROBOTID", chain_cloudapps_ROBOTID.trim());
            request.setAttribute("chain_cloudapps_ROLE", chain_cloudapps_ROLE.trim());
            request.setAttribute("chain_cloudapps_RENEWAL", chain_cloudapps_RENEWAL);
            request.setAttribute("chain_cloudapps_DISABLEVOMS", chain_cloudapps_DISABLEVOMS);
            
            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[3]="fedcloud";
            // Get the  INFRASTRUCTURE from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("fedcloud_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the FEDCLOUD VO
            fedcloud_cloudapps_VONAME = portletPreferences.getValue("fedcloud_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the FEDCLOUD VO
            fedcloud_cloudapps_TOPBDII = portletPreferences.getValue("fedcloud_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the FEDCLOUD VO
            String[] fedcloud_cloudapps_WMS = portletPreferences.getValues("fedcloud_cloudapps_WMS", new String[10]);
            // Get the  ETOKENSERVER from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_ETOKENSERVER = portletPreferences.getValue("fedcloud_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_MYPROXYSERVER = portletPreferences.getValue("fedcloud_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_PORT = portletPreferences.getValue("fedcloud_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_ROBOTID = portletPreferences.getValue("fedcloud_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_ROLE = portletPreferences.getValue("fedcloud_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_RENEWAL = portletPreferences.getValue("fedcloud_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the FEDCLOUD VO
            String fedcloud_cloudapps_DISABLEVOMS = portletPreferences.getValue("fedcloud_cloudapps_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the FEDCLOUD VO
            String fedcloud_WMS = "";
            if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (fedcloud_cloudapps_WMS!=null) {
                    //log.info("length="+fedcloud_cloudapps_WMS.length);
                    for (int i = 0; i < fedcloud_cloudapps_WMS.length; i++)
                        if (!(fedcloud_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            fedcloud_WMS += fedcloud_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for FEDCLOUD!"); fedcloud_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("fedcloud_cloudapps_INFRASTRUCTURE", fedcloud_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("fedcloud_cloudapps_VONAME", fedcloud_cloudapps_VONAME.trim());
            request.setAttribute("fedcloud_cloudapps_TOPBDII", fedcloud_cloudapps_TOPBDII.trim());
            request.setAttribute("fedcloud_cloudapps_WMS", fedcloud_WMS);
            request.setAttribute("fedcloud_cloudapps_ETOKENSERVER", fedcloud_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("fedcloud_cloudapps_MYPROXYSERVER", fedcloud_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("fedcloud_cloudapps_PORT", fedcloud_cloudapps_PORT.trim());
            request.setAttribute("fedcloud_cloudapps_ROBOTID", fedcloud_cloudapps_ROBOTID.trim());
            request.setAttribute("fedcloud_cloudapps_ROLE", fedcloud_cloudapps_ROLE.trim());
            request.setAttribute("fedcloud_cloudapps_RENEWAL", fedcloud_cloudapps_RENEWAL);
            request.setAttribute("fedcloud_cloudapps_DISABLEVOMS", fedcloud_cloudapps_DISABLEVOMS);

            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[4]="eumed";
            // Get the  INFRASTRUCTURE from the portlet preferences for the EUMED VO
            String eumed_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("eumed_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the EUMED VO
            eumed_cloudapps_VONAME = portletPreferences.getValue("eumed_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the EUMED VO
            eumed_cloudapps_TOPBDII = portletPreferences.getValue("eumed_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the EUMED VO
            String[] eumed_cloudapps_WMS = portletPreferences.getValues("eumed_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the EUMED VO
            String eumed_cloudapps_ETOKENSERVER = portletPreferences.getValue("eumed_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the EUMED VO
            String eumed_cloudapps_MYPROXYSERVER = portletPreferences.getValue("eumed_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the EUMED VO
            String eumed_cloudapps_PORT = portletPreferences.getValue("eumed_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the EUMED VO
            String eumed_cloudapps_ROBOTID = portletPreferences.getValue("eumed_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the EUMED VO
            String eumed_cloudapps_ROLE = portletPreferences.getValue("eumed_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the EUMED VO
            String eumed_cloudapps_RENEWAL = portletPreferences.getValue("eumed_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the EUMED VO
            String eumed_cloudapps_DISABLEVOMS = portletPreferences.getValue("eumed_cloudapps_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the EUMED VO
            String eumed_WMS = "";
            if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (eumed_cloudapps_WMS!=null) {
                    //log.info("length="+eumed_cloudapps_WMS.length);
                    for (int i = 0; i < eumed_cloudapps_WMS.length; i++)
                        if (!(eumed_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            eumed_WMS += eumed_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for EUMED!"); eumed_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("eumed_cloudapps_INFRASTRUCTURE", eumed_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("eumed_cloudapps_VONAME", eumed_cloudapps_VONAME.trim());
            request.setAttribute("eumed_cloudapps_TOPBDII", eumed_cloudapps_TOPBDII.trim());
            request.setAttribute("eumed_cloudapps_WMS", eumed_WMS);
            request.setAttribute("eumed_cloudapps_ETOKENSERVER", eumed_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("eumed_cloudapps_MYPROXYSERVER", eumed_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("eumed_cloudapps_PORT", eumed_cloudapps_PORT.trim());
            request.setAttribute("eumed_cloudapps_ROBOTID", eumed_cloudapps_ROBOTID.trim());
            request.setAttribute("eumed_cloudapps_ROLE", eumed_cloudapps_ROLE.trim());
            request.setAttribute("eumed_cloudapps_RENEWAL", eumed_cloudapps_RENEWAL);
            request.setAttribute("eumed_cloudapps_DISABLEVOMS", eumed_cloudapps_DISABLEVOMS);

            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }

        if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[5]="gisela";
            // Get the  INFRASTRUCTURE from the portlet preferences for the GISELA VO
            String gisela_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("gisela_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the GISELA VO
            gisela_cloudapps_VONAME = portletPreferences.getValue("gisela_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the GISELA VO
            gisela_cloudapps_TOPBDII = portletPreferences.getValue("gisela_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the GISELA VO
            String[] gisela_cloudapps_WMS = portletPreferences.getValues("gisela_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the GISELA VO
            String gisela_cloudapps_ETOKENSERVER = portletPreferences.getValue("gisela_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the GISELA VO
            String gisela_cloudapps_MYPROXYSERVER = portletPreferences.getValue("gisela_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the GISELA VO
            String gisela_cloudapps_PORT = portletPreferences.getValue("gisela_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the GISELA VO
            String gisela_cloudapps_ROBOTID = portletPreferences.getValue("gisela_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the GISELA VO
            String gisela_cloudapps_ROLE = portletPreferences.getValue("gisela_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the GISELA VO
            String gisela_cloudapps_RENEWAL = portletPreferences.getValue("gisela_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the GISELA VO
            String gisela_cloudapps_DISABLEVOMS = portletPreferences.getValue("gisela_cloudapps_DISABLEVOMS", "unchecked");              
            
            // Fetching all the WMS Endpoints for the GISELA VO
            String gisela_WMS = "";
            if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (gisela_cloudapps_WMS!=null) {
                    //log.info("length="+gisela_cloudapps_WMS.length);
                    for (int i = 0; i < gisela_cloudapps_WMS.length; i++)
                        if (!(gisela_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            gisela_WMS += gisela_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for GISELA!"); gisela_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("gisela_cloudapps_INFRASTRUCTURE", gisela_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("gisela_cloudapps_VONAME", gisela_cloudapps_VONAME.trim());
            request.setAttribute("gisela_cloudapps_TOPBDII", gisela_cloudapps_TOPBDII.trim());
            request.setAttribute("gisela_cloudapps_WMS", gisela_WMS);
            request.setAttribute("gisela_cloudapps_ETOKENSERVER", gisela_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("gisela_cloudapps_MYPROXYSERVER", gisela_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("gisela_cloudapps_PORT", gisela_cloudapps_PORT.trim());
            request.setAttribute("gisela_cloudapps_ROBOTID", gisela_cloudapps_ROBOTID.trim());
            request.setAttribute("gisela_cloudapps_ROLE", gisela_cloudapps_ROLE.trim());
            request.setAttribute("gisela_cloudapps_RENEWAL", gisela_cloudapps_RENEWAL);
            request.setAttribute("gisela_cloudapps_DISABLEVOMS", gisela_cloudapps_DISABLEVOMS);

            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[6]="sagrid";
            // Get the  INFRASTRUCTURE from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("sagrid_cloudapps_INFRASTRUCTURE", "N/A");
            // Get the  VONAME from the portlet preferences for the SAGRID VO
            sagrid_cloudapps_VONAME = portletPreferences.getValue("sagrid_cloudapps_VONAME", "N/A");
            // Get the  TOPPBDII from the portlet preferences for the SAGRID VO
            sagrid_cloudapps_TOPBDII = portletPreferences.getValue("sagrid_cloudapps_TOPBDII", "N/A");
            // Get the  WMS from the portlet preferences for the SAGRID VO
            String[] sagrid_cloudapps_WMS = portletPreferences.getValues("sagrid_cloudapps_WMS", new String[5]);
            // Get the  ETOKENSERVER from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_ETOKENSERVER = portletPreferences.getValue("sagrid_cloudapps_ETOKENSERVER", "N/A");
            // Get the  MYPROXYSERVER from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_MYPROXYSERVER = portletPreferences.getValue("sagrid_cloudapps_MYPROXYSERVER", "N/A");
            // Get the  PORT from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_PORT = portletPreferences.getValue("sagrid_cloudapps_PORT", "N/A");
            // Get the  ROBOTID from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_ROBOTID = portletPreferences.getValue("sagrid_cloudapps_ROBOTID", "N/A");
            // Get the  ROLE from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_ROLE = portletPreferences.getValue("sagrid_cloudapps_ROLE", "N/A");
            // Get the  RENEWAL from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_RENEWAL = portletPreferences.getValue("sagrid_cloudapps_RENEWAL", "checked");
            // Get the  DISABLEVOMS from the portlet preferences for the SAGRID VO
            String sagrid_cloudapps_DISABLEVOMS = portletPreferences.getValue("sagrid_cloudapps_DISABLEVOMS", "unchecked");              
            // Fetching all the WMS Endpoints for the SAGRID VO
            String sagrid_WMS = "";
            if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (sagrid_cloudapps_WMS!=null) {
                    //log.info("length="+sagrid_cloudapps_WMS.length);
                    for (int i = 0; i < sagrid_cloudapps_WMS.length; i++)
                        if (!(sagrid_cloudapps_WMS[i].trim().equals("N/A")) ) 
                            sagrid_WMS += sagrid_cloudapps_WMS[i] + " ";                        
                } else { log.info("WMS not set for SAGRID!"); sagrid_cloudapps_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("sagrid_cloudapps_INFRASTRUCTURE", sagrid_cloudapps_INFRASTRUCTURE.trim());
            request.setAttribute("sagrid_cloudapps_VONAME", sagrid_cloudapps_VONAME.trim());
            request.setAttribute("sagrid_cloudapps_TOPBDII", sagrid_cloudapps_TOPBDII.trim());
            request.setAttribute("sagrid_cloudapps_WMS", sagrid_WMS);
            request.setAttribute("sagrid_cloudapps_ETOKENSERVER", sagrid_cloudapps_ETOKENSERVER.trim());
            request.setAttribute("sagrid_cloudapps_MYPROXYSERVER", sagrid_cloudapps_MYPROXYSERVER.trim());
            request.setAttribute("sagrid_cloudapps_PORT", sagrid_cloudapps_PORT.trim());
            request.setAttribute("sagrid_cloudapps_ROBOTID", sagrid_cloudapps_ROBOTID.trim());
            request.setAttribute("sagrid_cloudapps_ROLE", sagrid_cloudapps_ROLE.trim());
            request.setAttribute("sagrid_cloudapps_RENEWAL", sagrid_cloudapps_RENEWAL);
            request.setAttribute("sagrid_cloudapps_DISABLEVOMS", sagrid_cloudapps_DISABLEVOMS);

            //request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);
            request.setAttribute("cloudapps_APPID", cloudapps_APPID.trim());
            request.setAttribute("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
            request.setAttribute("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
            request.setAttribute("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
         // Save in the preferences the list of supported infrastructures 
         request.setAttribute("cloudapps_ENABLEINFRASTRUCTURE", infras);        

         HashMap<String,Properties> GPS_table = new HashMap<String, Properties>();
         HashMap<String,Properties> GPS_queue = new HashMap<String, Properties>();

         // ********************************************************
         List<String> CEqueues_bes = null;                  
         List<String> CEqueues_garuda = null;
         List<String> CEqueues_chain = null;
         List<String> CEqueues_fedcloud = null;
         List<String> CEqueues_eumed = null;
         List<String> CEqueues_gisela = null;
         List<String> CEqueues_sagrid = null;
         
         List<String> CEs_list_bes = null;
         List<String> CEs_list_garuda = null;
         List<String> CEs_list_chain = null;
         List<String> CEs_list_fedcloud = null;
         List<String> CEs_list_eumed = null;
         List<String> CEs_list_gisela = null;
         List<String> CEs_list_sagrid = null;
         
         if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") && 
            (!bes_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<BES>*CLOUD*RESOURCES*-----");
                   
            CEs_list_bes = new ArrayList();
            CEqueues_bes = new ArrayList();
                    
            // Get the CR from the portlet preferences for the BES VO
            String[] bes_cloudapps_WMS = 
                portletPreferences.getValues("bes_cloudapps_WMS", 
                                              new String[5]);
                    
            if (bes_cloudapps_WMS!=null) 
            {
                for (int i = 0; i < bes_cloudapps_WMS.length; i++)
                    if (!(bes_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_bes.add(bes_cloudapps_WMS[i].trim());
                              
                        // Removing "rocci://" prefix from string
                        bes_cloudapps_WMS[i] = 
                            bes_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_bes.add(
                            bes_cloudapps_WMS[i].substring(0,
                            bes_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  bes_cloudapps_WMS[i].substring(0,
                                  bes_cloudapps_WMS[i].indexOf(":")));
                              
                     } else { log.info("Cloud Resource not set for the BES VO!");
                              bes_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                     }                            
            }
         }
                
         //=========================================================
         // IMPORTANT: THIS FIX IS ONLY FOR INSTANCIATE THE 
         //            CHAIN INTEROPERABILITY DEMO                
         //=========================================================
         // ===== ONLY FOR THE CHAIN INTEROPERABILITY DEMO =====
         if (garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") && 
            (!garuda_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<GARUDA>*CLOUD*RESOURCES*-----");
            CEs_list_garuda = new ArrayList();
            CEs_list_garuda.add("xn03.ctsf.cdacb.in");
                    
            CEqueues_garuda = new ArrayList();
            CEqueues_garuda.add("wsgram://xn03.ctsf.cdacb.in:8443/GW");
         }
         // ===== ONLY FOR THE CHAIN INTEROPERABILITY DEMO =====
             
         if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") && 
            (!chain_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<CHAIN>*CLOUD*RESOURCES*-----");
                    
            CEs_list_chain = new ArrayList();
            CEqueues_chain = new ArrayList();
                    
            // Get the CR from the portlet preferences for the CHAIN VO
            String[] chain_cloudapps_WMS = 
                portletPreferences.getValues("chain_cloudapps_WMS", 
                                              new String[5]);
                    
             if (chain_cloudapps_WMS!=null) 
             {
                for (int i = 0; i < chain_cloudapps_WMS.length; i++)
                    if (!(chain_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_chain.add(chain_cloudapps_WMS[i].trim());
                              
                        // Removing "rocci://" prefix from string
                        chain_cloudapps_WMS[i] = 
                            chain_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_chain.add(
                            chain_cloudapps_WMS[i].substring(0,
                            chain_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  chain_cloudapps_WMS[i].substring(0,
                                  chain_cloudapps_WMS[i].indexOf(":")));
                              
                    } else { log.info("Cloud Resource not set for the CHAIN VO!");
                             bes_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                    }                            
             }                    
         }
                
         if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") && 
            (!fedcloud_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<FEDCLOUD>*CLOUD*RESOURCES*-----");
                   
            CEs_list_fedcloud = new ArrayList();
            CEqueues_fedcloud = new ArrayList();
                    
            // Get the CR from the portlet preferences for the FEDCLOUD VO
            String[] fedcloud_cloudapps_WMS = 
                portletPreferences.getValues("fedcloud_cloudapps_WMS", 
                                              new String[10]);
            
            log.info("TOT Cloud Resource = " + fedcloud_cloudapps_WMS.length);
                    
            if (fedcloud_cloudapps_WMS!=null) 
            {
                for (int i = 0; i < fedcloud_cloudapps_WMS.length; i++)
                    if (!(fedcloud_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_fedcloud.add(fedcloud_cloudapps_WMS[i].trim());
                                                                              
                        // Removing "rocci://" prefix from string
                        fedcloud_cloudapps_WMS[i] = 
                            fedcloud_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_fedcloud.add(
                            fedcloud_cloudapps_WMS[i].substring(0,
                            fedcloud_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  fedcloud_cloudapps_WMS[i].substring(0,
                                  fedcloud_cloudapps_WMS[i].indexOf(":")));
                              
                    } else { log.info("Cloud Resource not set for the FEDCLOUD VO!");
                             fedcloud_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                    }                            
            }                    
         }
                
         if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") && 
            (!eumed_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<EUMED>*CLOUD*RESOURCES*-----");
                    
            CEs_list_eumed = new ArrayList();
            CEqueues_eumed = new ArrayList();
                    
            // Get the CR from the portlet preferences for the EUMED VO
            String[] eumed_cloudapps_WMS = 
                portletPreferences.getValues("eumed_cloudapps_WMS", 
                                              new String[5]);
                    
            if (eumed_cloudapps_WMS!=null) 
            {
                for (int i = 0; i < eumed_cloudapps_WMS.length; i++)
                    if (!(eumed_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_eumed.add(eumed_cloudapps_WMS[i].trim());                                                
                              
                        // Removing "rocci://" prefix from string
                        eumed_cloudapps_WMS[i] = 
                            eumed_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_eumed.add(
                            eumed_cloudapps_WMS[i].substring(0,
                            eumed_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  eumed_cloudapps_WMS[i].substring(0,
                                  eumed_cloudapps_WMS[i].indexOf(":")));
                              
                     } else { log.info("Cloud Resource not set for the EUMED VO!");
                              eumed_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                     }                            
            }                    
         }
                
         if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") &&
            (!gisela_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<GISELA>*CLOUD*RESOURCES*-----");
                    
            CEs_list_gisela = new ArrayList();
            CEqueues_gisela = new ArrayList();
                    
            // Get the CR from the portlet preferences for the GISELA VO
            String[] gisela_cloudapps_WMS = 
                portletPreferences.getValues("gisela_cloudapps_WMS", 
                                             new String[5]);
                    
             if (gisela_cloudapps_WMS!=null) 
             {
                for (int i = 0; i < gisela_cloudapps_WMS.length; i++)
                    if (!(gisela_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_gisela.add(gisela_cloudapps_WMS[i].trim());
                              
                        // Removing "rocci://" prefix from string
                        gisela_cloudapps_WMS[i] = 
                            gisela_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_gisela.add(
                            gisela_cloudapps_WMS[i].substring(0,
                            gisela_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  gisela_cloudapps_WMS[i].substring(0,
                                  gisela_cloudapps_WMS[i].indexOf(":")));
                              
                    } else { log.info("Cloud Resource not set for the GISELA VO!");
                             fedcloud_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                    }                            
             }                    
         }
                
         if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked") &&
            (!sagrid_cloudapps_TOPBDII.equals("N/A")) ) 
         {
            log.info("-----*FETCHING*THE*<SAGRID>*CLOUD*RESOURCES*-----");
                    
            CEs_list_sagrid = new ArrayList();
            CEqueues_sagrid = new ArrayList();
                    
            // Get the CR from the portlet preferences for the SAGRID VO
            String[] sagrid_cloudapps_WMS = 
                portletPreferences.getValues("sagrid_cloudapps_WMS", 
                                              new String[5]);
                    
            if (sagrid_cloudapps_WMS!=null) 
            {
                for (int i = 0; i < sagrid_cloudapps_WMS.length; i++)
                    if (!(sagrid_cloudapps_WMS[i].trim().equals("N/A")) )
                    {
                        CEqueues_sagrid.add(sagrid_cloudapps_WMS[i].trim());
                              
                        // Removing "rocci://" prefix from string
                        sagrid_cloudapps_WMS[i] = 
                            sagrid_cloudapps_WMS[i].replace("rocci://", "");
                              
                        // Removing port from string
                        CEs_list_sagrid.add(
                            sagrid_cloudapps_WMS[i].substring(0,
                            sagrid_cloudapps_WMS[i].indexOf(":"))
                        );
                        
                        log.info("Cloud Resource = " + 
                                  sagrid_cloudapps_WMS[i].substring(0,
                                  sagrid_cloudapps_WMS[i].indexOf(":")));
                              
                    } else { log.info("Cloud Resource not set for the SAGRID VO!");
                             fedcloud_cloudapps_ENABLEINFRASTRUCTURE="unchecked";
                    }                            
            }                    
         }
                
         // Merging the list of CEs and queues
         List<String> CEs_list_TOT = new ArrayList<String>();
         
         if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_bes);
         if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_chain);
         // ===== ONLY FOR THE CHAIN INTEROPERABILITY DEMO =====
         if (garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_garuda);
         // ===== ONLY FOR THE CHAIN INTEROPERABILITY DEMO =====
         if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_fedcloud);
         if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_eumed);
         if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_gisela);
         if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_list_TOT.addAll(CEs_list_sagrid);
                                
         List<String> CEs_queue_TOT = new ArrayList<String>();
         
         if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_bes);
         if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_chain);
         if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_fedcloud);                
         if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_eumed);
         if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_gisela);
         if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            CEs_queue_TOT.addAll(CEqueues_sagrid); 
                
         //=========================================================
         // IMPORTANT: INSTANCIATE THE UsersTrackingDBInterface
         //            CLASS USING THE EMPTY CONSTRUCTOR WHEN
         //            WHEN THE PORTLET IS DEPLOYED IN PRODUCTION!!!
         //=========================================================
         UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface(
                TRACKING_DB_HOSTNAME.trim(),
                TRACKING_DB_USERNAME.trim(),
                TRACKING_DB_PASSWORD.trim());
                
         /*UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface();*/
                    
         if ( (CEs_list_TOT != null) && (!CEs_queue_TOT.isEmpty()) )
         {
            log.info("NOT EMPTY LIST!");
            // Fetching the list of CEs publushing the SW
            for (String CE:CEs_list_TOT) 
            {
                //log.info("Fetching the CE="+CE);
                Properties coordinates = new Properties();
                Properties queue = new Properties();

                float coords[] = DBInterface.getCECoordinate(CE);                        

                String GPS_LAT = Float.toString(coords[0]);
                String GPS_LNG = Float.toString(coords[1]);

                coordinates.setProperty("LAT", GPS_LAT);
                coordinates.setProperty("LNG", GPS_LNG);
                log.info("Fetching CE settings for [ " + CE + 
                         " ] Coordinates [ " + GPS_LAT + 
                         ", " + GPS_LNG + " ]");

                // Fetching the Queues
                for (String CEqueue:CEs_queue_TOT) {
                    if (CEqueue.contains(CE))
                        queue.setProperty("QUEUE", CEqueue);
                 }

                 // Saving the GPS location in a Java HashMap
                 GPS_table.put(CE, coordinates);

                 // Saving the queue in a Java HashMap
                 GPS_queue.put(CE, queue);                                  
            }
         } else log.info ("EMPTY LIST!");
             
         // Checking the HashMap
         Set set = GPS_table.entrySet();
         Iterator iter = set.iterator();
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - GPS location of the CE " +
                       entry.getKey() + " => " + entry.getValue());
         }                  

         // Checking the HashMap
         set = GPS_queue.entrySet();
         iter = set.iterator();
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - Queue " +
                       entry.getKey() + " => " + entry.getValue());
         }
         
         Gson gson = new GsonBuilder().create();
         request.setAttribute ("GPS_table", gson.toJson(GPS_table));
         request.setAttribute ("GPS_queue", gson.toJson(GPS_queue));

         // ********************************************************
         dispatcher = getPortletContext().getRequestDispatcher("/view.jsp");       
         dispatcher.include(request, response);         
    }

    // The init method will be called when installing for the first time the portlet
    // This is the right time to setup the default values into the preferences
    @Override
    public void init() throws PortletException {
        super.init();
    }

    @Override
    public void processAction(ActionRequest request,
                              ActionResponse response)
                throws PortletException, IOException {

        String action = "";

        // Get the action to be processed from the request
        action = request.getParameter("ActionEvent");

        // Determine the username and the email address
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);        
        User user = themeDisplay.getUser();
        String username = user.getScreenName();
        String emailAddress = user.getDisplayEmailAddress();        

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();                
        
        if (action.equals("CONFIG_CLOUDAPPS_PORTLET")) {
            log.info("\nPROCESS ACTION => " + action);
            
            // Get the APPID from the portlet request
            String cloudapps_APPID = request.getParameter("cloudapps_APPID");
            // Get the LOGLEVEL from the portlet request
            String cloudapps_LOGLEVEL = request.getParameter("cloudapps_LOGLEVEL");
            // Get the OUTPUT from the portlet request
            String cloudapps_OUTPUT_PATH = request.getParameter("cloudapps_OUTPUT_PATH");
            // Get the SOFTWARE from the portlet request
            String cloudapps_SOFTWARE = request.getParameter("cloudapps_SOFTWARE");
            // Get the TRACKING_DB_HOSTNAME from the portlet request
            String TRACKING_DB_HOSTNAME = request.getParameter("TRACKING_DB_HOSTNAME");
            // Get the TRACKING_DB_USERNAME from the portlet request
            String TRACKING_DB_USERNAME = request.getParameter("TRACKING_DB_USERNAME");
            // Get the TRACKING_DB_PASSWORD from the portlet request
            String TRACKING_DB_PASSWORD = request.getParameter("TRACKING_DB_PASSWORD");
            // Get the SMTP_HOST from the portlet request
            String SMTP_HOST = request.getParameter("SMTP_HOST");
            // Get the SENDER_MAIL from the portlet request
            String SENDER_MAIL = request.getParameter("SENDER_MAIL");
            String[] infras = new String[7];
            
            String bes_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String garuda_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String chain_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String fedcloud_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String eumed_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String gisela_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
            String sagrid_cloudapps_ENABLEINFRASTRUCTURE = "unchecked";
        
            String[] cloudapps_INFRASTRUCTURES = request.getParameterValues("cloudapps_ENABLEINFRASTRUCTURES");
        
            if (cloudapps_INFRASTRUCTURES != null) {
                Arrays.sort(cloudapps_INFRASTRUCTURES);                    
                bes_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "bes") >= 0 ? "checked" : "unchecked";
                garuda_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "garuda") >= 0 ? "checked" : "unchecked";
                chain_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "chain") >= 0 ? "checked" : "unchecked";
                fedcloud_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "fedcloud") >= 0 ? "checked" : "unchecked";
                eumed_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "eumed") >= 0 ? "checked" : "unchecked";
                gisela_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "gisela") >= 0 ? "checked" : "unchecked";
                sagrid_cloudapps_ENABLEINFRASTRUCTURE = 
                    Arrays.binarySearch(cloudapps_INFRASTRUCTURES, "sagrid") >= 0 ? "checked" : "unchecked";
            }
        
            if (bes_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[0]="bes";
                // Get the  INFRASTRUCTURE from the portlet request for the BES VO
                String bes_cloudapps_INFRASTRUCTURE = request.getParameter("bes_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the BES VO
                String bes_cloudapps_VONAME = request.getParameter("bes_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the BES VO
                String bes_cloudapps_TOPBDII = request.getParameter("bes_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the BES VO
                String[] bes_cloudapps_WMS = request.getParameterValues("bes_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the BES VO
                String bes_cloudapps_ETOKENSERVER = request.getParameter("bes_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the BES VO
                String bes_cloudapps_MYPROXYSERVER = request.getParameter("bes_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the BES VO
                String bes_cloudapps_PORT = request.getParameter("bes_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the BES VO
                String bes_cloudapps_ROBOTID = request.getParameter("bes_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the BES VO
                String bes_cloudapps_ROLE = request.getParameter("bes_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the BES VO
                String[] bes_cloudapps_OPTIONS = request.getParameterValues("bes_cloudapps_OPTIONS");                

                String bes_cloudapps_RENEWAL = "";
                String bes_cloudapps_DISABLEVOMS = "";

                if (bes_cloudapps_OPTIONS == null){
                    bes_cloudapps_RENEWAL = "checked";
                    bes_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(bes_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the BES VO
                    bes_cloudapps_RENEWAL = Arrays.binarySearch(bes_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the BES VO
                    bes_cloudapps_DISABLEVOMS = Arrays.binarySearch(bes_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }
                
                int nmax=0;                
                for (int i = 0; i < bes_cloudapps_WMS.length; i++)
                    if ( bes_cloudapps_WMS[i]!=null && (!bes_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] bes_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    bes_cloudapps_WMS_trimmed[i]=bes_cloudapps_WMS[i].trim();
                    log.info ("\n\nBES [" + i + "] WMS=[" + bes_cloudapps_WMS_trimmed[i] + "]");
                }
                
                // Set the portlet preferences
                portletPreferences.setValue("bes_cloudapps_INFRASTRUCTURE", bes_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("bes_cloudapps_VONAME", bes_cloudapps_VONAME.trim());
                portletPreferences.setValue("bes_cloudapps_TOPBDII", bes_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("bes_cloudapps_WMS", bes_cloudapps_WMS_trimmed);
                portletPreferences.setValue("bes_cloudapps_ETOKENSERVER", bes_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("bes_cloudapps_MYPROXYSERVER", bes_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("bes_cloudapps_PORT", bes_cloudapps_PORT.trim());
                portletPreferences.setValue("bes_cloudapps_ROBOTID", bes_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("bes_cloudapps_ROLE", bes_cloudapps_ROLE.trim());
                portletPreferences.setValue("bes_cloudapps_RENEWAL", bes_cloudapps_RENEWAL);
                portletPreferences.setValue("bes_cloudapps_DISABLEVOMS", bes_cloudapps_DISABLEVOMS);                
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."
                    + "\nbes_cloudapps_INFRASTRUCTURE: " + bes_cloudapps_INFRASTRUCTURE
                    + "\nbes_cloudapps_VONAME: " + bes_cloudapps_VONAME
                    + "\nbes_cloudapps_TOPBDII: " + bes_cloudapps_TOPBDII                    
                    + "\nbes_cloudapps_ETOKENSERVER: " + bes_cloudapps_ETOKENSERVER
                    + "\nbes_cloudapps_MYPROXYSERVER: " + bes_cloudapps_MYPROXYSERVER
                    + "\nbes_cloudapps_PORT: " + bes_cloudapps_PORT
                    + "\nbes_cloudapps_ROBOTID: " + bes_cloudapps_ROBOTID
                    + "\nbes_cloudapps_ROLE: " + bes_cloudapps_ROLE
                    + "\nbes_cloudapps_RENEWAL: " + bes_cloudapps_RENEWAL
                    + "\nbes_cloudapps_DISABLEVOMS: " + bes_cloudapps_DISABLEVOMS
                        
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "bes"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }
            
            if (garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[1]="garuda";
                // Get the  INFRASTRUCTURE from the portlet request for the GARUDA VO
                String garuda_cloudapps_INFRASTRUCTURE = request.getParameter("garuda_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the GARUDA VO
                String garuda_cloudapps_VONAME = request.getParameter("garuda_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the GARUDA VO
                String garuda_cloudapps_TOPBDII = request.getParameter("garuda_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the GARUDA VO
                String[] garuda_cloudapps_WMS = request.getParameterValues("garuda_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the GARUDA VO
                String garuda_cloudapps_ETOKENSERVER = request.getParameter("garuda_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the GARUDA VO
                String garuda_cloudapps_MYPROXYSERVER = request.getParameter("garuda_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the GARUDA VO
                String garuda_cloudapps_PORT = request.getParameter("garuda_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the GARUDA VO
                String garuda_cloudapps_ROBOTID = request.getParameter("garuda_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the GARUDA VO
                String garuda_cloudapps_ROLE = request.getParameter("garuda_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the GARUDA VO
                String[] garuda_cloudapps_OPTIONS = request.getParameterValues("garuda_cloudapps_OPTIONS"); 

                String garuda_cloudapps_RENEWAL = "";
                String garuda_cloudapps_DISABLEVOMS = "";

                if (garuda_cloudapps_OPTIONS == null){
                    garuda_cloudapps_RENEWAL = "checked";
                    garuda_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(garuda_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the GARUDA VO
                    garuda_cloudapps_RENEWAL = Arrays.binarySearch(garuda_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the GARUDA VO
                    garuda_cloudapps_DISABLEVOMS = Arrays.binarySearch(garuda_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }
                
                int nmax=0;                
                for (int i = 0; i < garuda_cloudapps_WMS.length; i++)
                    if ( garuda_cloudapps_WMS[i]!=null && (!garuda_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] garuda_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    garuda_cloudapps_WMS_trimmed[i]=garuda_cloudapps_WMS[i].trim();
                    log.info ("\n\nGARUDA [" + i + "] WSGRAM=[" + garuda_cloudapps_WMS_trimmed[i] + "]");
                }
                
                // Set the portlet preferences
                portletPreferences.setValue("garuda_cloudapps_INFRASTRUCTURE", garuda_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("garuda_cloudapps_VONAME", garuda_cloudapps_VONAME.trim());
                portletPreferences.setValue("garuda_cloudapps_TOPBDII", garuda_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("garuda_cloudapps_WMS", garuda_cloudapps_WMS_trimmed);
                portletPreferences.setValue("garuda_cloudapps_ETOKENSERVER", garuda_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("garuda_cloudapps_MYPROXYSERVER", garuda_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("garuda_cloudapps_PORT", garuda_cloudapps_PORT.trim());
                portletPreferences.setValue("garuda_cloudapps_ROBOTID", garuda_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("garuda_cloudapps_ROLE", garuda_cloudapps_ROLE.trim());
                portletPreferences.setValue("garuda_cloudapps_RENEWAL", garuda_cloudapps_RENEWAL);
                portletPreferences.setValue("garuda_cloudapps_DISABLEVOMS", garuda_cloudapps_DISABLEVOMS);
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."
                    + "\ngaruda_cloudapps_INFRASTRUCTURE: " + garuda_cloudapps_INFRASTRUCTURE
                    + "\ngaruda_cloudapps_VONAME: " + garuda_cloudapps_VONAME
                    + "\ngaruda_cloudapps_TOPBDII: " + garuda_cloudapps_TOPBDII                    
                    + "\ngaruda_cloudapps_ETOKENSERVER: " + garuda_cloudapps_ETOKENSERVER
                    + "\ngaruda_cloudapps_MYPROXYSERVER: " + garuda_cloudapps_MYPROXYSERVER
                    + "\ngaruda_cloudapps_PORT: " + garuda_cloudapps_PORT
                    + "\ngaruda_cloudapps_ROBOTID: " + garuda_cloudapps_ROBOTID
                    + "\ngaruda_cloudapps_ROLE: " + garuda_cloudapps_ROLE
                    + "\ngaruda_cloudapps_RENEWAL: " + garuda_cloudapps_RENEWAL
                    + "\ngaruda_cloudapps_DISABLEVOMS: " + garuda_cloudapps_DISABLEVOMS
                            
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "garuda"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }

            if (chain_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[2]="chain";
                // Get the  INFRASTRUCTURE from the portlet request for the CHAIN VO
                String chain_cloudapps_INFRASTRUCTURE = request.getParameter("chain_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the CHAIN VO
                String chain_cloudapps_VONAME = request.getParameter("chain_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the CHAIN VO
                String chain_cloudapps_TOPBDII = request.getParameter("chain_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the CHAIN VO
                String[] chain_cloudapps_WMS = request.getParameterValues("chain_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the CHAIN VO
                String chain_cloudapps_ETOKENSERVER = request.getParameter("chain_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the CHAIN VO
                String chain_cloudapps_MYPROXYSERVER = request.getParameter("chain_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the CHAIN VO
                String chain_cloudapps_PORT = request.getParameter("chain_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the CHAIN VO
                String chain_cloudapps_ROBOTID = request.getParameter("chain_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the CHAIN VO
                String chain_cloudapps_ROLE = request.getParameter("chain_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the CHAIN VO
                String[] chain_cloudapps_OPTIONS = request.getParameterValues("chain_cloudapps_OPTIONS");                

                String chain_cloudapps_RENEWAL = "";
                String chain_cloudapps_DISABLEVOMS = "";

                if (chain_cloudapps_OPTIONS == null){
                    chain_cloudapps_RENEWAL = "checked";
                    chain_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(chain_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the CHAIN VO
                    chain_cloudapps_RENEWAL = Arrays.binarySearch(chain_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the CHAIN VO
                    chain_cloudapps_DISABLEVOMS = Arrays.binarySearch(chain_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }
                
                int nmax=0;                
                for (int i = 0; i < chain_cloudapps_WMS.length; i++)
                    if ( chain_cloudapps_WMS[i]!=null && (!chain_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] chain_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    chain_cloudapps_WMS_trimmed[i]=chain_cloudapps_WMS[i].trim();
                    log.info ("\n\nCHAIN [" + i + "] WMS=[" + chain_cloudapps_WMS_trimmed[i] + "]");
                }
                
                // Set the portlet preferences
                portletPreferences.setValue("chain_cloudapps_INFRASTRUCTURE", chain_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("chain_cloudapps_VONAME", chain_cloudapps_VONAME.trim());
                portletPreferences.setValue("chain_cloudapps_TOPBDII", chain_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("chain_cloudapps_WMS", chain_cloudapps_WMS_trimmed);
                portletPreferences.setValue("chain_cloudapps_ETOKENSERVER", chain_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("chain_cloudapps_MYPROXYSERVER", chain_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("chain_cloudapps_PORT", chain_cloudapps_PORT.trim());
                portletPreferences.setValue("chain_cloudapps_ROBOTID", chain_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("chain_cloudapps_ROLE", chain_cloudapps_ROLE.trim());
                portletPreferences.setValue("chain_cloudapps_RENEWAL", chain_cloudapps_RENEWAL);
                portletPreferences.setValue("chain_cloudapps_DISABLEVOMS", chain_cloudapps_DISABLEVOMS);                
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."
                    + "\nchain_cloudapps_INFRASTRUCTURE: " + chain_cloudapps_INFRASTRUCTURE
                    + "\nchain_cloudapps_VONAME: " + chain_cloudapps_VONAME
                    + "\nchain_cloudapps_TOPBDII: " + chain_cloudapps_TOPBDII                    
                    + "\nchain_cloudapps_ETOKENSERVER: " + chain_cloudapps_ETOKENSERVER
                    + "\nchain_cloudapps_MYPROXYSERVER: " + chain_cloudapps_MYPROXYSERVER
                    + "\nchain_cloudapps_PORT: " + chain_cloudapps_PORT
                    + "\nchain_cloudapps_ROBOTID: " + chain_cloudapps_ROBOTID
                    + "\nchain_cloudapps_ROLE: " + chain_cloudapps_ROLE
                    + "\nchain_cloudapps_RENEWAL: " + chain_cloudapps_RENEWAL
                    + "\nchain_cloudapps_DISABLEVOMS: " + chain_cloudapps_DISABLEVOMS
                        
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "chain"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }
            
            if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[3]="fedcloud";
                // Get the  INFRASTRUCTURE from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_INFRASTRUCTURE = request.getParameter("fedcloud_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_VONAME = request.getParameter("fedcloud_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_TOPBDII = request.getParameter("fedcloud_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the FEDCLOUD VO
                String[] fedcloud_cloudapps_WMS = request.getParameterValues("fedcloud_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_ETOKENSERVER = request.getParameter("fedcloud_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_MYPROXYSERVER = request.getParameter("fedcloud_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_PORT = request.getParameter("fedcloud_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_ROBOTID = request.getParameter("fedcloud_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the FEDCLOUD VO
                String fedcloud_cloudapps_ROLE = request.getParameter("fedcloud_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the FEDCLOUD VO
                String[] fedcloud_cloudapps_OPTIONS = request.getParameterValues("fedcloud_cloudapps_OPTIONS");                

                String fedcloud_cloudapps_RENEWAL = "";
                String fedcloud_cloudapps_DISABLEVOMS = "";

                if (fedcloud_cloudapps_OPTIONS == null){
                    fedcloud_cloudapps_RENEWAL = "checked";
                    fedcloud_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(fedcloud_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the FEDCLOUD VO
                    fedcloud_cloudapps_RENEWAL = Arrays.binarySearch(fedcloud_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the FEDCLOUD VO
                    fedcloud_cloudapps_DISABLEVOMS = Arrays.binarySearch(fedcloud_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }
                
                int nmax=0;                
                for (int i = 0; i < fedcloud_cloudapps_WMS.length; i++)
                    if ( fedcloud_cloudapps_WMS[i]!=null && (!fedcloud_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] fedcloud_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    fedcloud_cloudapps_WMS_trimmed[i]=fedcloud_cloudapps_WMS[i].trim();
                    log.info ("\n\nFEDCLOUD [" + i + "] Cloud Resource=[" + fedcloud_cloudapps_WMS_trimmed[i] + "]");
                }
                
                // Set the portlet preferences
                portletPreferences.setValue("fedcloud_cloudapps_INFRASTRUCTURE", fedcloud_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("fedcloud_cloudapps_VONAME", fedcloud_cloudapps_VONAME.trim());
                portletPreferences.setValue("fedcloud_cloudapps_TOPBDII", fedcloud_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("fedcloud_cloudapps_WMS", fedcloud_cloudapps_WMS_trimmed);
                portletPreferences.setValue("fedcloud_cloudapps_ETOKENSERVER", fedcloud_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("fedcloud_cloudapps_MYPROXYSERVER", fedcloud_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("fedcloud_cloudapps_PORT", fedcloud_cloudapps_PORT.trim());
                portletPreferences.setValue("fedcloud_cloudapps_ROBOTID", fedcloud_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("fedcloud_cloudapps_ROLE", fedcloud_cloudapps_ROLE.trim());
                portletPreferences.setValue("fedcloud_cloudapps_RENEWAL", fedcloud_cloudapps_RENEWAL);
                portletPreferences.setValue("fedcloud_cloudapps_DISABLEVOMS", fedcloud_cloudapps_DISABLEVOMS);
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."                    
                    + "\n\nfedcloud_cloudapps_INFRASTRUCTURE: " + fedcloud_cloudapps_INFRASTRUCTURE
                    + "\nfedcloud_cloudapps_VONAME: " + fedcloud_cloudapps_VONAME
                    + "\nfedcloud_cloudapps_TOPBDII: " + fedcloud_cloudapps_TOPBDII                    
                    + "\nfedcloud_cloudapps_ETOKENSERVER: " + fedcloud_cloudapps_ETOKENSERVER
                    + "\nfedcloud_cloudapps_MYPROXYSERVER: " + fedcloud_cloudapps_MYPROXYSERVER
                    + "\nfedcloud_cloudapps_PORT: " + fedcloud_cloudapps_PORT
                    + "\nfedcloud_cloudapps_ROBOTID: " + fedcloud_cloudapps_ROBOTID
                    + "\nfedcloud_cloudapps_ROLE: " + fedcloud_cloudapps_ROLE
                    + "\nfedcloud_cloudapps_RENEWAL: " + fedcloud_cloudapps_RENEWAL
                    + "\nfedcloud_cloudapps_DISABLEVOMS: " + fedcloud_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "fedcloud"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }

            if (eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[4]="eumed";
                // Get the  INFRASTRUCTURE from the portlet request for the EUMED VO
                String eumed_cloudapps_INFRASTRUCTURE = request.getParameter("eumed_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the EUMED VO
                String eumed_cloudapps_VONAME = request.getParameter("eumed_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the EUMED VO
                String eumed_cloudapps_TOPBDII = request.getParameter("eumed_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the EUMED VO
                String[] eumed_cloudapps_WMS = request.getParameterValues("eumed_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the EUMED VO
                String eumed_cloudapps_ETOKENSERVER = request.getParameter("eumed_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the EUMED VO
                String eumed_cloudapps_MYPROXYSERVER = request.getParameter("eumed_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the EUMED VO
                String eumed_cloudapps_PORT = request.getParameter("eumed_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the EUMED VO
                String eumed_cloudapps_ROBOTID = request.getParameter("eumed_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the EUMED VO
                String eumed_cloudapps_ROLE = request.getParameter("eumed_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the EUMED VO
                String[] eumed_cloudapps_OPTIONS = request.getParameterValues("eumed_cloudapps_OPTIONS");                

                String eumed_cloudapps_RENEWAL = "";
                String eumed_cloudapps_DISABLEVOMS = "";

                if (eumed_cloudapps_OPTIONS == null){
                    eumed_cloudapps_RENEWAL = "checked";
                    eumed_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(eumed_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the EUMED VO
                    eumed_cloudapps_RENEWAL = Arrays.binarySearch(eumed_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the CHAIN VO
                    eumed_cloudapps_DISABLEVOMS = Arrays.binarySearch(eumed_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }
                
                int nmax=0;                
                for (int i = 0; i < eumed_cloudapps_WMS.length; i++)
                    if ( eumed_cloudapps_WMS[i]!=null && (!eumed_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] eumed_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    eumed_cloudapps_WMS_trimmed[i]=eumed_cloudapps_WMS[i].trim();
                    log.info ("\n\nEUMED [" + i + "] WMS=[" + eumed_cloudapps_WMS_trimmed[i] + "]");
                }
                
                // Set the portlet preferences
                portletPreferences.setValue("eumed_cloudapps_INFRASTRUCTURE", eumed_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("eumed_cloudapps_VONAME", eumed_cloudapps_VONAME.trim());
                portletPreferences.setValue("eumed_cloudapps_TOPBDII", eumed_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("eumed_cloudapps_WMS", eumed_cloudapps_WMS_trimmed);
                portletPreferences.setValue("eumed_cloudapps_ETOKENSERVER", eumed_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("eumed_cloudapps_MYPROXYSERVER", eumed_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("eumed_cloudapps_PORT", eumed_cloudapps_PORT.trim());
                portletPreferences.setValue("eumed_cloudapps_ROBOTID", eumed_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("eumed_cloudapps_ROLE", eumed_cloudapps_ROLE.trim());
                portletPreferences.setValue("eumed_cloudapps_RENEWAL", eumed_cloudapps_RENEWAL);
                portletPreferences.setValue("eumed_cloudapps_DISABLEVOMS", eumed_cloudapps_DISABLEVOMS); 
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."                    
                    + "\n\neumed_cloudapps_INFRASTRUCTURE: " + eumed_cloudapps_INFRASTRUCTURE
                    + "\neumed_cloudapps_VONAME: " + eumed_cloudapps_VONAME
                    + "\neumed_cloudapps_TOPBDII: " + eumed_cloudapps_TOPBDII                    
                    + "\neumed_cloudapps_ETOKENSERVER: " + eumed_cloudapps_ETOKENSERVER
                    + "\neumed_cloudapps_MYPROXYSERVER: " + eumed_cloudapps_MYPROXYSERVER
                    + "\neumed_cloudapps_PORT: " + eumed_cloudapps_PORT
                    + "\neumed_cloudapps_ROBOTID: " + eumed_cloudapps_ROBOTID
                    + "\neumed_cloudapps_ROLE: " + eumed_cloudapps_ROLE
                    + "\neumed_cloudapps_RENEWAL: " + eumed_cloudapps_RENEWAL
                    + "\neumed_cloudapps_DISABLEVOMS: " + eumed_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "eumed"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }

            if (gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[5]="gisela";
                // Get the  INFRASTRUCTURE from the portlet request for the GISELA VO
                String gisela_cloudapps_INFRASTRUCTURE = request.getParameter("gisela_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the GISELA VO
                String gisela_cloudapps_VONAME = request.getParameter("gisela_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the GISELA VO
                String gisela_cloudapps_TOPBDII = request.getParameter("gisela_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the GISELA VO
                String[] gisela_cloudapps_WMS = request.getParameterValues("gisela_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the GISELA VO
                String gisela_cloudapps_ETOKENSERVER = request.getParameter("gisela_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the GISELA VO
                String gisela_cloudapps_MYPROXYSERVER = request.getParameter("gisela_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the GISELA VO
                String gisela_cloudapps_PORT = request.getParameter("gisela_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the GISELA VO
                String gisela_cloudapps_ROBOTID = request.getParameter("gisela_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the GISELA VO
                String gisela_cloudapps_ROLE = request.getParameter("gisela_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the GISELA VO
                String[] gisela_cloudapps_OPTIONS = request.getParameterValues("gisela_cloudapps_OPTIONS");                

                String gisela_cloudapps_RENEWAL = "";
                String gisela_cloudapps_DISABLEVOMS = "";

                if (gisela_cloudapps_OPTIONS == null){
                    gisela_cloudapps_RENEWAL = "checked";
                    gisela_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(gisela_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the GISELA VO
                    gisela_cloudapps_RENEWAL = Arrays.binarySearch(gisela_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the GISELA VO
                    gisela_cloudapps_DISABLEVOMS = Arrays.binarySearch(gisela_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }                       

                int nmax=0;                
                for (int i = 0; i < gisela_cloudapps_WMS.length; i++)
                    if ( gisela_cloudapps_WMS[i]!=null && (!gisela_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] gisela_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    gisela_cloudapps_WMS_trimmed[i]=gisela_cloudapps_WMS[i].trim();
                    log.info ("\n\nGISELA [" + i + "] WMS=[" + gisela_cloudapps_WMS_trimmed[i] + "]");
                }

                // Set the portlet preferences
                portletPreferences.setValue("gisela_cloudapps_INFRASTRUCTURE", gisela_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("gisela_cloudapps_VONAME", gisela_cloudapps_VONAME.trim());
                portletPreferences.setValue("gisela_cloudapps_TOPBDII", gisela_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("gisela_cloudapps_WMS", gisela_cloudapps_WMS_trimmed);
                portletPreferences.setValue("gisela_cloudapps_ETOKENSERVER", gisela_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("gisela_cloudapps_MYPROXYSERVER", gisela_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("gisela_cloudapps_PORT", gisela_cloudapps_PORT.trim());
                portletPreferences.setValue("gisela_cloudapps_ROBOTID", gisela_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("gisela_cloudapps_ROLE", gisela_cloudapps_ROLE.trim());
                portletPreferences.setValue("gisela_cloudapps_RENEWAL", gisela_cloudapps_RENEWAL);
                portletPreferences.setValue("gisela_cloudapps_DISABLEVOMS", gisela_cloudapps_DISABLEVOMS);
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."                    
                    + "\n\ngisela_cloudapps_INFRASTRUCTURE: " + gisela_cloudapps_INFRASTRUCTURE
                    + "\ngisela_cloudapps_VONAME: " + gisela_cloudapps_VONAME
                    + "\ngisela_cloudapps_TOPBDII: " + gisela_cloudapps_TOPBDII                    
                    + "\ngisela_cloudapps_ETOKENSERVER: " + gisela_cloudapps_ETOKENSERVER
                    + "\ngisela_cloudapps_MYPROXYSERVER: " + gisela_cloudapps_MYPROXYSERVER
                    + "\ngisela_cloudapps_PORT: " + gisela_cloudapps_PORT
                    + "\ngisela_cloudapps_ROBOTID: " + gisela_cloudapps_ROBOTID
                    + "\ngisela_cloudapps_ROLE: " + gisela_cloudapps_ROLE
                    + "\ngisela_cloudapps_RENEWAL: " + gisela_cloudapps_RENEWAL
                    + "\ngisela_cloudapps_DISABLEVOMS: " + gisela_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "gisela"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }
            
            if (sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("checked"))
            {
                infras[6]="sagrid";
                // Get the  INFRASTRUCTURE from the portlet request for the SAGRID VO
                String sagrid_cloudapps_INFRASTRUCTURE = request.getParameter("sagrid_cloudapps_INFRASTRUCTURE");
                // Get the  VONAME from the portlet request for the SAGRID VO
                String sagrid_cloudapps_VONAME = request.getParameter("sagrid_cloudapps_VONAME");
                // Get the  TOPBDII from the portlet request for the SAGRID VO
                String sagrid_cloudapps_TOPBDII = request.getParameter("sagrid_cloudapps_TOPBDII");
                // Get the  WMS from the portlet request for the SAGRID VO
                String[] sagrid_cloudapps_WMS = request.getParameterValues("sagrid_cloudapps_WMS");
                // Get the  ETOKENSERVER from the portlet request for the SAGRID VO
                String sagrid_cloudapps_ETOKENSERVER = request.getParameter("sagrid_cloudapps_ETOKENSERVER");
                // Get the  MYPROXYSERVER from the portlet request for the SAGRID VO
                String sagrid_cloudapps_MYPROXYSERVER = request.getParameter("sagrid_cloudapps_MYPROXYSERVER");
                // Get the  PORT from the portlet request for the SAGRID VO
                String sagrid_cloudapps_PORT = request.getParameter("sagrid_cloudapps_PORT");
                // Get the  ROBOTID from the portlet request for the SAGRID VO
                String sagrid_cloudapps_ROBOTID = request.getParameter("sagrid_cloudapps_ROBOTID");
                // Get the  ROLE from the portlet request for the SAGRID VO
                String sagrid_cloudapps_ROLE = request.getParameter("sagrid_cloudapps_ROLE");
                // Get the  OPTIONS from the portlet request for the SAGRID VO
                String[] sagrid_cloudapps_OPTIONS = request.getParameterValues("sagrid_cloudapps_OPTIONS");                

                String sagrid_cloudapps_RENEWAL = "";
                String sagrid_cloudapps_DISABLEVOMS = "";

                if (sagrid_cloudapps_OPTIONS == null){
                    sagrid_cloudapps_RENEWAL = "checked";
                    sagrid_cloudapps_DISABLEVOMS = "unchecked";
                } else {
                    Arrays.sort(sagrid_cloudapps_OPTIONS);
                    // Get the  RENEWAL from the portlet preferences for the SAGRID VO
                    sagrid_cloudapps_RENEWAL = Arrays.binarySearch(sagrid_cloudapps_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                    // Get the  DISABLEVOMS from the portlet preferences for the SAGRID VO
                    sagrid_cloudapps_DISABLEVOMS = Arrays.binarySearch(sagrid_cloudapps_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                }                       

                int nmax=0;                
                for (int i = 0; i < sagrid_cloudapps_WMS.length; i++)
                    if ( sagrid_cloudapps_WMS[i]!=null && (!sagrid_cloudapps_WMS[i].trim().equals("N/A")) )                        
                        nmax++;
                
                log.info("\n\nLength="+nmax);
                String[] sagrid_cloudapps_WMS_trimmed = new String[nmax];
                for (int i = 0; i < nmax; i++)
                {
                    sagrid_cloudapps_WMS_trimmed[i]=sagrid_cloudapps_WMS[i].trim();
                    log.info ("\n\nSAGRID [" + i + "] WMS=[" + sagrid_cloudapps_WMS_trimmed[i] + "]");
                }

                // Set the portlet preferences
                portletPreferences.setValue("sagrid_cloudapps_INFRASTRUCTURE", sagrid_cloudapps_INFRASTRUCTURE.trim());
                portletPreferences.setValue("sagrid_cloudapps_VONAME", sagrid_cloudapps_VONAME.trim());
                portletPreferences.setValue("sagrid_cloudapps_TOPBDII", sagrid_cloudapps_TOPBDII.trim());
                portletPreferences.setValues("sagrid_cloudapps_WMS", sagrid_cloudapps_WMS_trimmed);
                portletPreferences.setValue("sagrid_cloudapps_ETOKENSERVER", sagrid_cloudapps_ETOKENSERVER.trim());
                portletPreferences.setValue("sagrid_cloudapps_MYPROXYSERVER", sagrid_cloudapps_MYPROXYSERVER.trim());
                portletPreferences.setValue("sagrid_cloudapps_PORT", sagrid_cloudapps_PORT.trim());
                portletPreferences.setValue("sagrid_cloudapps_ROBOTID", sagrid_cloudapps_ROBOTID.trim());
                portletPreferences.setValue("sagrid_cloudapps_ROLE", sagrid_cloudapps_ROLE.trim());
                portletPreferences.setValue("sagrid_cloudapps_RENEWAL", sagrid_cloudapps_RENEWAL);
                portletPreferences.setValue("sagrid_cloudapps_DISABLEVOMS", sagrid_cloudapps_DISABLEVOMS);
                
                portletPreferences.setValue("cloudapps_APPID", cloudapps_APPID.trim());
                portletPreferences.setValue("cloudapps_LOGLEVEL", cloudapps_LOGLEVEL.trim());
                portletPreferences.setValue("cloudapps_OUTPUT_PATH", cloudapps_OUTPUT_PATH.trim());
                portletPreferences.setValue("cloudapps_SOFTWARE", cloudapps_SOFTWARE.trim());
                portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                    + "\n- Storing the  portlet preferences ..."                    
                    + "\n\nsagrid_cloudapps_INFRASTRUCTURE: " + sagrid_cloudapps_INFRASTRUCTURE
                    + "\nsagrid_cloudapps_VONAME: " + sagrid_cloudapps_VONAME
                    + "\nsagrid_cloudapps_TOPBDII: " + sagrid_cloudapps_TOPBDII                    
                    + "\nsagrid_cloudapps_ETOKENSERVER: " + sagrid_cloudapps_ETOKENSERVER
                    + "\nsagrid_cloudapps_MYPROXYSERVER: " + sagrid_cloudapps_MYPROXYSERVER
                    + "\nsagrid_cloudapps_PORT: " + sagrid_cloudapps_PORT
                    + "\nsagrid_cloudapps_ROBOTID: " + sagrid_cloudapps_ROBOTID
                    + "\nsagrid_cloudapps_ROLE: " + sagrid_cloudapps_ROLE
                    + "\nsagrid_cloudapps_RENEWAL: " + sagrid_cloudapps_RENEWAL
                    + "\nsagrid_cloudapps_DISABLEVOMS: " + sagrid_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + "sagrid"
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
            }
            
            for (int i=0; i<infras.length; i++)
                if (infras[i]!=null) 
                    log.info("\n - Infrastructure Enabled = " + infras[i]);           
            
            portletPreferences.setValues("cloudapps_ENABLEINFRASTRUCTURE", infras);
            portletPreferences.setValue("bes_cloudapps_ENABLEINFRASTRUCTURE",infras[0]);
            portletPreferences.setValue("garuda_cloudapps_ENABLEINFRASTRUCTURE",infras[1]);
            portletPreferences.setValue("chain_cloudapps_ENABLEINFRASTRUCTURE",infras[2]);
            portletPreferences.setValue("fedcloud_cloudapps_ENABLEINFRASTRUCTURE",infras[3]);
            portletPreferences.setValue("eumed_cloudapps_ENABLEINFRASTRUCTURE",infras[4]);
            portletPreferences.setValue("gisela_cloudapps_ENABLEINFRASTRUCTURE",infras[5]);
            portletPreferences.setValue("sagrid_cloudapps_ENABLEINFRASTRUCTURE",infras[6]);

            portletPreferences.store();
            response.setPortletMode(PortletMode.VIEW);
        } // end PROCESS ACTION [ CONFIG_CLOUDAPPS_PORTLET ]
        

        if (action.equals("SUBMIT_CLOUDAPPS_PORTLET")) {
            log.info("\nPROCESS ACTION => " + action); 
            
            InfrastructureInfo infrastructures[] = new InfrastructureInfo[7];
            int NMAX=0;           
            String wmsList[] = new String [10];
            String _wmsListFedCloud[] = new String [10];            
            String fedcloud_cloudapps_ETOKENSERVER = "";
            String fedcloud_cloudapps_PORT = "";
            String fedcloud_cloudapps_ROBOTID = "";
            String fedcloud_cloudapps_VONAME = "";
            String fedcloud_cloudapps_ROLE = "";
            
            String _wmsListChainCloud[] = new String [10];            
            String chain_cloudapps_ETOKENSERVER = "";
            String chain_cloudapps_PORT = "";
            String chain_cloudapps_ROBOTID = "";
            String chain_cloudapps_VONAME = "";
            String chain_cloudapps_ROLE = "";
            
            String _wmsListBesCloud[] = new String [10];
            String bes_cloudapps_ETOKENSERVER = "";
            String bes_cloudapps_PORT = "";
            String bes_cloudapps_ROBOTID = "";
            String bes_cloudapps_VONAME = "";
            String bes_cloudapps_ROLE = "";

            // Get the APPID from the portlet preferences
            String cloudapps_APPID = portletPreferences.getValue("cloudapps_APPID", "N/A");
            // Get the LOGLEVEL from the portlet preferences
            String cloudapps_LOGLEVEL = portletPreferences.getValue("cloudapps_LOGLEVEL", "INFO");
            // Get the APPID from the portlet preferences
            String cloudapps_OUTPUT_PATH = portletPreferences.getValue("cloudapps_OUTPUT_PATH", "/tmp");
            // Get the SOFTWARE from the portlet preferences
            String cloudapps_SOFTWARE = portletPreferences.getValue("cloudapps_SOFTWARE", "N/A");
            // Get the TRACKING_DB_HOSTNAME from the portlet request
            String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
            // Get the TRACKING_DB_USERNAME from the portlet request
            String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
            // Get the TRACKING_DB_PASSWORD from the portlet request
            String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD","N/A");
            // Get the SMTP_HOST from the portlet request
            String SMTP_HOST = portletPreferences.getValue("SMTP_HOST","N/A");
            // Get the SENDER_MAIL from the portlet request
            String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL","N/A");        
            
            String bes_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("bes_cloudapps_ENABLEINFRASTRUCTURE","null");
            String garuda_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("garuda_cloudapps_ENABLEINFRASTRUCTURE","null");
            String chain_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("chain_cloudapps_ENABLEINFRASTRUCTURE","null");
            String fedcloud_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("fedcloud_cloudapps_ENABLEINFRASTRUCTURE","null");
            String eumed_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("eumed_cloudapps_ENABLEINFRASTRUCTURE","null");
            String gisela_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("gisela_cloudapps_ENABLEINFRASTRUCTURE","null");
            String sagrid_cloudapps_ENABLEINFRASTRUCTURE =
                    portletPreferences.getValue("sagrid_cloudapps_ENABLEINFRASTRUCTURE","null");
        
            if (bes_cloudapps_ENABLEINFRASTRUCTURE != null &&
                bes_cloudapps_ENABLEINFRASTRUCTURE.equals("bes"))
            {
                String OCCI_AUTH = "x509";
                
                // Possible RESOURCE values: 'os_tpl', 'resource_tpl', 'compute'
                String OCCI_RESOURCE = "compute";
                
                // Possible ACTION values: 'list', 'describe', 'create' and 'delete'
                String OCCI_ACTION = "create";
                
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the BES VO
                String bes_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("bes_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the BES VO
                bes_cloudapps_VONAME = portletPreferences.getValue("bes_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the BES VO
                String bes_cloudapps_TOPBDII = portletPreferences.getValue("bes_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the BES VO                
                String[] bes_cloudapps_WMS = portletPreferences.getValues("bes_cloudapps_WMS", new String[5]);
                // Get the  ETOKENSERVER from the portlet preferences for the BES VO
                bes_cloudapps_ETOKENSERVER = portletPreferences.getValue("bes_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the BES VO
                String bes_cloudapps_MYPROXYSERVER = portletPreferences.getValue("bes_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the BES VO
                bes_cloudapps_PORT = portletPreferences.getValue("bes_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the BES VO
                bes_cloudapps_ROBOTID = portletPreferences.getValue("bes_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the BES VO
                bes_cloudapps_ROLE = portletPreferences.getValue("bes_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the BES VO
                String bes_cloudapps_RENEWAL = portletPreferences.getValue("bes_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the BES VO
                String bes_cloudapps_DISABLEVOMS = portletPreferences.getValue("bes_cloudapps_DISABLEVOMS", "unchecked");
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(bes_cloudapps_VONAME, bes_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\nbes_cloudapps_INFRASTRUCTURE: " + bes_cloudapps_INFRASTRUCTURE
                    + "\nbes_cloudapps_VONAME: " + bes_cloudapps_VONAME
                    + "\nbes_cloudapps_TOPBDII: " + bes_cloudapps_TOPBDII                    
                    + "\nbes_cloudapps_ETOKENSERVER: " + bes_cloudapps_ETOKENSERVER
                    + "\nbes_cloudapps_MYPROXYSERVER: " + bes_cloudapps_MYPROXYSERVER
                    + "\nbes_cloudapps_PORT: " + bes_cloudapps_PORT
                    + "\nbes_cloudapps_ROBOTID: " + bes_cloudapps_ROBOTID
                    + "\nbes_cloudapps_ROLE: " + bes_cloudapps_ROLE
                    + "\nbes_cloudapps_RENEWAL: " + bes_cloudapps_RENEWAL
                    + "\nbes_cloudapps_DISABLEVOMS: " + bes_cloudapps_DISABLEVOMS
                 
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + bes_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
                
                // Defining the rOCCIResource list for the "CHINESE" Infrastructure
                int nmax=0;
                for (int i = 0; i < bes_cloudapps_WMS.length; i++)
                    if ((bes_cloudapps_WMS[i]!=null) && 
                        (!bes_cloudapps_WMS[i].equals("N/A"))) 
                        nmax++;

                //String wmsList[] = new String [nmax];                
                for (int i = 0; i < nmax; i++)
                {
                    if (bes_cloudapps_WMS[i]!=null) {                    
                    _wmsListBesCloud[i]=bes_cloudapps_WMS[i].trim()+
                            "?" +
                            "action=" + OCCI_ACTION +
                            "&resource=" + OCCI_RESOURCE +
                            "&attributes_title=OCCI_VM_TITLE" +
                            "&mixin_os_tpl=OCCI_OS" +
                            "&mixin_resource_tpl=OCCI_FLAVOUR" +
                            "&auth=" + OCCI_AUTH;
                    
                    log.info ("\n\n[" + nmax
                                      + "] Submitting to BES ["
                                      + i
                                      + "] using WMS=["
                                      + _wmsListBesCloud[i]
                                      + "]");
                    }
                }

                infrastructures[0] = new InfrastructureInfo(
                    "BES",
                    "rocci",
                    "",
                    _wmsListBesCloud,
                    bes_cloudapps_ETOKENSERVER,
                    bes_cloudapps_PORT,
                    bes_cloudapps_ROBOTID,
                    bes_cloudapps_VONAME,
                    bes_cloudapps_ROLE,
                    true);
            }
            
            if (garuda_cloudapps_ENABLEINFRASTRUCTURE != null &&
                garuda_cloudapps_ENABLEINFRASTRUCTURE.equals("garuda"))
            {
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("garuda_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_VONAME = portletPreferences.getValue("garuda_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_TOPBDII = portletPreferences.getValue("garuda_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the GARUDA VO                
                String[] garuda_cloudapps_WMS = portletPreferences.getValues("garuda_cloudapps_WMS", new String[5]);
                // Get the  ETOKENSERVER from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_ETOKENSERVER = portletPreferences.getValue("garuda_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_MYPROXYSERVER = portletPreferences.getValue("garuda_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_PORT = portletPreferences.getValue("garuda_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_ROBOTID = portletPreferences.getValue("garuda_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_ROLE = portletPreferences.getValue("garuda_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_RENEWAL = portletPreferences.getValue("garuda_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the GARUDA VO
                String garuda_cloudapps_DISABLEVOMS = portletPreferences.getValue("garuda_cloudapps_DISABLEVOMS", "unchecked"); 
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\ngaruda_cloudapps_INFRASTRUCTURE: " + garuda_cloudapps_INFRASTRUCTURE
                    + "\ngaruda_cloudapps_VONAME: " + garuda_cloudapps_VONAME
                    + "\ngaruda_cloudapps_TOPBDII: " + garuda_cloudapps_TOPBDII                    
                    + "\ngaruda_cloudapps_ETOKENSERVER: " + garuda_cloudapps_ETOKENSERVER
                    + "\ngaruda_cloudapps_MYPROXYSERVER: " + garuda_cloudapps_MYPROXYSERVER
                    + "\ngaruda_cloudapps_PORT: " + garuda_cloudapps_PORT
                    + "\ngaruda_cloudapps_ROBOTID: " + garuda_cloudapps_ROBOTID
                    + "\ngaruda_cloudapps_ROLE: " + garuda_cloudapps_ROLE
                    + "\ngaruda_cloudapps_RENEWAL: " + garuda_cloudapps_RENEWAL
                    + "\ngaruda_cloudapps_DISABLEVOMS: " + garuda_cloudapps_DISABLEVOMS
                   
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + garuda_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
                
                // Defining the WMS list for the "GARUDA" Infrastructure
                int nmax=0;
                for (int i = 0; i < garuda_cloudapps_WMS.length; i++)
                    if ((garuda_cloudapps_WMS[i]!=null) && 
                        (!garuda_cloudapps_WMS[i].equals("N/A"))) 
                        nmax++;

                //String wmsList[] = new String [nmax];
                for (int i = 0; i < nmax; i++)
                {
                    if (garuda_cloudapps_WMS[i]!=null) {
                    wmsList[i]=garuda_cloudapps_WMS[i].trim();
                    log.info ("\n\n[" + nmax
                                      + "] Submitting to GARUDA ["
                                      + i
                                      + "] using WSGRAM=["
                                      + wmsList[i]
                                      + "]");
                    }
                }

                infrastructures[1] = new InfrastructureInfo(
                    "GARUDA",
                    "wsgram",
                    "",
                    wmsList,
                    garuda_cloudapps_ETOKENSERVER,
                    garuda_cloudapps_PORT,
                    garuda_cloudapps_ROBOTID,
                    garuda_cloudapps_VONAME,
                    garuda_cloudapps_ROLE);
            }
            
            if (chain_cloudapps_ENABLEINFRASTRUCTURE != null &&
                chain_cloudapps_ENABLEINFRASTRUCTURE.equals("chain"))
            {
                String OCCI_AUTH = "x509";
                
                // Possible RESOURCE values: 'os_tpl', 'resource_tpl', 'compute'
                String OCCI_RESOURCE = "compute";
                
                // Possible ACTION values: 'list', 'describe', 'create' and 'delete'
                String OCCI_ACTION = "create";
                
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the CHAIN VO
                String chain_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("chain_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the CHAIN VO
                chain_cloudapps_VONAME = portletPreferences.getValue("chain_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the CHAIN VO
                String chain_cloudapps_TOPBDII = portletPreferences.getValue("chain_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the CHAIN VO                
                String[] chain_cloudapps_WMS = portletPreferences.getValues("chain_cloudapps_WMS", new String[10]);
                // Get the  ETOKENSERVER from the portlet preferences for the CHAIN VO
                chain_cloudapps_ETOKENSERVER = portletPreferences.getValue("chain_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the CHAIN VO
                String chain_cloudapps_MYPROXYSERVER = portletPreferences.getValue("chain_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the CHAIN VO
                chain_cloudapps_PORT = portletPreferences.getValue("chain_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the CHAIN VO
                chain_cloudapps_ROBOTID = portletPreferences.getValue("chain_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the CHAIN VO
                chain_cloudapps_ROLE = portletPreferences.getValue("chain_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the CHAIN VO
                String chain_cloudapps_RENEWAL = portletPreferences.getValue("chain_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the CHAIN VO
                String chain_cloudapps_DISABLEVOMS = portletPreferences.getValue("chain_cloudapps_DISABLEVOMS", "unchecked"); 
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(chain_cloudapps_VONAME, chain_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\nchain_cloudapps_INFRASTRUCTURE: " + chain_cloudapps_INFRASTRUCTURE
                    + "\nchain_cloudapps_VONAME: " + chain_cloudapps_VONAME
                    + "\nchain_cloudapps_TOPBDII: " + chain_cloudapps_TOPBDII                    
                    + "\nchain_cloudapps_ETOKENSERVER: " + chain_cloudapps_ETOKENSERVER
                    + "\nchain_cloudapps_MYPROXYSERVER: " + chain_cloudapps_MYPROXYSERVER
                    + "\nchain_cloudapps_PORT: " + chain_cloudapps_PORT
                    + "\nchain_cloudapps_ROBOTID: " + chain_cloudapps_ROBOTID
                    + "\nchain_cloudapps_ROLE: " + chain_cloudapps_ROLE
                    + "\nchain_cloudapps_RENEWAL: " + chain_cloudapps_RENEWAL
                    + "\nchain_cloudapps_DISABLEVOMS: " + chain_cloudapps_DISABLEVOMS
                   
                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + chain_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                    
                    log.info("\n- Creating a proxy ...");
                    getRobotProxy(chain_cloudapps_ETOKENSERVER, 
                                  chain_cloudapps_PORT,
                                  chain_cloudapps_ROBOTID,
                                  chain_cloudapps_VONAME,
                                  chain_cloudapps_ROLE,
                                  chain_cloudapps_RENEWAL);
                }
                
                // Defining the rOCCIResource list for the "CHAIN-REDS" Infrastructure                
                int nmax=0;
                for (int i = 0; i < chain_cloudapps_WMS.length; i++)
                    if ((chain_cloudapps_WMS[i]!=null) && 
                        (!chain_cloudapps_WMS[i].equals("N/A"))) 
                        nmax++;
                                                
                //String wmsList[] = new String [nmax];
                for (int i = 0; i < nmax; i++)
                {
                    if (chain_cloudapps_WMS[i]!=null) {
                        _wmsListChainCloud[i]=chain_cloudapps_WMS[i].trim()+                       
                            "?" +
                            "action=" + OCCI_ACTION +
                            "&resource=" + OCCI_RESOURCE +
                            "&attributes_title=OCCI_VM_TITLE" +
                            "&mixin_os_tpl=OCCI_OS" +
                            "&mixin_resource_tpl=OCCI_FLAVOUR" +
                            "&auth=" + OCCI_AUTH;                                                    
                    
                    log.info ("\n\n[" + nmax
                                      + "] CHAIN-REDS ["
                                      + i
                                      + "] rOCCIResourceID=["
                                      + _wmsListChainCloud[i]                                      
                                      + "]");
                    }
                }

                infrastructures[2] = new InfrastructureInfo(
                    "CHAIN-REDS",
                    "rocci",
                    "",
                    _wmsListChainCloud,
                    chain_cloudapps_ETOKENSERVER,
                    chain_cloudapps_PORT,
                    chain_cloudapps_ROBOTID,
                    chain_cloudapps_VONAME,
                    chain_cloudapps_ROLE,
                    true);                    
            }
            
            if (fedcloud_cloudapps_ENABLEINFRASTRUCTURE != null &&
                fedcloud_cloudapps_ENABLEINFRASTRUCTURE.equals("fedcloud"))
            {
                String OCCI_AUTH = "x509";
                
                // Possible RESOURCE values: 'os_tpl', 'resource_tpl', 'compute'
                String OCCI_RESOURCE = "compute";
                
                String OCCI_ACTION = "create";
                
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the FEDCLOUD VO
                String fedcloud_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("fedcloud_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the FEDCLOUD VO
                fedcloud_cloudapps_VONAME = portletPreferences.getValue("fedcloud_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the FEDCLOUD VO
                String fedcloud_cloudapps_TOPBDII = portletPreferences.getValue("fedcloud_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the FEDCLOUD VO
                String[] fedcloud_cloudapps_WMS = portletPreferences.getValues("fedcloud_cloudapps_WMS", new String[10]);
                // Get the  ETOKENSERVER from the portlet preferences for the FEDCLOUD VO
                fedcloud_cloudapps_ETOKENSERVER = portletPreferences.getValue("fedcloud_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the FEDCLOUD VO
                String fedcloud_cloudapps_MYPROXYSERVER = portletPreferences.getValue("fedcloud_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the FEDCLOUD VO
                fedcloud_cloudapps_PORT = portletPreferences.getValue("fedcloud_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the FEDCLOUD VO
                fedcloud_cloudapps_ROBOTID = portletPreferences.getValue("fedcloud_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the FEDCLOUD VO
                fedcloud_cloudapps_ROLE = portletPreferences.getValue("fedcloud_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the FEDCLOUD VO
                String fedcloud_cloudapps_RENEWAL = portletPreferences.getValue("fedcloud_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the FEDCLOUD VO
                String fedcloud_cloudapps_DISABLEVOMS = portletPreferences.getValue("fedcloud_cloudapps_DISABLEVOMS", "unchecked");
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(fedcloud_cloudapps_VONAME, fedcloud_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\n\nfedcloud_cloudapps_INFRASTRUCTURE: " + fedcloud_cloudapps_INFRASTRUCTURE
                    + "\nfedcloud_cloudapps_VONAME: " + fedcloud_cloudapps_VONAME
                    + "\nfedcloud_cloudapps_TOPBDII: " + fedcloud_cloudapps_TOPBDII                    
                    + "\nfedcloud_cloudapps_ETOKENSERVER: " + fedcloud_cloudapps_ETOKENSERVER
                    + "\nfedcloud_cloudapps_MYPROXYSERVER: " + fedcloud_cloudapps_MYPROXYSERVER
                    + "\nfedcloud_cloudapps_PORT: " + fedcloud_cloudapps_PORT
                    + "\nfedcloud_cloudapps_ROBOTID: " + fedcloud_cloudapps_ROBOTID
                    + "\nfedcloud_cloudapps_ROLE: " + fedcloud_cloudapps_ROLE
                    + "\nfedcloud_cloudapps_RENEWAL: " + fedcloud_cloudapps_RENEWAL
                    + "\nfedcloud_cloudapps_DISABLEVOMS: " + fedcloud_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + fedcloud_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                    
                    log.info("\n- Creating a proxy ...");
                    getRobotProxy(fedcloud_cloudapps_ETOKENSERVER, 
                                  fedcloud_cloudapps_PORT,
                                  fedcloud_cloudapps_ROBOTID,
                                  fedcloud_cloudapps_VONAME,
                                  fedcloud_cloudapps_ROLE,
                                  fedcloud_cloudapps_RENEWAL);
                }
                
                // Defining the rOCCIResource list for the "FEDCLOUD" Infrastructure                
                int nmax=0;
                for (int i = 0; i < fedcloud_cloudapps_WMS.length; i++)
                    if ((fedcloud_cloudapps_WMS[i]!=null) && 
                        (!fedcloud_cloudapps_WMS[i].equals("N/A"))) 
                        nmax++;
                                                
                //String wmsList[] = new String [nmax];
                for (int i = 0; i < nmax; i++)
                {
                    if (fedcloud_cloudapps_WMS[i]!=null) {
                        _wmsListFedCloud[i]=fedcloud_cloudapps_WMS[i].trim()+                       
                            "?" +
                            "action=" + OCCI_ACTION +
                            "&resource=" + OCCI_RESOURCE +
                            "&attributes_title=OCCI_VM_TITLE" +
                            "&mixin_os_tpl=OCCI_OS" +
                            "&mixin_resource_tpl=OCCI_FLAVOUR" +
                            "&auth=" + OCCI_AUTH;                            
                    
                    log.info ("\n\n[" + nmax
                                      + "] FEDCLOUD ["
                                      + i
                                      + "] rOCCIResourceID=["
                                      + _wmsListFedCloud[i]                                      
                                      + "]");
                    }
                }

                infrastructures[3] = 
                        new InfrastructureInfo(
                            "EGI-FEDCLOUD",
                            "rocci",
                            "",
                            _wmsListFedCloud,                            
                            fedcloud_cloudapps_ETOKENSERVER,
                            fedcloud_cloudapps_PORT,
                            fedcloud_cloudapps_ROBOTID,
                            fedcloud_cloudapps_VONAME,
                            fedcloud_cloudapps_ROLE,
                            true);
            }
            
            if (eumed_cloudapps_ENABLEINFRASTRUCTURE != null &&
                eumed_cloudapps_ENABLEINFRASTRUCTURE.equals("eumed"))
            {
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the EUMED VO
                String eumed_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("eumed_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the EUMED VO
                String eumed_cloudapps_VONAME = portletPreferences.getValue("eumed_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the EUMED VO
                String eumed_cloudapps_TOPBDII = portletPreferences.getValue("eumed_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the EUMED VO
                String[] eumed_cloudapps_WMS = portletPreferences.getValues("eumed_cloudapps_WMS", new String[5]);
                // Get the  ETOKENSERVER from the portlet preferences for the EUMED VO
                String eumed_cloudapps_ETOKENSERVER = portletPreferences.getValue("eumed_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the EUMED VO
                String eumed_cloudapps_MYPROXYSERVER = portletPreferences.getValue("eumed_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the EUMED VO
                String eumed_cloudapps_PORT = portletPreferences.getValue("eumed_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the EUMED VO
                String eumed_cloudapps_ROBOTID = portletPreferences.getValue("eumed_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the EUMED VO
                String eumed_cloudapps_ROLE = portletPreferences.getValue("eumed_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the EUMED VO
                String eumed_cloudapps_RENEWAL = portletPreferences.getValue("eumed_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the EUMED VO
                String eumed_cloudapps_DISABLEVOMS = portletPreferences.getValue("eumed_cloudapps_DISABLEVOMS", "unchecked");
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(eumed_cloudapps_VONAME, eumed_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\n\neumed_cloudapps_INFRASTRUCTURE: " + eumed_cloudapps_INFRASTRUCTURE
                    + "\neumed_cloudapps_VONAME: " + eumed_cloudapps_VONAME
                    + "\neumed_cloudapps_TOPBDII: " + eumed_cloudapps_TOPBDII                    
                    + "\neumed_cloudapps_ETOKENSERVER: " + eumed_cloudapps_ETOKENSERVER
                    + "\neumed_cloudapps_MYPROXYSERVER: " + eumed_cloudapps_MYPROXYSERVER
                    + "\neumed_cloudapps_PORT: " + eumed_cloudapps_PORT
                    + "\neumed_cloudapps_ROBOTID: " + eumed_cloudapps_ROBOTID
                    + "\neumed_cloudapps_ROLE: " + eumed_cloudapps_ROLE
                    + "\neumed_cloudapps_RENEWAL: " + eumed_cloudapps_RENEWAL
                    + "\neumed_cloudapps_DISABLEVOMS: " + eumed_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + eumed_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
                
                // Defining the WMS list for the "EUMED" Infrastructure
                int nmax=0;
                for (int i = 0; i < eumed_cloudapps_WMS.length; i++)
                    if ((eumed_cloudapps_WMS[i]!=null) && (!eumed_cloudapps_WMS[i].equals("N/A"))) nmax++;
                
                //String wmsList[] = new String [nmax];
                for (int i = 0; i < nmax; i++)
                {
                    if (eumed_cloudapps_WMS[i]!=null) {
                    wmsList[i]=eumed_cloudapps_WMS[i].trim();
                    log.info ("\n\n[" + nmax
                                      + "] Submitting to EUMED ["
                                      + i
                                      + "] using WMS=["
                                      + wmsList[i]
                                      + "]");
                    }
                }

                infrastructures[4] = new InfrastructureInfo(
                    eumed_cloudapps_VONAME,
                    eumed_cloudapps_TOPBDII,
                    wmsList,
                    eumed_cloudapps_ETOKENSERVER,
                    eumed_cloudapps_PORT,
                    eumed_cloudapps_ROBOTID,
                    eumed_cloudapps_VONAME,
                    eumed_cloudapps_ROLE,
                    "VO-" + eumed_cloudapps_VONAME + "-" + cloudapps_SOFTWARE);
            }

            if (gisela_cloudapps_ENABLEINFRASTRUCTURE != null &&
                gisela_cloudapps_ENABLEINFRASTRUCTURE.equals("gisela")) 
            {
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the GISELA VO
                String gisela_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("gisela_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the GISELA VO
                String gisela_cloudapps_VONAME = portletPreferences.getValue("gisela_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the GISELA VO
                String gisela_cloudapps_TOPBDII = portletPreferences.getValue("gisela_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the GISELA VO
                String[] gisela_cloudapps_WMS = portletPreferences.getValues("gisela_cloudapps_WMS", new String[5]);
                // Get the  ETOKENSERVER from the portlet preferences for the GISELA VO
                String gisela_cloudapps_ETOKENSERVER = portletPreferences.getValue("gisela_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the GISELA VO
                String gisela_cloudapps_MYPROXYSERVER = portletPreferences.getValue("gisela_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the GISELA VO
                String gisela_cloudapps_PORT = portletPreferences.getValue("gisela_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the GISELA VO
                String gisela_cloudapps_ROBOTID = portletPreferences.getValue("gisela_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the GISELA VO
                String gisela_cloudapps_ROLE = portletPreferences.getValue("gisela_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the GISELA VO
                String gisela_cloudapps_RENEWAL = portletPreferences.getValue("gisela_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the GISELA VO
                String gisela_cloudapps_DISABLEVOMS = portletPreferences.getValue("gisela_cloudapps_DISABLEVOMS", "unchecked");          
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(gisela_cloudapps_VONAME, gisela_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\n\ngisela_cloudapps_INFRASTRUCTURE: " + gisela_cloudapps_INFRASTRUCTURE
                    + "\ngisela_cloudapps_VONAME: " + gisela_cloudapps_VONAME
                    + "\ngisela_cloudapps_TOPBDII: " + gisela_cloudapps_TOPBDII                        
                    + "\ngisela_cloudapps_ETOKENSERVER: " + gisela_cloudapps_ETOKENSERVER
                    + "\ngisela_cloudapps_MYPROXYSERVER: " + gisela_cloudapps_MYPROXYSERVER
                    + "\ngisela_cloudapps_PORT: " + gisela_cloudapps_PORT
                    + "\ngisela_cloudapps_ROBOTID: " + gisela_cloudapps_ROBOTID
                    + "\ngisela_cloudapps_ROLE: " + gisela_cloudapps_ROLE
                    + "\ngisela_cloudapps_RENEWAL: " + gisela_cloudapps_RENEWAL
                    + "\ngisela_cloudapps_DISABLEVOMS: " + gisela_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + gisela_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);
                }
                
                // Defining the WMS list for the "GISELA" Infrastructure
                int nmax=0;
                for (int i = 0; i < gisela_cloudapps_WMS.length; i++)
                    if ((gisela_cloudapps_WMS[i]!=null) && (!gisela_cloudapps_WMS[i].equals("N/A"))) nmax++;
                
                //String wmsList[] = new String [gisela_cloudapps_WMS.length];
                for (int i = 0; i < gisela_cloudapps_WMS.length; i++)
                {
                    if (gisela_cloudapps_WMS[i]!=null) {
                    wmsList[i]=gisela_cloudapps_WMS[i].trim();
                    log.info ("\n\nSubmitting for GISELA [" + i + "] using WMS=[" + wmsList[i] + "]");
                    }
                }

                infrastructures[5] = new InfrastructureInfo(
                    gisela_cloudapps_VONAME,
                    gisela_cloudapps_TOPBDII,
                    wmsList,
                    gisela_cloudapps_ETOKENSERVER,
                    gisela_cloudapps_PORT,
                    gisela_cloudapps_ROBOTID,
                    gisela_cloudapps_VONAME,
                    gisela_cloudapps_ROLE,
                    "VO-" + gisela_cloudapps_VONAME + "-" + cloudapps_SOFTWARE);
            }
            
            if (sagrid_cloudapps_ENABLEINFRASTRUCTURE != null &&
                sagrid_cloudapps_ENABLEINFRASTRUCTURE.equals("sagrid")) 
            {
                NMAX++;                
                // Get the  VONAME from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_INFRASTRUCTURE = portletPreferences.getValue("sagrid_cloudapps_INFRASTRUCTURE", "N/A");
                // Get the  VONAME from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_VONAME = portletPreferences.getValue("sagrid_cloudapps_VONAME", "N/A");
                // Get the  TOPPBDII from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_TOPBDII = portletPreferences.getValue("sagrid_cloudapps_TOPBDII", "N/A");
                // Get the  WMS from the portlet preferences for the SAGRID VO
                String[] sagrid_cloudapps_WMS = portletPreferences.getValues("sagrid_cloudapps_WMS", new String[5]);
                // Get the  ETOKENSERVER from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_ETOKENSERVER = portletPreferences.getValue("sagrid_cloudapps_ETOKENSERVER", "N/A");
                // Get the  MYPROXYSERVER from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_MYPROXYSERVER = portletPreferences.getValue("sagrid_cloudapps_MYPROXYSERVER", "N/A");
                // Get the  PORT from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_PORT = portletPreferences.getValue("sagrid_cloudapps_PORT", "N/A");
                // Get the  ROBOTID from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_ROBOTID = portletPreferences.getValue("sagrid_cloudapps_ROBOTID", "N/A");
                // Get the  ROLE from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_ROLE = portletPreferences.getValue("sagrid_cloudapps_ROLE", "N/A");
                // Get the  RENEWAL from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_RENEWAL = portletPreferences.getValue("sagrid_cloudapps_RENEWAL", "checked");
                // Get the  DISABLEVOMS from the portlet preferences for the SAGRID VO
                String sagrid_cloudapps_DISABLEVOMS = portletPreferences.getValue("sagrid_cloudapps_DISABLEVOMS", "unchecked");          
                // Get the random CE for the Sonification portlet               
                //RANDOM_CE = getRandomCE(sagrid_cloudapps_VONAME, sagrid_cloudapps_TOPBDII, cloudapps_SOFTWARE);
                
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the  portlet preferences ..."
                    + "\n\nsagrid_cloudapps_INFRASTRUCTURE: " + sagrid_cloudapps_INFRASTRUCTURE
                    + "\nsagrid_cloudapps_VONAME: " + sagrid_cloudapps_VONAME
                    + "\nsagrid_cloudapps_TOPBDII: " + sagrid_cloudapps_TOPBDII                        
                    + "\nsagrid_cloudapps_ETOKENSERVER: " + sagrid_cloudapps_ETOKENSERVER
                    + "\nsagrid_cloudapps_MYPROXYSERVER: " + sagrid_cloudapps_MYPROXYSERVER
                    + "\nsagrid_cloudapps_PORT: " + sagrid_cloudapps_PORT
                    + "\nsagrid_cloudapps_ROBOTID: " + sagrid_cloudapps_ROBOTID
                    + "\nsagrid_cloudapps_ROLE: " + sagrid_cloudapps_ROLE
                    + "\nsagrid_cloudapps_RENEWAL: " + sagrid_cloudapps_RENEWAL
                    + "\nsagrid_cloudapps_DISABLEVOMS: " + sagrid_cloudapps_DISABLEVOMS

                    + "\n\ncloudapps_ENABLEINFRASTRUCTURE: " + sagrid_cloudapps_ENABLEINFRASTRUCTURE
                    + "\ncloudapps_APPID: " + cloudapps_APPID
                    + "\ncloudapps_LOGLEVEL: " + cloudapps_LOGLEVEL
                    + "\ncloudapps_OUTPUT_PATH: " + cloudapps_OUTPUT_PATH
                    + "\ncloudapps_SOFTWARE: " + cloudapps_SOFTWARE
                    + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                    + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                    + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                    + "\nSMTP_HOST: " + SMTP_HOST
                    + "\nSENDER_MAIL: " + SENDER_MAIL);                                        
                }
                
                // Defining the WMS list for the "SAGRID" Infrastructure
                int nmax=0;
                for (int i = 0; i < sagrid_cloudapps_WMS.length; i++)
                    if ((sagrid_cloudapps_WMS[i]!=null) && (!sagrid_cloudapps_WMS[i].equals("N/A"))) nmax++;
                
                for (int i = 0; i < sagrid_cloudapps_WMS.length; i++)
                {
                    if (sagrid_cloudapps_WMS[i]!=null) {
                    wmsList[i]=sagrid_cloudapps_WMS[i].trim();
                    log.info ("\n\nSubmitting for SAGRID [" + i + "] using WMS=[" + wmsList[i] + "]");
                    }
                }

                infrastructures[6] = new InfrastructureInfo(
                    sagrid_cloudapps_VONAME,
                    sagrid_cloudapps_TOPBDII,
                    wmsList,
                    sagrid_cloudapps_ETOKENSERVER,
                    sagrid_cloudapps_PORT,
                    sagrid_cloudapps_ROBOTID,
                    sagrid_cloudapps_VONAME,
                    sagrid_cloudapps_ROLE,
                    "VO-" + sagrid_cloudapps_VONAME + "-" + cloudapps_SOFTWARE);
            }
            
            String[] _Parameters = new String [7];

            // Upload the input settings for the application
            _Parameters = uploadSettings( request, response, username );

            log.info("\n- Input Parameters: ");
            log.info("\n- ASCII or Text = " + _Parameters[0]);
            log.info("\n- VM Profile Type = " + _Parameters[1]);
            log.info("\n- VM Template = " + _Parameters[5]);
            log.info("\n- VM Name = " + _Parameters[6]);
            log.info("\n- Cloud Resource = " + _Parameters[2]);
            log.info("\n- Enable Notification = " + _Parameters[3]);
            log.info("\n- Description = " + _Parameters[4]);
            
            // Preparing to submit applications in different cloud infrastructure..
            //=============================================================
            // IMPORTANT: INSTANCIATE THE MultiInfrastructureJobSubmission
            //            CLASS USING THE EMPTY CONSTRUCTOR WHEN
            //            WHEN THE PORTLET IS DEPLOYED IN PRODUCTION!!!
            //=============================================================
            MultiInfrastructureJobSubmission CloudMultiJobSubmission =
            new MultiInfrastructureJobSubmission(TRACKING_DB_HOSTNAME,
                                                 TRACKING_DB_USERNAME,
                                                 TRACKING_DB_PASSWORD);
            
            /*MultiInfrastructureJobSubmission CloudMultiJobSubmission =
                new MultiInfrastructureJobSubmission();*/
            
            String CloudFilesPath = getPortletContext().getRealPath("/") +
                                    "WEB-INF/config"; 
            
            // Set the Output path for results            
            CloudMultiJobSubmission.setOutputPath(cloudapps_OUTPUT_PATH);

            // Set the StandardOutput for 
            CloudMultiJobSubmission.setJobOutput("std.out");

            // Set the StandardError for 
            CloudMultiJobSubmission.setJobError("std.err");  

            // A.) CLOUDAPPSTYPE = [ octave ]
            if (_Parameters[1].equals("octave")) 
            {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                log.info ("\n\nPreparing to launch an i686 VM with GNU OCTAVE-3.6.4 [ octave ] ");
                
                // Set the Executable for 
                //CloudMultiJobSubmission.setExecutable("start_generic_OCTAVE.sh");
                CloudMultiJobSubmission.setExecutable("start_cloudApps.sh");

                //String Arguments = _Parameters[0];
                String Arguments = "octave" + "," + _Parameters[0];
                
                // Set the list of Arguments for 
                CloudMultiJobSubmission.setArguments(Arguments);
                
                //String InputSandbox = CloudFilesPath + "/start_generic_OCTAVE.sh" 
                String InputSandbox = CloudFilesPath + "/start_cloudApps.sh" 
                                      + "," 
                                      + _Parameters[0];

                // Set InputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setInputFiles(InputSandbox);                                

                // OutputSandbox (string with comma separated list of file names)                
                //String CloudFiles="demo_output.eps, output.README";
                String CloudFiles="results.tar.gz";

                // Set the OutputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setOutputFiles(CloudFiles);
            }
            
            // B.) CLOUDAPPSTYPE = [ r ]
            if (_Parameters[1].equals("r")) 
            {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                log.info ("\n\nPreparing to launch an i686 VM with R-2.15.3 [ r ] ");
                
                // Set the Executable for 
                //CloudMultiJobSubmission.setExecutable("start_generic_R.sh");
                CloudMultiJobSubmission.setExecutable("start_cloudApps.sh");

                /*String Arguments = _Parameters[0]
                                   + "," +
                                   "std.out";*/
                
		String Arguments = "r" 
			   + "," 
                           + _Parameters[0]
			   + "," 
			   + "std.out";
                
                // Set the list of Arguments for 
                CloudMultiJobSubmission.setArguments(Arguments);
                
                //String InputSandbox = CloudFilesPath + "/start_generic_R.sh" 
                String InputSandbox = CloudFilesPath + "/start_cloudApps.sh" 
                                      + "," 
                                      + _Parameters[0];

                // Set InputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setInputFiles(InputSandbox);                                

                // OutputSandbox (string with comma separated list of file names)                
                //String CloudFiles="Rplots.pdf, output.README";                    
                String CloudFiles="results.tar.gz";

                // Set the OutputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setOutputFiles(CloudFiles);
            }
            
            // C.) CLOUDAPPSTYPE = [ generic_vm ]
            if (_Parameters[1].equals("generic_vm")) 
            {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                log.info ("\n\nPreparing to launch an i686 generic VM [ generic_vm ] ");
                
                // Set the Executable for 
                CloudMultiJobSubmission.setExecutable("start_cloudApps.sh");
                
		String Arguments = "normal";

 		// Set the list of Arguments for
                CloudMultiJobSubmission.setArguments(Arguments);

                String InputSandbox = CloudFilesPath + "/start_cloudApps.sh";

                // Set InputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setInputFiles(InputSandbox);                                

                // OutputSandbox (string with comma separated list of file names)                
                //String CloudFiles="output.README";
                String CloudFiles="results.tar.gz";

                // Set the OutputSandbox files (string with comma separated list of file names)
                CloudMultiJobSubmission.setOutputFiles(CloudFiles);
            }

            // Set the list of infrastructure(s) activated for the  portlet           
            if (infrastructures[0]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the BES Infrastructure.");
                    log.info("\n- length = " +  _wmsListBesCloud.length);
                    
                for (int i = 0; i < _wmsListBesCloud.length; i++)
                if ((_wmsListBesCloud[i]!=null) && (!_wmsListBesCloud[i].equals("N/A")))
                {                    
                    // === SETTINGS for the IHEP CLOUD RESOURCE === //
                    if ((_wmsListBesCloud[i].indexOf("sched02.ihep.ac.cn")) != -1)   
                    {
                        log.info("\n- Adding some customization for the BEIJING-IHEP Cloud Provider.");
                        _wmsListBesCloud[i] = (_wmsListBesCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListBesCloud[i] = (_wmsListBesCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                         if (_Parameters[1].equals("generic_vm")) 
                            _wmsListBesCloud[i] = (_wmsListBesCloud[i].trim()).replace("OCCI_OS", 
                            "361d991f-fed3-4fb5-b267-5df3913d2b68");
                            
                         if (_Parameters[1].equals("octave")) 
                            _wmsListBesCloud[i] = (_wmsListBesCloud[i].trim()).replace("OCCI_OS", 
                            "3ade7193-5db3-4e9c-87fb-3f9b059620f9");
                            
                         if (_Parameters[1].equals("r")) 
                            _wmsListBesCloud[i] = (_wmsListBesCloud[i].trim()).replace("OCCI_OS", 
                            "f1514748-66f0-4c1e-a169-2526f5f953d3");
                    }
                }
                
                // Defining the Cloud ResourceID list for the "IHEP-BEIJING" Infrastructure
                int nmax=0;
                for (int i = 0; i < _wmsListBesCloud.length; i++)
                    if ((_wmsListBesCloud[i]!=null) && (!_wmsListBesCloud[i].equals("N/A"))) nmax++;
                
                String bescloudwmsList[] = new String [nmax];
                for (int i = 0; i < _wmsListBesCloud.length; i++)
                {
                    if (_wmsListBesCloud[i]!=null) {
                    bescloudwmsList[i]=_wmsListBesCloud[i].trim();
                    log.info ("\n\nCloud ResourceID = [" + bescloudwmsList[i] + "]");
                    }
                }
                
                infrastructures[0] = 
                        new InfrastructureInfo(
                            "BES",
                            "rocci",
                            "",                            
                            bescloudwmsList,                            
                            bes_cloudapps_ETOKENSERVER,
                            bes_cloudapps_PORT,
                            bes_cloudapps_ROBOTID,
                            bes_cloudapps_VONAME,
                            bes_cloudapps_ROLE,
                            true);
                    
                CloudMultiJobSubmission.addInfrastructure(infrastructures[0]); 
            }
            
            if (infrastructures[1]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the GARUDA Infrastructure.");
                CloudMultiJobSubmission.addInfrastructure(infrastructures[1]); 
            }
            
            if (infrastructures[2]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the CHAIN-REDS Infrastructure.");
                    log.info("\n- length = " +  _wmsListChainCloud.length);
                    
                for (int i = 0; i < _wmsListChainCloud.length; i++)
                if ((_wmsListChainCloud[i]!=null) && (!_wmsListChainCloud[i].equals("N/A")))
                {                    
                    // === SETTINGS for the INFN-CATANIA-NEBULA CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("nebula-server-01.ct.infn.it")) != -1)   
                    {
                        log.info("\n- Adding some customization for the INFN-CATANIA-NEBULA Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                         if (_Parameters[1].equals("generic_vm")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_generic_vm_19");
                            
                         if (_Parameters[1].equals("octave")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_octave_20");
                            
                         if (_Parameters[1].equals("r")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_r_7");
                    }                    
                    
                    // === SETTINGS for the INFN-CATANIA-STACK CLOUD PROVIDER === //
                    if ((_wmsListChainCloud[i].indexOf("stack-server-01.ct.infn.it")) != -1)   
                    {
                        log.info("\n- Adding some customization for the INFN-CATANIA-STACK Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "f36b8eb8-8247-4b4f-a101-18c7834009e0");
                            
                            if (_Parameters[1].equals("octave")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "bb623e1c-e693-4c7d-a90f-4e5bf96b4787");
                            
                            if (_Parameters[1].equals("r")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "91632086-39ef-4e52-a6d1-0e4f1bf95a7b");
                    }
                    
                    
                    // === SETTINGS for the VESPA CLOUD PROVIDER === //
                    if ((_wmsListChainCloud[i].indexOf("stack-server-01.cloud.dfa.unict.it")) != -1)   
                    {
                        log.info("\n- Adding some customization for the VESPA Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "5949cec9-dce0-4d6f-9c55-8e46a9c78ee9");
                            
                            if (_Parameters[1].equals("octave")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "0645246b-2937-43c0-9a32-aefaf4123b9c");
                            
                            if (_Parameters[1].equals("r")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "978edc01-b944-4b05-b096-6fc951161a1dt");
                    }
                    
                    // === SETTINGS for the PRISMA-INFN-BARI (prisma-cloud) CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("prisma-cloud.ba.infn.it")) != -1)   
                    {
                        log.info("\n- Adding some customization for the PRIMSA-INFN-BARI Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "623a86f7-f5f9-4bc7-816a-80e7bd6603ed");
                            
                            if (_Parameters[1].equals("octave")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "4aca9ee4-8638-4f95-824f-5128e8b0e90f");
                            
                            if (_Parameters[1].equals("r")) 
                                _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "217535d6-7315-4cb7-bc40-2aa20cfef60b");
                    }
                    
                    // === SETTINGS for the CESGA CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("cloud.cesga.es")) != -1)   
                    {
                        log.info("\n- Adding some customization for the CESGA Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                        if (_Parameters[1].equals("generic_vm")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_generic_vm_sl6_4_364");
                            
                        if (_Parameters[1].equals("octave")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_octave_sl6_4_365");
                            
                        if (_Parameters[1].equals("r")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_r_sl6_4_366");
                    }
                    
                    // === SETTINGS for the CESNET CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("carach5.ics.muni.cz")) != -1)   
                    {
                        log.info("\n- Adding some customization for the CESNET Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                        if (_Parameters[1].equals("generic_vm")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_generic_vm_fedcloud_dukan_100");
                            
                        if (_Parameters[1].equals("octave")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_octave_fedcloud_dukan_101");
                            
                        if (_Parameters[1].equals("r")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_chain_reds_r_fedcloud_dukan_102");
                    }
                    
                    // === SETTINGS for the CIEMAT CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("one01.ciemat.es")) != -1)   
                    {
                        log.info("\n- Adding some customization for the CIEMAT Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                         if (_Parameters[1].equals("generic_vm")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_chain_reds_generic_vm_25");
                            
                         if (_Parameters[1].equals("octave")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_chain_reds_octave_28");
                            
                         if (_Parameters[1].equals("r")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_chain_reds_r_27");
                    }
                    
                    // === SETTINGS for the ARN CLOUD RESOURCE === //
                    if ((_wmsListChainCloud[i].indexOf("rocci.grid.arn.dz")) != -1)   
                    {
                        log.info("\n- Adding some customization for the ARN.DZ Cloud Provider.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                            _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                            _Parameters[6]);
                            
                         if (_Parameters[1].equals("generic_vm")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_generic_vm_32");
                            
                         if (_Parameters[1].equals("octave")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_virt_octave_34");
                            
                         if (_Parameters[1].equals("r")) 
                            _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                            "uuid_virt_r_33");
                    }
                    
                    else {
                        log.info("\n- Adding some customization for the CHAIN-REDS Cloud Infrastructure.");
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_OS", 
                                _Parameters[1]);
                        
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                        _wmsListChainCloud[i] = (_wmsListChainCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                    }
                }
                
                // Defining the Cloud ResourceID list for the "CHAIN-REDS" Infrastructure
                int nmax=0;
                for (int i = 0; i < _wmsListChainCloud.length; i++)
                    if ((_wmsListChainCloud[i]!=null) && (!_wmsListChainCloud[i].equals("N/A"))) nmax++;
                
                String chaincloudwmsList[] = new String [nmax];
                for (int i = 0; i < _wmsListChainCloud.length; i++)
                {
                    if (_wmsListChainCloud[i]!=null) {
                    chaincloudwmsList[i]=_wmsListChainCloud[i].trim();
                    log.info ("\n\nCloud ResourceID = [" + chaincloudwmsList[i] + "]");
                    }
                }
                
                infrastructures[2] = 
                        new InfrastructureInfo(
                            "CHAIN-REDS",
                            "rocci",
                            "",                            
                            chaincloudwmsList,                            
                            chain_cloudapps_ETOKENSERVER,
                            chain_cloudapps_PORT,
                            chain_cloudapps_ROBOTID,
                            chain_cloudapps_VONAME,
                            chain_cloudapps_ROLE,
                            true);
                
                CloudMultiJobSubmission.addInfrastructure(infrastructures[2]); 
            }
            if (infrastructures[3]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the FEDCLOUD Infrastructure.");
                
                //=========================================================
                // IMPORTANT: THIS FIX IS ONLY FOR THE 
                //            CHAIN-REDS INTEROPERABILITY 
                //            DEMO EGI-TF-2013
                //=========================================================
                for (int i = 0; i < _wmsListFedCloud.length; i++)
                    if ((_wmsListFedCloud[i]!=null) && (!_wmsListFedCloud[i].equals("N/A")))
                    {
                        // === SETTINGS for the GRNET CLOUD RESOURCE === //
                        if ((_wmsListFedCloud[i].indexOf("okeanos-occi2.hellasgrid.gr")) != -1)
                        {
                            log.info("\n- Adding some customization for the GRNET Cloud Infrastructure.");
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_FLAVOUR",
                            "c1r2048d10drbd");
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_VM_TITLE",
                            _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm"))
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS",
                                "generic-vm");
                            else
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS",
                                _Parameters[1]);                                                        
                        }
                        
                        // === SETTINGS for the GWDG CLOUD RESOURCE === //
                        if ((_wmsListFedCloud[i].indexOf("occi.cloud.gwdg.de")) != -1)   
                        {
                            log.info("\n- Adding some customization for the GWDG Cloud Provider.");
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_generic_vm_46");
                            
                            if (_Parameters[1].equals("octave")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_octave_47");
                            
                            if (_Parameters[1].equals("r")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "uuid_r_48");
                        }
                                                
                        // === OCCI SETTINGS for the KTH CLOUD RESOURCE === //
                        if ((_wmsListFedCloud[i].indexOf("egi.cloud.pdc.kth.se")) != -1) {  
                            log.info("\n- Adding some customization for the KTH Cloud Infrastructure.");
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "egi_debian");
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                        }
                        
                        // === SETTINGS for the FZ Jülich CLOUD RESOURCE === //
                        if ((_wmsListFedCloud[i].indexOf("egi-cloud.zam.kfa-juelich.de")) != -1)   
                        {
                            log.info("\n- Adding some customization for the FZ Jülich Cloud Infrastructure.");
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                            
                            if (_Parameters[1].equals("generic_vm")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "053236c6-1eaa-43ec-b738-10dc4969d091");
                            
                            if (_Parameters[1].equals("octave")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "464f2315-9f8b-4c8d-9235-1b7a8aa520ed");
                            
                            if (_Parameters[1].equals("r")) 
                                _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                "dc87c573-8f09-46b1-ac0f-8be9743bd74f");
                        }                                                
                        
                        else 
                        {
                            log.info("\n- Adding some customization for the Cloud Infrastructure.");
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_OS", 
                                _Parameters[1]);
                        
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_FLAVOUR", 
                                _Parameters[5]);
                            
                            _wmsListFedCloud[i] = (_wmsListFedCloud[i].trim()).replace("OCCI_VM_TITLE", 
                                _Parameters[6]);
                        }
                    }               
                // ==== ONLY FOR THE CHAIN-REDS INTEROPERABILITY DEMO 2013 ====
                
                // Defining the Cloud ResourceID list for the "FEDCLOUD" Infrastructure
                int nmax=0;
                for (int i = 0; i < _wmsListFedCloud.length; i++)
                    if ((_wmsListFedCloud[i]!=null) && (!_wmsListFedCloud[i].equals("N/A"))) nmax++;
                
                String fedcloudwmsList[] = new String [nmax];
                for (int i = 0; i < _wmsListFedCloud.length; i++)
                {
                    if (_wmsListFedCloud[i]!=null) {
                    fedcloudwmsList[i]=_wmsListFedCloud[i].trim();
                    log.info ("\n\nCloud ResourceID = [" + fedcloudwmsList[i] + "]");
                    }
                }
                
                infrastructures[3] = 
                        new InfrastructureInfo(
                            "EGI-FEDCLOUD",
                            "rocci",
                            "",                            
                            fedcloudwmsList,
                            //wmsList,
                            fedcloud_cloudapps_ETOKENSERVER,
                            fedcloud_cloudapps_PORT,
                            fedcloud_cloudapps_ROBOTID,
                            fedcloud_cloudapps_VONAME,
                            fedcloud_cloudapps_ROLE,
                            true);
                
                CloudMultiJobSubmission.addInfrastructure(infrastructures[3]);
            }
            if (infrastructures[4]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the EUMED Infrastructure.");
                CloudMultiJobSubmission.addInfrastructure(infrastructures[4]);
            }
            if (infrastructures[5]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the GISELA Infrastructure.");
                CloudMultiJobSubmission.addInfrastructure(infrastructures[5]);
            }
            if (infrastructures[6]!=null) {
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE"))
                    log.info("\n- Adding the SAGRID Infrastructure.");
                CloudMultiJobSubmission.addInfrastructure(infrastructures[6]);
            }

            // Get the infra
            InfrastructureInfo infrastructure = 
                    CloudMultiJobSubmission.getInfrastructure();
            
            // Set the queue if it's defined
            // This option is not supported in multi-infrastructures mode
            /*if (NMAX==1) {
                if (!_Parameters[2].isEmpty())
                    CloudMultiJobSubmission.setJobQueue(_Parameters[2]);
                //else //SonificationMultiJobSubmission.setRandomCE(true);
                  //  SonificationMultiJobSubmission.setJobQueue(RANDOM_CE);
            }*/
            
            InetAddress addr = InetAddress.getLocalHost();           
            Company company;
            
            try {
                company = PortalUtil.getCompany(request);
                String gateway = company.getName();
                
                // Send a notification email to the user if enabled.
                if (_Parameters[3]!=null)
                    if ( (SMTP_HOST==null) || 
                         (SMTP_HOST.trim().equals("")) ||
                         (SMTP_HOST.trim().equals("N/A")) ||
                         (SENDER_MAIL==null) || 
                         (SENDER_MAIL.trim().equals("")) ||
                         (SENDER_MAIL.trim().equals("N/A"))
                       )
                    log.info ("\nThe Notification Service is not properly configured!!");
                    
                 else {
                        // Enabling Job's notification via email
                        CloudMultiJobSubmission.setUserEmail(emailAddress);
                        
                        sendHTMLEmail(username, 
                                      emailAddress, 
                                      SENDER_MAIL, 
                                      SMTP_HOST, 
                                      _Parameters[1],                                      
                                      gateway);
                }
                
                // Submitting...
                if (cloudapps_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n-  Job submittion in progress using JSAGA JobEngine");
                    CloudMultiJobSubmission.submitJobAsync(
                    infrastructure,
                    username,
                    addr.getHostAddress()+":8162",
                    Integer.valueOf(cloudapps_APPID),
                    _Parameters[4]);
                }
                
            } catch (PortalException ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            }                        
        } // end PROCESS ACTION [ SUBMIT_CLOUDAPPS_PORTLET ]
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
                throws PortletException, IOException
    {
        //super.serveResource(request, response);

        PortletPreferences portletPreferences = (PortletPreferences) request.getPreferences();

        final String action = (String) request.getParameter("action");

        if (action.equals("get-ratings")) {
            //Get CE Ratings from the portlet preferences
            String cloudapps_CR = (String) request.getParameter("cloudapps_CR");

            String json = "{ \"avg\":\"" + 
        	          portletPreferences.getValue(cloudapps_CR+"_avg", "0.0") +
                    	  "\", \"cnt\":\"" + 
			  portletPreferences.getValue(cloudapps_CR+"_cnt", "0") + "\"}";

            response.setContentType("application/json");
            response.getPortletOutputStream().write( json.getBytes() );

        } else if (action.equals("set-ratings")) {

            String cloudapps_CR = (String) request.getParameter("cloudapps_CR");
            int vote = Integer.parseInt(request.getParameter("vote"));

             double avg = Double.parseDouble(portletPreferences.getValue(cloudapps_CR+"_avg", "0.0"));
             long cnt = Long.parseLong(portletPreferences.getValue(cloudapps_CR+"_cnt", "0"));

             portletPreferences.setValue(cloudapps_CR+"_avg", Double.toString(((avg*cnt)+vote) / (cnt +1)));
             portletPreferences.setValue(cloudapps_CR+"_cnt", Long.toString(cnt+1));

             portletPreferences.store();
        }
    }


    // Upload  input files
    public String[] uploadSettings(ActionRequest actionRequest,
                                        ActionResponse actionResponse, String username)
    {        
        String[] _Parameters = new String [7];
        boolean status;

        // Check that we have a file upload request
        boolean isMultipart = PortletFileUpload.isMultipartContent(actionRequest);

        if (isMultipart) {
            // Create a factory for disk-based file items.
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Set factory constrains
            File _Repository = new File ("/tmp");
            if (!_Repository.exists()) status = _Repository.mkdirs();
            factory.setRepository(_Repository);

            // Create a new file upload handler.
            PortletFileUpload upload = new PortletFileUpload(factory);

            try {
                    // Parse the request
                    List items = upload.parseRequest(actionRequest);

                    // Processing items
                    Iterator iter = items.iterator();

                    while (iter.hasNext())
                    {
                        FileItem item = (FileItem) iter.next();

                        String fieldName = item.getFieldName();
                        
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());

                        // Processing a regular form field
                        if ( item.isFormField() )
                        {                                                        
                            if (fieldName.equals("cloudapps_textarea_OCTAVE")) 
                            {
                                _Parameters[0]=
                                        _Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        ".m";                                        
                            
                                // Store the textarea in a ASCII file
                                storeString(_Parameters[0], 
                                            item.getString());                               
                            }
                            
                            if (fieldName.equals("cloudapps_textarea_R")) 
                            {
                                _Parameters[0]=
                                        _Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        ".r";                                        
                            
                                // Store the textarea in a ASCII file
                                storeString(_Parameters[0], 
                                            item.getString());                               
                            }
                            
                            if (fieldName.equals("cloudappstype"))
                                _Parameters[1]=item.getString();
                                                        
                            if (fieldName.equals("cloudapps_CR"))
                                _Parameters[2]=item.getString();
                            
                            if (fieldName.equals("cloudappsvmflavor"))
                                _Parameters[5]=item.getString();
                            
                            if (fieldName.equals("cloudapps_vmname"))
                                _Parameters[6]=item.getString();
                            
                        } else {
                            // Processing a file upload
                            if (fieldName.equals("cloudapps_file_OCTAVE") ||
                                fieldName.equals("cloudapps_file_R"))
                            {                                                               
                                log.info("\n- Uploading the following user's file: "
                                       + "\n[ " + item.getName() + " ]"
                                       + "\n[ " + item.getContentType() + " ]"
                                       + "\n[ " + item.getSize() + "KBytes ]"
                                       );                               

                                // Writing the file to disk
                                String uploadFile = 
                                        _Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        "_" + item.getName();

                                log.info("\n- Writing the user's file: [ "
                                        + uploadFile.toString()
                                        + " ] to disk");

                                item.write(new File(uploadFile)); 
                                
                                _Parameters[0]=uploadFile;                                
                            }
                        }
                        
                        if (fieldName.equals("EnableNotification"))
                                _Parameters[3]=item.getString(); 
                        
                        if (fieldName.equals("cloudapps_desc"))                                
                                if (item.getString().equals("Please, insert here a description for your run"))
                                    _Parameters[4]="Cloud Simulation Started";
                                else
                                    _Parameters[4]=item.getString();                                                
                        
                    } // end while
            } catch (FileUploadException ex) {
              Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
              Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return _Parameters;
    }
    
    // Retrieve a random Computing Element
    // matching the Software Tag for the  application    
    public String getRandomCE(String cloudapps_VONAME,
                              String cloudapps_TOPBDII,
                              String cloudapps_SOFTWARE)
                              throws PortletException, IOException
    {
        String randomCE = null;
        BDII bdii = null;    
        List<String> CEqueues = null;
                        
        log.info("\n- Querying the Information System [ " + 
                  cloudapps_TOPBDII + 
                  " ] and retrieving a random CE matching the SW tag [ VO-" + 
                  cloudapps_VONAME +
                  "-" +
                  cloudapps_SOFTWARE + " ]");  

       try {               

                bdii = new BDII( new URI(cloudapps_TOPBDII) );
                
                // Get the list of the available queues
                CEqueues = bdii.queryCEQueues(cloudapps_VONAME);
                
                // Get the list of the Computing Elements for the given SW tag
                randomCE = bdii.getRandomCEForSWTag("VO-" + 
                                              cloudapps_VONAME + 
                                              "-" +
                                              cloudapps_SOFTWARE);
                
                // Fetching the Queues
                for (String CEqueue:CEqueues) {
                    if (CEqueue.contains(randomCE))
                        randomCE=CEqueue;
                }

        } catch (URISyntaxException ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
        }                   

        return randomCE;
    }

    // Retrieve the list of Computing Elements
    // matching the Software Tag for the  application    
    public List<String> getListofCEForSoftwareTag(String cloudapps_VONAME,
                                                  String cloudapps_TOPBDII,
                                                  String cloudapps_SOFTWARE)
                                throws PortletException, IOException
    {
        List<String> CEs_list = null;
        BDII bdii = null;        
        
        log.info("\n- Querying the Information System [ " + 
                  cloudapps_TOPBDII + 
                  " ] and looking for CEs matching the SW tag [ VO-" + 
                  cloudapps_VONAME +
                  "-" +
                  cloudapps_SOFTWARE + " ]");  

       try {               
           
                bdii = new BDII( new URI(cloudapps_TOPBDII) );                
                
                if (!cloudapps_SOFTWARE.trim().isEmpty())                     
                    CEs_list = bdii.queryCEForSWTag("VO-" +
                                                    cloudapps_VONAME +
                                                    "-" +
                                                    cloudapps_SOFTWARE);                
                /*else
                    CEs_list = bdii.queryCEQueues(cloudapps_VONAME);*/
                
                // Fetching the CE hostnames
                for (String CE:CEs_list) 
                    log.info("\n- CE host found = " + CE);

        } catch (URISyntaxException ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
        }

        return CEs_list;
    }

    // Get the GPS location of the given grid resource
    public String[] getCECoordinate(RenderRequest request,
                                    String CE)
                                    throws PortletException, IOException
    {
        String[] GPS_locations = null;
        BDII bdii = null;

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        // Get the  TOPPBDII from the portlet preferences
        String bes_cloudapps_TOPBDII = 
                portletPreferences.getValue("bes_cloudapps_TOPBDII", "N/A");
        String chain_cloudapps_TOPBDII = 
                portletPreferences.getValue("chain_cloudapps_TOPBDII", "N/A");
        String fedcloud_cloudapps_TOPBDII = 
                portletPreferences.getValue("fedcloud_cloudapps_TOPBDII", "N/A");
        String eumed_cloudapps_TOPBDII = 
                portletPreferences.getValue("eumed_cloudapps_TOPBDII", "N/A");
        String gisela_cloudapps_TOPBDII = 
                portletPreferences.getValue("gisela_cloudapps_TOPBDII", "N/A");
        String sagrid_cloudapps_TOPBDII = 
                portletPreferences.getValue("sagrid_cloudapps_TOPBDII", "N/A");
        
        // Get the  ENABLEINFRASTRUCTURE from the portlet preferences
        String cloudapps_ENABLEINFRASTRUCTURE = 
                portletPreferences.getValue("cloudapps_ENABLEINFRASTRUCTURE", "N/A");

            try {
                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("bes") )
                     bdii = new BDII( new URI(bes_cloudapps_TOPBDII) );
                
                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("chain") )
                     bdii = new BDII( new URI(chain_cloudapps_TOPBDII) );
                
                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("fedcloud") )
                     bdii = new BDII( new URI(fedcloud_cloudapps_TOPBDII) );                

                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("eumed") )
                     bdii = new BDII( new URI(eumed_cloudapps_TOPBDII) );

                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("gisela") )
                    bdii = new BDII( new URI(gisela_cloudapps_TOPBDII) );
                
                if ( cloudapps_ENABLEINFRASTRUCTURE.equals("sagrid") )
                    bdii = new BDII( new URI(sagrid_cloudapps_TOPBDII) );

                GPS_locations = bdii.queryCECoordinate("ldap://" + CE + ":2170");

            } catch (URISyntaxException ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Cloudapps.class.getName()).log(Level.SEVERE, null, ex);
            }

            return GPS_locations;
    }
    
    private void storeString (String fileName, String fileContent) 
                              throws IOException 
    { 
        log.info("\n- Writing textarea in a ASCII file [ " + fileName + " ]");        
        // Removing the Carriage Return (^M) from text
        String pattern = "[\r]";
        String stripped = fileContent.replaceAll(pattern, "");        
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));        
        writer.write(stripped);
        writer.write("\n");
        writer.close();
    }
    
    private void sendHTMLEmail (String USERNAME,
                                String TO, 
                                String FROM, 
                                String SMTP_HOST, 
                                String ApplicationAcronym,
                                String GATEWAY)
    {
                
        log.info("\n- Sending email notification to the user " + USERNAME + " [ " + TO + " ]");
        
        log.info("\n- SMTP Server = " + SMTP_HOST);
        log.info("\n- Sender = " + FROM);
        log.info("\n- Receiver = " + TO);
        log.info("\n- Application = " + ApplicationAcronym);
        log.info("\n- Gateway = " + GATEWAY);        
        
        // Assuming you are sending email from localhost
        String HOST = "localhost";
        
        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty(SMTP_HOST, HOST);
        
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(FROM));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
         //message.addRecipient(Message.RecipientType.CC, new InternetAddress(FROM));

         // Set Subject: header field
         message.setSubject(" [liferay-sg-gateway] - [ " + GATEWAY + " ] ");

	 Date currentDate = new Date();
	 currentDate.setTime (currentDate.getTime());

         // Send the actual HTML message, as big as you like
         message.setContent(
	 "<br/><H4>" +         
	 "<img src=\"http://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc6/195775_220075701389624_155250493_n.jpg\" width=\"100\">Science Gateway Notification" +
	 "</H4><hr><br/>" +
         "<b>Description:</b> Notification for the application <b>[ " + ApplicationAcronym + " ]</b><br/><br/>" +         
         "<i>The application has been successfully submitted from the [ " + GATEWAY + " ]</i><br/><br/>" +
         "<b>TimeStamp:</b> " + currentDate + "<br/><br/>" +
	 "<b>Disclaimer:</b><br/>" +
	 "<i>This is an automatic message sent by the Science Gateway based on Liferay technology.<br/>" + 
	 "If you did not submit any jobs through the Science Gateway, please " +
         "<a href=\"mailto:" + FROM + "\">contact us</a></i>",         
	 "text/html");

         // Send message
         Transport.send(message);         
      } catch (MessagingException mex) { mex.printStackTrace(); }        
    }
    
    private Integer getUID()
    {
        Integer result = null;
        
        try {
            Process p = Runtime.getRuntime().exec("id -u");
            BufferedReader is = new BufferedReader(
                new InputStreamReader(p.getInputStream())
            );

            result = Integer.parseInt(is.readLine());
        }
        catch (java.io.IOException e) {
            log.error(e.getMessage());      
        }
        
        return result;
    }

    private void getRobotProxy (String eTokenServer, 
                                String eTokenServerPort, 
                                String proxyId, 
                                String VO, 
                                String FQAN, 
                                String proxyRenewal) 
    {
        File proxyFile;
        Integer UID = getUID();

        proxyFile = new File("/tmp/x509up_u" + UID);
        
        String proxyContent="";
        
        try {
            
            URL proxyURL = 
                    new URL("http://" 
                    + eTokenServer
                    + ":"
                    + eTokenServerPort
                    + "/eTokenServer/eToken/" 
                    + proxyId 
                    + "?voms=" 
                    + VO 
                    + ":/" 
                    + VO 
                    + "&proxy-renewal=" 
                    + proxyRenewal
                    + "&disable-voms-proxy=false&rfc-proxy=true&cn-label=Insert");            
                    
            URLConnection proxyConnection = proxyURL.openConnection();
            proxyConnection.setDoInput(true);
            
            InputStream proxyStream = proxyConnection.getInputStream();
            BufferedReader input = new BufferedReader(new  InputStreamReader(proxyStream));
            
            String line = "";
            while ((line = input.readLine()) != null)                                 
                proxyContent += line+"\n";
                        
            FileUtils.writeStringToFile(proxyFile, proxyContent);
        } catch (Exception e) { e.printStackTrace(); }        
    }
}
