// ==================================================================
// FICHIER : CarteInondation.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Représente une carte de la pioche Inondation ciblant une tuile du plateau.
//
// Rôle de CarteInondation :
//   - maintenir la référence à la Tuile qu'elle représente
//   - être piochée par le Controleur pour faire progresser l'état de la tuile
//   - passer dans la défausse ou être retirée définitivement si la tuile a sombré
//
// Utilisé par : Controleur (piocheInondation, défausseInondation)
// ==================================================================

package Modèle;

/**
 * Représente une carte de la pioche inondation.
 *
 * <p>Chaque carte est liée à une {@link Tuile} du plateau. Lorsqu'elle est piochée,
 * la tuile associée progresse d'un état ({@code Sec → Inondé → Sombré}).
 * Une carte dont la tuile a sombré est retirée définitivement du jeu.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class CarteInondation {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Tuile ciblée par cette carte d'inondation. */
    private final Tuile tuile;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée une carte d'inondation associée à la tuile donnée.
     *
     * @param tuile la tuile que cette carte représente (non nulle)
     */
    public CarteInondation(Tuile tuile) {
        this.tuile = tuile;
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne la tuile associée à cette carte.
     *
     * @return la tuile ciblée
     */
    public Tuile getTuile() {
        return tuile;
    }
}
