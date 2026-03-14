// ==================================================================
// FICHIER : VueFinDePartie.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Fenêtre de fin de partie affichant le résultat (victoire ou défaite).
//
// Rôle de VueFinDePartie :
//   - afficher le message de fin selon le code de finDeJeu (0=victoire, 1-4=défaite)
//   - proposer un bouton Quitter pour fermer l'application
//   - s'afficher après la fermeture de toutes les autres fenêtres
//
// Utilisé par : Controleur
// ==================================================================

package Vue;

import Controleur.Observateur;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenêtre de fin de partie affichant victoire ou défaite.
 *
 * La méthode {#update(int)} configure les textes selon le résultat :
 * 
 *   0 — Victoire : tous les joueurs se sont échappés en hélicoptère
 *   1 — Défaite : l'héliport a sombré
 *   2 — Défaite : un aventurier est mort
 *   3 — Défaite : une relique n'est plus récupérable
 *   4 — Défaite : le niveau d'eau a atteint le seuil critique
 *
 */
public class VueFinDePartie {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Fenêtre principale de fin de partie. */
    private final JFrame window;

    /** Label affichant « Victoire » ou « Défaite ». */
    private final JLabel titre;

    /** Label affichant la raison spécifique de la fin de partie. */
    private final JLabel sousTitre;

    /** Observateur MVC (non utilisé directement mais conservé pour extensibilité). */
    @SuppressWarnings("unused")
    private Observateur observateur;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise la fenêtre de fin de partie avec ses composants graphiques.
     */
    public VueFinDePartie() {
        // Police personnalisée
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                new File("src/resources/PiecesofEight.ttf")));
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(VueFinDePartie.class.getName()).log(Level.WARNING, null, ex);
        }

        window = new JFrame("Fin Du Jeu");
        window.setContentPane(new AfficheImage("src/resources/fondinscription.jpg"));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setResizable(false);

        Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - 400, dim.height / 2 - 300);

        // Panneaux
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        JPanel auxPanel  = new JPanel(new GridLayout(2, 1));
        JPanel buttPanel = new JPanel(new GridLayout(4, 5));

        mainPanel.setOpaque(false);
        auxPanel.setOpaque(false);
        buttPanel.setOpaque(false);

        // Titres
        titre = new JLabel();
        titre.setFont(new Font("Pieces of Eight", Font.PLAIN, 100));
        titre.setHorizontalAlignment(JLabel.CENTER);

        sousTitre = new JLabel();
        sousTitre.setFont(new Font("Pieces of Eight", Font.PLAIN, 50));
        sousTitre.setHorizontalAlignment(JLabel.CENTER);

        // Bouton Quitter
        JButton quitter = new JButton("Quitter");
        quitter.addActionListener(e -> System.exit(0));

        // Construction
        auxPanel.add(sousTitre);
        auxPanel.add(new JLabel());

        for (int i = 0; i < 9; i++) buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(quitter);
        buttPanel.add(new JLabel());

        mainPanel.add(titre);
        mainPanel.add(auxPanel);
        mainPanel.add(buttPanel);
        window.add(mainPanel);
    }

    // ==================================================================
    // Méthodes
    // ==================================================================

    /**
     * Configure les textes de la fenêtre selon le type de fin de partie.
     *
     * @param numFin code de fin : 0 = victoire, 1–4 = type de défaite
     */
    public void update(int numFin) {
        if (numFin == 0) {
            titre.setText(" * Victoire * ");
            sousTitre.setText("Les Aventuriers ont pu s'échapper");
        } else {
            titre.setText(" $ Défaite $ ");
            switch (numFin) {
                case 1 -> sousTitre.setText("L'Héliport a sombré");
                case 2 -> sousTitre.setText("Un Aventurier est mort");
                case 3 -> sousTitre.setText("Une Relique n'est plus récupérable");
                case 4 -> sousTitre.setText("L'Eau a atteint un niveau critique");
            }
        }
    }

    /**
     * Rend la fenêtre visible à l'écran.
     */
    public void afficher() {
        window.setVisible(true);
    }

    /**
     * Connecte l'observateur MVC à cette vue.
     *
     * @param o l'observateur (contrôleur) à notifier
     */
    public void setObservateur(Observateur o) {
        this.observateur = o;
    }
}
