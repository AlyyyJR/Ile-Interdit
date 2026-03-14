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
 * <p>Chaque vue connaît un {@code Observateur} et lui transmet des
 * {@link Message} lorsque l'utilisateur interagit. Le contrôleur
 * ({@link Controleur}) étend cette classe et implémente les trois
 * méthodes abstraites de traitement.</p>
 *
 * <p>La synchronisation entre le thread Swing (EDT) et le thread de jeu
 * principal est assurée par un {@link ReentrantLock} et sa {@link Condition}
 * associée :</p>
 * <ul>
 *   <li>{@link #waitForInput()} bloque le thread courant en attente d'un signal ;</li>
 *   <li>{@link #notifier()} réveille le thread bloqué.</li>
 * </ul>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
 * @see Controleur
 * @see Message
 * @see MessageAventurier
 * @see MessagePlateau
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
     * Suspend le thread appelant jusqu'à ce que {@link #notifier()} soit invoqué.
     *
     * <p>Utilisé pour bloquer le thread de jeu en attente d'une interaction
     * utilisateur provenant de l'EDT Swing.</p>
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
     * Réveille le thread bloqué dans {@link #waitForInput()}.
     *
     * <p>Appelé depuis l'EDT Swing lorsqu'une interaction utilisateur
     * a été traitée et que le jeu peut reprendre.</p>
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
