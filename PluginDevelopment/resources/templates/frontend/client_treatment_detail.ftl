<div ng-init="loadTreatmentDetail();">

<div ng-if="!selectedTreatment" class="empty-state" style="margin-top:30px;">
	<span class="dots">...</span>
	<p>Loading treatment...</p>
</div>

<div ng-if="selectedTreatment">
	<div style="margin-bottom: 30px;">
		<a href="#/treatments" style="color: #888; text-decoration: none;">&larr; Back to Treatments</a>
	</div>

	<div class="row">
		<div class="col-sm-7">
			<h2 style="margin-top: 0; font-weight: bold;">{{ selectedTreatment.name }}</h2>
			<p style="color: #555; font-size: 15px; margin-bottom: 20px;">{{ selectedTreatment.description }}</p>
			<p style="font-size: 20px; font-weight: bold; color: #333;">RSD {{ selectedTreatment.price }}</p>
			<a class="btn btn-salon" href="#/book/{{ selectedTreatment.id }}" style="margin-top: 10px;">Book Appointment</a>
		</div>
	</div>

	<hr style="margin: 40px 0 30px;">

	<h3 style="font-weight: bold; margin-bottom: 20px;">Reviews</h3>

	<div ng-if="treatmentReviews.length === 0" style="color: #aaa; text-align: center; padding: 30px 0;">
		<p>No reviews yet for this treatment.</p>
		<a class="btn btn-salon" href="#/leaveReview">Be the first to leave a review</a>
	</div>

	<div ng-if="treatmentReviews.length > 0">
		<div style="margin-bottom: 20px; color: #555;">
			<span style="font-size: 20px; color: #f0ad4e;">&#9733;</span>
			<strong style="font-size: 18px;">{{ averageRating }}</strong>
			<span> / 5</span>
			<span style="margin-left: 8px; color: #999;">({{ treatmentReviews.length }} review{{ treatmentReviews.length !== 1 ? 's' : '' }})</span>
		</div>
		<hr class="salon-divider">
		<div ng-repeat="review in treatmentReviews">
			<div class="review-row">
				<span class="review-star">{{ getStars(review.rating) }}</span>
				<span class="review-rating">{{ review.rating }}/5</span>
				<span class="review-comment">"{{ review.comment }}"</span>
				<span style="flex-shrink:0; font-style:italic; color:#666;">{{ review.client.firstName }} {{ review.client.lastName }}</span>
				<span class="review-date">{{ review.reviewDate | date:'mediumDate' }}</span>
			</div>
			<hr class="salon-divider">
		</div>
	</div>
</div>

</div>
