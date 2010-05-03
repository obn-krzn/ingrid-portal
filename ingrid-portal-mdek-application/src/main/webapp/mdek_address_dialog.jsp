<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<title>Adresse hinzuf&uuml;gen</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="author" content="wemove digital solutions" />
<meta name="copyright" content="wemove digital solutions GmbH" />

<script type="text/javascript">
var adrPageNav = new PageNavigation({ resultsPerPage: 10, infoSpan:dojo.byId("addressLinkSearchResultsInfo"), pagingSpan:dojo.byId("addressLinkSearchResultsPaging") });

var currentQuery = {
	organisation: null,
	name: null,
	givenName: null
}

// All the possible values for special ref links
var referenceMap = [["Standort", "3360"],
					["Projektleiter", "3400"],
					["Beteiligte", "3410"]];


_container_.addOnUnload(function() {
	if (_container_.customParams && _container_.customParams.resultHandler) {
		if (_container_.customParams.resultHandler.fired == -1) {
			_container_.customParams.resultHandler.errback();
		}
	}
});


_container_.addOnLoad(function() {
	// Initialize the address tree	
	// Load initial first level of the tree from the server
	TreeService.getSubTree(null, null, 
		function (str) {
			for (var i = 0; i < str.length; i++) {
				str[i].id = "AssignAdr_"+str[i].id;
			}

			var tree = dojo.widget.byId("treeAdr");

			// Only use 'addresses' and drop 'objects'
			tree.setChildren([str[1]]);
		});

	// Load children of the node from server
	// Overwritten to work with dwr.
	var treeController = dojo.widget.byId("treeControllerAdr");
	treeController.loadRemote = function(node, sync){
		var _this = this;

		var params = {
			node: this.getInfo(node),
			tree: this.getInfo(node.tree)
		};

		var deferred = new dojo.Deferred();

		TreeService.getSubTree(node.id.substring(10, node.id.length), node.nodeAppType, {
  			callback:function(res) { deferred.callback(res); },
//			timeout:20000,
			errorHandler:function(message) { deferred.errback(new dojo.RpcError(message, this)); },
			exceptionHandler:function(message) { deferred.errback(new dojo.RpcError(message, this)); }
  		});

		deferred.addCallback(function(res) {
			for (var i = 0; i < res.length; i++) {
				res[i].id = "AssignAdr_"+res[i].id;
			}
			return _this.loadProcessResponse(node,res);
		});
		
		deferred.addErrback(function(res) { alert("Error while loading data from the server. Please check your connection and try again!"); return res;});
		return deferred;
	};

	// Hide the result table
	dojo.html.hide(dojo.byId("addressLinkSearchResults"));

	dojo.event.connect("after", adrPageNav, "onPageSelected", browseSearchResults);

	// Connect the 'enter' button on the three input fields with startNewSearch
	var startSearchOnEnter = function(event) {
        if (event.keyCode == event.KEY_ENTER) {
            startNewSearch();
        }
	}

    dojo.event.connect(dojo.widget.byId("addressSearchUnit").domNode, "onkeypress", startSearchOnEnter);
    dojo.event.connect(dojo.widget.byId("addressSearchLastname").domNode, "onkeypress", startSearchOnEnter);
    dojo.event.connect(dojo.widget.byId("addressSearchFirstname").domNode, "onkeypress", startSearchOnEnter);
});


function addAddressIcon(adr) {
	switch (adr.addressClass) {
		case 0:	// Institution
			adr.icon = "<img src='img/UDK/addr_institution.gif' width=\"16\" height=\"16\" alt=\"Address\" />";		
			break;
		case 1:	// Unit
			adr.icon = "<img src='img/UDK/addr_unit.gif' width=\"16\" height=\"16\" alt=\"Address\" />";		
			break;
		case 2:	// Person
			adr.icon = "<img src='img/UDK/addr_person.gif' width=\"16\" height=\"16\" alt=\"Address\" />";		
			break;
		case 3:	// Free
			adr.icon = "<img src='img/UDK/addr_free.gif' width=\"16\" height=\"16\" alt=\"Address\" />";		
			break;
		default:
			adr.icon = "<img src='img/UDK/addr_institution.gif' width=\"16\" height=\"16\" alt=\"Address\" />";		
			break;
	}
}

function addAddressTitle(adr) {
	adr.title = UtilAddress.createAddressTitle(adr);
}

function addAddressLinkLabel(adr) {
	adr.linkLabel = "<a href='javascript:menuEventHandler.handleSelectNodeInTree(\""+adr.uuid+"\", \"A\");'"+
					   "title='"+adr.title+"'>"+adr.title+"</a>";
}


function addAddressToStore(adrId) {
	// If the dialog was called with a resultHandler, call it with the selected address uuid and return
	if (_container_.customParams && _container_.customParams.resultHandler) {
		_container_.customParams.resultHandler.callback(adrId);
		return;
	}

    var store = dojo.widget.byId("generalAddress").store;

	var linkType = null;
	var nameOfRelation = "";
	var typeOfRelation = 0;
	if (_container_.customParams && _container_.customParams.linkType) {
		linkType = _container_.customParams.linkType;
		// TODO get value for link Type, add it to the address
		// getIdValueForLinkType(linkType);
		// ...
		for (var i = 0; i < referenceMap.length; ++i) {
			if (referenceMap[i][1] == linkType) {
				nameOfRelation = referenceMap[i][0];
				break;
			}
		}
	}
	
	AddressService.getAddressData(adrId, "false", {
		callback:function(res){
			if (res != null) {
				addAddressIcon(res);
				addAddressTitle(res);
				addAddressLinkLabel(res);			

				// If the user adds a new address to the 'generalAddress' table
				if (nameOfRelation == "") {
					var data = store.getData();
					// Ckeck if there already is an empty row where the address can be added
					for (var i in data) {
						if (typeof(data[i].uuid) == "undefined") {
							// Remove the object references since we don't need them in the address table
							// This information is removed because it can be quite large if the address is referenced by many objects 
							res.linksFromObjectTable = [];

							// Copy all properties from res to data[i] except the nameOfRelation field
							for (p in res) {
								if (p == "nameOfRelation")
									continue;
								data[i][p] = res[p];
							}

							store.update(data[i], "icon", res.icon);
							store.update(data[i], "linkLabel", res.linkLabel);
							data[i].typeOfRelation = linkType;
							data[i].refOfRelation = 2010;
							dojo.widget.byId("generalAddress").applyValidation();
							return;
						}
					}
				}

				res.Id = UtilStore.getNewKey(store);
				res.typeOfRelation = linkType;
				res.nameOfRelation = nameOfRelation;
				res.refOfRelation = 2010;
				// Remove the object references since we don't need them in the address table
				// This information is removed because it can be quite large if the address is referenced by many objects 
				res.linksFromObjectTable = [];
				store.addData(res);
			}
		},
//		timeout:20000,
		errorHandler:function(message) {
			dojo.debug("Error in js/udkDataProxy.js: Error while waiting for addressData: " + message);
		}
	});
}

addSelectedAddressFromTree = function() {
	var selectedNode = dojo.widget.byId("treeAdr").selectedNode;

	if (selectedNode) {
		var nodeId = selectedNode.id.substring(10);
		if (nodeId != "addressRoot" && nodeId != "addressFreeRoot") {
//			if (dojo.lang.every(dstStore.getData(), function(item){ return (item.uuid != nodeId); })) {
				addAddressToStore(nodeId);
//			}
		}
	}
	_container_.closeWindow();
}

addSelectedAddress = function() {
	var selectedNode = dojo.widget.byId("addressSearchResultsTable").getSelectedData();

	if (selectedNode) {
		var nodeId = selectedNode.uuid;
//		if (dojo.lang.every(dstStore.getData(), function(item){ return (item.uuid != nodeId); })) {
			addAddressToStore(nodeId);
//		}
	}
	_container_.closeWindow();
}


function updateResults(searchResult) {
	dojo.html.show(dojo.byId("addressLinkSearchResults"));	

	UtilList.addIcons(searchResult.resultList);
	UtilList.addAddressTitles(searchResult.resultList);
	UtilList.addTableIndices(searchResult.resultList);

	dojo.widget.byId("addressSearchResultsTable").store.setData(searchResult.resultList);

	adrPageNav.setTotalNumHits(searchResult.totalNumHits);
	adrPageNav.updateDomNodes();
}

function updateQueryFromInput() {
	currentQuery.organisation = dojo.string.trim(dojo.widget.byId("addressSearchUnit").getValue());
	currentQuery.name = dojo.string.trim(dojo.widget.byId("addressSearchLastname").getValue());
	currentQuery.givenName = dojo.string.trim(dojo.widget.byId("addressSearchFirstname").getValue());

	if (currentQuery.organisation.length == 0)
		currentQuery.organisation = null;
	
	if (currentQuery.name.length == 0)
		currentQuery.name = null;
	
	if (currentQuery.givenName.length == 0)
		currentQuery.givenName = null;
}

function browseSearchResults() {
	QueryService.searchAddresses(currentQuery, adrPageNav.getStartHit(), 10, {
		callback: function(res){
			updateResults(res);
		},
//		timeout:30000,
		errorHandler:function(message) {dojo.debug("Error in mdek_address_dialog.jsp: Error while searching for addresses:"+message); }
	});
}

startNewSearch = function() {
	dojo.html.hide(dojo.byId("addressLinkSearchResults"));	

	// build query
	updateQueryFromInput();
	
	// reset the page navigation
	adrPageNav.reset();

	QueryService.searchAddresses(currentQuery, adrPageNav.getStartHit(), 10, {
		callback: function(res){
			updateResults(res);
		},
//		timeout:30000,
		errorHandler:function(message) {dojo.debug("Error in mdek_address_dialog.jsp: Error while searching for addresses:"+message); }
	});
}

</script>

</head>
<body>

<div dojoType="ContentPane">

	<div class="contentBlockWhite top fullBlock">
		<div id="winNavi">
			<a href="javascript:void(0);" onclick="javascript:window.open('mdek_help.jsp?hkey=maintanance-of-objects-2#maintanance-of-objects-2', 'Hilfe', 'width=750,height=550,resizable=yes,scrollbars=yes,locationbar=no');" title="<fmt:message key="general.help" />">[?]</a>
		</div>
	<div id="addressContent" class="content">

		<!-- LEFT HAND SIDE CONTENT START -->
		<div class="spacer"></div>
		<div id="addressContainer" dojoType="ingrid:TabContainer" class="full h600" selectedChild="addressSearch">

			<!-- TAB 1 START -->
			<div id="addressSearch" dojoType="ContentPane" class="blueTopBorder" label="<fmt:message key="dialog.searchAddress.directSearch" />">

				<div class="inputContainer field grey noSpaceBelow">
					<div>
						<span class="label"><label for="addressSearchUnit" onclick="javascript:dialog.showContextHelp(arguments[0], 7017, 'Einheit/Institution')"><fmt:message key="dialog.searchAddress.unitOrInstitution" /></label></span>
						<span class="input spaceBelow"><input type="text" id="addressSearchUnit" class="w640" dojoType="ingrid:ValidationTextBox" /></span>
					</div>
					<div class="half left">
						<span class="label"><label for="addressSearchLastname" onclick="javascript:dialog.showContextHelp(arguments[0], 7018, 'Nachname')"><fmt:message key="dialog.searchAddress.surName" /></label></span>
						<span class="input"><input type="text" id="addressSearchLastname" class="w308" dojoType="ingrid:ValidationTextBox" /></span>
					</div>
					<div class="half noSpaceBelow">
						<span class="label"><label for="addressSearchFirstname" onclick="javascript:dialog.showContextHelp(arguments[0], 7019, 'Vorname')"><fmt:message key="dialog.searchAddress.foreName" /></label></span>
						<span class="input"><input type="text" id="addressSearchFirstname" class="w308" dojoType="ingrid:ValidationTextBox" /></span>
					</div>
					<div class="fill"></div>
					<div class="spacerField"></div>
				</div>

				<div class="inputContainer w684">
					<span class="button w684" style="float:right; height:20px !important;">
						<span style="float:right;"><button dojoType="ingrid:Button" onClick="startNewSearch"><fmt:message key="dialog.searchAddress.search" /></button></span>
						<span id="thesLoadingZone" style="float:left; margin-top:1px; z-index: 100; visibility:hidden">
							<img id="thesImageZone" src="img/ladekreis.gif" />
						</span>
	        		</span>
  	  			</div>

				<div id="addressLinkSearchResults" class="inputContainer">
					<div class="noSpaceBelow">
						<span class="label"><fmt:message key="dialog.searchAddress.results" /></span>
						<div class="listInfo full">
							<span id="addressLinkSearchResultsInfo" class="searchResultsInfo">&nbsp;</span>
							<span id="addressLinkSearchResultsPaging" class="searchResultsPaging">&nbsp;</span>
							<div class="fill"></div>
						</div>
					</div>				

					<div class="tableContainer rows10 full">
						<table id="addressSearchResultsTable" multiple="false" dojoType="ingrid:FilteringTable" minRows="10" cellspacing="0" class="filteringTable relativePos nosort interactive readonly">
							<thead>
								<tr>
									<th nosort="true" field="icon" dataType="String" width="23"></th>
									<th nosort="true" field="title" dataType="String" width="652">Name</th>
								</tr>
							</thead>
							<colgroup>
								<col width="23">
								<col width="652">
							</colgroup>
							<tbody>
							</tbody>
						</table>
					</div>

					<div class="w684">
						<span class="button w684" style="height:20px !important; float:right;">
							<span style="float:right;"><button dojoType="ingrid:Button" onClick="addSelectedAddress"><fmt:message key="dialog.searchAddress.selectAddress" /></button></span>
						</span>
					</div>
				</div>	
			</div>
	        <!-- TAB 1 END -->
    		
    		<!-- TAB 2 START -->
			<div id="addressHierarchy" dojoType="ContentPane" class="blueTopBorder" label="<fmt:message key="dialog.searchAddress.treeSearch" />">

				<div class="inputContainer grey full h360 scrollable">
					<div dojoType="ContentPane" id="addressTreeContainer">
					<!-- tree components -->
						<div dojoType="ingrid:TreeController" widgetId="treeControllerAdr" RpcUrl="server/treelistener.php"></div>
						<div dojoType="ingrid:TreeListener" widgetId="treeListenerAdr"></div>	
						<div dojoType="ingrid:TreeDocIcons" widgetId="treeDocIconsAdr"></div>	
						<div dojoType="ingrid:TreeDecorator" listener="treeListenerAdr"></div>
              
						<!-- tree -->
						<div dojoType="ingrid:Tree" listeners="treeControllerAdr;treeListenerAdr;treeDocIconsAdr" widgetId="treeAdr"></div>
					</div>
				<div class="spacer"></div>
			</div>
            
			<div class="inputContainer w684">
				<span class="button w684" style="height:20px !important; float:right;">
					<span style="float:right;"><button dojoType="ingrid:Button" onClick="addSelectedAddressFromTree"><fmt:message key="dialog.searchAddress.selectAddress" /></button></span>
				</span>
			</div>
		</div>
        <!-- TAB 2 END -->

	</div>
	<!-- LEFT HAND SIDE CONTENT END -->
</div>

</body>
</html>
