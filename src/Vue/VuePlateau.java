// ==================================================================
// FICHIER : VuePlateau.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Vue principale du plateau de jeu — grille de 36 cases avec tuiles interactives.
//
// Rôle de VuePlateau :
//   - afficher la grille 6×6 de tuiles avec leurs états (sec, inondé, sombré)
//   - gérer les clics sur les tuiles et émettre des MessagePlateau
//   - surligner les cases accessibles lors d'un déplacement ou assèchement
//   - afficher les pions des aventuriers positionnés sur les tuiles
//
// Utilisé par : Controleur, Joueur (surligner, obligationDeplacement)
// ==================================================================

package Vue;

import Controleur.Controleur;
import Controleur.MessagePlateau;
import Controleur.Observateur;
import Controleur.TypeMessage;
import Modèle.Grille;
import Modèle.Joueur;
import Modèle.Tuile;
import Modèle.Zone;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Fenêtre principale du plateau de jeu.
 *
 * <p>Affiche la grille 6×6 de tuiles sur trois calques superposés :</p>
 * <ol>
 *   <li><b>Tuiles</b> — fond coloré selon l'état (Sec, Inondé) et image de la tuile ;</li>
 *   <li><b>Pions</b> — icônes des joueurs présents sur chaque case ;</li>
 *   <li><b>Boutons</b> — boutons invisibles capturant les clics utilisateur.</li>
 * </ol>
 *
 * <p>Lors d'une action (déplacement, assèchement…), les tuiles accessibles
 * sont surlignées en vert via {@link #surligner(ArrayList)}.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VuePlateau extends JFrame {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Contrôleur central du jeu. */
    private Controleur controleur;

    /** Observateur MVC (contrôleur) réceptionnant les clics plateau. */
    private Observateur observateur;

    /** Panneaux représentant l'image de chaque tuile. */
    private ArrayList<JPanel> caseTuiles;

    /** Panneaux contenant les pions des joueurs. */
    private ArrayList<JPanel> panelPions;

    /** Boutons invisibles captant les clics sur les tuiles. */
    private ArrayList<JButton> buttonsCase;

    /** Liste des tuiles à surligner en vert lors d'une action. */
    private ArrayList<Tuile> aSurligner;

    /** Calque principal gérant la superposition des couches. */
    private final JLayeredPane calque;

    /** Panneau de la couche tuiles (fond). */
    private final JPanel mapPanel;

    /** Panneau de la couche pions. */
    private final JComponent pionsPlateau;

    /** Panneau de la couche boutons (premier plan). */
    private final JComponent calqueButtons;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise la fenêtre du plateau avec tous ses composants graphiques.
     *
     * @param c le contrôleur central du jeu
     */
    public VuePlateau(Controleur c) {
        super("L'Ile Interdite : Jeu");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (dim.getWidth() * 0.55), (int) (dim.getHeight() * 0.95));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.controleur = c;

        caseTuiles   = new ArrayList<>();
        panelPions   = new ArrayList<>();
        buttonsCase  = new ArrayList<>();
        aSurligner   = new ArrayList<>();

        calque = new JLayeredPane();
        calque.setPreferredSize(this.getSize());

        mapPanel = new JPanel(new GridLayout(6, 6));
        mapPanel.setBounds(new Rectangle(this.getWidth(), this.getHeight() - 33));

        pionsPlateau = new JPanel(new GridLayout(6, 6));
        pionsPlateau.setOpaque(false);
        pionsPlateau.setBounds(mapPanel.getBounds());

        calqueButtons = new JPanel(new GridLayout(6, 6));
        calqueButtons.setOpaque(false);
        calqueButtons.setBounds(mapPanel.getBounds());

        peindreTuiles();

        calque.add(mapPanel,      Integer.valueOf(1));
        calque.add(pionsPlateau,  Integer.valueOf(2));
        calque.add(calqueButtons, Integer.valueOf(3));
        this.add(calque, BorderLayout.CENTER);
    }

    // ==================================================================
    // Rendu
    // ==================================================================

    /**
     * Parcourt la grille et construit les trois couches graphiques (tuiles, pions, boutons).
     */
    private void peindreTuiles() {
        Grille g = controleur.getGrille();

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {

                Tuile t = g.getTuile(x, y);

                // Bouton invisible (capture des clics)
                JButton bouton = new JButton("");
                bouton.setEnabled(false);
                buttonsCase.add(bouton);

                if (t != null) {
                    bouton.addActionListener(e -> {
                        MessagePlateau msg = new MessagePlateau(
                            TypeMessage.ClicPlateau, t.getCoordonees());
                        observateur.traiterMessagePlateau(msg);
                    });
                }

                // Panneaux tuile et pions
                JPanel panelTuile = new JPanel(new BorderLayout());
                JPanel panelPion  = new JPanel(new GridLayout(1, 4));
                caseTuiles.add(panelTuile);
                panelPions.add(panelPion);

                if (t == null) {
                    // Case vide (eau)
                    String imgPath = (x == 0 && y == 5)
                        ? "/ImagesTuiles/EauRoseVent.png"
                        : "/ImagesTuiles/Eau.png";
                    ImageIcon icona = new ImageIcon(this.getClass().getResource(imgPath));
                    icona = new ImageIcon(icona.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));
                    panelTuile.add(new JLabel(icona), BorderLayout.CENTER);
                } else {
                    // Couleur de fond selon l'état de la tuile
                    Color colorBack = switch (t.getEtat()) {
                        case Inondé -> new Color(10, 110, 230);
                        case Sec    -> new Color(240, 230, 140);
                        default     -> Color.LIGHT_GRAY;
                    };
                    panelTuile.setBackground(colorBack);
                    panelTuile.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    // Image de la tuile
                    ImageIcon icon = new ImageIcon(
                        t.getImage().getImage().getScaledInstance(170, 170, Image.SCALE_DEFAULT));
                    panelTuile.add(new JLabel(icon), BorderLayout.CENTER);

                    // Pions des joueurs présents
                    for (Joueur j : t.getLocataires()) {
                        if (j != null) {
                            panelPion.add(new JLabel(new ImageIcon(
                                j.getImage().getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT))),
                                BorderLayout.NORTH);
                        }
                    }

                    // Nom de la tuile
                    panelTuile.add(new JLabel(t.getIntitule().nomEspace()), BorderLayout.SOUTH);

                    // Bordure jaune pour l'héliport
                    if (t.getIntitule() == Zone.Heliport) {
                        panelTuile.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                    }

                    // Bordure verte si la tuile est surlignée
                    if (aSurligner.contains(t)) {
                        bouton.setBorder(BorderFactory.createLineBorder(new Color(89, 214, 114), 5));
                        bouton.setEnabled(true);
                    }
                }
            }
        }

        // Ajout sur les couches
        for (JPanel tuile  : caseTuiles)  mapPanel.add(tuile);

        for (JButton b : buttonsCase) {
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            calqueButtons.add(b);
        }

        for (JPanel pion : panelPions) {
            pion.setOpaque(false);
            pionsPlateau.add(pion);
        }
    }

    // ==================================================================
    // Méthodes publiques
    // ==================================================================

    /**
     * Définit les tuiles à surligner lors d'une action et relance l'affichage.
     *
     * @param tuiles la liste des tuiles accessibles à mettre en évidence
     */
    public void surligner(ArrayList<Tuile> tuiles) {
        aSurligner = tuiles;
        this.update();
    }

    /**
     * Rafraîchit complètement le contenu du plateau (toutes les couches).
     */
    public void update() {
        caseTuiles.clear();
        panelPions.clear();
        buttonsCase.clear();

        mapPanel.removeAll();
        pionsPlateau.removeAll();
        calqueButtons.removeAll();

        peindreTuiles();

        this.revalidate();
        this.repaint();
    }

    /**
     * Rend la fenêtre du plateau visible.
     */
    public void afficher() {
        this.setVisible(true);
    }

    /**
     * Définit le contrôleur central associé à cette vue.
     *
     * @param c le contrôleur du jeu
     */
    public void setControleur(Controleur c) {
        this.controleur = c;
    }

    /**
     * Connecte l'observateur MVC à cette vue.
     *
     * @param o l'observateur à notifier lors des clics
     */
    public void setObservateur(Observateur o) {
        this.observateur = o;
    }

    /**
     * Affiche une boîte de dialogue informant qu'un joueur doit se déplacer
     * car sa tuile est en train de sombrer.
     *
     * @param j le joueur en danger
     */
    public void obligationDeplacement(Joueur j) {
        JOptionPane.showMessageDialog(this,
            "La case sous " + j.getNom() + " sombre, il doit se déplacer.",
            "Aventurier en Danger",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Affiche un message de succès lorsqu'une relique est récupérée.
     *
     * @param nomRelique le nom de la relique acquise
     */
    public void signalerPriseRelique(String nomRelique) {
        JOptionPane.showMessageDialog(this,
            "La relique " + nomRelique + " a été ramassée.",
            "Relique Acquise",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
