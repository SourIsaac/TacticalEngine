package tactical.game.item;

import tactical.game.battle.spell.SpellDefinition;
import tactical.game.resource.SpellResource;
import tactical.loading.ResourceManager;

public class SpellItemUse {
	private transient SpellDefinition spell;
	private String spellId;
	private int level;
	private boolean singleUse;

	public SpellItemUse(String spellId, int level, boolean singleUse) {
		super();
		this.spellId = spellId;
		this.level = level;
		this.singleUse = singleUse;
	}

	public SpellDefinition getSpell() {
		return spell;
	}

	public String getSpellId() {
		return spellId;
	}

	public int getLevel() {
		return level;
	}

	public boolean isSingleUse() {
		return singleUse;
	}

	public void initialize(ResourceManager fcrm) {
		spell = SpellResource.getSpell(spellId);
	}
}
