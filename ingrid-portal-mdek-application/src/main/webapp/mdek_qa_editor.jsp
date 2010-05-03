<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<script type="text/javascript">
var scriptScope = this;

// display 10 datasets per page
var resultsPerPage = 10;
//var tableIdList = ["expObjTable", "expAdrTable", "modObjTable", "modAdrTable", "qaObjTable", "qaAdrTable"];
var tableIdList = ["expObjTable", "expAdrTable", "modObjTable", "modAdrTable", "qaObjTable", "qaAdrTable", "spatialObjTable"];

_container_.addOnLoad(function() {
	initTables();
	initSortFunctions();
	reloadTables();
});


// Initialise the tables with PageNavs, sortParams, etc.
function initTables() {
	// Add PageNavs to tables
	dojo.lang.forEach(tableIdList, function(tableId) {
		dojo.widget.byId(tableId).pageNav = new PageNavigation({
				resultsPerPage: resultsPerPage,
				infoSpan:dojo.byId(tableId+"Info"),
				pagingSpan:dojo.byId(tableId+"Paging") });
	});

	// Add types to tables
	dojo.widget.byId("expObjTable").type = "EXPIRED";
	dojo.widget.byId("expAdrTable").type = "EXPIRED";
	dojo.widget.byId("modObjTable").type = "MODIFIED";
	dojo.widget.byId("modAdrTable").type = "MODIFIED";
	dojo.widget.byId("qaObjTable").type = "IN_QA_WORKFLOW";
	dojo.widget.byId("qaAdrTable").type = "IN_QA_WORKFLOW";
	dojo.widget.byId("spatialObjTable").type = "SPATIAL_REF_EXPIRED";

	// Add initial sortParams to tables
	dojo.lang.forEach(tableIdList, function(tableId) {
		dojo.widget.byId(tableId).sortParams = { sortBy: "NAME", sortAsc: true }
	});

	// Connect PageNavs with navigate functions
	dojo.lang.forEach(tableIdList, function(tableId) {
		dojo.event.connectOnce("after", dojo.widget.byId(tableId).pageNav, "onPageSelected", function() { navigateTable(tableId); });
	});
}

// Init the sort functions
function initSortFunctions() {
	dojo.lang.forEach(tableIdList, function(tableId) {
		var table = dojo.widget.byId(tableId);
		table.onSort = dojo.lang.curry(table, sortTable, tableId);
		// remove the standard sorter since we sort by starting a new query
		table.createSorter = function(){return null;}
	});
}


// If a table header is clicked, the sortTable function is invoked with the corresponding table
function sortTable(tableId, e) {
	var table = dojo.widget.byId(tableId);
	var field = getFieldFromTableHeaderClickEvent(table, e);
	var sortBy = getSortIdentifierFor(field);

	if (sortBy == "") {
		return;

	} else if (sortBy == table.sortParams.sortBy) {
		table.sortParams.sortAsc = !table.sortParams.sortAsc;

	} else {
		table.sortParams.sortBy = sortBy;
		table.sortParams.sortAsc = true;
	}

	navigateTable(tableId);
}


// Convert table field identifiers to sort identifiers (de.ingrid.mdek.MdekUtils.IdcEntityOrderBy)
function getSortIdentifierFor(field) {
//	dojo.debug("getSortIdentifierFor("+field+") called");

	if (field == "icon") {
		return "CLASS";

	} else if (field == "linkLabel") {
		return "NAME";

	} else if (field == "expiryDate" || field == "date") {
		return "DATE";

	} else if (field == "modUserTitle" || field == "assignerUserTitle") {
		return "USER";

	} else if (field == "state") {
		return "STATE";

	} else {
		return "";
	}
}

// Get the column identifier from the ClickEvent.
function getFieldFromTableHeaderClickEvent(table, e) {
	var source = e.target;
	var row = dojo.html.getParentByType(source,"tr");
	var cellTag = "td";
	if(row.getElementsByTagName(cellTag).length == 0){
		cellTag = "th";
	}

	var headers = row.getElementsByTagName(cellTag);
	var header = dojo.html.getParentByType(source,cellTag);

	for(var i=0; i<headers.length; i++){
		if(headers[i] == header){
			var meta = table.columns[i];
			return meta.getField();
		}
	}
	// Field was not found (should not happen), return an empty string
	return "";
}

// Reset PageNavs
function resetAllPageNavs() {
	dojo.lang.forEach(tableIdList, function(tableId) {
		dojo.widget.byId(tableId).pageNav.reset();
	});
}

// Reset the tables sorting parameters
function resetAllSortParams() {
	dojo.lang.forEach(tableIdList, function(tableId) {
		var tableWidget = dojo.widget.byId(tableId);
		tableWidget.sortParams.sortBy = "NAME";
		tableWidget.sortParams.sortAsc = true;
	});
}

// Reload all the tables on the page
function reloadTables() {
	resetAllPageNavs();
	resetAllSortParams();

	var defList = [];
	dojo.lang.forEach(tableIdList, function(tableId) {
		defList.push(navigateTable(tableId));
	});

	enterLoadingState();

	var l1 = new dojo.DeferredList(defList, false, false, true);
	l1.addCallback(function (resultList) {
		var expObjResult = resultList[0][1];
		var expAdrResult = resultList[1][1];
		var modObjResult = resultList[2][1];
		var modAdrResult = resultList[3][1];
		var qaObjResult = resultList[4][1];
		var qaAdrResult = resultList[5][1];
		var spatialObjResult = resultList[6][1];

		// Count the number of occurences and set the overview lst
		var numAdrStateQ = qaAdrResult.additionalData['total-num-qa-assigned'];
		var numAdrStateR = qaAdrResult.additionalData['total-num-qa-reassigned'];
		var numObjStateQ = qaObjResult.additionalData['total-num-qa-assigned'];
		var numObjStateR = qaObjResult.additionalData['total-num-qa-reassigned'];
		initOverviewTable(expObjResult.totalNumHits, expAdrResult.totalNumHits, numObjStateQ, numObjStateR, numAdrStateQ, numAdrStateR, spatialObjResult.totalNumHits);

		exitLoadingState();
	});

	l1.addErrback(function(err) {
		dojo.debug("Error: "+err);
		dojo.debugShallow(err);
		exitLoadingState();
	});
}

// NavigateTable should be called whenever the page or sortParameters have changed
// A new request will be started with the new starHit (from PageNav) and sortParameters (from the Table)
function navigateTable(tableId) {
	var table = dojo.widget.byId(tableId);
	var startHit = table.pageNav.getStartHit();

	var def;
	if (tableId.indexOf("Obj") != -1) {
		def = getWorkObjects(table.type, table.sortParams.sortBy,
			table.sortParams.sortAsc, startHit, resultsPerPage);

	} else {
		def = getWorkAddresses(table.type, table.sortParams.sortBy,
			table.sortParams.sortAsc, startHit, resultsPerPage);
	}

	// After the data is loaded from the backend, update the table and return the result for the
	// following callback functions
	def.addCallback(function(res) {updateTable(tableId, res); return res; });

	return def;
}

// Update a table with new data from SearchResult (see de.ingrid.mdek.beans.query.(Object|Address)SearchResultBean)
// The connected PageNav is updated automatically
function updateTable(tableId, searchResult) {
	var table = dojo.widget.byId(tableId);
	var resList = searchResult.resultList;
	var numHits = searchResult.numHits;
	var totalNumHits = searchResult.totalNumHits;

	if (tableId.indexOf("Obj") != -1) {
		addObjectTableIdentifiers(resList);
	} else {
		addAddressTableIdentifiers(resList);
	}
	addCommonTableIdentifiers(resList);

	table.store.setData(resList);
	table.pageNav.setTotalNumHits(totalNumHits);
	table.pageNav.updateDomNodes();
}

// Get Work Objects from the backend. The result is returned in a deferred object.
// type - de.ingrid.mdek.MdekUtils.IdcWorkEntitiesSelectionType
// sortBy - de.ingrid.mdek.MdekUtils.IdcEntityOrderBy
// sortAsc - boolean
// startHit - int, where to start
// numHits - int, limit number of results
function getWorkObjects(type, sortBy, sortAsc, startHit, numHits) {
	var def = new dojo.Deferred();

	ObjectService.getWorkObjects(type, sortBy, sortAsc, startHit, numHits, {
		callback: function(result) {
			def.callback(result);
		},
		errorHandler: function(errMsg, err) {
			dojo.debug("Error: "+errMsg);
			dojo.debugShallow(err);
			displayErrorMessage(err);
			def.errback(err);
		}
	});
	return def;
}

// Get Work Addresses from the backend. The result is returned in a deferred object.
// type - de.ingrid.mdek.MdekUtils.IdcWorkEntitiesSelectionType
// sortBy - de.ingrid.mdek.MdekUtils.IdcEntityOrderBy
// sortAsc - boolean
// startHit - int, where to start
// numHits - int, limit number of results
function getWorkAddresses(type, sortBy, sortAsc, startHit, numHits) {
	var def = new dojo.Deferred();

	AddressService.getWorkAddresses(type, sortBy, sortAsc, startHit, numHits, {
		callback: function(result) {
			def.callback(result);
		},
		errorHandler: function(errMsg, err) {
			dojo.debug("Error: "+errMsg);
			dojo.debugShallow(err);
			displayErrorMessage(err);
			def.errback(err);
		}
	});
	return def;
}

// Add labels to each dataset in dsList, so they are properly displayed in the table.
// Properties that will be set:
// ds.lastEditor, ds.assignerUser, ds.date (last modification), ds.expiryData, ds.userOperation, ds.workState
function addCommonTableIdentifiers(dsList) {
	if (dsList) {
		dojo.lang.forEach(dsList, function(ds) {
			// modUser
			if (ds.lastEditor) {
				var modUserAddressTitle = UtilAddress.createAddressTitle(ds.lastEditor);
				ds.modUserTitle = UtilAddress.createAddressLinkLabel(ds.lastEditor.uuid, modUserAddressTitle);
			}
			// assignerUser
			if (ds.assignerUser) {
				var assignerUserAddressTitle = UtilAddress.createAddressTitle(ds.assignerUser);
				ds.assignerUserTitle = UtilAddress.createAddressLinkLabel(ds.assignerUser.uuid, assignerUserAddressTitle);
			}
			// date & expiryDate
			ds.date = dojo.date.parse(ds.modificationTime, {datePattern: "dd.MM.yyyy"});
			ds.expiryDate = dojo.date.parse(ds.modificationTime, {datePattern: "dd.MM.yyyy"});
 			ds.expiryDate.setDate(ds.expiryDate.getDate()+catalogData.expiryDuration);
			// state
			if (ds.userOperation) {
				ds.type = getUserOperationText(ds.userOperation);
			}
			if (ds.workState) {
				ds.state = getWorkStateText(ds.workState);
			}
		});
	}
}

// Add titles to all objects in objList (escape obj.objectName and write it to obj.title)
function addObjectTableIdentifiers(objList) {
	if (objList) {
		dojo.lang.forEach(objList, function(obj) {
			obj.title = dojo.string.escape("html", obj.objectName);
		});
		UtilList.addObjectLinkLabels(objList);
		UtilList.addIcons(objList);
	}
}

// Add titles to all objects in objList (escape adr title and write it to adr.title)
function addAddressTableIdentifiers(adrList) {
	if (adrList) {
		dojo.lang.forEach(adrList, function(adr) {
			adr.title = UtilAddress.createAddressTitle(adr);
		});
		UtilList.addAddressLinkLabels(adrList);
		UtilList.addIcons(adrList);
	}

}

function initOverviewTable(numExpObj, numExpAdr, numObjStateQ, numObjStateR, numAdrStateQ, numAdrStateR, numSpatialObj) {
	var types = [{Id:0, type:message.get("dialog.qa.spatialRefMod"), obj:numSpatialObj, adr:0},
				 {Id:1, type:message.get("dialog.qa.expired"), obj:numExpObj, adr:numExpAdr},
				 {Id:2, type:message.get("dialog.qa.assignedToQa"), obj:numObjStateQ, adr:numAdrStateQ},
				 {Id:3, type:message.get("dialog.qa.reassignedFromQa"), obj:numObjStateR, adr:numAdrStateR}];

	dojo.widget.byId("editorsOverviewTable").store.setData(types);
}

// Get The localized text for a userOperation (NEW, EDITED, DELETED)
function getUserOperationText(userOperation) {
	return message.get("general.userOperation."+userOperation);
}

// Get The localized text for a workState (Q, R)
function getWorkStateText(workState) {
	return message.get("general.workState."+workState);
}

function enterLoadingState() {
	dojo.byId("qaEditorLoadingZone").style.display = "block";
	dojo.byId("qaEditorContent").style.visibility = "hidden";
}

function exitLoadingState() {
	dojo.byId("qaEditorLoadingZone").style.display = "none";
	dojo.byId("qaEditorContent").style.visibility = "visible";
}

// Button function to reload the page
scriptScope.reloadPage = function() {
	reloadTables();
}

</script>
</head>

<body>
<div dojoType="ContentPane" layoutAlign="client">

	<div class="contentBlockWhite top wideBlock">
		<div id="winNavi">
			<a href="javascript:void(0);" onclick="javascript:window.open('mdek_help.jsp?hkey=quality-assurance-1#quality-assurance-1', 'Hilfe', 'width=750,height=550,resizable=yes,scrollbars=yes,locationbar=no');" title="<fmt:message key="general.help" />">[?]</a>
		</div>

		<span class="label" id="qaEditorLoadingZone">
			<div z-index: 100;">
		        <img src="img/ladekreis.gif" style="background-color:#FFFFFF;" />
		        <label>&nbsp;Bitte warten, die Daten werden geladen...</label>
		    </div>
		</span>

		<div class="content" id="qaEditorContent" style="visibility:hidden;">

			<div class="spacer"></div>
			<button dojoType="ingrid:Button" title="<fmt:message key="dialog.qa.refresh" />" onClick="javascript:scriptScope.reloadPage();"><fmt:message key="dialog.qa.refresh" /></button>
			<div class="spacer"></div>

			<div class="inputContainer noSpaceBelow">
				<div id="editorsOverview" class="infobox w478">
					<span class="icon"><img src="img/ic_info.gif" width="16" height="16" alt="Info" /></span>
					<span class="title"><a href="javascript:toggleInfo('editorsOverview');" title="<fmt:message key="dialog.qa.editor.overview" />"><fmt:message key="dialog.qa.editor.overview" />
						<img src="img/ic_info_deflate.gif" width="8" height="8" alt="Pfeil" /></a></span>
					<div id="editorsOverviewContent">
						<table id="editorsOverviewTable" dojoType="ingrid:FilteringTable" minRows="3" cellspacing="0" class="filteringTableBlue relativePos nosort">
							<thead>
								<tr>
									<th nosort="true" field="type" dataType="String" width="295"><fmt:message key="dialog.qa.editor.objectState" /></th>
									<th nosort="true" field="obj" dataType="String" width="92"><fmt:message key="dialog.qa.objects" /></th>
									<th nosort="true" field="adr" dataType="String" width="92"><fmt:message key="dialog.qa.addresses" /></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div> <!-- inputContainer end -->

			<span class="label"><label onclick="javascript:dialog.showContextHelp(arguments[0], 7066)"><fmt:message key="dialog.qa.editor.expired" /></label></span>

        	<div id="expDSTabContainer" dojoType="ingrid:TabContainer" doLayout="false" selectedChild="expObjContentPane">
        		<div id="expObjContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.objects" />" style="overflow:hidden;">

					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="expObjTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="expObjTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
		
				        <div class="tableContainer rows10 wide">
							<table id="expObjTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="700"><fmt:message key="dialog.qa.name" /></th>
										<th field="modUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.modUser" /></th>
										<th field="expiryDate" dataType="Date" width="100"><fmt:message key="dialog.qa.expiredAt" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->

        		<div id="expAdrContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.addresses" />" style="overflow:hidden;">

					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="expAdrTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="expAdrTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>

				        <div class="tableContainer rows10 wide">
							<table id="expAdrTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="700"><fmt:message key="dialog.qa.name" /></th>
										<th field="modUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.modUser" /></th>
										<th field="expiryDate" dataType="Date" width="100"><fmt:message key="dialog.qa.expiredAt" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->
			</div> <!-- TabContainer end -->

			<span class="label"><label onclick="javascript:dialog.showContextHelp(arguments[0], 7067)"><fmt:message key="dialog.qa.editor.modified" /></label></span>

        	<div id="modDSTabContainer" dojoType="ingrid:TabContainer" doLayout="false" selectedChild="modObjContentPane">

        		<div id="modObjContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.objects" />" style="overflow:hidden;">
					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="modObjTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="modObjTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
		
				        <div class="tableContainer rows10 wide">
							<table id="modObjTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="550"><fmt:message key="dialog.qa.name" /></th>
										<th field="modUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.modUser" /></th>
										<th field="type" dataType="String" width="160"><fmt:message key="dialog.qa.type" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->

        		<div id="modAdrContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.addresses" />" style="overflow:hidden;">
					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="modAdrTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="modAdrTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
		
				        <div class="tableContainer rows10 wide">
							<table id="modAdrTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="550"><fmt:message key="dialog.qa.name" /></th>
										<th field="modUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.modUser" /></th>
										<th field="type" dataType="String" width="160"><fmt:message key="dialog.qa.type" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->
			</div> <!-- TabContainer end -->

			<span class="label"><label onclick="javascript:dialog.showContextHelp(arguments[0], 7068)"><fmt:message key="dialog.qa.editor.qa" /></label></span>

        	<div id="qaDSTabContainer" dojoType="ingrid:TabContainer" doLayout="false" selectedChild="qaObjContentPane">

        		<div id="qaObjContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.objects" />" style="overflow:hidden;">
					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="qaObjTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="qaObjTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
		
				        <div class="tableContainer rows10 wide">
							<table id="qaObjTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="340"><fmt:message key="dialog.qa.name" /></th>
										<th field="assignerUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.assignedBy" /></th>
										<th field="state" dataType="String" width="210"><fmt:message key="dialog.qa.state" /></th>
										<th field="type" dataType="String" width="160"><fmt:message key="dialog.qa.type" /></th>
										<th field="date" dataType="Date" width="100"><fmt:message key="dialog.qa.date" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->

        		<div id="qaAdrContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.addresses" />" style="overflow:hidden;">
					<div class="inputContainer">
						<div class="listInfo wide">
							<span id="qaAdrTableInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="qaAdrTablePaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
		
				        <div class="tableContainer rows10 wide">
							<table id="qaAdrTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
								<thead>
									<tr>
										<th field="icon" dataType="String" width="32"></th>
										<th field="linkLabel" dataType="String" width="340"><fmt:message key="dialog.qa.name" /></th>
										<th field="assignerUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.assignedBy" /></th>
										<th field="state" dataType="String" width="210"><fmt:message key="dialog.qa.state" /></th>
										<th field="type" dataType="String" width="160"><fmt:message key="dialog.qa.type" /></th>
										<th field="date" dataType="Date" width="100"><fmt:message key="dialog.qa.date" /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> <!-- tableContainer end -->
					</div> <!-- inputContainer end -->
				</div> <!-- ContentPane end -->
			</div> <!-- TabContainer end -->

			<span class="label"><label onclick="javascript:dialog.showContextHelp(arguments[0], 7066)"><fmt:message key="dialog.qa.editor.spatial" /></label></span>

    		<div id="spatialObjContentPane" dojoType="ContentPane" label="<fmt:message key="dialog.qa.objects" />" style="overflow:hidden;">

				<div class="inputContainer">
					<div class="listInfo wide">
						<span id="spatialObjTableInfo" class="searchResultsInfo">&nbsp;</span>
						<span id="spatialObjTablePaging" class="searchResultsPaging">&nbsp;</span>
						<div class="fill"></div>
					</div>
	
			        <div class="tableContainer rows10 wide">
						<table id="spatialObjTable" dojoType="ingrid:FilteringTable" defaultDateFormat="%d.%m.%Y" minRows="10" cellspacing="0" class="filteringTable">
							<thead>
								<tr>
									<th field="icon" dataType="String" width="32"></th>
									<th field="linkLabel" dataType="String" width="700"><fmt:message key="dialog.qa.name" /></th>
									<th field="modUserTitle" dataType="String" width="130"><fmt:message key="dialog.qa.modUser" /></th>
									<th field="expiryDate" dataType="Date" width="100"><fmt:message key="dialog.qa.expiredAt" /></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div> <!-- tableContainer end -->
				</div> <!-- inputContainer end -->
			</div> <!-- ContentPane end -->

		</div> <!-- content end -->
	</div> <!-- contentBlock end -->
</div> <!-- contentPane end -->
</body>
</html>
