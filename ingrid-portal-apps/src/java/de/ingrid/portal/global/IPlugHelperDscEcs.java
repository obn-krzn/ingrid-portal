/*
 * Copyright (c) 1997-2007 by wemove GmbH
 */
package de.ingrid.portal.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.ingrid.portal.config.PortalConfig;
import de.ingrid.portal.interfaces.impl.IBUSInterfaceImpl;
import de.ingrid.portal.search.UtilsSearch;
import de.ingrid.utils.IngridHitDetail;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.PlugDescription;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

/**
 * Helper class dealing with all aspects of DSC_ECS iPlugs.
 *
 * @author joachim@wemove.com
 */
public class IPlugHelperDscEcs extends IPlugHelper {

    static private final Log log = LogFactory.getLog(IPlugHelperDscEcs.class);

    /**
     * Get top ECS Objects as a List of IngridHits (containing metadata ID and UDK_CLASS)
     *  
     * @param objId parent object
     * @param iPlugId plug id
     * @return List of IngridHit
     */
    static public ArrayList getTopObjects(String iPlugId) {
        String[] requestedMetadata = new String[2];
        requestedMetadata[0] = Settings.HIT_KEY_OBJ_ID;
        requestedMetadata[1] = Settings.HIT_KEY_UDK_CLASS;
        ArrayList result = getHits("t01_object.root:1".concat(
                " iplugs:\"".concat(getPlugIdFromAddressPlugId(iPlugId)).concat("\"")), requestedMetadata, null);
        return result;
    }

    /**
     * Get Restricted Number of subordinated ECS Objects as a List of IngridHits (containing metadata ID and UDK_CLASS).
     * Or pass null to request ALL objects
     *  
     * @param objId parent object
     * @param iPlugId plug id
     * @param maxNumber how many objects requested ? pass null if all objects !
     * @return List of IngridHit
     */
    static private ArrayList getSubordinatedObjects(String objId, String iPlugId, Integer maxNumber) {
        String[] requestedMetadata = new String[2];
        requestedMetadata[0] = Settings.HIT_KEY_OBJ_ID;
        requestedMetadata[1] = Settings.HIT_KEY_UDK_CLASS;
        HashMap filter = new HashMap();
        filter.put(Settings.HIT_KEY_OBJ_ID, objId);
        ArrayList result = getHits("t012_obj_obj.object_from_id:".concat(objId).concat(
        	" t012_obj_obj.typ:0 iplugs:\"".concat(getPlugIdFromAddressPlugId(iPlugId)).concat("\"")),
        	requestedMetadata, filter, maxNumber);
        return result;
    }
    
    /**
     * Get ALL subordinated ECS Objects as a List of IngridHits (containing metadata ID and UDK_CLASS)
     *  
     * @param objId parent object
     * @param iPlugId plug id
     * @return List of IngridHit
     */
    static public ArrayList getSubordinatedObjects(String objId, String iPlugId) {
    	return getSubordinatedObjects(objId, iPlugId, null);
    }

    /**
     * Get top ECS Addresses as a List of IngridHits (containing metadata ID and UDK_CLASS)
     *  
     * @param objId parent object
     * @param iPlugId plug id
     * @return List of IngridHit
     */
    static public ArrayList getTopAddresses(String iPlugId) {
        String[] requestedMetadata = new String[7];
        requestedMetadata[0] = Settings.HIT_KEY_WMS_URL;
        requestedMetadata[1] = Settings.HIT_KEY_ADDRESS_CLASS;
        requestedMetadata[2] = Settings.HIT_KEY_ADDRESS_FIRSTNAME;
        requestedMetadata[3] = Settings.HIT_KEY_ADDRESS_LASTNAME;
        requestedMetadata[4] = Settings.HIT_KEY_ADDRESS_TITLE;
        requestedMetadata[5] = Settings.HIT_KEY_ADDRESS_ADDRESS;
        requestedMetadata[6] = Settings.HIT_KEY_ADDRESS_ADDRID;
        ArrayList result = getHits("T02_address.root:1".concat(
        	" iplugs:\"".concat(getAddressPlugIdFromPlugId(iPlugId)).concat("\"")),
            requestedMetadata, null);
        return result;
    }

    /**
     * Get a restricted Number of Sub Addresses or all Sub Addresses
     * @param maxNumber how many addresses requested ? pass null if all addresses !
     */
    static private ArrayList getAddressChildren(String addrId, String iPlugId, Integer maxNumber) {
        String[] requestedMetadata = new String[7];
        requestedMetadata[0] = Settings.HIT_KEY_WMS_URL;
        requestedMetadata[1] = Settings.HIT_KEY_ADDRESS_CLASS;
        requestedMetadata[2] = Settings.HIT_KEY_ADDRESS_FIRSTNAME;
        requestedMetadata[3] = Settings.HIT_KEY_ADDRESS_LASTNAME;
        requestedMetadata[4] = Settings.HIT_KEY_ADDRESS_TITLE;
        requestedMetadata[5] = Settings.HIT_KEY_ADDRESS_ADDRESS;
        requestedMetadata[6] = Settings.HIT_KEY_ADDRESS_ADDRID;
        HashMap filter = new HashMap();
        filter.put(Settings.HIT_KEY_ADDRESS_ADDRID, addrId);
        ArrayList result = getHits(
                "T022_adr_adr.adr_from_id:".concat(addrId).concat(" iplugs:\"".concat(getAddressPlugIdFromPlugId(iPlugId)).concat("\"")),
                requestedMetadata, filter, maxNumber);
        return result;    	
    }

    /**
     * Get ALL Sub Addresses !
     */
    static public ArrayList getAddressChildren(String addrId, String iPlugId) {
    	return getAddressChildren(addrId, iPlugId, null);
    }

    static public ArrayList getTopDocs(String plugId, String plugType) {
        ArrayList hits = new ArrayList();
    	if (plugType.equals(Settings.QVALUE_DATATYPE_IPLUG_DSC_ECS)) {
    		hits = IPlugHelperDscEcs.getTopObjects(plugId);
    	} else if (plugType.equals(Settings.QVALUE_DATATYPE_IPLUG_DSC_ECS_ADDRESS)) {
    		hits = IPlugHelperDscEcs.getTopAddresses(plugId);
    	}

        return hits;
    }

    /**
     * Get a restricted Number of Sub UDK Documents or all Sub Documents
     * NOTICE: DUE TO BUG IN BACKEND REQUEST MAXNUMBER = 1 DOESN'T WORK !!!! IS SET TO 2 INTERNALLY !!!
     * @param maxNumber how many docs requested ? HAS TO BE > 1 due to bug in backend !!!!
     * IF 1 IS PASSED IT IS SET TO 2 !!! pass null if all documents !
     */
    static public ArrayList getSubDocs(String docParentId, String plugId, String plugType, Integer maxNumber) {
        ArrayList hits = new ArrayList();
        
        // TODO: REMOVE FIX TO BUG IN BACKEND WHEN BACKEND IS FIXED !!!
        if (maxNumber != null && maxNumber.intValue() == 1) {
        	maxNumber = new Integer(2);
        }

    	if (plugType.equals(Settings.QVALUE_DATATYPE_IPLUG_DSC_ECS)) {
   			hits = IPlugHelperDscEcs.getSubordinatedObjects(docParentId, plugId, maxNumber);

    	} else if (plugType.equals(Settings.QVALUE_DATATYPE_IPLUG_DSC_ECS_ADDRESS)) {
    			hits = IPlugHelperDscEcs.getAddressChildren(docParentId, plugId, maxNumber);
    	}

        return hits;
    }

    /**
     * Returns the address plug id of the corresponding plug id, by adding the postfix
     * "_addr" to the plug id. Only plug id's that contain the string "udk-db" and do
     * not contain the postfix "_addr" will be changed.
     * 
     * @param plugId
     * @return
     */
    static public String getAddressPlugIdFromPlugId(String plugId) {
        if (plugId != null && plugId.indexOf("udk-db") > -1 && !plugId.endsWith("_addr")) {
            return plugId.concat("_addr");
        } else {
            return plugId;
        }
    }

    /**
     * Returns the plug id of the corresponding address plug id. Address plug
     * id's are detected from the postfix "_addr". The postfix will be stripped.
     * Plug id's without this postfix remain unchanged.
     * 
     * @param plugId
     * @return
     */
    static public String getPlugIdFromAddressPlugId(String plugId) {
        if (plugId == null) {
            return "";
        } else  if (plugId.endsWith("_addr")) {
            return plugId.substring(0, plugId.lastIndexOf("_addr"));
        } else {
            return plugId;
        }
    }

    /**
     * Get a restricted Number of Hits or all hits.
     * @param maxNumberOfHits how many hits requested ? pass null if all hits !
     * @return
     */
    static private ArrayList getHits(String queryStr, String[] requestedMetaData, HashMap filter, Integer maxNumberOfHits) {
        ArrayList result = new ArrayList();
        
        // request hits in chunks of 20 results per page, when all hits requested !
        int chunkSize = 20;
        boolean getAllHits = true;
        if (maxNumberOfHits != null) {
            // restricted number of hits, requested all at once !
            getAllHits = false;
            chunkSize = maxNumberOfHits.intValue();
        }

        try {
            IngridQuery query = QueryStringParser.parse(queryStr.concat(" ranking:any datatype:any"));
            IngridHits hits;
            // request hits in chunks or all at once, when restricted number of hits !
            int page = 0;
            do {
                page++;
                hits = IBUSInterfaceImpl.getInstance().search(query, chunkSize, page, (page-1) * chunkSize,
                        PortalConfig.getInstance().getInt(PortalConfig.QUERY_TIMEOUT_RANKED, 3000));
                IngridHitDetail details[] = IBUSInterfaceImpl.getInstance().getDetails(hits.getHits(), query,
                        requestedMetaData);
                for (int j = 0; j < details.length; j++) {
                    IngridHitDetail detail = (IngridHitDetail) details[j];
                    boolean include = true;
                    if (filter != null && filter.size() > 0) {
                        Iterator it = filter.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            String recordKey = (String) entry.getKey();
                            String value = (String) entry.getValue();
                            if (value.equals(UtilsSearch.getDetailValue(detail, recordKey))) {
                                include = false;
                                break;
                            }
                        }
                    }
                    if (include) {
                        // flatten alle detail fields
                        for (int i = 0; i < requestedMetaData.length; i++) {
                            detail.put(requestedMetaData[i], UtilsSearch.getDetailValue(detail, requestedMetaData[i]));
                        }
                        hits.getHits()[j].put("detail", detail);
                        result.add(hits.getHits()[j]);
                    }
                }
            } while (hits.getHits().length == chunkSize && getAllHits);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("Problems getting hits from iBus!", e);
            } else {
                log.info("Problems getting hits from iBus!");
            }
        }
        return result;
    }

    /**
     * Get ALL hits
     */
    static public ArrayList getHits(String queryStr, String[] requestedMetaData, HashMap filter) {
    	return getHits(queryStr, requestedMetaData, filter, null);
    }


    /**
     * Inner class: PlugComparator for ECS plugs sorting -> sorted by "Partner"/"Name"/"Type" (Object before Address ECS)
     */
    static public class PlugComparatorECS implements Comparator {
        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public final int compare(Object a, Object b) {
            try {
            	PlugDescription[] plugs = new PlugDescription[] {
            		(PlugDescription) a,
            		(PlugDescription) b
            	}; 
            	String[] plugSortCriteria = new String[plugs.length];
            	
            	for (int i = 0; i < plugs.length; i++) {
                    String[] partners = plugs[i].getPartners();
                    String name = plugs[i].getDataSourceName().toLowerCase();
                    // object ECS before Address ECS !
                    String type = hasDataType(plugs[i], Settings.QVALUE_DATATYPE_IPLUG_DSC_ECS) ? "0" : "1";

                    StringBuffer sortString = new StringBuffer("");
                    for (int j = 0; j < partners.length; j++) {
                    	sortString.append(UtilsDB.getPartnerFromKey(partners[j]));
                    }
                    sortString.append(name);
                    sortString.append(type);
                    
                    plugSortCriteria[i] = sortString.toString();
            	}

                return plugSortCriteria[0].compareTo(plugSortCriteria[1]);
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
