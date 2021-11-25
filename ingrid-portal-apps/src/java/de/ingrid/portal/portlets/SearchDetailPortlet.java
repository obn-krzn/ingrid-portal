/*
 * **************************************************-
 * Ingrid Portal Apps
 * ==================================================
 * Copyright (C) 2014 - 2021 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.portal.portlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.apache.jetspeed.PortalReservedParameters;
import org.apache.jetspeed.request.RequestContext;
import org.apache.portals.bridges.velocity.GenericVelocityPortlet;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.generic.EscapeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import de.ingrid.portal.config.IngridSessionPreferences;
import de.ingrid.portal.config.PortalConfig;
import de.ingrid.portal.global.CodeListServiceFactory;
import de.ingrid.portal.global.IngridResourceBundle;
import de.ingrid.portal.global.IngridSysCodeList;
import de.ingrid.portal.global.Settings;
import de.ingrid.portal.global.UniversalSorter;
import de.ingrid.portal.global.UtilsDB;
import de.ingrid.portal.global.UtilsMimeType;
import de.ingrid.portal.global.UtilsQueryString;
import de.ingrid.portal.global.UtilsString;
import de.ingrid.portal.global.UtilsVelocity;
import de.ingrid.portal.interfaces.IBUSInterface;
import de.ingrid.portal.interfaces.impl.IBUSInterfaceImpl;
import de.ingrid.portal.search.IPlugVersionInspector;
import de.ingrid.portal.search.detail.DetailDataPreparer;
import de.ingrid.portal.search.detail.DetailDataPreparerFactory;
import de.ingrid.portal.search.detail.DetailDataPreparerHelper;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.PlugDescription;
import de.ingrid.utils.dsc.Record;
import de.ingrid.utils.idf.IdfTool;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;
import de.ingrid.utils.udk.UtilsLanguageCodelist;
import de.ingrid.utils.xml.IDFNamespaceContext;
import de.ingrid.utils.xml.XPathUtils;
import sun.misc.BASE64Encoder;

public class SearchDetailPortlet extends GenericVelocityPortlet {
    private static final Logger log = LoggerFactory.getLogger(SearchDetailPortlet.class);

    private static final String TEMPLATE_DETAIL_GENERIC = "/WEB-INF/templates/detail/search_detail_generic.vm";

    private static final String TEMPLATE_DETAIL_IDF_2_0_0 = "/WEB-INF/templates/detail/search_detail_idf_2_0.vm";
    
    // ecs fields that represent a date, used for date parsing and formating
    private List dateFields = null;
    
    private HashMap replacementFields = new HashMap();

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws IOException {
        String resourceID = request.getResourceID();
        String paramURL = request.getParameter( "url" );

        String login = PortalConfig.getInstance().getString(PortalConfig.PORTAL_DETAIL_UVP_DOCUMENTS_HTACCESS_LOGIN);
        String password = PortalConfig.getInstance().getString(PortalConfig.PORTAL_DETAIL_UVP_DOCUMENTS_HTACCESS_PASSWORD);

        IngridResourceBundle messages = new IngridResourceBundle(getPortletConfig().getResourceBundle(
                request.getLocale()), request.getLocale());

        if(paramURL != null){
            if (resourceID.equals( "httpURL" )) {
                try {
                    URL url = new URL(paramURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    if(StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(password)) {
                        urlConnectionAuth(con, login, password);
                    }
                    con.setRequestMethod("HEAD");
                    response.setContentType( "application/javascript" );
                    StringBuilder s = new StringBuilder();
                    response.getWriter().write( "{" );
                    if(con.getContentLength() > 0 && con.getContentType().indexOf( "text" ) < 0){
                        s.append( "\"contentLength\":");
                        s.append( "\"" + con.getContentLength() + "\"" );
                    }
                    response.getWriter().write( s.toString() );
                    response.getWriter().write( "}" );
                } catch (Exception e) {
                    log.error("Error get contentLength from: " + paramURL, e);
                    response.getWriter().write( "{}" );
                }
            }

            if (resourceID.equals( "httpURLDataType" )) {
                String extension = "";
                if(paramURL != null) {
                    if(paramURL.toLowerCase().indexOf("service=csw") > -1) {
                        extension = "csw";
                    } else if(paramURL.toLowerCase().indexOf("service=wms") > -1) {
                        extension = "wms";
                    } else if(paramURL.toLowerCase().indexOf("service=wfs") > -1) {
                        extension = "wfs";
                    } else if(paramURL.toLowerCase().indexOf("service=wmts") > -1) {
                        extension = "wmts";
                    }
                    if(extension.isEmpty()) {
                        try {
                            URL url = new URL(paramURL);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("HEAD");
                            if(StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(password)) {
                                urlConnectionAuth(con, login, password);
                            }
                            String contentType = con.getContentType();
    
                            if((contentType == null || contentType.equals("text/html")) && paramURL.startsWith("http://")) {
                                url = new URL(paramURL.replace("http://", "https://"));
                                con = (HttpURLConnection) url.openConnection();
                                if(StringUtils.isNotEmpty(password)) {
                                    urlConnectionAuth(con, login, password);
                                }
                                con.setRequestMethod("HEAD");
                                contentType = con.getContentType();
                                if(contentType != null) {
                                    extension = UtilsMimeType.getFileExtensionOfMimeType(contentType.split(";")[0]);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Error get datatype from: " + paramURL, e);
                        }
                    }
                    response.setContentType( "text/plain" );
                    response.getWriter().write( extension );
                }
            }

            if (resourceID.equals( "httpURLImage" )) {
                try {
                    getURLResponse(paramURL, response);
                } catch (Exception e) {
                    log.error( "Error creating resource for resource ID: " + resourceID, e );
                    if (resourceID.equals( "httpURLImage" )) {
                        log.error( "Error creating HTTP resource for resource ID: " + resourceID, e );
                        String httpsUrl = paramURL.replace("http", "https").replace(":80/", "/");
                        log.error( "Try https URL: " + httpsUrl);
                        try {
                            getURLResponse(httpsUrl, response);
                            log.error( "Try https URL: " + httpsUrl);
                        } catch (Exception e1) {
                            log.error( "Error creating HTTPS resource for resource ID: " + resourceID, e );
                            response.getWriter().write(paramURL);
                        }
                    }
                }
            }
        }

        if (resourceID.equals( "httpURLDownloadUVP" )) {
            String uuid = request.getParameter( "docuuid" );
            String plugid = request.getParameter( "plugid" );
            File zip = searchFilesToCreateZip(uuid, plugid, messages);
            if(zip != null) {
                try(
                    OutputStream outputStream = response.getPortletOutputStream();
                    FileInputStream fileInputStream = new FileInputStream(zip.getAbsoluteFile());
                    BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(fileInputStream);
                ){
                    response.setContentType( "application/zip" );
                    response.addProperty("Content-Disposition", "attachment; filename=\"" + zip.getName() + "\"");
                    response.setContentLength(bufferedInputStream.available());
                    byte[] buffer = new byte[4096];
                    int len = 0;
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                      outputStream.write(buffer, 0, len);
                    }
                }
            }
        }

        if (resourceID.equals( "httpURLDownloadUVPCreate" )) {
            String uuid = request.getParameter( "docuuid" );
            String plugid = request.getParameter( "plugid" );
            searchFilesToCreateZip(uuid, plugid, messages);
            response.flushBuffer();
        }
    }

    private void urlConnectionAuth(URLConnection conn, String login, String password) {
        String userPassword = login + ":" + password;
        String encoding = new BASE64Encoder().encode(userPassword.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encoding);
    }

    private File searchFilesToCreateZip(String uuid, String plugid, IngridResourceBundle messages) {
        File zip = null;
        IBUSInterface ibus = IBUSInterfaceImpl.getInstance();
        String query = Settings.HIT_KEY_OBJ_ID + ":" + uuid + " iplugs:\"" + plugid + "\" ranking:score";
        try {
            IngridQuery q = QueryStringParser.parse(query);
            IngridHits hits = ibus.search(q, 1, 1, 0, 3000);
            IngridHit hit = null;
            if(hits.getHits().length > 0) {
                hit = hits.getHits()[0];
            }
            if(hit != null) { 
                Record record = ibus.getRecord(hit);
                String idfString = IdfTool.getIdfDataFromRecord(record);

                if(idfString != null){
                    if(log.isDebugEnabled()){
                        log.debug(idfString);
                    } 
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
                    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                    DocumentBuilder db;
                    db = dbf.newDocumentBuilder();
                    Document idfDoc = db.parse(new InputSource(new StringReader(idfString)));
                    
                    XPathUtils.getXPathInstance(new IDFNamespaceContext());
                    
                    Element root = idfDoc.getDocumentElement();
                    String title = plugid.replace(':', '_') + uuid;
                    String date = "";
                    String xpathExpression = "//idf:idfMdMetadata/name";
                    if(XPathUtils.nodeExists(root, xpathExpression)) {
                        title = XPathUtils.getString(root, xpathExpression);
                    }
                    xpathExpression = "//idf:idfMdMetadata/date";
                    if(XPathUtils.nodeExists(root, xpathExpression)) {
                        date = XPathUtils.getString(root, xpathExpression);
                    }
                    String downloadPath = createDownloadPath(plugid, uuid, date);
                    xpathExpression = "//idf:idfMdMetadata/steps/step[docs/doc]";
                    if(XPathUtils.nodeExists(root, xpathExpression)) {
                        zip = createDownload(uuid, title, downloadPath, XPathUtils.getNodeList(root, xpathExpression), messages);
                    }
                    xpathExpression = "//idf:idfMdMetadata[docs/doc]";
                    if(XPathUtils.nodeExists(root, xpathExpression)) {
                        zip = createDownload(uuid, title, downloadPath, XPathUtils.getNodeList(root, xpathExpression), messages);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error create download file for iPlug '" + plugid + "' and docuuid '" + uuid + "':", e);
        }
        return zip;
    }

    private File createDownload(String uuid, String title, String downloadPath, NodeList nodeList, IngridResourceBundle messages) {
        String zipName = downloadPath + "/" + title.replaceAll("[\\\\/:*?\"<>|]", "") + ".zip";
        File zipFile = new File(zipName);
        File processFile = new File(downloadPath + "/CREATE");
        if(!zipFile.exists()) {
            if(nodeList.getLength() > 0) {
                try (
                    FileOutputStream f = new FileOutputStream(zipName);
                    ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(f));
                ){
                    // Create download status file
                    if(processFile.createNewFile()) {
                        log.debug("Create download process file.");
                    }
                    ArrayList<String> stepFoldernames = new ArrayList<String>(); 
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node stepNode = nodeList.item(i);
                        String stepType = "";
                        String date = "";
                        if(XPathUtils.nodeExists(stepNode, "./@type")) {
                            stepType = XPathUtils.getString(stepNode, "./@type");
                        }
                        if(XPathUtils.nodeExists(stepNode, "./datePeriod/from | ./date/from")) {
                            date = XPathUtils.getString(stepNode, "./datePeriod/from | ./date/from");
                            date = date.split("T")[0];
                            date = date.replace("-", "");
                        }
                        if(XPathUtils.nodeExists(stepNode, "./docs/doc")) {
                            String stepFoldername = date;
                            if(!stepType.isEmpty()) {
                                stepFoldername = stepFoldername + "_" + messages.getString("common.steps.uvp." + stepType);
                            } else {
                                stepType = "neg";
                            }
                            // Create ZIP step folder
                            if(stepFoldernames.indexOf(stepFoldername) > -1) {
                                int count = 1;
                                for (int j = 0; j < stepFoldernames.size(); j++) {
                                    if(stepFoldernames.get(j).equals(stepFoldername)) {
                                        count++;
                                    }
                                }
                                stepFoldernames.add(stepFoldername);
                                stepFoldername = stepFoldername + " (" + count + ")";
                            } else {
                                stepFoldernames.add(stepFoldername);
                            }
                            if(XPathUtils.nodeExists(stepNode, "./docs")) {
                                NodeList docsList = XPathUtils.getNodeList(stepNode, "./docs");
                                for (int j = 0; j < docsList.getLength(); j++) {
                                    Node docsNode = docsList.item(j);
                                    String docType = "";
                                    if(XPathUtils.nodeExists(docsNode, "./@type")) {
                                        docType = XPathUtils.getString(docsNode, "./@type");
                                    }
                                    if(!docType.isEmpty()) {
                                        if(!stepType.isEmpty()) {
                                            docType = messages.getString("search.detail.uvp." + stepType + ".doc." + docType);
                                        }
                                    }
                                    // Create ZIP doc folder into step folder
                                    String docFoldername = stepFoldername + "/" + docType;
                                    if(XPathUtils.nodeExists(docsNode, "./doc")) {
                                        zip.putNextEntry(new ZipEntry(docFoldername + "/"));
                                        if(docsNode.hasChildNodes()) {
                                            if(XPathUtils.nodeExists(docsNode, "./doc")) {
                                                NodeList docList = XPathUtils.getNodeList(docsNode, "./doc");
                                                ArrayList<String> docFilenames = new ArrayList<String>(); 
                                                for (int k = 0; k < docList.getLength(); k++) {
                                                    Node doc = docList.item(k);
                                                    // Download links and add to ZIP
                                                    if(XPathUtils.nodeExists(doc, "./link")) {
                                                        String link = XPathUtils.getString(doc, "./link");
                                                        String label = XPathUtils.getString(doc, "./label");
                                                        String[] linkSplit = link.split(uuid + "/");
                                                        if(linkSplit.length == 1) {
                                                            linkSplit = link.split("/");
                                                        }
                                                        String docFilename = linkSplit[linkSplit.length - 1];
                                                        if(docFilename.indexOf(".") == -1) {
                                                            docFilename = label + "_" + link;
                                                        }
                                                        if(docFilenames.indexOf(docFilename) > -1) {
                                                            int count = 1;
                                                            for (int l = 0; l < docFilenames.size(); l++) {
                                                                if(docFilenames.get(l).equals(docFilename)) {
                                                                    count++;
                                                                }
                                                            }
                                                            docFilenames.add(docFilename);
                                                            docFilename = docFilename + " (" + count + ")";
                                                        } else {
                                                            docFilenames.add(docFilename);
                                                        }
                                                        try (
                                                            InputStream in = new URL(link).openStream();
                                                        ){
                                                            ZipEntry zipEntry = new ZipEntry(docFoldername + "/" + URLDecoder.decode(docFilename, "UTF-8"));
                                                            zip.putNextEntry(zipEntry);
                                                            zipEntry.getName();
                                                            byte[] bytes = new byte[1024];
                                                            int length;
                                                            while ((length = in.read(bytes)) >= 0) {
                                                                zip.write(bytes, 0, length);
                                                            }
                                                        } catch (IOException e) {
                                                            log.error("Error download file for ZIP: '" + title + "'", e);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("Error create download file: " + zipName, e);
                    if(zipFile.delete()) {
                        log.debug("Delete download zip file!");
                    }
                } finally {
                    // Delete download status file
                    if(processFile.delete()) {
                        log.debug("Delete download process file!");
                    }
                }
            }
        } else if(processFile.exists()) {
            // Wait until download file create finished
            while (processFile.exists()) {
                log.debug("ZIP is in process. Wait for download file.");
            }
        }
        return zipFile;
    }

    private String createDownloadPath(String plugid, String uuid, String date) {
        File dirData = new File(PortalConfig.getInstance().getString(PortalConfig.PORTAL_DETAIL_UVP_ZIP_PATH, "./data"));
        if(!dirData.exists()) {
            dirData.mkdirs();
        }
        File dirDownload = new File(dirData.getPath() + "/downloads");
        if(!dirDownload.exists()) {
            dirDownload.mkdirs();
        }
        File dirPlugId = new File(dirDownload.getPath() + "/" + plugid.replace(':', '_'));
        if(!dirPlugId.exists()) {
            dirPlugId.mkdirs();
        }
        File dirUuid = new File(dirPlugId.getPath() + "/" + uuid);
        if(!dirUuid.exists()) {
            dirUuid.mkdirs();
        }
        File dirDate = new File(dirUuid.getPath() + "/" + date.replace("-", ""));
        if(!dirDate.exists()) {
            dirDate.mkdirs();
        }
        return dirDate.getPath();
    }

    private void getURLResponse (String paramURL, ResourceResponse response) throws IOException, URISyntaxException {
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(paramURL)) {
            URL url = new URL(paramURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if(con.getContentType().indexOf("image/") > -1) {
                InputStream inStreamConvert = con.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                if (null != con.getContentType()) {
                    byte[] chunk = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inStreamConvert.read(chunk)) > 0) {
                        os.write(chunk, 0, bytesRead);
                    }
                    os.flush();
                    URI dataUri = new URI("data:" + con.getContentType() + ";base64," +
                            Base64.getEncoder().encodeToString(os.toByteArray()));
                    response.getWriter().write(dataUri.toString());
                }
            }
        }
    }

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

        // get fields from config that should be treated as date fields
        dateFields = Arrays.asList(PortalConfig.getInstance().getStringArray(PortalConfig.UDK_FIELDS_DATE));
        
        // get translation(replacement) rules from config file
        // map(map)
        String[] translations = PortalConfig.getInstance().getStringArray(PortalConfig.UDK_FIELDS_TRANSLATE);
        if (translations != null) {
            for (int i=0; i<translations.length; i++) {
                String[] translation = translations[i].split("\\|");
                if (!replacementFields.containsKey(translation[0])) {
                    replacementFields.put(translation[0], new HashMap());
                }
                Map replacements = (Map)replacementFields.get(translation[0]);
                replacements.put(translation[1], convertLangCode(translation[2]));
            }
        }
    }

    private String convertLangCode(String oldCode) {
    	if (oldCode.equals("121"))
    		return Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("de"));
    	else if (oldCode.equals("94"))
    		return Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("en"));
    	else {
    		return oldCode;
    	}		
	}

    @Override
	public void doView(javax.portlet.RenderRequest request, javax.portlet.RenderResponse response)
            throws PortletException, IOException {
	    long startTimer = 0;
	    
	    if (log.isDebugEnabled()) {
	        log.debug("Start building detail view.");
	        startTimer = System.currentTimeMillis();
	    }
	    
        Context context = getContext(request);

        IngridResourceBundle messages = new IngridResourceBundle(getPortletConfig().getResourceBundle(
                request.getLocale()), request.getLocale());
        context.put("MESSAGES", messages);
        context.put("lang", "de".equalsIgnoreCase(request.getLocale().getLanguage().toLowerCase()) ? "" : "en");
        context.put("Codelists", CodeListServiceFactory.instance());

        // add velocity utils class
        context.put("tool", new UtilsVelocity());
        context.put("stringTool", new UtilsString());
        context.put("escTool", new EscapeTool());
        context.put("sorter", new UniversalSorter(Locale.GERMAN) );

        context.put("transformCoupledCSWUrl", PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_SEARCH_HIT_TRANSFORM_COUPLED_CSW_URL, false)); 

        context.put("enableMapLink", PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false)); 
        
        context.put( "leafletBgLayerWMTS", PortalConfig.getInstance().getString(PortalConfig.PORTAL_MAPCLIENT_LEAFLET_BG_LAYER_WMTS));
        context.put( "leafletBgLayerAttribution", PortalConfig.getInstance().getString(PortalConfig.PORTAL_MAPCLIENT_LEAFLET_BG_LAYER_ATTRIBUTION));
        
        String [] leafletBgLayerWMS = PortalConfig.getInstance().getStringArray(PortalConfig.PORTAL_MAPCLIENT_LEAFLET_BG_LAYER_WMS);
        String leafletBgLayerWMSURL = leafletBgLayerWMS[0];
        if(leafletBgLayerWMSURL.length() > 0 && leafletBgLayerWMS.length > 1){
            context.put( "leafletBgLayerWMSUrl", leafletBgLayerWMSURL);
            StringBuilder leafletBgLayerWMSName = new StringBuilder("");
            for (int i = 1; i < leafletBgLayerWMS.length; i++) {
                leafletBgLayerWMSName.append(leafletBgLayerWMS[i]);
                if(i < (leafletBgLayerWMS.length - 1)) {
                    leafletBgLayerWMSName.append(",");
                }
            }
            context.put( "leafletBgLayerWMSName", leafletBgLayerWMSName.toString());
        }

        ResourceURL restUrl = response.createResourceURL();
        restUrl.setResourceID( "httpURL" );
        request.setAttribute( "restUrlHttpGet", restUrl.toString() );

        restUrl = response.createResourceURL();
        restUrl.setResourceID( "httpURLImage" );
        request.setAttribute( "restUrlHttpGetImage", restUrl.toString() );

        restUrl = response.createResourceURL();
        restUrl.setResourceID( "httpURLDataType" );
        request.setAttribute( "restUrlHttpGetDataType", restUrl.toString() );

        restUrl = response.createResourceURL();
        restUrl.setResourceID( "httpURLDownloadUVPCreate" );
        request.setAttribute( "restUrlHttpDownloadUVPCreate", restUrl.toString() );

        restUrl = response.createResourceURL();
        restUrl.setResourceID( "httpURLDownloadUVP" );
        request.setAttribute( "restUrlHttpDownloadUVP", restUrl.toString() );

        try {
        	// check whether we come from google (no IngridSessionPreferences)
        	boolean noIngridSession = false;
    		IngridSessionPreferences ingridPrefs =
    			(IngridSessionPreferences) request.getPortletSession().getAttribute(
    				IngridSessionPreferences.SESSION_KEY, PortletSession.APPLICATION_SCOPE);
    		if (ingridPrefs == null) {
            	noIngridSession = true;
    		}
            context.put("noIngridSession", noIngridSession);
            	
            String testIDF = request.getParameter("testIDF");
            String cswURL = request.getParameter("cswURL");
            String docUuid = request.getParameter("docuuid");
            String altDocumentId = request.getParameter("altdocid");
            String iplugId = request.getParameter("plugid");
            if(iplugId != null) {
                iplugId = iplugId.toLowerCase();
            }
            boolean isAddress = "address".equals( request.getParameter("type") );
            IngridHit hit = null;
            PlugDescription plugDescription = null;
            IBUSInterface ibus = IBUSInterfaceImpl.getInstance();
            String iPlugVersion = null;
            Record record = null;
            
            if (iplugId != null && iplugId.length() > 0) {

	            context.put("docUuid", docUuid);
	            context.put("plugId", iplugId);
	            
	            plugDescription = ibus.getIPlug(iplugId);
	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
	            
	            String[] partners = plugDescription.getPartners();
	            String plugPartner = PortalConfig.getInstance().getString(PortalConfig.PORTAL_SEARCH_RESTRICT_PARTNER, "bund");
	            if(partners != null && partners.length > 0) {
	                if(partners.length > 1) {
	                    List<String> tmpPartners = new ArrayList<String>(Arrays.asList(partners));
	                    tmpPartners.remove("bund");
	                    partners = tmpPartners.toArray(new String[0]);
	                }
	                plugPartner = partners[0];
	                context.put("plugPartner", plugPartner);
	            }
	            
	            if(plugDescription.getProviders() != null) {
	                ArrayList<String> plugProviders = new ArrayList<>();
	                for (String provider : plugDescription.getProviders()) {
	                    String dbProvider = UtilsDB.getProviderFromKey(provider);
	                    if(dbProvider != null) {
	                        plugProviders.add(dbProvider);
	                    }
                    }
	                context.put("plugProviders", plugProviders);
	            }

	            if(plugDescription.getDataSourceName() != null) {
                    context.put("plugDataSourceName", plugDescription.getDataSourceName());
                }
	            
	            // try to get the result for a objects UUID
	            if (docUuid != null && docUuid.length() > 0) {
	            	// remove possible invalid characters
	            	docUuid = UtilsQueryString.normalizeUuid(docUuid);
	            	String qStr = null;
	                if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_9_DSC_OBJECT)) {
                	  qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
                  	} else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_8_DSC_OBJECT)) {
	            		qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_5_DSC_OBJECT)) {
	            		qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_3_DSC_OBJECT)) {
	            		qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_2_DSC_OBJECT)) {
	            		qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_UDK_5_0_DSC_OBJECT)) {
	            		qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_UDK_5_0_DSC_ADDRESS)) {
	            		qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_5_DSC_ADDRESS)) {
	            		qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	            	} else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDC_1_0_2_DSC_ADDRESS)) {
	            		qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT)){
	                    // new iPlug IGE-DSC contains both: objects and addresses!
	                    if (isAddress) {
	                        qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score datatype:address";
	                    } else {
	                        qStr = Settings.HIT_KEY_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                    }
	                } else if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDF_2_0_0_ADDRESS)){
	                	qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                } else {
	            		qStr = docUuid.trim() + " iplugs:\"" + iplugId.trim() + "\" ranking:score";
	                }
	
	            	IngridQuery q = QueryStringParser.parse(qStr);
	            	IngridHits hits = ibus.search(q, 1, 1, 0, 3000);
	            	
	            	if (hits.length() < 1) {
	            		log.error("No record found for document uuid:" + docUuid.trim() + " using iplug: " + iplugId.trim());
	            		
	            		qStr = Settings.HIT_KEY_ORG_OBJ_ID + ":\"" + docUuid.trim() + "\" iplugs:\"" + iplugId.trim() + "\" ranking:score";
	            		q = QueryStringParser.parse(qStr);
		            	hits = ibus.search(q, 1, 1, 0, 3000);
		            	if(hits.length() < 1){
		            		log.error("No object record found for document uuid:" + docUuid.trim() + " for field: " + Settings.HIT_KEY_ORG_OBJ_ID);
		            		
		            		qStr = Settings.HIT_KEY_OBJ_ID + ":" + docUuid.trim() + " ranking:score";
		  	                q = QueryStringParser.parse(qStr);
			            	hits = ibus.search(q, 1, 1, 0, 3000);
			            	if(hits.length() < 1){
			            		log.error("No object record found for document uuid:" + docUuid.trim() + " for field: " + Settings.HIT_KEY_OBJ_ID);
			            		
			            		qStr = Settings.HIT_KEY_ORG_OBJ_ID + ":" + docUuid.trim() + " ranking:score";
			  	                q = QueryStringParser.parse(qStr);
				            	hits = ibus.search(q, 1, 1, 0, 3000);
				            	if(hits.length() < 1){
				            		log.error("No object record found for document uuid:" + docUuid.trim() + " for field: " + Settings.HIT_KEY_ORG_OBJ_ID);
				            		
				            		qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":" + docUuid.trim() + " ranking:score datatype:address";
				  	                q = QueryStringParser.parse(qStr);
					            	hits = ibus.search(q, 1, 1, 0, 3000);
					            	if(hits.length() < 1){
					            		log.error("No address record found for document uuid:" + docUuid.trim() + " for field: " + Settings.HIT_KEY_ADDRESS_ADDRID);
					            	}else{
					            		hit = hits.getHits()[0];
					            		if(plugDescription == null){
					            			iplugId = hit.getPlugId();
					            			plugDescription = ibus.getIPlug(iplugId);
					        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
					            		}
					            	}
				            	}else{
				            		hit = hits.getHits()[0];
				            		if(plugDescription == null){
				            			iplugId = hit.getPlugId();
				            			plugDescription = ibus.getIPlug(iplugId);
				        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
				            		}
				            	}
			            	}else{
			            		hit = hits.getHits()[0];
			            		if(plugDescription == null){
			            			iplugId = hit.getPlugId();
			            			plugDescription = ibus.getIPlug(iplugId);
			        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
			            		}
			            	}
		            	}else{
		            		hit = hits.getHits()[0];
		            	}
	            	} else {
	            		hit = hits.getHits()[0];
	            	}
	            } else if (altDocumentId != null && altDocumentId.length() > 0) {
	                hit = new IngridHit();
                    hit.put("alt_document_id", altDocumentId);
	                hit.setPlugId(iplugId);
	                hit.setDocumentId("0");
	            
	            } else {
	                String documentId = request.getParameter("docid");
	                hit = new IngridHit();
	                hit.setDocumentId(documentId);
	                hit.setPlugId(iplugId);
	                context.put("docId", documentId);
	                // backward compatibilty where docId was integer
	                try {
	                    hit.putInt( 0, Integer.valueOf( documentId ) );
	                } catch (NumberFormatException ex) { /* ignore */ }
	            }
            }else{
            	log.error("No plugId set for detail.");
        		if(docUuid != null && docUuid.length() > 0){
        			String qStr = Settings.HIT_KEY_OBJ_ID + ":" + docUuid.trim() + " ranking:score";
        			IngridQuery q = QueryStringParser.parse(qStr);
        			IngridHits hits = ibus.search(q, 1, 1, 0, 3000);
	            	if(hits.length() < 1){
	            		log.error("No object record found for document uuid:" + docUuid.trim() + " for field: " + Settings.HIT_KEY_OBJ_ID);
	            		
	            		qStr = Settings.HIT_KEY_ORG_OBJ_ID + ":" + docUuid.trim() + " ranking:score";
	        			q = QueryStringParser.parse(qStr);
	        			hits = ibus.search(q, 1, 1, 0, 3000);
	        			if(hits.length() < 1){
	        				qStr = Settings.HIT_KEY_ADDRESS_ADDRID + ":" + docUuid.trim() + " ranking:score datatype:address";
		  	                q = QueryStringParser.parse(qStr);
			            	hits = ibus.search(q, 1, 1, 0, 3000);
			            	if(hits.length() < 1){
			            		log.error("No object record found for document uuid:" + docUuid.trim());
			            	}else{
			            		hit = hits.getHits()[0];
			            		if(plugDescription == null){
			            			iplugId = hit.getPlugId();
			            			plugDescription = ibus.getIPlug(iplugId);
			        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
			            		}
			            	}
	        			}else{
	        				hit = hits.getHits()[0];
		            		if(plugDescription == null){
		            			iplugId = hit.getPlugId();
		            			plugDescription = ibus.getIPlug(iplugId);
		        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
		            		}
	        			}
	            	}else{
	            		hit = hits.getHits()[0];
	            		if(plugDescription == null){
	            			iplugId = hit.getPlugId();
	            			plugDescription = ibus.getIPlug(iplugId);
	        	            iPlugVersion = IPlugVersionInspector.getIPlugVersion(plugDescription);
	            		}
	            	}
            	}
            }

            if (hit != null) {
	            record = ibus.getRecord(hit);
            // TODO: remove code after the iplugs deliver IDF records
            } else if (testIDF != null) {
                // create IDF record, see below how the record will be filled
                record = new Record();
                iPlugVersion = IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT;	
            } else if (cswURL != null) {
                record = new Record();
                iPlugVersion = IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT;  
            }
            
            if (record == null) {
               	log.error("No record found for document id:" + (hit != null ? hit.getDocumentId() : null) + " using iplug: " + iplugId + " for request: " + ((RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE)).getRequest().getRequestURL() + "?" + ((RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE)).getRequest().getQueryString());
            } else {

                // set language code list
                HashMap sysLangHashs = new HashMap();

                HashMap sysLangHash = new HashMap();
                sysLangHash.put("sys_language.lang_id", Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("de")));
                sysLangHash.put("sys_language.name", "Deutsch");
                sysLangHash.put("sys_language.description", "Deutsch");
                sysLangHash.put("sys_language.def_lang", "1");
                sysLangHashs.put(Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("de")), sysLangHash);

                sysLangHash = new HashMap();
                sysLangHash.put("sys_language.lang_id", Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("en")));
                sysLangHash.put("sys_language.name", "English");
                sysLangHash.put("sys_language.description", "English");
                sysLangHash.put("sys_language.def_lang", "1");
                sysLangHashs.put(Integer.toString(UtilsLanguageCodelist.getCodeFromShortcut("en")), sysLangHash);

                context.put("sysLangList", sysLangHashs);

                // put codelist fetcher into context
                context.put("codeList", new IngridSysCodeList(request.getLocale()));

                DetailDataPreparerFactory ddpf = new DetailDataPreparerFactory(context, iplugId, dateFields, request, response, replacementFields);
                
                if (iPlugVersion.equals(IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT) || iPlugVersion.equals(IPlugVersionInspector.VERSION_IDF_2_0_0_ADDRESS)) {
                	setDefaultViewPage(TEMPLATE_DETAIL_IDF_2_0_0);
                } else {
                    setDefaultViewPage(TEMPLATE_DETAIL_GENERIC);
                }
                
                // if "testIDF"-Parameter exist, use DetailDataPreparer for "IDF" version
                // TODO: remove code after the iplugs deliver IDF records
                if (testIDF != null) {
                    File file = new File(testIDF);
                    if(file.exists()){  
                        StringBuilder stringBuilder = new StringBuilder();
                        Scanner scanner = new Scanner(file);
                        try {
                            while(scanner.hasNextLine()) {        
                                stringBuilder.append(scanner.nextLine() + "\n");
                            }
                        } finally {
                            scanner.close();
                        }
                        record.put("data", stringBuilder.toString());
                        record.put("compressed", "false");
                    }
                    
                    DetailDataPreparer detailPreparer; 
                    detailPreparer = ddpf.getDetailDataPreparer(IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT);
                    detailPreparer.prepare(record);
                } else if (cswURL != null) {
                    URL url = new URL(cswURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    if(br != null) {
                        record.put("data", IOUtils.toString(br));
                        record.put("compressed", "false");
                    }
                    DetailDataPreparer detailPreparer; 
                    detailPreparer = ddpf.getDetailDataPreparer(IPlugVersionInspector.VERSION_IDF_2_0_0_OBJECT);
                    detailPreparer.prepare(record);
                } else {
                    long startTimer2 = 0;
                    if (log.isDebugEnabled()) {
                        startTimer2 = System.currentTimeMillis();
                    }
	                DetailDataPreparer detailPreparer = ddpf.getDetailDataPreparer(iPlugVersion);
	                detailPreparer.prepare(record);
	                if (log.isDebugEnabled()) {
	                    log.debug("Executed detail preparer '" + detailPreparer.getClass().getName() + "' within " + (System.currentTimeMillis() - startTimer2) + "ms.");
	                }
                }
            }
        } catch (NumberFormatException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error getting result record. doc id is no valid number", e);
            } else if (log.isInfoEnabled()) {
                log.info("Error getting result record. doc id is no valid number" + "(switch to debug mode for exception.)");
            }
        } catch (Throwable t) {
            log.error("Error getting result record.", t);
        }

        if (log.isDebugEnabled()) {
            log.debug("Finished preparing detail data for view within " + (System.currentTimeMillis() - startTimer) + "ms.");
            startTimer = System.currentTimeMillis();
        }
        
        super.doView(request, response);
        
        // Add page title by hit title 
        if(context.get("title") != null){
            org.w3c.dom.Element title = response.createElement("title");
            title.setTextContent((String) context.get("title") + " - " + messages.getString("search.detail.portal.institution"));
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, title);
        }
        // Add page doi by hit for dublin-core
        if(context.get("doi") != null){
            org.w3c.dom.Element link = response.createElement("link");
            link.setAttribute("rel", "schema.DC");
            link.setAttribute("href", "http://purl.org/dc/elements/1.1/");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, link);
            if(context.get("title") != null){
                org.w3c.dom.Element meta = response.createElement("meta");
                meta.setAttribute("name", "DC.title");
                meta.setAttribute("content", (String) context.get("title"));
                response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, meta);
            }
            org.w3c.dom.Element meta = response.createElement("meta");
            meta.setAttribute("name", "DC.identifier");
            meta.setAttribute("content", (String) context.get("doi"));
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, meta);
        }
        if (log.isDebugEnabled()) {
            log.debug("Finished rendering detail data view within " + (System.currentTimeMillis() - startTimer) + "ms.");
        }
    }





    /**
     * @see org.apache.portals.bridges.velocity.GenericVelocityPortlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
     */
	@Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String cmd = request.getParameter("cmd");
        if (cmd == null) {
        } else if (cmd.equals("doShowAddressDetail")) {
            String addrId = request.getParameter("addrId");
            String plugId = DetailDataPreparerHelper.getAddressPlugIdFromPlugId(request.getParameter("plugid"));
            if(log.isDebugEnabled()){
            	log.debug("doShowAddressDetail addrId: " + addrId);
            	log.debug("doShowAddressDetail plugid: " + plugId);
            }
            try {
                IngridHit hit = getAddressHit(addrId, plugId);
                if(hit != null) {
                    response.setRenderParameter("docuuid", addrId);
                    response.setRenderParameter("plugid", hit.getPlugId());
                    if(log.isDebugEnabled()){
                    	log.debug("doShowAddressDetail hit.getPlugId(): " + hit.getPlugId());
                    }
                    if (hit.get(".alt_document_id") != null) {
                        response.setRenderParameter("altdocid", (String) hit.get("alt_document_id"));
                    }
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error fetching address data for address id: " + addrId, e);
                } else if (log.isInfoEnabled()) {
                    log.info("Error fetching address data for address id: " + addrId + "(switch to debug mode for exception.)");
                }
            }
        } else if (cmd.equals("doShowObjectDetail")) {
            String objId = request.getParameter("objId");
            String plugId = DetailDataPreparerHelper.getPlugIdFromAddressPlugId(request.getParameter("plugid"));
            if(log.isDebugEnabled()){
            	log.debug("doShowObjectDetail objId: " + objId);
            	log.debug("doShowObjectDetail plugid: " + plugId);
            }
            try {
                IngridHit hit = getObjectHit(objId, plugId);
                if(hit != null) {
                    response.setRenderParameter("docuuid", objId);
                    response.setRenderParameter("plugid", hit.getPlugId());
                    if(log.isDebugEnabled()){
                    	log.debug("doShowObjectDetail hit.getPlugId(): " + hit.getPlugId());
                    }
                    if (hit.get(".alt_document_id") != null) {
                        response.setRenderParameter("altdocid", (String) hit.get("alt_document_id"));
                    }
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error fetching object data for object id: " + objId, e);
                } else {
                    log.info("Error fetching object data for object id: " + objId + "(switch to debug mode for exception.)");
                }
            }
        } else if (cmd.equals("doShowDocument")) {
        	if (request.getParameter("docid") != null) {
            	response.setRenderParameter("docid", request.getParameter("docid"));
            }
            if (request.getParameter("docuuid") != null) {
            	response.setRenderParameter("docuuid", request.getParameter("docuuid"));
            }
            response.setRenderParameter("plugid", request.getParameter("plugid"));
            if (request.getParameter("alt_document_id") != null) {
                response.setRenderParameter("altdocid", request.getParameter("alt_document_id"));
            }
            if (request.getParameter("maxORs") != null) {
                response.setRenderParameter("maxORs", request.getParameter("maxORs"));
            }
            if (request.getParameter("maxSubORs") != null) {
                response.setRenderParameter("maxSubORs", request.getParameter("maxSubORs"));
            }
        }
    }



    private IngridHit getAddressHit(String addrId, String iPlugId) {
        String[] requestedMetadata = new String[7];
        requestedMetadata[0] = Settings.HIT_KEY_WMS_URL;
        requestedMetadata[1] = Settings.HIT_KEY_ADDRESS_CLASS;
        requestedMetadata[2] = Settings.HIT_KEY_ADDRESS_FIRSTNAME;
        requestedMetadata[3] = Settings.HIT_KEY_ADDRESS_LASTNAME;
        requestedMetadata[4] = Settings.HIT_KEY_ADDRESS_TITLE;
        requestedMetadata[5] = Settings.HIT_KEY_ADDRESS_ADDRESS;
        requestedMetadata[6] = Settings.HIT_KEY_ADDRESS_ADDRID;
        ArrayList result = (ArrayList) DetailDataPreparerHelper.getHits("T02_address.adr_id:".concat(addrId).concat(" iplugs:\"".concat(DetailDataPreparerHelper.getAddressPlugIdFromPlugId(iPlugId)).concat("\"")),
                requestedMetadata, null);
        if (!result.isEmpty()) {
            return (IngridHit) result.get(0);
        } else {
            return null;
        }
    }

    private IngridHit getObjectHit(String objId, String iPlugId) {
        String[] requestedMetadata = new String[2];
        requestedMetadata[0] = Settings.HIT_KEY_OBJ_ID;
        requestedMetadata[1] = Settings.HIT_KEY_UDK_CLASS;
        ArrayList result = (ArrayList) DetailDataPreparerHelper.getHits("T01_object.obj_id:".concat(objId).concat(" iplugs:\"".concat(DetailDataPreparerHelper.getPlugIdFromAddressPlugId(iPlugId)).concat("\"")),
                requestedMetadata, null);
        if (!result.isEmpty()) {
            return (IngridHit) result.get(0);
        } else {
            return null;
        }
    }


    
}
