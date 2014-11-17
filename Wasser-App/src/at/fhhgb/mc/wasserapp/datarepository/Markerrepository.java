package at.fhhgb.mc.wasserapp.datarepository;

import java.util.ArrayList;
import at.fhhgb.mc.wasserapp.model.*;

/**
 * Repository for fountain, toilet and healing spring
 * 
 * @author mariobaumgartner
 *
 */
public class Markerrepository {
	ArrayList<Toilet> mToilets;
	ArrayList<Fountain> mFountains;
	ArrayList<HealingSpring> mHealingSprings;

	ArrayList<RatingToilet> mRatingToilets;
	ArrayList<RatingFountain> mRatingFountains;
	ArrayList<RatingHealingSpring> mRatingHealingSprings;

	public ArrayList<Toilet> getToilets() {
		return null;
	}

	public ArrayList<Toilet> getFountains() {
		return null;
	}

	public ArrayList<Toilet> getHealingSprings() {
		return null;
	}

	public void updateToilet(Toilet _toilet) {

	}

	public void updateFountain(Fountain _fountain) {

	}

	public void updateHealingSpring(HealingSpring _healingspring) {

	}

	public void createToilet(Toilet _toilet) {

	}

	public void createFountain(Fountain _fountain) {

	}

	public void createHealingSpring(HealingSpring _healingspring) {

	}
	public void deleteToilet(Toilet _toilet) {

	}

	public void deleteFountain(Fountain _fountain) {

	}

	public void deleteHealingSpring(HealingSpring _healingspring) {

	}

	/*
	 * get Ratings
	 */
	public ArrayList<RatingToilet> getToiletRatings(Toilet _toilet) {
		return null;
	}

	public ArrayList<RatingFountain> getFountainRatings(Fountain _fountain) {
		return null;
	}

	public ArrayList<RatingHealingSpring> getHealingSpringRatings(
			HealingSpring _healingspring) {
		return null;
	}
	
	public void createToiletRating(Toilet _toilet) {

	}

	public void createFountainRating(Fountain _fountain) {

	}

	public void createHealingSpringRating(HealingSpring _healingspring) {

	}
}
