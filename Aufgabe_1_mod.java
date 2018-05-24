
/**
 * @author Joscha Hasselbeck
 * 
 * Modifkation nach c)
 *
 */

import SoFTlib.*;
import static SoFTlib.Helper.*;

class QuellKnotenMod extends Node {
	String Nachbarn(char Knoten) {
		if (Knoten == 'A')
			return "BE";
		if (Knoten == 'B')
			return "AEC";
		if (Knoten == 'C')
			return "BED";
		if (Knoten == 'D')
			return "CG";
		if (Knoten == 'E')
			return "ABCFG";
		if (Knoten == 'F')
			return "EG";
		/* G */ return "DEF";
	}

	public String runNode(String input) throws SoFTException {
		String Quittierung = "";
		String Empfang = "";
		Msg msg;
		int zeitschranke = 800;

		msg = form('i', "1"); // A Nachricht-i mit Inhalt 1
		msg.send(Nachbarn(this.myChar())); // senden an alle Nachbarn von A

		while (diff(Nachbarn(this.myChar()), Empfang) != "" && time() <= zeitschranke) { // solange bis von allen
																						 // Nachbarknoten empfangen wurde
			msg = receive(diff(Nachbarn(this.myChar()), Empfang), zeitschranke);
			if (msg != null) {
				Empfang = Empfang + msg.getSe();
				Quittierung = or(Quittierung, msg.getCo());
			}
		}
		return Quittierung;
	}
}

class KnotenMod extends Node {

	String Nachbarn(char Knoten) {
		if (Knoten == 'A')
			return "BE";
		if (Knoten == 'B')
			return "AEC";
		if (Knoten == 'C')
			return "BED";
		if (Knoten == 'D')
			return "CG";
		if (Knoten == 'E')
			return "ABCFG";
		if (Knoten == 'F')
			return "EG";
		/* G */ return "DEF";
	}

	public String runNode(String input) throws SoFTException {
		String Quittierung = "";
		String ersterEmpfang = "";
		String Empfang = "";
		Msg msg;
		int zeitschranke = 800;

		if (ersterEmpfang == "") {
			msg = receive(Nachbarn(this.myChar()), 'i', zeitschranke); // Nachricht vom ersten Vorgänger Knoten
																		// Empfangen
			if (msg != null) {
				ersterEmpfang = "" + msg.getSe();
				zeitschranke = zeitschranke - 100 * number(msg.getCo());
				msg = form('i', number(msg.getCo()) + 1); // Entfernung um 1 erhöhen
				msg.send(diff(Nachbarn(this.myChar()), ersterEmpfang)); // senden an alle Nachbarn außer von dem Knoten
																		// von dem sie kam
			}

		}

		while (equal(diff(Nachbarn(this.myChar()), Empfang), ersterEmpfang) == false && time() <= zeitschranke) {
			// solange bis von allen Nachbarknoten empfangen wurde

			msg = receive(diff(Nachbarn(this.myChar()), Empfang), zeitschranke);
			if (msg != null) {
				Empfang = Empfang + msg.getSe();
				if (msg.getTy() == 'q') { // wenn Quittierung dann konkatenieren
					Quittierung = Quittierung + msg.getCo();
				}
				if (equal(diff(Nachbarn(this.myChar()), Empfang), ersterEmpfang)) { // wenn von allen Nachbarknoten
																					// empfangen wurde dann Quittierung
																					// and Vorgängerknoten
					Quittierung = Quittierung + this.myChar();
					msg = form('q', Quittierung);
					msg.send(ersterEmpfang);
				}
			}

		}

		if (time() >= zeitschranke && ersterEmpfang != "") { // wenn Zeitschranke überschritten Quttierung			
			Quittierung = Quittierung + this.myChar();
			msg = form('q', Quittierung);
			msg.send(ersterEmpfang);
		}

		return "";
	}

}

public class Aufgabe_1_mod extends SoFT {

	public int result(String input, String[] output) {

		String result_a = words(output[0], 1, 1, 1);
		if (diff("BCDEFG", result_a) == "") { // wenn Quittierung richtig dann 0 sonst 1
			return 0;
		} else {
			return 1;
		}
	}

	public static void main(String[] args) {
		new Aufgabe_1()
				.runSystem(
						new Node[] { new QuellKnotenMod(), new KnotenMod(), new KnotenMod(), new KnotenMod(),
								new KnotenMod(), new KnotenMod(), new KnotenMod() },
						"Aufgabe 1", "Aufgabe 1 mit SoFT", "Joscha Hasselbeck");
	}

}
