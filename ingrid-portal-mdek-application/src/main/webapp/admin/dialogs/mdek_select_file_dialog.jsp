<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>

<script type="text/javascript">
var scriptScope = _container_;

scriptScope.selectFile = function() {
    this.hide();
	if (this.customParams.resultHandler) {
		var file = dwr.util.getValue("selectFileInputField");

		if (file && file.textLength != 0 && file.value != "") {
			this.customParams.resultHandler.resolve(file);
		}
	}
}

</script>
</head>

<body>

	<div class="">
        <span class="label left">
            <label for="selectFileInputField" onclick="require('ingrid/dialog').showContextHelp(arguments[0], 8060)"><fmt:message key="dialog.file.selectFile" /></label>
        </span>
		<div id="winNavi" style="top:0;">
			<a href="javascript:void(0);" onclick="javascript:window.open('mdek_help.jsp?lang='+userLocale+'&hkey=overall-catalog-management-4#overall-catalog-management-4', 'Hilfe', 'width=750,height=550,resizable=yes,scrollbars=yes,locationbar=no');" title="<fmt:message key="general.help" />">[?]</a>
		</div>
		<div class="content">
			<!-- LEFT HAND SIDE CONTENT START -->
			<div class="inputContainer field grey">
				
					<span>
						<input type="file" id="selectFileInputField" size="80" style="" />
					</span>
			</div>

			<div class="dijitDialogPaneActionBar">
		        <span><button data-dojo-type="dijit/form/Button" title="<fmt:message key="dialog.file.select" />" onclick="javascript:scriptScope.selectFile();"><fmt:message key="dialog.file.select" /></button></span>
			</div>
			<!-- LEFT HAND SIDE CONTENT END -->
		</div>
	</div>

</body>
</html>
