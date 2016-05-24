package com.mohamad.Ciphers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Attack {

	// all pairs are encrypted by the key : mohammad
	String[][] pairs = { { "Iamastudent", "NMODTUZGFLSY" }, { "mathmatics", "ODSAODQNER" }, { "freedom", "CTLHGHHO" },
			{ "playfairattackcodescript", "INFANFKQFYYFKRKCHGREQKNU" },
			{ "INFANFKQFYYFKRKCHGREQKNU", "KPNFTNIRNAANRWRKDESCRIPT" },
			{ "TEXTMESXSAGINGORTEXTINGISTHEACTOFCOMPOSINGANDSENDINGELECTRONICMESXSAGESBETWEENTWOXORMOREMOBILEPHONESORFIXEDORPORTABLEDEVICESOVERAPHONENETWORKTHETERMORIGINALLYREFERXREDTOMESSAGESXSENTUSINGTHESHORTMESSAGESERVICESMSITHASGROWNTOINCLUDEMULTIMEDIAMESSAGESKNOWNASMXMSCONTAININGIMAGEVIDEOSANDSOUNDCONTENTASWELXLASIDEOGRAMSKNOWNASEMOIXITHESERVICEITSELFHASDIFXFERENTCOLXLOQUIALISMSDEPENDINGONTHEREGIONITMAYSIMPLYBEREFERXREDTOASATEXTINNORTHAMERICATHEUNITEDKINGDOMAUSTRALIANEWZEALANDANDTHEPHILIPXPINESANSMSINMOSTOFMAINLANDEUROPEORANMXMSORSMSINTHEMIDXDLEXEASTAFRICAANDASIATHESENDEROFATEXTMESSAGEISCOMXMONLYREFERREDTOASATEXTER",
					"SFYSHBXHTHBPPFCWSFYSKPBPTUELOFRAGEHOKDQLPFFTHUFLMPPFLSFEUSAKKBHBXHTHBFQEFSXCFLRYHWCWOHSCOHIQSLLDAKLXCWBNHLMHUKCWYFEIGHBXKBLXMWCSDNAHLFLFRYCWNRELSFQOCWPBKPHNNXSCGFSWSCAUHOLXTHBFXHXLTYQTKPFUELXECWQALXTHBFXLQWKBLXHQNQADUEWCYKRAKPEKZGBHSPQNHBMPDOLXTHBFRLKAYKHTHVHQKCTYMNPKPFQBDFBXPMCHTHPARHTPOGAKSFTYHTXCSHNHQLHGDCTOHQLPCOTFXLOHLVNQELXLQWKBBLUTLSEAHTMPEYGFSCTYKCSHKHRQNMNKQHUHGLFLMPPFAKSACSFBKMPKQAFAQLDINXCFSCGFSWSCAUHDTHSFYSKPKASUADHBQKFOSAGSPKSFOPKPPGHODTTUTONKFTCXXGHNFTMDPASAGLMLNKLZIKLFTHLTHQKPOHTUACODKPNHPAGSWCLGCWFTHVHQCWQHQLTYELBQHZHPLHFHTUFNQKFOFTMDQLFYELXLPACSACFYLHQALXTHBFLQKCHVOHPNWTFGCSSCAUHDTHSFYSCS" } };
	int n = 20;

	public void AffineCaesarAttack(File outputFile, String cipherText, String plainText) throws IOException {
		FileWriter fw = new FileWriter(outputFile);
		plainText = plainText.toUpperCase();
		plainText = plainText.replaceAll(" ", "");
		Decript dec = new Decript();
		String outtxt = "\t\tAFFINE CAESAR ATTACK!\n\ntrying to detect\t" + plainText + "\tfrom\t" + cipherText
				+ "\n\n";

		String PText = "";
		for (int a = 1; a < 26; a++) {
			
			for (int b = 0; b < 26; b++) {
				
				if (plainText.equals(PText))
					break;
				
				PText = dec.AffineDecryption(a, b, cipherText);

				
				outtxt += "Attack with Key :  a = " + a + "\tb = " + b + "	:\n\n\tPlaneText :  " + PText + "\n\n";
				
				if(PText.charAt(0) == '#')
					break;
			}
			if(PText.charAt(0) == '#')
				continue;
		}

		fw.write(outtxt);
		fw.close();
	}

	@SuppressWarnings("resource")
	public int[][] HillCipherAttack(File outputFile, String choosenPlainText, String cipherText, int KEY_DIM)
			throws IOException {
		FileWriter fw = new FileWriter(outputFile);
		choosenPlainText = choosenPlainText.toUpperCase();
		choosenPlainText = choosenPlainText.replaceAll(" ", "");

		cipherText = cipherText.toUpperCase();
		cipherText = cipherText.replaceAll(" ", "");

		int m = KEY_DIM;
		if (m * m > choosenPlainText.length() || m * m > cipherText.length()) {
			System.err.println("ERROR: number of plaintext string error");
			return null;
		}
		String pt = choosenPlainText.substring(0, m * m);

		cipherText = cipherText.substring(0, m * m);
		;
		int[][] P = new int[m][m];
		int[][] C = new int[m][m];

		int ind = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				P[j][i] = pt.charAt(ind) - Decript.asciiA;
				C[j][i] = cipherText.charAt(ind) - Decript.asciiA;
				ind++;
			}
		}

		Decript dec = new Decript();
		int[][] p_inv = dec.matInv_mod(P);
		int[][] K = dec.mult(C, p_inv);

		/*
		 * for(int i=0;i<p_inv.length;i++){ for(int j=0;j<p_inv[0].length;j++)
		 * System.out.print(p_inv[i][j]+"( "+C[i][j]+" ),    ");
		 * System.out.println(); }
		 */

		String out = "\t\tHill Cipher Attack!\n\nPlainText:\t" + pt + "\nCipherText:\t" + cipherText + "\n\nKEY:\n";
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				out += (K[i][j] % 26) + "\t";
			}
			out += "\n";
		}
		System.out.println(out);
		fw.write(out);
		fw.close();

		return K;
	}

	public void PlayFairAttack(File outputFile, String cipherText) throws IOException {
		FileWriter fw = new FileWriter(outputFile);
		for (int i = 0; i < pairs.length; i++) {
			pairs[i][0] = pairs[i][0].toUpperCase();
			pairs[i][0] = pairs[i][0].replaceAll(" ", "");

			pairs[i][1] = pairs[i][1].toUpperCase();
			pairs[i][1] = pairs[i][1].replaceAll(" ", "");
		}

		cipherText = cipherText.toUpperCase();
		cipherText = cipherText.replaceAll(" ", "");
		String out = "\t\tPlayFairAttack\n\n\tAttacking : " + cipherText + "\n\nresult:\n\t";

		String rem_txt = cipherText;

		while (rem_txt.length() > 0) {
			if (rem_txt.length() == 1)
				rem_txt += "X";

			String sub = rem_txt.substring(0, 2);
			rem_txt = rem_txt.substring(2);
			String pt = "";
			for (int i = 0; i < pairs.length; i++) {
				char[] chArr = pairs[i][1].toCharArray();
				for (int j = 0; j < chArr.length - 1; j += 2) {
					if (chArr[j] == sub.charAt(0) && chArr[j + 1] == sub.charAt(1)) {
						pt += pairs[i][0].charAt(j);
						pt += pairs[i][0].charAt(j + 1);
						break;
					}
				}
				if (!pt.equals(""))
					break;
			}
			if (pt.equals("")) {
				pt = " \'" + sub + "\' ";
			}
			out += pt;

		}

		fw.write(out);
		fw.close();
	}

}
