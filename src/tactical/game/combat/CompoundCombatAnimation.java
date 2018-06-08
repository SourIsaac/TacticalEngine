package tactical.game.combat;

import org.newdawn.slick.Graphics;

import tactical.game.sprite.CombatSprite;
import tactical.game.ui.PaddedGameContainer;
import tactical.utils.AnimationWrapper;

public class CompoundCombatAnimation extends CombatAnimation {

	private CombatAnimation secondAnimation;
	public CompoundCombatAnimation(AnimationWrapper animationWrapper, CombatSprite combatSprite, 
			CombatAnimation secondAnimation) {
		super(animationWrapper, combatSprite, animationWrapper.getAnimationLength());
		this.secondAnimation = secondAnimation;
	}
	@Override
	public boolean update(int delta) {
		boolean firstDone = super.update(delta);
		return secondAnimation.update(delta) && firstDone;
	}
	@Override
	public void render(PaddedGameContainer fcCont, Graphics g, int yDrawPos, float scale) {

		secondAnimation.render(fcCont, g, yDrawPos, scale);
		super.render(fcCont, g, yDrawPos, scale);
	}
	@Override
	public int getAnimationLength() {
		return Math.max(super.getAnimationLength(), secondAnimation.getAnimationLength());
	}
	@Override
	public void setMinimumTimePassed(int minimumTimePassed) {
		// TODO Auto-generated method stub
		super.setMinimumTimePassed(minimumTimePassed);
		secondAnimation.setMinimumTimePassed(minimumTimePassed);
	}
}
