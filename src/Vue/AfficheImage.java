// ==================================================================
// FICHIER : AfficheImage.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Composant JPanel affichant une image redimensionnée au fond d'un panneau Swing.
//
// Rôle de AfficheImage :
//   - dessiner une image de fond en la redimensionnant à la taille du composant
//   - être utilisé par VuePlateau pour les tuiles et VueAventurier pour les portraits
//   - fournir un rendu graphique immédiat via paintComponent
//
// Utilisé par : VuePlateau, VueAventurier, VueMonteeEaux, VueReliques
// ==================================================================

package Vue;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * Panneau affichant une image en fond plein écran.
 *
 * <p>Utilisé comme contenu de fenêtre pour les écrans personnalisés
 * (fin de partie, inscription…). L'image est chargée depuis un chemin
 * absolu ou relatif sur le disque et étirée pour remplir le panneau.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class AfficheImage extends JPanel {

    // ==================================================================
    // Attribut
    // ==================================================================
    /** Image dessinée en fond du panneau. */
    private final Image image;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Charge l'image depuis le chemin fourni.
     *
     * @param chemin chemin vers l'image (absolu ou relatif au répertoire de travail)
     */
    public AfficheImage(String chemin) {
        image = getToolkit().getImage(chemin);
    }

    // ==================================================================
    // Rendu
    // ==================================================================

    /**
     * Dessine l'image en fond, étirée pour couvrir l'intégralité du panneau.
     *
     * @param g le contexte graphique fourni par Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
