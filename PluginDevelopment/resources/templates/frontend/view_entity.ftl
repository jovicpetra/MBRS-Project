<h2>View ${class.name}</h2>

<div ng-init="getOne();">

<form class="form-horizontal">

	<#if persistentProperties??>
		<#list persistentProperties as property>
			<#if !property.isId>
			<div class="form-group">
				<label class="col-sm-3 control-label">
					<#if property.uiProperty?? && property.uiProperty.label??>${property.uiProperty.label}<#else>${property.columnName?cap_first}</#if>
				</label>
				<div class="col-sm-5">
					<input type="text" class="form-control"
						ng-model="${class.name?uncap_first}.${property.columnName}" disabled>
				</div>
			</div>
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
			<div class="form-group">
				<label class="col-sm-3 control-label">
					<#if property.uiProperty?? && property.uiProperty.label??>${property.uiProperty.label}<#else>${property.name?cap_first}</#if>
				</label>
				<div class="col-sm-5">
					<input type="text" class="form-control"
						ng-model="${class.name?uncap_first}.${property.name}.${dispProp}" disabled>
				</div>
			</div>
			</#if>
		</#list>
	</#if>

</form>

<#if class.name == "Client">

<div ng-init="loadClientDetails();" style="margin-top:30px;">

	<h3 style="font-weight:bold; margin-bottom:15px;">Appointments</h3>

	<div ng-if="!clientAppointments || clientAppointments.length === 0" class="empty-state" style="margin-top:20px;">
		<span class="dots">...</span>
		<p>No appointments yet.</p>
	</div>

	<div ng-if="clientAppointments.length > 0" style="margin-top:20px;">
		<hr class="salon-divider">
		<div ng-repeat="a in clientAppointments">
			<div class="review-row">
				<span class="review-service">{{ a.treatment.name }}</span>
				<span class="review-comment">{{ a.status }}</span>
				<span class="review-date"><em>{{ a.dateTime | date:'MMM, dd yyyy HH:mm' }}</em></span>
			</div>
			<hr class="salon-divider">
		</div>
	</div>

	<h3 style="font-weight:bold; margin-top:30px; margin-bottom:15px;">Reviews</h3>

	<div ng-if="!clientReviews || clientReviews.length === 0" class="empty-state" style="margin-top:20px;">
		<span class="dots">...</span>
		<p>No reviews yet.</p>
	</div>

	<div ng-if="clientReviews.length > 0" style="margin-top:20px;">
		<hr class="salon-divider">
		<div ng-repeat="r in clientReviews">
			<div class="review-row">
				<span class="review-star">&#9733;</span>
				<span class="review-rating">{{ r.rating }}</span>
				<span class="review-service">{{ r.treatment.name }}</span>
				<span class="review-comment">"{{ r.comment }}"</span>
				<span class="review-date"><em>{{ r.reviewDate | date:'mediumDate' }}</em></span>
			</div>
			<hr class="salon-divider">
		</div>
	</div>

</div>

<#else>
<#if referencedProperties??>
	<#list referencedProperties as property>
		<#if property.connectionType?string == "ONE_TO_MANY">
		<div class="row" style="margin-top:20px;">
			<h4>
				<#if property.uiProperty?? && property.uiProperty.label??>${property.uiProperty.label}<#else>${property.name?cap_first}</#if>
			</h4>
			<div class="col-md-6">
				<ul class="list-group">
					<li class="list-group-item"
						ng-repeat="item in ${class.name?uncap_first}.${property.name}">
						{{ item.id }}
					</li>
				</ul>
			</div>
		</div>
		</#if>
	</#list>
</#if>
</#if>

<div style="margin-top:15px;">
	<#if class.name != "Client"><a class="btn btn-warning" href="#/admin/${class.name?uncap_first}s/edit/{{ ${class.name?uncap_first}.id }}">Edit</a></#if>
	<a class="btn btn-default" href="#/admin/${class.name?uncap_first}s">Back</a>
</div>

</div>
