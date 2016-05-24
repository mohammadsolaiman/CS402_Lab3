package com.mohamad.Ciphers;

import java.util.List;

import org.jblas.DoubleMatrix;

import java.util.ArrayList;

public class Encrypt {

	public static int asciiA = 65, asciiZ = 90;

	////////////////////////////////////////// PlayFair/////////////////////////////////////////////////
	public String PlayFairEncryption(String playfairKeyword, String plainText) {

		Key pfkey = new Key(playfairKeyword);
		char[][] KEY = pfkey.getPlayFairMatrixKey();
		List<String> d_plain = new ArrayList<String>();
		plainText = plainText.replaceAll(" ", "");
		char[] pt_chars = plainText.toUpperCase().toCharArray();

		int ptr = 0;
		while (ptr < pt_chars.length) {
			String pair = "";
			char ch1 = pt_chars[ptr];
			char ch2;
			if (ptr + 1 < pt_chars.length)
				ch2 = pt_chars[ptr + 1];
			else
				ch2 = 'X';

			if (ch1 == ch2) {
				if (ch1 == 'X') {
					pair += ch1;
					pair += 'Y';
				} else {
					pair += ch1;
					pair += 'X';
				}

				ptr++;
				d_plain.add(pair);

			} else {
				pair += ch1;
				pair += ch2;
				d_plain.add(pair);
				ptr += 2;
			}
		}

		String enc_text = "";
		for (String st : d_plain) {
			char enc_c1, enc_c2;
			// System.out.print("\t"+st);
			AddressXY c1 = new AddressXY(), c2 = new AddressXY(), imgC1 = new AddressXY(), imgC2 = new AddressXY();
			c1 = getCharPosition(st.charAt(0), KEY);
			c2 = getCharPosition(st.charAt(1), KEY);
			getEncryptedPositions(c1, c2, imgC1, imgC2);
			enc_c1 = KEY[imgC1.x][imgC1.y];
			enc_c2 = KEY[imgC2.x][imgC2.y];

			enc_text += enc_c1;
			enc_text += enc_c2;

		}

		return enc_text;
	}

	private void getEncryptedPositions(AddressXY c1, AddressXY c2, AddressXY imgC1, AddressXY imgC2) {
		if (c1.x == c2.x) {
			imgC1.x = c1.x;
			imgC2.x = c1.x;

			imgC1.y = (c1.y + 1) % 5;
			imgC2.y = (c2.y + 1) % 5;

			return;
		}

		if (c1.y == c2.y) {
			imgC1.y = c1.y;
			imgC2.y = c1.y;

			imgC1.x = (c1.x + 1) % 5;
			imgC2.x = (c2.x + 1) % 5;

			return;
		}
		imgC1.x = c1.x;
		imgC2.x = c2.x;

		imgC1.y = c2.y;
		imgC2.y = c1.y;

	}

	private AddressXY getCharPosition(char c, char[][] key_matrix) {
		for (int i = 0; i < key_matrix.length; i++) {
			for (int j = 0; j < key_matrix[0].length; j++) {
				if (key_matrix[i][j] == c
						|| ((c == 'I' && key_matrix[i][j] == 'J') || (c == 'J' && key_matrix[i][j] == 'I'))) {
					AddressXY addr = new AddressXY(i, j);
					return addr;
				}
			}
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////// Hill
	/////////////////////////////////////////////////////////////////////////////////////////////////// Cipher///////////////////////////////////////////////

	public String HillEncryption(int[][] KEY, String plainText) {
		int m = KEY.length;
		DoubleMatrix K = new DoubleMatrix(m, m);
		DoubleMatrix P = new DoubleMatrix(m, 1);
		DoubleMatrix C = new DoubleMatrix(m, 1);

		// fill the K matrix
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++) {
				K.put(i, j, KEY[i][j]);
			}

		String remText = plainText.toUpperCase();

		String out = "";
		while (remText.length() > 0) {
			String Pt = "";
			if (remText.length() >= m) {

				Pt = remText.substring(0, m);
				remText = remText.substring(m);

			} else {
				Pt = remText;
				remText = "";
				for (int i = Pt.length(); i < m; i++)
					Pt += 'X';
			}

			char[] cPt = Pt.toCharArray();
			for (int i = 0; i < m; i++) {
				int cPos = cPt[i] - asciiA;
				P.put(i, cPos);
			}
			C = K.mmul(P);
			// System.out.println(K.get(0, 1));
			for (int i = 0; i < m; i++) {
				int ind = (int) (C.get(i) % 26);
				char c = (char) (ind + asciiA);
				out += c;
			}
		}

		return out;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////// Affine//////////////////////////////////////////////////////////
	//////////////////////////////// Caesar///////////////////////////////////////////////////////
	public String AffineEncryption(int a, int b, String PlainText) {
		PlainText = PlainText.toUpperCase();
		
		PlainText=PlainText.replaceAll(" ", "");
		
		char[] cs = PlainText.toCharArray();
		String out = "";
		
		for(char p:cs){
			int p_ind = p - asciiA;
			
			int C_ind = (a*p_ind + b)%26;
			char c = (char) (C_ind + asciiA);
			out+=c;
		}
		
		return out;
	}
}

class AddressXY {
	public int x, y;

	public AddressXY() {

	}

	public AddressXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

}