// ==================================================================
// FICHIER : Plongeur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Plongeur : peut traverser des tuiles sombées pour se déplacer.
//
// Rôle de Plongeur :
//   - étendre Joueur et redéfinir listerCasesDispo() pour ignorer les tuiles sombées
//   - spawner sur LeLagonPerdu
//   - permettre un déplacement à travers les zones submergées
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Représente le rôle du Plongeur dans le jeu L'Île Interdite.
 *
 * <p>Le Plongeur peut traverser librement les tuiles inondées ou sombrées et rejoindre
 * la première tuile sèche ou inondée accessible dans n'importe quelle direction.
 * Son déplacement est calculé par un parcours en largeur (BFS) via
 * {@link Tuile#tuilesPlongeurs()}.</p>
 *
 * <p>Point de départ : {@link Zone#LaPorteDeFer} — Couleur : Noir.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class Plongeur extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise le Plongeur avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Plongeur(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.LaPorteDeFer;
        this.setCouleur(Color.BLACK);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Plongeur.png")));
    }

    // ==================================================================
    // Capacité spéciale
    // ==================================================================

    /**
     * Retourne les tuiles accessibles au Plongeur, en traversant les tuiles inondées et sombrées.
     *
     * <p>Le résultat est la liste des tuiles atteignables via le BFS de {@link Tuile#tuilesPlongeurs()},
     * après exclusion des tuiles sombrées (interdites à l'arrivée) et de la position actuelle.</p>
     *
     * @return liste des tuiles accessibles (inondées ou sèches, hors position actuelle)
     */
    @Override
    public ArrayList<Tuile> listerCasesDispo() {
        ArrayList<Tuile> tuilesDispo = new ArrayList<>(this.getPosition().tuilesPlongeurs());

        tuilesDispo.removeIf(t -> t.getEtat() == Etat.Sombré);
        tuilesDispo.remove(this.getPosition());

        return tuilesDispo;
    }
}
