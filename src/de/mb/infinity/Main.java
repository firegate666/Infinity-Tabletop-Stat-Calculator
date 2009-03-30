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
public class Main {
	public static byte DICE_MISSED = 0;
	public static byte DICE_HIT = 1;
	public static byte DICE_CRITICAL = 2;

	protected String stateToName(byte state) {
		switch (state) {
		case 0:
			return "Misserfolg";
		case 1:
			return "Treffer";
		case 2:
			return "kritischer Treffer";
		}
		return "undef.";
	}

	byte EW_ANGREIFER = 10, EW_VERTEIDIGER = 10;
	double PUNKTE_ANGREIFER = 0;
	double PUNKTE_VERTEIDIGER = 0;
	double PUNKTE_SPIELLEITER = 0;
	double counter = 0;

	boolean writeMatrix = false;

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

	protected String outfile = "";

	public Main(byte ew_angreifer, byte ew_verteidiger, byte anzahl_wuerfel,
			boolean writeMatrix, String outfile) {
		this.EW_ANGREIFER = ew_angreifer;
		this.EW_VERTEIDIGER = ew_verteidiger;
		this.numdice = anzahl_wuerfel;
		this.writeMatrix = writeMatrix;
		this.outfile = outfile;
	}

	protected void analyze(DiceRoll wuerfel_angreifer,
			DiceRoll wuerfel_verteidiger) {
		boolean count_points = false;

		wuerfel_verteidiger.tested++;
		if (wuerfel_angreifer.state == DICE_CRITICAL) {
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				if (EW_ANGREIFER == EW_VERTEIDIGER) {
					if (count_points)
						PUNKTE_SPIELLEITER++;
				} else if (EW_ANGREIFER > EW_VERTEIDIGER) {
					wuerfel_angreifer.success = true;
					if (count_points)
						PUNKTE_ANGREIFER++;
				} else {
					wuerfel_verteidiger.success_counter++;
					if (count_points)
						PUNKTE_VERTEIDIGER++;
				}
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				wuerfel_angreifer.success = true;
				if (count_points)
					PUNKTE_ANGREIFER++;
			} else { // MISS
				wuerfel_angreifer.success = true;
				if (count_points)
					PUNKTE_ANGREIFER++;
			}

		} else if (wuerfel_angreifer.state == DICE_HIT) {
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				wuerfel_verteidiger.success_counter++;
				if (count_points)
					PUNKTE_VERTEIDIGER++;
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				if (wuerfel_angreifer.roll > wuerfel_verteidiger.roll) {
					wuerfel_angreifer.success = true;
					if (count_points)
						PUNKTE_ANGREIFER++;
				} else if (wuerfel_verteidiger.roll > wuerfel_angreifer.roll) {
					wuerfel_verteidiger.success_counter++;
					if (count_points)
						PUNKTE_VERTEIDIGER++;
				} else {
					if (EW_ANGREIFER == EW_VERTEIDIGER) {
					} else if (EW_ANGREIFER > EW_VERTEIDIGER) {
						wuerfel_angreifer.success = true;
						if (count_points)
							PUNKTE_ANGREIFER++;
					} else {
						wuerfel_verteidiger.success_counter++;
						if (count_points)
							PUNKTE_VERTEIDIGER++;
					}
					if (count_points)
						PUNKTE_SPIELLEITER++;
				}
			} else { // MISS
				wuerfel_angreifer.success = true;
				if (count_points)
					PUNKTE_ANGREIFER++;
			}
		} else if (wuerfel_angreifer.state == DICE_MISSED) { // MISS
			if (wuerfel_verteidiger.state == DICE_CRITICAL) {
				wuerfel_verteidiger.success_counter++;
				if (count_points)
					PUNKTE_VERTEIDIGER++;
			} else if (wuerfel_verteidiger.state == DICE_HIT) {
				wuerfel_verteidiger.success_counter++;
				if (count_points)
					PUNKTE_VERTEIDIGER++;
			} else { // MISS
				if (count_points)
					PUNKTE_SPIELLEITER++;
			}
		}
	}

	protected void exec_5_dice(byte dice_6, byte[] dices) {
		for (byte dice_5 = 1; dice_5 <= dices[4]; dice_5++) {
			for (byte dice_4 = 1; dice_4 <= dices[3]; dice_4++) {
				for (byte dice_3 = 1; dice_3 <= dices[2]; dice_3++) {
					for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
						for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
							DiceRoll state_1 = this.calc_dice_state(
									EW_ANGREIFER, dice_1, (byte) 1);
							DiceRoll state_2 = this.calc_dice_state(
									EW_ANGREIFER, dice_2, (byte) 2);
							DiceRoll state_3 = this.calc_dice_state(
									EW_ANGREIFER, dice_3, (byte) 3);
							DiceRoll state_4 = this.calc_dice_state(
									EW_ANGREIFER, dice_4, (byte) 4);
							DiceRoll state_5 = this.calc_dice_state(
									EW_ANGREIFER, dice_5, (byte) 5);
							DiceRoll state_6 = this.calc_dice_state(
									EW_VERTEIDIGER, dice_6, (byte) 6);

							analyze(state_1, state_6);
							analyze(state_2, state_6);
							analyze(state_3, state_6);
							analyze(state_4, state_6);
							analyze(state_5, state_6);

							if (state_6.totalSuccess()) {
								PUNKTE_VERTEIDIGER++;
							} else if (state_1.isSuccess()
									|| state_2.isSuccess()
									|| state_3.isSuccess()
									|| state_4.isSuccess()
									|| state_5.isSuccess()) {
								PUNKTE_ANGREIFER++;
							} else {
								PUNKTE_SPIELLEITER++;
							}
							writeToFile(state_1.roll + ";" + state_2.roll + ";"
									+ state_3.roll + ";" + state_4.roll + ";"
									+ state_5.roll + ";" + state_6.roll + ";"
									+ stateToName(state_1.state) + ";"
									+ stateToName(state_2.state) + ";"
									+ stateToName(state_3.state) + ";"
									+ stateToName(state_4.state) + ";"
									+ stateToName(state_5.state) + ";"
									+ stateToName(state_6.state) + ";"
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
						DiceRoll state_1 = this.calc_dice_state(EW_ANGREIFER,
								dice_1, (byte) 1);
						DiceRoll state_2 = this.calc_dice_state(EW_ANGREIFER,
								dice_2, (byte) 2);
						DiceRoll state_3 = this.calc_dice_state(EW_ANGREIFER,
								dice_3, (byte) 3);
						DiceRoll state_4 = this.calc_dice_state(EW_ANGREIFER,
								dice_4, (byte) 4);
						DiceRoll state_6 = this.calc_dice_state(EW_VERTEIDIGER,
								dice_6, (byte) 6);

						analyze(state_1, state_6);
						analyze(state_2, state_6);
						analyze(state_3, state_6);
						analyze(state_4, state_6);

						if (state_6.totalSuccess()) {
							PUNKTE_VERTEIDIGER++;
						} else if (state_1.isSuccess() || state_2.isSuccess()
								|| state_3.isSuccess() || state_4.isSuccess()) {
							PUNKTE_ANGREIFER++;
						} else {
							PUNKTE_SPIELLEITER++;
						}
						writeToFile(state_1.roll + ";" + state_2.roll + ";"
								+ state_3.roll + ";" + state_4.roll + ";"
								+ state_6.roll + ";"
								+ stateToName(state_1.state) + ";"
								+ stateToName(state_2.state) + ";"
								+ stateToName(state_3.state) + ";"
								+ stateToName(state_4.state) + ";"
								+ stateToName(state_6.state) + ";"
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
					DiceRoll state_1 = this.calc_dice_state(EW_ANGREIFER,
							dice_1, (byte) 1);
					DiceRoll state_2 = this.calc_dice_state(EW_ANGREIFER,
							dice_2, (byte) 2);
					DiceRoll state_3 = this.calc_dice_state(EW_ANGREIFER,
							dice_3, (byte) 3);
					DiceRoll state_6 = this.calc_dice_state(EW_VERTEIDIGER,
							dice_6, (byte) 6);

					analyze(state_1, state_6);
					analyze(state_2, state_6);
					analyze(state_3, state_6);

					if (state_6.totalSuccess()) {
						PUNKTE_VERTEIDIGER++;
					} else if (state_1.isSuccess() || state_2.isSuccess()
							|| state_3.isSuccess()) {
						PUNKTE_ANGREIFER++;
					} else {
						PUNKTE_SPIELLEITER++;
					}
					writeToFile(state_1.roll + ";" + state_2.roll + ";"
							+ state_3.roll + ";" + state_6.roll + ";"
							+ stateToName(state_1.state) + ";"
							+ stateToName(state_2.state) + ";"
							+ stateToName(state_3.state) + ";"
							+ stateToName(state_6.state) + ";"
							+ ((state_1.isSuccess()) ? "1" : "0") + ";"
							+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
				}
			}
		}
	}

	protected void exec_2_dice(byte dice_6, byte[] dices) {
		for (byte dice_2 = 1; dice_2 <= dices[1]; dice_2++) {
			for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
				DiceRoll state_1 = this.calc_dice_state(EW_ANGREIFER, dice_1,
						(byte) 1);
				DiceRoll state_2 = this.calc_dice_state(EW_ANGREIFER, dice_2,
						(byte) 2);
				DiceRoll state_6 = this.calc_dice_state(EW_VERTEIDIGER, dice_6,
						(byte) 6);

				analyze(state_1, state_6);
				analyze(state_2, state_6);

				if (state_6.totalSuccess()) {
					PUNKTE_VERTEIDIGER++;
				} else if (state_1.isSuccess() || state_2.isSuccess()) {
					PUNKTE_ANGREIFER++;
				} else {
					PUNKTE_SPIELLEITER++;
				}
				writeToFile(state_1.roll + ";" + state_2.roll + ";"
						+ state_6.roll + ";" + stateToName(state_1.state) + ";"
						+ stateToName(state_2.state) + ";"
						+ stateToName(state_6.state) + ";"
						+ ((state_1.isSuccess()) ? "1" : "0") + ";"
						+ ((state_6.totalSuccess()) ? "1" : "0") + "\n");
			}
		}
	}

	protected void exec_1_dice(byte dice_6, byte[] dices) {
		for (byte dice_1 = 1; dice_1 <= dices[0]; dice_1++) {
			DiceRoll state_1 = this.calc_dice_state(EW_ANGREIFER, dice_1,
					(byte) 1);
			DiceRoll state_6 = this.calc_dice_state(EW_VERTEIDIGER, dice_6,
					(byte) 6);

			analyze(state_1, state_6);

			if (state_6.totalSuccess()) {
				PUNKTE_VERTEIDIGER++;
			} else if (state_1.isSuccess()) {
				PUNKTE_ANGREIFER++;
			} else {
				PUNKTE_SPIELLEITER++;
			}

			writeToFile(state_1.roll + ";" + state_6.roll + ";"
					+ stateToName(state_1.state) + ";"
					+ stateToName(state_6.state) + ";"
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

	public double[] start() {

		long start = System.currentTimeMillis();

		byte[] dices = { 1, 1, 1, 1, 1 };
		for (int i = 0; i < numdice; i++) {
			dices[i] = 20;
		}

		writeToFile("Angreifer EW;" + EW_ANGREIFER + ";Verteidiger EW;"
				+ EW_VERTEIDIGER + ";Wuerfel;" + numdice + "\n\n");

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
		result[0] = ((PUNKTE_ANGREIFER / counter) * 100);
		result[1] = ((PUNKTE_VERTEIDIGER / counter) * 100);

		if (fileWriter != null)
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return result;
	}

	public static void main(String[] args) {

		final JDefaultFrame gui = new JDefaultFrame();
		gui.setIconImage(Toolkit.getDefaultToolkit().createImage(gui.getClass().getResource("/resource/images/ilogo.png")));
		gui.setTitle("Infinity Berechner");
		gui.setLayout(new BorderLayout());
		gui.setBounds(50, 50, 640, 480);

		final ClosableTabbedPane tab = new ClosableTabbedPane();
		JScrollPane scrolltab = new JScrollPane(tab);
		
		/*final JAdvancedTextArea result = new JAdvancedTextArea("");
		result.setEditable(false);*/

		/*
		 * final JAdvancedTextArea table = new JAdvancedTextArea("");
		 * table.setEditable(false);
		 */

		JPanel output = new JPanel(new GridLayout(1, 1));

		JPanel settings = new JPanel(new FlowLayout());

		final JSpinner ew_a_text = new JSpinner(new SpinnerNumberModel(10, 1,
				20, 1));
		final JSpinner ew_v_text = new JSpinner(new SpinnerNumberModel(10, 1,
				20, 1));
		final JSpinner num_dices_text = new JSpinner(new SpinnerNumberModel(2,
				1, 5, 1));
		
		final JProgressBar progress = new JProgressBar(0, 100);
		progress.setOrientation(JProgressBar.VERTICAL);
		progress.setBorder(BorderFactory.createLineBorder(Color.gray));
		progress.setForeground(Color.blue);
		progress.setValue(0);

		final JCheckBox with_matrix = new JCheckBox();

		final JButton start = new JButton("Berechnung starten");
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					
					byte ew_a = new Byte(ew_a_text.getValue().toString())
							.byteValue();
					byte ew_v = new Byte(ew_v_text.getValue().toString())
							.byteValue();
					byte dices = new Byte(num_dices_text.getValue().toString())
							.byteValue();
					if (dices > 3) {
						int ret = JOptionPane
						.showConfirmDialog(
								gui,
								"Die Berechnung mit mehr als 3 Würfeln dauert länger. Wirklich fortfahren?",
								"Achtung!",
								JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.NO_OPTION)
							return;
					}
					JAdvancedTextArea result = new JAdvancedTextArea("");
					result.setEditable(false);
					result.setVisible(false);
					String outfile = "";
					start.setText("Berechnung läuft");
					result.setText("Berechnung läuft");
					if (with_matrix.isSelected()) {
						FileDialog fd = new FileDialog(gui);
						fd.setAlwaysOnTop(true);
						fd.setMode(FileDialog.SAVE);
						fd.setFile("output.csv");
						fd.setVisible(true);
						outfile = fd.getDirectory() + fd.getFile();
					}

					Main main = new Main(ew_a, ew_v, dices, with_matrix
							.isSelected(), outfile);
					start.setText("Berechnung läuft");
					gui.repaint();
					double[] result_counter = main.start();
					Thread.sleep(100);
					start.setText("Berechnung starten");
					if (result_counter[0] > 50.0
							&& result_counter[0] > result_counter[1] * 1.5) {
						result.setBorder(BorderFactory
								.createLineBorder(Color.GREEN));
					} else {
						result.setBorder(BorderFactory
								.createLineBorder(Color.RED));
					}
					result.setText("Angreifer " + result_counter[0]
							+ "%\nVerteidiger " + result_counter[1] + "%");

					if (with_matrix.isSelected()) {
						result.setText(result.getText()
								+ "\n\nDas Ergebnis der Analyse wurde nach "
								+ outfile + " geschrieben.");
					}
					tab.add(result, ew_a+"("+dices+")/"+ew_v);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(null,
									"Es ist ein Fehler aufgetreten: "
											+ ex.getMessage());
				}
				//result.setVisible(true);
			}

		});

		final JButton iterStart = new JButton("iterative Berechnung starten");
		iterStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				byte ew_a = new Byte(ew_a_text.getValue().toString())
						.byteValue();
				byte ew_v = new Byte(ew_v_text.getValue().toString())
						.byteValue();
				byte dices = new Byte(num_dices_text.getValue().toString())
						.byteValue();
				
				if (dices > 3) {
					int ret = JOptionPane
					.showConfirmDialog(
							gui,
							"Die Berechnung mit mehr als 3 Würfeln dauert länger. Wirklich fortfahren?",
							"Achtung!",
							JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.NO_OPTION)
						return;
				}
				
				int ret = JOptionPane
						.showConfirmDialog(
								gui,
								"Die iterative Berechnung startet jeweils bei EW=0 bis zum gewählten Limit ("
										+ ew_a
										+ "/"
										+ ew_v
										+ ")\nund wählt die Würfel von Anzahl=1 bis zum gewählten Limit ("
										+ dices
										+ ").\n\nBerechnung jetzt starten?",
								"Iterative Berechnung",
								JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					final StringBuffer outfile_name = new StringBuffer();
					final StringBuffer outfile_path  = new StringBuffer();
					start.setText("Berechnung läuft");
					final JAdvancedTextArea result = new JAdvancedTextArea("");
					result.setEditable(false);
					result.setText("");
					if (with_matrix.isSelected()) {
						FileDialog fd = new FileDialog(gui);
						fd.setAlwaysOnTop(true);
						fd.setMode(FileDialog.SAVE);
						fd.setFile("output.csv");
						fd.setVisible(true);
						
						outfile_path.append(fd.getDirectory());
						outfile_name.append(fd.getFile());
						if (fd.getDirectory() == null || fd.getFile() == null) {
							result.setText("Da keine Datei ausgewählt wurde, wurde die Matrixausgabe abgeschaltet.\n\n");
							with_matrix.setSelected(false);
						}
					}

					final byte max_ew_a = ew_a;
					final byte max_ew_v = ew_v;
					final byte max_dices = dices;

					result
							.setText(result.getText()+"Berechne Angreifer EW bis " + ew_a
									+ " und " + dices
									+ " Würfel und Verteidiger EW bis " + ew_v
									+ "\n\n");
					Thread t = new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							
					progress.setMaximum(max_ew_a*max_ew_v*max_dices);
					for (byte ew_a = 1; ew_a <= max_ew_a; ew_a++) {
						for (byte ew_v = 1; ew_v <= max_ew_v; ew_v++) {
							for (byte dices = 1; dices <= max_dices; dices++) {
								progress.setValue(progress.getValue()+1);
								String outfile = outfile_path.toString() + ew_a + "_"
										+ ew_v + "_" + dices + "_"
										+ outfile_name.toString();
								Main main = new Main(ew_a, ew_v, dices,
										with_matrix.isSelected(), outfile);
								start.setText("Berechnung läuft");
								double[] result_counter = main.start();
								start.setText("Berechnung starten");

								result.setText(result.getText() + "\nAngreifer\t"
										+ ew_a + "\t" + result_counter[0]
										+ "% (" + dices
										+ " Würfel)\nVerteidiger\t" + ew_v
										+ "\t" + result_counter[1] + "%\n");

								if (with_matrix.isSelected()) {

									result
											.setText(result.getText()
													+ "\nDas Ergebnis der Analyse wurde nach "
													+ outfile
													+ " geschrieben.\n");
								}
							}
						}
					}
					
						progress.setValue(0);
						tab.add(result, ">"+max_ew_a+"(>"+max_dices+")/>"+max_ew_v);
					}
						
					});
					t.start();
				}

			}

		});
		// iterStart.setEnabled(false);

		JPanel top = new JPanel(new GridLayout(2, 1));
		
		settings.add(new JLabel("EW Angreifer"));
		settings.add(ew_a_text);
		settings.add(new JLabel("EW Verteidiger"));
		settings.add(ew_v_text);
		settings.add(new JLabel("Anzahl Würfel Angreifer"));
		settings.add(num_dices_text);
		settings.add(new JLabel("Matrixausgabe?"));
		settings.add(with_matrix);
		
		ImageIcon logo = new ImageIcon(Toolkit.getDefaultToolkit().createImage(gui.getClass().getResource("/resource/images/fulllogo.png")));
		
		top.add(new JLabel(logo));
		top.add(settings);
		
		gui.add(top, BorderLayout.NORTH);

		//output.add(result.getScrollPane());
		//tab.add(result.getScrollPane());
		output.add(tab);
		// output.add(table.getScrollPane());

		gui.add(output, BorderLayout.CENTER);
		gui.add(progress, BorderLayout.EAST);

		JPanel bottom = new JPanel(new GridLayout(1, 2));
		bottom.add(start);
		bottom.add(iterStart);

		gui.add(bottom, BorderLayout.SOUTH);
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
		return success_counter > 0 && success_counter == tested;
	}
}