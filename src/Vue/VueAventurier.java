// ==================================================================
// FICHIER : VueAventurier.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Fenêtre individuelle affichant les informations et les boutons d'action d'un joueur.
//
// Rôle de VueAventurier :
//   - afficher le nom, le rôle, la couleur et la main de cartes du joueur
//   - proposer les boutons Aller, Assécher, Donner, Relique, Carte Spé, Fin Tour
//   - activer ou désactiver les boutons selon les actions disponibles
//   - émettre des MessageAventurier vers l'Observateur à chaque clic
//
// Utilisé par : Controleur, Joueur (setVueAventurier)
// ==================================================================

package Vue;

import Controleur.MessageAventurier;
import Controleur.Observateur;
import Controleur.TypeMessage;
import Modèle.CarteTresor;
import Modèle.Joueur;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Fenêtre graphique associée à un aventurier.
 *
 * <p>Affiche trois zones :</p>
 * <ul>
 *   <li><b>Nord</b> — nom du rôle avec la couleur du joueur en fond ;</li>
 *   <li><b>Centre</b> — les cartes Trésor en main ;</li>
 *   <li><b>Sud</b> — les six boutons d'action (Déplacer, Assécher, Donner,
 *       Prendre Relique, Carte Spéciale, Terminer Tour).</li>
 * </ul>
 *
 * <p>Les boutons sont activés ou désactivés par le contrôleur à chaque début
 * d'action selon les possibilités du joueur actif.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VueAventurier {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Fenêtre Swing dédiée à ce joueur. */
    private final JFrame window;

    /** Panneau contenant les images des cartes en main. */
    private final JPanel panelCartes;

    /** Bouton d'action : déplacer le joueur. */
    private final JButton btnAller;

    /** Bouton d'action : assécher une tuile inondée. */
    private final JButton btnAssecher;

    /** Bouton d'action : donner une carte à un coéquipier. */
    private final JButton btnDonner;

    /** Bouton d'action : prendre la relique sur la tuile courante. */
    private final JButton btnRelique;

    /** Bouton d'action : utiliser une carte spéciale. */
    private final JButton btnCarteSpeciale;

    /** Bouton d'action : terminer le tour. */
    private final JButton btnTerminerTour;

    /** Joueur associé à cette vue. */
    private final Joueur joueur;

    /** Observateur MVC partagé entre toutes les instances (static). */
    private static Observateur observateur;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée la fenêtre aventurier pour le joueur spécifié.
     *
     * @param j le joueur associé à cette vue
     */
    public VueAventurier(Joueur j) {
        this.joueur = j;

        window = new JFrame(j.getNom());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int) (dim.getWidth() * 0.2), (int) (dim.getHeight() * 0.22));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // ==================================================================
        // Panel principal
        // ==================================================================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 230));
        mainPanel.setBorder(BorderFactory.createLineBorder(j.getCouleur(), 2));
        window.add(mainPanel);

        // ==================================================================
        // NORD : nom du rôle
        // ==================================================================
        JPanel panelAventurier = new JPanel();
        panelAventurier.setBackground(j.getCouleur());
        JLabel labelNom = new JLabel(j.getClass().toString().substring(13), SwingConstants.CENTER);
        if (j.getCouleur() == Color.BLACK) {
            labelNom.setForeground(Color.WHITE); // lisibilité pour le Plongeur
        }
        panelAventurier.add(labelNom);
        mainPanel.add(panelAventurier, BorderLayout.NORTH);

        // ==================================================================
        // CENTRE : cartes en main
        // ==================================================================
        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.setBorder(new MatteBorder(0, 0, 2, 0, j.getCouleur()));
        panelCentre.add(new JLabel("Cartes", SwingConstants.CENTER), BorderLayout.NORTH);

        panelCartes = new JPanel(new GridLayout(1, 5));
        for (CarteTresor c : j.getMainJoueur()) {
            panelCartes.add(new JLabel(
                new ImageIcon(c.getImage().getImage().getScaledInstance(77, 77, Image.SCALE_DEFAULT))
            ));
        }
        panelCentre.add(panelCartes, BorderLayout.CENTER);
        mainPanel.add(panelCentre, BorderLayout.CENTER);

        // ==================================================================
        // SUD : boutons d'action
        // ==================================================================
        JPanel panelBoutons = new JPanel(new GridLayout(3, 2));
        mainPanel.add(panelBoutons, BorderLayout.SOUTH);

        btnAller = new JButton("Déplacer");
        btnAller.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.Deplacer, j)));

        btnAssecher = new JButton("Assécher");
        btnAssecher.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.Assecher, j)));

        btnDonner = new JButton("Donner une Carte");
        btnDonner.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.Donner, j)));

        btnRelique = new JButton("Prendre Relique");
        btnRelique.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.PrendreRelique, j)));

        btnCarteSpeciale = new JButton("Carte Spéciale");
        btnCarteSpeciale.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.CarteSpe, j)));

        btnTerminerTour = new JButton("Terminer Tour");
        btnTerminerTour.addActionListener(e ->
            getObservateur().traiterMessageAventurier(
                new MessageAventurier(TypeMessage.TerminerTour, j)));

        panelBoutons.add(btnAller);
        panelBoutons.add(btnAssecher);
        panelBoutons.add(btnDonner);
        panelBoutons.add(btnRelique);
        panelBoutons.add(btnCarteSpeciale);
        panelBoutons.add(btnTerminerTour);

        window.setVisible(true);
        mainPanel.repaint();
    }

    // ==================================================================
    // Affichage
    // ==================================================================

    /**
     * Retourne la fenêtre Swing associée à ce joueur.
     *
     * @return la fenêtre {@link JFrame}
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Ferme et libère la fenêtre.
     */
    public void dispose() {
        window.dispose();
    }

    /**
     * Rafraîchit l'affichage des cartes en main du joueur.
     */
    public void update() {
        panelCartes.removeAll();
        for (CarteTresor c : joueur.getMainJoueur()) {
            panelCartes.add(new JLabel(
                new ImageIcon(c.getImage().getImage().getScaledInstance(77, 77, Image.SCALE_DEFAULT))
            ));
        }
        window.validate();
        window.repaint();
    }

    // ==================================================================
    // Gestion des boutons
    // ==================================================================

    /**
     * Désactive tous les boutons d'action.
     */
    public void desactiverBoutons() {
        btnAller.setEnabled(false);
        btnAssecher.setEnabled(false);
        btnDonner.setEnabled(false);
        btnRelique.setEnabled(false);
        btnCarteSpeciale.setEnabled(false);
        btnTerminerTour.setEnabled(false);
    }

    /**
     * Active uniquement le bouton « Terminer Tour ».
     * Appelé en début de tour pour garantir un minimum d'interaction.
     */
    public void activerBoutons() {
        btnTerminerTour.setEnabled(true);
    }

    /**
     * Active le bouton « Déplacer ».
     */
    public void activerBoutonAller() {
        btnAller.setEnabled(true);
    }

    /**
     * Active le bouton « Assécher ».
     */
    public void activerBoutonAssecher() {
        btnAssecher.setEnabled(true);
    }

    /**
     * Active le bouton « Donner une Carte ».
     */
    public void activerBoutonDonner() {
        btnDonner.setEnabled(true);
    }

    /**
     * Active le bouton « Prendre Relique ».
     */
    public void activerBoutonRelique() {
        btnRelique.setEnabled(true);
    }

    /**
     * Active le bouton « Carte Spéciale ».
     */
    public void activerBoutonCS() {
        btnCarteSpeciale.setEnabled(true);
    }

    // ==================================================================
    // Observateur
    // ==================================================================

    /**
     * Retourne l'observateur partagé entre toutes les vues aventurier.
     *
     * @return l'observateur MVC
     */
    public static Observateur getObservateur() {
        return observateur;
    }

    /**
     * Connecte l'observateur MVC à toutes les instances de cette vue.
     *
     * @param o l'observateur à définir
     */
    public void setObservateur(Observateur o) {
        observateur = o;
    }
}
