// ==================================================================
// FICHIER : VueMonteeEaux.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Fenêtre affichant la jauge de montée des eaux et le niveau de difficulté courant.
//
// Rôle de VueMonteeEaux :
//   - afficher le niveau de montée des eaux (1 à 10) sous forme graphique
//   - mettre à jour l'affichage via monteDesEaux(int) à chaque montée
//   - rester visible en permanence pendant la partie
//
// Utilisé par : Controleur
// ==================================================================

package Vue;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenêtre affichant la carte du niveau d'eau courant.
 *
 * Une image correspondant au niveau (de 1 à 10) est chargée depuis
 * les ressources internes. La méthode {#monteDesEaux(int)} permet
 * de mettre à jour l'affichage dynamiquement quand le niveau monte.
 */
public class VueMonteeEaux extends JFrame {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Image de la carte de niveau d'eau actuellement affichée. */
    private ImageIcon image;

    /** Panneau principal contenant l'image. */
    private final JPanel mainPanel;

    /** Label graphique contenant l'image redimensionnée. */
    private JLabel imgCarte;

    /** Dimensions de l'écran (pour le positionnement et la mise à l'échelle). */
    private final Dimension dim;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée la fenêtre et affiche l'image correspondant au niveau initial.
     *
     * @param niveau niveau d'eau de départ (entre 1 et 10)
     */
    public VueMonteeEaux(int niveau) {
        super();
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (dim.getWidth() * 0.15), (int) (dim.getHeight() * 0.5));
        this.setLocation((int) (dim.getWidth() * 0.8), (int) (dim.getHeight() * 0.01));
        this.setTitle("Niveau d'eau");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        mainPanel = new JPanel();
        this.add(mainPanel);

        afficherCarte(niveau);
    }

    // ==================================================================
    // Méthodes
    // ==================================================================

    /**
     * Charge et affiche l'image du niveau d'eau spécifié.
     *
     * @param niveau niveau à afficher (entre 1 et 10)
     */
    private void afficherCarte(int niveau) {
        String chemin = "/resources/niveauDesEaux" + niveau + ".png";
        image = new ImageIcon(this.getClass().getResource(chemin));
        Image imgRedimensionnee = image.getImage().getScaledInstance(
            (int) (dim.getWidth() * 0.12),
            (int) (dim.getHeight() * 0.52),
            Image.SCALE_DEFAULT
        );
        imgCarte = new JLabel(new ImageIcon(imgRedimensionnee));
        mainPanel.add(imgCarte);
    }

    /**
     * Met à jour l'image affichée quand le niveau d'eau monte.
     *
     * @param niveau nouveau niveau d'eau (entre 1 et 10)
     */
    public void monteDesEaux(int niveau) {
        mainPanel.remove(imgCarte);
        afficherCarte(niveau);
        this.validate();
        this.repaint();
    }
}
