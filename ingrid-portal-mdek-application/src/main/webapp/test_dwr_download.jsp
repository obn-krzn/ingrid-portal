<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>

<script src='/ingrid-portal-mdek-application/dwr/interface/DownloadTest.js'></script>
<script src='/ingrid-portal-mdek-application/dwr/engine.js'></script>


<script type="text/javascript">
// Save as CSV values link 'onclick' function
saveAsCSV = function() {
	DownloadTest.downloadCSV(function(data) {
		window.open(data);
	});
}
</script>
</head>

<body>
	<a href="javascript:void(0);" onclick="javascript:saveAsCSV();" title="Download Test">Download Test</a>
</body>
</html>
