<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>

<portlet:defineObjects />
<liferay-portlet:resourceURL var="resourceURL" />

<%@include file="banner.jsp"%>

<style type="text/css">
	.minH {
		min-height:500px;
		height:auto !important;
		height:500px; 
	}
	
	.thumbnail {
		max-width: 75px;
		max-height: 75px;
	}
	
	.photo {
		max-width: 200px;
		max-height: 150px;
	}
</style>

<script type="text/javascript">

var usedcar = {
	timer : null,
	resourceURL	: '<%= resourceURL %>'
};

</script>

<div id="tabs">
	<ul>
		<li><a href="#store-tab">Store</a></li>
		<li><a href="#profile-tab">My Profile</a></li>
		<li><a href="#admin-tab">Admin</a></li>
		<li><a href="#help-tab">Help</a></li>
	</ul>
	<div id="store-tab" class="minH">
		<div id="tab-1-content">
			<div id="rows"></div>
		</div>
	</div>
	<div id="profile-tab" class="minH">
		<%@include file="profile.jspf"%>
	</div>
	<div id="admin-tab" class="minH">
		<%@include file="admin.jspf"%>
	</div>
	<div id="help-tab" class="minH">
		<%@include file="help.jspf"%>
	</div>
</div>

<script id="rowTemplate" type="text/html">
	<table align="center" width="90%" border="0">
		<tr>
			<td align="left" width="10%">
				<img class="thumbnail" src="data:image/png;base64,%imageBinData%" alt="Thumbnail" />
				<input type="button" value="Like" onClick='likeCar("%vin%")' />
			</td>
			<td width="10%">
				<table>
					<tr>
						<td>
							VIN <br />
							Make <br />
							Model <br />
							Style <br />
							Year <br />
							Mileage <br />
							Color <br />
							Price <br />
							Automatic <br />
						
						</td>
						<td>
							&nbsp;&nbsp;
						</td>
						<td>
							%vin% <br />
							%make% <br />
							%model% </br>
							%style% <br />
							%year% <br />
							%mileage% <br />
							%color% <br />
							%price% <br />
							%automatic%  <br />
						</td>
					</tr>
				</table>
			</td>
			<td align="right" width="80%">
				<table border="0">
					<tr>
						<td>
							<img class="photo" src="data:image/png;base64,%p0%" alt="Photo1" />
						</td>
						<td>
							<img class="photo" src="data:image/png;base64,%p1%" alt="Photo2" />
						</td>
						<td>
							<img class="photo" src="data:image/png;base64,%p2%" alt="Photo3" />
						</td>
					</tr>
					<tr>
						<td>
							<img class="photo" src="data:image/png;base64,%p3%" alt="Photo4" />
						</td>
						<td>
							<img class="photo" src="data:image/png;base64,%p4%" alt="Photo5" />
						</td>
						<td>
							<img class="photo" src="data:image/png;base64,%p5%" alt="Photo6" />
						</td>					
					</tr>
				</table>
			</td>
		</tr>
	</table>
</script>



