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
 * <p>Le Pilote possède la capacité de voler librement vers n'importe quelle tuile du plateau
 * qui n'est pas dans l'état {@code Sombré}, sans tenir compte de l'adjacence ou des obstacles.</p>
 *
 * <p>Point de départ : {@link Zone#Heliport} — Couleur : Bleu.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
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
     * <p>Le Pilote peut se déplacer sur n'importe quelle tuile non sombrée,
     * à l'exception de sa position actuelle.</p>
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
