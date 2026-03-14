// ==================================================================
// FICHIER : Joueur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Classe abstraite représentant un aventurier avec son rôle et ses capacités.
//
// Rôle de Joueur :
//   - définir les attributs communs à tous les rôles (nom, couleur, main, position)
//   - déclarer listerCasesDispo() en Template Method, spécialisée par chaque sous-classe
//   - implémenter les actions communes : déplacer, assecher, donner, prendreRelique
//
// Utilisé par : Controleur, MessageAventurier, VueAventurier, toutes les sous-classes
// ==================================================================

package Modèle;

import Controleur.Controleur;
import Vue.VueAventurier;
import Vue.VueDefausse;
import Vue.VueDonDeCartes;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Classe abstraite représentant un joueur du jeu L'Île Interdite.
 *
 * <p>Un joueur possède un nom, une couleur, une position sur le plateau, une main de cartes
 * trésor, et une vue graphique associée ({@link VueAventurier}). Cette classe implémente
 * les actions de base communes à tous les rôles : déplacement, assèchement, don de cartes,
 * prise de relique et utilisation de cartes spéciales.</p>
 *
 * <p>Chaque rôle du jeu étend cette classe et peut surcharger les méthodes
 * {@link #listerCasesDispo()}, {@link #listerTuilesAssechables()}, {@link #assecher()}
 * et {@link #donnerCarte()} pour implémenter ses capacités spéciales.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public abstract class Joueur {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Tuile sur laquelle le joueur se trouve actuellement. */
    private Tuile position;

    /** Cartes trésor en main du joueur (maximum 5 avant défausse). */
    private ArrayList<CarteTresor> mainJoueur;

    /** Nom du joueur affiché dans l'interface. */
    private String nom;

    /** Couleur distinctive du pion du joueur. */
    private Color couleur;

    /** Zone de départ du joueur, définie par chaque sous-classe. */
    protected Zone spawnPoint;

    /** Interface graphique associée à ce joueur. */
    public VueAventurier vueAventurier;

    /** Référence vers le contrôleur central du jeu. */
    protected Controleur controleur;

    /** Image du pion du joueur, chargée par chaque sous-classe. */
    private ImageIcon image;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise un joueur avec son nom et son contrôleur.
     *
     * <p>La couleur, la position et le point de spawn sont définis à {@code null}
     * et doivent être renseignés par chaque sous-classe.</p>
     *
     * @param nom        le nom du joueur (non nul, non vide)
     * @param controleur le contrôleur central du jeu
     */
    public Joueur(String nom, Controleur controleur) {
        this.mainJoueur  = new ArrayList<>();
        this.nom         = nom;
        this.couleur     = null;
        this.spawnPoint  = null;
        this.position    = null;
        this.controleur  = controleur;
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne la tuile sur laquelle le joueur est positionné.
     *
     * @return la tuile courante du joueur
     */
    public Tuile getPosition() {
        return position;
    }

    /**
     * Retourne la main du joueur (liste mutable des cartes trésor).
     *
     * @return liste des cartes trésor en main
     */
    public ArrayList<CarteTresor> getMainJoueur() {
        return mainJoueur;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la couleur distinctive du pion du joueur.
     *
     * @return la couleur du joueur
     */
    public Color getCouleur() {
        return couleur;
    }

    /**
     * Retourne la zone de départ du joueur.
     *
     * @return la zone de spawn
     */
    public Zone getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Retourne la vue graphique associée à ce joueur.
     *
     * @return l'interface graphique du joueur
     */
    public VueAventurier getVueAventurier() {
        return vueAventurier;
    }

    /**
     * Retourne le contrôleur central du jeu.
     *
     * @return le contrôleur
     */
    public Controleur getControleur() {
        return controleur;
    }

    /**
     * Retourne l'image du pion du joueur.
     *
     * @return l'icône du pion
     */
    public ImageIcon getImage() {
        return image;
    }

    // ==================================================================
    // Mutateurs
    // ==================================================================

    /**
     * Déplace le joueur sur la tuile indiquée.
     *
     * @param tuile la nouvelle tuile d'accueil du joueur
     */
    public void setPosition(Tuile tuile) {
        this.position = tuile;
    }

    /**
     * Définit la couleur du pion du joueur.
     *
     * @param couleur la couleur à attribuer
     */
    protected void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    /**
     * Associe la vue graphique du joueur.
     *
     * @param vueAventurier la vue à associer
     */
    public void setVueAventurier(VueAventurier vueAventurier) {
        this.vueAventurier = vueAventurier;
    }

    /**
     * Définit l'image du pion du joueur.
     *
     * @param image l'icône à utiliser
     */
    protected void setImage(ImageIcon image) {
        this.image = image;
    }

    // ==================================================================
    // Actions de base
    // ==================================================================

    /**
     * Liste les tuiles vers lesquelles ce joueur peut se déplacer.
     *
     * <p>Par défaut, renvoie les tuiles adjacentes (4 directions orthogonales)
     * qui ne sont pas dans l'état {@code Sombré}. Les sous-classes peuvent surcharger
     * cette méthode pour ajouter des possibilités spéciales (diagonales, vol libre, etc.).</p>
     *
     * @return liste des tuiles accessibles pour un déplacement
     */
    public ArrayList<Tuile> listerCasesDispo() {
        ArrayList<Tuile> tuilesLibres = new ArrayList<>();
        for (Tuile t : this.getPosition().getAdjacent()) {
            if (t.getEtat() != Etat.Sombré) {
                tuilesLibres.add(t);
            }
        }
        return tuilesLibres;
    }

    /**
     * Liste les tuiles que ce joueur peut assécher.
     *
     * <p>Par défaut, inclut la tuile actuelle du joueur et ses tuiles adjacentes
     * si elles sont dans l'état {@code Inondé}.</p>
     *
     * @return liste des tuiles asséchables
     */
    public ArrayList<Tuile> listerTuilesAssechables() {
        ArrayList<Tuile> tuilesAss = new ArrayList<>();

        if (this.getPosition().getEtat() == Etat.Inondé) {
            tuilesAss.add(this.getPosition());
        }
        for (Tuile t : this.getPosition().getAdjacent()) {
            if (t.getEtat() == Etat.Inondé) {
                tuilesAss.add(t);
            }
        }
        return tuilesAss;
    }

    /**
     * Effectue un déplacement : affiche les cases disponibles, attend la sélection du joueur
     * et déplace le pion vers la tuile choisie.
     */
    public void déplacer() {
        ArrayList<Tuile> casesDispo = new ArrayList<>(this.listerCasesDispo());
        controleur.surligner(casesDispo);
        controleur.waitForInput();
        Tuile caseDepl = controleur.getLastCase();

        if (!casesDispo.contains(caseDepl)) {
            controleur.waitForInput();
            caseDepl = controleur.getLastCase();
        }

        Tuile tuileQuittee = this.getPosition();
        tuileQuittee.delLocataire(this);
        this.setPosition(caseDepl);
        caseDepl.addLocataire(this);

        controleur.surligner(new ArrayList<>());
    }

    /**
     * Effectue une action d'assèchement : affiche les tuiles asséchables, attend la sélection
     * et fait passer la tuile choisie de l'état {@code Inondé} à {@code Sec}.
     */
    public void assecher() {
        ArrayList<Tuile> casesDispo = new ArrayList<>(this.listerTuilesAssechables());
        controleur.surligner(casesDispo);
        controleur.waitForInput();
        Tuile caseAss = controleur.getLastCase();

        if (!casesDispo.contains(caseAss)) {
            controleur.waitForInput();
            caseAss = controleur.getLastCase();
        }

        caseAss.setEtat(Etat.Sec);
        controleur.surligner(new ArrayList<>());
    }

    /**
     * Donne une carte trésor à un autre joueur (opération directe, même tuile par défaut).
     *
     * @param jDest   le joueur destinataire
     * @param cRecue  la carte trésor à transférer
     */
    public void donnerCarte(Joueur jDest, CarteTresor cRecue) {
        jDest.getMainJoueur().add(cRecue);
        this.getMainJoueur().remove(cRecue);
        this.vueAventurier.getWindow().validate();
        this.vueAventurier.update();
        jDest.vueAventurier.getWindow().validate();
        jDest.vueAventurier.update();
    }

    /**
     * Ouvre l'interface de don de cartes permettant de choisir le destinataire
     * parmi les joueurs sur la même tuile.
     */
    public void donnerCarte() {
        ArrayList<Joueur> joueursechangeables = new ArrayList<>(position.getLocataires());
        new VueDonDeCartes(this, joueursechangeables);
    }

    /**
     * Récupère la relique disponible sur la tuile actuelle, en échange de 4 cartes trésor du bon type.
     *
     * <p>Les 4 cartes correspondantes sont retirées de la main et envoyées dans la défausse.</p>
     */
    public void prendreRelique() {
        Color relique = this.getPosition().getReliqueDispo();
        TypeCarte compType;
        int indexRelique;

        if (relique == Color.MAGENTA) {
            compType    = TypeCarte.TresorMagenta;
            indexRelique = 0;
        } else if (relique == Color.CYAN) {
            compType    = TypeCarte.TresorCyan;
            indexRelique = 3;
        } else if (relique == Color.GRAY) {
            compType    = TypeCarte.TresorGray;
            indexRelique = 2;
        } else {
            compType    = TypeCarte.TresorOrange;
            indexRelique = 1;
        }

        controleur.addRelique(indexRelique);

        ArrayList<CarteTresor> aSupprimer = new ArrayList<>();
        int n = 0;
        for (CarteTresor c : this.getMainJoueur()) {
            if (c.getType() == compType && n < 4) {
                aSupprimer.add(c);
                n++;
            }
        }
        for (CarteTresor c : aSupprimer) {
            this.defausserCarte(c);
        }

        controleur.signalerPriseRelique(compType);
    }

    /**
     * Défausse une carte spécifique de la main du joueur vers la pile de défausse trésor.
     *
     * @param c la carte à défausser
     */
    public void defausserCarte(CarteTresor c) {
        controleur.getDefausseCarteTresor().add(c);
        this.getMainJoueur().remove(c);
        this.vueAventurier.getWindow().validate();
        this.vueAventurier.update();
    }

    /**
     * Ouvre la fenêtre de défausse forcée lorsque le joueur dépasse la limite de 5 cartes.
     */
    public void defausserCarte() {
        new VueDefausse(this);
        this.controleur.waitForInput();
    }

    /**
     * Gère l'utilisation d'une carte spéciale (Hélicoptère ou Sac de Sable).
     *
     * <p>Affiche un menu de sélection si le joueur possède plusieurs cartes spéciales,
     * retire la carte choisie de la main et déclenche l'action correspondante.</p>
     */
    public void utiliserCarte() {
        ArrayList<CarteTresor> cSpeciales = new ArrayList<>();
        for (CarteTresor c : this.getMainJoueur()) {
            if (c.getType() == TypeCarte.SpécialHélicoptère
                    || c.getType() == TypeCarte.SpécialSacDeSable) {
                cSpeciales.add(c);
            }
        }

        ArrayList<String> nomsCSpeciales = new ArrayList<>();
        for (CarteTresor c : cSpeciales) {
            nomsCSpeciales.add(c.getType().name());
        }

        String choixCarte = null;
        while (choixCarte == null) {
            String[] bouton = {"OK"};
            JPanel panel = new JPanel();
            panel.add(new JLabel("Quelle carte spéciale voulez-vous utiliser ?"));
            JComboBox<String> combo = new JComboBox<>(nomsCSpeciales.toArray(new String[0]));
            panel.add(combo);
            int rep = JOptionPane.showOptionDialog(null, panel, "Carte Spéciale",
                JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, bouton, bouton[0]);
            if (rep == 0) {
                choixCarte = (String) combo.getSelectedItem();
            }
        }

        int i = 0;
        if (choixCarte.equals(TypeCarte.SpécialHélicoptère.name())) {
            while (i < getMainJoueur().size()
                    && getMainJoueur().get(i).getType() != TypeCarte.SpécialHélicoptère) {
                i++;
            }
            getMainJoueur().remove(i);
            getVueAventurier().update();
            this.utiliserHelico();
        } else {
            while (i < getMainJoueur().size()
                    && getMainJoueur().get(i).getType() != TypeCarte.SpécialSacDeSable) {
                i++;
            }
            getMainJoueur().remove(i);
            getVueAventurier().update();
            this.utiliserSac();
        }

        getVueAventurier().update();
    }

    /**
     * Utilise la carte Hélicoptère : affiche toutes les tuiles non sombrées et permet
     * de téléporter le joueur ou un groupe de joueurs vers la destination choisie.
     */
    public void utiliserHelico() {
        JOptionPane.showMessageDialog(null,
            "Si vous cliquez sur une tuile inoccupée, cela vous téléportera dessus.\n"
            + "Pour déplacer un groupe, sélectionnez d'abord la tuile de départ du groupe,\n"
            + "puis cliquez sur la tuile de destination.");

        ArrayList<Tuile> casesDispo = new ArrayList<>();
        for (Tuile[] ligne : this.getPosition().getPlateau().getTuiles()) {
            for (Tuile t : ligne) {
                if (t != null && t.getEtat() != Etat.Sombré) {
                    casesDispo.add(t);
                }
            }
        }

        controleur.surligner(casesDispo);
        controleur.waitForInput();
        ArrayList<Joueur> groupe = new ArrayList<>(controleur.getLastCase().getLocataires());

        if (groupe.isEmpty()) {
            this.deplacerHelico(controleur.getLastCase());
        } else {
            deplacerHelico(casesDispo, groupe);
        }
    }

    /**
     * Utilise la carte Sac de Sable : affiche toutes les tuiles inondées et assèche
     * celle choisie par le joueur.
     */
    public void utiliserSac() {
        ArrayList<Tuile> dispo = new ArrayList<>();
        for (Tuile[] ligne : this.getPosition().getPlateau().getTuiles()) {
            for (Tuile t : ligne) {
                if (t != null && t.getEtat() == Etat.Inondé) {
                    dispo.add(t);
                }
            }
        }

        controleur.surligner(dispo);
        controleur.waitForInput();
        Tuile caseAss = controleur.getLastCase();
        caseAss.setEtat(Etat.Sec);
        controleur.surligner(new ArrayList<>());
    }

    /**
     * Déplace un joueur individuel vers une tuile cible via l'hélicoptère.
     *
     * @param destination la tuile de destination
     */
    public void deplacerHelico(Tuile destination) {
        Tuile tuileQuittee = this.getPosition();
        tuileQuittee.delLocataire(this);
        this.setPosition(destination);
        destination.addLocataire(this);
        controleur.surligner(new ArrayList<>());
    }

    /**
     * Déplace un groupe de joueurs vers une tuile choisie parmi les tuiles disponibles,
     * via l'hélicoptère.
     *
     * @param tuilesDispo les tuiles valides pour la destination
     * @param groupe      la liste des joueurs à déplacer
     */
    public void deplacerHelico(ArrayList<Tuile> tuilesDispo, ArrayList<Joueur> groupe) {
        controleur.surligner(tuilesDispo);
        controleur.waitForInput();
        Tuile destination = controleur.getLastCase();

        if (!tuilesDispo.contains(destination)) {
            controleur.waitForInput();
            destination = controleur.getLastCase();
        }

        for (Joueur j : groupe) {
            Tuile depart = j.getPosition();
            depart.delLocataire(j);
            j.setPosition(destination);
            destination.addLocataire(j);
        }

        controleur.surligner(new ArrayList<>());
    }

    // ==================================================================
    // Tests de faisabilité
    // ==================================================================

    /**
     * Vérifie si le joueur peut se déplacer (au moins une tuile accessible).
     *
     * @return {@code true} si un déplacement est possible
     */
    public boolean isMvmntPossible() {
        return !this.listerCasesDispo().isEmpty();
    }

    /**
     * Vérifie si le joueur peut assécher au moins une tuile.
     *
     * @return {@code true} si un assèchement est possible
     */
    public boolean isAssPossible() {
        return !this.listerTuilesAssechables().isEmpty();
    }

    /**
     * Vérifie si le joueur peut récupérer la relique de sa tuile actuelle
     * (au moins 4 cartes trésor du bon type en main).
     *
     * @return {@code true} si la prise de relique est possible
     */
    public boolean isReliquePossible() {
        Color relique = this.getPosition().getReliqueDispo();
        if (relique == null) {
            return false;
        }

        TypeCarte compType;
        if      (relique == Color.MAGENTA) compType = TypeCarte.TresorMagenta;
        else if (relique == Color.CYAN)    compType = TypeCarte.TresorCyan;
        else if (relique == Color.GRAY)    compType = TypeCarte.TresorGray;
        else if (relique == Color.ORANGE)  compType = TypeCarte.TresorOrange;
        else return false;

        int n = 0;
        for (CarteTresor c : this.getMainJoueur()) {
            if (c.getType() == compType) {
                n++;
            }
        }
        return n >= 4;
    }

    /**
     * Vérifie si le joueur peut donner une carte à un autre joueur.
     *
     * <p>Conditions : posséder au moins une carte trésor non spéciale
     * et partager la même tuile avec au moins un autre joueur.</p>
     *
     * @return {@code true} si le don est possible
     */
    public boolean isDonPossible() {
        boolean aCarte = false;
        for (CarteTresor ct : this.getMainJoueur()) {
            if (ct.getType() != TypeCarte.SpécialHélicoptère
                    && ct.getType() != TypeCarte.SpécialSacDeSable) {
                aCarte = true;
                break;
            }
        }
        return aCarte && this.getPosition().getLocataires().size() > 1;
    }

    /**
     * Vérifie si le joueur possède au moins une carte spéciale
     * (Hélicoptère ou Sac de Sable).
     *
     * @return {@code true} si une carte spéciale est disponible
     */
    public boolean isCSPossible() {
        for (CarteTresor cs : this.getMainJoueur()) {
            if (cs.getType() == TypeCarte.SpécialHélicoptère
                    || cs.getType() == TypeCarte.SpécialSacDeSable) {
                return true;
            }
        }
        return false;
    }
}
