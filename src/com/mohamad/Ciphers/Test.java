package com.mohamad.Ciphers;

import java.io.File;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {

		Key key = new Key("mohammad");

		System.out.println(key.displayPlayFairKey());
		Encrypt e = new Encrypt();
		Decript d = new Decript();
		String enc = e.PlayFairEncryption("mohammad", "");
		
		System.out.println(enc+"\n"+d.PlayFairDecryption("mohammad", enc));	 
		
		int[][] hillKey ={{3,3},{2,5}}; //{{6,24,1},{13,16,10},{20,17,15}};
		String hillEnc = e.HillEncryption(hillKey, "HELP"), 
				hillDec = d.HillDecryption(hillKey, hillEnc);
		
		System.out.println(hillEnc+"\n"+hillDec);
		
		String aff = e.AffineEncryption(5, 8, "AFFINE CIPHER"), D_aff = d.AffineDecryption(5, 8, aff);
		System.out.println(aff+"\t-->\t"+D_aff);
		
		Attack attacker = new Attack();
		int[][] hk = {{23,17},{21,2}};
		Key k = new Key(hk);
		String h_enc = e.HillEncryption(k.getHillKey(), "THHE");
		System.out.println("HILL -------encrypted------>"+h_enc);
	//	File f = new File("hillAttack.txt"), affineFile = new File("affineAttack.txt");
	//	int[][] att_k = attacker.HillCipherAttack(f, "THHE", k);
	//	System.out.println("THHE -------hacked------>"+e.HillEncryption(att_k, "THHE"));
		//attacker.AffineCaesarAttack(affineFile, aff, "AFFINECIPHER");
	}

}
