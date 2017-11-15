import java.io.File;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		// Choix du fichier
		System.out.println("Ecrivez le nom du fichier sans l'extension !");
		String fileName = scanner.nextLine();
		
		// On r�cup�re le fichier
		File fichier = new File("files/" + fileName + ".txt");
		
		// On r�cup�re le contenu du fichier
		String fileContent = utils.ReadFile(fichier);
		
		System.out.println(fileContent);
		
		// choix de la m�thode de chiffrement
		System.out.println("S�lectionner votre fonction de chiffrement");
		System.out.println("1: Chiffrement sym�trique ThreeFish");
		System.out.println("2: Chiffrement de Cramer-Shoup");
		System.out.println("3: Hashage d'un message");
		System.out.println("4: D�chiffrement sym�trique ThreeFish");
		System.out.println("5: D�chiffrement Cramer-Shoup");
		System.out.println("6: V�rification d'un Hash");
		
		// On d�clare la variable qui contient le menu
		int menuInt = scanner.nextInt();
		
		System.out.println("Vous avez s�lectionner le menu : " + menuInt);
		
		// Variable qui contient le r�sultat de l'op�ration
		String newContentFile = "";
		
		switch(menuInt) {
			case 1 : 
				newContentFile = crypto.ThreeFish("");
				break;
			case 2 : 
				newContentFile = crypto.CramerShoup("");
				break;
			case 3 :
				newContentFile =  crypto.Hashage("");
				break;
			case 4 : 
				newContentFile =  crypto.DecThreeFish("");
				break;
			case 5 : 
				newContentFile =  crypto.DecCramerShoup("");
				break;
			case 6 : 
				newContentFile =  crypto.DecHashage("");
				break;
		}
		
		// On cr�� un nouveau fichier avec le r�sultat
		utils.CreateFile(fileName, newContentFile);
	}

}
