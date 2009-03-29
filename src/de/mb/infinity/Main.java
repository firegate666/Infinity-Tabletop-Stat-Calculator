package de.mb.infinity;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.mb.swing.JAdvancedTextArea;
import de.mb.swing.JDefaultFrame;

/**
 * main start for infinity
 * 
 * @author Marco
 * 
 */
public class Main {
	public static byte DICE_MISSED = 0;
	public static byte DICE_HIT = 1;
	public static byte DICE_CRITICAL = 2;

	byte EW_ANGREIFER = 10, EW_VERTEIDIGER = 10;
	double PUNKTE_ANGREIFER = 0;
	double PUNKTE_VERTEIDIGER = 0;
	double PUNKTE_SPIELLEITER = 0;
	double counter = 0;

	String matrix = "";
	
	protected DiceRoll calc_dice_state(byte ew, byte dv, byte num) {
		if (ew == dv)
			return new DiceRoll(dv, DICE_CRITICAL, num);
		else if (ew > dv)
			return new DiceRoll(dv, DICE_HIT, num);
		return new DiceRoll(dv, DICE_MISSED, num);
	}

	protected boolean isHit(byte dv) {
		return dv == DICE_HIT;
	}

	protected boolean isCrit(byte dv) {
		return dv == DICE_CRITICAL;
	}

	protected boolean isMiss(byte dv) {
		return dv == DICE_MISSED;
	}

	protected byte numdice = 1;
	
	public Main(byte ew_angreifer, byte ew_verteidiger, byte anzahl_wuerfel) {
		this.EW_ANGREIFER = ew_angreifer;
		this.EW_VERTEIDIGER = ew_verteidiger;
		this.numdice = anzahl_wuerfel;
	}
	
	protected void analyze(DiceRoll wuerfel_angreifer, DiceRoll wuerfel_verteidiger) {
		boolean count_points = false;
		
		wuerfel_verteidiger.tested++;
		if (wuerfel_angreifer.state == DICE_CRITICAL) {
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				if (EW_ANGREIFER == EW_VERTEIDIGER) {
					if (count_points) PUNKTE_SPIELLEITER++;
				} else if (EW_ANGREIFER > EW_VERTEIDIGER) {
					wuerfel_angreifer.success = true;
					if (count_points) PUNKTE_ANGREIFER++;
				} else {
					wuerfel_verteidiger.success_counter++;
					if (count_points) PUNKTE_VERTEIDIGER++;
				}
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				wuerfel_angreifer.success = true;
				if (count_points) PUNKTE_ANGREIFER++;
			} else { // MISS
				wuerfel_angreifer.success = true;
				if (count_points) PUNKTE_ANGREIFER++;
			}

		} else if (wuerfel_angreifer.state == DICE_HIT) {
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				wuerfel_verteidiger.success_counter++;
				if (count_points) PUNKTE_VERTEIDIGER++;
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				if (EW_ANGREIFER == EW_VERTEIDIGER) {
					if (wuerfel_angreifer.roll > wuerfel_verteidiger.roll) {
						wuerfel_angreifer.success = true;
						if (count_points) PUNKTE_ANGREIFER++;
					} else if (wuerfel_verteidiger.roll > wuerfel_angreifer.roll) {
						wuerfel_verteidiger.success_counter++;
						if (count_points) PUNKTE_VERTEIDIGER++;
					} else {
						if (count_points) PUNKTE_SPIELLEITER++;
					}
				} else if (EW_ANGREIFER > EW_VERTEIDIGER) {
					wuerfel_angreifer.success = true;
					if (count_points) PUNKTE_ANGREIFER++;
				} else {
					wuerfel_verteidiger.success_counter++;
					if (count_points) PUNKTE_VERTEIDIGER++;
				}
			} else { // MISS
				wuerfel_angreifer.success = true;
				if (count_points) PUNKTE_ANGREIFER++;
			}
		} else if (wuerfel_angreifer.state == DICE_MISSED) { // MISS
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				wuerfel_verteidiger.success_counter++;
				if (count_points) PUNKTE_VERTEIDIGER++;
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				wuerfel_verteidiger.success_counter++;
				if (count_points) PUNKTE_VERTEIDIGER++;
			} else { // MISS
				if (count_points) PUNKTE_SPIELLEITER++;
			}
		}
	}

	public double[] start() {

		long start = System.currentTimeMillis();

		byte[] dices = { 1, 1, 1, 1, 1 };
		for (int i = 0; i < numdice; i++) {
			dices[i] = 20;
		}

		DiceRoll state_1 = null;
		DiceRoll state_2 = null;
		DiceRoll state_3 = null;
		DiceRoll state_4 = null;
		DiceRoll state_5 = null;
		DiceRoll state_6 = null;
		
		for (byte dice_5 = 1; dice_5 <= dices[4]; dice_5++) {
			for (byte dice_4 = 1; dice_4 <= dices[3]; dice_4++) {
				for (byte dice_3 = 1; dice_3 <= dices[2]; dice_3++) {
					for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
						for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
							for (byte dice_6 = 1; dice_6 <= 20; dice_6++) {
								 state_1 = this.calc_dice_state( EW_ANGREIFER, dice_1, (byte) 1);
								 state_2 = this.calc_dice_state( EW_ANGREIFER, dice_2, (byte) 2);
								 state_3 = this.calc_dice_state( EW_ANGREIFER, dice_3, (byte) 3);
								 state_4 = this.calc_dice_state( EW_ANGREIFER, dice_4, (byte) 4);
								 state_5 = this.calc_dice_state( EW_ANGREIFER, dice_5, (byte) 5);
								 state_6 = this.calc_dice_state( EW_VERTEIDIGER, dice_6, (byte) 6);
								
								String matrix_t1 = "";
								String matrix_t2 = "";
								
								if (dices[0] > 1) {
									analyze(state_1, state_6);
									matrix_t1 += state_1.roll;
								}
								if (dices[1] > 1) {
									analyze(state_2, state_6);
									matrix_t1 += "\t"+state_2.roll;
								}
								if (dices[2] > 1) {
									analyze(state_3, state_6);
									matrix_t1 += "\t"+state_3.roll;
								}
								if (dices[3] > 1) {
									analyze(state_4, state_6);
									matrix_t1 += "\t"+state_4.roll;
								}
								if (dices[4] > 1) {
									analyze(state_5, state_6);
									matrix_t1 += "\t"+state_5.roll;
								}
								
								if (state_6.totalSuccess()) {
									PUNKTE_VERTEIDIGER++;
								} else if (state_1.isSuccess() || state_2.isSuccess() || state_3.isSuccess() || state_4.isSuccess() || state_5.isSuccess()) {
									PUNKTE_ANGREIFER++;
								} else {
									PUNKTE_SPIELLEITER++;
								}
								
								matrix += matrix_t1 + matrix_t2+"\n";
								
							}
						}
					}
				}
			}
		}

		counter = Math.pow(20, numdice+1);
		
		// OUTPUT
		long end = System.currentTimeMillis();
		//String result = "";
		/*result += "Laufzeit: " + (end - start) + " ms" + " ("
				+ counter + ")\n";
		result += "Sum up (A)" + PUNKTE_ANGREIFER + " %"
				+ ((PUNKTE_ANGREIFER / counter) * 100)+"\n";
		
		result += "Sum up (V)" + PUNKTE_VERTEIDIGER + " %"
				+ ((PUNKTE_VERTEIDIGER / counter) * 100)+"\n";

		result += "Sum up (S)" + PUNKTE_SPIELLEITER + " %"
				+ ((PUNKTE_SPIELLEITER / counter) * 100);*/
		
		double[] result = new double[2];
		result[0] = ((PUNKTE_ANGREIFER / counter) * 100);
		result[1] = ((PUNKTE_VERTEIDIGER / counter) * 100);
		
		return result;
	}

	public static void main(String[] args) {
		
		JDefaultFrame gui = new JDefaultFrame();
		gui.setLayout(new BorderLayout());
		gui.setBounds(50, 50, 640, 480);
		
		
		final JAdvancedTextArea result = new JAdvancedTextArea("");
		result.setEditable(false);
		
		final JAdvancedTextArea table = new JAdvancedTextArea("");
		table.setEditable(false);

		JPanel output = new JPanel(new GridLayout(2, 1));

		JPanel settings = new JPanel(new FlowLayout());
		
		final JSpinner ew_a_text = new JSpinner(new SpinnerNumberModel(10, 1, 20, 1));
		final JSpinner ew_v_text = new JSpinner(new SpinnerNumberModel(10, 1, 20, 1));
		final JSpinner num_dices_text = new JSpinner(new SpinnerNumberModel(2, 1, 5, 1));
		
		final JCheckBox with_matrix = new JCheckBox();

		
		JButton start = new JButton("Berechnung starten");
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					byte ew_a = new Byte(ew_a_text.getValue().toString()).byteValue();
					byte ew_v = new Byte(ew_v_text.getValue().toString()).byteValue();
					byte dices = new Byte(num_dices_text.getValue().toString()).byteValue();
					
					Main main = new Main(ew_a, ew_v, dices);
					double[] result_counter = main.start();
					if (result_counter[0] > 50.0 && result_counter[0] > result_counter[1]*1.5) {
						result.setBorder(BorderFactory.createLineBorder(Color.GREEN));
					} else {
						result.setBorder(BorderFactory.createLineBorder(Color.RED));
					}
					result.setText("Angreifer "+result_counter[0]+"%\nVerteidiger "+result_counter[1]+"%");
					if (with_matrix.isSelected())
						table.setText(main.matrix);
					else
						table.setText("");
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Es ist ein Fehler aufgetreten: "+ex.getMessage());
				}
			}
			
		});
		


		settings.add(new JLabel("EW Angreifer"));
		settings.add(ew_a_text);
		settings.add(new JLabel("EW Verteidiger"));
		settings.add(ew_v_text);
		settings.add(new JLabel("Anzahl Würfel Angreifer"));
		settings.add(num_dices_text);
		settings.add(new JLabel("Matrixausgabe?"));
		settings.add(with_matrix);
		
		gui.add(settings, BorderLayout.NORTH);
		
		output.add(result.getScrollPane());
		output.add(table.getScrollPane());
		
		gui.add(output, BorderLayout.CENTER);
		gui.add(start, BorderLayout.SOUTH);
		gui.setVisible(true);

	}
}

class DiceRoll {
	public byte dice = 0;
	public byte roll = 0;
	public byte state = 0;
	
	public boolean success = false;

	public boolean isSuccess() {
		return success;
	}
	
	public DiceRoll() {
		
	}
	
	public DiceRoll(byte roll, byte state, byte dice) {
		this.roll = roll;
		this.dice = dice;
		this.state = state;
	}
	public byte success_counter = 0;
	public byte tested = 0;
	
	public boolean totalSuccess() {
		System.out.println(success_counter);
		return success_counter > 0 && success_counter == tested;
	}
}