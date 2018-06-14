
/**
 * @author Joscha Hasselbeck
 * 
 *		DBA1 nach Algorithmus aus der Vorlesung:
 *		aktuell F = 2 => m = 7
 *
 *		Fragen:
 *
 *		=> 	ICB1 + ICB2 Fehlerfrei:
 *			Wenn nicht alle Knoten am Anfang den gleichen Wert besitzen ist ICB2 immer wahr
 *
 *		=> fester Wert: 1 <= F <= 3 
 *
 *		=> Anfang ungerade Epoche
 *
 * 		TODO:		
 *		erstellen F = 3 => m = 10
 *
 */

import SoFTlib.*;
import static SoFTlib.Helper.*;

import java.util.ArrayList;

import javax.security.auth.x500.X500Principal;

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

	public String runNode(String input) throws SoFTException {
		String nodes = "ABCDEFG";
		String other_nodes = diff(nodes, this.myChar());
		int nodes_anzahl = nodes.length();
		int Anfangswert = number(word(input, 1, InputPosition(this.myChar())));
		boolean epoche_gerade = false;
		int Anzahl = 1;
		int phasenanzahl = 1;
		int phasendauer = 200;
		Msg msg;
		boolean last_round = false;
		boolean runloop = true;

		do {
			msg = form('i', Anfangswert + " " + Anzahl);
			String Empfang = "";
			ArrayList<Integer> node_werte = new ArrayList<Integer>();

			msg.send(other_nodes);

			while (diff(other_nodes, Empfang) != "" && time() <= phasenanzahl * phasendauer) { // solange bis von allen
																								// Nachbarknoten
				// empfangen wurde
				msg = receive(diff(other_nodes, Empfang), phasenanzahl * phasendauer); // bzw. Zeit auf 2 Sek begrenzt
				if (msg != null) {
					Empfang = Empfang + msg.getSe(); // Empfänger konkatenieren
					if (msg.getCo().charAt(0) == '0' || msg.getCo().charAt(0) == '1') {
					node_werte.add(Integer.parseInt(msg.getCo().charAt(0) + ""));
					}
				}
			}

			phasenanzahl = phasenanzahl + 1;

			int A = Anfangswert;
			for (int i = 0; i < node_werte.size(); i++) { // 1 LISte addieren + eigenen Wert
				A = A + node_werte.get(i);
			//	System.out.println(this.myChar() + " " + A);
			}

			if (last_round) {
				runloop = false;
			}

			if (A >= nodes_anzahl - (nodes_anzahl - 1) / 3) { // Anzahl 1 >= m-F => es gilt F = (m - 1)/3
				Anfangswert = 1;
				Anzahl = A;
				last_round = true;
			} else if ((nodes_anzahl - A) >= nodes_anzahl - (nodes_anzahl - 1) / 3) { // Anzahl 0 >= m-F
				Anfangswert = 0;
				Anzahl = nodes_anzahl - A;
				last_round = true;
			} else { // ansonsten Epochenwert
				if (epoche_gerade) {
					Anfangswert = 0;
				} else {
					Anfangswert = 1;
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
		String result_1 = null;

		for (int i = 0; i < nodes_anzahl; i++) {
			if (fault(i) == 0) {
				result_1 = words(output[i], 1, 1, 1);
				break;
			}
		}
		
		for (int i = 0; i < nodes_anzahl; i++) {
			if (!result_1.equals(words(output[i], 1, 1, 1)) && fault(i) == 0) {
				ic1 = false;
			}
		}
		
		for (int i = 0; i < nodes_anzahl; i++) {
			if (!word(output[i], 1, 1).equals(word(input, 1, i + 1)) && fault(i) == 0) {
				ic2 = false;
			}
		
		}
		
		
		boolean ic2_0 = false;
		boolean ic2_1  = false;
		
		for (int i = 0; i < nodes_anzahl; i++) { 
			if( fault(i) == 0 ) {
			if (word(input, 1, i ).equals("0")){
				ic2_0 = true;
		//		System.out.println(nodes.charAt(i) + " " + word(input, 1, i ) );
			}
			if (word(input, 1, i).equals("1")){
				ic2_1 = true;
		//		System.out.println(nodes.charAt(i) + " " + word(input, 1, i ) );
			}
			}
		}
		
		if (ic2 == false) {
		ic2 = ic2_0 && ic2_1;
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
