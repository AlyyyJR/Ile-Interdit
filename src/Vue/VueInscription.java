// ==================================================================
// FICHIER : VueInscription.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Formulaire d'inscription permettant de configurer les joueurs et la difficulté.
//
// Rôle de VueInscription :
//   - afficher les champs nom et combo rôle pour 2 à 4 joueurs
//   - proposer le choix de la difficulté et le mode debug
//   - émettre TypeMessage.Valider ou TypeMessage.Annuler
//
// Utilisé par : Controleur (inscriptionJoueurs)
// ==================================================================

package Vue;

import Controleur.Message;
import Controleur.Observateur;
import Controleur.TypeMessage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Fenêtre d'inscription des joueurs avant le démarrage d'une partie.
 *
 * Permet de configurer jusqu'à 4 joueurs (nom et rôle), de choisir
 * la difficulté et d'activer le mode debug. Les rôles disponibles sont :
 * Aléatoire, Pilote, Messager, Explorateur, Navigateur, Plongeur, Ingénieur, Vide.
 *
 * Le bouton « Valider » vérifie l'unicité des rôles non-aléatoires avant
 * d'envoyer un {TypeMessage#Valider} au contrôleur.
 */
public class VueInscription {

    // ==================================================================
    // Attributs graphiques
    // ==================================================================
    /** Fenêtre principale de l'inscription. */
    private final JFrame window;

    // Champs de saisie des noms
    private static JTextField nomFieldJ1;
    private static JTextField nomFieldJ2;
    private static JTextField nomFieldJ3;
    private static JTextField nomFieldJ4;

    // Menus déroulants des rôles
    @SuppressWarnings("rawtypes")
    private static JComboBox roleComboJ1;
    @SuppressWarnings("rawtypes")
    private static JComboBox roleComboJ2;
    @SuppressWarnings("rawtypes")
    private static JComboBox roleComboJ3;
    @SuppressWarnings("rawtypes")
    private static JComboBox roleComboJ4;

    /** Menu déroulant de sélection de la difficulté. */
    @SuppressWarnings("rawtypes")
    private JComboBox comboDiff;

    /** Case à cocher pour activer le mode debug. */
    private JCheckBox checkDebug;

    /** Observateur MVC à notifier lors de la validation. */
    private static Observateur observateur;

    /** Liste des rôles disponibles (dernier = "Vide" pour joueurs optionnels). */
    private final String[] roles = {
        "Aléatoire", "Pilote", "Messager", "Explorateur",
        "Navigateur", "Plongeur", "Ingénieur", "Vide"
    };

    /** Niveaux de difficulté proposés dans l'interface. */
    private final String[] niveauxDiff = {
        "Novice", "Normal", "Élite", "Légendaire", "Mortel"
    };

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Construit et initialise la fenêtre d'inscription avec tous ses composants.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public VueInscription() {
        // Police personnalisée
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                new File("src/resources/PiecesofEight.ttf")));
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.WARNING, null, ex);
        }

        // ==================================================================
        // Fenêtre principale
        // ==================================================================
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(
            dim.width  / 2 - window.getSize().width  / 2,
            dim.height / 2 - window.getSize().height / 2);
        window.setTitle("L'Ile Interdite : Inscription");

        // ==================================================================
        // Structure principale
        // ==================================================================
        JComponent mainPanel = new JPanel(new BorderLayout());
        ((JPanel) mainPanel).setPreferredSize(
            new Dimension((int) window.getSize().getWidth() - 7,
                          (int) window.getSize().getHeight() - 35));
        ((JPanel) mainPanel).setOpaque(false);
        window.setContentPane(new AfficheImage("src/resources/fondinscription.jpg"));
        window.add(mainPanel);

        JPanel titrePanel  = new JPanel();
        JPanel buttPanel   = new JPanel(new GridLayout(2, 5));
        JPanel centrePanel = new JPanel(new GridLayout(2, 2));
        buttPanel.setOpaque(false);
        centrePanel.setOpaque(false);

        // ==================================================================
        // Panneaux joueurs
        // ==================================================================
        JPanel j1Panel = new JPanel(new BorderLayout()); j1Panel.setOpaque(false);
        JPanel j2Panel = new JPanel(new BorderLayout()); j2Panel.setOpaque(false);
        JPanel j3Panel = new JPanel(new BorderLayout()); j3Panel.setOpaque(false);
        JPanel j4Panel = new JPanel(new BorderLayout()); j4Panel.setOpaque(false);

        // ==================================================================
        // Titre
        // ==================================================================
        JLabel titreLabel = new JLabel("Inscription de Joueurs");
        titreLabel.setFont(new Font("Pieces of Eight", Font.PLAIN, 48));
        titreLabel.setOpaque(false);

        // ==================================================================
        // Boutons Valider / Quitter
        // ==================================================================
        JButton buttVal = new JButton("Valider");
        JButton buttQui = new JButton("Quitter");

        // ==================================================================
        // Champs Joueur 1
        // ==================================================================
        JLabel nomLabel1  = new JLabel("     Nom : ");
        JLabel roleLabel1 = new JLabel("     Role : ");
        JLabel j1Label    = new JLabel("Joueur 1 :");
        JPanel j1Grid     = new JPanel(new GridLayout(4, 2)); j1Grid.setOpaque(false);
        nomFieldJ1  = new JTextField("Aly");
        roleComboJ1 = new JComboBox(roles);
        roleComboJ1.setSelectedItem(roles[0]);
        roleComboJ1.removeItem(roles[7]);

        // ==================================================================
        // Champs Joueur 2
        // ==================================================================
        JLabel nomLabel2  = new JLabel("     Nom : ");
        JLabel roleLabel2 = new JLabel("     Role : ");
        JLabel j2Label    = new JLabel("Joueur 2 :");
        JPanel j2Grid     = new JPanel(new GridLayout(4, 2)); j2Grid.setOpaque(false);
        nomFieldJ2  = new JTextField("Julien");
        roleComboJ2 = new JComboBox(roles);
        roleComboJ2.setSelectedItem(roles[0]);
        roleComboJ2.removeItem(roles[7]);

        // ==================================================================
        // Champs Joueur 3
        // ==================================================================
        JLabel nomLabel3  = new JLabel("     Nom : ");
        JLabel roleLabel3 = new JLabel("     Role : ");
        JLabel j3Label    = new JLabel("Joueur 3 :");
        JPanel j3Grid     = new JPanel(new GridLayout(4, 2)); j3Grid.setOpaque(false);
        nomFieldJ3  = new JTextField(" ");
        roleComboJ3 = new JComboBox(roles);
        roleComboJ3.setSelectedItem(roles[7]);

        // ==================================================================
        // Champs Joueur 4
        // ==================================================================
        JLabel nomLabel4  = new JLabel("     Nom : ");
        JLabel roleLabel4 = new JLabel("     Role : ");
        JLabel j4Label    = new JLabel("Joueur 4 :");
        JPanel j4Grid     = new JPanel(new GridLayout(4, 2)); j4Grid.setOpaque(false);
        nomFieldJ4  = new JTextField(" ");
        roleComboJ4 = new JComboBox(roles);
        roleComboJ4.setSelectedItem(roles[7]);

        // ==================================================================
        // Mise en page
        // ==================================================================
        mainPanel.add(titrePanel,  BorderLayout.NORTH);
        mainPanel.add(buttPanel,   BorderLayout.SOUTH);
        mainPanel.add(centrePanel, BorderLayout.CENTER);

        centrePanel.add(j1Panel);
        centrePanel.add(j2Panel);
        centrePanel.add(j3Panel);
        centrePanel.add(j4Panel);

        titrePanel.add(titreLabel);
        titrePanel.setOpaque(false);

        // Difficulté + debug + boutons bas
        Font fontPieces = new Font("Pieces of Eight", Font.PLAIN, 24);
        JLabel diffLabel = new JLabel("Difficulté : ");
        diffLabel.setFont(fontPieces);
        diffLabel.setHorizontalAlignment(JLabel.RIGHT);
        buttPanel.add(diffLabel);
        comboDiff = new JComboBox(getNiveauxDiff());
        buttPanel.add(comboDiff);
        buttPanel.add(new JLabel(" "));
        checkDebug = new JCheckBox("Mode Debug");
        checkDebug.setFont(fontPieces);
        checkDebug.setOpaque(false);
        buttPanel.add(checkDebug);
        buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(buttVal);
        buttPanel.add(new JLabel());
        buttPanel.add(buttQui);
        buttPanel.add(new JLabel());

        // Helpers pour peupler les panneaux joueurs
        ajouterChampsJoueur(j1Panel, j1Grid, j1Label, nomLabel1, roleLabel1,
            nomFieldJ1, roleComboJ1, "Joueur 1 :", "Aly");
        ajouterChampsJoueur(j2Panel, j2Grid, j2Label, nomLabel2, roleLabel2,
            nomFieldJ2, roleComboJ2, "Joueur 2 :", "Julien");
        ajouterChampsJoueur(j3Panel, j3Grid, j3Label, nomLabel3, roleLabel3,
            nomFieldJ3, roleComboJ3, "Joueur 3 :", " ");
        ajouterChampsJoueur(j4Panel, j4Grid, j4Label, nomLabel4, roleLabel4,
            nomFieldJ4, roleComboJ4, "Joueur 4 :", " ");

        // ==================================================================
        // Action : Valider
        // ==================================================================
        buttVal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rolesValides()) {
                    observateur.traiterMessage(new Message(TypeMessage.Valider));
                } else {
                    fenetreInfo();
                }
            }
        });

        // ==================================================================
        // Action : Quitter
        // ==================================================================
        buttQui.addActionListener(e ->
            observateur.traiterMessage(new Message(TypeMessage.Annuler)));
    }

    /**
     * Peuple un panneau joueur avec ses labels, champs de saisie et menus déroulants.
     *
     * @param panel    panneau principal du joueur
     * @param grid     grille interne 4×2
     * @param titre    label de titre (ex. "Joueur 1 :")
     * @param nomLbl   label "Nom :"
     * @param roleLbl  label "Role :"
     * @param nomField champ de saisie du nom
     * @param roleBox  menu déroulant des rôles
     * @param titreTxt texte du titre
     * @param nomDef   valeur par défaut du nom
     */
    @SuppressWarnings("rawtypes")
    private void ajouterChampsJoueur(JPanel panel, JPanel grid, JLabel titre,
                                     JLabel nomLbl, JLabel roleLbl,
                                     JTextField nomField, JComboBox roleBox,
                                     String titreTxt, String nomDef) {
        Font f = new Font("Pieces of Eight", Font.PLAIN, 24);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nomLbl.setText("     Nom : ");
        nomLbl.setFont(f);
        roleLbl.setText("     Role : ");
        roleLbl.setFont(f);
        nomField.setText(nomDef);
        titre.setText(titreTxt);
        titre.setFont(new Font("Pieces of Eight", Font.PLAIN, 30));

        panel.add(titre, BorderLayout.NORTH);
        panel.add(grid,  BorderLayout.CENTER);

        grid.add(nomLbl);
        grid.add(nomField);
        grid.add(new JLabel());
        grid.add(new JLabel());
        grid.add(roleLbl);
        grid.add(roleBox);
        grid.add(new JLabel());
        grid.add(new JLabel());
    }

    /**
     * Vérifie qu'aucun rôle explicite n'est sélectionné par deux joueurs différents.
     *
     * @return {@code true} si la sélection est valide, {@code false} sinon
     */
    private boolean rolesValides() {
        String r1 = getRoleComboJ1();
        String r2 = getRoleComboJ2();
        String r3 = getRoleComboJ3();
        String r4 = getRoleComboJ4();

        if (!isAleatoireVide(r1) && !isAleatoireVide(r2) && r1.equals(r2)) return false;
        if (!isAleatoireVide(r1) && !isAleatoireVide(r3) && r1.equals(r3)) return false;
        if (!isAleatoireVide(r1) && !isAleatoireVide(r4) && r1.equals(r4)) return false;
        if (!isAleatoireVide(r2) && !isAleatoireVide(r3) && r2.equals(r3)) return false;
        if (!isAleatoireVide(r2) && !isAleatoireVide(r4) && r2.equals(r4)) return false;
        if (!isAleatoireVide(r3) && !isAleatoireVide(r4) && r3.equals(r4)) return false;
        return true;
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /** @return le nom saisi pour le joueur 1 */
    public static String getNomFieldJ1() { return nomFieldJ1.getText(); }

    /** @return le rôle sélectionné pour le joueur 1 */
    public static String getRoleComboJ1() { return roleComboJ1.getSelectedItem().toString(); }

    /** @return le nom saisi pour le joueur 2 */
    public static String getNomFieldJ2() { return nomFieldJ2.getText(); }

    /** @return le rôle sélectionné pour le joueur 2 */
    public static String getRoleComboJ2() { return roleComboJ2.getSelectedItem().toString(); }

    /** @return le nom saisi pour le joueur 3 */
    public static String getNomFieldJ3() { return nomFieldJ3.getText(); }

    /** @return le rôle sélectionné pour le joueur 3 */
    public static String getRoleComboJ3() { return roleComboJ3.getSelectedItem().toString(); }

    /** @return le nom saisi pour le joueur 4 */
    public static String getNomFieldJ4() { return nomFieldJ4.getText(); }

    /** @return le rôle sélectionné pour le joueur 4 */
    public static String getRoleComboJ4() { return roleComboJ4.getSelectedItem().toString(); }

    /**
     * Retourne la difficulté sélectionnée dans le menu déroulant.
     *
     * @return la difficulté sous forme de chaîne (ex. "Normal")
     */
    public String getComboDiff() { return comboDiff.getSelectedItem().toString(); }

    /**
     * Retourne l'état de la case à cocher « Mode Debug ».
     *
     * @return {@code true} si le mode debug est activé
     */
    public boolean getCheckDebug() { return checkDebug.isSelected(); }

    /**
     * Retourne le tableau des niveaux de difficulté disponibles.
     *
     * @return les niveaux de difficulté
     */
    public String[] getNiveauxDiff() { return niveauxDiff; }

    /**
     * Retourne la fenêtre principale de l'inscription.
     *
     * @return la fenêtre {@link JFrame}
     */
    public JFrame getWindow() { return window; }

    /**
     * Retourne le tableau des rôles jouables.
     *
     * @return les rôles disponibles
     */
    public String[] getRoles() { return roles; }

    // ==================================================================
    // Méthodes utilitaires
    // ==================================================================

    /**
     * Connecte l'observateur MVC à toutes les instances de cette vue.
     *
     * @param o l'observateur à définir
     */
    public static void setObservateur(Observateur o) { observateur = o; }

    /**
     * Rend la fenêtre d'inscription visible.
     */
    public void afficher() { window.setVisible(true); }

    /**
     * Indique si la valeur de rôle donnée représente "Aléatoire" ou "Vide".
     *
     * Ces deux valeurs sont ignorées lors de la vérification d'unicité des rôles.
     *
     * @param s la valeur à tester
     * @return {@code true} si {@code s} est "Aléatoire" ou "Vide"
     */
    public boolean isAleatoireVide(String s) {
        return "Aléatoire".equals(s) || "Vide".equals(s);
    }

    /**
     * Affiche une boîte de dialogue d'erreur signalant un doublon de rôle.
     */
    public void fenetreInfo() {
        JOptionPane.showMessageDialog(null,
            "Plusieurs Joueurs ne peuvent pas avoir le même rôle",
            "Erreur",
            JOptionPane.ERROR_MESSAGE);
    }
}
