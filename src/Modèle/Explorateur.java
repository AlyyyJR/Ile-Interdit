// ==================================================================
// FICHIER : Explorateur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Explorateur : peut se déplacer et assécher en diagonale.
//
// Rôle de Explorateur :
//   - étendre Joueur et redéfinir listerCasesDispo() avec les 8 directions
//   - redéfinir listerCasesAssechables() avec les 8 directions
//   - spawner sur LaPorteDeCuivre
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Représente le rôle de l'Explorateur dans le jeu L'Île Interdite.
 *
 * <p>L'Explorateur possède deux capacités spéciales par rapport aux autres aventuriers :</p>
 * <ul>
 *   <li><b>Déplacement en diagonale</b> : peut se déplacer vers les 8 tuiles voisines
 *       (4 orthogonales + 4 diagonales) en une seule action.</li>
 *   <li><b>Assèchement en diagonale</b> : peut assécher non seulement les tuiles adjacentes,
 *       mais aussi les tuiles situées en diagonale.</li>
 * </ul>
 *
 * <p>Point de départ : {@link Zone#LaPorteDeCuivre} — Couleur : Vert.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class Explorateur extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise l'Explorateur avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Explorateur(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.LaPorteDeCuivre;
        this.setCouleur(Color.GREEN);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Explorateur.png")));
    }

    // ==================================================================
    // Capacités spéciales
    // ==================================================================

    /**
     * Retourne les tuiles vers lesquelles l'Explorateur peut se déplacer.
     *
     * <p>Inclut les tuiles orthogonalement et diagonalement adjacentes
     * qui ne sont pas dans l'état {@code Sombré}.</p>
     *
     * @return liste des tuiles accessibles (orthogonales + diagonales)
     */
    @Override
    public ArrayList<Tuile> listerCasesDispo() {
        ArrayList<Tuile> tuilesDispo = super.listerCasesDispo();

        Coordonnees coo = this.getPosition().getCoordonees();
        Grille g        = this.getPosition().getPlateau();

        int[][] deltas = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        for (int[] d : deltas) {
            int nx = coo.getX() + d[0];
            int ny = coo.getY() + d[1];
            if (nx >= 0 && nx < 6 && ny >= 0 && ny < 6) {
                Tuile t = g.getTuile(nx, ny);
                if (t != null && t.getEtat() != Etat.Sombré && !tuilesDispo.contains(t)) {
                    tuilesDispo.add(t);
                }
            }
        }

        return tuilesDispo;
    }

    /**
     * Retourne les tuiles que l'Explorateur peut assécher.
     *
     * <p>Inclut les tuiles orthogonalement et diagonalement adjacentes
     * qui sont dans l'état {@code Inondé}, en plus de la tuile actuelle.</p>
     *
     * @return liste des tuiles asséchables (orthogonales + diagonales)
     */
    @Override
    public ArrayList<Tuile> listerTuilesAssechables() {
        ArrayList<Tuile> tuilesDispo = super.listerTuilesAssechables();

        for (Tuile t : this.getPosition().getDiagonales()) {
            if (t.getEtat() == Etat.Inondé && !tuilesDispo.contains(t)) {
                tuilesDispo.add(t);
            }
        }

        return tuilesDispo;
    }
}
