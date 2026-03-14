// ==================================================================
// FICHIER : Coordonnees.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Classe immuable représentant une position (ligne, colonne) sur la grille.
//
// Rôle de Coordonnees :
//   - encapsuler les indices x (ligne) et y (colonne) d'une case
//   - être transmise dans MessagePlateau lors d'un clic sur le plateau
//   - permettre au Controleur d'identifier la tuile cliquée
//
// Utilisé par : MessagePlateau, VuePlateau, Grille, Controleur
// ==================================================================

package Modèle;

/**
 * Classe immuable représentant une position (ligne, colonne) sur la grille de jeu.
 *
 * <p>Les coordonnées sont définies à la construction et ne peuvent plus être modifiées.
 * L'axe {@code x} correspond à la ligne (0–5) et l'axe {@code y} à la colonne (0–5).</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class Coordonnees {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Indice de ligne sur la grille (0 à 5). */
    private final int x;

    /** Indice de colonne sur la grille (0 à 5). */
    private final int y;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise les coordonnées avec la ligne et la colonne données.
     *
     * @param x indice de ligne (0–5)
     * @param y indice de colonne (0–5)
     */
    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne l'indice de ligne.
     *
     * @return la ligne (axe x)
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne l'indice de colonne.
     *
     * @return la colonne (axe y)
     */
    public int getY() {
        return y;
    }
}
