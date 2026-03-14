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
 * Instancie le {Controleur} — qui initialise les vues et
 * démarre l'interface graphique — puis lance la boucle de jeu via
 * {Controleur#play()}.
 *
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
