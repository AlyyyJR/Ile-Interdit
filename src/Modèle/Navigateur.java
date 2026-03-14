// ==================================================================
// FICHIER : Navigateur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Navigateur : peut déplacer un autre joueur de 2 cases par action.
//
// Rôle de Navigateur :
//   - étendre Joueur et redéfinir déplacer() pour cibler un autre joueur
//   - spawner sur LeLagonPerdu
//   - offrir une mobilité accrue en déplaçant ses coéquipiers
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Représente le rôle du Navigateur dans le jeu L'Île Interdite.
 *
 * Le Navigateur bénéficie d'une action supplémentaire par tour (4 au lieu de 3),
 * ce qui lui permet d'agir plus souvent que les autres aventuriers. Cette capacité est
 * gérée par le contrôleur via la couleur distinctive {Color#YELLOW}.
 *
 * Dans les règles officielles du jeu, le Navigateur peut également déplacer d'autres
 * joueurs jusqu'à 2 tuiles par action — cette fonctionnalité avancée n'est pas encore
 * implémentée dans la présente version.
 *
 * Point de départ : {Zone#LaPorteDOr} — Couleur : Jaune.
 * 
 * Le Navigateur est un aventurier spécialisé dans la mobilité et la coordination de l'équipe. Sa capacité à effectuer une action supplémentaire par tour lui permet de se déplacer plus fréquemment, ce qui peut être crucial pour atteindre rapidement des zones clés du plateau ou pour réagir efficacement aux inondations. 
 * En tant que tel, il est souvent considéré comme un rôle stratégique pour les joueurs qui souhaitent maximiser leur influence sur le déroulement de la partie et soutenir leurs coéquipiers de manière proactive.    
 */
public class Navigateur extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise le Navigateur avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Navigateur(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.LaPorteDOr;
        this.setCouleur(Color.YELLOW);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Navigateur.png")));
    }

    // Capacité spéciale (4 actions / tour) gérée dans Controleur#débutTour()
    // via détection de la couleur YELLOW.
}
