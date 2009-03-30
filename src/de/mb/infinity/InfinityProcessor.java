package de.mb.infinity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import de.mb.swing.JAdvancedTextArea;
import de.mb.swing.JDefaultFrame;


/**
 * main start for infinity
 * 
 * @author Marco
 * 
 */
public class InfinityProcessor {

	protected byte ew_attacker = 10, ew_defender = 10;
	protected double points_attacker = 0;
	protected double points_defender = 0;
	protected double points_deuce = 0;
	protected double counter = 0;

	protected boolean writeMatrix = false;

	protected byte numdice = 1;

	protected String outfile = "";

	public InfinityProcessor(byte ew_angreifer, byte ew_verteidiger,
			byte anzahl_wuerfel, boolean writeMatrix, String outfile) {
		this.ew_attacker = ew_angreifer;
		this.ew_defender = ew_verteidiger;
		this.numdice = anzahl_wuerfel;
		this.writeMatrix = writeMatrix;
		this.outfile = outfile;
	}

	/**
	 * analyze dice rolls
	 * 
	 * @param roll_attacker
	 * @param roll_defender
	 */
	protected void analyze(DiceRoll roll_attacker,
			DiceRoll roll_defender) {

		roll_defender.tested++;
		if (roll_attacker.state == DiceState.CRITICAL) {
			if (roll_defender.state == DiceState.CRITICAL) {
				if (ew_attacker == ew_defender) {

				} else if (ew_attacker > ew_defender) {
					roll_attacker.success = true;

				} else {
					roll_defender.success_counter++;

				}
			} else if (roll_defender.state == DiceState.HIT) {
				roll_attacker.success = true;

			} else { // MISS
				roll_attacker.success = true;

			}

		} else if (roll_attacker.state == DiceState.HIT) {
			if (roll_defender.state == DiceState.CRITICAL) {
				roll_defender.success_counter++;

			} else if (roll_defender.state == DiceState.HIT) {
				if (roll_attacker.roll > roll_defender.roll) {
					roll_attacker.success = true;

				} else if (roll_defender.roll > roll_attacker.roll) {
					roll_defender.success_counter++;

				} else {
					if (ew_attacker == ew_defender) {
					} else if (ew_attacker > ew_defender) {
						roll_attacker.success = true;

					} else {
						roll_defender.success_counter++;

					}

				}
			} else { // MISS
				roll_attacker.success = true;

			}
		} else if (roll_attacker.state == DiceState.MISS) { // MISS
			if (roll_defender.state == DiceState.CRITICAL) {
				roll_defender.success_counter++;

			} else if (roll_defender.state == DiceState.HIT) {
				roll_defender.success_counter++;

			} else { // MISS

			}
		}
	}

	protected void exec_5_dice(byte dice_6, byte[] dices) {
		for (byte dice_5 = 1; dice_5 <= dices[4]; dice_5++) {
			for (byte dice_4 = 1; dice_4 <= dices[3]; dice_4++) {
				for (byte dice_3 = 1; dice_3 <= dices[2]; dice_3++) {
					for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
						for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
							DiceRoll state_1 = DiceState.calc_dice_state(
									ew_attacker, dice_1, (byte) 1);
							DiceRoll state_2 = DiceState.calc_dice_state(
									ew_attacker, dice_2, (byte) 2);
							DiceRoll state_3 = DiceState.calc_dice_state(
									ew_attacker, dice_3, (byte) 3);
							DiceRoll state_4 = DiceState.calc_dice_state(
									ew_attacker, dice_4, (byte) 4);
							DiceRoll state_5 = DiceState.calc_dice_state(
									ew_attacker, dice_5, (byte) 5);
							DiceRoll state_6 = DiceState.calc_dice_state(
									ew_defender, dice_6, (byte) 6);

							analyze(state_1, state_6);
							analyze(state_2, state_6);
							analyze(state_3, state_6);
							analyze(state_4, state_6);
							analyze(state_5, state_6);

							if (state_6.totalSuccess()) {
								points_defender++;
							} else if (state_1.isSuccess()
									|| state_2.isSuccess()
									|| state_3.isSuccess()
									|| state_4.isSuccess()
									|| state_5.isSuccess()) {
								points_attacker++;
							} else {
								points_deuce++;
							}
							writeToFile(state_1.roll + ";" + state_2.roll + ";"
									+ state_3.roll + ";" + state_4.roll + ";"
									+ state_5.roll + ";" + state_6.roll + ";"
									+ DiceState.stateToName(state_1.state) + ";"
									+ DiceState.stateToName(state_2.state) + ";"
									+ DiceState.stateToName(state_3.state) + ";"
									+ DiceState.stateToName(state_4.state) + ";"
									+ DiceState.stateToName(state_5.state) + ";"
									+ DiceState.stateToName(state_6.state) + ";"
									+ ((state_1.isSuccess()) ? "1" : "0") + ";"
									+ ((state_6.totalSuccess()) ? "1" : "0")
									+ "\n");
						}
					}
				}
			}
		}
	}

	protected void exec_4_dice(byte dice_6, byte[] dices) {
		for (byte dice_4 = 1; dice_4 <= dices[3]; dice_4++) {
			for (byte dice_3 = 1; dice_3 <= dices[2]; dice_3++) {
				for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
					for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
						DiceRoll state_1 = DiceState.calc_dice_state(ew_attacker,
								dice_1, (byte) 1);
						DiceRoll state_2 = DiceState.calc_dice_state(ew_attacker,
								dice_2, (byte) 2);
						DiceRoll state_3 = DiceState.calc_dice_state(ew_attacker,
								dice_3, (byte) 3);
						DiceRoll state_4 = DiceState.calc_dice_state(ew_attacker,
								dice_4, (byte) 4);
						DiceRoll state_6 = DiceState.calc_dice_state(ew_defender,
								dice_6, (byte) 6);

						analyze(state_1, state_6);
						analyze(state_2, state_6);
						analyze(state_3, state_6);
						analyze(state_4, state_6);

						if (state_6.totalSuccess()) {
							points_defender++;
						} else if (state_1.isSuccess() || state_2.isSuccess()
								|| state_3.isSuccess() || state_4.isSuccess()) {
							points_attacker++;
						} else {
							points_deuce++;
						}
						writeToFile(state_1.roll + ";" + state_2.roll + ";"
								+ state_3.roll + ";" + state_4.roll + ";"
								+ state_6.roll + ";"
								+ DiceState.stateToName(state_1.state) + ";"
								+ DiceState.stateToName(state_2.state) + ";"
								+ DiceState.stateToName(state_3.state) + ";"
								+ DiceState.stateToName(state_4.state) + ";"
								+ DiceState.stateToName(state_6.state) + ";"
								+ ((state_1.isSuccess()) ? "1" : "0") + ";"
								+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
					}
				}
			}
		}
	}

	protected void exec_3_dice(byte dice_6, byte[] dices) {
		for (byte dice_3 = 1; dice_3 <= dices[2]; dice_3++) {
			for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
				for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
					DiceRoll state_1 = DiceState.calc_dice_state(ew_attacker,
							dice_1, (byte) 1);
					DiceRoll state_2 = DiceState.calc_dice_state(ew_attacker,
							dice_2, (byte) 2);
					DiceRoll state_3 = DiceState.calc_dice_state(ew_attacker,
							dice_3, (byte) 3);
					DiceRoll state_6 = DiceState.calc_dice_state(ew_defender,
							dice_6, (byte) 6);

					analyze(state_1, state_6);
					analyze(state_2, state_6);
					analyze(state_3, state_6);

					if (state_6.totalSuccess()) {
						points_defender++;
					} else if (state_1.isSuccess() || state_2.isSuccess()
							|| state_3.isSuccess()) {
						points_attacker++;
					} else {
						points_deuce++;
					}
					writeToFile(state_1.roll + ";" + state_2.roll + ";"
							+ state_3.roll + ";" + state_6.roll + ";"
							+ DiceState.stateToName(state_1.state) + ";"
							+ DiceState.stateToName(state_2.state) + ";"
							+ DiceState.stateToName(state_3.state) + ";"
							+ DiceState.stateToName(state_6.state) + ";"
							+ ((state_1.isSuccess()) ? "1" : "0") + ";"
							+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
				}
			}
		}
	}

	protected void exec_2_dice(byte dice_6, byte[] dices) {
		for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
			for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
				DiceRoll state_1 = DiceState.calc_dice_state(ew_attacker, dice_1,
						(byte) 1);
				DiceRoll state_2 = DiceState.calc_dice_state(ew_attacker, dice_2,
						(byte) 2);
				DiceRoll state_6 = DiceState.calc_dice_state(ew_defender, dice_6,
						(byte) 6);

				analyze(state_1, state_6);
				analyze(state_2, state_6);

				if (state_6.totalSuccess()) {
					points_defender++;
				} else if (state_1.isSuccess() || state_2.isSuccess()) {
					points_attacker++;
				} else {
					points_deuce++;
				}
				writeToFile(state_1.roll + ";" + state_2.roll + ";"
						+ state_6.roll + ";" + DiceState.stateToName(state_1.state) + ";"
						+ DiceState.stateToName(state_2.state) + ";"
						+ DiceState.stateToName(state_6.state) + ";"
						+ ((state_1.isSuccess()) ? "1" : "0") + ";"
						+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
			}
		}
	}

	protected void exec_1_dice(byte dice_6, byte[] dices) {
		for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
			DiceRoll state_1 = DiceState.calc_dice_state(ew_attacker, dice_1,
					(byte) 1);
			DiceRoll state_6 = DiceState.calc_dice_state(ew_defender, dice_6,
					(byte) 6);

			analyze(state_1, state_6);

			if (state_6.totalSuccess()) {
				points_defender++;
			} else if (state_1.isSuccess()) {
				points_attacker++;
			} else {
				points_deuce++;
			}

			writeToFile(state_1.roll + ";" + state_6.roll + ";"
					+ DiceState.stateToName(state_1.state) + ";"
					+ DiceState.stateToName(state_6.state) + ";"
					+ ((state_1.isSuccess()) ? "1" : "0") + ";"
					+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
		}
	}

	protected FileWriter fileWriter = null;
	protected boolean writeError = false;

	protected void writeToFile(String text) {
		if (!writeError && writeMatrix && !outfile.equalsIgnoreCase(""))
			try {
				if (fileWriter == null)
					fileWriter = new FileWriter(outfile);
				fileWriter.write(text);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Beim Schreiben der Datei ist ein Fehler aufgetreten: "
								+ e.getMessage());
				writeError = true;
			}
	}

	public double[] process() {

		long start = System.currentTimeMillis();

		byte[] dices = { 1, 1, 1, 1, 1 };
		for (int i = 0; i < numdice; i++) {
			dices[i] = 20;
		}

		writeToFile("Angreifer EW;" + ew_attacker + ";Verteidiger EW;"
				+ ew_defender + ";Wuerfel;" + numdice + "\n\n");

		String matrix = "W1";
		String mtemp = "W1-Status";
		for (int i = 2; i <= numdice; i++) {
			matrix += ";" + "W" + i;
			mtemp += ";" + "W" + i + "-Status";
		}
		matrix += ";" + "WV";
		matrix += ";" + mtemp + ";WV-Status";
		matrix += ";Erfolg(A);Erfolg(V)";
		writeToFile(matrix += "\n");

		for (byte dice_6 = 1; dice_6 <= 20; dice_6++) {
			switch (numdice) {
			case 1:
				this.exec_1_dice(dice_6, dices);
				break;
			case 2:
				this.exec_2_dice(dice_6, dices);
				break;
			case 3:
				this.exec_3_dice(dice_6, dices);
				break;
			case 4:
				this.exec_4_dice(dice_6, dices);
				break;
			case 5:
				this.exec_5_dice(dice_6, dices);
				break;
			}
		}

		counter = Math.pow(20, numdice + 1);

		// OUTPUT
		long end = System.currentTimeMillis();

		double[] result = new double[2];
		result[0] = ((points_attacker / counter) * 100);
		result[1] = ((points_defender / counter) * 100);

		if (fileWriter != null)
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return result;
	}

}