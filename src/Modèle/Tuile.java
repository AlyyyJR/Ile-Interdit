// ==================================================================
// FICHIER : Tuile.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Représente une tuile du plateau avec son identité, son état et ses occupants.
//
// Rôle de Tuile :
//   - encapsuler la Zone, l'Etat et l'image de la tuile
//   - gérer la liste des Joueurs locataires (addLocataire, delLocataire)
//   - exposer isSombre() pour les vérifications de fin de partie
//
// Utilisé par : Grille, Joueur, CarteInondation, Controleur, VuePlateau
// ==================================================================

package Modèle;

import java.awt.Color;
import java.util.*;
import javax.swing.ImageIcon;

/**
 * Représente une tuile du plateau de jeu.
 *
 * <p>Une tuile possède un nom de zone ({@link Zone}), un état ({@link Etat}), une position
 * ({@link Coordonnees}) sur la grille et une liste de joueurs présents ({@link Joueur}).
 * Son image graphique est automatiquement mise à jour lors de tout changement d'état.</p>
 *
 * <p>Certaines tuiles contiennent une relique disponible, identifiée par une couleur :</p>
 * <ul>
 *   <li>Magenta → Cristal Ardent (zones préfixées "LAC")</li>
 *   <li>Gray    → Pierre Sacrée (zones préfixées "LET")</li>
 *   <li>Cyan    → Calice de l'Onde (zones préfixées "LEP")</li>
 *   <li>Orange  → Statue du Zéphyr (zones préfixées "LEJ")</li>
 * </ul>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class Tuile {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Position de la tuile sur la grille. */
    private final Coordonnees coordonnees;

    /** Joueurs actuellement présents sur cette tuile. */
    private ArrayList<Joueur> locataires;

    /** Nom de la zone représentée par cette tuile. */
    private final Zone intitule;

    /** État courant de la tuile (Sec, Inondé, Sombré). */
    private Etat etat;

    /** Référence vers la grille parente pour naviguer vers les tuiles adjacentes. */
    private final Grille plateau;

    /** Couleur de la relique disponible sur cette tuile, ou {@code null} si aucune. */
    private final Color reliqueDispo;

    /** Image graphique représentant l'état visuel actuel de la tuile. */
    private ImageIcon image;

    // ==================================================================
    // Constructeurs
    // ==================================================================

    /**
     * Crée une tuile et détermine automatiquement sa relique disponible
     * à partir du préfixe de son nom de zone.
     *
     * @param nom   la zone nommée associée à cette tuile
     * @param etat  l'état initial de la tuile
     * @param grille la grille de jeu contenant cette tuile
     * @param coo   la position de la tuile sur la grille
     */
    public Tuile(Zone nom, Etat etat, Grille grille, Coordonnees coo) {
        this.intitule    = nom;
        this.coordonnees = coo;
        this.locataires  = new ArrayList<>();
        this.plateau     = grille;
        this.etat        = etat;
        this.reliqueDispo = detecterRelique(nom);
        this.setImage();
    }

    /**
     * Crée une tuile avec une relique explicitement spécifiée (utilisé en mode debug).
     *
     * @param nom          la zone nommée associée à cette tuile
     * @param etat         l'état initial de la tuile
     * @param grille       la grille de jeu contenant cette tuile
     * @param coo          la position de la tuile sur la grille
     * @param reliqueDispo la couleur de la relique disponible, ou {@code null}
     */
    public Tuile(Zone nom, Etat etat, Grille grille, Coordonnees coo, Color reliqueDispo) {
        this.intitule     = nom;
        this.coordonnees  = coo;
        this.locataires   = new ArrayList<>();
        this.plateau      = grille;
        this.etat         = etat;
        this.reliqueDispo = reliqueDispo;
        this.setImage();
    }

    // ==================================================================
    // Méthode privée : détection de relique
    // ==================================================================

    /**
     * Détermine la couleur de la relique associée à une zone selon son préfixe.
     *
     * @param zone la zone à analyser
     * @return la couleur de la relique, ou {@code null} si aucune relique n'est associée
     */
    private static Color detecterRelique(Zone zone) {
        String prefixe = zone.toString().substring(0, 3).toUpperCase();
        switch (prefixe) {
            case "LAC": return Color.MAGENTA;
            case "LET": return Color.GRAY;
            case "LEP": return Color.CYAN;
            case "LEJ": return Color.ORANGE;
            default:    return null;
        }
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne les tuiles situées en diagonale de cette tuile (jusqu'à 4).
     *
     * @return liste des tuiles diagonales existantes (non nulles)
     */
    public ArrayList<Tuile> getDiagonales() {
        Coordonnees coo = this.getCoordonees();
        ArrayList<Tuile> tuilesDia = new ArrayList<>();
        int[][] deltas = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] d : deltas) {
            int nx = coo.getX() + d[0];
            int ny = coo.getY() + d[1];
            if (nx >= 0 && nx < 6 && ny >= 0 && ny < 6) {
                Tuile t = plateau.getTuile(nx, ny);
                if (t != null) {
                    tuilesDia.add(t);
                }
            }
        }
        return tuilesDia;
    }

    /**
     * Retourne les tuiles orthogonalement adjacentes (haut, bas, gauche, droite).
     *
     * @return liste des tuiles adjacentes existantes (non nulles)
     */
    public ArrayList<Tuile> getAdjacent() {
        Coordonnees coo = this.getCoordonees();
        ArrayList<Tuile> tuilesAdj = new ArrayList<>();
        int[][] deltas = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : deltas) {
            int nx = coo.getX() + d[0];
            int ny = coo.getY() + d[1];
            if (nx >= 0 && nx < 6 && ny >= 0 && ny < 6) {
                Tuile t = plateau.getTuile(nx, ny);
                if (t != null) {
                    tuilesAdj.add(t);
                }
            }
        }
        return tuilesAdj;
    }

    /**
     * Retourne le nom de zone de cette tuile.
     *
     * @return la zone associée
     */
    public Zone getIntitule() {
        return intitule;
    }

    /**
     * Retourne l'état courant de la tuile.
     *
     * @return l'état ({@code Sec}, {@code Inondé} ou {@code Sombré})
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Retourne les coordonnées de la tuile sur la grille.
     *
     * @return les coordonnées (ligne, colonne)
     */
    public Coordonnees getCoordonees() {
        return coordonnees;
    }

    /**
     * Retourne la liste des joueurs présents sur cette tuile.
     *
     * @return liste mutable des locataires
     */
    public ArrayList<Joueur> getLocataires() {
        return locataires;
    }

    /**
     * Retourne la grille parente de cette tuile.
     *
     * @return la grille de jeu
     */
    public Grille getPlateau() {
        return plateau;
    }

    /**
     * Retourne la couleur de la relique disponible sur cette tuile.
     *
     * @return la couleur de la relique, ou {@code null} si aucune
     */
    public Color getReliqueDispo() {
        return reliqueDispo;
    }

    /**
     * Retourne l'image graphique correspondant à l'état actuel de la tuile.
     *
     * @return l'icône de la tuile
     */
    public ImageIcon getImage() {
        return image;
    }

    // ==================================================================
    // Mutateurs
    // ==================================================================

    /**
     * Met à jour l'image graphique de la tuile en fonction de son état courant.
     *
     * <p>Les images sont chargées depuis {@code /ImagesTuiles/} :</p>
     * <ul>
     *   <li>Sec     → {@code {Zone.name()}.png}</li>
     *   <li>Inondé  → {@code {Zone.name()}2.png}</li>
     *   <li>Sombré  → {@code EauSombree.png}</li>
     * </ul>
     */
    public void setImage() {
        if (etat == null) {
            image = new ImageIcon(this.getClass().getResource("/ImagesTuiles/Eau.png"));
            return;
        }
        switch (etat) {
            case Sec:
                image = new ImageIcon(this.getClass().getResource(
                    "/ImagesTuiles/" + this.intitule.name() + ".png"));
                break;
            case Inondé:
                image = new ImageIcon(this.getClass().getResource(
                    "/ImagesTuiles/" + this.intitule.name() + "2.png"));
                break;
            case Sombré:
                image = new ImageIcon(this.getClass().getResource(
                    "/ImagesTuiles/EauSombree.png"));
                break;
            default:
                image = new ImageIcon(this.getClass().getResource("/ImagesTuiles/Eau.png"));
        }
    }

    /**
     * Modifie l'état de la tuile et met automatiquement à jour son image.
     *
     * @param etat le nouvel état de la tuile
     */
    public void setEtat(Etat etat) {
        this.etat = etat;
        setImage();
    }

    // ==================================================================
    // Gestion des joueurs
    // ==================================================================

    /**
     * Indique si cette tuile est sombrée (inaccessible).
     *
     * @return {@code true} si l'état est {@code Sombré}
     */
    public boolean isSombre() {
        return this.etat == Etat.Sombré;
    }

    /**
     * Ajoute un joueur sur cette tuile.
     *
     * @param joueur le joueur à ajouter
     */
    public void addLocataire(Joueur joueur) {
        this.locataires.add(joueur);
    }

    /**
     * Retire un joueur de cette tuile.
     *
     * @param joueur le joueur à retirer
     */
    public void delLocataire(Joueur joueur) {
        this.locataires.remove(joueur);
    }

    // ==================================================================
    // Déplacement spécial (Plongeur)
    // ==================================================================

    /**
     * Calcule l'ensemble des tuiles accessibles par le Plongeur depuis cette tuile.
     *
     * <p>Le Plongeur peut traverser librement les tuiles inondées et sombrées,
     * en se propageant récursivement à partir des tuiles adjacentes non sèches.
     * La collection retournée inclut les tuiles sèches et inondées atteignables,
     * mais pas les tuiles sombrées (destinations illégales).</p>
     *
     * @return collection de toutes les tuiles atteignables (y compris la position actuelle)
     */
    public Collection<Tuile> tuilesPlongeurs() {
        ArrayList<Tuile> accessibles = new ArrayList<>(this.getAdjacent());

        for (int i = 0; i < accessibles.size(); i++) {
            Tuile courante = accessibles.get(i);
            if (courante.getEtat() != Etat.Sec) {
                // Propagation à travers les tuiles non sèches
                for (Tuile voisine : courante.getAdjacent()) {
                    if (!accessibles.contains(voisine)) {
                        accessibles.add(voisine);
                    }
                }
            }
        }
        return accessibles;
    }
}
