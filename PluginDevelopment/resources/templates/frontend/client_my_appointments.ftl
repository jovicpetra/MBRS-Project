<h2 style="margin-bottom: 24px;">My Appointments</h2>

<div ng-init="">

	<!-- Email lookup -->
	<div style="max-width: 480px; margin-bottom: 24px;">
		<p style="color: #555; margin-bottom: 12px;">Enter your email address to view your appointments.</p>
		<div style="display: flex; gap: 8px;">
			<input type="email" class="form-control" placeholder="Your email"
				ng-model="emailInput"
				ng-change="getMyAppointments()"
				style="border-radius: 4px; border: 1px solid #ccc; padding: 10px;"
				ng-keydown="$event.key === 'Enter' && getMyAppointments()">
			<button class="btn btn-salon" ng-click="getMyAppointments()">Search</button>
		</div>
		<p ng-if="clientNotFound" style="color: #c0392b; margin-top: 8px; font-size: 14px;">
			No account found with that email.
		</p>
	</div>

	<!-- Results -->
	<div ng-if="myAppointmentList !== null">

		<div ng-if="myAppointmentList.length === 0 && !clientNotFound" class="empty-state" style="margin-top: 20px;">
			<span class="dots">...</span>
			<p>No appointments found for this email.</p>
		</div>

		<table class="table" style="margin-top: 8px;" ng-if="myAppointmentList.length > 0">
			<thead>
				<tr style="border-bottom: 1px solid #eee;">
					<th style="font-weight: 500; color: #555; border: none;">Service</th>
					<th style="font-weight: 500; color: #555; border: none;">Date &amp; Time</th>
					<th style="font-weight: 500; color: #555; border: none;">Status</th>
					<th style="border: none;"></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="a in myAppointmentList" style="border-top: 1px solid #eee;">
					<td style="border: none; vertical-align: middle;">{{ a.treatment.name }}</td>
					<td style="border: none; vertical-align: middle;">{{ a.dateTime | date:'medium' }}</td>
					<td style="border: none; vertical-align: middle;">{{ a.status }}</td>
					<td style="border: none; vertical-align: middle; text-align: right;">
						<button class="btn btn-salon btn-sm"
							ng-click="setStatus(a.id, 'CANCELED')"
							ng-disabled="a.status === 'CANCELED'">Cancel</button>
					</td>
				</tr>
			</tbody>
		</table>

	</div>

	<div ng-repeat="alert in alerts">
		<uib-alert type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</uib-alert>
	</div>

</div>
