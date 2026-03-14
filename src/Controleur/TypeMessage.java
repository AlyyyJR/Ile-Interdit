// ==================================================================
// FICHIER : TypeMessage.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Énumération des types de messages échangés entre la Vue et le Contrôleur.
//
// Rôle de TypeMessage :
//   - identifier chaque action utilisateur déclenchée depuis l'interface
//   - être transporté dans un Message, MessageAventurier ou MessagePlateau
//   - permettre au Contrôleur de dispatche les traitements selon l'action
//
// Utilisé par : Message, MessageAventurier, MessagePlateau, Controleur,
//               VueAventurier, VuePlateau, VueCarteSpe, VueDefausse,
//               VueDonDeCartes, VueEcranTitre, VueInscription
// ==================================================================

package Controleur;

/**
 * Énumération des types de messages échangés entre la Vue et le Contrôleur.
 *
 * Les messages permettent à chaque vue d'informer le contrôleur de l'action
 * choisie par l'utilisateur. Ils sont encapsulés dans {Message},
 * {MessageAventurier} ou {MessagePlateau} selon leur nature.
 *
 * TypeMessage
 * ├── CLIQUER
 * ├── DEPLACER
 * ├── ASSECHER
 * ├── DONNER
 * ├── UTILISER
 * ├── PASSER
 * └── AUTRE (pour les actions spécifiques à certaines vues)
 * En somme, {TypeMessage} est un élément clé du système de communication entre les vues et le contrôleur, permettant une identification claire et structurée des différentes actions que les joueurs peuvent déclencher depuis l'interface graphique. Par exemple, lorsque le joueur "Alice" clique sur le bouton "Déplacer" dans sa vue d'aventurier, la {VueAventurier} émet un {MessageAventurier} avec le type {DEPLACER} et la référence au joueur "Alice". Le contrôleur reçoit ce message, identifie que c'est une action de déplacement pour "Alice", vérifie les déplacements possibles pour son rôle et sa position actuelle, puis met à jour le modèle et les vues en conséquence. De cette manière, {TypeMessage} facilite une gestion efficace des interactions des joueurs tout en maintenant une séparation claire entre la logique du jeu et l'interface utilisateur.
 */
public enum TypeMessage {

    // ==================================================================
    // Messages globaux
    // ==================================================================

    /** Annulation d'une action ou fermeture de l'application. */
    Annuler,

    /** Validation du formulaire d'inscription des joueurs. */
    Valider,

    /** Démarrage d'une nouvelle partie depuis l'écran titre. */
    Jouer,

    /** Clic sur une tuile du plateau. */
    ClicPlateau,

    // ==================================================================
    // Messages d'action joueur
    // ==================================================================

    /** Action : déplacer le joueur actif. */
    Deplacer,

    /** Action : assécher une tuile. */
    Assecher,

    /** Action : donner une carte à un autre joueur. */
    Donner,

    /** Action : prendre la relique sur la tuile actuelle. */
    PrendreRelique,

    /** Action : utiliser une carte spéciale (dialogue de choix). */
    CarteSpe,

    /** Action : utiliser la carte Hélicoptère directement. */
    CarteSpeHelico,

    /** Action : utiliser la carte Sac de Sable directement. */
    CarteSpeSac,

    /** Action : terminer le tour du joueur actif. */
    TerminerTour
}
