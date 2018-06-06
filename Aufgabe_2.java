
/**
 * @author Joscha Hasselbeck
 * 
 *		DBA1 nach Algorithmus aus der Vorlesung:		
 *		F = 2 => m = 7
 *
 *		Fragen:
 *
 *		=> ICB1 + ICB2 Fehlerfrei
 *		=> Wenn nicht alle Knoten den gleichen Wert besitzen ist ICB2 immer falsch?
 *
 *		=> dynamische Knotenanzahl
 *
 *		=> Funktionalität richtig?
 *
 */

import SoFTlib.*;
import static SoFTlib.Helper.*;

import java.util.ArrayList;

class DBA1_Knoten extends Node {

	int InputPosition(char Knoten) {
		if (Knoten == 'A')
			return 1;
		if (Knoten == 'B')
			return 2;
		if (Knoten == 'C')
			return 3;
		if (Knoten == 'D')
			return 4;
		if (Knoten == 'E')
			return 5;
		if (Knoten == 'F')
			return 6;
		return 7;
	}

	@SuppressWarnings("null")
	public String runNode(String input) throws SoFTException {
		String nodes = "ABCDEFG";
		String other_nodes = diff(nodes, this.myChar());
		int nodes_anzahl = nodes.length();
		int Anfangswert = number(word(input, 1, InputPosition(this.myChar())));
		boolean epoche_gerade = false;
		int Anzahl = 1;
		int Endwert = 0;
		Msg msg;
		boolean runloop = true;

		do {
			msg = form('i', Anfangswert + " " + Anzahl);
			String Empfang = "";
			ArrayList<Integer> node_werte = new ArrayList<Integer>();

			msg.send(other_nodes);

			while (diff(other_nodes, Empfang) != "" && time() <= 2000) { // solange bis von allen Nachbarknoten
																			// empfangen wurde
				msg = receive(diff(other_nodes, Empfang), 2000); // bzw. Zeit auf 2 Sek begrenzt
				if (msg != null) {
					Empfang = Empfang + msg.getSe(); // Empfänger konkatenieren
					node_werte.add(Integer.parseInt(msg.getCo().charAt(0) + ""));
				}
			}

			int A = Anfangswert;
			for (int i = 0; i < node_werte.size(); i++) { // 1 LISte addieren
				A = A + node_werte.get(i);
			}

			if (A >= nodes_anzahl - (nodes_anzahl - 1) / 3) { // Anzahl 1 >= m-F
				Anfangswert = 1;
				Anzahl = A;
				runloop = false;
			} else if ((nodes_anzahl - A) >= nodes_anzahl - (nodes_anzahl - 1) / 3) { // Anzahl 0 >= m-F
				Anfangswert = 0;
				Anzahl = nodes_anzahl - A;
				runloop = false;
			} else { // ansonsten Epochenwert
				if (epoche_gerade) {
					Anfangswert = 1;
				} else {
					Anfangswert = 0;
				}
				Anzahl = 1;
			}

			epoche_gerade = !epoche_gerade;

		} while (runloop);

		return "" + Anfangswert;
	}

}

public class Aufgabe_2 extends SoFT {

	public int result(String input, String[] output) {

		String nodes = "ABCDEFG";
		int nodes_anzahl = nodes.length();

		boolean ic1 = true;
		boolean ic2 = true;

		String result_a = words(output[0], 1, 1, 1);

		for (int i = 1; i < nodes_anzahl; i++) {
			if (!result_a.equals(words(output[i], 1, 1, 1))) {
				ic1 = false;
			}
		}

		for (int i = 0; i < nodes_anzahl; i++) {
			if (!word(output[i], 1, 1).equals(word(input, 1, i + 1))) {
				ic2 = false;
			}
	//		System.out.println(i + " " + word(output[i], 1, 1) + " = " + word(input, 1, i + 1));
		}

		if (ic1 == true && ic2 == true) {
			return 0;
		} else if (ic1 == true && ic2 == false) {
			return 1;
		} else if (ic1 == false && ic2 == true) {
			return 2;
		} else {
			return 3;
		}
	}

	public static void main(String[] args) {
		new Aufgabe_2().runSystem(
				new Node[] { new DBA1_Knoten(), new DBA1_Knoten(), new DBA1_Knoten(), new DBA1_Knoten(),
						new DBA1_Knoten(), new DBA1_Knoten(), new DBA1_Knoten() },
				"Aufgabe 2", "Aufgabe 2 mit SoFT", "Joscha Hasselbeck");
	}

}
