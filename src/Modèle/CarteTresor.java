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
 * <p>Chaque carte possède un {@link TypeCarte} et une image graphique chargée
 * automatiquement depuis les ressources du projet. Les cartes trésor classiques
 * ({@code Trésor*}) doivent être collectées en quantité de 4 pour récupérer une relique.
 * Les cartes spéciales ({@code Spécial*}) offrent des capacités ponctuelles.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
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
     * <p>L'image est recherchée dans {@code /ImagesCartesTresor/{type.name()}.png}.</p>
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
