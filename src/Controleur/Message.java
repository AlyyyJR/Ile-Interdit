// ==================================================================
// FICHIER : Message.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Message de base échangé entre une Vue et l'Observateur (Contrôleur).
//
// Rôle de Message :
//   - encapsuler un TypeMessage identifiant l'action déclenchée
//   - servir de classe parente à MessageAventurier et MessagePlateau
//   - circuler entre les vues et le contrôleur via le patron Observateur
//
// Utilisé par : Observateur, Controleur, VueAventurier, VuePlateau,
//               VueCarteSpe, VueDefausse, VueDonDeCartes,
//               VueEcranTitre, VueInscription, VueMonteeEaux,
//               VueFinDePartie, VueReliques
// ==================================================================

package Controleur;

/**
 * Message de base échangé entre une Vue et l'Observateur (Contrôleur).
 *
 * Chaque message transporte un {TypeMessage} qui identifie
 * l'action déclenchée par l'utilisateur dans l'interface graphique.
 * Les sous-classes {MessageAventurier} et {MessagePlateau}
 * enrichissent ce message avec des données contextuelles.
 * 
 * Message
 * ├── type : TypeMessage
 * └── aventurier : Joueur (pour identifier le joueur concerné)
 *
 */
public class Message {

    // ==================================================================
    // Attributs
    // ==================================================================

    /** Type de l'action déclenchée. */
    private TypeMessage type;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée un message du type spécifié.
     *
     * @param type le type de message (non nul)
     */
    public Message(TypeMessage type) {
        this.type = type;
    }

    // ==================================================================
    // Getters / Setters
    // ==================================================================

    /**
     * Retourne le type de ce message.
     *
     * @return le type du message
     */
    public TypeMessage getType() {
        return type;
    }

    /**
     * Modifie le type de ce message.
     *
     * @param type le nouveau type
     */
    public void setType(TypeMessage type) {
        this.type = type;
    }
}
