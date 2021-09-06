package yeelp.scalingfeast.integration.module.solcarrot;

public class FoodEfficiencyMilestone extends Milestone<Float> {
	private float reward;

	public FoodEfficiencyMilestone(String milestone) throws NumberFormatException, IllegalArgumentException {
		super(milestone);
	}

	@Override
	void setReward(String str) {
		this.reward = Float.parseFloat(str);
		if(this.reward <= 0) {
			throw new IllegalArgumentException(this.reward + " isn't a valid food efficiency milestone reward amount!");
		}
	}

	@Override
	public Float getReward() {
		return this.reward;
	}
}
