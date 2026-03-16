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

<div style="margin-top:15px;">
	<a class="btn btn-warning" href="#/admin/${class.name?uncap_first}s/edit/{{ ${class.name?uncap_first}.id }}">Edit</a>
	<a class="btn btn-default" href="#/admin/${class.name?uncap_first}s">Back</a>
</div>

</div>
