import java.util.Scanner;

// Classe qui comprend tous les algorithmes de chiffrage et de hashage

public class crypto {
	
	static String ThreeFish(String fileOriginalContent) {
		Scanner scanner = new Scanner(System.in);
		int menuInt = scanner.nextInt();
		// size : taille des blocs ; keynumb : nombre de mots de 64 bits
		int size = 256, keynumb = 4;
		String kinit, t0, t1;
		String c = "0001101111010001000110111101101010101001111111000001101000100010";
		//String fileOrigin = 
		
		System.out.println("Sélectionner la taille des blocs");
		System.out.println("1: 256 bits");
		System.out.println("2: 512 bits");
		System.out.println("3: 1024 bits");		
		
		//Choix de la taille de blocs/clés à utiliser
		switch(menuInt) {
			case 1 : 
				size = 256; keynumb = 4;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 256 bits.");
				break;
			case 2 : 
				size = 512;	keynumb = 8;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 512 bits.");
				break;
			case 3 : 
				size = 1024; keynumb = 16;
				System.out.println("Vous avez choisi une longueur de blocs/clés de 1024 bits.");
				break;
			default:
				System.out.println("Merci de saisir 256, 512 ou 1024");
				break;
		}
		
		//On récupère la clé ainsi que les deux tweaks
		kinit = utils.KeyGenerator(size)[0];
		t0 = utils.KeyGenerator(size)[1];
		t1 = utils.KeyGenerator(size)[2];
		
		System.out.println(kinit);
		System.out.println(t0);
		System.out.println(t1);
		
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
