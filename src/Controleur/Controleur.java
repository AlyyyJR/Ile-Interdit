// ==================================================================
// FICHIER : Controleur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Contrôleur central du jeu, orchestrant l'intégralité du déroulement.
//
// Rôle de Controleur :
//   - étendre Observateur et implémenter les trois handlers de messages MVC
//   - gérer l'initialisation : pioches, positions des aventuriers, cartes
//   - conduire la boucle de jeu tour par tour (débutTour → pioche → inondation)
//   - évaluer les conditions de victoire et de défaite à chaque tour
//   - créer les joueurs (Explorateur, Ingénieur, Navigateur, Pilote, etc.)
//
// Patron : MVC — Contrôleur, Observateur (synchronisation EDT / thread jeu)
// Étend : Observateur
// Utilisé par : Main, tous les Joueurs, toutes les Vues
// ==================================================================

package Controleur;

import Modèle.*;
import Vue.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Contrôleur central du jeu « L'Île Interdite ».
 *
 * <p>Orchestre l'intégralité du déroulement de la partie : initialisation des
 * pioches, placement des aventuriers, boucle de jeu, gestion des tours et
 * traitement des messages MVC en provenance des vues.</p>
 *
 * <p>Étend {@link Observateur} pour bénéficier du mécanisme
 * {@code waitForInput()} / {@code notifier()} assurant la synchronisation
 * entre le fil principal et les événements Swing.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 */
public class Controleur extends Observateur {

    // ==================================================================
    // Attributs — Modèle
    // ==================================================================

    /** Grille de jeu 6×6. */
    private static Grille grille;

    /** Liste de tous les joueurs inscrits. */
    private static ArrayList<Joueur> joueurs;

    /** Joueur dont c'est actuellement le tour. */
    private static Joueur joueurActif;

    /** Dernière tuile cliquée sur le plateau. */
    private Tuile lastCase;

    /** Pioche de cartes Inondation. */
    private static Stack<CarteInondation> piocheInondation;

    /** Défausse de cartes Inondation. */
    private Stack<CarteInondation> défausseInondation;

    /** Pioche de cartes Trésor. */
    private static Stack<CarteTresor> piocheCarteTresor;

    /** Défausse de cartes Trésor. */
    private static Stack<CarteTresor> defausseCarteTresor;

    /** Numéro du tour en cours (incrémenté à chaque fin de tour). */
    private static int numTour = 0;

    /** Nombre d'actions restantes pour le joueur actif. */
    private static int nbact;

    /** Indique si un joueur est mort (condition de défaite). */
    private boolean joueurMort = false;

    /** Tableau indiquant quelles reliques ont été récupérées (indices 0–3). */
    private boolean[] reliquesPrises = new boolean[4];

    /** Identifiant numérique de l'action choisie par le joueur actif. */
    private int actionChoisie;

    /** Indicateurs d'actions possibles : [0]=mouvement, [1]=assèchement, [2]=don, [3]=relique. */
    private boolean[] actionsPossibles = new boolean[4];

    // ==================================================================
    // Attributs — Vues
    // ==================================================================

    /** Vue du formulaire d'inscription. */
    private VueInscription vueInscription;

    /** Vue de l'écran titre. */
    private VueEcranTitre vueEcranTitre;

    /** Vue de fin de partie. */
    private VueFinDePartie vueFinDePartie;

    /** Vue de la jauge de montée des eaux. */
    private VueMonteeEaux vueMonteeEau;

    /** Vue du statut des reliques. */
    private VueReliques vueReliques;

    /** Vue individuelle du joueur 1. */
    private VueAventurier vj1;

    /** Vue individuelle du joueur 2. */
    private VueAventurier vj2;

    /** Vue individuelle du joueur 3 (optionnel). */
    private VueAventurier vj3;

    /** Vue individuelle du joueur 4 (optionnel). */
    private VueAventurier vj4;

    /** Vue principale du plateau de jeu, accessible depuis les autres classes. */
    public VuePlateau vuePlateau;

    /** Liste de toutes les vues aventuriers actives. */
    private ArrayList<VueAventurier> vuesAventuriers;

    // ==================================================================
    // Attributs — Paramètres de partie
    // ==================================================================

    /** Niveau de difficulté courant (1 = Novice, monte avec la montée des eaux). */
    private int difficulte;

    /** {@code true} si le mode debug est activé. */
    private boolean modeDebug;

    /** Code de fin de jeu : 0 = victoire, 1 = héliport sombré, 2 = mort, 3 = relique perdue, 4 = eau critique. */
    private int finDeJeu;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Construit le contrôleur, affiche l'écran titre, gère l'inscription
     * des joueurs, initialise la grille et ouvre toutes les fenêtres de jeu.
     */
    public Controleur() {
        piocheInondation    = new Stack<>();
        défausseInondation  = new Stack<>();
        defausseCarteTresor = new Stack<>();
        joueurActif         = null;
        piocheCarteTresor   = new Stack<>();

        vueEcranTitre = new VueEcranTitre();
        vueEcranTitre.setObservateur(this);
        vueEcranTitre.afficher();

        this.waitForInput();
        this.inscriptionJoueurs();

        grille = new Grille(modeDebug);
        this.init();

        vuePlateau = new VuePlateau(this);
        vuePlateau.setObservateur(this);
        vuePlateau.afficher();

        vueFinDePartie = new VueFinDePartie();
        vueFinDePartie.setObservateur(this);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        vj1 = new VueAventurier(this.getJoueurs().get(0));
        this.getJoueurs().get(0).setVueAventurier(vj1);
        vj1.getWindow().setLocation((int) (dim.getWidth() * 0.01), (int) (dim.getHeight() * 0.015));
        vj1.getWindow().setVisible(true);
        vj1.setObservateur(this);

        vj2 = new VueAventurier(this.getJoueurs().get(1));
        this.getJoueurs().get(1).setVueAventurier(vj2);
        vj2.getWindow().setLocation((int) (dim.getWidth() * 0.01), (int) (dim.getHeight() * 0.245));
        vj2.getWindow().setVisible(true);
        vj2.setObservateur(this);

        if (this.getJoueurs().size() >= 3) {
            vj3 = new VueAventurier(this.getJoueurs().get(2));
            this.getJoueurs().get(2).setVueAventurier(vj3);
            vj3.getWindow().setLocation((int) (dim.getWidth() * 0.01), (int) (dim.getHeight() * 0.475));
            vj3.getWindow().setVisible(true);
            vj3.setObservateur(this);
        }

        if (this.getJoueurs().size() == 4) {
            vj4 = new VueAventurier(this.getJoueurs().get(3));
            this.getJoueurs().get(3).setVueAventurier(vj4);
            vj4.getWindow().setLocation((int) (dim.getWidth() * 0.01), (int) (dim.getHeight() * 0.705));
            vj4.getWindow().setVisible(true);
            vj4.setObservateur(this);
        }

        vuesAventuriers = new ArrayList<>();
        vuesAventuriers.add(vj1);
        vuesAventuriers.add(vj2);
        if (this.getJoueurs().size() > 2) {
            vuesAventuriers.add(vj3);
            if (this.getJoueurs().size() == 4) {
                vuesAventuriers.add(vj4);
            }
        }

        vueMonteeEau = new VueMonteeEaux(difficulte);
        vueMonteeEau.setVisible(true);
        vueReliques = new VueReliques();
        vueReliques.setVisible(true);
    }

    // ==================================================================
    // Méthodes — Initialisation
    // ==================================================================

    /**
     * Affiche la fenêtre d'inscription et suspend l'exécution jusqu'à
     * la validation des noms et rôles.
     */
    public void inscriptionJoueurs() {
        joueurs = new ArrayList<>();
        vueInscription = new VueInscription();
        vueInscription.setObservateur(this);
        vueInscription.afficher();
        this.waitForInput();
    }

    /**
     * Initialise les positions des aventuriers, les pioches de cartes et
     * distribue les cartes de départ.
     */
    public void init() {
        initPositionAventurier();
        initPiocheTresor();
        initPiocheInondation();
        Arrays.fill(reliquesPrises, false);

        if (!modeDebug) {
            for (Joueur j : getJoueurs()) {
                for (int i = 0; i < 2; i++) {
                    CarteTresor c = piocheCarteTresor.firstElement();
                    piocheCarteTresor.remove(0);
                    if (c.getType() == TypeCarte.MontéeEaux) {
                        piocheCarteTresor.add(piocheCarteTresor.size(), c);
                        i--;
                    } else {
                        j.getMainJoueur().add(c);
                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                this.getJoueurs().get(0).getMainJoueur().add(new CarteTresor(TypeCarte.TresorCyan));
            }
            for (int i = 0; i < 2; i++) {
                this.getJoueurs().get(1).getMainJoueur().add(new CarteTresor(TypeCarte.SpécialSacDeSable));
                this.getJoueurs().get(1).getMainJoueur().add(new CarteTresor(TypeCarte.SpécialHélicoptère));
            }
        }
    }

    /**
     * Place chaque joueur sur sa tuile de départ en parcourant la grille.
     */
    private static void initPositionAventurier() {
        for (Joueur j : getJoueurs()) {
            Zone spawn = j.getSpawnPoint();
            for (Tuile[] tArr : grille.getTuiles()) {
                for (Tuile t : tArr) {
                    if (t != null && t.getIntitule().equals(spawn)) {
                        t.addLocataire(j);
                        j.setPosition(t);
                    }
                }
            }
        }
    }

    /**
     * Construit et mélange la pioche de cartes Trésor
     * (20 trésors, 2 montées des eaux, 3 hélicoptères, 2 sacs de sable).
     */
    private static void initPiocheTresor() {
        for (int i = 0; i < 5; i++) {
            piocheCarteTresor.add(new CarteTresor(TypeCarte.TresorGray));
            piocheCarteTresor.add(new CarteTresor(TypeCarte.TresorCyan));
            piocheCarteTresor.add(new CarteTresor(TypeCarte.TresorMagenta));
            piocheCarteTresor.add(new CarteTresor(TypeCarte.TresorOrange));
        }
        for (int i = 0; i < 2; i++) {
            piocheCarteTresor.add(new CarteTresor(TypeCarte.MontéeEaux));
        }
        for (int i = 0; i < 3; i++) {
            piocheCarteTresor.add(new CarteTresor(TypeCarte.SpécialHélicoptère));
        }
        for (int i = 0; i < 2; i++) {
            piocheCarteTresor.add(new CarteTresor(TypeCarte.SpécialSacDeSable));
        }
        Collections.shuffle(piocheCarteTresor);
    }

    /**
     * Construit et mélange la pioche de cartes Inondation à partir de
     * toutes les tuiles non-sombres de la grille.
     */
    private static void initPiocheInondation() {
        for (Tuile[] tArray : grille.getTuiles()) {
            for (Tuile t : tArray) {
                if (t != null && t.getEtat() != Etat.Sombré) {
                    piocheInondation.add(new CarteInondation(t));
                }
            }
        }
        Collections.shuffle(piocheInondation);
    }

    // ==================================================================
    // Méthodes — Boucle de jeu
    // ==================================================================

    /**
     * Lance la boucle principale : alterne les tours jusqu'à fin de partie,
     * puis ouvre l'écran de fin.
     */
    public void play() {
        while (!isPartieFinie()) {
            setJoueurActif();
            débutTour();
            piocherCarteTresorFinTour();
            verifMain(joueurActif);
            piocherCarteInondeFinTour(difficulte);
            vuePlateau.update();
            for (VueAventurier v : vuesAventuriers) {
                v.update();
            }
            numTour++;
        }
        vuePlateau.dispose();
        for (VueAventurier v : vuesAventuriers) {
            v.dispose();
        }
        vueMonteeEau.dispose();
        vueReliques.dispose();
        vueFinDePartie.update(finDeJeu);
        vueFinDePartie.afficher();
        this.waitForInput();
    }

    /**
     * Gère le tour du joueur actif : active les boutons selon les actions
     * disponibles et traite chaque action jusqu'à épuisement du compteur.
     */
    public void débutTour() {
        setNbact(joueurActif.getCouleur().equals(Color.YELLOW) ? 4 : 3);

        while (getNbact() > 0) {
            verifMain(joueurActif);
            actionsPossibles[0] = joueurActif.isMvmntPossible();
            actionsPossibles[1] = joueurActif.isAssPossible();
            actionsPossibles[2] = joueurActif.isDonPossible();
            actionsPossibles[3] = joueurActif.isReliquePossible();

            for (Joueur j : joueurs) {
                j.getVueAventurier().desactiverBoutons();
                if (j == joueurActif) {
                    j.getVueAventurier().activerBoutons();
                    if (j.isMvmntPossible())  j.getVueAventurier().activerBoutonAller();
                    if (j.isAssPossible())    j.getVueAventurier().activerBoutonAssecher();
                    if (j.isDonPossible())    j.getVueAventurier().activerBoutonDonner();
                    if (j.isReliquePossible()) j.getVueAventurier().activerBoutonRelique();
                    if (j.isCSPossible())     j.getVueAventurier().activerBoutonCS();
                }
            }

            this.waitForInput();

            switch (actionChoisie) {
                case 1 -> { joueurActif.getVueAventurier().desactiverBoutons(); joueurActif.déplacer(); }
                case 2 -> { joueurActif.getVueAventurier().desactiverBoutons(); joueurActif.assecher(); }
                case 3 -> { joueurActif.getVueAventurier().desactiverBoutons(); joueurActif.donnerCarte(); this.waitForInput(); }
                case 4 -> { joueurActif.getVueAventurier().desactiverBoutons(); joueurActif.prendreRelique(); vueReliques.update(reliquesPrises); }
                case 5 -> { joueurActif.getVueAventurier().desactiverBoutons(); joueurActif.utiliserCarte(); }
                case 6 -> { joueurActif.getVueAventurier().desactiverBoutons(); this.terminerTour(); }
            }

            if (actionChoisie != 5) {
                setNbact(getNbact() - 1);
            }
        }
    }

    // ==================================================================
    // Méthodes — Conditions de fin de partie
    // ==================================================================

    /**
     * Vérifie les quatre conditions de défaite.
     *
     * @return {@code true} si une condition de défaite est remplie
     */
    private boolean isPartiePerdue() {
        boolean resultat = false;

        // L'héliport a-t-il sombré ?
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                Tuile t = grille.getTuile(x, y);
                if (t != null && t.getIntitule() == Zone.Heliport && t.getEtat() == Etat.Sombré) {
                    resultat = true;
                    finDeJeu = 1;
                }
            }
        }

        // Un joueur est-il mort ?
        if (joueurMort) {
            resultat = true;
            finDeJeu = 2;
        }

        // Une relique non prise est-elle irrécupérable ?
        for (int i = 0; i < 4; i++) {
            if (!reliquesPrises[i]) {
                switch (i) {
                    case 0 -> {
                        if (grille.getTuile(Zone.LaCaverneDesOmbres).isSombre()
                                && grille.getTuile(Zone.LaCaverneDuBrasier).isSombre()) {
                            resultat = true; finDeJeu = 3;
                        }
                    }
                    case 1 -> {
                        if (grille.getTuile(Zone.LeJardinDesHurlements).isSombre()
                                && grille.getTuile(Zone.LeJardinDesMurmures).isSombre()) {
                            resultat = true; finDeJeu = 3;
                        }
                    }
                    case 2 -> {
                        if (grille.getTuile(Zone.LeTempleDeLaLune).isSombre()
                                && grille.getTuile(Zone.LeTempleDuSoleil).isSombre()) {
                            resultat = true; finDeJeu = 3;
                        }
                    }
                    case 3 -> {
                        if (grille.getTuile(Zone.LePalaisDeCorail).isSombre()
                                && grille.getTuile(Zone.LePalaisDesMarees).isSombre()) {
                            resultat = true; finDeJeu = 3;
                        }
                    }
                }
            }
        }

        // Niveau d'eau critique ?
        if (difficulte > 9) {
            resultat = true;
            finDeJeu = 4;
        }

        return resultat;
    }

    /**
     * Vérifie la condition de victoire : tous les joueurs réunis sur l'héliport,
     * 4 reliques récupérées et au moins une carte Hélicoptère en main.
     *
     * @return {@code true} si la condition de victoire est remplie
     */
    private boolean isPartieGagnée() {
        if (grille.getTuile(Zone.Heliport).getLocataires().size() != joueurs.size()) {
            return false;
        }
        int nbPrises = 0;
        for (boolean prise : reliquesPrises) {
            if (prise) nbPrises++;
        }
        if (nbPrises < 4) return false;

        for (Joueur j : joueurs) {
            for (CarteTresor c : j.getMainJoueur()) {
                if (c.getType() == TypeCarte.SpécialHélicoptère) {
                    finDeJeu = 0;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Indique si la partie est terminée (victoire ou défaite).
     *
     * @return {@code true} si la partie est finie
     */
    private boolean isPartieFinie() {
        return isPartiePerdue() || isPartieGagnée();
    }

    // ==================================================================
    // Méthodes — Gestion des cartes
    // ==================================================================

    /**
     * Pioche une carte Trésor pour le joueur spécifié ; si la pioche est vide,
     * la défausse est mélangée pour la reconstituer.
     *
     * @param j le joueur qui pioche
     */
    private static void piocherCarte(Joueur j) {
        if (piocheCarteTresor.isEmpty()) {
            piocheCarteTresor.addAll(defausseCarteTresor);
            defausseCarteTresor.clear();
            Collections.shuffle(piocheCarteTresor);
        }
        CarteTresor carte = piocheCarteTresor.firstElement();
        j.getMainJoueur().add(carte);
        piocheCarteTresor.remove(0);
    }

    /**
     * Fait piocher deux cartes Trésor au joueur actif en fin de tour.
     * Les cartes « Montée des Eaux » déclenchent immédiatement {@link #monteeEau()}.
     */
    public void piocherCarteTresorFinTour() {
        for (int i = 0; i < 2; i++) {
            piocherCarte(joueurActif);
            CarteTresor cartePiochee = joueurActif.getMainJoueur()
                    .get(joueurActif.getMainJoueur().size() - 1);
            if (cartePiochee.getType() == TypeCarte.MontéeEaux) {
                monteeEau();
                joueurActif.getMainJoueur().remove(cartePiochee);
            }
        }
        for (VueAventurier v : vuesAventuriers) {
            v.update();
        }
    }

    /**
     * Pioche le nombre requis de cartes Inondation en fin de tour et applique
     * leurs effets sur la grille. Si un joueur se retrouve sur une tuile sombrant,
     * il doit se déplacer ; sinon il est considéré mort.
     *
     * @param difficulte le niveau de difficulté courant
     */
    public void piocherCarteInondeFinTour(int difficulte) {
        int niveauEau;
        if (difficulte <= 2) {
            niveauEau = 2;
        } else if (difficulte <= 5) {
            niveauEau = 3;
        } else if (difficulte <= 7) {
            niveauEau = 4;
        } else {
            niveauEau = 5;
        }

        for (int i = 0; i < niveauEau; i++) {
            if (piocheInondation.isEmpty()) {
                piocheInondation.addAll(défausseInondation);
                défausseInondation.clear();
                Collections.shuffle(piocheInondation);
            }
            CarteInondation carteInondeFinTour = piocheInondation.firstElement();
            if (carteInondeFinTour.getTuile().getEtat() == Etat.Sec) {
                carteInondeFinTour.getTuile().setEtat(Etat.Inondé);
                piocheInondation.remove(carteInondeFinTour);
                défausseInondation.add(carteInondeFinTour);
            } else if (carteInondeFinTour.getTuile().getEtat() == Etat.Inondé) {
                carteInondeFinTour.getTuile().setEtat(Etat.Sombré);
                piocheInondation.remove(carteInondeFinTour);
                if (!carteInondeFinTour.getTuile().getLocataires().isEmpty()) {
                    while (carteInondeFinTour.getTuile().getLocataires().size() > 0) {
                        Joueur j = carteInondeFinTour.getTuile().getLocataires().get(0);
                        if (j.isMvmntPossible()) {
                            vuePlateau.obligationDeplacement(j);
                            j.déplacer();
                            carteInondeFinTour.getTuile().delLocataire(j);
                        } else {
                            joueurMort = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Incrémente la difficulté (montée des eaux), met à jour la vue et
     * mélange la défausse d'inondation dans la pioche.
     */
    private void monteeEau() {
        difficulte++;
        vueMonteeEau.monteDesEaux(difficulte);
        piocheInondation.addAll(défausseInondation);
        défausseInondation.clear();
        Collections.shuffle(piocheInondation);
    }

    // ==================================================================
    // Méthodes — Utilitaires
    // ==================================================================

    /**
     * Vérifie que le joueur donné ne dépasse pas 5 cartes en main ;
     * force une défausse tant que la limite est dépassée.
     *
     * @param joueur le joueur dont la main est vérifiée
     */
    private static void verifMain(Joueur joueur) {
        while (joueurActif.getMainJoueur().size() >= 6) {
            joueur.getVueAventurier().desactiverBoutons();
            joueur.defausserCarte();
        }
    }

    /**
     * Met à zéro le compteur d'actions pour terminer le tour immédiatement.
     */
    public void terminerTour() {
        setNbact(0);
    }

    /**
     * Sélectionne le joueur actif en fonction du numéro du tour.
     */
    private static void setJoueurActif() {
        joueurActif = getJoueurs().get(numTour % getJoueurs().size());
    }

    /**
     * Demande à la vue plateau de surligner les tuiles accessibles.
     *
     * @param casesDispo la liste des tuiles à mettre en évidence
     */
    public void surligner(ArrayList<Tuile> casesDispo) {
        vuePlateau.surligner(casesDispo);
    }

    /**
     * Marque la relique d'indice {@code i} comme récupérée.
     *
     * @param i l'indice de la relique (0–3)
     */
    public void addRelique(int i) {
        reliquesPrises[i] = true;
    }

    /**
     * Affiche une notification de prise de relique selon son type.
     *
     * @param compType le type de carte correspondant à la relique ramassée
     */
    public void signalerPriseRelique(TypeCarte compType) {
        String couleur = switch (compType.toString()) {
            case "TresorMagenta" -> "Cristal Ardent";
            case "TresorGray"    -> "Pierre Sacrée";
            case "TresorCyan"    -> "Calice de l'Onde";
            case "TresorOrange"  -> "Statue du Zéphyr";
            default              -> compType.toString();
        };
        vuePlateau.signalerPriseRelique(couleur);
    }

    /**
     * Affiche une boîte de dialogue invitant l'utilisateur à saisir un nom
     * de joueur lorsque le champ correspondant est vide à la validation.
     *
     * @param nomJoueur le libellé du joueur (ex. « Joueur 1 »)
     * @return le nom saisi, ou {@code null} si la fenêtre est fermée
     */
    public String fenetreNom(String nomJoueur) {
        String nom = null;
        String[] bouton = {"OK"};
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Saisissez le nom du " + nomJoueur);
        JTextField txt = new JTextField(10);
        panel.add(label);
        panel.add(txt);
        int selectedOption = JOptionPane.showOptionDialog(null, panel,
                "Saisie de Nom", JOptionPane.NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, bouton, bouton[0]);
        if (selectedOption == 0) {
            nom = txt.getText();
        }
        return nom;
    }

    // ==================================================================
    // Méthodes — Traitement des messages MVC
    // ==================================================================

    /**
     * Traite les messages généraux envoyés par les vues (écran titre,
     * inscription, cartes spéciales, etc.).
     *
     * @param msg le message reçu
     */
    @Override
    public void traiterMessage(Message msg) {
        switch (msg.getType().toString()) {
            case "Valider"        -> traiterValider();
            case "Annuler"        -> System.exit(0);
            case "Jouer"          -> { vueEcranTitre.fermer(); this.notifier(); }
            case "CarteSpeHelico" -> joueurActif.utiliserHelico();
            case "CarteSpeSac"    -> joueurActif.utiliserSac();
        }
    }

    /**
     * Traite la validation du formulaire d'inscription : crée les joueurs avec
     * leurs rôles (aléatoires ou choisis), configure la difficulté et le mode debug.
     */
    private void traiterValider() {
        Random randomGenerator = new Random();
        ArrayList<String> listRoles = new ArrayList<>();
        for (String s : vueInscription.getRoles()) {
            if (!vueInscription.isAleatoireVide(s)) {
                listRoles.add(s);
            }
        }
        // Retirer les rôles explicitement choisis de la liste des rôles disponibles
        if (!vueInscription.isAleatoireVide(vueInscription.getRoleComboJ1())) listRoles.remove(vueInscription.getRoleComboJ1());
        if (!vueInscription.isAleatoireVide(vueInscription.getRoleComboJ2())) listRoles.remove(vueInscription.getRoleComboJ2());
        if (!vueInscription.isAleatoireVide(vueInscription.getRoleComboJ3())) listRoles.remove(vueInscription.getRoleComboJ3());
        if (!vueInscription.isAleatoireVide(vueInscription.getRoleComboJ4())) listRoles.remove(vueInscription.getRoleComboJ4());

        // --- Joueur 1 (obligatoire) ---
        String nom1 = vueInscription.getNomFieldJ1();
        while (nom1 == null || nom1.isBlank()) { nom1 = fenetreNom("Joueur 1"); }
        String role1 = vueInscription.getRoleComboJ1();
        if ("Aléatoire".equals(role1)) {
            role1 = listRoles.get(randomGenerator.nextInt(listRoles.size()));
        }
        creerJoueur(nom1, role1);
        listRoles.remove(role1);

        // --- Joueur 2 (obligatoire) ---
        String nom2 = vueInscription.getNomFieldJ2();
        while (nom2 == null || nom2.isBlank()) { nom2 = fenetreNom("Joueur 2"); }
        String role2 = vueInscription.getRoleComboJ2();
        if ("Aléatoire".equals(role2)) {
            int idx = randomGenerator.nextInt(listRoles.size());
            role2 = listRoles.get(idx);
            listRoles.remove(idx);
        }
        creerJoueur(nom2, role2);
        listRoles.remove(role2);

        // --- Joueur 3 (optionnel) ---
        String nom3  = vueInscription.getNomFieldJ3();
        String role3 = vueInscription.getRoleComboJ3();
        while ((nom3 == null || nom3.isBlank()) && !"Vide".equals(role3)) { nom3 = fenetreNom("Joueur 3"); }
        if ("Aléatoire".equals(role3)) {
            int idx = randomGenerator.nextInt(listRoles.size());
            role3 = listRoles.get(idx);
            listRoles.remove(idx);
        }
        creerJoueur(nom3, role3);
        listRoles.remove(role3);

        // --- Joueur 4 (optionnel) ---
        String nom4  = vueInscription.getNomFieldJ4();
        String role4 = vueInscription.getRoleComboJ4();
        while ((nom4 == null || nom4.isBlank()) && !"Vide".equals(role4)) { nom4 = fenetreNom("Joueur 4"); }
        if ("Aléatoire".equals(role4)) {
            int idx = randomGenerator.nextInt(listRoles.size());
            role4 = listRoles.get(idx);
            listRoles.remove(idx);
        }
        creerJoueur(nom4, role4);

        vueInscription.getWindow().dispose();

        // --- Difficulté ---
        difficulte = switch (vueInscription.getComboDiff()) {
            case "Novice"     -> 1;
            case "Normal"     -> 2;
            case "Élite"      -> 3;
            case "Légendaire" -> 4;
            case "Mortel"     -> 10;
            default           -> 2;
        };

        modeDebug = vueInscription.getCheckDebug();
        this.notifier();
    }

    /**
     * Instancie un {@link Joueur} du rôle donné et l'ajoute à la liste.
     * Si le rôle est « Vide », aucun joueur n'est créé.
     *
     * @param nom  le nom du joueur
     * @param role le rôle choisi (ex. « Explorateur », « Pilote »…)
     */
    private void creerJoueur(String nom, String role) {
        Joueur j = switch (role) {
            case "Explorateur" -> new Explorateur(nom, this);
            case "Ingénieur"   -> new Ingénieur(nom, this);
            case "Messager"    -> new Messager(nom, this);
            case "Navigateur"  -> new Navigateur(nom, this);
            case "Pilote"      -> new Pilote(nom, this);
            case "Plongeur"    -> new Plongeur(nom, this);
            default            -> null; // "Vide" ou rôle inconnu
        };
        if (j != null) {
            getJoueurs().add(j);
        }
    }

    /**
     * Traite un clic sur une tuile du plateau : mémorise la tuile cliquée
     * et notifie l'observateur.
     *
     * @param msg le message contenant les coordonnées de la tuile cliquée
     */
    @Override
    public void traiterMessagePlateau(MessagePlateau msg) {
        lastCase = grille.getTuile(msg.getCoo().getX(), msg.getCoo().getY());
        this.notifier();
    }

    /**
     * Traite les actions déclenchées depuis la vue aventurier (boutons Aller,
     * Assécher, Donner, etc.) et met à jour {@link #actionChoisie}.
     *
     * @param msg le message contenant le type d'action et le joueur concerné
     */
    @Override
    public void traiterMessageAventurier(MessageAventurier msg) {
        actionChoisie = switch (msg.getType().toString()) {
            case "Deplacer"       -> 1;
            case "Assecher"       -> 2;
            case "Donner"         -> 3;
            case "PrendreRelique" -> 4;
            case "CarteSpe"       -> 5;
            case "TerminerTour"   -> 6;
            default               -> 0;
        };
        this.notifier();
    }

    // ==================================================================
    // Getters / Setters
    // ==================================================================

    /**
     * Retourne le nombre d'actions restantes pour le joueur actif.
     *
     * @return le nombre d'actions restantes
     */
    public static int getNbact() { return nbact; }

    /**
     * Définit le nombre d'actions restantes pour le joueur actif.
     *
     * @param aNbact le nouveau nombre d'actions
     */
    private static void setNbact(int aNbact) { nbact = aNbact; }

    /**
     * Retourne la liste des joueurs inscrits.
     *
     * @return la liste des joueurs
     */
    public static ArrayList<Joueur> getJoueurs() { return joueurs; }

    /**
     * Retourne la défausse de cartes Trésor.
     *
     * @return la pile de défausse
     */
    public static Stack<CarteTresor> getDefausseCarteTresor() { return defausseCarteTresor; }

    /**
     * Remplace la défausse de cartes Trésor.
     *
     * @param aDefausseCarteTresor la nouvelle défausse
     */
    public static void setDefausseCarteTresor(Stack<CarteTresor> aDefausseCarteTresor) {
        defausseCarteTresor = aDefausseCarteTresor;
    }

    /**
     * Retourne la grille de jeu.
     *
     * @return la grille
     */
    public Grille getGrille() { return grille; }

    /**
     * Retourne la dernière tuile cliquée sur le plateau.
     *
     * @return la tuile cliquée, ou {@code null} si aucune
     */
    public Tuile getLastCase() { return lastCase; }
}
