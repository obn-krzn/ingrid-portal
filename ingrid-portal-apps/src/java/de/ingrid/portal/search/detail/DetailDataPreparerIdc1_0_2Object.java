/**
 * 
 */
package de.ingrid.portal.search.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.Context;

import de.ingrid.portal.global.IPlugHelperDscEcs;
import de.ingrid.portal.global.IngridResourceBundle;
import de.ingrid.portal.global.IngridSysCodeList;
import de.ingrid.portal.global.Settings;
import de.ingrid.portal.global.UtilsVelocity;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.dsc.Column;
import de.ingrid.utils.dsc.Record;
import de.ingrid.utils.udk.UtilsDate;
import de.ingrid.utils.udk.UtilsUDKCodeLists;

/**
 * @author joachim
 *
 */
public class DetailDataPreparerIdc1_0_2Object implements DetailDataPreparer {

    private final static Log log = LogFactory.getLog(DetailDataPreparerIdc1_0_2Object.class);
	
	private Context context;
	private String iPlugId;
	private RenderRequest request;
	private RenderResponse response;
	private IngridResourceBundle messages;
	private IngridSysCodeList sysCodeList;
	
	public DetailDataPreparerIdc1_0_2Object(Context context, String iPlugId, RenderRequest request, RenderResponse response) {
		this.context = context;
		this.iPlugId = iPlugId;
		this.request = request;
		this.response = response;
		messages = (IngridResourceBundle)context.get("MESSAGES");
		sysCodeList = new IngridSysCodeList(request.getLocale());
	}
	
	/* (non-Javadoc)
	 * @see de.ingrid.portal.search.detail.DetailDataPreparer#prepare(de.ingrid.utils.dsc.Record)
	 */
	public void prepare(Record record) {
		HashMap data = new HashMap();
		HashMap general = new HashMap();
		general.put("title", record.getString("t01_object.obj_name"));
		general.put("modTime", UtilsDate.convertDateString(record.getString("t01_object.mod_time").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
		String objClassStr = record.getString("t01_object.obj_class");
		if (objClassStr != null) {
			general.put("udkObjClass", objClassStr);
			general.put("udkObjClassName", messages.getString("udk_obj_class_name_".concat(objClassStr)));
		}

		data.put("general", general);
		
		ArrayList elements = new ArrayList();

		// alternate name
		addElementEntry(elements, record.getString("t01_object.dataset_alternate_name"), null);
		// udk class
		addElementUdkClass(elements, record.getString("t01_object.obj_class"));
		// description
		addElementEntry(elements, record.getString("t01_object.obj_descr"), null);
		
		// add horizontal line
		addLine(elements);
		
		// superior objects
		addSuperiorObjects(elements, record.getString("t01_object.obj_uuid"));

		// subordinated objects
		addSubordinatedObjects(elements, record.getString("t01_object.obj_uuid"));

		// cross referenced objects
		addCrossReferencedObjects(elements, record.getString("t01_object.id"));
		
		//	URL references
		addUrlReferences(elements, record);

		// add horizontal line
		addLine(elements);

		//	addresses
		addAddresses(elements, record);

		// add horizontal line
		addLine(elements);
		
		// add subject data
		if (objClassStr.equals("5")) {
			addReferenceObjectClass5(elements, record);
		} else if (objClassStr.equals("3")) {
			addReferenceObjectClass3(elements, record);
		} else if (objClassStr.equals("2")) {
			addReferenceObjectClass2(elements, record);
		} else if (objClassStr.equals("4")) {
			addReferenceObjectClass4(elements, record);
		} else if (objClassStr.equals("1")) {
			addReferenceObjectClass1(elements, record);
		}
		
		// add horizontal line
		addLine(elements);

		// add spatial reference data
		addSpatialReference(elements, record);

		// add horizontal line
		addLine(elements);
		
		// add time reference data
		addTimeReference(elements, record);
		
		data.put("elements", elements);
		
		context.put("data", data);
	}

	
	private void addTimeReference(List elements, Record record) {
    	// time reference
		List refRecords = getSubRecordsByColumnName(record, "t01_object.id");
    	if (refRecords.size() > 0) {
	    	for (int i=0; i<refRecords.size(); i++) {
	    		Record refRecord = (Record)refRecords.get(i);
       	    	String timeType = refRecord.getString("t01_object.time_type");
	    		if(UtilsVelocity.hasContent(timeType).booleanValue()) {
	       	    	String entryLine = "";
	       	    	if (timeType.equals("von")) {
	       	    		entryLine = entryLine.concat(messages.getString("search.detail.time.from")).concat(": ");
	       	    		entryLine = entryLine.concat(UtilsDate.convertDateString(refRecord.getString("t01_object.time_from").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	       	    		entryLine = entryLine.concat(" ").concat(messages.getString("search.detail.time.to")).concat(": ");
	       	    		entryLine = entryLine.concat(UtilsDate.convertDateString(refRecord.getString("t01_object.time_to").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	       	    	} else if (timeType.equals("seit")) {
	       	    		entryLine = entryLine.concat(messages.getString("search.detail.time.since")).concat(": ");
	       	    		entryLine = entryLine.concat(UtilsDate.convertDateString(refRecord.getString("t01_object.time_from").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	       	    	} else if (timeType.equals("am")) {
	       	    		entryLine = entryLine.concat(messages.getString("search.detail.time.at")).concat(": ");
	       	    		entryLine = entryLine.concat(UtilsDate.convertDateString(refRecord.getString("t01_object.time_from").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	       	    	} else if (timeType.equals("bis")) {
	       	    		entryLine = entryLine.concat(messages.getString("search.detail.time.until")).concat(": ");
	       	    		entryLine = entryLine.concat(UtilsDate.convertDateString(refRecord.getString("t01_object.time_to").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	       	    	}
	       	    	if (entryLine.length() > 0) {
	       	    		addElementEntry(elements, entryLine, messages.getString("time_reference_content"));
	       	    	}
	    		}
	    	}
    	}
    	
    	addElementEntry(elements, sysCodeList.getName("523", record.getString("t01_object.time_status")), messages.getString("t01_object.time_status"));
    	addElementEntry(elements, sysCodeList.getName("518", record.getString("t01_object.time_period")), messages.getString("t01_object.time_period"));
    	if (UtilsVelocity.hasContent(record.getString("t01_object.time_alle")).booleanValue() 
    			&& UtilsVelocity.hasContent(record.getString("t01_object.time_interval")).booleanValue()) {
        	String entryLine = record.getString("t01_object.time_alle").concat(" ").concat(record.getString("t01_object.time_interval"));
    		addElementEntry(elements, entryLine, messages.getString("t01_object.time_interval"));
    	}
    	
    	// time references
		List listRecords = getSubRecordsByColumnName(record, "t0113_dataset_reference.line");
    	if (listRecords.size() > 0) {
	    	ArrayList lines = new ArrayList();
	    	for (int i=0; i<listRecords.size(); i++) {
	    		Record listRecord = (Record)listRecords.get(i);
	    		HashMap line = new HashMap();
	        	line.put("type", "textLine");
	        	String textLine = sysCodeList.getName("502", listRecord.getString("t0113_dataset_reference.type"));
	        	textLine = textLine.concat(": ").concat(UtilsDate.convertDateString(listRecord.getString("t0113_dataset_reference.reference_date").trim(), "yyyyMMddHHmmssSSS", "dd.MM.yyyy"));
	        	line.put("body", textLine);
	        	lines.add(line);
	    	}
	    	if (lines.size() > 0) {
		    	HashMap element = new HashMap();
		    	element.put("type", "multiLine");
		    	element.put("title", messages.getString("time_reference_record"));
		    	element.put("elements", lines);
	    	    elements.add(element);
	    	}
    	}
    	addElementEntry(elements, record.getString("t01_object.time_descr"), messages.getString("t01_object.time_descr"));
	}

	
	private void addSpatialReference(List elements, Record record) {
    	// geo thesaurus references
		List refRecords = getSubRecordsByColumnName(record, "spatial_ref_value.name_value");
    	if (refRecords.size() > 0) {
	    	ArrayList lines = new ArrayList();
	    	for (int i=0; i<refRecords.size(); i++) {
	    		Record listRecord = (Record)refRecords.get(i);
	        	if (listRecord.getString("spatial_ref_value.type") != null && listRecord.getString("spatial_ref_value.type").equals("G")) {
		    		HashMap line = new HashMap();
		        	line.put("type", "textLine");
		        	String textLine = listRecord.getString("spatial_ref_value.name_value");
		        	if (textLine != null && listRecord.getString("spatial_ref_value.nativekey") != null) {
		        		textLine = textLine.concat(" (").concat(listRecord.getString("spatial_ref_value.nativekey")).concat(")");
		        	}
		        	line.put("body", textLine);
		        	lines.add(line);
	        	}
	    	}
	    	if (lines.size() > 0) {
		    	HashMap element = new HashMap();
		    	element.put("type", "multiLine");
		    	element.put("title", messages.getString("t011_township.township_no"));
		    	element.put("elements", lines);
	    	    elements.add(element);
	    	}
    	}

    	// geo thesaurus references
		refRecords = getSubRecordsByColumnName(record, "spatial_ref_value.name_value");
    	if (refRecords.size() > 0) {
	    	ArrayList lines = new ArrayList();
	    	for (int i=0; i<refRecords.size(); i++) {
	    		Record listRecord = (Record)refRecords.get(i);
	        	if (listRecord.getString("spatial_ref_value.type") != null && listRecord.getString("spatial_ref_value.type").equals("F") && listRecord.getString("spatial_ref_value.name_value") != null ) {
		    		HashMap line = new HashMap();
		        	line.put("type", "textLine");
		        	line.put("body", listRecord.getString("spatial_ref_value.name_value"));
		        	lines.add(line);
	        	}
	    	}
	    	if (lines.size() > 0) {
		    	HashMap element = new HashMap();
		    	element.put("type", "multiLine");
		    	element.put("title", messages.getString("t019_coordinates.bezug"));
		    	element.put("elements", lines);
	    	    elements.add(element);
	    	}
    	}
    	
    	// vertical extend
    	if (UtilsVelocity.hasContent(record.getString("t01_object.vertical_extent_minimum")).booleanValue()
    			|| UtilsVelocity.hasContent(record.getString("t01_object.vertical_extent_maximum")).booleanValue()
    			|| UtilsVelocity.hasContent(record.getString("t01_object.vertical_extent_unit")).booleanValue()
    			|| UtilsVelocity.hasContent(record.getString("t01_object.vertical_extent_vdatum")).booleanValue()
    		) {
	    	HashMap element = new HashMap();
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
			ArrayList row = new ArrayList();
			row.add(notNull(record.getString("t01_object.vertical_extent_maximum")));
			row.add(notNull(record.getString("t01_object.vertical_extent_minimum")));
			row.add(notNull(record.getString("t01_object.vertical_extent_unit")));
			row.add(notNull(record.getString("t01_object.vertical_extent_vdatum")));
			body.add(row);
	    	elements.add(element);
	    	element = new HashMap();
	    	element.put("type", "space");
			elements.add(element);
    	}
    	
    	this.addElementEntry(elements, record.getString("t01_object.loc_descr"), messages.getString("t01_object.loc_descr"));
	}

	
	private void addReferenceObjectClass1(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t011_obj_geo.special_base");
    	if (refRecords.size() > 0) {
    		Record refRecord = (Record)refRecords.get(0);
    		addElementEntry(elements, sysCodeList.getName("525", refRecord.getString("t011_obj_geo.hierarchy_level")), messages.getString("t011_obj_geo.hierarchy_level"));
    		
    		List tableRecords = getSubRecordsByColumnName(record, "t011_obj_geo_symc.line");
    		if (tableRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "table");
    	    	element.put("title", messages.getString("t011_obj_geo_symc"));
    			ArrayList head = new ArrayList();
    			head.add(messages.getString("t011_obj_geo_symc.symbol_cat"));
    			head.add(messages.getString("t011_obj_geo_symc.symbol_date"));
    			head.add(messages.getString("t011_obj_geo_symc.edition"));
    			element.put("head", head);
    			ArrayList body = new ArrayList();
    			element.put("body", body);
    	    	for (int i=0; i<tableRecords.size(); i++) {
    	    		Record tableRecord = (Record)tableRecords.get(i);
    	    		ArrayList row = new ArrayList();
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_symc.symbol_cat_value")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_symc.symbol_date")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_symc.edition")));
    	    		body.add(row);
    	    	}
    	    	elements.add(element);
    	    	element = new HashMap();
    	    	element.put("type", "space");
    			elements.add(element);
    		}    	

    		tableRecords = getSubRecordsByColumnName(record, "t011_obj_geo_keyc.line");
    		if (tableRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "table");
    	    	element.put("title", messages.getString("t011_obj_geo_keyc"));
    			ArrayList head = new ArrayList();
    			head.add(messages.getString("t011_obj_geo_keyc.subject_cat"));
    			head.add(messages.getString("t011_obj_geo_keyc.key_date"));
    			head.add(messages.getString("t011_obj_geo_keyc.edition"));
    			element.put("head", head);
    			ArrayList body = new ArrayList();
    			element.put("body", body);
    	    	for (int i=0; i<tableRecords.size(); i++) {
    	    		Record tableRecord = (Record)tableRecords.get(i);
    	    		ArrayList row = new ArrayList();
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_keyc.keyc_value")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_keyc.key_date")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_keyc.edition")));
    	    		body.add(row);
    	    	}
    	    	elements.add(element);
    	    	element = new HashMap();
    	    	element.put("type", "space");
    			elements.add(element);
    		}
    		
    		boolean showVectorData = false;
    		List listRecords = getSubRecordsByColumnName(record, "t011_obj_geo_spatial_rep.line");
    		if (listRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "multiLine");
    	    	element.put("title", messages.getString("t011_obj_geo_spatial_rep.type"));
    	    	ArrayList lines = new ArrayList();
    	    	element.put("elements", lines);
    	    	for (int i=0; i<listRecords.size(); i++) {
    	    		Record listRecord = (Record)listRecords.get(i);
    	        	HashMap line = new HashMap();
    	        	line.put("type", "textLine");
    	        	String repType = listRecord.getString("t011_obj_geo_spatial_rep.type");
    	        	line.put("body", sysCodeList.getName("526", repType));
    	        	lines.add(line);
    	        	if (repType != null && repType.equals("1")) {
    	        		showVectorData = true;
    	        	}
    	    	}
        	    elements.add(element);
    		}
    		
    		if (showVectorData) {
        		addElementEntryInline(elements, sysCodeList.getName("528", refRecord.getString("t011_obj_geo.vector_topology_level")), messages.getString("t011_obj_geo.vector_topology_level"));
    			
        		tableRecords = getSubRecordsByColumnName(record, "t011_obj_geo_vector.line");
        		if (tableRecords.size() > 0) {
        	    	HashMap element = new HashMap();
        	    	element.put("type", "table");
        	    	element.put("title", messages.getString("t011_obj_geo_vector"));
        			ArrayList head = new ArrayList();
        			head.add(messages.getString("t011_obj_geo_vector.geometric_object_type"));
        			head.add(messages.getString("t011_obj_geo_vector.geometric_object_count"));
        			element.put("head", head);
        			ArrayList body = new ArrayList();
        			element.put("body", body);
        	    	for (int i=0; i<tableRecords.size(); i++) {
        	    		Record tableRecord = (Record)tableRecords.get(i);
        	    		ArrayList row = new ArrayList();
        	    		row.add(notNull(tableRecord.getString("t011_obj_geo_vector.geometric_object_type")));
        	    		row.add(notNull(tableRecord.getString("t011_obj_geo_vector.geometric_object_count")));
        	    		body.add(row);
        	    	}
        	    	elements.add(element);
        	    	element = new HashMap();
        	    	element.put("type", "space");
        			elements.add(element);
        		}
    		}
    		if (refRecord.getString("t011_obj_geo.rec_grade") != null) {
    			addElementEntryInline(elements, refRecord.getString("t011_obj_geo.rec_grade").concat(" %"), messages.getString("t011_obj_geo.rec_grade"));
    		}
    		addElementEntryInline(elements, sysCodeList.getName("100", refRecord.getString("t011_obj_geo.referencesystem_value")), messages.getString("t011_obj_geo.referencesystem_id"));
    		
    		tableRecords = getSubRecordsByColumnName(record, "t011_obj_geo_scale.line");
    		if (tableRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "table");
    	    	element.put("title", messages.getString("t011_obj_geo_scale"));
    			ArrayList head = new ArrayList();
    			head.add(messages.getString("t011_obj_geo_scale.scale").concat(" 1:x"));
    			head.add(messages.getString("t011_obj_geo_scale.resolution_ground").concat(" m"));
    			head.add(messages.getString("t011_obj_geo_scale.resolution_scan").concat(" dpi"));
    			element.put("head", head);
    			ArrayList body = new ArrayList();
    			element.put("body", body);
    	    	for (int i=0; i<tableRecords.size(); i++) {
    	    		Record tableRecord = (Record)tableRecords.get(i);
    	    		ArrayList row = new ArrayList();
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_scale.scale")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_scale.resolution_ground")));
    	    		row.add(notNull(tableRecord.getString("t011_obj_geo_scale.resolution_scan")));
    	    		body.add(row);
    	    	}
    	    	elements.add(element);
    	    	element = new HashMap();
    	    	element.put("type", "space");
    			elements.add(element);
    		}    		
    		if (refRecord.getString("t011_obj_geo.pos_accuracy_vertical") != null) {
    			addElementEntry(elements, refRecord.getString("t011_obj_geo.pos_accuracy_vertical").concat(" m"), messages.getString("t011_obj_geo.pos_accuracy_vertical"));
    		}
    		if (refRecord.getString("t011_obj_geo.rec_exact") != null) {
    			addElementEntry(elements, refRecord.getString("t011_obj_geo.rec_exact").concat(" m"), messages.getString("t011_obj_geo.rec_exact"));
    		}
    		if (refRecord.getString("t011_obj_geo.special_base") != null) {
    			addElementEntry(elements, refRecord.getString("t011_obj_geo.special_base").concat(" m"), messages.getString("t011_obj_geo.special_base"));
    		}
    		if (refRecord.getString("t011_obj_geo.data_base") != null) {
    			addElementEntry(elements, refRecord.getString("t011_obj_geo.data_base").concat(" m"), messages.getString("t011_obj_geo.data_base"));
    		}
    		
    		listRecords = getSubRecordsByColumnName(record, "t011_obj_geo_supplinfo.line");
    		if (listRecords.size() > 0 && !(listRecords.size() == 1 && ((Record)(listRecords.get(0))).getString("t011_obj_geo_supplinfo.feature_type") == null)) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "multiLine");
    	    	element.put("title", messages.getString("t011_obj_geo_supplinfo.feature_type"));
    	    	ArrayList lines = new ArrayList();
    	    	element.put("elements", lines);
    	    	for (int i=0; i<listRecords.size(); i++) {
    	    		Record listRecord = (Record)listRecords.get(i);
    	        	HashMap line = new HashMap();
    	        	line.put("type", "textLine");
    	        	line.put("body", listRecord.getString("t011_obj_geo_supplinfo.feature_type"));
    	        	lines.add(line);
    	    	}
        	    elements.add(element);
    		}
    		addElementEntry(elements, refRecord.getString("t011_obj_geo.method"), messages.getString("t011_obj_geo.method"));
    	}
	}	

	
	private void addReferenceObjectClass4(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t011_obj_project.leader");
    	if (refRecords.size() > 0) {
    		Record refRecord = (Record)refRecords.get(0);
    		addElementEntry(elements, refRecord.getString("t011_obj_project.leader"), messages.getString("t011_obj_project.leader"));
    		addElementEntry(elements, refRecord.getString("t011_obj_project.member"), messages.getString("t011_obj_project.member"));
    		addElementEntry(elements, refRecord.getString("t011_obj_project.description"), messages.getString("t011_obj_project.description"));
    	}
	}	
	
	private void addReferenceObjectClass2(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t011_obj_literatur.publisher");
    	if (refRecords.size() > 0) {
    		Record refRecord = (Record)refRecords.get(0);
    		addElementEntry(elements, refRecord.getString("t011_obj_literature.author"), messages.getString("t011_obj_literatur.autor"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.publisher"), messages.getString("t011_obj_literatur.publisher"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.publish_in"), messages.getString("t011_obj_literatur.publish_in"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.volume"), messages.getString("t011_obj_literatur.volume"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.sides"), messages.getString("t011_obj_literatur.sides"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.publish_year"), messages.getString("t011_obj_literatur.publish_year"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.isbn"), messages.getString("t011_obj_literatur.isbn"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.publishing"), messages.getString("t011_obj_literatur.publishing"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.publish_loc"), messages.getString("t011_obj_literatur.publish_loc"));
    		addElementEntryInline(elements, refRecord.getString("t011_obj_literature.type_value"), messages.getString("t011_obj_literatur.typ"));
    		addElementEntry(elements, refRecord.getString("t011_obj_literature.doc_info"), messages.getString("t011_obj_literatur.doc_info"));
    		addElementEntry(elements, refRecord.getString("t011_obj_literature.loc"), messages.getString("t011_obj_literatur.loc"));
    		addElementEntry(elements, refRecord.getString("t011_obj_literature.description"), messages.getString("t011_obj_literatur.description"));
    	}
	}
	
	
	private void addReferenceObjectClass3(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t011_obj_serv.description");
    	if (refRecords.size() > 0) {
    		Record refRecord = (Record)refRecords.get(0);
    		addElementEntry(elements, refRecord.getString("t011_obj_serv.type_value"), messages.getString("t011_obj_serv.type"));
    		addElementEntry(elements, refRecord.getString("t011_obj_serv.environment"), messages.getString("t011_obj_serv.environment"));
    		addElementEntry(elements, refRecord.getString("t011_obj_serv.history"), messages.getString("t011_obj_serv.history"));
    		addElementEntry(elements, refRecord.getString("t011_obj_serv.base"), messages.getString("t011_obj_serv.base"));
    		refRecords = getSubRecordsByColumnName(record, "t011_obj_serv_version.line");
    		if (refRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "multiLine");
    	    	element.put("title", messages.getString("t011_obj_serv_version.version"));
    	    	ArrayList lines = new ArrayList();
    	    	element.put("elements", lines);
    	    	for (int i=0; i<refRecords.size(); i++) {
    	    		refRecord = (Record)refRecords.get(i);
    	        	HashMap line = new HashMap();
    	        	line.put("type", "textLine");
    	        	line.put("body", refRecord.getString("t011_obj_serv_version.version"));
    	        	lines.add(line);
    	    	}
        	    elements.add(element);
    		}
    		refRecords = getSubRecordsByColumnName(record, "t011_obj_serv_operation.line");
    		if (refRecords.size() > 0) {
    	    	HashMap element = new HashMap();
    	    	element.put("type", "table");
    	    	element.put("title", messages.getString("t011_obj_serv_operation"));
    			ArrayList head = new ArrayList();
    			head.add(messages.getString("t011_obj_serv_operation.name"));
    			head.add(messages.getString("t011_obj_serv_operation.descr"));
    			head.add(messages.getString("t011_obj_serv_operation.invocation_name"));
    			element.put("head", head);
    			ArrayList body = new ArrayList();
    			element.put("body", body);
    	    	for (int i=0; i<refRecords.size(); i++) {
    	    		refRecord = (Record)refRecords.get(i);
    	    		ArrayList row = new ArrayList();
    	    		row.add(notNull(refRecord.getString("t011_obj_serv_operation.name_value")));
    	    		row.add(notNull(refRecord.getString("t011_obj_serv_operation.descr")));
    	    		row.add(notNull(refRecord.getString("t011_obj_serv_operation.invocation_name")));
    	    		body.add(row);
    	    	}
    	    	elements.add(element);
    	    	element = new HashMap();
    	    	element.put("type", "space");
    			elements.add(element);

    		}
    		refRecords = getSubRecordsByColumnName(record, "t011_obj_serv_op_connpoint.line");
    		if (refRecords.size() > 0) {
    	    	ArrayList linkList = new ArrayList();
    			HashMap element = new HashMap();
	        	element.put("type", "linkList");
	        	element.put("linkList", linkList);
	        	elements.add(element);
    	    	for (int i=0; i<refRecords.size(); i++) {
    	    		refRecord = (Record)refRecords.get(i);
    	        	if (UtilsVelocity.hasContent(refRecord.getString("t011_obj_serv_op_connpoint.connect_point")).booleanValue()) {
	    	    		HashMap link = new HashMap();
	    	        	link.put("hasLinkIcon", new Boolean(true));
	    	        	link.put("isExtern", new Boolean(false));
	    	        	link.put("title", messages.getString("common.result.showMap"));
	    	        	link.put("href", "portal/main-maps.psml?wms_url=" + UtilsVelocity.urlencode(refRecord.getString("t011_obj_serv_op_connpoint.connect_point")));
	    	        	linkList.add(link);
    	        	}
    	    	}
    		}
    	}
	}
	
	
	private void addReferenceObjectClass5(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t011_obj_data_para.line");
    	if (refRecords.size() > 0) {
			HashMap element = new HashMap();
			element.put("type", "table");
			element.put("title", messages.getString("t011_obj_data_para"));
			// add table headers
			ArrayList head = new ArrayList();
			head.add(messages.getString("t011_obj_data_para.parameter"));
			head.add(messages.getString("t011_obj_data_para.unit"));
			element.put("head", head);
			ArrayList body = new ArrayList();
			element.put("body", body);
	    	for (int i=0; i<refRecords.size(); i++) {
	    		Record refRecord = (Record)refRecords.get(i);
	    		ArrayList row = new ArrayList();
	    		row.add(notNull(refRecord.getString("t011_obj_data_para.parameter")));
	    		row.add(notNull(refRecord.getString("t011_obj_data_para.unit")));
	    		body.add(row);
	    	}
	    	elements.add(element);
	    	element = new HashMap();
	    	element.put("type", "space");
			elements.add(element);
	    	
    	}
    	refRecords = getSubRecordsByColumnName(record, "t011_obj_data.base");
    	if (refRecords.size() > 0) {
    		Record refRecord = (Record)refRecords.get(0);
    		addElementEntry(elements, refRecord.getString("t011_obj_data.base"), messages.getString("t011_obj_data.base"));
    		addElementEntry(elements, refRecord.getString("t011_obj_data.description"), messages.getString("t011_obj_data.description"));
    	}
	}
	
	
	
	private void addElementEntry(List elements, String body, String title) {
		if (UtilsVelocity.hasContent(body).booleanValue()) {
			HashMap element = new HashMap();
			element.put("type", "entry");
			element.put("title", title);
			element.put("body", body);
			elements.add(element);
		}
	}

	private void addElementEntryInline(List elements, String body, String title) {
		if (UtilsVelocity.hasContent(body).booleanValue()) {
			HashMap element = new HashMap();
			element.put("type", "entryInline");
			element.put("title", title);
			element.put("body", body);
			elements.add(element);
		}
	}
	
	private void addElementUdkClass(List elements, String udkClass) {
		if (UtilsVelocity.hasContent(udkClass).booleanValue()) {
			HashMap element = new HashMap();
			element.put("type", "udkClass");
			element.put("udkClass", udkClass);
			element.put("udkClassName", messages.getString("udk_obj_class_name_".concat(udkClass)));
			elements.add(element);
		}
	}
	
    private void addSuperiorObjects(List elements, String objUuid) {
        List linkList = getLinkListOfObjectsFromQuery("children.object_node.obj_uuid:".concat(objUuid).concat(
                " iplugs:\"").concat(DetailDataPreparerHelper.getPlugIdFromAddressPlugId(iPlugId)).concat("\""));
        if (!linkList.isEmpty()) {
        	HashMap element = new HashMap();
        	element.put("type", "linkList");
        	element.put("title", messages.getString("superior_references"));
        	element.put("linkList", linkList);
        	elements.add(element);
        }
    }

    private void addSubordinatedObjects(List elements, String objUuid) {
        List linkList = getLinkListOfObjectsFromQuery("parent.object_node.obj_uuid:".concat(objUuid).concat(
                " iplugs:\"").concat(DetailDataPreparerHelper.getPlugIdFromAddressPlugId(iPlugId)).concat("\""));
        if (!linkList.isEmpty()) {
        	HashMap element = new HashMap();
        	element.put("type", "linkList");
        	element.put("title", messages.getString("subordinated_references"));
        	element.put("linkList", linkList);
        	elements.add(element);
        }
    }

    private void addCrossReferencedObjects(List elements, String objId) {
    	List linkList = getLinkListOfObjectsFromQuery("object_reference.obj_from_id:".concat(objId).concat(
                " iplugs:\"").concat(iPlugId).concat("\""));
        if (!linkList.isEmpty()) {
        	HashMap element = new HashMap();
        	element.put("type", "linkList");
        	element.put("title", messages.getString("cross_references"));
        	element.put("linkList", linkList);
        	elements.add(element);
        }
    }
    
    private void addUrlReferences(List elements, Record record) {
        ArrayList linkList = new ArrayList();
    	List refRecords = getSubRecordsByColumnName(record, "t017_url_ref.line");
    	for (int i=0; i<refRecords.size(); i++) {
    		Record refRecord = (Record)refRecords.get(i);
        	HashMap link = new HashMap();
        	link.put("hasLinkIcon", new Boolean(true));
        	link.put("isExtern", new Boolean(true));
        	link.put("title", refRecord.getString("t017_url_ref.content"));
        	link.put("href", refRecord.getString("t017_url_ref.url_link"));
        	linkList.add(link);
    	}
        if (!linkList.isEmpty()) {
        	HashMap element = new HashMap();
        	element.put("type", "linkList");
        	element.put("title", messages.getString("www_references"));
        	element.put("linkList", linkList);
        	elements.add(element);
        }
    }
    
    private void addAddresses(List elements, Record record) {
    	List refRecords = getSubRecordsByColumnName(record, "t012_obj_adr.adr_id");
    	for (int i=0; i<refRecords.size(); i++) {
    		Record refRecord = (Record)refRecords.get(i);
    		addAddressType(elements, refRecord);
    	}
    }
    
    private void addAddressType(List elements, Record record) {
    	HashMap element = new HashMap();
    	element.put("type", "multiLine");
    	element.put("title", record.getString("t012_obj_adr.special_name"));
    	element.put("elements", new ArrayList());
    	elements.add(element);
    	List refRecords = getSubRecordsByColumnName(record, "t02_address.adr_id");
    	for (int i=0; i<refRecords.size(); i++) {
    		Record refRecord = (Record)refRecords.get(i);
    		addAddress((List)(element.get("elements")), refRecord);
    	}
    }

    private void addAddress(List elements, Record record) {
    	HashMap element = new HashMap();
    	// get address parents
    	// TODO after mapping addresses

    	// generate action url to show address detail
    	PortletURL actionUrl = response.createActionURL();
    	actionUrl.setParameter("cmd", "doShowAddressDetail");
		actionUrl.setParameter("addrId", record.getString("t02_address.adr_uuid"));
		actionUrl.setParameter("plugid", iPlugId);
    	
    	// address type
    	int addressType = -1;
		try {
			addressType = Integer.parseInt(record.getString("t02_address.adr_type"));
		} catch (NumberFormatException e) {
			log.debug("Illegal address classification (institution, unit, ...) found: " + record.getString("t02_address.adr_type"));
		}
    	if (addressType == 0 || addressType == 3) {
	    	element.put("type", "linkLine");
	    	element.put("hasLinkIcon", new Boolean(false));
	    	element.put("isExtern", new Boolean(false));
			element.put("title", record.getString("t02_address.institution"));
			element.put("href", actionUrl.toString());
    	} else {
	    	element.put("type", "textLine");
			element.put("body", record.getString("t02_address.institution"));
    	}
		elements.add(element);
    	// address, title, name with link
		if (UtilsVelocity.hasContent(record.getString("t02_address.lastname")).booleanValue()) {
	    	String textLine = "";
	    	if (UtilsVelocity.hasContent(record.getString("t02_address.address_value")).booleanValue()) {
	    		textLine = textLine.concat(record.getString("t02_address.address_value"));
	    	}
	    	if (UtilsVelocity.hasContent(record.getString("t02_address.title_value")).booleanValue()) {
	    		if (textLine.length() > 0) {
	    			textLine = textLine.concat(" ");
	    		}
	    		textLine = textLine.concat(record.getString("t02_address.title_value"));
	    	}
	    	if (UtilsVelocity.hasContent(record.getString("t02_address.firstname")).booleanValue()) {
	    		if (textLine.length() > 0) {
	    			textLine = textLine.concat(" ");
	    		}
	    		textLine = textLine.concat(record.getString("t02_address.firstname"));
	    	}
	    	if (UtilsVelocity.hasContent(record.getString("t02_address.lastname")).booleanValue()) {
	    		if (textLine.length() > 0) {
	    			textLine = textLine.concat(" ");
	    		}
	    		textLine = textLine.concat(record.getString("t02_address.lastname"));
	    	}
			
	    	element = new HashMap();
	    	element.put("type", "linkLine");
	    	element.put("hasLinkIcon", new Boolean(false));
	    	element.put("isExtern", new Boolean(false));
			element.put("title", textLine);
			element.put("href", actionUrl.toString());
			elements.add(element);
		}
		// description
		if (UtilsVelocity.hasContent(record.getString("t02_address.descr")).booleanValue()) {
	    	element = new HashMap();
	    	element.put("type", "textLine");
			element.put("body", record.getString("t02_address.descr"));
			elements.add(element);
		}
		// post box information
		if (UtilsVelocity.hasContent(record.getString("t02_address.postbox")).booleanValue()) {
	    	element = new HashMap();
	    	element.put("type", "textLine");
			element.put("body", messages.getString("postbox_label").concat(" ").concat(record.getString("t02_address.postbox")));
			elements.add(element);
			if (UtilsVelocity.hasContent(record.getString("t02_address.postbox_pc")).booleanValue()) {
				String textLine = record.getString("t02_address.postbox_pc");
				if (UtilsVelocity.hasContent(record.getString("t02_address.country_code")).booleanValue()) {
					textLine = messages.getString("postal.country.code.".concat(record.getString("t02_address.country_code"))).concat("-").concat(textLine);
				}
				if (UtilsVelocity.hasContent(record.getString("t02_address.city")).booleanValue()) {
					textLine = textLine.concat(" ").concat(record.getString("t02_address.city"));
				}
				element = new HashMap();
				element.put("type", "textLine");
				element.put("body", textLine);
				elements.add(element);
			}
	    	element = new HashMap();
	    	element.put("type", "space");
			elements.add(element);
			
		}
		if (UtilsVelocity.hasContent(record.getString("t02_address.street")).booleanValue()) {
			if (UtilsVelocity.hasContent(record.getString("t02_address.street")).booleanValue()) {
				element = new HashMap();
				element.put("type", "textLine");
				element.put("body", record.getString("t02_address.street"));
				elements.add(element);
			}
			if (UtilsVelocity.hasContent(record.getString("t02_address.postcode")).booleanValue()) {
				String textLine = record.getString("t02_address.postcode");
				if (UtilsVelocity.hasContent(record.getString("t02_address.country_code")).booleanValue()) {
					textLine = messages.getString("postal.country.code.".concat(record.getString("t02_address.country_code"))).concat("-").concat(textLine);
				}
				if (UtilsVelocity.hasContent(record.getString("t02_address.city")).booleanValue()) {
					textLine = textLine.concat(" ").concat(record.getString("t02_address.city"));
				}
				element = new HashMap();
				element.put("type", "textLine");
				element.put("body", textLine);
				elements.add(element);
			}
	    	element = new HashMap();
	    	element.put("type", "space");
			elements.add(element);
		}
    	List refRecords = getSubRecordsByColumnName(record, "t021_communication.comm_value");
    	for (int i=0; i<refRecords.size(); i++) {
    		Record refRecord = (Record)refRecords.get(i);
    		addCommunication(elements, refRecord);
    	}
    }

    
    private void addCommunication(List elements, Record record) {
    	HashMap element = new HashMap();
		element.put("type", "textLine");
		String textLine = record.getString("t021_communication.comm_value");
		if (UtilsVelocity.hasContent(record.getString("t021_communication.commtype_value")).booleanValue()) {
			if (textLine != null) {
				record.getString("t021_communication.commtype_value").concat(": ").concat(textLine);
			}
		}
		element.put("body", textLine);
    	elements.add(element);
    }
    
    private void addLine(List elements) {
    	HashMap element = new HashMap();
		element.put("type", "line");
		elements.add(element);
    }
    
    
    private List getLinkListOfObjectsFromQuery(String queryStr) {
        ArrayList result = DetailDataPreparerHelper.getHits(queryStr, new String[] {}, null);
        ArrayList linkList = new ArrayList();
        for (int i=0; i<result.size(); i++) {
        	IngridHit hit = (IngridHit)result.get(i);
        	HashMap link = new HashMap();
        	link.put("hasLinkIcon", new Boolean(true));
        	link.put("isExtern", new Boolean(false));
        	link.put("title", ((HashMap)hit.get("detail")).get("title"));
        	PortletURL actionUrl = response.createActionURL();
        	actionUrl.setParameter("cmd", "doShowDocument");
    		actionUrl.setParameter("docid", hit.getId().toString());
    		actionUrl.setParameter("plugid", iPlugId);
    		if (hit.getString("alt_document_id") != null) {
    			actionUrl.setParameter("altdocid", hit.getString("alt_document_id"));
    		}
        	link.put("href", actionUrl.toString());
        	linkList.add(link);
        }
    	return linkList;
    }
    
    
    /**
     * Get all subrecords that contain a given column name.
     * 
     * @param record The record to examine.
     * @param columnName The column name.
     * @return A List of records.
     */
    private List getSubRecordsByColumnName(Record record, String columnName) {
    	ArrayList result = new ArrayList();
        Record[] subRecords = record.getSubRecords();
        for (int i = 0; i < subRecords.length; i++) {
        	Record subRecord = subRecords[i];
        	Column[] columns =  subRecord.getColumns();
            for (int j = 0; j < columns.length; j++) {
            	Column column =  columns[j];
            	if (column.getTargetName().equalsIgnoreCase(columnName)) {
            		result.add(subRecord);
            		break;
            	}
            }
            result.addAll(getSubRecordsByColumnName(subRecord, columnName));
        }
        return result;
    }
    
    
    private String notNull(String in) {
    	if (in == null) {
    		return "";
    	} else {
    		return in;
    	}
    }
    
	
}
