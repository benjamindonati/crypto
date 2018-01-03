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
		// Récupération de la date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date date = new Date();
		//System.out.println(dateFormat.format(date));
		
		//Récupération des données
		byte[] data = fileContent.getBytes(StandardCharsets.UTF_8);
		
		// Création du fichier
		Path file = Paths.get("files/" + fileName + "_" + dateFormat.format(date) + ".txt");
		try {
			Files.write(file, data);
			System.out.println(fileName + "_" + dateFormat.format(date) + ".txt créé.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

//	Casse une chaîne en plusieurs (longueur des sous_chaînes en paramètre)
	static String[] SplitByNumber(String str, int size) {
		String[] tab = str.split("(?<=\\G.{"+size+"})");
		String toAdd = "";
//		Si la dernière chaîne est plus courte, on comble avec des 0		
		if (tab[tab.length-1].length() != size) {
			for (int i = tab[tab.length-1].length()-1; i < size-1; i++) {
				toAdd += "0";
			}
		}
		tab[tab.length-1] += toAdd;
		return tab;
	}
	
//	Convertit une chaîne en binaire (table ascii)
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

//	Convertit un hexadécimal (passé en String) en binaire
	static String hexToBin(String s) {
		String bin = String.format("%128s", new BigInteger(s, 16).toString(2)).replace(" ", "0");
//		System.out.println(bin);
		return bin;
	}
	
//	Renvoie le résultat du XOR de 2 binaires
	static String xor(String num1, String num2) {
		StringBuilder sb = new StringBuilder();
		try{
			for (int i = 0; i < num1.length(); i++) {
		    	sb.append(((num1.charAt(i) == '1') ? '1' : '0') ^ ((num2.charAt(i) == '1') ? '1' : '0'));
		    }
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}	    
//	    System.out.println(sb.toString());
	    return sb.toString();
	}
	
	
//	Addition binaire tronquée au 64ème bit (inclut) 
	static String add2pow64 (String num1, String num2) {
		System.out.println(num1);
		System.out.println(String.format("%0$64s", num2));
		BigInteger number0 = new BigInteger(num1, 2);
		BigInteger number1 = new BigInteger(num2, 2);
		String result = String.format("%0$64s", number0.add(number1).toString(2)).replace(" ", "0");
//		System.out.println(result);
//		System.out.println(String.format("%0$64s", result).replace(" ", "0"));
		if (result.length() > 64) {
			return result.substring(1, 65);
		} else {
			return result;
		}
		
	}
	
//	Fonction de hashage utilisée : MD5
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
//		xor("010", "111");
		add2pow64("0011011111001001011001011010100011010110110101111011111011000010", "1110");
	}
}
