package com.mohamad.Ciphers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AbstractDocument.Content;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.omg.CORBA.portable.ApplicationException;

public class Decript {

	public static int asciiA = 65, asciiZ = 90;

	public String PlayFairDecryption(String playfairKeyword, String encriptedText) {

		Key pfkey = new Key(playfairKeyword);
		char[][] KEY = pfkey.getPlayFairMatrixKey();
		List<String> d_plain = new ArrayList<String>();
		encriptedText = encriptedText.replaceAll(" ", "");
		char[] pt_chars = encriptedText.toUpperCase().toCharArray();

		int ptr = 0;
		while (ptr < pt_chars.length) {
			String pair = "";
			char ch1 = pt_chars[ptr];
			char ch2 = pt_chars[ptr + 1];

			pair += ch1;
			pair += ch2;
			d_plain.add(pair);
			ptr += 2;

		}

		String dec_text = "";
		for (String st : d_plain) {
			char enc_c1, enc_c2;
			// System.out.print("\t"+st);
			AddressXY c1 = new AddressXY(), c2 = new AddressXY(), imgC1 = new AddressXY(), imgC2 = new AddressXY();
			c1 = getCharPosition(st.charAt(0), KEY);
			c2 = getCharPosition(st.charAt(1), KEY);
			getDecryptedPositions(c1, c2, imgC1, imgC2);
			System.out.println(imgC2.x+ "\t&&\t"+imgC2.y);
			enc_c1 = KEY[imgC1.x][imgC1.y];
			enc_c2 = KEY[imgC2.x][imgC2.y];

			dec_text += enc_c1;
			dec_text += enc_c2;

		}

		return dec_text;
	}

	private void getDecryptedPositions(AddressXY c1, AddressXY c2, AddressXY imgC1, AddressXY imgC2) {
		if (c1.x == c2.x) {
			imgC1.x = c1.x;
			imgC2.x = c1.x;

			imgC1.y = (c1.y - 1) ;
			imgC2.y = (c2.y - 1) ;
			
			while(imgC1.y < 0){
				imgC1.y += 5;
			}
			while(imgC2.y < 0){
				imgC2.y += 5;
			}

			imgC1.y = imgC1.y %5;
			imgC2.y = imgC2.y %5;
			
			return;
		}

		if (c1.y == c2.y) {
			imgC1.y = c1.y;
			imgC2.y = c1.y;

			imgC1.x = (c1.x - 1) ;
			imgC2.x = (c2.x - 1) ;
			
			while(imgC1.x < 0){
				imgC1.x += 5;
			}
			while(imgC2.x < 0){
				imgC2.x += 5;
			}

			imgC1.x = imgC1.x %5;
			imgC2.x = imgC2.x %5;

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

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////// Hill
	//////////////////////////////////////////////////////////////////////////////////////////////////// Cipher////////////////////////////////////////////////////

	public String HillDecryption(int[][] KEY, String cipherText) {
		int m = KEY.length;
		DoubleMatrix K_inv = new DoubleMatrix(m, m);
		DoubleMatrix P = new DoubleMatrix(m, 1);
		DoubleMatrix C = new DoubleMatrix(m, 1);

		int detK = determinant(KEY);
		detK = getDet_inv_mod_26(detK);
		if (detK < 0)
			detK += 26;

		int[][] cof = Cofactors(KEY);
		// fill the K matrix
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++) {
				K_inv.put(j, i, (cof[i][j] * detK) % 26);
			}

		// System.out.println("detK= "+detK);

		// Div_Pair[][] dpm = getA_Bs(K_inv, m, m);

	//	for (int i = 0; i < K_inv.length; i++) {
	//		System.out.println(K_inv.get(i) + "\tcof:\t" + cof[i % m][(i - (i % m)) / m]);
	//	}

		String remText = cipherText.toUpperCase();
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
				C.put(i, cPos);
			}
			// P = K_inv.mmul(C);
			// P=K_inv.mmul(C);
			P = K_inv.mmul(C);

			// System.out.println(P.get(0, 0)+"\t"+P.get(1, 0)+"\t"+P.get(2,
			// 0));
			for (int i = 0; i < m; i++) {
				int ind = (int) (P.get(i) % 26);
				char c = (char) (ind + asciiA);
				out += c;
			}
		}

		return out;
	}

	private int getDet_inv_mod_26(int det) {

		while(det < 0){
			det+=26;
		}
		det = det % 26;
		System.out.println("DET HEER : "+det);
		for (int x = 0; x < 26; x++) {
			if ((x * det) % 26 == 1)
				return x;

		}

		System.err.println("ERROR: "+det+" IS NOT INVERTUBLE mod26 PLEAS CHECK YOUR MATRIX!\n\tMETHOD getDet_inv_mod_26([]) ERROR");
		
		return 0;
	}

	private Div_Pair[][] getA_Bs(DoubleMatrix mat, int colNum, int rowNum) {
		Div_Pair[][] out = new Div_Pair[rowNum][colNum];

		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				double matI = mat.get(i, j);
				out[i][j] = new Div_Pair(100, 1);
				if (matI < 0) {

					for (int cI = -25; cI <= 0; cI++) {
						for (int cJ = 1; cJ < 26; cJ++) {
							double minResult = out[i][j].a / out[i][j].b;
							double curResult = (double) cI / (double) cJ;

							if (Math.abs(minResult - matI) > Math.abs(curResult - matI)) {
								out[i][j].a = cI;
								out[i][j].b = cJ;
							}
						}
					}

				} else {
					for (int cI = 25; cI >= 0; cI--) {
						for (int cJ = 1; cJ < 26; cJ++) {
							double minResult = out[i][j].a / out[i][j].b;
							double curResult = (double) cI / (double) cJ;

							if (Math.abs(minResult - matI) > Math.abs(curResult - matI)) {
								out[i][j].a = cI;
								out[i][j].b = cJ;
							}
						}
					}
				}
			}
		}

		return out;
	}

	public int[][] mult(int[][] A, int[][] B) {
		int A_rows = A.length;
		int A_col = A[0].length;
		int B_rows = B.length;
		int B_col = B[0].length;

		int[][] result = new int[A_rows][ B_col];
		for (int i = 0; i < result.length; i++)
			for(int j=0;j<result[0].length;j++)
				result[i][j]= 0;

		if (A_col != B_rows) {
			System.err.println("Error MAtrix dimentions!");
			return null;
		}

		for (int i = 0; i < A_rows; i++) {
			for (int j = 0; j < B_col; j++) {

				for (int k = 0; k < B_rows; k++) {
					result[i][ j]=result[i][ j] + (A[i][k]) * B[k][ j];
				}
			}

		}

		return result;

	}

	private int[][] Cofactors(int[][] mat) {
		int[][] out = new int[mat.length][mat.length];

		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				int sign = (i + j) % 2 == 0 ? +1 : -1;

				int[][] smallMat = new int[mat.length - 1][mat.length - 1];
				int s_ctr_i = 0, s_ctr_j = 0;
				for (int ii = 0; ii < mat.length; ii++) {
					if (ii == i)
						continue;
					for (int jj = 0; jj < mat.length; jj++) {
						if (jj == j)
							continue;

						smallMat[s_ctr_i][s_ctr_j] = mat[ii][jj];
						s_ctr_j = (s_ctr_j + 1) % (mat.length - 1);

					}
					s_ctr_i++;
				}
				int det = determinant(smallMat);
				while (det < 0) {
					det += 26;
				}

				det = det % 26;
				out[i][j] = sign * det;

				while (out[i][j] < 0) {
					out[i][j] += 26;
				}

				out[i][j] = out[i][j] % 26;
			}
		}

		return out;
	}

	////////// copy right @
	////////// http://professorjava.weebly.com/matrix-determinant.html
	private int determinant(int[][] matrix) { // method sig. takes a matrix (two
												// dimensional array), returns
												// determinant.
		int sum = 0;
		int s;
		if (matrix.length == 1) { // bottom case of recursion. size 1 matrix
									// determinant is itself.
			return (matrix[0][0]);
		}
		for (int i = 0; i < matrix.length; i++) { // finds determinant using
													// row-by-row expansion
			int[][] smaller = new int[matrix.length - 1][matrix.length - 1]; // creates
																				// smaller
																				// matrix-
																				// values
																				// not
																				// in
																				// same
																				// row,
																				// column
			for (int a = 1; a < matrix.length; a++) {
				for (int b = 0; b < matrix.length; b++) {
					if (b < i) {
						smaller[a - 1][b] = matrix[a][b];
					} else if (b > i) {
						smaller[a - 1][b - 1] = matrix[a][b];
					}
				}
			}
			if (i % 2 == 0) { // sign changes based on i
				s = 1;
			} else {
				s = -1;
			}
			sum += s * matrix[0][i] * (determinant(smaller)); // recursive step:
																// determinant
																// of larger
																// determined by
																// smaller.
		}
		return (sum); // returns determinant value. once stack is finished,
						// returns final determinant.
	}

	public int[][] matInv_mod(int[][] mat){
		if(mat.length != mat[0].length){
			System.err.println("ERROR: MATRIX HAS NO INVERSE!");
			return null;
		}
		int m= mat.length;
		int[][] out = new int[m][m];
		int detK = determinant(mat);
		
		detK = getDet_inv_mod_26(detK);
		int[][] cof = Cofactors(mat);
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++) {
				out[i][j]= (cof[j][i] * detK) % 26;
			}
		
		return out;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Affine
	//////////////////////////////////////////////////////////////////////////////////////////////////// Caesar///////////////////////////////////////////////////////
	public String AffineDecryption(int a, int b, String cipherText) {
		cipherText = cipherText.toUpperCase();

		cipherText = cipherText.replaceAll(" ", "");

		char[] cs = cipherText.toCharArray();
		String out = "";
		int a_1 = -1;
		for (int i = 0; i < 26; i++) {
			if ((a * i) % 26 == 1) {
				a_1 = i;
				break;
			}
		}
		if(a_1 == -1){
			System.err.println("ERROR : AffineDecryption error ' a = "+a+" '   has no inverse modulo 26 \n since ("+a+", 26) not coprime !! ");
			return "#ERROR : AffineDecryption error ' a = "+a+" '   has no inverse modulo 26 \n\tsince ("+a+", 26) not co-prime !!";
		}
		for (char p : cs) {
			int p_ind = p - asciiA;

			int C_ind = a_1 * (p_ind - b) ;
			while(C_ind < 0)
				C_ind+=26;
			
			char c = (char) (C_ind%26 + asciiA);
			out += c;
		}

		return out;
	}

}

class Div_Pair {
	public double a, b;

	public Div_Pair() {
	}

	public Div_Pair(double a, double b) {
		this.a = a;
		this.b = b;
	}
}