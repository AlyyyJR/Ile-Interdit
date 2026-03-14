// ==================================================================
// FICHIER : Grille.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Modèle du plateau de jeu — grille 6×6 de 24 tuiles placées aléatoirement.
//
// Rôle de Grille :
//   - construire et mélanger les 24 tuiles sur la grille 6×6
//   - exposer getTuile(x, y) et getTuile(Zone) pour l'accès direct
//   - délimiter la forme de l'île (certaines cases de la grille sont nulles)
//
// Utilisé par : Controleur, Joueur, VuePlateau
// ==================================================================

package Modèle;

import java.awt.Color;
import java.util.*;

/**
 * Représente le plateau de jeu : une grille 6×6 contenant des {Tuile}s.
 *
 * Seules 24 positions sur les 36 sont jouables ; les autres restent à {null}.
 * En mode normal, les zones et les positions de départ inondées sont mélangées aléatoirement.
 * En mode debug, une configuration fixe prédéfinie est utilisée pour les tests.
 * Chaque tuile est associée à une {Zone} spécifique, dont certaines portent des reliques de couleur :
 * 
 *   Magenta → Cristal Ardent (zones préfixées "LAC")
 *   Grise   → Pierre Sacrée (zones préfixées "LET")
 *   Cyan    → Calice de l'Onde (zones préfixées "LEP")
 *   Orange  → Statue du Zéphyr (zones préfixées "LEJ")
 * 
 * La grille fournit des méthodes d'accès direct aux tuiles par coordonnées ou par zone,
 * ce qui facilite les interactions du contrôleur et des joueurs avec le plateau.
 * Par exemple, la méthode {getTuile(Zone.Heliport)} permet d'obtenir directement la tuile de l'Héliport, point de départ du Pilote, sans avoir à connaître ses coordonnées exactes sur la grille. 
 * De même, les joueurs peuvent utiliser {getTuile(x, y)} pour vérifier l'état d'une tuile spécifique avant de tenter un déplacement ou un assèchement. En somme, la classe {Grille} est un élément central du modèle du jeu, encapsulant la structure et la dynamique du plateau de L'Île Interdite. 
 */
public class Grille {

    // ==================================================================
    // Attributs
    // ==================================================================
    /** Tableau bidimensionnel des tuiles (6 lignes × 6 colonnes). Les cases hors plateau sont {null}. */
    private Tuile[][] tuiles;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Crée la grille de jeu.
     *
     * @param modeDebug si {true}, initialise une grille fixe de test ;
     *                  sinon génère une grille aléatoire
     */
    public Grille(boolean modeDebug) {
        tuiles = new Tuile[6][6];
        if (modeDebug) {
            initGrilleTest();
        } else {
            initGrille();
        }
    }

    // ==================================================================
    // Initialisation
    // ==================================================================

    /**
     * Génère une grille aléatoire en mode normal.
     *
     * Les 24 zones du jeu sont assignées aléatoirement aux 24 positions jouables.
     * Parmi ces positions, 6 sont tirées au sort et reçoivent l'état {Inondé} dès le départ.
     */
    public void initGrille() {
        int[][] positionsJouables = {
            {0,2},{0,3},{1,1},{1,2},{1,3},{1,4},
            {2,0},{2,1},{2,2},{2,3},{2,4},{2,5},
            {3,0},{3,1},{3,2},{3,3},{3,4},{3,5},
            {4,1},{4,2},{4,3},{4,4},{5,2},{5,3}
        };

        ArrayList<Zone> listeZones = new ArrayList<>(EnumSet.allOf(Zone.class));
        Collections.shuffle(listeZones);

        ArrayList<Coordonnees> positions = new ArrayList<>();
        for (int[] rc : positionsJouables) {
            positions.add(new Coordonnees(rc[0], rc[1]));
        }
        Collections.shuffle(positions);

        // 6 premières positions démarrent inondées
        ArrayList<Coordonnees> debutInondees = new ArrayList<>(positions.subList(0, 6));

        for (int i = 0; i < positions.size(); i++) {
            Coordonnees c  = positions.get(i);
            Etat etatInit  = debutInondees.contains(c) ? Etat.Inondé : Etat.Sec;
            tuiles[c.getX()][c.getY()] = new Tuile(listeZones.get(i), etatInit, this, c);
        }
    }

    /**
     * Initialise une grille fixe pour les tests et le débogage.
     *
     * Les zones, états et couleurs de reliques sont définis statiquement
     * afin de garantir un scénario de test reproductible.
     */
    private void initGrilleTest() {
        tuiles[0][2] = new Tuile(Zone.LePontDesAbimes,       Etat.Sec,    this, new Coordonnees(0,2));
        tuiles[0][3] = new Tuile(Zone.LaPorteDeBronze,       Etat.Inondé, this, new Coordonnees(0,3));
        tuiles[1][1] = new Tuile(Zone.LaCaverneDesOmbres,    Etat.Sec,    this, new Coordonnees(1,1), Color.MAGENTA);
        tuiles[1][2] = new Tuile(Zone.LaPorteDeFer,          Etat.Sec,    this, new Coordonnees(1,2));
        tuiles[1][3] = new Tuile(Zone.LaPorteDOr,            Etat.Sec,    this, new Coordonnees(1,3));
        tuiles[1][4] = new Tuile(Zone.LesFalaisesDeLOubli,   Etat.Sec,    this, new Coordonnees(1,4));
        tuiles[2][0] = new Tuile(Zone.LePalaisDeCorail,      Etat.Sec,    this, new Coordonnees(2,0), Color.CYAN);
        tuiles[2][1] = new Tuile(Zone.LaPorteDArgent,        Etat.Sec,    this, new Coordonnees(2,1));
        tuiles[2][2] = new Tuile(Zone.LesDunesDeLIllusion,   Etat.Sombré, this, new Coordonnees(2,2));
        tuiles[2][3] = new Tuile(Zone.Heliport,              Etat.Sec,    this, new Coordonnees(2,3));
        tuiles[2][4] = new Tuile(Zone.LaPorteDeCuivre,       Etat.Sec,    this, new Coordonnees(2,4));
        tuiles[2][5] = new Tuile(Zone.LeJardinDesHurlements, Etat.Sec,    this, new Coordonnees(2,5), Color.ORANGE);
        tuiles[3][0] = new Tuile(Zone.LaForetPourpre,        Etat.Sec,    this, new Coordonnees(3,0));
        tuiles[3][1] = new Tuile(Zone.LeLagonPerdu,          Etat.Inondé, this, new Coordonnees(3,1));
        tuiles[3][2] = new Tuile(Zone.LeMaraisBrumeux,       Etat.Sombré, this, new Coordonnees(3,2));
        tuiles[3][3] = new Tuile(Zone.Observatoire,          Etat.Inondé, this, new Coordonnees(3,3));
        tuiles[3][4] = new Tuile(Zone.LeRocherFantome,       Etat.Sombré, this, new Coordonnees(3,4));
        tuiles[3][5] = new Tuile(Zone.LaCaverneDuBrasier,    Etat.Inondé, this, new Coordonnees(3,5), Color.MAGENTA);
        tuiles[4][1] = new Tuile(Zone.LeTempleDuSoleil,      Etat.Sec,    this, new Coordonnees(4,1), Color.GRAY);
        tuiles[4][2] = new Tuile(Zone.LeTempleDeLaLune,      Etat.Sombré, this, new Coordonnees(4,2), Color.GRAY);
        tuiles[4][3] = new Tuile(Zone.LePalaisDesMarees,     Etat.Sec,    this, new Coordonnees(4,3), Color.CYAN);
        tuiles[4][4] = new Tuile(Zone.LeValDuCrepuscule,     Etat.Sec,    this, new Coordonnees(4,4));
        tuiles[5][2] = new Tuile(Zone.LaTourDuGuet,          Etat.Sec,    this, new Coordonnees(5,2));
        tuiles[5][3] = new Tuile(Zone.LeJardinDesMurmures,   Etat.Inondé, this, new Coordonnees(5,3), Color.ORANGE);
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================

    /**
     * Retourne le tableau complet des tuiles de la grille.
     *
     * @return tableau 6×6 de tuiles (certaines cellules peuvent être {null})
     */
    public Tuile[][] getTuiles() {
        return tuiles;
    }

    /**
     * Retourne la tuile à la position (x, y).
     *
     * @param x indice de ligne (0–5)
     * @param y indice de colonne (0–5)
     * @return la tuile à cette position, ou {null} si hors plateau
     */
    public Tuile getTuile(int x, int y) {
        return tuiles[x][y];
    }

    /**
     * Retourne la tuile aux coordonnées données.
     *
     * @param coo les coordonnées de la tuile
     * @return la tuile correspondante, ou {null} si hors plateau
     */
    public Tuile getTuile(Coordonnees coo) {
        return tuiles[coo.getX()][coo.getY()];
    }

    /**
     * Recherche et retourne la tuile associée à une zone nommée.
     *
     * @param zone la zone à rechercher
     * @return la tuile correspondante, ou {null} si non trouvée
     */
    public Tuile getTuile(Zone zone) {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                Tuile t = tuiles[x][y];
                if (t != null && t.getIntitule() == zone) {
                    return t;
                }
            }
        }
        return null;
    }
}
