// ==================================================================
// FICHIER : Observateur.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Classe abstraite représentant un observateur dans le patron MVC.
//
// Rôle de Observateur :
//   - définir le contrat de réception des messages émis par les vues
//   - assurer la synchronisation entre le thread de jeu et l'EDT Swing
//   - fournir waitForInput() pour bloquer le jeu en attente d'une action
//   - fournir notifier() pour réveiller le thread de jeu depuis l'EDT
//
// Patron : Observateur (variante du patron Observer)
// Étendu par : Controleur
// Connu de : toutes les vues (Vue.*) via leur référence à Observateur
// ==================================================================

package Controleur;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe abstraite représentant un observateur dans le patron MVC.
 *
 * Chaque vue connaît un {Observateur} et lui transmet des
 * {Message} lorsque l'utilisateur interagit. Le contrôleur
 * {Controleur} étend cette classe et implémente les trois
 * méthodes abstraites de traitement.
 *
 * La synchronisation entre le thread Swing (EDT) et le thread de jeu
 * principal est assurée par un {ReentrantLock} et sa {Condition}
 * associée :
 * 
 *   {#waitForInput()} bloque le thread courant en attente d'un signal ;
 *   {#notifier()} réveille le thread bloqué.
 * 
 * Observateur
 * ├── update(Message) : reçoit les messages des vues et réagit en conséquence
 * En somme, {Observateur} est un élément clé du design MVC du jeu, fournissant une interface claire pour la communication entre les vues et le contrôleur, tout en assurant une synchronisation efficace entre les différents threads impliqués dans l'exécution du jeu. 
 * Par exemple, lorsque le joueur "Aly" clique sur une tuile du plateau, la {VuePlateau} émet un {MessagePlateau} avec les coordonnées de la tuile ciblée. 
 * Le contrôleur reçoit ce message, traite l'action en fonction du rôle et de la position d'Alice, puis met à jour le modèle et les vues en conséquence. 
 * Pendant ce temps, le thread de jeu principal est bloqué dans {waitForInput()} en attendant que l'EDT Swing signale que l'action a été traitée, assurant ainsi une coordination fluide entre les interactions utilisateur et la logique du jeu. De cette manière, {Observateur} joue un rôle central dans la gestion des interactions et de la synchronisation dans le jeu, facilitant une expérience utilisateur réactive et cohérente. 
 */
public abstract class Observateur {

    // ==================================================================
    // Attributs — synchronisation
    // ==================================================================

    /** Verrou réentrant pour la synchronisation thread de jeu / EDT. */
    private final Lock lock;

    /** Condition associée au verrou pour attendre ou signaler. */
    private final Condition condition;

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise le verrou réentrant et la condition de synchronisation.
     */
    public Observateur() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    // ==================================================================
    // Méthodes abstraites — traitement des messages
    // ==================================================================

    /**
     * Traite un message générique envoyé par une vue (menu, cartes, validation).
     *
     * @param msg le message reçu (non nul)
     */
    public abstract void traiterMessage(Message msg);

    /**
     * Traite un message issu d'un clic sur le plateau de jeu.
     *
     * @param msg le message plateau contenant les coordonnées cliquées (non nul)
     */
    public abstract void traiterMessagePlateau(MessagePlateau msg);

    /**
     * Traite un message issu d'un clic sur un bouton d'action d'un aventurier.
     *
     * @param msg le message aventurier contenant le type d'action et le joueur (non nul)
     */
    public abstract void traiterMessageAventurier(MessageAventurier msg);

    // ==================================================================
    // Méthodes — synchronisation
    // ==================================================================

    /**
     * Suspend le thread appelant jusqu'à ce que {#notifier()} soit invoqué.
     *
     * Utilisé pour bloquer le thread de jeu en attente d'une interaction
     * utilisateur provenant de l'EDT Swing.
     */
    public void waitForInput() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Réveille le thread bloqué dans {#waitForInput()}.
     *
     * Appelé depuis l'EDT Swing lorsqu'une interaction utilisateur
     * a été traitée et que le jeu peut reprendre.
     */
    public void notifier() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
