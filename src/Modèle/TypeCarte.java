// ==================================================================
// FICHIER : TypeCarte.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Énumération des types de cartes présentes dans la pioche Trésor.
//
// Rôle de TypeCarte :
//   - identifier les 4 types de cartes trésor (Magenta, Gray, Cyan, Orange)
//   - distinguer les cartes spéciales (Hélicoptère, Sac de Sable)
//   - signaler la carte événement Montée des Eaux
//
// Utilisé par : CarteTresor, Joueur, Controleur, VueAventurier, VueCarteSpe
// ==================================================================

package Modèle;

/**
 * Énumération des types de cartes présentes dans la pioche Trésor.
 *
 * Le jeu utilise sept types de cartes répartis en trois catégories :
 * 
 *   Cartes Trésor (4 couleurs) : collectées par les joueurs pour récupérer les reliques.
 *   Cartes Spéciales : l'Hélicoptère permet un déplacement libre,
 *       le Sac de Sable assèche n'importe quelle tuile inondée.
 *   Montée des Eaux : fait monter le niveau de l'eau et rebat la défausse inondation.
 * 
 * Les cartes trésor sont identifiées par leur couleur et leur relique associée :
 * Magenta → Cristal Ardent
 * Grise   → Pierre Sacrée
 * Cyan    → Calice de l'Onde
 * Orange  → Statue du Zéphyr
 */
public enum TypeCarte {

    // ==================================================================
    // Cartes Trésor
    // ==================================================================
    /** Carte trésor de couleur Magenta (Cristal Ardent). */
    TresorMagenta,

    /** Carte trésor de couleur Grise (Pierre Sacrée). */
    TresorGray,

    /** Carte trésor de couleur Cyan (Calice de l'Onde). */
    TresorCyan,

    /** Carte trésor de couleur Orange (Statue du Zéphyr). */
    TresorOrange,

    // ==================================================================
    // Cartes Spéciales
    // ==================================================================
    /** Carte spéciale Hélicoptère : permet de déplacer librement un groupe de joueurs. */
    SpécialHélicoptère,

    /** Carte spéciale Sac de Sable : permet d'assécher immédiatement n'importe quelle tuile inondée. */
    SpécialSacDeSable,

    // ==================================================================
    // Carte Événement
    // ==================================================================
    /** Carte Montée des Eaux : augmente le niveau de l'eau et reformule la pioche inondation. */
    MontéeEaux;

    /**
     * Retourne une représentation lisible du nom de la carte, avec des espaces
     * insérés avant chaque lettre majuscule.
     *
     * Exemple : {SpécialHélicoptère} → {"Spécial Hélicoptère"}
     *
     * @return le nom de la carte avec espaces
     */
    public String nameEsp() {
        String nom = this.name();
        StringBuilder res = new StringBuilder();
        res.append(nom.charAt(0));

        for (int i = 1; i < nom.length(); i++) {
            char c = nom.charAt(i);
            if (Character.isUpperCase(c)) {
                res.append(' ');
            }
            res.append(c);
        }

        return res.toString();
    }
}
