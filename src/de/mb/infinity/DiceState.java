package de.mb.infinity;

public enum DiceState {
	CRITICAL, HIT, MISS;
	

	public static String stateToName(DiceState state) {
		if(state == DiceState.MISS)
			return "Misserfolg";
		else if(state == DiceState.HIT)
			return "Treffer";
		else if(state == DiceState.CRITICAL)
			return "kritischer Treffer";
		return "undef.";
	}
	
	public static DiceRoll calc_dice_state(byte ew, byte dv, byte num) {
		if (ew == dv)
			return new DiceRoll(dv, CRITICAL, num);
		else if (ew > dv)
			return new DiceRoll(dv, HIT, num);
		return new DiceRoll(dv, MISS, num);
	}
}
