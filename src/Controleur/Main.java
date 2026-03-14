// ==================================================================
// FICHIER : Main.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Point d'entrée principal du jeu L'Île Interdite.
//
// Rôle de Main :
//   - instancier le Contrôleur qui initialise toutes les vues
//   - lancer la boucle de jeu via Controleur.play()
//   - constituer l'unique point de démarrage de l'application
//
// Utilisé par : JVM (point d'entrée)
// ==================================================================

package Controleur;

/**
 * Point d'entrée principal du jeu L'Île Interdite.
 *
 * <p>Instancie le {@link Controleur} — qui initialise les vues et
 * démarre l'interface graphique — puis lance la boucle de jeu via
 * {@link Controleur#play()}.</p>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 * @see Controleur
 */
public class Main {

    // ==================================================================
    // Point d'entrée
    // ==================================================================

    /**
     * Lance l'application L'Île Interdite.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        Controleur c = new Controleur();
        c.play();
    }
}
