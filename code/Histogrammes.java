package code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import code.Internet;

/**
 * Codes des histogrammes utilisés et de leurs analyses
 * 
 * @author Roxane Cellier
 * @version 4.0
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
		
		return valeursHist;
	}

	/**
	 * Crée l'histogramme projeté des lignes d'une image et l'affiche dans une image de même
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
				 * est incrémentée
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
			System.out.println(valeursHist[i]);
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
		
		hist.flush();
		
		return valeursHist;
	}
	
	
	
	/**
	 * Compte le nombre de marches en fonction de l'histogramme
	 * 
	 * @param histo Le tableau de l'histogramme projeté
	 * @param z La largeur de l'image de base
	 * 
	 * @author Juliette/Nicolas
	 */
	public static int compterMarches(int[] histo, int z) {
		// Méthode pics du pseudo-code Python
		int n = histo.length;
		Boolean pic = false;
		int lim = max(histo)/10;
		int compt = 0;
		
		ArrayList<Double> sommets = new ArrayList<Double>();
		ArrayList<Double> ind_sommets = new ArrayList<Double>();
		ArrayList<Double> temp = new ArrayList<Double>();
		ArrayList<Double> ind_temp = new ArrayList<Double>();
		
		/*
		* Parcours des valeurs de l'histogramme
		*/
		for(double i=0 ; i < n ; i++) {
			
			if (histo[(int) i] > lim) { 
				
				/*
				* Si on passe au-dessus de la limite et qu'on n'était pas dans un pic,
				* on incrémente le nombre de pics
				*/				
				if (!pic) {
					compt += 1;
				}
				
				/*
				* Dans tous les cas, on indique qu'on se situe dans un pic
				* et on stocke l'indice et la valeur de l'histogramme dans des listes temporaires
				*/
				pic = true;
				temp.add((double) histo[(int) i]);
				ind_temp.add(i);
				
			} else {
				
				/*
				* Si on passe sous la limite est qu'on était dans un pic,
				* on ajoute son max et l'indice de son max aux listes sommets et ind_sommets
				* puis dans tous les cas on indique qu'on sort n'est pas (ou plus) dans un pic
				*/
				if (pic) {
					Double m = temp.get(0);
					int ind_max = 0;
					
					for(int j = 1 ; j<temp.size() ; j++) {
						if (temp.get(j)>m) {
							m = temp.get(j);
							ind_max = j;
						}
					}
					
					sommets.add(m);
					ind_sommets.add(z - ind_temp.get(ind_max)); // On veut la liste des sommets de haut en bas
					temp = new ArrayList<Double>();
					ind_temp = new ArrayList<Double>();
				}
				pic = false;
			}
		}
		
		// Méthode filtre du pseudo-code Python
		
		int part = 2;
		int nb_part = compt/part; // Nombre de sommets par partie (excepté pour la dernière partie)
		ArrayList<Double> reste = new ArrayList<Double>();
		ArrayList<Double> ind_reste = new ArrayList<Double>();
		
		/*
		* Parcours des valeurs max des pics
		*/
		for(int k=0 ; k<part-1 ; k++) {
			/*
			* Parcours des sommets des k-1 premières parties
			*/
			List<Double> y = sommets.subList(k*nb_part, (k+1)*nb_part);
			double c = conf(y); // Calcul des paramètres du seuil de hauteur minimale
			double m = moyenne(y);
			
			for(int i = k*nb_part ; i < (k+1)*nb_part ; i++) {
				if (sommets.get(i) > (m-c)) {
					reste.add(sommets.get(i));
					ind_reste.add(ind_sommets.get(i));
					
				}
			}
		}
		
		
		/*
		* Parcours des sommets de la dernière partie
		*/
		List<Double> x = sommets.subList((part-1)*nb_part, sommets.size());
		double conf = conf(x);
		double moy = moyenne(x);
		
		for(int i=(part-1)*nb_part ; i < compt ; i++) {
			if (sommets.get(i) > (moy-conf)) {
				reste.add(sommets.get(i));
				ind_reste.add(ind_sommets.get(i));
			}
		}
		
		// Méthode marches du pseudo-code Python
		
		ArrayList<Double> res = new ArrayList<Double>();
		res.add(ind_reste.get(0));
		n = ind_reste.size();
		ArrayList<Double> dist = new ArrayList<Double>();
		
		/*
		* Liste des espacements entre sommets consécutifs
		*/
		for(int i = 0 ; i< n-1 ; i++) {
			dist.add(ind_reste.get(i) - ind_reste.get(i+1));
		}
		
		double m = 0;
		double c = 0;
		
		/*
		* Parcours des indices des sommets restants
		*/
		for(int i=1 ; i<n ; i++) {
			List<Double> y = dist.subList(0, i);
			c = conf(y);
			m = moyenne(y);
			
			/*
			* On compare l'espacement entre le sommet traité et le dernier sommet accepté dans la liste res
			* avec un seuil construit à partir de la moyenne et de l'écart-type des espacements des sommets précédents
			*/
			if( (res.get(res.size()-1)-ind_reste.get(i))  >= (m-c)) {
				res.add(ind_reste.get(i));
			}
			
		}
		
		
		return res.size();
	}
	
	
	// METHODES PRIVÉES UTILISÉES
	
	/**
	 * Renvoie la valeur maximale d'un tableau d'entiers
	 * 
	 * @param tab le tableau d'entiers
	 * @return la valeur maximale
	 */
	private static int max(int[] tab) {
		int res = 0;
		
		for(int x : tab) {
			if (x >= res)
				res = x;
		}
		
		return res;
	}
	
	/**
	 * Renvoie la moyenne des valeurs d'un tableau d'entiers
	 * 
	 * @param tab le tableau d'entiers
	 * @return la valeur moyenne
	 */
	private static double moyenne(List<Double> y) {
		int sum = 0;
		int n = 0;
		
		for(Double x : y) {
			sum += x;
			n++;
		}
		
		return sum/n;
	}
	
	/**
	 * Renvoie la variance des valeurs d'un tableau d'entiers
	 * 
	 * @param tab le tableau d'entiers
	 * @return la variance
	 * 
	 * @author Juliette/Nicolas
	 */
	private static double variance(List<Double> list) {
		double moy = moyenne(list);
		double sum = 0;
		int n = 0;
		
		for(Double x : list) {
			sum += Math.pow(x-moy, 2);
			n++;
		}
		
		return sum/(n-1);
	}

	/**
	 * Calcule l'intervalle de confiance
	 * 
	 * @param y Le tableau de valeurs
	 * @return L'intervalle de confiance
	 * 
	 * @author Juliette/Nicolas
	 */
	private static double conf(List<Double> list) {
		double var = variance(list);
		
		return 1.96*Math.sqrt(var)/Math.sqrt(list.size());
	}
	
}
