package de.mb.infinity;

public class DiceRoll {
	public byte dice = 0;
	public byte roll = 0;
	public DiceState state = DiceState.MISS;

	public boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public DiceRoll(byte roll, DiceState state, byte dice) {
		this.roll = roll;
		this.dice = dice;
		this.state = state;
	}

	public byte success_counter = 0;
	public byte tested = 0;

	public boolean totalSuccess() {
		return success_counter > 0 && success_counter == tested;
	}
}