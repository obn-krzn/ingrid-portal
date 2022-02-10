/*
 * **************************************************-
 * Ingrid Portal Apps
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
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
package de.ingrid.portal.search.detail.idf.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.ingrid.geo.utils.transformation.GmlToWktTransformUtil;
import de.ingrid.portal.config.PortalConfig;
import de.ingrid.portal.global.UtilsVelocity;
import de.ingrid.utils.capabilities.CapabilitiesUtils;
import de.ingrid.utils.capabilities.CapabilitiesUtils.ServiceType;

public class DetailPartPreparerIdfMetadata extends DetailPartPreparer{

    private static final Logger log = LoggerFactory.getLogger(DetailPartPreparerIdfMetadata.class);

    @Override
    public void init(Node node, String iPlugId, RenderRequest request, RenderResponse response, Context context) {
        super.init( node, iPlugId, request, response, context );

        this.templateName = "/WEB-INF/templates/detail/parts/detail_part_preparer_metadata.vm";
        this.localTagName = "idfMdMetadata";
    }
    
    public String getUdkObjectClassType() {
        String xpathExpression = ".";
        if(xPathUtils.nodeExists(rootNode, xpathExpression)){
            Node node = xPathUtils.getNode(rootNode, xpathExpression);
            if(node.hasChildNodes()){
                String hierachyLevel = "";
                String hierachyLevelName = "";
                
                xpathExpression = "./gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue";
                if(xPathUtils.nodeExists(node, xpathExpression)){
                    hierachyLevel = xPathUtils.getString(node, xpathExpression).trim();
                }
                
                xpathExpression = "./gmd:hierarchyLevelName/gco:CharacterString";
                if(xPathUtils.nodeExists(node, xpathExpression)){
                    hierachyLevelName = xPathUtils.getString(node, xpathExpression).trim();
                }
                
                if(hierachyLevel.equalsIgnoreCase("service")){
                    return "3";
                }else if(hierachyLevel.equalsIgnoreCase("application")){
                    return "6";
                }else if(hierachyLevelName.equalsIgnoreCase("job") && hierachyLevel.equals("nonGeographicDataset")){
                    return "0";
                }else if(hierachyLevelName.equalsIgnoreCase("document") && hierachyLevel.equals("nonGeographicDataset")){
                    return "2";
                }else if(hierachyLevelName.equalsIgnoreCase("project") && hierachyLevel.equals("nonGeographicDataset")){
                    return "4";
                }else if(hierachyLevelName.equalsIgnoreCase("database") && hierachyLevel.equals("nonGeographicDataset")){
                    return "5";
                }else if(hierachyLevel.equalsIgnoreCase("dataset") || hierachyLevel.equals("series")){
                    return "1";
                }else if(hierachyLevel.equalsIgnoreCase("tile")){
                    // tile should be mapped to "Geoinformation/Karte" explicitly, see INGRID-2225
                    return "1";
                } else {
                    // Default to "Geoinformation/Karte", see INGRID-2225
                    return "1";
                }
            }
        }
        return "1"; 
    }
    
    public String getTitle(){
        String value = null;
        String xpathExpression = "./gmd:identificationInfo/*/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString";
        Node node = xPathUtils.getNode(this.rootNode, xpathExpression);
        if(node != null && node.getTextContent().length() > 0){
            value = node.getTextContent();
        }
        return value;
    }
    
    public String getTimeStamp(String xpathExpression){
        String value = null;
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            Node node = xPathUtils.getNode(rootNode, xpathExpression);
            String modTime = "";
            if(xPathUtils.nodeExists(node, "./gco:DateTime")){
                modTime = getDateFormatValue(xPathUtils.getString(node, "./gco:DateTime").trim());
            }else if(xPathUtils.nodeExists(node, "./gco:Date")){
                modTime = getDateFormatValue(xPathUtils.getString(node, "./gco:Date").trim());
            }else {
                modTime = getDateFormatValue(xPathUtils.getString(node, ".").trim());
            }
            if(modTime.length() > 0){
                value = modTime;
            }
        }
        return value;
    }

    public List<String> getAlternateTitle(){
        List<String> listSearch = new ArrayList<>();
        String xpathExpression = "./gmd:identificationInfo/*/gmd:citation/gmd:CI_Citation/gmd:alternateTitle/gco:CharacterString";
        NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
        for (int i=0; i< nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String content = node.getTextContent();
            String codelistAdvGroup = "8010";
            String codelistValue = sysCodeList.getNameByCodeListValue(codelistAdvGroup, content);
            if(codelistValue.isEmpty()) {
                listSearch.add(content);
            }
        }
        return listSearch;
    }

    public List<String> getAlternateTitleListFromCodelist(String codelist){
        List<String> listSearch = new ArrayList<>();
        String xpathExpression = "./gmd:identificationInfo/*/gmd:citation/gmd:CI_Citation/gmd:alternateTitle/gco:CharacterString";
        NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
        for (int i=0; i< nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String content = node.getTextContent();
            String codelistValue = sysCodeList.getNameByCodeListValue(codelist, content);
            if(!codelistValue.isEmpty()) {
                listSearch.add(codelistValue);
            }
        }
        return listSearch;
    }

    public String getDescription(){
        String value = null;
        
        String xpathExpressionAbstract = "./idf:abstract";
        Node abstractParentNode = rootNode;
        Node node = xPathUtils.getNode(rootNode, "./gmd:identificationInfo/*");
        
        if (!xPathUtils.nodeExists(abstractParentNode, xpathExpressionAbstract)) {
            xpathExpressionAbstract = "./gmd:abstract";
            abstractParentNode = node;
        }
        value = xPathUtils.getString(abstractParentNode, xpathExpressionAbstract);
        if (value != null) {
            value = removeLocalisation(value);
            value = value.trim();
        }

        return value;
    }
    
    public Map<String, Object> getMapImage(){
        return getMapImage(null, false);
    }

    public Map<String, Object> getMapImage(String partner, boolean isNewLayout){
        HashMap<String, Object> map = new HashMap<>();
        // showMap/Preview-Link
        if ( getUdkObjectClassType().equals("1") && PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false)) {
            // first try to get any valid WMS url from the crossReference section
            String xpathCrossReference = "./idf:crossReference/idf:serviceType[text() = 'view']/../idf:serviceOperation[text() = 'GetCapabilities']/../idf:serviceUrl";
            NodeList crossReferenceNodeList = xPathUtils.getNodeList(rootNode, xpathCrossReference);
            if(crossReferenceNodeList.getLength() > 0) {
                for (int i=0; i< crossReferenceNodeList.getLength(); i++) {
                    Node crossReferenceNode = crossReferenceNodeList.item(i);
                    String getCapUrl = crossReferenceNode.getTextContent();
                    if ( !getCapUrl.isEmpty() ) {
                        // since this link will be going to the webmap-client, the service must be WMS!
                        getCapUrl += CapabilitiesUtils.getMissingCapabilitiesParameter( getCapUrl, ServiceType.WMS );
                        getCapUrl = UtilsVelocity.urlencode( getCapUrl + "||");
                        
                        String layerIdentifier = getLayerIdentifier(null);
                        if(!layerIdentifier.equals("NOT_FOUND")) {
                            getCapUrl += UtilsVelocity.urlencode(layerIdentifier);
                        }
                        
                        if(!getCapUrl.isEmpty()) {
                            map = addBigMapLink(crossReferenceNode.getParentNode(), getCapUrl, false, partner, isNewLayout);
                            if(!hasAccessConstraints(crossReferenceNode.getParentNode())) {
                                break;
                            }
                        }
                    }
                }
            } else {
                // search for it in onlineResources
                String xpathExpression = "./gmd:distributionInfo/*/gmd:transferOptions";
                map = getCapabilityUrls(xpathExpression);
            }
            
        // otherwise try within distribution info or SV_OperationMetadata
        } else if ( getUdkObjectClassType().equals("3")  && PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false)) {
            String capabilitiesUrl = getCapabilityUrl();
            if(capabilitiesUrl != null){
                capabilitiesUrl += CapabilitiesUtils.getMissingCapabilitiesParameter( capabilitiesUrl, ServiceType.WMS ) + "||";
            }
            // get it directly from the operation
            map = addBigMapLink(rootNode, capabilitiesUrl, true, partner, isNewLayout);
        } else {
            // show preview image (with map link information if provided)
            map = getPreviewImage("./gmd:identificationInfo/*/gmd:graphicOverview/gmd:MD_BrowseGraphic/gmd:fileName/gco:CharacterString");
        }
        
        return map;
    }
    
    public List<HashMap<String, Object>> getReference(String xpathExpression){
        return getReference(xpathExpression, false); 
    }
        
    public List<HashMap<String, Object>> getReference(String xpathExpression, boolean isCoupled){
        ArrayList<HashMap<String, Object>> linkList = new ArrayList<>();
        
        int limitReferences = PortalConfig.getInstance().getInt(PortalConfig.PORTAL_DETAIL_VIEW_LIMIT_REFERENCES, 100);
        
        String serviceType = null;
        
        serviceType = xPathUtils.getString(rootNode, "./gmd:identificationInfo/*/srv:serviceType");
        if(serviceType != null){
            serviceType = serviceType.trim();
        }
        
        if(xPathUtils.nodeExists(rootNode, xpathExpression)){
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i<nodeList.getLength();i++){
                
                if (linkList.size() >= limitReferences){
                    HashMap<String, Object> link = new HashMap<>();
                    link.put("type", "html");
                    link.put("body", messages.getString("info_limit_references"));
                    linkList.add(link);
                    break;
                }
                Node node = nodeList.item(i);
                String uuid = "";
                String title = "";
                String type = getUdkObjectClassType();
                String attachedToField = "";
                String entryId = "";
                String description = "";
                String serviceUrl = null;
                String objServiceType = null;
                String[] graphicOverview = null;
                String tmp = null;
                String[] tmpList = null;

                xpathExpression = "./@uuid";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    uuid = tmp.trim();
                }
                
                xpathExpression = "./idf:objectName";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    title = tmp.trim();
                }
                
                xpathExpression = "./idf:objectType";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    type = tmp.trim();
                }
                
                xpathExpression = "./idf:attachedToField";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    attachedToField = tmp.trim();
                }
                
                xpathExpression = "./idf:attachedToField/@entry-id";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    entryId = tmp.trim();
                }
                
                xpathExpression = "./idf:description";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    description = tmp.trim();
                }

                xpathExpression = "./idf:serviceType";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    objServiceType = tmp.trim();
                }
                
                xpathExpression = "./idf:serviceUrl";
                tmp = xPathUtils.getString(node, xpathExpression);
                if(tmp != null){
                    serviceUrl = tmp.trim();
                }
                
                xpathExpression = "./idf:graphicOverview";
                tmpList = xPathUtils.getStringArray(node, xpathExpression);
                if(tmpList != null){
                    graphicOverview = tmpList;
                }

                HashMap<String, Object> link = new HashMap<>();
                link.put("hasLinkIcon", true);
                link.put("isExtern", false);
                link.put("title", title);
                link.put("objectClass", type);
                link.put("serviceType", objServiceType);
                link.put("graphicOverview", graphicOverview);
                if (description.length() > 0) {
                    link.put("description", description);
                }
                if (attachedToField.length() > 0) {
                    link.put("attachedToField", attachedToField);
                }
                if(this.iPlugId != null){
                    if(uuid != null){
                        link.put("href", "?cmd=doShowDocument&docuuid="+uuid+"&plugid="+this.iPlugId);
                    }else{
                        link.put("href", "");
                    }
                }else{
                    link.put("href", "");
                }
                
                if(isCoupled && entryId.equals("3600")){
                    // add map links to data objects from services
                    if (type.equals("3")) {
                        if (PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false)) {
                            StringBuilder capabilityUrl = null;
                            // get link from operation (unique one)
                            if ((serviceType != null && serviceType.trim().equals("view")) || (objServiceType != null && objServiceType.trim().equals("view")) && !hasAccessConstraints(node)) {
                                if(serviceUrl != null){
                                    capabilityUrl= new StringBuilder(serviceUrl);
                                }
                            } else {
                                String tmpUrl = getCapabilityUrl();
                                if(tmpUrl != null) {
                                    capabilityUrl =  new StringBuilder(tmpUrl);
                                }
                            }
                            if ( capabilityUrl != null && capabilityUrl.length() != 0) {
                                capabilityUrl.append(CapabilitiesUtils.getMissingCapabilitiesParameter( capabilityUrl.toString(), ServiceType.WMS ));
                                link.put("mapLink", UtilsVelocity.urlencode(capabilityUrl.toString() + "||" + getLayerIdentifier(node)));
                            }
                        }
                        // do not show link relation for coupled resources (INGRID-2285)
                        link.remove("attachedToField");
                        linkList.add(link);
                    } else if (type.equals("1")) {
                        StringBuilder capUrl = new StringBuilder();
                        if (PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false)) {
                            if(serviceUrl != null){
                                capUrl.append(serviceUrl);
                            } else {
                                String xpathCrossReference = "./idf:crossReference[@uuid='" + uuid + "']/idf:serviceType[text() = 'view']/../idf:serviceOperation[text() = 'GetCapabilities']/../idf:serviceUrl";
                                NodeList crossReferenceNodeList = xPathUtils.getNodeList(rootNode, xpathCrossReference);
                                if(crossReferenceNodeList.getLength() > 0) {
                                    for (int j=0; j< crossReferenceNodeList.getLength(); j++) {
                                        Node crossReferenceNode = crossReferenceNodeList.item(j);
                                        String getCapUrl = crossReferenceNode.getTextContent();
                                        if(!getCapUrl.isEmpty()) {
                                            if(!hasAccessConstraints(crossReferenceNode.getParentNode())) {
                                                capUrl.append(crossReferenceNode.getTextContent());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if ( capUrl != null && capUrl.length() != 0 && !hasAccessConstraints(node)) {
                                // add possible missing parameters
                                capUrl.append(CapabilitiesUtils.getMissingCapabilitiesParameter( capUrl.toString() ));
                                link.put("mapLink",  UtilsVelocity.urlencode(capUrl.toString() + "||" + getLayerIdentifier(node)));
                            }
                        }
                        // do not show link relation for coupled resources (INGRID-2285)
                        link.remove("attachedToField");
                        linkList.add(link);
                    }
                }else{
                    if(!entryId.equals("3600")){
                        linkList.add(link);
                    }
                }
            }
        }
        return linkList;
    }

    public List<HashMap<String, Object>> getExternLinks(String xpathExpression) {
        return getExternLinks(xpathExpression, false);
    }

    public List<HashMap<String, Object>> getExternLinks(String xpathExpression, boolean isDownload) {
        ArrayList<HashMap<String, Object>> linkList = new ArrayList<>();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i<nodeList.getLength();i++){
                String url = "";
                String name = "";
                String description = "";
                String attachedToField = "";
                String applicationProfile = "";
                String size = "";
                float roundSize = 0;

                NodeList onLineList = xPathUtils.getNodeList(nodeList.item(i), "./gmd:MD_DigitalTransferOptions/gmd:onLine");
                
                for (int j=0; j<onLineList.getLength();j++){
                    
                    xpathExpression = "./*/gmd:linkage/gmd:URL";
                    if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                        url = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                    }
                    
                    xpathExpression = "./*/gmd:description";
                    if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                        description = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                    }
                    
                    xpathExpression = "./*/gmd:name";
                    if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                        name = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                    }
                    
                    xpathExpression = "./*/idf:attachedToField";
                    if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                        attachedToField = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                    }
                    
                    if(attachedToField.isEmpty()) {
                        xpathExpression = "./*/gmd:function/gmd:CI_OnLineFunctionCode/@codeListValue";
                        if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                            attachedToField = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                            if(!attachedToField.isEmpty()) {
                                attachedToField = sysCodeList.getNameByCodeListValue("2000", attachedToField);
                            }
                        }
                    }
                    
                    xpathExpression = "./*/gmd:applicationProfile";
                    if(xPathUtils.nodeExists(onLineList.item(j), xpathExpression)){
                        applicationProfile = xPathUtils.getString(onLineList.item(j), xpathExpression).trim();
                    }

                    // also mapped by T0112_media_option !!!
                    xpathExpression = "./gmd:MD_DigitalTransferOptions/gmd:transferSize";
                    if(xPathUtils.nodeExists(nodeList.item(i), xpathExpression)){
                        size = xPathUtils.getString(nodeList.item(i), xpathExpression).trim();
                        roundSize = Float.parseFloat(size);
                        roundSize = (float) (Math.round(roundSize * 1000) / 1000.0);
                    }

                    if(url.length() > 0){
                        HashMap<String, Object> link = new HashMap<>();
                        link.put("hasLinkIcon", true);
                      if (isDownload) {
                          link.put("isDownload", isDownload);
                          
                      } else {
                          link.put("isExtern", true);
                      }

                      if(!applicationProfile.isEmpty()) {
                          link.put("serviceType", applicationProfile);
                      }

                      link.put("href", url);

                      if(name.length() > 0){
                          link.put("title", name);
                        }else{
                            link.put("title", url);
                      }
                      if (description.length() > 0) {
                          link.put("description", description);
                      }
                      if (attachedToField.length() > 0) {
                          link.put("attachedToField", attachedToField);
                      }
                      if (size.length() > 0) {
                          link.put("linkInfo", "[" + roundSize + " MB]");
                      }
                      linkList.add(link);
                    }
                }
            }
        }
        return linkList;
    }
    
    public List<String> getIndexInformationKeywords(String xpathExpression, String keywordType) {
        List<String> listSearch = new ArrayList<>();
        List<String> listGemet = new ArrayList<>();
        List<String> listInspire = new ArrayList<>();
        List<String> listPriorityDataset = new ArrayList<>();
        List<String> listSpatialScope = new ArrayList<>();

        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String type = "";
                String thesaurusName = "";
                
                // type
                xpathExpression = "./gmd:MD_Keywords/gmd:type/gmd:MD_KeywordTypeCode/@codeListValue";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    type = xPathUtils.getString(node, xpathExpression);
                }
                
                // thesaurus
                xpathExpression = "./gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    thesaurusName = xPathUtils.getString(node, xpathExpression).trim();
                }
                
                // keywords
                xpathExpression = "./gmd:MD_Keywords/gmd:keyword/*[self::gco:CharacterString or self::gmx:Anchor]";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    NodeList keywordNodeList = xPathUtils.getNodeList(node, xpathExpression);
                    for (int j = 0; j < keywordNodeList.getLength(); j++) {
                        Node keywordNode = keywordNodeList.item(j);
                        String value = xPathUtils.getString(keywordNode, ".").trim(); 
                        if(value.length() < 1){
                            value = xPathUtils.getString(keywordNode, ".").trim();
                        }
                        boolean isHidden = false;
                        
                        if(value != null){
                            List hiddenKeywordList = PortalConfig.getInstance().getList(PortalConfig.PORTAL_DETAIL_VIEW_HIDDEN_KEYWORDS);
                            if(hiddenKeywordList != null){
                                for(int h=0; h < hiddenKeywordList.size(); h++){
                                    String hiddenValue = (String) hiddenKeywordList.get(h);
                                    if(value.equalsIgnoreCase(hiddenValue.toLowerCase())){
                                        isHidden = true; 
                                        break;
                                    }
                                }
                            }
                            
                            // "Service Classification, version 1.0"
                            if (!isHidden && !thesaurusName.contains("Service")) {
                                // "UMTHES Thesaurus"
                                if (thesaurusName.contains("UMTHES")) {
                                    listSearch.add(value);
                                // "GEMET - Concepts, version 2.1"
                                } else if (thesaurusName.contains("Concepts")) {
                                    String tmpValue = sysCodeList.getNameByCodeListValue("5200", value);
                                    if(tmpValue.isEmpty()){
                                        tmpValue = value;
                                    }
                                    listGemet.add(tmpValue);
                                    // "GEMET - INSPIRE themes, version 1.0"
                                } else if (thesaurusName.contains("priority")) {
                                    String tmpValue = sysCodeList.getNameByCodeListValue("6350", value);
                                    if(tmpValue.isEmpty()){
                                        tmpValue = value;
                                    }
                                    listPriorityDataset.add(tmpValue);
                                } else if (thesaurusName.contains("Spatial scope") || sysCodeList.hasCodeListDataKeyValue("6360", value, "thesaurusTitle", thesaurusName)) {
                                    String tmpValue = sysCodeList.getNameByCodeListValue("6360", value);
                                    if(tmpValue.isEmpty()){
                                        tmpValue = value;
                                    }
                                    listSpatialScope.add(tmpValue);
                                } else if (thesaurusName.contains("INSPIRE")) {
                                    String tmpValue = sysCodeList.getNameByCodeListValue("6100", value);
                                    if(tmpValue.isEmpty()){
                                        tmpValue = value;
                                    }
                                    listInspire.add(tmpValue);
                                    // "German Environmental Classification - Category, version 1.0"
                                } else if (thesaurusName.contains("German Environmental Classification")) {
                                    // do not used in detail view.

                                } else if (thesaurusName.contains("Further legal basis")) {
                                    // do not used in detail view.

                                } else if (thesaurusName.isEmpty() && type.isEmpty()) {
                                    listSearch.add(value);
                                } else{
                                    // try to match keyword to  Opendata Category
                                    String tmpValue = sysCodeList.getNameByData("6400", value);
                                    if(tmpValue.isEmpty()){
                                        tmpValue = value;
                                    }
                                    listSearch.add(tmpValue);
                                }
                            }
                        }
                    }
                }
            }
        }

        switch (keywordType) {
            case "gemet":
                sortList(listGemet);
                return listGemet;
            case "inspire":
                sortList(listInspire);
                return listInspire;
            case "priority":
                sortList(listPriorityDataset);
                return listPriorityDataset;
            case "spatial_scope":
                sortList(listSpatialScope);
                return listSpatialScope;
            default:
                sortList(listSearch);
                return listSearch;
        }
    }

    /**
     * @param xpathExpressions is the xpath where the relevant elements are, including author names, publish year, title, doi.
     * rootName is the mandatory rootXpath that repeats itself for the possibility of multiple authors.
     */
    public List<HashMap<String, Object>> getListAPACitation(Map<String, String> xpathExpressions) {
        return getListAPACitation(xpathExpressions, null);
    }

    /**
     * @param rootXpathExpression is the recurrent rootXpath that contains multiple citations; every element should have
     * relative xpath to the rootXpath.
     * when extracting a single citation, the second argument should not be given; the xpath for every element should
     * be absolute xpath.
     */
    public List<HashMap<String, Object>> getListAPACitation(Map<String, String> xpathExpressions, String rootXpathExpression) {
        List<HashMap<String, Object>> listCitation = new ArrayList<>();
        if(rootXpathExpression != null && xPathUtils.nodeExists(rootNode, rootXpathExpression)){
            // return multiple citations from recurrent rootXpath
            NodeList tmpNodeList = xPathUtils.getNodeList(rootNode, rootXpathExpression);
            for (int i = 0; i < tmpNodeList.getLength(); i++){
                Node node = tmpNodeList.item(i);
                HashMap<String, Object> element = getAPACitationElements(node, xpathExpressions);
                if (!element.isEmpty()){listCitation.add(element);}
            }
        } else {
            // return a single citation from absolute xpath
            Node root = this.rootNode;
            HashMap<String, Object> element = getAPACitationElements(root, xpathExpressions);
            if (!element.isEmpty()){listCitation.add(element);}
        }
        return listCitation;
    }

    public HashMap<String, Object> getAPACitationElements(Node node, Map<String, String> xpathExpressions){
        HashMap<String, Object> element = new HashMap<>();
        if (xpathExpressions.containsKey("rootName") && xPathUtils.nodeExists(node, xpathExpressions.get("rootName"))){
            String value = getAPACitationValueFromXpath("name", node, xpathExpressions.get("name"), xpathExpressions.get("rootName"));
            if ((value == null || value.trim().isEmpty()) && xpathExpressions.containsKey("org_name")) {
                String org = getAPACitationValueFromXpath("org_name", node, xpathExpressions.get("org_name"));
                if (org != null) {element.put("authors", org);}
            } else {
                element.put("authors", value);
            }
        }
        if (xpathExpressions.containsKey("year") && xPathUtils.nodeExists(node, xpathExpressions.get("year"))){
            String value = getAPACitationValueFromXpath("year", node, xpathExpressions.get("year"));
            if (value != null) {element.put("year", value);}
        }
        if (xpathExpressions.containsKey("title") && xPathUtils.nodeExists(node, xpathExpressions.get("title"))){
            String value = getAPACitationValueFromXpath("title", node, xpathExpressions.get("title"));
            if (value != null) {element.put("title", value);}
        }
        if (xpathExpressions.containsKey("publisher") && xPathUtils.nodeExists(node, xpathExpressions.get("publisher"))){
            String value = getAPACitationValueFromXpath("publisher", node, xpathExpressions.get("publisher"));
            if (value != null) {element.put("publisher", value);}
        }
        if (xpathExpressions.containsKey("doi") && xPathUtils.nodeExists(node, xpathExpressions.get("doi"))){
            String value = getAPACitationValueFromXpath("doi", node, xpathExpressions.get("doi"));
            if (value != null) {element.put("doi", value);}
        }
        if (xpathExpressions.containsKey("doi_type") && xPathUtils.nodeExists(node, xpathExpressions.get("doi_type"))){
            String value = getAPACitationValueFromXpath("doi_type", node, xpathExpressions.get("doi_type"));
            if ("Dataset".equals(value)) {element.put("doi_type", messages.getString("doi.dataset"));}
        }
        return element;
    }

    public String getAPACitationValueFromXpath(String type, Node node, String xpathExpression){
        return getAPACitationValueFromXpath(type, node, xpathExpression, null);
    }

    public String getAPACitationValueFromXpath(String type, Node node, String xpathExpression, String rootXpathExpression){
        if (type.equals("name")){
            StringBuilder value = new StringBuilder();
            NodeList nameList = xPathUtils.getNodeList(node, rootXpathExpression);
            for (int i = 0; i < nameList.getLength(); i++){
                Node nameNode = xPathUtils.getNode(nameList.item(i), xpathExpression);
                if (nameNode != null && nameNode.getTextContent().length() > 0){
                    if (!value.toString().equals("")){value.append(", ");}
                    String name = nameNode.getTextContent().trim();
                    // last name
                    List<String> nameSplits = Arrays.asList(name.split(","));
                    value.append(String.format("%s,", nameSplits.get(0)));
                    // first name
                    String[] firstnameSplits = nameSplits.get(1).trim().split(" ");
                    for (String split : firstnameSplits) {
                        value.append(String.format(" %s.", split.charAt(0)));
                    }
                }
            }
            return value.toString();
        } else {
            Node valueNode = xPathUtils.getNode(node, xpathExpression);
            if (valueNode != null && valueNode.getTextContent().length() > 0){
                String value = valueNode.getTextContent().trim();
                switch (type){
                    case "year":
                        Pattern pattern = Pattern.compile("\\d{4}");
                        Matcher matcher = pattern.matcher(value);
                        if (matcher.find()) {
                            return matcher.group(0);
                        }
                    default:
                        return value;
                }
            }
        }
        return null;
    }

    public Map getAvailability(String xpathExpression) {
        HashMap element = new HashMap();
        
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            Node node = xPathUtils.getNode(rootNode, xpathExpression);
            if (node.hasChildNodes()) {
                xpathExpression = "./gmd:distributionFormat/gmd:MD_Format";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    NodeList nodeList = xPathUtils.getNodeList(node, xpathExpression);
                    element.put("type", "table");
                    element.put("title", messages.getString("data_format"));
                    
                    ArrayList head = new ArrayList();
                    head.add(messages.getString("t0110_avail_format.name"));
                    head.add(messages.getString("t0110_avail_format.version"));
                    head.add(messages.getString("t0110_avail_format.file_decompression_technique"));
                    head.add(messages.getString("t0110_avail_format.specification"));
                    element.put("head", head);
                    ArrayList body = new ArrayList();
                    element.put("body", body);
                    
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node childNode = nodeList.item(i);
                        ArrayList row = new ArrayList();
                        
                        String name = "";
                        String version = "";
                        
                        xpathExpression = "./gmd:name";
                        if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                            name = xPathUtils.getString(childNode, xpathExpression).trim();
                        }
                        
                        xpathExpression = "./gmd:version";
                        if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                            version = xPathUtils.getString(childNode, xpathExpression).trim();
                        }
                        
                        if(!name.equals("Geographic Markup Language (GML)") && !version.equals("unknown")){
                            xpathExpression = "./gmd:name";
                            if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                                row.add(notNull(name));
                            } else {
                                row.add("");
                            }
                            
                            xpathExpression = "./gmd:version";
                            if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                                row.add(notNull(version));
                            } else {
                                row.add("");
                            }
                            
                            xpathExpression = "./gmd:fileDecompressionTechnique";
                            if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                                String value = xPathUtils.getString(childNode, xpathExpression).trim();
                                row.add(notNull(value));
                            } else {
                                row.add("");
                            }
                            
                            xpathExpression = "./gmd:specification";
                            if (xPathUtils.nodeExists(childNode, xpathExpression)) {
                                String value = xPathUtils.getString(childNode, xpathExpression).trim();
                                row.add(notNull(value));
                            } else {
                                row.add("");
                            }
                        }
                        
                        if (!isEmptyRow(row)) {
                            body.add(row);
                        }
                    }
                }
            }
        }
        return element;
    }

    public Map getMediumOptions(String xpathExpression) {
        HashMap element = new HashMap();
        
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            Node node = xPathUtils.getNode(rootNode, xpathExpression);
            xpathExpression = "./gmd:transferOptions";
            if (xPathUtils.nodeExists(node, xpathExpression)) {
                NodeList nodeList = xPathUtils.getNodeList(node, xpathExpression);
                element.put("type", "table");
                element.put("title", messages.getString("t0112_media_option.medium"));
                ArrayList head = new ArrayList();
                head.add(messages.getString("t0112_media_option.medium_name"));
                head.add(messages.getString("t0112_media_option.transfer_size") + " [MB]");
                head.add(messages.getString("t0112_media_option.medium_note"));
                element.put("head", head);
                ArrayList body = new ArrayList();
                element.put("body", body);
                
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nodeListItem = nodeList.item(i);
                    ArrayList row = new ArrayList();
                    if(xPathUtils.nodeExists(nodeListItem, "./gmd:MD_DigitalTransferOptions/gmd:offLine")){
                        
                        xpathExpression = "./gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:name/gmd:MD_MediumNameCode/@codeListValue";
                        if (xPathUtils.nodeExists(nodeListItem, xpathExpression)) {
                            String value = xPathUtils.getString(nodeListItem, xpathExpression).trim();
                            row.add(notNull(sysCodeList.getNameByCodeListValue("520", value)));
                        } else {
                            row.add("");
                        }
                        
                        xpathExpression = "./gmd:MD_DigitalTransferOptions/gmd:transferSize";
                        if (xPathUtils.nodeExists(nodeListItem, xpathExpression)) {
                            row.add(notNull(xPathUtils.getString(nodeListItem, xpathExpression)).trim());
                        } else {
                            row.add("");
                        }
                        
                        xpathExpression = "./gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:mediumNote";
                        if (xPathUtils.nodeExists(nodeListItem, xpathExpression)) {
                            row.add(notNull(xPathUtils.getString(nodeListItem, xpathExpression)).trim());
                        } else {
                            row.add("");
                        }
                        
                        if (!isEmptyRow(row)) {
                            body.add(row);
                        }
                    }
                }
            }
        }
        return element;
    }
    
    public Map getConformityData(String xpathExpression) {
        HashMap element = new HashMap();
        
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            element.put("type", "table");
            element.put("title", messages.getString("object_conformity"));
            
            ArrayList head = new ArrayList();
            head.add(messages.getString("object_conformity.specification"));
            head.add(messages.getString("object_conformity.publication_date"));
            head.add(messages.getString("object_conformity.degree_value"));
            head.add(messages.getString("object_conformity.explanation"));
            element.put("head", head);
            ArrayList body = new ArrayList();
            element.put("body", body);
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(xPathUtils.nodeExists(node, "./gmd:DQ_DomainConsistency")){
                    ArrayList row = new ArrayList();
                    
                    xpathExpression = "./gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString";
                    if (xPathUtils.nodeExists(node, xpathExpression)) {
                        String value = xPathUtils.getString(node, xpathExpression).trim();
                        row.add(notNull(value));
                    } else {
                        row.add("");
                    }

                    xpathExpression = "./gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date";
                    if (xPathUtils.nodeExists(node, xpathExpression)) {
                        if(xPathUtils.nodeExists(node, xpathExpression + "/@gco:nilReason")){
                            row.add("");
                        }else {
                            String value = xPathUtils.getString(node, xpathExpression).trim();
                            row.add(notNull(getDateFormatValue(value)));
                        }
                    } else {
                        row.add("");
                    }

                    xpathExpression = "./gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:pass";
                    if (xPathUtils.nodeExists(node, xpathExpression)) {
                        String value = xPathUtils.getString(node, xpathExpression).trim();
                        if(xPathUtils.nodeExists(node, xpathExpression + "/@gco:nilReason")){
                            row.add(notNull(sysCodeList.getName("6000", "3")));
                        }else{
                            if(value.equals("true")){
                                row.add(notNull(sysCodeList.getName("6000", "1")));
                            }else if(value.equals("false")){
                                row.add(notNull(sysCodeList.getName("6000", "2")));
                            }else{
                                row.add("");
                            }
                        }
                    } else {
                        row.add("");
                    }

                    xpathExpression = "./gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString";
                    if (xPathUtils.nodeExists(node, xpathExpression)) {
                        String value = xPathUtils.getString(node, xpathExpression).trim();
                        // only add explanation if non standard value REDMINE-1817
                        if (value.equals("see the referenced specification")){
                            row.add("");
                        } else {
                            row.add(notNull(value));
                        }
                    } else {
                        row.add("");
                    }
                    
                    if (!isEmptyRow(row)) {
                        body.add(row);
                    }
                }
            }
        }
        return element;
    }
    
    
    public List getServiceClassification(String xpathExpression) {
        ArrayList list = new ArrayList();
        
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String thesaurusName = "";
                
                // thesaurus
                xpathExpression = "./gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    thesaurusName = xPathUtils.getString(node, xpathExpression).trim();
                }
                
                // keywords
                xpathExpression = "./gmd:MD_Keywords/gmd:keyword";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    NodeList keywordNodeList = xPathUtils.getNodeList(node, xpathExpression);
                    for (int j = 0; j < keywordNodeList.getLength(); j++) {
                        Node keywordNode = keywordNodeList.item(j);
                        String value = xPathUtils.getString(keywordNode, ".").trim(); 
                        if(value.length() < 1){
                            value = xPathUtils.getString(keywordNode, ".").trim();
                        }
                        
                        // "Service Classification, version 1.0"
                        if (thesaurusName.contains("Service")) {
                            String tmpValue = sysCodeList.getNameByCodeListValue("5200", value);
                            if(tmpValue.length() > 0){
                                value = tmpValue;
                            }
                            list.add(value);
                        } 
                    }
                }
            }
        }
        sortList(list);
        return list;
    }
    
    public Map<String, Object> getReferenceObject(String xpathExpression) {
        HashMap element = new HashMap();
        
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList childNodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            ArrayList<String> listDominator = new ArrayList<>();
            ArrayList<String> listMeter = new ArrayList<>();
            ArrayList<String> listDpi = new ArrayList<>();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                xpathExpression = "./gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator";
                if (xPathUtils.nodeExists(childNodeList.item(j), xpathExpression)) {
                    listDominator.add(xPathUtils.getString(childNodeList.item(j), xpathExpression).trim());
                }
                
                xpathExpression = "./gmd:distance/gco:Distance[@uom='meter']";
                if (xPathUtils.nodeExists(childNodeList.item(j), xpathExpression)) {
                    listMeter.add(xPathUtils.getString(childNodeList.item(j), xpathExpression).trim());
                }
                
                xpathExpression = "./gmd:distance/gco:Distance[@uom='dpi']";
                if (xPathUtils.nodeExists(childNodeList.item(j), xpathExpression)) {
                    listDpi.add(xPathUtils.getString(childNodeList.item(j), xpathExpression).trim());
                }
            }
            
            element.put("type", "table");
            element.put("title", messages.getString("t011_obj_serv_scale"));
            
            ArrayList head = new ArrayList();
            head.add(messages.getString("t011_obj_serv_scale.scale").concat(" 1:x"));
            head.add(messages.getString("t011_obj_serv_scale.resolution_ground").concat(" m"));
            head.add(messages.getString("t011_obj_serv_scale.resolution_scan").concat(" dpi"));
            element.put("head", head);
            ArrayList body = new ArrayList();
            element.put("body", body);
            
            for (int i = 0; i < listDominator.size(); i++) {
                ArrayList row = new ArrayList();
                int value;
                
                value = listDominator.size();
                if (value != 0) {
                    if(value > i){
                        row.add(notNull((String) listDominator.get(i)));
                    }else{
                        row.add("");
                    }
                } else {
                    row.add("");
                }
                
                value = listMeter.size();
                if (value != 0) {
                    if(value > i){
                        row.add(notNull((String) listMeter.get(i)));
                    }else {
                        row.add("");
                    }
                } else {
                    row.add("");
                }
                
                value = listDpi.size();
                if (value != 0) {
                    if(value > i){
                        row.add(notNull((String) listDpi.get(i)));
                    }else{
                        row.add("");
                    }
                } else {
                    row.add("");
                }
                
                if (!isEmptyRow(row)) {
                    body.add(row);
                }
            }
        }
        return element;
    }
    
    public Map getConnectionPoints(String xpathExpression) {
        String serviceType = getValueFromXPath("./gmd:identificationInfo/*/srv:serviceType");
        if(serviceType != null){
            serviceType = serviceType.trim();
        }
        HashMap element = new HashMap();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            
            for (int i=0; i<nodeList.getLength();i++){
                if(xPathUtils.nodeExists(nodeList.item(i), "./gmd:CI_OnlineResource/gmd:linkage/gmd:URL")){
                    Node node = xPathUtils.getNode(nodeList.item(i), "./gmd:CI_OnlineResource/gmd:linkage/gmd:URL");
                    StringBuilder urlValue = new StringBuilder(xPathUtils.getString(node, ".").trim());
                    // do not display empty URLs
                    if (urlValue.length() == 0) {
                        continue;
                    }

                    // do not display empty operations
                    String operationName = xPathUtils.getString(nodeList.item(i), "./../srv:operationName").trim();
                    if (operationName == null || operationName.length() == 0) {
                        continue;
                    }

                    if (operationName.equals("GetCapabilities")) {
                        // add missing parameters
                        urlValue.append(CapabilitiesUtils.getMissingCapabilitiesParameter( urlValue.toString(), serviceType ));

                        element.put("type", "textLabelLeft");
                        element.put("line", true);

                        HashMap elementCapabilitiesLink = new HashMap();
                        elementCapabilitiesLink.put("type", "linkLine");
                        elementCapabilitiesLink.put("hasLinkIcon", true);
                        elementCapabilitiesLink.put("isExtern", true);
                        elementCapabilitiesLink.put("title", messages.getString("search.detail.showGetCapabilityUrl.title"));
                        elementCapabilitiesLink.put("href", urlValue);
                          element.put("body", elementCapabilitiesLink);

                        if (!hasAccessConstraints()) {
                            element.put("title", messages.getString("search.detail.showGetCapabilityUrl"));
                            if(PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false) 
                                    && (serviceType != null && (serviceType.trim().equalsIgnoreCase("view") || serviceType.trim().equalsIgnoreCase("wms")))){
                                HashMap elementMapLink = new HashMap();
                                elementMapLink.put("type", "linkLine");
                                elementMapLink.put("hasLinkIcon", true);
                                elementMapLink.put("isExtern", false);
                                elementMapLink.put("title", messages.getString("common.result.showMap"));
                                elementMapLink.put("href", UtilsVelocity.urlencode(urlValue.toString() + "||" ));
                                element.put("link", elementMapLink);
                                element.put("linkLeft", true);
                            }
                        } else {
                            // do not display "show in map" link if the map has access constraints
                            element.put("title", messages.getString("search.detail.showGetCapabilityUrlRestricted"));
                        }
                        // ADD FIRST ONE FOUND !!!
                        break;
                    }
                }
            }
        }
        return element;
    }
    
    public Map<String, Object> getAreaGeothesaurus(String xpathExpression){
        HashMap element = new HashMap();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            ArrayList subjectEntries = new ArrayList();
            for (int i = 0; i < nodeList.getLength(); i++) {
                xpathExpression = "./gmd:geographicElement/gmd:EX_GeographicDescription/gmd:geographicIdentifier/gmd:MD_Identifier/gmd:code/gco:CharacterString";
                Node node = nodeList.item(i);
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    NodeList childNodeList = xPathUtils.getNodeList(node, xpathExpression);
                    for (int j = 0; j < childNodeList.getLength(); j++) {
                        String domainValue = xPathUtils.getString(childNodeList.item(j), ".").trim();
                        subjectEntries.add(domainValue);
                    }
                }
                
                // "Geothesaurus-Raumbezug"
                xpathExpression = "./gmd:geographicElement/gmd:EX_GeographicBoundingBox";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    NodeList subNodeList = xPathUtils.getNodeList(node, xpathExpression);
                    element.put("type", "table");
                    element.put("title", messages.getString("geothesaurus_spacial_reference"));
                    
                    ArrayList head = new ArrayList();
                    head.add("");
                    head.add(messages.getString("spatial_ref_value_x1"));
                    head.add(messages.getString("spatial_ref_value_y1"));
                    element.put("head", head);
                    ArrayList body = new ArrayList();
                    element.put("body", body);
                    
                    ArrayList numbers = new ArrayList();
                    numbers.add(subjectEntries.size());
                    numbers.add(subNodeList.getLength());
                    int maxRows = getGreatestInt(numbers); 
                    
                    for (int j = 0; j < maxRows; j++) {
                        Node childNode = subNodeList.item(j);
                        ArrayList row = new ArrayList();

                        if(!subjectEntries.isEmpty()){
                            if (subjectEntries.get(j)!= null) {
                                row.add(subjectEntries.get(j));
                            }else {
                                row.add("");
                            }
                        } else {
                            row.add("");
                        }

                        if (xPathUtils.nodeExists(childNode, "./gmd:westBoundLongitude") && xPathUtils.nodeExists(childNode, "./gmd:southBoundLatitude")) {
                            String valueW = xPathUtils.getString(childNode, "./gmd:westBoundLongitude").trim();
                            String valueS = xPathUtils.getString(childNode, "./gmd:southBoundLatitude").trim();
                            row.add((Math.round(Double.parseDouble(valueW) * 1000000) / 1000000.0) + "\u00B0/" + (Math.round(Double.parseDouble(valueS) * 1000000) / 1000000.0) + "\u00B0");
                        } else {
                            row.add("");
                        }
                        if (xPathUtils.nodeExists(childNode, "./gmd:eastBoundLongitude") && xPathUtils.nodeExists(childNode, "./gmd:northBoundLatitude")) {
                            String valueE = xPathUtils.getString(childNode, "./gmd:eastBoundLongitude").trim();
                            String valueN = xPathUtils.getString(childNode,  "./gmd:northBoundLatitude").trim();
                            row.add((Math.round(Double.parseDouble(valueE) * 1000000) / 1000000.0) + "\u00B0/" + (Math.round(Double.parseDouble(valueN) * 1000000) / 1000000.0) + "\u00B0");
                        } else {
                            row.add("");
                        }

                        if (!isEmptyRow(row)) {
                            body.add(row);
                        }
                    }
                }
            }
        }
        return element;
    }
    
    public Map<String, Object> getAreaHeight(String xpathExpression){
        HashMap element = new HashMap();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                xpathExpression = "./gmd:verticalElement";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    element.put("type", "table");
                    element.put("title", messages.getString("t01_object.vertical_extent"));
                    
                    ArrayList head = new ArrayList();
                    head.add(messages.getString("t01_object.vertical_extent_maximum"));
                    head.add(messages.getString("t01_object.vertical_extent_minimum"));
                    head.add(messages.getString("t01_object.vertical_extent_unit"));
                    head.add(messages.getString("t01_object.vertical_extent_vdatum"));
                    element.put("head", head);
                    ArrayList body = new ArrayList();
                    element.put("body", body);
                    if (xPathUtils.nodeExists(node, "./gmd:verticalElement/gmd:EX_VerticalExtent")) {
                        NodeList subNodeList = xPathUtils.getNodeList(node, "./gmd:verticalElement/gmd:EX_VerticalExtent");
                        for (int j = 0; j < subNodeList.getLength(); j++) {
                            Node childNode = subNodeList.item(j);
                            ArrayList row = new ArrayList();
                            
                            // "Maximum"
                            xpathExpression = "./gmd:maximumValue";
                            if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                row.add(notNull(xPathUtils.getString(childNode, xpathExpression)).trim());
                            }
                            
                            // "Minimum"
                            xpathExpression = "./gmd:minimumValue";
                            if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                row.add(notNull(xPathUtils.getString(childNode, xpathExpression)).trim());
                            }
                            
                            String rowValue;
                            // "Maßeinheit"
                            xpathExpression = "./gmd:verticalCRS/gml:VerticalCRS/gml:verticalCS/gml:VerticalCS/gml:axis/gml:CoordinateSystemAxis/@uom";
                            if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                rowValue = xPathUtils.getString(childNode, xpathExpression).trim();
                                row.add(notNull(sysCodeList.getNameByCodeListValue("102", rowValue)));
                            }else{
                                row.add("");
                            }
                            
                            // "Vertikaldatum"
                            String verticalDatum = "" ; 
                            xpathExpression = "./gmd:verticalCRS/gml:VerticalCRS/gml:verticalDatum/gml:VerticalDatum/gml:name";
                            if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                rowValue = xPathUtils.getString(childNode, xpathExpression).trim();
                                if(sysCodeList.getNameByCodeListValue("101", rowValue).length() > 0){
                                    verticalDatum = sysCodeList.getNameByCodeListValue("101", rowValue);
                                }else{
                                    verticalDatum = rowValue;
                                }
                            }
                            
                            if(StringUtils.isEmpty(verticalDatum)) {
                                xpathExpression = "./gmd:verticalCRS/gml:VerticalCRS/gml:verticalDatum/gml:VerticalDatum/gml:identifier";
                                if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                    rowValue = xPathUtils.getString(childNode, xpathExpression).trim();
                                    if(sysCodeList.getNameByCodeListValue("101", rowValue).length() > 0){
                                        verticalDatum = sysCodeList.getNameByCodeListValue("101", rowValue);
                                    }else{
                                        verticalDatum = rowValue;
                                    }
                                }
                            }
                            if(StringUtils.isEmpty(verticalDatum)){
                                xpathExpression = "./gmd:verticalCRS/gml:VerticalCRS/gml:name";
                                if(xPathUtils.nodeExists(childNode, xpathExpression)){
                                    rowValue = xPathUtils.getString(childNode, xpathExpression).trim();
                                    if(sysCodeList.getNameByCodeListValue("101", rowValue).length() > 0){
                                        verticalDatum = sysCodeList.getNameByCodeListValue("101", rowValue);
                                    }else{
                                        verticalDatum = rowValue;
                                    }
                                }
                            }
                            
                            row.add(verticalDatum);
                            if (!isEmptyRow(row)) {
                                body.add(row);
                            }
                        }
                    }
                }
            }
        }
        return element;
    }

    public List getPolygon(String xpathExpression) {
        List<String> result = new ArrayList<>();
        if(xPathUtils.nodeExists(rootNode, xpathExpression)){
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node != null) {
                    try {
                        result.add(GmlToWktTransformUtil.gml3_2ToWktString(node));
                    } catch (Exception e) {
                        log.error("Error transform GML to string: ", e);
                    }
                }
            }
        }
        return result;
    }

    public List getNodeListValueReferenceSystem(String xpathExpression) {
        ArrayList<Map> list = new ArrayList<>();
        if(xPathUtils.nodeExists(rootNode, xpathExpression)){
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i<nodeList.getLength(); i++){
                String codeSpace = ""; 
                String code = "";
                xpathExpression = "./gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code";
                if (xPathUtils.nodeExists(nodeList.item(i), xpathExpression)) {
                    code = xPathUtils.getString(nodeList.item(i), xpathExpression).trim();
                }
                
                xpathExpression = "./gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:codeSpace";
                if (xPathUtils.nodeExists(nodeList.item(i), xpathExpression)) {
                    codeSpace = xPathUtils.getString(nodeList.item(i), xpathExpression).trim();
                }
                
                String value = "";
                if(!code.isEmpty() && !codeSpace.isEmpty()){
                    if(code.contains("EPSG")){
                        value = code;
                    }else{
                        value = codeSpace.concat(":" + code);
                    }
                }else if(!codeSpace.isEmpty()){
                    value = codeSpace;
                }else if(!code.isEmpty()){
                    value = code;
                }
                if (!value.isEmpty()) {
                    String[] startsWithReplaces = PortalConfig.getInstance().getStringArray(PortalConfig.PORTAL_DETAIL_REFERENCE_SYSTEM_LINK_REPLACE);
                    for (String startsWithReplace : startsWithReplaces) {
                        if(value.startsWith(startsWithReplace)) {
                            value = value.replace(startsWithReplace, "");
                        }
                    }
                    Map link = new HashMap();
                    link.put("title", value);
                    link.put("hasLinkIcon", true);

                    Pattern p = Pattern.compile("EPSG( |:)[0-9]*");  // insert your pattern here
                    Matcher m = p.matcher(value);
                    if (m.find()) {
                       value = value.substring(m.start(), m.end());
                    }

                    if (value.startsWith("EPSG")) {
                        int endIndex = value.indexOf(':', 5);
                        String epsgCode = value.substring(5, endIndex == -1 ? value.length() : endIndex);

                        link.put("isExtern", true);
                        link.put("href", PortalConfig.getInstance().getString(PortalConfig.PORTAL_DETAIL_REFERENCE_SYSTEM_LINK, "https://epsg.io/") + epsgCode);
                    } else {
                        link.put("noLink", true);
                    }
                    list.add(link);
                }
            }
        }
        return list;
    }

    public String getGeoReport(String xpathExpression, String checkDescription, String checkNameOfMeasure) {
        StringBuilder value = new StringBuilder("");
        if(xPathUtils.nodeExists(rootNode, xpathExpression)){
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                String symbol = null;
                String nameOfMeasure = null;
                String description = null;
                String content = "";
                
                if(node != null){
                    xpathExpression = "./gmd:nameOfMeasure";
                    if(xPathUtils.nodeExists(node, xpathExpression)){
                        nameOfMeasure = xPathUtils.getString(node, xpathExpression).trim();
                    }
                    xpathExpression = "./gmd:measureDescription";
                    if(xPathUtils.nodeExists(node, xpathExpression)){
                        description = xPathUtils.getString(node, xpathExpression).trim();
                    }
                    xpathExpression = "./gmd:result/gmd:DQ_QuantitativeResult/gmd:value";
                    if(xPathUtils.nodeExists(node, xpathExpression)){
                        content = xPathUtils.getString(node, xpathExpression).trim();
                    }
                    xpathExpression = "./gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:UnitDefinition/gml:catalogSymbol";
                    if(xPathUtils.nodeExists(node, xpathExpression)){
                        symbol = xPathUtils.getString(node, xpathExpression).trim();
                    }
                    
                    if ((description != null || nameOfMeasure != null) && (checkDescription.equals(description) || checkNameOfMeasure.equalsIgnoreCase(nameOfMeasure))){
                        value.append(content);
                        if(symbol != null){
                            value.append(" " + symbol);
                        }
                        break;
                    }
                }
            }
        }
        return value.toString();
    }
    
    public String addLinkToGetXML() {
        String htmlLink = null;
        String cswUrl = PortalConfig.getInstance().getString(PortalConfig.CSW_INTERFACE_URL, "");
        if (!cswUrl.isEmpty()) {
            htmlLink = "<a target=\"_blank\" class=\"external-link\" href=\""+cswUrl+"?REQUEST=GetRecordById&SERVICE=CSW&VERSION=2.0.2&id="+this.uuid+"&iplug="+this.iPlugId+"&elementSetName=full\">"+messages.getString("xml_link")+"</a>";
        }
        return htmlLink;
    }
    
    public HashMap addLinkElementToGetXML() {
        HashMap elementLink = null;
        String cswUrl = PortalConfig.getInstance().getString(PortalConfig.CSW_INTERFACE_URL, "");
        if (!cswUrl.isEmpty()) {
            elementLink = new HashMap();
            elementLink.put("type", "linkLine");
            elementLink.put("hasLinkIcon", true);
            elementLink.put("isDownload", true);
            elementLink.put("title", messages.getString("xml_link"));
            elementLink.put("href", cswUrl + "?REQUEST=GetRecordById&SERVICE=CSW&VERSION=2.0.2&id=" + this.uuid + "&iplug=" + this.iPlugId + "&elementSetName=full");
        }
        return elementLink;
    }

    public String getPublishId(String value) {
        if (value == null) {
            return null;
        }
        String publishId = "1";
        if (value.length() > 0) {
            if (value.equals("unclassified")) {
                publishId = "1";
            } else if (value.equals("restricted")) {
                publishId = "2";
            } else if (value.equals("confidential")) {
                publishId = "3";
            }
        }
        return messages.getString("t01_object.publish_id_" + publishId);
    }

    public Map<String, String> getDOI() {
        String doiId = getValueFromXPath("./idf:doi/id");
        String doiType = getValueFromXPath("./idf:doi/type");

        Map<String, String> element =null;
        if(doiId != null || doiType != null) {
            element = new HashMap<>();
            if(doiId != null) {
                element.put("id", doiId);
            }
            if(doiType != null) {
                element.put("type", doiType);
            }
        }

        return element;
    }

    /*
     * Private functiions
     *
     */
    private HashMap<String,Object> getCapabilityUrls(String xpathExpression) {
        boolean mapLinkAdded = false;
        HashMap<String, Object> map = new HashMap<>();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i<nodeList.getLength();i++){
                if(xPathUtils.nodeExists(nodeList.item(i), "./gmd:MD_DigitalTransferOptions/gmd:onLine/*/gmd:linkage/gmd:URL")){
                    Node node = xPathUtils.getNode(nodeList.item(i), "./gmd:MD_DigitalTransferOptions/gmd:onLine/*/gmd:linkage/gmd:URL");
                    String urlValue = xPathUtils.getString(node, ".").trim();
                    // do not display empty URLs
                    if (urlValue == null || urlValue.length() == 0) {
                        continue;
                    }
                    String serviceType = xPathUtils.getString(nodeList.item(i), "./gmd:MD_DigitalTransferOptions/gmd:onLine/*/gmd:function/gmd:CI_OnLineFunctionCode");
                    if (urlValue.toLowerCase().contains("request=getcapabilities") && urlValue.toLowerCase().contains("service=wms") &&
                            (serviceType != null && (serviceType.trim().equalsIgnoreCase("view") || serviceType.trim().equalsIgnoreCase("wms")))) {
                        // also add an identifier to select the correct layer in the map client 
                        map = addBigMapLink(node, urlValue + "||" + getLayerIdentifier(null), true);
                        // ADD FIRST ONE FOUND !!!
                        mapLinkAdded = true;
                        break;
                    }
                }
            }
        }
        if (!mapLinkAdded) {
            // check if preview image is available
            xpathExpression = "./gmd:identificationInfo/*/gmd:graphicOverview/gmd:MD_BrowseGraphic/gmd:fileName/gco:CharacterString";
            map = getPreviewImage(xpathExpression);
        }
        return map;
    }
    
    private HashMap<String,Object> getPreviewImage(String xpathExpression) {
        ArrayList<HashMap<String, String>> urls = getPreviewImageUrl(xpathExpression);
        HashMap<String,Object> elementCapabilities = new HashMap<>();
        if(!urls.isEmpty()) {
            elementCapabilities.put("type", "multiLineImage");
            ArrayList<HashMap<String,Object>> list = new ArrayList<>();
            for (HashMap<String, String> url : urls) {
                // put link in a list so that it is aligned correctly in detail view (<div class="width_two_thirds">)
                HashMap<String,Object> elementMapLink = new HashMap<>();
                elementMapLink.put("type", "linkLine");
                elementMapLink.put("isMapLink", true);
                elementMapLink.put("isExtern", false);
                elementMapLink.put("title", messages.getString("preview"));
                elementMapLink.put("description", url.get("description"));
                elementMapLink.put("href", url.get("name"));
                elementMapLink.put("src", url.get("name"));
                list.add(elementMapLink);
            }
            elementCapabilities.put("elements", list);
            elementCapabilities.put("width", "full");
        }
        return elementCapabilities;
    }
    
    private String getLayerIdentifier(Node crossReference) {
        if (getUdkObjectClassType().equals("1")) {
            String href = xPathUtils.getString(rootNode, "./gmd:identificationInfo/gmd:MD_DataIdentification/@uuid");
            if (href != null) {
                return href;
            }
        } else {
            String origId = xPathUtils.getString(crossReference, "./@orig-uuid");
            String uuid   = xPathUtils.getString(crossReference, "./@uuid");
            String xpathExpression = "./gmd:identificationInfo/*/srv:operatesOn";
            
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i=0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
            
                String uuidRef = xPathUtils.getString(node, "./@uuidref");
                String href = xPathUtils.getString(node, "./@xlink:href");
                
                if (uuidRef != null && (uuidRef.equals(uuid) || uuidRef.equals(origId))) {
                    return href;
                }
            }
        }
        
        return "NOT_FOUND";
    }

    private HashMap<String, Object> addBigMapLink(String urlValue, boolean urlEncodeHref) {
        return addBigMapLink(rootNode, urlValue, urlEncodeHref);
    }

    private HashMap<String, Object> addBigMapLink(Node node, String urlValue, boolean urlEncodeHref) {
        return addBigMapLink(node, urlValue, urlEncodeHref, null, false);
    }

    private HashMap<String, Object> addBigMapLink(Node node, String urlValue, boolean urlEncodeHref, String partner, boolean isNewLayout) {
        HashMap<String, Object> elementCapabilities = new HashMap<>();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        // use preview image if provided otherwise static image
        ArrayList<HashMap<String, String>> imageUrls = getPreviewImageUrl(null);
        if (imageUrls.isEmpty()) {
            HashMap<String, Object> elementMapLink = new HashMap<>();
            elementMapLink.put("type", "linkLine");
            elementMapLink.put("isMapLink", true);
            elementMapLink.put("isExtern", false);

            if (hasAccessConstraints(node) || !PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false) || urlValue == null) {
                // do not render "show in map" link if the map has access constraints (no href added).
                if(!isNewLayout) {
                    elementMapLink.put("title", messages.getString("preview"));
                }
            } else {
                String href = urlValue;
                if(urlEncodeHref){
                    href = UtilsVelocity.urlencode(urlValue);
                }
                if(!isNewLayout) {
                    elementMapLink.put("title", messages.getString("common.result.showMap.tooltip.short"));
                    elementMapLink.put("href", href);
                } else {
                   elementCapabilities.put("href", href);
                }
            }

            if(elementCapabilities.get("href") != null || elementMapLink.get("href") != null) {
                String mapImage = "image_map";
                if(partner != null) {
                    mapImage += "_" + partner;
                }
                mapImage += ".png";
                elementMapLink.put("src", "/ingrid-portal-apps/images/maps/" + mapImage);
                list.add(elementMapLink);
            }
        } else {
            for (HashMap<String, String> imageUrl : imageUrls) {
                HashMap<String, Object> elementMapLink = new HashMap<>();
                elementMapLink.put("type", "linkLine");
                elementMapLink.put("isMapLink", true);
                elementMapLink.put("isExtern", false);

                if (hasAccessConstraints(node) || !PortalConfig.getInstance().getBoolean(PortalConfig.PORTAL_ENABLE_MAPS, false) || urlValue == null) {
                    // do not render "show in map" link if the map has access constraints (no href added).
                    if(!isNewLayout) {
                        elementMapLink.put("title", messages.getString("preview"));
                    }
                } else {
                    String href = urlValue;
                    if(urlEncodeHref){
                        href = UtilsVelocity.urlencode(urlValue);
                    }
                    if(!isNewLayout) {
                        elementMapLink.put("title", messages.getString("common.result.showMap.tooltip.short"));
                        elementMapLink.put("href", href);
                    } else {
                       elementCapabilities.put("href", href);
                    }
                }
                elementMapLink.put("src", imageUrl.get("name"));
                if(elementMapLink.get("href") == null) {
                    elementMapLink.put("href", imageUrl.get("name"));
                } 
                elementMapLink.put("description", imageUrl.get("description"));
                list.add(elementMapLink);
            }
        }
        
        elementCapabilities.put("type", "multiLineImage");
        elementCapabilities.put("elements", list);
        elementCapabilities.put("width", "full");
        return elementCapabilities;
    }
    
    private ArrayList<HashMap<String, String>> getPreviewImageUrl(String xpathExpression) {
        if (xpathExpression == null)
            xpathExpression = "./gmd:identificationInfo/*/gmd:graphicOverview/gmd:MD_BrowseGraphic/gmd:fileName/gco:CharacterString";
        
        ArrayList<HashMap<String, String>> values = new ArrayList<>();
        if (xPathUtils.nodeExists(rootNode, xpathExpression)) {
            NodeList nodeList = xPathUtils.getNodeList(rootNode, xpathExpression);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                HashMap<String, String> map = new HashMap<>(); 
                map.put("name", node.getTextContent());
                xpathExpression = "../../gmd:fileDescription/gco:CharacterString";
                if (xPathUtils.nodeExists(node, xpathExpression)) {
                    Node tmpNode = xPathUtils.getNode(node, xpathExpression);
                    map.put("description", tmpNode.getTextContent());
                }
                values.add(map);
            }
        }
        return values;
    }
    
    private String getCapabilityUrl() {
        String url = null;
        String serviceType = null;
        String xpathExpression = "";
        
        xpathExpression = "./gmd:identificationInfo/*/srv:serviceType";
        if(xPathUtils.nodeExists(this.rootNode, xpathExpression)){
            serviceType = xPathUtils.getString(this.rootNode, xpathExpression);
        }
        if (serviceType != null && (serviceType.trim().equalsIgnoreCase("view") || serviceType.trim().equalsIgnoreCase("wms"))) {
            Node capNode = xPathUtils.getNode( this.rootNode, "./gmd:identificationInfo/*/srv:containsOperations/srv:SV_OperationMetadata/srv:operationName/gco:CharacterString[text() = 'GetCapabilities']/../../srv:connectPoint//gmd:URL");
            if(capNode != null && capNode.getTextContent() != null){
                url = capNode.getTextContent().trim();
            }
        }
        return url;
    }

    public boolean hasAccessConstraints() {
        return hasAccessConstraints(rootNode);
    }

    public boolean hasAccessConstraints(Node node) {
        boolean hasAccessConstraints = false;
        String xpathExpression = "./idf:hasAccessConstraint";
        if (xPathUtils.nodeExists(node, xpathExpression)) {
            String hasAccessConstraintsValue = xPathUtils.getString(node, xpathExpression).trim();
            if(hasAccessConstraintsValue.length() > 0){
                hasAccessConstraints = Boolean.parseBoolean(hasAccessConstraintsValue); 
            }
        }
        return hasAccessConstraints;
    }
}
