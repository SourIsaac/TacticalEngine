package tactical.game.ai;

import java.awt.Point;

import org.newdawn.slick.util.Log;

import tactical.engine.state.StateInfo;
import tactical.game.Range;
import tactical.game.battle.command.BattleCommand;
import tactical.game.sprite.CombatSprite;
import tactical.game.turnaction.AttackSpriteAction;

public class WarriorAI extends AI
{
	public WarriorAI(int approachType, int vision) {
		super(approachType, false, vision);
	}

	@Override
	protected AIConfidence getConfidence(CombatSprite currentSprite, CombatSprite targetSprite,
			int tileWidth , int tileHeight, Point attackPoint, int distance, StateInfo stateInfo) {
		int damage = Math.max(1, currentSprite.getCurrentAttack() - targetSprite.getCurrentDefense());

		if (targetSprite.isHero() == currentSprite.isHero())
			return new AIConfidence(Integer.MIN_VALUE);

		// Check to make sure that if we're using a ranged weapon that has spots that it cannot target in the range that the enemy is not in one of those spaces
		Range attackRange = currentSprite.getAttackRange();
		Log.debug("Warrior attack range " + attackRange);
		if (!attackRange.isInDistance(distance))
			return new AIConfidence(Integer.MIN_VALUE);

		// Determine confidence, add 5 because the attacked sprite will probably always be in range
		int currentConfidence = 5;
		int nearbyAlly = getNearbySpriteAmount(stateInfo, currentSprite.isHero(), tileWidth, tileHeight, attackPoint, 2, currentSprite) * 5;
		int nearbyEnemy = getNearbySpriteAmount(stateInfo, !currentSprite.isHero(), tileWidth, tileHeight, attackPoint, 2, currentSprite) * 5;
		
		// Get the percent of damage that will be done to the hero
		int damageDone = Math.min(50, (int)(50.0 * damage / targetSprite.getMaxHP()));
		currentConfidence += nearbyAlly - nearbyEnemy + damageDone;
		
		boolean willKill = false;

		// If this attack would kill the target then add 50 confidence
		if (targetSprite.getCurrentHP() <= damage)
		{
			currentConfidence += 50;
			willKill = true;
		}

		// Warriors don't run!
		AIConfidence aiC = new AIConfidence(Math.max(1, currentConfidence));
		aiC.allyInfluence = nearbyAlly;
		aiC.enemyInfluence = nearbyEnemy;
		aiC.damageInfluence = damageDone;
		aiC.willKill = willKill;

		return aiC;
	}

	@Override
	protected int getMaxRange(CombatSprite currentSprite) {
		// Check for weapons that have a range, but areas in the range they cannot hit
		return currentSprite.getAttackRange().getMaxRange();
	}

	@Override
	public void initialize(CombatSprite puppet) {
		super.initialize(puppet);
	}

	@Override
	protected AttackSpriteAction getPerformedTurnAction(CombatSprite target) {
		return new AttackSpriteAction(target,
				new BattleCommand(BattleCommand.COMMAND_ATTACK));
	}

	@Override
	protected int getLandEffectWeight(int landEffect) {
		return landEffect / 3;
	}

}
