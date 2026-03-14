// ==================================================================
// FICHIER : VueCarteSpe.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Dialogue de choix pour l'utilisation d'une carte spéciale (Hélicoptère ou Sac de Sable).
//
// Rôle de VueCarteSpe :
//   - afficher les cartes spéciales disponibles dans la main du joueur actif
//   - permettre au joueur de sélectionner et utiliser une carte spéciale
//   - émettre CarteSpeHelico ou CarteSpeSac via l'Observateur
//
// Utilisé par : Joueur (utiliserCarte), Controleur
// ==================================================================

package Vue;

import Controleur.Message;
import Controleur.Observateur;
import Controleur.TypeMessage;
import Modèle.CarteTresor;
import Modèle.Joueur;
import Modèle.TypeCarte;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Fenêtre de sélection d'une carte spéciale à utiliser.
 *
 * Affiche les cartes spéciales disponibles (Hélicoptère et/ou Sac de Sable)
 * et envoie le message approprié au contrôleur selon le choix du joueur :
 *   {TypeMessage#CarteSpeHelico} pour une carte Hélicoptère
 *   {TypeMessage#CarteSpeSac} pour un Sac de Sable
 */
public class VueCarteSpe {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Observateur MVC à notifier lors du choix. */
    private final Observateur observateur;

    /** Fenêtre principale de la vue. */
    private final JFrame window;

    /** Boutons représentant les cartes spéciales affichables. */
    private final ArrayList<JButton> cartesBoutons = new ArrayList<>();

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée et affiche la fenêtre de sélection de carte spéciale.
     *
     * @param joueur          le joueur actif utilisant la carte
     * @param cartesSpeciales la liste des cartes spéciales en main du joueur
     */
    public VueCarteSpe(Joueur joueur, ArrayList<CarteTresor> cartesSpeciales) {
        window = new JFrame("Utiliser une Carte Spéciale");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 200);
        window.setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - 250, dim.height / 2 - 100);

        observateur = joueur.getVueAventurier().getObservateur();

        JPanel mainPanel = new JPanel(new BorderLayout());
        window.add(mainPanel);

        JPanel cartesAUtiliser = new JPanel(new GridLayout(1, 9));
        cartesAUtiliser.setBorder(BorderFactory.createTitledBorder("Choix de la Carte à utiliser"));

        for (CarteTresor carte : cartesSpeciales) {
            JButton boutonCarte = new JButton(carte.getImage());
            cartesBoutons.add(boutonCarte);
            cartesAUtiliser.add(boutonCarte);

            boutonCarte.addActionListener(e -> {
                Message msg;
                if (carte.getType() == TypeCarte.SpécialHélicoptère) {
                    msg = new Message(TypeMessage.CarteSpeHelico);
                } else {
                    msg = new Message(TypeMessage.CarteSpeSac);
                }
                window.dispose();
                observateur.traiterMessage(msg);
            });
        }

        mainPanel.add(cartesAUtiliser, BorderLayout.CENTER);
        window.setVisible(true);
    }
}
