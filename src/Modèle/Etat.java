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
 * Une tuile peut passer par trois états successifs au cours de la partie :
 * d'abord sèche, puis inondée lorsqu'une carte inondation la cible,
 * puis sombrée si elle est à nouveau inondée sans avoir été asséchée.
 * 
 * L'état d'une tuile détermine son accessibilité pour les joueurs : une tuile sèche ou inondée est praticable, tandis qu'une tuile sombrée est inaccessible et ne peut plus être asséchée. Les joueurs doivent donc surveiller attentivement l'état des tuiles pour planifier leurs déplacements et éviter de se retrouver bloqués par des zones submergées. 
 * Par exemple, si la tuile de l'Héliport devient inondée, le Pilote devra trouver un autre moyen de se déplacer, tandis que si une tuile contenant une relique devient inondée, les joueurs devront agir rapidement pour l'assécher avant qu'elle ne sombre, ce qui rend la gestion de l'état des tuiles un aspect crucial de la stratégie du jeu. 
 * En somme, l'énumération {Etat} est un élément central du modèle du jeu, encapsulant la dynamique de l'inondation et ses conséquences sur le gameplay.
 */
public enum Etat {

    /** La tuile est normale, praticable sans restriction. */
    Sec,

    /** La tuile est partiellement recouverte d'eau ; elle reste praticable et peut être asséchée. */
    Inondé,

    /** La tuile est totalement submergée et a disparu ; elle n'est plus accessible ni asséchable. */
    Sombré
}
