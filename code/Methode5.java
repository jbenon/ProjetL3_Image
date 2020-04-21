package code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import code.Histogrammes;
import code.Filtres;
import code.Internet;


/**
 * Traite une photo pour compter le nombre de marches d'un escalier
 * 
 * @author Roxane Cellier
 * @version 2.0
 *
 */
public class Methode5 {
	
	/**
	* Binarise l'image selon la méthode Otsu
	*
	* @param img L'image à binariser
	* @return L'image binarisée
	* 
	* @author Juliette/Nicolas
	*/
	public static BufferedImage binarisationOtsu(BufferedImage img){
		int[] histo = Histogrammes.histogrammeGris(img);
		
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		double nbPixels = largeur * longueur;
		
		double[] w1 = new double[256];
		double[] w2 = new double[256];
		double[] mu1 = new double[256];
		double[] mu2 = new double[256];
		double[] sig = new double[256];
		
		for(int i = 0 ; i<256 ; i++) {
			w1[i] = Arrays.stream(histo, 0, i+1).sum()/nbPixels;
			w2[i] = Arrays.stream(histo, i+1, 256).sum()/nbPixels;
			
			int nbPixelsClasse1 = 0;
			int nbPixelsClasse2 = 0;
			
			/*
			 * Mise à jour des moyennes
			 */
			for(int j = 0 ; j < i ; j++) {
				nbPixelsClasse1 += histo[j];
				mu1[i] += j*histo[j]; 
			}
			if (nbPixelsClasse1 != 0) {
				mu1[i] /= nbPixelsClasse1;
			}
			
			for(int j = i ; j < 256 ; j++) {
				nbPixelsClasse2 += histo[j];
				mu2[i] += j*histo[j]; 
			}
			if (nbPixelsClasse2 != 0) {
				mu2[i] /= nbPixelsClasse2;
			}
			
			/*
			 * Mise à jour des variances
			 */
			sig[i] = (w1[i] * w2[i] * Math.pow(mu1[i] - mu2[i], 2));
		}
		
		int seuil = indiceMax(sig);
				
		BufferedImage binaire = binairiser(img, seuil);
		return binaire;
	}	
	
	/**
	 * Normalise une image
	 * 
	 * @param img L'image à normaliser
	 * @return L'image normalisée en niveau de gris
	 * 
	 * @author Juliette/Nicolas
	 */
	public static BufferedImage normalisation(BufferedImage img) {
		/*
		 * Transforme l'image en niveau de gris
		 */
		niveauGris(img);
		
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage resultat = new BufferedImage(largeur, longueur, BufferedImage.TYPE_4BYTE_ABGR);
		
		int[] hist = Histogrammes.histogrammeGris(img);
		int x = minMod(hist);
		int y = maxMod(hist);
		
		for(int colonne = 0; colonne<largeur ; colonne++) {
			for(int ligne = 0 ; ligne<longueur ; ligne++) {
				int p = img.getRGB(colonne, ligne);
				resultat.setRGB(colonne, ligne, (255*(p-x)/(y-x)));
			}
		}
		
		return resultat;
	}
		
	
	//METHODES PRIVÉES UTILISÉES
	/**
	 * Binarise une image en fonction d'un seuil. Les pixels plus clairs que le seuil
	 * deviennent blancs, et les plus foncés deviennent noirs
	 * 
	 * @param img L'image à binariser
	 * @param seuil Le seuil de binarisation
	 * @return L'image binarisée
	 */
	private static BufferedImage binairiser(BufferedImage img, int seuil) {

		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage resultat = new BufferedImage(largeur, longueur, BufferedImage.TYPE_4BYTE_ABGR);
		
		for(int colonne = 0; colonne < largeur ; colonne++) {
			for(int ligne = 0 ; ligne < longueur ; ligne++) {
				int p = img.getRGB(colonne, ligne);
				if ((p & 0xFF) < seuil) {
					resultat.setRGB(colonne,ligne, Color.black.getRGB());
				} else {
					resultat.setRGB(colonne,ligne, Color.white.getRGB());
				}
			}
		}
		return resultat;
	}
	
	/**
	 * Renvoie l'indice de la valeur maximale d'un tableau d'entier
	 * 
	 * @param tab Le tableau à étudier
	 * @return L'indice de la valeur maximale
	 */
	private static int indiceMax(double[] tab) {
		int iMax = 0;
		
		for(int i = 0 ; i < tab.length ; i++) {
			if (tab[i] > tab[iMax]) {
				iMax = i;
			}
		}
		
		return iMax;
	}
	
	/**
	 * Permet de transformer une image couleur en image de niveaux de gris
	 * 
	 * @param img L'image à modifier
	 */
	private static void niveauGris(BufferedImage img) {
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		/*
		 * Parcours de l'image
		 */
		for(int colonne = 0 ; colonne < largeur ; colonne++) {
			for(int ligne = 0 ; ligne < longueur ; ligne++) {
				int p = img.getRGB(colonne, ligne);
				
				/*
				 * Récuperation des valeurs RGB
				 */
				int rouge = (p >> 16) & 0xff;
				int vert = (p >> 8) & 0xff;
				int bleu = p  & 0xff;
				
				int a = (rouge+vert+bleu)/3;
				
				/*
				 * Reaffectation comme niveau de gris
				 */
				img.setRGB(colonne, ligne, (0xff<<24) | (a<<16) | (a<<8) | a);
			}
		}
	}
	
	/**
	 * Trouve l'indice de la première valeur différente de 0 d'un tableau
	 * 
	 * @param liste La liste à étudier
	 * @return L'indice de la première valeur différente de 0
	 */
	private static int minMod(int[] tab) {
		int i = 0;
		/*
		 * On parcourt la liste dans l'ordre croissant
		 */
		while (tab[i] == 0) {
			i++;
		}
		
		return i; 
	}
	
	/**
	 * Trouve l'indice de la dernière valeur différente de 0 d'un tableau
	 * 
	 * @param liste La liste à étudier
	 * @return L'indice de la dernière valeur différente de 0
	 */
	private static int maxMod(int[] tab) {
		int i = tab.length - 1;
		/*
		 * On parcourt la liste dans l'ordre décroissant
		 */
		while (tab[i] == 0) {
			i--;
		}
		
		return i; 
	}
	
	
	
	
	public static void main(String[] args) {
		
		// Mettre le path complet de l'image à étudier /!\
		BufferedImage imgBase = Internet.rotateClockwise90(Internet.chargerImage("/bdd/escaliers_droits_2.jpg"));	//---> CHARGER : DONE OK

		imgBase = normalisation(imgBase);						//---> NORMALISATION : DONE NOt?OK
		imgBase = Filtres.filtreSobelY(imgBase);				//---> SOBEL 3*3 : DONE OK
		imgBase = binarisationOtsu(imgBase);					//---> BINARISATION OTSU : DONE OK
		imgBase = Filtres.flouGaussien(imgBase);				//---> GAUSSIEN (5*5 pour l'instant) : DONE OK		
		imgBase = Filtres.filtreMedian(imgBase);				//---> MEDIAN 3*3 : DONE OK		
		imgBase = Filtres.fermeture(imgBase);					//---> FERMETURE 7*7 : DONE OK
		
		int[] histProj = Histogrammes.histogrammeProjete(imgBase);				//---> HISTOGRAMME PROJETÉ : DONE OK
		int resultat = Histogrammes.compterMarches(histProj, imgBase);			//---> CALCUL NB MARCHES : DONE ?
		
		System.out.print(resultat);
		
		
		try {
			Internet.afficherImage(imgBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
}
