// ==================================================================
// FICHIER : CarteTresor.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Représente une carte de la pioche Trésor avec son type et son image.
//
// Rôle de CarteTresor :
//   - encapsuler le TypeCarte et l'image graphique d'une carte trésor
//   - charger automatiquement l'image depuis /ImagesCartesTresor/
//   - être distribuée aux joueurs et défaussée lors de l'utilisation
//
// Utilisé par : Joueur (mainJoueur), Controleur, VueAventurier, VueDefausse
// ==================================================================

package Modèle;

import javax.swing.ImageIcon;

/**
 * Représente une carte de la pioche Trésor.
 *
 * Chaque carte possède un {TypeCarte} et une image graphique chargée
 * automatiquement depuis les ressources du projet. Les cartes trésor classiques
 * {Trésor} doivent être collectées en quantité de 4 pour récupérer une relique.
 * Les cartes spéciales {Spécial} offrent des capacités ponctuelles.
 * La carte {MontéeEaux} déclenche une montée du niveau de l'eau et rebat la pioche inondation.
 * Les cartes sont utilisées par les joueurs pour progresser dans la partie, et leur gestion est centrale dans la stratégie du jeu. Elles peuvent être distribuées, échangées ou défaussées selon les actions des joueurs et les événements du jeu.
 * Par exemple, un joueur peut utiliser une carte Hélicoptère pour déplacer un groupe d'aventuriers vers une tuile éloignée, ou une carte Sac de Sable pour assécher une tuile inondée critique. La carte Montée des Eaux, quant à elle, peut bouleverser la partie en augmentant le niveau de l'eau et en réintroduisant des tuiles inondées dans la pioche, ce qui peut forcer les joueurs à adapter rapidement leur stratégie pour éviter de perdre la partie. 
 * En somme, les cartes trésor sont un élément clé du gameplay de L'Île Interdite, offrant à la fois des opportunités et des défis pour les joueurs tout au long de la partie. 
 */
public class CarteTresor {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Type de la carte (trésor, spéciale ou montée des eaux). */
    private final TypeCarte type;

    /** Image graphique représentant la carte dans l'interface. */
    private final ImageIcon image;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée une carte trésor du type spécifié et charge son image depuis les ressources.
     *
     * L'image est recherchée dans {/ImagesCartesTresor/{type.name()}.png}.
     *
     * @param type le type de la carte (non nul)
     */
    public CarteTresor(TypeCarte type) {
        this.type  = type;
        this.image = new ImageIcon(
            this.getClass().getResource("/ImagesCartesTresor/" + type.name() + ".png")
        );
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne le type de la carte.
     *
     * @return le type de la carte
     */
    public TypeCarte getType() {
        return type;
    }

    /**
     * Retourne l'image graphique de la carte.
     *
     * @return l'icône associée à cette carte
     */
    public ImageIcon getImage() {
        return image;
    }
}
