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
 * Envoyé depuis {Vue.VueAventurier} chaque fois que l'utilisateur
 * clique sur un bouton d'action (Déplacer, Assécher, Donner…).
 * Le {TypeMessage} indique l'action choisie et le {Joueur} désigne
 * le joueur actif dont le panneau a déclenché l'événement.
 *
 * 
 *MessageAventurier
 * ├── type : TypeMessage
 * └── aventurier : Joueur (pour identifier le joueur concerné) 
 * En somme, {MessageAventurier} est un élément clé du système de communication entre les vues et le contrôleur, permettant une interaction fluide et contextuelle entre l'interface utilisateur et la logique du jeu. Par exemple, lorsque le joueur "Alice" clique sur le bouton "Déplacer", la {VueAventurier} émet un {MessageAventurier} avec le type {DEPLACER} et la référence au joueur "Alice". Le contrôleur reçoit ce message, identifie que c'est "Alice" qui souhaite se déplacer, vérifie les déplacements possibles pour son rôle et sa position actuelle, puis met à jour le modèle et les vues en conséquence. De cette manière, {MessageAventurier} facilite une gestion efficace des actions des joueurs tout en maintenant une séparation claire entre la logique du jeu et l'interface utilisateur. 
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
