// ==================================================================
// FICHIER : MessageAventurier.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Message enrichi transportant une référence au joueur concerné.
//
// Rôle de MessageAventurier :
//   - étendre Message avec la référence du joueur ayant déclenché l'action
//   - être émis par VueAventurier lors des clics sur les boutons d'action
//   - permettre au Contrôleur d'identifier quel joueur est concerné
//
// Utilisé par : Observateur, Controleur, VueAventurier
// ==================================================================

package Controleur;

import Modèle.Joueur;

/**
 * Message enrichi transportant une référence au joueur concerné.
 *
 * <p>Envoyé depuis {@link Vue.VueAventurier} chaque fois que l'utilisateur
 * clique sur un bouton d'action (Déplacer, Assécher, Donner…).
 * Le {@link TypeMessage} indique l'action choisie et le {@link Joueur} désigne
 * le joueur actif dont le panneau a déclenché l'événement.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 * @see Message
 * @see TypeMessage
 * @see Modèle.Joueur
 */
public class MessageAventurier extends Message {

    // ==================================================================
    // Attributs
    // ==================================================================

    /** Joueur à l'origine de l'action. */
    private final Joueur joueur;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée un message d'aventurier avec son type et le joueur associé.
     *
     * @param type   le type d'action déclenchée (non nul)
     * @param joueur le joueur ayant déclenché l'action (non nul)
     */
    public MessageAventurier(TypeMessage type, Joueur joueur) {
        super(type);
        this.joueur = joueur;
    }

    // ==================================================================
    // Getters
    // ==================================================================

    /**
     * Retourne le joueur associé à ce message.
     *
     * @return le joueur émetteur du message
     */
    public Joueur getJoueur() {
        return joueur;
    }
}
