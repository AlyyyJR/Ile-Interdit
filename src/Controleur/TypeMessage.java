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
 * <p>Les messages permettent à chaque vue d'informer le contrôleur de l'action
 * choisie par l'utilisateur. Ils sont encapsulés dans {@link Message},
 * {@link MessageAventurier} ou {@link MessagePlateau} selon leur nature.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 * @see Message
 * @see MessageAventurier
 * @see MessagePlateau
 * @see Controleur
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
