import java.io.File;
import java.io.IOException;
import java.util.*;

public class main {
	public static void main(String[] args) {				
		Scanner scanner = new Scanner(System.in);
		
		// choix de la m�thode de chiffrement
		System.out.println("\nS�lectionner l'action souhait�e :");
		System.out.println("1: Chiffrement sym�trique ThreeFish");
		System.out.println("2: Chiffrement de Cramer-Shoup");
		System.out.println("3: V�rification d'un Hash");
		System.out.println("4: D�chiffrement sym�trique ThreeFish");
		System.out.println("5: D�chiffrement Cramer-Shoup");
		// On d�clare la variable qui contient le menu
		int menuInt = scanner.nextInt();
		if (menuInt == 3) {
			crypto.Hashage();
			System.out.println("\nFin du programme.");
			return;
		}
		
		// Choix du fichier
		Scanner scanner2 = new Scanner(System.in);
		System.out.println("Ecrivez le nom du fichier avec l'extension !");
		String fileName = scanner2.nextLine();
		String[] fileInfo = fileName.split("\\.");	//[0] Nom 	[1] Extension
		//System.out.println(Arrays.toString(fileInfo));
		
		// On r�cup�re le fichier
		System.out.println("files/" + fileName);
		File fichier = new File("files/" + fileName);
		
		
		
		
		// On r�cup�re le contenu du fichier
		String fileContent = utils.ReadFile(fichier);
		//System.out.println(fileContent);
		
		System.out.println("Vous avez s�lectionner le menu : " + menuInt);
		
		// Variable qui contient le r�sultat de l'op�ration
		String newContentFile = "";
		
		switch(menuInt) {
			case 1 : 
				newContentFile = crypto.ThreeFish(fileInfo, fileContent);
				break;
			case 2 : 
				crypto.CramerShoup(fileContent);
				break;
			case 4 : 
				newContentFile =  crypto.DecThreeFish(fileInfo, fileContent);
				break;
			case 5 : 
				crypto.DecCramerShoup();
				break;
		}
		
		// On cr�� un nouveau fichier avec le r�sultat
		//utils.CreateFile(fileName, newContentFile);
	}

}
