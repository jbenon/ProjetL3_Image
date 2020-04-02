package code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import code.Histogrammes;
import code.Filtres;
import code.Internet;


/**
 * Traite une photo pour compter le nombre de marches d'un escalier
 * 
 * @author Roxane Cellier
 * @version 0.8
 *
 */
public class Methode5 {
	
	/**
	* Binarise l'image selon la méthode Otsu
	*
	* @param img L'image à binariser
	*/
	public static void binarisationOtsu(BufferedImage img){

	}	
	
	//EN ATTENDANT LA OTSU
	public static BufferedImage binairiser(BufferedImage img, int seuil) {

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

	
	
	
	public static void main(String[] args) {
		
		BufferedImage imgBase = Internet.rotateClockwise90(Internet.chargerImage("/bdd/escaliers_droits_2.jpg"));	//---> CHARGER : DONE OK

		imgBase = Filtres.filtreSobelY(imgBase);				//---> SOBEL 3*3 : DONE OK
		imgBase = binairiser(imgBase,170);					// BINARISATION OTSU (en attendant, binarisation simple)
		imgBase = Filtres.flouGaussien(imgBase);				//---> GAUSSIEN (5*5 pour l'instant) : DONE OK		
		imgBase = Filtres.filtreMedian(imgBase);				//---> MEDIAN 3*3 : DONE OK		
		imgBase = Filtres.fermeture(imgBase);					//---> FERMETURE 7*7 : DONE midOK if binaire
		
		Histogrammes.histogrammeProjete(imgBase);
		
		try {
			Internet.afficherImage(imgBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
}
