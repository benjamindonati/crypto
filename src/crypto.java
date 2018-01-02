import java.util.*;

// Classe qui comprend tous les algorithmes de chiffrage et de hashage

public class crypto {
	
	static String ThreeFish(String [] fileOriginalInfo, String fileOriginalContent) {
		Scanner scanner = new Scanner(System.in);
		// size : taille des blocs ; keynumb : nombre de mots de 64 bits
		int size = 256, keynumb = 4, pwdLength = 0;
		String kinit, t0, t1, kFile = "";
		String c = "0001101111010001000110111101101010101001111111000001101000100010";
		
		
		//Choix du mot de passe de chiffrement
		String pwd = "";
		do {
			System.out.println("Saisir un mot de passe de chiffrement :");
			pwd = scanner.nextLine();
			pwdLength = pwd.length();
			if (pwdLength < 4) {
				System.out.println("Ce mot de passe n'est pas assez long (4 caractères minimum)");
			}
		} while (pwdLength < 4);
		//System.out.println(pwd);
		String[] pwdChar = pwd.split("");
			
		//Choix de la taille de blocs/clés à utiliser
		System.out.println("Sélectionner la taille des blocs");
		System.out.println("1: 256 bits");
		System.out.println("2: 512 bits");
		System.out.println("3: 1024 bits");	
		
		int menu2Int = scanner.nextInt();
				
		switch(menu2Int) {
			case 1 : 
				size = 256; keynumb = 4;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 256 bits.");
//				Hashage du mdp :
//					- Hash du mdp entier
//					- Hash du 1er et dernier caractère
				String finalHash256 = utils.calcMD5(pwd.getBytes()) + utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes());
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key256" ;
				utils.CreateFile(kFile, finalHash256);				
				break;
			case 2 : 
				size = 512;	keynumb = 8;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 512 bits.");
//				Hashage du mdp :
//					- Idem 256 +
//					- Hash des deux premiers caractères
//					- Hash des deux derneiers caractères
				String finalHash512 = utils.calcMD5(pwd.getBytes()) +
									  utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes()) +
									  utils.calcMD5((pwdChar[0] + pwdChar[1]).getBytes()) +
									  utils.calcMD5((pwdChar[pwd.length()-2] + pwdChar[pwd.length()-1]).getBytes());
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key512" ;
				utils.CreateFile(kFile, finalHash512);					
				break;
			case 3 : 				
				size = 1024; keynumb = 16;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 1024 bits.");
//				Hashage du mdp :
//					- Idem 512 +
//					- Hash du 1er et avant-dernier caractère
//					- Hash du 1er et dernier caractère				
//					- Hash du 2nd et avant-dernier caractère
//					- Hash du 2er et dernier caractère
				String finalHash1024 = utils.calcMD5(pwd.getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[1]).getBytes()) +
									   utils.calcMD5((pwdChar[pwd.length()-2] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-2]).getBytes()) +
									   utils.calcMD5((pwdChar[0] + pwdChar[pwd.length()-1]).getBytes()) +
									   utils.calcMD5((pwdChar[1] + pwdChar[pwd.length()-2]).getBytes()) +
									   utils.calcMD5((pwdChar[1] + pwdChar[pwd.length()-1]).getBytes());				
//				Création du fichier dans lequel on stocke le hash du mdp
				kFile = fileOriginalInfo[0] + "_key1024" ;
				utils.CreateFile(kFile, finalHash1024);					
				break;
			default:
				System.out.println("Merci de saisir 256, 512 ou 1024");
				break;
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
