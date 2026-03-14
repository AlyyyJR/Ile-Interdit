// ==================================================================
// FICHIER : Pilote.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Pilote : peut voler vers n'importe quelle tuile non sombée (1 fois/tour).
//
// Rôle de Pilote :
//   - étendre Joueur et redéfinir listerCasesDispo() pour inclure toutes les tuiles sèches
//   - spawner sur LeRocherFantome
//   - limiter le vol à une seule utilisation par tour
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Représente le rôle du Pilote dans le jeu L'Île Interdite.
 *
 * Le Pilote possède la capacité de voler librement vers n'importe quelle tuile du plateau
 * qui n'est pas dans l'état {Sombré}, sans tenir compte de l'adjacence ou des obstacles.
 *
 * Point de départ : {Zone#Heliport} — Couleur : Bleu.
 * 
 * Le Pilote est un aventurier spécialisé dans les déplacements rapides et stratégiques à travers l'île. Sa capacité à voler vers n'importe quelle tuile non sombrée lui permet de contourner les zones inondées et de rejoindre des endroits clés du plateau en un seul mouvement, ce qui peut être crucial pour sauver des reliques ou échapper à des situations dangereuses. 
 * Cependant, cette capacité ne peut être utilisée qu'une seule fois par tour, ce qui oblige le joueur à planifier soigneusement ses déplacements pour maximiser l'efficacité du Pilote. 
 * En tant que tel, il est souvent considéré comme un rôle puissant mais nécessitant une bonne gestion pour tirer pleinement parti de ses avantages.
 */
public class Pilote extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise le Pilote avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Pilote(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.Heliport;
        this.setCouleur(Color.BLUE);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Pilote.png")));
    }

    // ==================================================================
    // Capacité spéciale
    // ==================================================================

    /**
     * Retourne toutes les tuiles du plateau accessibles au Pilote.
     *
     * Le Pilote peut se déplacer sur n'importe quelle tuile non sombrée,
     * à l'exception de sa position actuelle.
     *
     * @return liste de toutes les tuiles non sombrées, hors position actuelle
     */
    @Override
    public ArrayList<Tuile> listerCasesDispo() {
        ArrayList<Tuile> dispo = new ArrayList<>();
        Tuile actuelle = this.getPosition();

        for (Tuile[] ligne : actuelle.getPlateau().getTuiles()) {
            for (Tuile t : ligne) {
                if (t != null && !t.equals(actuelle) && t.getEtat() != Etat.Sombré) {
                    dispo.add(t);
                }
            }
        }
        return dispo;
    }
}
