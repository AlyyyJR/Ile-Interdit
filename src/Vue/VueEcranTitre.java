// ==================================================================
// FICHIER : VueEcranTitre.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Fenêtre d'accueil affichant le titre du jeu et le bouton Jouer.
//
// Rôle de VueEcranTitre :
//   - afficher l'écran titre avec le logo et le nom du jeu
//   - proposer un bouton Jouer pour démarrer la partie
//   - émettre TypeMessage.Jouer vers l'Observateur au clic
//
// Utilisé par : Controleur
// ==================================================================

package Vue;

import Controleur.Message;
import Controleur.Observateur;
import Controleur.TypeMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Écran d'accueil du jeu « L'Île Interdite ».
 *
 * <p>Affiche trois boutons sur un fond personnalisé :</p>
 * <ul>
 *   <li><b>Jouer</b> — envoie {@link TypeMessage#Jouer} au contrôleur ;</li>
 *   <li><b>Règles</b> — ouvre le fichier PDF des règles ;</li>
 *   <li><b>Quitter</b> — termine l'application.</li>
 * </ul>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VueEcranTitre extends JPanel {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Image de fond de l'écran titre. */
    private Image image;

    /** Largeur de la fenêtre en pixels. */
    private static final int WIDTH  = 800;

    /** Hauteur de la fenêtre en pixels. */
    private static final int HEIGHT = 650;

    /** Observateur MVC à notifier lors d'une action utilisateur. */
    private Observateur observateur;

    /** Fenêtre contenant cet écran titre. */
    private JFrame ecranTitreFenetre;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Construit le panneau d'écran titre avec ses boutons.
     */
    public VueEcranTitre() {
        super();

        // Chargement des images des boutons
        ImageIcon iconJouer  = loadImage("/resources/planches.jpg",       265, 65);
        ImageIcon iconQuitter = loadImage("/resources/planchesQuitter.jpg", 265, 65);
        ImageIcon iconRegles  = loadImage("/resources/planchesRegles.jpg",  265, 65);

        JButton jouer   = new JButton(iconJouer);
        JButton quitter = new JButton(iconQuitter);
        JButton regles  = new JButton(iconRegles);
        jouer.setContentAreaFilled(false);

        // Image de fond
        try {
            this.image = ImageIO.read(
                new File(System.getProperty("user.dir") + "/src/resources/fond.png"));
        } catch (IOException ex) {
            Logger.getLogger(VueEcranTitre.class.getName())
                  .log(Level.WARNING, "fond.png introuvable", ex);
        }

        // Mise en page
        this.setLayout(new BorderLayout());
        JComponent boutons = new JPanel(new GridLayout(10, 3));
        boutons.setOpaque(false);

        for (int i = 0; i < 13; i++) boutons.add(new JLabel());
        boutons.add(jouer);
        boutons.add(new JLabel()); boutons.add(new JLabel());
        boutons.add(regles);
        boutons.add(new JLabel()); boutons.add(new JLabel());
        boutons.add(quitter);
        for (int i = 0; i < 10; i++) boutons.add(new JLabel());

        this.add(boutons, BorderLayout.CENTER);

        // Actions
        jouer.addActionListener((ActionEvent e) -> {
            if (observateur != null) {
                observateur.traiterMessage(new Message(TypeMessage.Jouer));
            }
        });

        regles.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(
                    new File(System.getProperty("user.dir") + "/src/resources/regles.pdf"));
            } catch (IOException ex) {
                Logger.getLogger(VueEcranTitre.class.getName())
                      .log(Level.WARNING, "regles.pdf introuvable", ex);
            }
        });

        quitter.addActionListener(e -> System.exit(0));
    }

    // ==================================================================
    // Méthodes
    // ==================================================================

    /**
     * Affiche la fenêtre de l'écran titre centrée à l'écran.
     */
    public void afficher() {
        ecranTitreFenetre = new JFrame("L'Île Interdite : Écran Titre");
        ecranTitreFenetre.setSize(WIDTH, HEIGHT - 50);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        ecranTitreFenetre.setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
        ecranTitreFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ecranTitreFenetre.setResizable(false);
        ecranTitreFenetre.add(this);
        ecranTitreFenetre.setVisible(true);
        ecranTitreFenetre.repaint();
    }

    /**
     * Ferme la fenêtre de l'écran titre et libère ses ressources.
     */
    public void fermer() {
        if (ecranTitreFenetre != null) {
            ecranTitreFenetre.dispose();
        }
    }

    /**
     * Connecte l'observateur MVC à cette vue.
     *
     * @param o l'observateur (contrôleur) à notifier
     */
    public void setObservateur(Observateur o) {
        this.observateur = o;
    }

    /**
     * Redessine l'image de fond sur toute la surface du panneau.
     *
     * @param g le contexte graphique fourni par Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null, this);
    }

    /**
     * Charge et redimensionne une image depuis le classpath.
     *
     * @param path chemin dans le classpath
     * @param w    largeur cible en pixels
     * @param h    hauteur cible en pixels
     * @return l'icône redimensionnée
     */
    private ImageIcon loadImage(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
        return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
    }
}
