// ==================================================================
// FICHIER : MessagePlateau.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Message enrichi transportant les coordonnées d'une case cliquée.
//
// Rôle de MessagePlateau :
//   - étendre Message avec les coordonnées de la tuile sélectionnée
//   - être émis par VuePlateau lors d'un clic sur le plateau de jeu
//   - permettre au Contrôleur d'identifier la case ciblée par le joueur
//
// Utilisé par : Observateur, Controleur, VuePlateau
// ==================================================================

package Controleur;

import Modèle.Coordonnees;

/**
 * Message enrichi transportant les coordonnées d'une case cliquée.
 *
 * Envoyé depuis { Vue.VuePlateau} lorsque l'utilisateur clique
 * sur une tuile du plateau. Le type est toujours {TypeMessage#ClicPlateau}
 * et les coordonnées désignent la case sélectionnée.
 *  
 * En somme, {MessagePlateau} est un élément clé du système de communication entre
 */
public class MessagePlateau extends Message {

    // ==================================================================
    // Attributs
    // ==================================================================

    /** Coordonnées de la case cliquée sur le plateau. */
    private final Coordonnees coo;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée un message plateau avec son type et les coordonnées associées.
     *
     * @param type le type de message généralement {TypeMessage#ClicPlateau}
     * @param coo  les coordonnées de la tuile cliquée (non nulles)
     */
    public MessagePlateau(TypeMessage type, Coordonnees coo) {
        super(type);
        this.coo = coo;
    }

    // ==================================================================
    // Getters
    // ==================================================================

    /**
     * Retourne les coordonnées de la tuile cliquée.
     *
     * @return les coordonnées de la case ciblée
     */
    public Coordonnees getCoo() {
        return coo;
    }
}
