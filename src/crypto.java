import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.math.BigInteger;

// Classe qui comprend tous les algorithmes de chiffrage et de hashage

public class crypto {

	// Nombres permettant de générer les clés de CramerShoup
	private static BigInteger c,d,h,x1,x2,y1,y2,z;

	// Générateurs du groupe 
	private static BigInteger g1,g2,hg1,hg2;

	// Facteurs premiers de (p-1)
	private static BigInteger[] pMinus1factorization;


	// Exposants des facteurs premiers de (p-1)
	private static int[] pMinus1factorizationExponents;

	// Variables de gestion des nb premiers
	private static BigInteger primeP;
	private static int nbits = -1;
	static int primePointer;
	static Vector primes;
	static long[] initialPrimes = {2, 3, 5, 7, 11, 13, 17, 19, 23};

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

		// On commence par générer les clés de chiffrement et déchiffrement
		// On enregistre également les clés dans un fichier
		GenerateKeysCS();

		// Ensuite on chiffre le fichier avec la clé publique

		// On retourne un fichier chiffré
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

	// Cette méthode génère les clés pour CramerShoup
	static void GenerateKeysCS() {

		System.out.println("Génération des clés...");

		// On génère q (grand nb premier)
		Random rnd = new Random();
		// 1024 étant la longueur et 100 l'indice de confiance
		BigInteger primeQ = new BigInteger(1024, 100, rnd);
		// On vérifie qu'il soit premier
		while(
				(primeQ.mod(BigInteger.valueOf(3))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(5))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(7))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(9))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(11))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(13))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(17))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(19))).equals(BigInteger.ZERO) ||
				(primeQ.mod(BigInteger.valueOf(23))).equals(BigInteger.ZERO)
				){
			primeQ = new BigInteger(1024, 100, rnd);        
		}
		System.out.println("P : " +primeQ);
		System.out.println("Size : " +primeQ.bitLength());

		// On doit maintenant générer un deuxième nombre premier p compatible avec q
		Vector<BigInteger> nbPotentiels = new Vector<BigInteger>();
		nbPotentiels.addElement((primeQ.multiply(BigInteger.valueOf(2))).add(BigInteger.ONE));

		BigInteger numberP = primeQ.mod(BigInteger.valueOf(2310));

		BigInteger shift;
		BigInteger bigI;
		int i;

		for(i=2; i < 2310; i++){
			bigI = (new BigInteger((new Integer(i)).toString()));
			shift = (numberP.multiply(bigI));
			if(
					((shift.mod(BigInteger.valueOf(3))).equals(BigInteger.ONE)) ||
					((shift.mod(BigInteger.valueOf(5))).equals(BigInteger.valueOf(2))) ||
					((shift.mod(BigInteger.valueOf(7))).equals(BigInteger.valueOf(3))) ||
					((shift.mod(BigInteger.valueOf(11))).equals(BigInteger.valueOf(5)))
					){
			}
			else {
				nbPotentiels.addElement(((primeQ.multiply(bigI)).multiply(BigInteger.valueOf(2))).subtract((BigInteger)(nbPotentiels.elementAt(nbPotentiels.size() - 1))));
			}
		}

		System.out.println("Taille du vecteur (max 2310): " + nbPotentiels.size());

		BigInteger longJ = BigInteger.ONE;
		boolean notfound = true;

		while(notfound){
			System.out.print("=");

			numberP = ((longJ.multiply(primeQ)).multiply(BigInteger.valueOf(2))).add(BigInteger.ONE);
			longJ = longJ.add(BigInteger.valueOf(2310));

			i = 1;

			while(i < nbPotentiels.size()){
				if(numberP.isProbablePrime(100)){
					notfound = false;
					i = nbPotentiels.size();
				}
				else {
					numberP = numberP.add((BigInteger)nbPotentiels.elementAt(i++));
					System.out.print("+");
				}
			}
		}

		System.out.println("");
		System.out.println("p = " + numberP.toString());
		System.out.println("p = " + numberP.bitLength());
		System.out.println("q = " + primeQ.toString());
		System.out.println("p = " + primeQ.bitLength());

		nbits = numberP.bitLength();

		primeP = numberP;


		// On factorise p-1 pour vérifier qu'il n'est pas friable
		initPrimes();
		primes = new Vector();

		for(i = 0; i < initialPrimes.length; i++){
			primes.addElement(new BigInteger((new Long(initialPrimes[i])).toString()));
		}

		BigInteger bigN = (numberP.subtract(BigInteger.ONE)).divide(primeQ);

		Vector pMinus1Factors = new Vector();
		Vector pMinus1FactorMultiplicity = new Vector();

		pMinus1Factors.addElement(primeQ);
		pMinus1FactorMultiplicity.addElement(new Integer(1));

		BigInteger pp = nextSmallestPrime();

		while(!bigN.equals(BigInteger.ONE)){
			if((bigN.mod(pp)).equals(BigInteger.ZERO)){
				int j = 1;
				bigN = bigN.divide(pp);
				while( (bigN.mod(pp)).equals(BigInteger.ZERO) ){
					bigN = bigN.divide(pp);
					j++;
				}
				pMinus1Factors.addElement(pp);
				pMinus1FactorMultiplicity.addElement((new Integer(j)));
				System.out.print("/");
			}
			pp = nextSmallestPrime();
		}
		System.out.println();

		pMinus1factorization = new BigInteger[pMinus1Factors.size()];
		pMinus1factorizationExponents = new int[pMinus1Factors.size()];

		for(i = 0; i < pMinus1Factors.size(); i++){
			pMinus1factorization[i] = (BigInteger)pMinus1Factors.elementAt(i);
			pMinus1factorizationExponents[i] = ((Integer)pMinus1FactorMultiplicity.elementAt(i)).intValue();
		}

		// On créé les générateurs de q et p
		g1 = projectDown(primel(), primeP);
		do {
			System.out.print("\n>> Guessing g2 ");
			g2 = projectDown(primel(), primeP);
		} while (g2.equals(g1));
		// On s'assure que les générateurs soient bien différents
		do {
			System.out.print("\n>> Guessing hg1 ");
			hg1 = projectDown(primel(), primeP);
		} while (hg1.equals(g1) || hg1.equals(g2));
		do {
			System.out.print("\n>> Guessing hg2 ");
			hg2 = projectDown(primel(), primeP);
		} while (hg2.equals(g1) || hg2.equals(g2) || hg2.equals(hg1) );
		
		System.out.println("-----------------------------------------------------------");
		System.out.println(g1);
		System.out.println(g1.bitLength());
		System.out.println(g2);
		System.out.println(g2.bitLength());
		System.out.println(hg1);
		System.out.println(hg1.bitLength());
		System.out.println(hg2);
		System.out.println(hg2.bitLength());
		
		// On calcule les variables qui servent à générer les clés
		
		Random rnd2 = new Random();

	    do {  x1 = (new BigInteger(nbits, rnd2)).mod(primeP); }
	    	while(x1.equals(BigInteger.ZERO));
	    do {  x2 = (new BigInteger(nbits, rnd2)).mod(primeP); }
	    	while(x2.equals(BigInteger.ZERO));
	    do {  y1 = (new BigInteger(nbits, rnd2)).mod(primeP); }
	    	while(y1.equals(BigInteger.ZERO));
	    do {  y2 = (new BigInteger(nbits, rnd2)).mod(primeP); }
	    	while(y2.equals(BigInteger.ZERO));
	    do {  z  = (new BigInteger(nbits, rnd2)).mod(primeP); }
	    	while(z.equals(BigInteger.ZERO));
	    
		c = ((g1.modPow(x1,primeP)).multiply(g2.modPow(x2,primeP))).mod(primeP);
	    d = ((g1.modPow(y1,primeP)).multiply(g2.modPow(y2,primeP))).mod(primeP);

	    h = g1.modPow(z, primeP);
	    
	    System.out.println(c);
	    System.out.println(d);
	    System.out.println(h);
	}	

	private static void initPrimes(){
		primePointer = 0;
	}

	private static BigInteger nextSmallestPrime(){
		if(primePointer < primes.size() )
			return (BigInteger)primes.elementAt(primePointer++);
		else {
			BigInteger newPrime = ((BigInteger)primes.lastElement()).add(BigInteger.valueOf(2));
			int i = 1;
			while(-1 == ((((BigInteger)primes.elementAt(i-1)).multiply((BigInteger)primes.elementAt(i-1))).compareTo(newPrime))){
				if(newPrime.mod((BigInteger)primes.elementAt(i)).equals(BigInteger.ZERO)){
					newPrime = newPrime.add(BigInteger.valueOf(2));
					System.out.print("#");
					i = 1;
				}
				else {
					i++;
				}
			}

			System.out.print(newPrime.toString());
			primes.addElement(newPrime);

			return newPrime;
		}
	}

	// Calcule l'ordre d'un élément
	private static BigInteger ordInG(BigInteger x){

		BigInteger ord = primeP.subtract(BigInteger.ONE);
		int j = 0;

		for(int i = 0; i < pMinus1factorization.length; i++){
			System.out.print("*");
			for(j = 1; j < pMinus1factorizationExponents[i]; j++){
				if(x.modPow((ord.divide(pMinus1factorization[i])),primeP).equals(BigInteger.ONE)
						){
					System.out.print("@");
					ord = ord.divide(pMinus1factorization[i]);
				}
			}
			if(x.modPow(ord.divide(pMinus1factorization[i]),primeP).equals(BigInteger.ONE)){
				ord = ord.divide(pMinus1factorization[i]);
			}
		}
		return ord;
	}
	private static BigInteger primel(){

		try {

			BigInteger p1 = primeP.subtract(BigInteger.ONE);
			BigInteger p3 = primeP.subtract(BigInteger.valueOf(3));
			BigInteger prim =
					((new BigInteger(nbits, new Random())).mod(p3)).add(BigInteger.valueOf(2));
			BigInteger ord = ordInG(prim);
			BigInteger y;
			BigInteger ordy;
			BigInteger c;
			BigInteger s;
			BigInteger ss;


			System.out.print("--ok--");
			while(-1 == ord.compareTo(primeP.subtract(BigInteger.ONE))){
				y = ((new BigInteger(nbits, new Random())).mod(p3)).add(BigInteger.valueOf(2));
				ordy = ordInG(y);
				c = ordy.gcd(ord);
				s = ord.mod(ordy.divide(c));

				if(ordy.equals(p1)){
					prim = y;
					ord = ordy;
				}
				else {

					if ( (-1 == c.compareTo(ordy)) &&
							(-1 == (BigInteger.ZERO).compareTo(s))
							){
						ss = ordy.mod(ord.divide(c));
						ss = ss.divide(ss.gcd(s));
						prim = prim.modPow(ord.divide(s),
								primeP);
						prim = (prim.multiply(y.modPow(ordy.divide(ss),
								primeP))
								).mod(primeP);
					}
				}
			}
			return prim;
		}
		catch (java.lang.ArithmeticException e){
			// Il peut y avoir des divisions par 0, il faut donc refaire le calcul
			return primel();
		}
	}

	// On choisit un générateur du bas
	private static BigInteger projectDown(BigInteger gen, BigInteger p){
		if(-1 == (p.divide(BigInteger.valueOf(2))).compareTo(gen))
			return p.subtract(gen);
		else
			return gen;
	}
}
