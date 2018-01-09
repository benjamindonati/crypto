import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

public class utils {
	
	static String ReadFile(File fileName) {
		String fileContent = "";
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			System.out.println("Contenu :");
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				fileContent+=ligne+"\n";
			}
			br.close();
		}		
		catch (Exception e){
			System.out.println(e.toString());
			System.out.println("Fin du programme.");
			System.exit( 0 );
		}
		
		return fileContent;
	}
	
	static void CreateFile(String fileName, String fileContent) {
		// R�cup�ration de la date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date date = new Date();
		//System.out.println(dateFormat.format(date));
		
		//R�cup�ration des donn�es
		byte[] data = fileContent.getBytes(StandardCharsets.UTF_8);
		
		// Cr�ation du fichier
		Path file = Paths.get("files/" + fileName + "_" + dateFormat.format(date) + ".txt");
		try {
			Files.write(file, data);
			System.out.println(fileName + "_" + dateFormat.format(date) + ".txt cr��.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void CreateFile(String fileName, String fileContent, boolean b) {
		// R�cup�ration de la date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date date = new Date();
		//System.out.println(dateFormat.format(date));
		
		//R�cup�ration des donn�es
		byte[] data = fileContent.getBytes(StandardCharsets.UTF_8);
		
		// Cr�ation du fichier
		Path file = Paths.get("files/" + fileName + ".txt");
		try {
			Files.write(file, data);
			System.out.println(fileName + ".txt cr��.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	Casse une cha�ne en plusieurs (longueur des sous_cha�nes en param�tre)
	static String[] SplitByNumber(String str, int size) {
		String[] tab = str.split("(?<=\\G.{"+size+"})");
		String toAdd = "";
//		Si la derni�re cha�ne est plus courte, on comble avec des 0		
		if (tab[tab.length-1].length() != size) {
			for (int i = tab[tab.length-1].length()-1; i < size-1; i++) {
				toAdd += "0";
			}
		}
		tab[tab.length-1] += toAdd;
		return tab;
	}
	
//	Convertit une cha�ne en binaire (table ascii)
	static String StringToBin(String text) {
		byte[] bytes = text.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
		   int val = b;
		   for (int i = 0; i < 8; i++)
		   {
		      binary.append((val & 128) == 0 ? 0 : 1);
		      val <<= 1;
		   }
//		   binary.append(' ');
		}
//		System.out.println(binary);
		return binary.toString();
	}

//	Convertit un hexad�cimal (pass� en String) en binaire
	static String hexToBin(String s) {
		String bin = String.format("%128s", new BigInteger(s, 16).toString(2)).replace(" ", "0");
//		System.out.println(bin);
		return bin;
	}
	
//	Renvoie le r�sultat du XOR de 2 binaires
	static String XOR(String num1, String num2) {;
		String form = "0000000000000000000000000000000000000000000000000000000000000000";
		BigInteger num1bi = new BigInteger(num1, 2); 
		BigInteger num2bi = new BigInteger(num2, 2);
		String XOR = (num1bi.xor(num2bi)).toString(2);
		int xorLength = XOR.length();
		int finalLength = 64 - (num1.length()-xorLength);
		String res = (form + (num1bi.xor(num2bi)).toString(2));
		return res.substring(finalLength);
	}
	
	
//	Addition binaire tronqu�e au N�me bit (inclus) 
	static String add2powN (String num1, String num2, int N) {
//		System.out.println(num1);
//		System.out.println(String.format("%0$64s", num2));
		BigInteger number0 = new BigInteger(num1, 2);
		BigInteger number1 = new BigInteger(num2, 2);
		String result = String.format("%0$" + N + "s", number0.add(number1).toString(2)).replace(" ", "0");
//		System.out.println(result);
//		System.out.println(String.format("%0$64s", result).replace(" ", "0"));
		if (result.length() > N) {
			return result.substring(1, N+1);
		} else {
			return result;
		}
		
	}
	
//	Convertit un array simple de N �l�ments en un array de 2 colomnes et N/2 lignes
//	Cet array contiendra nos blocs de 2 mots
	static String[][] Divide2 (String[] tab, int N) {
		String[][] blocks = new String[N/2][2];
		int num = 0;
		for (int i = 0; i < (N/2); i++) {
			blocks[i][0] = tab[num];
			num++;
			blocks[i][1] = tab[num];
			num++;
		}
				
		return blocks;
	}
	
	static String[][] Mix(String[][] blocks, int N) {
		int shift = 37;
		for (int i = 0; i < (N/2); i++) {
//			System.out.println("iMix = " + i);
//	        System.out.println(blocks[i][0]);
//	        System.out.println(blocks[i][1]);
//			Calcul de m'1
			blocks[i][0] = add2powN(blocks[i][0], blocks[i][1], 64);
//			D�clage de m2 de 37 bits
			int length = blocks[i][1].length();
	        int offset = ((shift % length) + length) % length;
	        String m2shifted = blocks[i][1].substring( offset, length ) + blocks[i][1].substring( 0, offset );
//	        Calcul de m'2
	        blocks[i][1] = XOR(blocks[i][0], m2shifted);
//	        System.out.println(blocks[i][0]);
//	        System.out.println(blocks[i][1]);
		}
//	System.out.println(Arrays.deepToString(blocks));
	return blocks;
	}
	
	
//	Chaque bloc de deux mots est invers� (le mot 1 devient le 2�me et vice-versa)
//	Chaque mot de 64 bits est divis� en 16 mots de 4 bits
//	On inversera ensuite l'ordre des bits de chacun de ces mots 
	static String[] Permute (String[][]blocks, int N) {
//		String[][] tempTab = blocks;
//		System.out.println(tempTab[0][0]);
//		System.out.println(tempTab[0][1]);
		String[] permute = new String[16];
		String[] result = new String[N];
		for (int i = 0; i < (N/2); i++) {
//			Inversment des blocs de 2 mots
			String temp1 = blocks[i][0], temp2 = blocks[i][1];
			blocks[i][0] = temp2;
			blocks[i][1] = temp1;
//			System.out.println("i = " + i);
//			System.out.println(blocks[i][0]);
//			System.out.println(blocks[i][1]);			
			for (int j = 0; j < 2; j++) {
//				On split le mot de 64 bits en 16 mots de 4 bits
//				System.out.println("i = " + i + " et j = " + j);
				permute = SplitByNumber(blocks[i][j], 4);
//				System.out.println(Arrays.toString(permute));
				blocks[i][j] = "";
				for(String bits : permute) {
//					On inverse l'ordre des bits de chaque mot
//					Et on recompose le mot de 64 bits
					blocks[i][j] += StrReverse(bits);
//					System.out.println("bits = " + bits);
//					System.out.println("inv = " + blocks[i][j]);
				}
//				System.out.println(Arrays.toString(blocks[i]));
			}			
			result[2*i] = blocks[i][0];
			result[(2*i)+1] = blocks[i][1];
//			System.out.println(Arrays.toString(result));
		}
		return result;
	}
	
//	Inverse une cha�ne
	static String StrReverse(String word) {
	    String reverse = "";
	    int length = word.length();
	    for( int i = length - 1 ; i >= 0 ; i-- ) {
	       reverse = reverse + word.charAt(i);
	    }
	    return reverse;
	}
	
//	Renvoie le nombre d'�l�ments d'un array
	static int Counter(String[] tab) {
		int i = 0;
		for(String s : tab) {
			i++;
		}
		return i;
	}
	
//	Fonction de hashage utilis�e : MD5
//	Taille du digest de sortie : 128 bits	
	private static final int INIT_A = 0x67452301;
	private static final int INIT_B = (int)0xEFCDAB89L;
	private static final int INIT_C = (int)0x98BADCFEL;
	private static final int INIT_D = 0x10325476;
	 
	private static final int[] SHIFT_AMTS = {
		7, 12, 17, 22,
		5,  9, 14, 20,
		4, 11, 16, 23,
		6, 10, 15, 21
	};
	 
	private static final int[] TABLE_T = new int[64];
	static {
		for (int i = 0; i < 64; i++)
			TABLE_T[i] = (int)(long)((1L << 32) * Math.abs(Math.sin(i + 1)));
	}
	 
	public static String calcMD5(byte[] message) {
		int messageLenBytes = message.length;
		int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
		int totalLen = numBlocks << 6;
		byte[] paddingBytes = new byte[totalLen - messageLenBytes];
		paddingBytes[0] = (byte)0x80;
	 
		long messageLenBits = (long)messageLenBytes << 3;
		for (int i = 0; i < 8; i++) {
			paddingBytes[paddingBytes.length - 8 + i] = (byte)messageLenBits;
			messageLenBits >>>= 8;
		}
	 
		int a = INIT_A;
		int b = INIT_B;
		int c = INIT_C;
		int d = INIT_D;
		int[] buffer = new int[16];
		for (int i = 0; i < numBlocks; i ++) {
			int index = i << 6;
			for (int j = 0; j < 64; j++, index++)
				buffer[j >>> 2] = ((int)((index < messageLenBytes) ? message[index] : paddingBytes[index - messageLenBytes]) << 24) | (buffer[j >>> 2] >>> 8);
			int originalA = a;
			int originalB = b;
			int originalC = c;
			int originalD = d;
			for (int j = 0; j < 64; j++) {
				int div16 = j >>> 4;
		      	int f = 0;
		      	int bufferIndex = j;
		      	switch (div16) {
		      		case 0:
		      			f = (b & c) | (~b & d);
		      			break;
		      		case 1:
		      			f = (b & d) | (c & ~d);
		      			bufferIndex = (bufferIndex * 5 + 1) & 0x0F;
		      			break;
		      		case 2:
		      			f = b ^ c ^ d;
		      			bufferIndex = (bufferIndex * 3 + 5) & 0x0F;
		      			break;
		      		case 3:
		      			f = c ^ (b | ~d);
		      			bufferIndex = (bufferIndex * 7) & 0x0F;
		      			break;
		      	}
		      	int temp = b + Integer.rotateLeft(a + f + buffer[bufferIndex] + TABLE_T[j], SHIFT_AMTS[(div16 << 2) | (j & 3)]);
		      	a = d;
		      	d = c;
		      	c = b;
		      	b = temp;
			}
			a += originalA;
			b += originalB;
			c += originalC;
			d += originalD;
		}
		byte[] md5 = new byte[16];
		int count = 0;
		for (int i = 0; i < 4; i++) {
			int n = (i == 0) ? a : ((i == 1) ? b : ((i == 2) ? c : d));
			for (int j = 0; j < 4; j++) {
				md5[count++] = (byte)n;
				n >>>= 8;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < md5.length; i++) {
			sb.append(String.format("%02X", md5[i] & 0xFF));
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		System.out.println(Arrays.toString(SplitByNumber("GS15UTT", 4)));
//		StringToBin("gs15");
//		System.out.println(calcMD5("test".getBytes()));
//		System.out.println(calcMD5("Test".getBytes()));
//		hexToBin(calcMD5("Test".getBytes()));
//		XOR("0001101111010001000110111101101010101001111111000001101000100010000110111101000100011011110110101010100111111100000110100010001000011011110100010001101111011010101010011111110000011010001000100001101111010001000110111101101010101001111111000001101000100010000110111101000100011011110110101010100111111100000110100010001000011011110100010001101111011010101010011111110000011010001000100001101111010001000110111101101010101001111111000001101000100010", "0000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000001");
//		add2pow64("0011011111001001011001011010100011010110110101111011111011000010", "1110");
		String[][] test = new String[][]{
			  {"0101010101010000011101001010101010101011001010101010100010101001", "0011011111001001011001011010100011010110110101111011111011000010"},
			  {"0100101101010101010100101010100101010011101101011010101011111011", "0000110110110110001100000110110000101010100011101001000110001011"}
			};
	}
}
