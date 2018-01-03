import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

// Classe qui comprend tous les algorithmes de chiffrage et de hashage

public class crypto {
	
	static String ThreeFish(String [] fileOriginalInfo, String fileOriginalContent) {
		Scanner scanner = new Scanner(System.in);
//		size : taille des blocs ; N : nombre de mots de 64 bits
		int size = 256, N = 4, pwdLength = 0;
		String kinit = "", t2 = "", kFile = "", kn1 = "0000000000000000000000000000000000000000000000000000000000000000";
		String[] fileSplittedContent = {};
		String c = "0001101111010001000110111101101010101001111111000001101000100010";
		
		
//		Choix du mot de passe de chiffrement
		String pwd = "";
		do {
			System.out.println("Saisir un mot de passe de chiffrement :");
			pwd = scanner.nextLine();
			pwdLength = pwd.length();
			if (pwdLength < 4) {
				System.out.println("Ce mot de passe n'est pas assez long (4 caractères minimum)");
			}
		} while (pwdLength < 4);
//		System.out.println(pwd);
		String[] pwdChar = pwd.split("");
			
//		Choix de la taille de blocs/clés à utiliser
		System.out.println("Sélectionner la taille des blocs");
		System.out.println("1: 256 bits");
		System.out.println("2: 512 bits");
		System.out.println("3: 1024 bits");	
		
		int menu2Int = scanner.nextInt();
				
		switch(menu2Int) {
			case 1 : 
				size = 256; N = 4;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 256 bits.");
//				Hashage du mdp :
//					Hash du mdp entier
//					+ Hash du 1er et du dernier caractère
				String finalHash256 = utils.calcMD5(pwd.getBytes()) +
									  utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes());
				kinit = finalHash256;
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key256" ;
				utils.CreateFile(kFile, finalHash256);
//				Découpage du contenu du fichier en blocs de 256 bits
				fileSplittedContent = utils.SplitByNumber(utils.StringToBin(fileOriginalContent), 256);
				break;
			case 2 : 
				size = 512;	N = 8;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 512 bits.");
//				Hashage du mdp :
//					Idem 256 +
//					+ Hash des deux premiers caractères
//					+ Hash des deux derniers caractères
				String finalHash512 = utils.calcMD5(pwd.getBytes()) +
									  utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes()) +
									  utils.calcMD5((pwdChar[0] + pwdChar[1]).getBytes()) +
									  utils.calcMD5((pwdChar[pwd.length()-2] + pwdChar[pwd.length()-1]).getBytes());
				kinit = finalHash512;
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key512" ;
				utils.CreateFile(kFile, finalHash512);	
//				Découpage du contenu du fichier en blocs de 256 bits
				fileSplittedContent = utils.SplitByNumber(utils.StringToBin(fileOriginalContent), 512);				
				break;
			case 3 : 				
				size = 1024; N = 16;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 1024 bits.");
//				Hashage du mdp :
//					Idem 512 +
//					+ Hash du 1er et avant-dernier caractère
//					+ Hash du 2ème et dernier caractère
//					+ Hash des 3 premiers caractères			
//					+ Hash des 3 derniers caractères
				String finalHash1024 = utils.calcMD5(pwd.getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[1]).getBytes()) +
									   utils.calcMD5((pwdChar[pwd.length()-2] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-2]).getBytes()) +
									   utils.calcMD5((pwdChar[1] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[1] + pwdChar[2]).getBytes()) +
				   					   utils.calcMD5((pwdChar[1] + pwdChar[pwd.length()-3] + pwdChar[pwd.length()-2] + pwdChar[pwd.length()-1]).getBytes());				
				kinit = finalHash1024;
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key1024" ;
				utils.CreateFile(kFile, finalHash1024);	
//				Découpage du contenu du fichier en blocs de 256 bits
				fileSplittedContent = utils.SplitByNumber(utils.StringToBin(fileOriginalContent), 1024);				
				break;
			default:
				System.out.println("Merci de saisir 256, 512 ou 1024");
				break;
		}		

		kinit = utils.hexToBin(kinit);		
		String[] Ktemp = utils.SplitByNumber(kinit, 64);
		
//		Ajout de la constante C
		for (String e : Ktemp){
			kn1 = utils.xor(kn1, e);
		}
		kn1 = utils.xor(kn1, c);
		System.out.println(kn1);
		
		kinit += kn1;
		String[] K = utils.SplitByNumber(kinit, 64);
		System.out.println(Arrays.toString(K) + "\n");
		
//		Tweaks
		String[] tweaks = {K[0], K[1], utils.xor(K[0], K[1])};
		
//		Calculs des clés de tournées
		int i, j;
		String[][] roundKeys = new String[20][N+1];
		for (i = 0; i < 20; i++) {
			for (j = 0; j <= N-4; j++) {
				System.out.println(i + " " + j);
				roundKeys[i][j] = K[(i+j)%(N+1)];
				System.out.println(roundKeys[i][j]);
			}
			if (j == N-3) {
				System.out.println(i + " " + j);
				roundKeys[i][j] = utils.add2pow64(K[(i+j)%(N+1)], tweaks[i%3]) ;
				System.out.println(roundKeys[i][j]);
				j++;
			}
			if (j == N-2) {
				System.out.println(i + " " + j);
				roundKeys[i][j] = utils.add2pow64(K[(i+j)%(N+1)], tweaks[(i+1)%3]) ;
				System.out.println(roundKeys[i][j]);
				j++;
			}
			if (j == N-1) {
				System.out.println(i + " " + j);
				roundKeys[i][j] = utils.add2pow64(K[(i+j)%(N+1)], Integer.toBinaryString(i)) ;
				System.out.println(roundKeys[i][j]);
			}
		}			
		return "";
	}
	
	static String DecThreeFish(String fichier) {
		System.out.println("Déchiffrement de ThreeFish en cours...");
		return "";
	}
	
	static String CramerShoup(String fichier) {
		System.out.println("Chiffrement de CramerShoup en cours...");
		return "";
	}
	
	static String DecCramerShoup(String fichier) {
		System.out.println("Déchiffrement de ThreeFish en cours...");
		return "";
	}
	
	static String Hashage(String fichier) {
		System.out.println("Hashage en cours...");
		return "";
	}
	
	static String DecHashage(String fichier) {
		System.out.println("Vérification du Hash en cours...");
		return "";
	}
	
}
