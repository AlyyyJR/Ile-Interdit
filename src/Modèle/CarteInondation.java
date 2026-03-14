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
 * Chaque carte est liée à une {Tuile} du plateau. Lorsqu'elle est piochée,
 * la tuile associée progresse d'un état ({Sec → Inondé → Sombré}).
 * Une carte dont la tuile a sombré est retirée définitivement du jeu.
 * Les cartes d'inondation sont utilisées par le contrôleur pour faire avancer
 * l'état des tuiles et pour gérer la dynamique de l'inondation sur le plateau.
 * Par exemple, si la carte d'inondation ciblant la tuile de l'Héliport est piochée alors que celle-ci est déjà inondée, elle sombrera, ce qui peut compliquer les déplacements du Pilote et rendre la partie plus difficile. 
 * De même, si une carte d'inondation ciblant une tuile contenant une relique est piochée alors que la tuile est déjà inondée, elle sombrera, ce qui peut rendre la collecte de cette relique plus urgente pour les joueurs. 
 * En somme, les cartes d'inondation sont un élément clé du gameplay de L'Île Interdite, ajoutant du suspense et des défis à chaque tour de jeu. 
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
