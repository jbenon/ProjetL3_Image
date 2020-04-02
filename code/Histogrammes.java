package code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import code.Internet;

/**
 * Codes des histogrammes utilisés
 * 
 * @author Roxane Cellier
 * @version 1.0
 *
 */
public class Histogrammes {

	
	/**
	 * Crée l'histogramme des niveaux de gris d'une image contenue dans un BufferedImage
	 * entré en paramètre et l'affiche dans une image 255*255
	 * 
	 * @param img le BufferedImage contenant l'image à étudier
	 * @return les valeurs de l'histogramme
	 */
	public static int[] histogrammeGris(BufferedImage img) {
		/*
		 * Initialisation de l'image représentant l'histogramme
		 */
		BufferedImage hist = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_BINARY);
		
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		/*
		 * Tableau des valeurs de l'histogramme
		 */
		int[] valeursHist = new int[256];
		
		/*
		 * Initialisation à 0 de tous les niveaux de gris de l'image
		 */
		for(int i : valeursHist) {
			valeursHist[i] = 0;
		}
		
		/*
		 * Affectation des valeurs de l'histogramme à leurs niveau de gris
		 */
		for(int colonne = 0 ; colonne < largeur ; colonne++) {
			for(int ligne = 0 ; ligne < longueur ; ligne++) {
				int p = img.getRGB(colonne, ligne) & 0xFF;
				valeursHist[p] += 1;
			}
		}
		
		/*
		 * Représentation de l'histogramme dans une image noire
		 */
		for(int colonne = 1 ; colonne < 255 ; colonne++) {
			for (int x = 0; x < valeursHist[colonne]/500; x++) {
	            hist.setRGB(colonne, 255-x, Color.WHITE.getRGB());
	        }
		}
		
		/*
		 * Affichage des valeurs de l'histogramme
		 */
		for(int i = 0 ; i<valeursHist.length ; i++) {
			System.out.println(i + ":" + valeursHist[i]);
		}
		
		/*
		 * Affichage de l'image représentant l'histogramme
		 */
		try {
			Internet.afficherImage(hist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return valeursHist;
	}


	/**
	 * Crée l'histogramme projeté des lignes d'une image et l'affuche dans une image de même
	 * dimension que l'image de base
	 * 
	 * @param img le BufferedImage contenant l'image à étudier
	 * @return les valeurs par lignes de l'histogramme
	 */
	public static int[] histogrammeProjete(BufferedImage img) {
		/*
		 * Initialisation de l'image représentant l'histogramme
		 */		
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage hist = new BufferedImage(largeur, longueur, BufferedImage.TYPE_BYTE_BINARY);
		
		/*
		 * Tableau des valeurs de l'histogramme
		 */
		int[] valeursHist = new int[longueur];
		
		/*
		 * Initialisation à 0 de tous les niveaux de gris de l'image
		 */
		for(int i : valeursHist) {
			valeursHist[i] = 0;
		}
		
		/*
		 * Affectation des valeurs de l'histogramme
		 */
		for(int colonne = 0 ; colonne < largeur ; colonne++) {
			for(int ligne = 0 ; ligne < longueur ; ligne++) {
				/*
				 * Si le pixel est allumé, la valeur
				 * 
				 */
				if (img.getRGB(colonne, ligne) == Color.white.getRGB()) {
					valeursHist[ligne] += 1;
				}
			}
		}
		
		/*
		 * Représentation de l'histogramme dans une image noire
		 */
		for(int ligne = 1 ; ligne < valeursHist.length - 1 ; ligne++) {
			for (int x = 0; x < valeursHist[ligne]; x++) {
	            hist.setRGB(x, ligne, Color.WHITE.getRGB());
	        }
		}

		/*
		 * Affichage des valeurs de l'histogramme
		 */
		for(int i = 0 ; i<valeursHist.length ; i++) {
			System.out.println(i + ":" + valeursHist[i]);
		}
		
		/*
		 * Affichage de l'image représentant l'histogramme
		 */
		try {
			Internet.afficherImage(hist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return valeursHist;
	}
	
}
