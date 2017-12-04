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
        
		BigInteger k = new BigInteger((size-128), new Random());
		BigInteger t0 = new BigInteger((64), new Random());
		BigInteger t1 = new BigInteger((64), new Random());
		
		System.out.println("Clé initiale (" + size + " bits) :");
		System.out.println("BIN key " + k.toString(2) + " t0 " + t0.toString(2) + " t1 " + t1.toString(2));
		System.out.println("DEC key " + k.toString() + " t0 " + t0.toString() + " t1" + t1.toString());
		System.out.println("HEX key " + k.toString(16) + " t0 " + t0.toString(16) + " t1 " + t0.toString(16) + "\n");
		
		
		tab[0] = k.toString(2);
		tab[1] = t0.toString(2);
		tab[2] = t1.toString(2);
		return tab;
	}

	public static void main(String[] args) {
		KeyGenerator(256);
	}
}
