// ==================================================================
// FICHIER : VueReliques.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Fenêtre affichant le statut des 4 reliques (récupérées ou non).
//
// Rôle de VueReliques :
//   - afficher les 4 reliques avec leur état (prise ou non) via des icônes
//   - mettre à jour l'affichage via update(boolean[]) après chaque prise
//   - rester visible en permanence pendant la partie
//
// Utilisé par : Controleur
// ==================================================================

package Vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenêtre affichant l'état des quatre reliques à collecter.
 *
 * <p>Chaque relique est initialement représentée par une version grisée.
 * Lorsqu'un joueur collecte une relique, {@link #update(boolean[])} remplace
 * l'image grisée correspondante par sa version colorée.</p>
 *
 * <p>Les quatre reliques, dans l'ordre d'index, sont :</p>
 * <ol>
 *   <li>Calice de l'Onde (Cyan)</li>
 *   <li>Pierre Sacrée (Gray)</li>
 *   <li>Statue du Zéphyr (Orange)</li>
 *   <li>Cristal Ardent (Magenta)</li>
 * </ol>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VueReliques extends JFrame {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Panneau principal de la fenêtre. */
    private final JPanel mainPanel;

    /** Labels affichant les quatre reliques. */
    private final JLabel[] imgReliques;

    /** Images grises initiales des reliques (non collectées). */
    private final ImageIcon[] imagesGrises;

    /** Dimensions de l'écran pour le positionnement et la mise à l'échelle. */
    private final Dimension dim;

    /** Chemins vers les images grises des reliques. */
    private static final String[] CHEMINS_GRIS = {
        "/resources/Trésors/CaliceG.png",
        "/resources/Trésors/PierreG.png",
        "/resources/Trésors/StatueG.png",
        "/resources/Trésors/CristalG.png"
    };

    /** Chemins vers les images colorées des reliques (collectées). */
    private static final String[] CHEMINS_COULEURS = {
        "/resources/Trésors/CaliceC.png",
        "/resources/Trésors/PierreC.png",
        "/resources/Trésors/StatueC.png",
        "/resources/Trésors/CristalC.png"
    };

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise la fenêtre avec les quatre reliques en version grisée.
     */
    public VueReliques() {
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (dim.getWidth() * 0.15), (int) (dim.getHeight() * 0.4));
        this.setLocation((int) (dim.getWidth() * 0.8), (int) (dim.getHeight() * 0.53));
        this.setResizable(false);
        this.setTitle("Reliques récupérées");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        JPanel panelReliques = new JPanel(new GridLayout(2, 2));
        imgReliques = new JLabel[4];
        imagesGrises = new ImageIcon[4];

        for (int i = 0; i < 4; i++) {
            imagesGrises[i] = loadImage(CHEMINS_GRIS[i]);
            imgReliques[i] = new JLabel(imagesGrises[i]);
            panelReliques.add(imgReliques[i]);
        }

        mainPanel.add(panelReliques, BorderLayout.CENTER);
        this.add(mainPanel);
    }

    // ==================================================================
    // Méthodes
    // ==================================================================

    /**
     * Met à jour l'affichage : remplace les reliques collectées par leur image colorée.
     *
     * @param reliquesPrises tableau de 4 booléens ({@code true} = relique collectée)
     */
    public void update(boolean[] reliquesPrises) {
        for (int i = 0; i < 4; i++) {
            if (reliquesPrises[i]) {
                imgReliques[i].setIcon(loadImage(CHEMINS_COULEURS[i]));
            }
        }
        this.validate();
        this.repaint();
    }

    /**
     * Charge une image et la redimensionne selon la taille de l'écran.
     *
     * @param chemin chemin de la ressource dans le classpath
     * @return l'icône redimensionnée
     */
    private ImageIcon loadImage(String chemin) {
        ImageIcon icon = new ImageIcon(this.getClass().getResource(chemin));
        return new ImageIcon(icon.getImage().getScaledInstance(
            (int) (dim.getWidth() * 0.098),
            (int) (dim.getHeight() * 0.16),
            Image.SCALE_DEFAULT
        ));
    }
}
