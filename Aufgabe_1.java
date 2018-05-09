
/**
 * @author Joscha Hasselbeck
 * 
 *		Echoprotokoll nach Algorithmus aus der Vorlesung:
 *		
 *		Inf		:	zu übertragende Nutzinformation
 *		Quitt	:	Quittierung
 *		Q 		:	Quellknoten
 *		N(X) 	:	Menge der Nachbarknoten eines Knotens X
 *		
 *		Quellknoten Q:
 *			send Inf to N(Q);
 *			loop for all Y element of N(Q)
 *			receive Inf or Quitt from Y
 *			end loop
 *		
 *		andere Knoten X:
 *			receive Inf from Y element of N(X);
 *			send Inf to N(X) – {Y};
 *			loop for all Z element of N(X) – {Y}
 *			receive Inf or Quitt from Z
 *			end loop
 *			send Quitt to Y
 *
 */

import SoFTlib.*;
import static SoFTlib.Helper.*;

class QuellKnoten extends Node {
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

		msg = form('i', "1"); // A Nachricht-i mit Inhalt 1
		msg.send(Nachbarn(this.myChar())); // senden an alle Nachbarn von A

		while (diff(Nachbarn(this.myChar()), Empfang) != "") { // solange bis von allen Nachbarknoten empfangen wurde
			msg = receive(diff(Nachbarn(this.myChar()), Empfang), 2000);
			if (msg != null) {
				Empfang = Empfang + msg.getSe();
				Quittierung = or(Quittierung, msg.getCo());
			}
		}
		return Quittierung;
	}
}

class Knoten extends Node {

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

		if (ersterEmpfang == "") {
			msg = receive(Nachbarn(this.myChar()), 'i', 2000); // Nachricht vom ersten Vorgänger Knoten Empfangen
			if (msg != null) {
				ersterEmpfang = "" + msg.getSe();
				msg = form('i', number(msg.getCo()) + 1); // Entfernung um 1 erhöhen
				msg.send(diff(Nachbarn(this.myChar()), ersterEmpfang)); // senden an alle Nachbarn außer von dem Knoten
																		// von dem sie kam
			}

		}

		while (equal(diff(Nachbarn(this.myChar()), Empfang), ersterEmpfang) == false) { // solange bis von allen
																						// Nachbarknoten empfangen wurde

			msg = receive(diff(Nachbarn(this.myChar()), Empfang), 2000);
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

		return "";
	}

}

public class Aufgabe_1 extends SoFT {

	public int result(String input, String[] output) {

		String result_a = words(output[0], 1, 1, 1);
		if (diff("BCDEFG", result_a) == "") { // wenn Quittierung richtig dann 0 sonst 1
			return 0;
		} else {
			return 1;
		}
	}

	public static void main(String[] args) {
		new Aufgabe_1().runSystem(new Node[] { new QuellKnoten(), new Knoten(), new Knoten(), new Knoten(),
				new Knoten(), new Knoten(), new Knoten() }, "Aufgabe 1", "Aufgabe 1 mit SoFT", "Joscha Hasselbeck");
	}

}
