package com.mohamad.Ciphers;

import java.util.*;

public class Key {

	private String PlayFairKeyword;

	private char[][] PlayFairMatrixKey;
	
	private int[][] HillKey;
	
	private int AffineKey_a, AffineKey_b;
	
	public Key(int affineKey_a, int affineKey_b) {
		AffineKey_a = affineKey_a;
		AffineKey_b = affineKey_b;
	}
	public int getAffineKey_a() {
		return AffineKey_a;
	}
	public void setAffineKey_a(int affineKey_a) {
		AffineKey_a = affineKey_a;
	}
	public int getAffineKey_b() {
		return AffineKey_b;
	}
	public void setAffineKey_b(int affineKey_b) {
		AffineKey_b = affineKey_b;
	}
	public Key(int[][] hillKey) {
		HillKey = hillKey;
	}
	public int[][] getHillKey() {
		return HillKey;
	}
	public void setHillKey(int[][] hillKey) {
		HillKey = hillKey;
	}
	public Key(){}
	public Key(String playFairKeyword) {
		this.PlayFairKeyword = playFairKeyword;
		this.PlayFairMatrixKey = calcPlayFairKeyMatrix();
	}

	public String getPlayFairKeyword() {
		return PlayFairKeyword;
	}

	public void setPlayFairKeyword(String playFairKeyString) {
		this.PlayFairKeyword = playFairKeyString;
		this.PlayFairMatrixKey = calcPlayFairKeyMatrix();
	}

	private char[][] calcPlayFairKeyMatrix() {

		
		if (PlayFairKeyword == null || PlayFairKeyword == "") {
			System.err.println("Error: getPlayFairKeyMatrix method can not find KeyString\n"
					+ "pleas make sure that the keyString is setted!..");
			return null;
		}

		char[][] PFMatrix = new char[5][5];
		PlayFairKeyword = PlayFairKeyword.toUpperCase();
		char[] keyS = PlayFairKeyword.toCharArray();

		List<Character> lc = new ArrayList<Character>();
		String nrK = "";
		for(Character c : keyS){
			if(!lc.contains(c)){
				lc.add(c);
				nrK+=c;
			}
		}
		char[] keyStr = nrK.toCharArray();
		
		int keyLength = keyStr.length;
		int asciiA = 65, asciiZ = 90;
		int asciiPtr = asciiA;
		
		
		
		for (int i = 0; i < 25; i++) {
			if (i < keyLength && !isCharInMatrix(keyStr[i], PFMatrix)) {
				int column = i % 5, row = (i - column) / 5;
				PFMatrix[row][column] = keyStr[i];
			} else {
				for (int j = asciiPtr; j <= asciiZ; j++) {
					if (!isCharInMatrix((char) j, PFMatrix)) {
						int column = i % 5, row = (i - column) / 5;
						PFMatrix[row][column] = (char) j;
						asciiPtr = j + 1;
						break;
					}
				}
			}
		}

		return PFMatrix;
	}

	public String displayPlayFairKey(){
		
		String out = "";
		for(char[] ar : PlayFairMatrixKey){
			for(char c : ar)
				out+=""+c+"\t";
		
			out += "\n";
		}
		
		return out;
	}

	
	public char[][] getPlayFairMatrixKey() {
		return PlayFairMatrixKey;
	}

	private boolean isCharInMatrix(char c, char[][] matrix) {

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				char kc = matrix[i][j];
				if (c == kc || (c == 'I' && kc == 'J') || (c == 'J' && kc == 'I'))
					return true;

			}
		}

		return false;
	}

}
