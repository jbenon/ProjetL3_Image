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
	
	
	
	public static void main(String[] args) {
		
		// Mettre le path complet de l'image à étudier /!\
		BufferedImage imgBase = Internet.rotateClockwise90(Internet.chargerImage("/home/roxane/eclipse-workspace/ImageProjet/bdd/escaliers_droits_1.jpg"));	//---> CHARGER : DONE OK

		imgBase = Filtres.filtreSobelY(imgBase);				//---> SOBEL 3*3 : DONE OK
		imgBase = binarisationOtsu(imgBase);					//---> BINARISATION OTSU : DONE OK
		imgBase = Filtres.flouGaussien(imgBase);				//---> GAUSSIEN (5*5 pour l'instant) : DONE OK
		imgBase = Filtres.filtreMedian(imgBase);				//---> MEDIAN 3*3 : DONE OK	
		imgBase = Filtres.fermeture(imgBase);					//---> FERMETURE 7*7 : DONE OK
		
		try {
			Internet.afficherImage(imgBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] histProj = Histogrammes.histogrammeProjete(imgBase);				//---> HISTOGRAMME PROJETÉ : DONE OK
		int resultat = Histogrammes.compterMarches(histProj, imgBase);			//---> CALCUL NB MARCHES : DONE ?
		
		imgBase.flush();
		
		System.out.print("Nombre de marches : " + resultat);
						
	}
	
}
