// ==================================================================
// FICHIER : Ingénieur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Ingénieur : peut assécher 2 tuiles par action d'assèchement.
//
// Rôle de Ingénieur :
//   - étendre Joueur et redéfinir assecher() pour assécher deux tuiles en une action
//   - spawner sur LaPorteDeBronze
//   - doubler l'efficacité de l'assèchement par rapport aux autres rôles
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Représente le rôle de l'Ingénieur dans le jeu L'Île Interdite.
 *
 * L'Ingénieur peut assécher deux tuiles par action d'assèchement au lieu d'une seule.
 * Après le premier assèchement, le jeu lui propose d'en effectuer un second gratuitement.
 *
 * Point de départ : {Zone#LaPorteDeBronze} — Couleur : Rouge.
 * L'Ingénieur est un aventurier spécialisé dans la gestion des zones inondées. Sa capacité à assécher deux tuiles en une seule action lui permet de contrôler plus efficacement les zones critiques du plateau, offrant ainsi une meilleure protection contre les inondations. 
 * En tant que tel, il est souvent considéré comme un rôle essentiel pour maintenir la stabilité du terrain et faciliter les déplacements des autres aventuriers.  
 */
public class Ingénieur extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise l'Ingénieur avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Ingénieur(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.LaPorteDeBronze;
        this.setCouleur(Color.RED);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Ingenieur.png")));
    }

    // ==================================================================
    // Capacité spéciale
    // ==================================================================

    /**
     * Effectue jusqu'à deux assèchements en une seule action.
     *
     * Après le premier assèchement, si des tuiles inondées restent disponibles,
     * une boîte de dialogue propose à l'Ingénieur d'en assécher une seconde gratuitement.
     */
    @Override
    public void assecher() {
        super.assecher();

        if (this.isAssPossible()) {
            int choix = JOptionPane.showConfirmDialog(
                null,
                "Assécher une deuxième tuile gratuitement ?",
                "2ème Assèchement (Ingénieur)",
                JOptionPane.YES_NO_OPTION
            );
            if (choix == JOptionPane.YES_OPTION) {
                super.assecher();
            }
        }
    }
}
