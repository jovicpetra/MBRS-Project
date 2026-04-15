<h2><#if class.uiClass??>${class.uiClass.label!class.name}<#else>${class.name}</#if></h2>

<#-- Scan persistent AND referenced properties for lookUp=true to build per-field search inputs -->
<#assign hasLookUp = false />
<#assign lookUpFields = [] />
<#if persistentProperties??>
	<#list persistentProperties as p>
		<#if !p.isId && p.uiProperty?? && (p.uiProperty.lookUp!false)>
			<#assign hasLookUp = true />
			<#assign lookUpFields = lookUpFields + [p] />
		</#if>
	</#list>
</#if>
<#if referencedProperties??>
	<#list referencedProperties as p>
		<#if p.uiProperty?? && (p.uiProperty.lookUp!false)>
			<#assign hasLookUp = true />
			<#assign lookUpFields = lookUpFields + [p] />
		</#if>
	</#list>
</#if>

<#-- UIClass action permissions — default true when no UIClass stereotype is applied -->
<#assign canCreate  = !(class.uiClass??) || (class.uiClass.create!true) />
<#assign canUpdate  = !(class.uiClass??) || (class.uiClass.update!true) />
<#assign canDelete  = !(class.uiClass??) || (class.uiClass.delete!true) />
<#assign canView    = !(class.uiClass??) || (class.uiClass.view!true)   />
<#assign showActions = canView || canUpdate || canDelete />

<div ng-init="getAll();">

<#if class.name == "Appointment">

<#if hasLookUp>
<div style="margin:15px 0;">
	<#list lookUpFields as p>
	<input class="form-control" type="text"
		style="max-width:300px; display:inline-block; margin-right:8px;"
		ng-model="tableSearch.${(p.columnName)!p.name}<#if p.connectionType??>.${p.uiProperty.presPropertyName}</#if>"
		placeholder="Search by <#if p.uiProperty.label??>${p.uiProperty.label}<#else>${((p.columnName)!p.name)?cap_first}</#if>...">
	</#list>
</div>
</#if>

<div ng-if="appointmentList.length === 0" class="empty-state" style="margin-top:30px;">
	<span class="dots">...</span>
	<p>No appointments yet.</p>
</div>

<table class="table" style="margin-top:15px;" ng-if="appointmentList.length > 0">
	<thead>
		<tr style="border-bottom: 1px solid #eee;">
			<th style="font-weight:500; color:#555; border:none;">Service Name</th>
			<th style="font-weight:500; color:#555; border:none;">Date and Time</th>
			<th style="font-weight:500; color:#555; border:none;">Client Name</th>
			<th style="font-weight:500; color:#555; border:none;">Status</th>
			<#if showActions><th style="border:none;"></th></#if>
		</tr>
	</thead>
	<tbody>
		<tr ng-repeat="appointment in appointmentList | filter:tableSearch" style="border-top: 1px solid #eee;">
			<td style="border:none; vertical-align:middle;">{{ appointment.treatment.name }}</td>
			<td style="border:none; vertical-align:middle;">{{ appointment.dateTime | date:'medium' }}</td>
			<td style="border:none; vertical-align:middle;">{{ appointment.client.firstName }} {{ appointment.client.lastName }}</td>
			<td style="border:none; vertical-align:middle;">{{ appointment.status }}</td>
			<#if showActions>
			<td style="border:none; vertical-align:middle; text-align:right;">
				<#if canUpdate>
				<button class="btn btn-salon btn-sm"
					ng-click="setStatus(appointment.id, 'CONFIRMED')"
					ng-disabled="appointment.status!='WAITING'">Confirm</button>
				<button class="btn btn-salon btn-sm" style="margin-left:4px;"
					ng-click="setStatus(appointment.id, 'CANCELED')"
					ng-disabled="appointment.status=='CANCELED' || appointment.status=='DONE'">Cancel</button>
				</#if>
			</td>
			</#if>
		</tr>
	</tbody>
</table>

<#elseif class.name == "Review">

<#if hasLookUp>
<div style="margin:15px 0;">
	<#list lookUpFields as p>
	<input class="form-control" type="text"
		style="max-width:300px; display:inline-block; margin-right:8px;"
		ng-model="tableSearch.${(p.columnName)!p.name}<#if p.connectionType??>.${p.uiProperty.presPropertyName}</#if>"
		placeholder="Search by <#if p.uiProperty.label??>${p.uiProperty.label}<#else>${((p.columnName)!p.name)?cap_first}</#if>...">
	</#list>
</div>
</#if>

<div ng-if="reviewList.length === 0" class="empty-state" style="margin-top:30px;">
	<span class="dots">...</span>
	<p>No reviews yet.</p>
</div>

<div ng-if="reviewList.length > 0" style="margin-top:20px;">
	<hr class="salon-divider">
	<div ng-repeat="review in reviewList | filter:tableSearch">
		<div class="review-row">
			<span class="review-star">&#9733;</span>
			<span class="review-rating">{{ review.rating }}</span>
			<span class="review-service">{{ review.treatment.name }}</span>
			<span class="review-comment">"{{ review.comment }}" <em>{{ review.client.firstName }} {{ review.client.lastName }}</em></span>
			<span class="review-date"><em>{{ review.reviewDate | date:'mediumDate' }}</em></span>
		</div>
		<hr class="salon-divider">
	</div>
</div>

<#elseif class.name == "Treatment">

<#if hasLookUp>
<div style="margin-bottom:15px;">
	<#list lookUpFields as p>
	<input class="form-control" type="text"
		style="max-width:300px; display:inline-block; margin-right:8px;"
		ng-model="tableSearch.${(p.columnName)!p.name}<#if p.connectionType??>.${p.uiProperty.presPropertyName}</#if>"
		placeholder="Search by <#if p.uiProperty.label??>${p.uiProperty.label}<#else>${((p.columnName)!p.name)?cap_first}</#if>...">
	</#list>
</div>
</#if>

<div ng-if="treatmentList.length === 0" class="empty-state" style="margin-top:30px;">
	<span class="dots">...</span>
	<p>No services available yet.</p>
</div>

<div class="row" style="margin-top:30px; display: flex; gap: 20px;" ng-if="treatmentList.length > 0">
	<div class="col-sm-4 service-card" ng-repeat="treatment in treatmentList | filter:tableSearch">
		<a href="#/treatments/{{ treatment.id }}" style="text-decoration: none; color: inherit; display: block;">
			<h4><strong>{{ treatment.name }}</strong></h4>
			<p>{{ treatment.description }}</p>
		</a>
		<div class="service-footer">
			<span>RSD {{ treatment.price }}</span>
			<a class="btn btn-salon" href="#/treatments/{{ treatment.id }}">Details</a>
		</div>
	</div>
</div>

<#else>

<#if canCreate && class.name != "Client">
<a class="btn btn-salon" href="#/admin/${class.name?uncap_first}s/add">Add new ${class.name}</a>
</#if>

<#if hasLookUp>
<div style="margin:15px 0;">
	<#list lookUpFields as p>
	<input class="form-control" type="text"
		style="max-width:300px; display:inline-block; margin-right:8px;"
		ng-model="tableSearch.${(p.columnName)!p.name}<#if p.connectionType??>.${p.uiProperty.presPropertyName}</#if>"
		placeholder="Search by <#if p.uiProperty.label??>${p.uiProperty.label}<#else>${((p.columnName)!p.name)?cap_first}</#if>...">
	</#list>
</div>
</#if>

<div ng-if="${class.name?uncap_first}List.length === 0" class="empty-state" style="margin-top:30px;">
	<span class="dots">...</span>
	<p>No ${class.name?lower_case}s yet.<#if canCreate> <a href="#/admin/${class.name?uncap_first}s/add">Add the first ${class.name?lower_case}</a></#if></p>
</div>

<table class="table table-hover" style="margin-top:15px;" ng-if="${class.name?uncap_first}List.length > 0">
	<thead>
		<tr>
		<#if persistentProperties??>
			<#list persistentProperties as property>
				<#if !property.isId>
					<th><#if property.uiProperty?? && property.uiProperty.label??>${property.uiProperty.label}<#else>${property.columnName?cap_first}</#if></th>
				</#if>
			</#list>
		</#if>
		<#if referencedProperties??>
			<#list referencedProperties as property>
				<#if property.connectionType?string == "MANY_TO_ONE">
					<th><#if property.uiProperty?? && property.uiProperty.label??>${property.uiProperty.label}<#else>${property.name?cap_first}</#if></th>
				</#if>
			</#list>
		</#if>
		<#if showActions>
			<th>Actions</th>
		</#if>
		</tr>
	</thead>
	<tbody>
		<tr ng-repeat="${class.name?uncap_first} in ${class.name?uncap_first}List | filter:tableSearch">
			<#if persistentProperties??>
				<#list persistentProperties as property>
					<#if !property.isId>
						<td>{{ ${class.name?uncap_first}.${property.columnName} }}</td>
					</#if>
				</#list>
			</#if>
			<#if referencedProperties??>
				<#list referencedProperties as property>
					<#if property.connectionType?string == "MANY_TO_ONE">
						<#assign dispProp = "name" />
						<#if property.uiProperty?? && property.uiProperty.presPropertyName??>
							<#assign dispProp = property.uiProperty.presPropertyName />
						</#if>
						<td>{{ ${class.name?uncap_first}.${property.name}.${dispProp} }}</td>
					</#if>
				</#list>
			</#if>
			<#if showActions>
			<td>
				<#if canView><a class="btn btn-salon btn-sm" href="#/admin/${class.name?uncap_first}s/{{ ${class.name?uncap_first}.id }}">view</a></#if>
				<#if canUpdate && class.name != "Client"><a class="btn btn-salon btn-sm" href="#/admin/${class.name?uncap_first}s/edit/{{ ${class.name?uncap_first}.id }}" style="margin-left:4px;">edit</a></#if>
				<#if canDelete && class.name != "Client"><button class="btn btn-salon btn-sm" ng-click="remove(${class.name?uncap_first}.id)" style="margin-left:4px;">delete</button></#if>
			</td>
			</#if>
		</tr>
	</tbody>
</table>

</#if>

<div ng-repeat="alert in alerts">
	<uib-alert type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</uib-alert>
</div>

</div>
