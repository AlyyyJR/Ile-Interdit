// ==================================================================
// FICHIER : Etat.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Énumération représentant l'état d'une tuile sur le plateau de jeu.
//
// Rôle de Etat :
//   - modéliser les trois stades de submersion d'une tuile
//   - être utilisé par Tuile pour décider de son accessibilité
//   - être comparé par Controleur et les Joueurs lors des déplacements
//
// Utilisé par : Tuile, Grille, Joueur, Controleur
// ==================================================================

package Modèle;

/**
 * Énumération représentant l'état d'une tuile sur le plateau de jeu.
 *
 * <p>Une tuile peut passer par trois états successifs au cours de la partie :
 * d'abord sèche, puis inondée lorsqu'une carte inondation la cible,
 * puis sombrée si elle est à nouveau inondée sans avoir été asséchée.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public enum Etat {

    /** La tuile est normale, praticable sans restriction. */
    Sec,

    /** La tuile est partiellement recouverte d'eau ; elle reste praticable et peut être asséchée. */
    Inondé,

    /** La tuile est totalement submergée et a disparu ; elle n'est plus accessible ni asséchable. */
    Sombré
}
