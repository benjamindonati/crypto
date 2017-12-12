import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class utils {
	
	static String ReadFile(File fileName) {
		String fileContent = "";
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				fileContent+=ligne+"\n";
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return fileContent;
	}
	
	static void CreateFile(String fileName, String fileContent) {
		// Récupération de la date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		//Récupération des données
		byte[] data = fileContent.getBytes(StandardCharsets.UTF_8);
		
		// Création du fichier
		Path file = Paths.get("files/" + fileName + "_" + dateFormat.format(date) + ".txt");
		try {
			Files.write(file, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Retourne une chaîne de bits dont la longueur est définie en paramètre
	static String[] KeyGenerator(int size) {
		String[] tab;
        tab = new String[3];
		String kstr2 = null, t0str2 = null, t1str2 = null;
		String kstr16 = null, t0str16 = null, t1str16 = null;
        
		//On créé nos nombres aléatoires (clé + tweaks)
		BigInteger k = new BigInteger((size-128), new Random());
		BigInteger t0 = new BigInteger((64), new Random());
		BigInteger t1 = new BigInteger((64), new Random());
		
		//On formatte en binaire en conservant les 0 de début de chaîne
		switch(size) {
			case 256 : 
				kstr2 = String.format("%128s", k.toString(2)).replace(' ', '0');
				kstr16 = String.format("%32s", k.toString(16)).replace(' ', '0');
				break;
			case 512 : 
				kstr2 = String.format("%384s", k.toString(2)).replace(' ', '0');
				kstr16 = String.format("%96s", k.toString(16)).replace(' ', '0');
				break;
			case 1024 : 
				kstr2 = String.format("%896s", k.toString(2)).replace(' ', '0');
				kstr16 = String.format("%224s", k.toString(16)).replace(' ', '0');
				break;
			default:
				break;
		}
		t0str2 = String.format("%64s", t0.toString(2)).replace(' ', '0');
		t1str2 = String.format("%64s", t1.toString(2)).replace(' ', '0');
		t0str16 = String.format("%16s", t0.toString(16)).replace(' ', '0');
		t1str16 = String.format("%16s", t1.toString(16)).replace(' ', '0');
		
		tab[0] = kstr2 + t0str2 + t1str2;
		tab[1] = t0str2 ;
		tab[2] = t1str2 ;
		
		//Affichage en binaire, décimal et hexadécimal (optionnel)
		System.out.println("Clé initiale (" + size + " bits) :");
		System.out.println("BIN key " + tab[0] + "\n    t0  " + tab[1] + "\n    t1  " + tab[2]);
		System.out.println("DEC key " + k.toString() + t0.toString() + t1.toString() + "\n    t0  " + t0.toString() + "\n    t1  " + t1.toString());
		System.out.println("HEX key " + kstr16 + t0str16 + t1str16 + "\n    t0  " + t0str16 + "\n    t1  " + t1str16);
				
		return tab;
	}

	//Casse une chaîne en plusieurs (longeur en paramètre)
	static String[] SplitByNumber(String str, int size) {
		String[] tab = str.split("(?<=\\G.{"+size+"})");
		return tab;
	}

	//Convertit une chaîne de charactère en une chaîne de binaire
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
		   //binary.append(' ');
		}
		System.out.println("'" + text + "' = " + binary);
		return binary.toString();
	}
	
	public static void main(String[] args) {
		//KeyGenerator(256);
		//System.out.println(Arrays.toString(SplitByNumber("Ceci est ~une (courte) phrase.", 4)));
		//StringToBin("GS15");
	}
}
