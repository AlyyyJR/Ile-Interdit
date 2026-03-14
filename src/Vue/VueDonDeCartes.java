// ==================================================================
// FICHIER : VueDonDeCartes.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Dialogue permettant au joueur actif de choisir une carte et un destinataire pour un don.
//
// Rôle de VueDonDeCartes :
//   - afficher les cartes du joueur donneur et la liste des joueurs receveurs
//   - valider le don et notifier l'Observateur
//   - fermer la fenêtre automatiquement après validation
//
// Utilisé par : Joueur (donnerCarte), Controleur
// ==================================================================

package Vue;

import Controleur.Observateur;
import Modèle.CarteTresor;
import Modèle.Joueur;
import Modèle.TypeCarte;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Fenêtre de don de carte entre deux joueurs.
 *
 * <p>Le don se déroule en deux étapes :</p>
 * <ol>
 *   <li>Sélection du joueur receveur parmi ceux présents sur la case
 *       (ou tous, dans le cas du {@link Modèle.Messager}) ;</li>
 *   <li>Sélection de la carte à donner, en excluant les cartes spéciales
 *       (Hélicoptère et Sac de Sable).</li>
 * </ol>
 * <p>Une fois la sélection faite, la carte est transférée via
 * {@link Joueur#donnerCarte(Joueur, CarteTresor)} et l'observateur est notifié.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class VueDonDeCartes {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Fenêtre principale de la vue. */
    private final JFrame window;

    /** Joueur receveur sélectionné lors de la première étape. */
    private Joueur joueurReceveur;

    /** Boutons de sélection des joueurs (première étape). */
    private final ArrayList<JButton> joueursBoutons = new ArrayList<>();

    /** Boutons de sélection des cartes (deuxième étape). */
    private final ArrayList<JButton> cartesBoutons = new ArrayList<>();

    /** Observateur MVC à notifier après le don. */
    private final Observateur observateur;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée et affiche la fenêtre de don de cartes.
     *
     * @param joueurActuel   le joueur qui effectue le don
     * @param joueursSurCase la liste des joueurs pouvant recevoir (selon le rôle)
     */
    public VueDonDeCartes(Joueur joueurActuel, ArrayList<Joueur> joueursSurCase) {
        window = new JFrame("Don de Cartes");
        window.setSize(500, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - 250, dim.height / 2 - 150);

        JPanel mainPanel = new JPanel(new BorderLayout());
        window.add(mainPanel);

        observateur = joueurActuel.getVueAventurier().getObservateur();

        // ==================================================================
        // Étape 1 : choix du joueur receveur
        // ==================================================================
        JPanel listeJoueurs = new JPanel(new GridLayout(1, joueursSurCase.size()));
        listeJoueurs.setBorder(BorderFactory.createTitledBorder("Choix du joueur receveur"));

        for (Joueur j : joueursSurCase) {
            if (j != joueurActuel) {
                JButton boutonJoueur = new JButton(j.getImage());
                joueursBoutons.add(boutonJoueur);
                listeJoueurs.add(boutonJoueur);

                boutonJoueur.addActionListener(e -> {
                    joueurReceveur = j;
                    joueursBoutons.forEach(b -> b.setEnabled(false));
                    cartesBoutons.forEach(b -> b.setEnabled(true));
                });
            }
        }

        // ==================================================================
        // Étape 2 : choix de la carte à donner
        // ==================================================================
        JPanel cartesADonner = new JPanel(new GridLayout(1, 9));
        cartesADonner.setBorder(BorderFactory.createTitledBorder("Carte à donner"));

        for (CarteTresor carte : joueurActuel.getMainJoueur()) {
            if (carte.getType() != TypeCarte.SpécialHélicoptère
                    && carte.getType() != TypeCarte.SpécialSacDeSable) {

                JButton boutonCarte = new JButton(carte.getImage());
                boutonCarte.setEnabled(false);
                cartesBoutons.add(boutonCarte);
                cartesADonner.add(boutonCarte);

                boutonCarte.addActionListener(e -> {
                    cartesBoutons.forEach(b -> b.setEnabled(false));
                    joueurActuel.donnerCarte(joueurReceveur, carte);
                    window.dispose();
                    observateur.notifier();
                });
            }
        }

        mainPanel.add(listeJoueurs, BorderLayout.NORTH);
        mainPanel.add(cartesADonner, BorderLayout.SOUTH);
        window.setVisible(true);
    }
}
