// ==================================================================
// FICHIER : VueDefausse.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Dialogue forçant le joueur à défausser une carte lorsque sa main dépasse 5 cartes.
//
// Rôle de VueDefausse :
//   - afficher la main complète du joueur sous forme de boutons d'image
//   - permettre la sélection et la défausse d'une carte en cliquant dessus
//   - notifier l'Observateur après chaque défausse
//
// Utilisé par : Joueur (defausserCarte), Controleur
// ==================================================================

package Vue;

import Controleur.Observateur;
import Modèle.CarteTresor;
import Modèle.Joueur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Fenêtre de défausse de carte pour un joueur ayant dépassé la limite.
 *
 * <p>S'affiche automatiquement lorsqu'un joueur possède 6 cartes ou plus.
 * Il doit en sélectionner une à défausser avant de continuer à jouer.
 * La carte choisie est retirée de sa main et ajoutée à la défausse via
 * {@link Joueur#defausserCarte(CarteTresor)}.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VueDefausse {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Fenêtre principale de la vue. */
    private final JFrame window;

    /** Boutons représentant les cartes en main du joueur. */
    private final ArrayList<JButton> cartesBoutons = new ArrayList<>();

    /** Observateur MVC à notifier après la défausse. */
    private final Observateur observateur;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée et affiche la fenêtre de défausse pour le joueur spécifié.
     *
     * @param joueur le joueur qui doit défausser une carte
     */
    public VueDefausse(Joueur joueur) {
        window = new JFrame("Défausser une carte");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(520, 180);
        window.setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - 260, dim.height / 2 - 90);

        observateur = joueur.getVueAventurier().getObservateur();

        JPanel mainPanel = new JPanel(new BorderLayout());
        window.add(mainPanel);

        JLabel messageDef = new JLabel(
            "Le joueur " + joueur.getNom()
            + " a trop de cartes : il doit en défausser pour n'en garder que 5 au maximum."
        );
        mainPanel.add(messageDef, BorderLayout.NORTH);

        JPanel cartesADefausser = new JPanel(new GridLayout(1, 9));
        cartesADefausser.setBorder(BorderFactory.createTitledBorder("Choisir une carte à défausser"));

        for (CarteTresor carte : joueur.getMainJoueur()) {
            JButton boutonCarte = new JButton(carte.getImage());
            cartesBoutons.add(boutonCarte);
            cartesADefausser.add(boutonCarte);

            boutonCarte.addActionListener(e -> {
                joueur.defausserCarte(carte);
                window.dispose();
                observateur.notifier();
            });
        }

        mainPanel.add(cartesADefausser, BorderLayout.CENTER);
        window.setVisible(true);
    }
}
