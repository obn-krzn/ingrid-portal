function ingrid_openWindow(url, winWidth, winHeight)
{
  popupWin = window.open(url, "InternalWin", 'width=' + winWidth + ',height=' + winHeight + ',resizable=yes,scrollbars=yes,location=no,toolbar=yes');
  popupWin.focus();
}

function ingrid_checkAll(group) {
  // NOTICE: first field in group has to be "checkAll" field
  if (group[0]) {
      if (group[0].checked == true) {
          for (i=1; i<group.length; i++) {
              group[i].checked = true;
          }
      }	else {
          for (i=1; i<group.length; i++) {
              group[i].checked = false;
          }
      }
  }
}

// Select all or nothing in group1 and force group2 to same selection state.
function ingrid_checkAll2Groups(group1, group2) {
    group2[0].checked = group1[0].checked;
    ingrid_checkAll(group1);
    ingrid_checkAll(group2);
}

//Select all or nothing in group1 and adapt only "all field" in group2.
function ingrid_checkAllAdapt(group1, group2) {
    ingrid_checkAll(group1);
    if (group1[0].checked == false) {
        group2[0].checked = false;
    } else {
        ingrid_checkGroup(group2);
    }
}

function ingrid_checkGroup(group) {
    // NOTICE: first field in group has to be "checkAll" field
    var allChecked = true;
    for (i=1; i<group.length; i++) {
        if (group[i].checked != true) {
            allChecked = false;
            break;
        }
    }
    if (allChecked) {
        group[0].checked = true;
    } else {
        group[0].checked = false;       
    }
}

//Check for selection of all field in group1 and then adapt group 2.
function ingrid_checkGroupAdapt(group1, group2) {
    ingrid_checkGroup(group1);
    if (group1[0].checked == false) {
        group2[0].checked = false;
    } else {
        ingrid_checkGroup(group2);
    }
}

function login() {
	if (document.getElementById("login").value == "admin") {
    	document.location.href="mpu_admin.html";
    } else if (document.getElementById("passwd").value != "") {
      	document.location.href="mpu_home.html";
    }
}

function clearUser() {
    document.getElementById("login").value = "";
}

function clearPasswd() {
    document.getElementById("passwd").value = "";
}


function adaptProviderNodes(partnerElementName, formName) {
  var partnerIdent = document.forms[formName].elements[partnerElementName].id;
  if (partnerIdent == "bund") {
    partnerIdent = "bu";
  }
  var checked = document.forms[formName].elements[partnerElementName].checked;
  for (i=0; i<document.forms[formName].elements.length; i++) {
    if (document.forms[formName].elements[i].id.indexOf(partnerIdent+'_') == 0) {
      document.forms[formName].elements[i].checked = checked;
    }
  }
}

function adaptPartnerNode(providerElementName, formName) {
  var partnerIdent = document.forms[formName].elements[providerElementName].id;
  partnerIdent = partnerIdent.substring(0, partnerIdent.indexOf('_'));
  var checked = document.forms[formName].elements[providerElementName].checked;
  var checkPartner = false;
  if (!checked) {
	  for (i=0; i<document.forms[formName].elements.length; i++) {
	    if (document.forms[formName].elements[i].id.indexOf(partnerIdent+'_') == 0) {
	      if (document.forms[formName].elements[i].checked) {
	      	checkPartner = true;
	      	break;
	      }
	    }
	  }
  } else {
      checkPartner = true;
  }
  if (partnerIdent == "bu") {
    partnerIdent = "bund";
  }
  document.forms[formName].elements["chk_"+partnerIdent].checked = checkPartner;
}

/* Open and close categories */
function openNode(element){
	var status = document.getElementById(element).style.display;
	var status_img = document.getElementById(element + "_img").src;
	
	document.getElementById(element).style.display = 'none';
	if(status == "none"){
		document.getElementById(element).style.display = "block";
	}
	
	document.getElementById(element + "_img").src = '/ingrid-portal-apps/images/facete/facete_cat_close.png';
	if(status_img.indexOf('/ingrid-portal-apps/images/facete/facete_cat_close.png') != -1){
		document.getElementById(element + "_img").src = '/ingrid-portal-apps/images/facete/facete_cat_open.png';
	}	
	
}

/* Open and close facete */
function openFaceteNode(element, id_facete, id_hits, width_min, width_max){
	var status = document.getElementById(element).style.display;
	var status_img = document.getElementById(element + "_img").src;
	document.getElementById(element).style.display = 'none';
	document.getElementById(id_facete).style.width = '3%';
	document.getElementById(id_facete).style.margin = '0 0 0 -35px';
	document.getElementById(id_hits).style.width = width_max;
	document.getElementById(element + "_img").src = '/ingrid-portal-apps/images/facete/facete_close.png'
	if(status == "none"){
		document.getElementById(element).style.display = "block";
		document.getElementById(id_facete).style.width = '208px';
		document.getElementById(id_facete).style.margin = '0';
		document.getElementById(id_hits).style.width = width_min;
		}
	if(status_img.indexOf('/ingrid-portal-apps/images/facete_close.png') != -1){
		document.getElementById(element + "_img").src = '/ingrid-portal-apps/images/facete/facete_open.png';
		}
}

/* select all checkboxes in form */
function faceteDialogSelectAll(field){
	for (i = 0; i < field.length; i++)
		field[i].checked = true ;
}

/* deselect all checkboxes in form */
function faceteDialogDeselectAll(field){
	for (i = 0; i < field.length; i++)
		field[i].checked = false ;
}

/* cancel dialog */
function faceteDialogCancel(id){
	var dialog = dijit.byId(id);
	dialog.hide();
}

/* open dialog by onclick-event */
function prepareDialog (id, button) {
   var dialog = dijit.byId(id);
   dojo.connect(dijit.byId(button), "onClick", dialog, "show");
}

/* open dialog for map */
function prepareDialogMap (id, wms, divId, iframeId) {
		var dialog = dijit.byId(id);
		var map = document.getElementById(divId);
		if (map.childNodes.length <= 1) {
			var iframe = document.createElement('iframe');
			iframe.setAttribute('id', iframeId);
			iframe.setAttribute('class', 'facete_map');
			iframe.setAttribute('name', 'wms_viewer');
			iframe.setAttribute('marginheight', '0');
			iframe.setAttribute('marginwidth', '0');
			iframe.setAttribute('frameborder', '0');
			iframe.setAttribute('src', wms);
			map.appendChild(iframe);
		}
		dialog.show();
}

/* open dialog */
function loadDialog(id){
	var dialog = dijit.byId(id);
	dialog.show();
}

function loadingProgressDialog(element){
	var status = document.getElementById(element).style.display;
	console.debug(status);
	if(status == "inline"){
		document.getElementById(element).style.display = "none";
	}
	if(status == "none"){
		document.getElementById(element).style.display = "inline";}
	}
