// ==================================================================
// FICHIER : Messager.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Rôle Messager : peut donner une carte à n'importe quel joueur quelle que soit sa position.
//
// Rôle de Messager :
//   - étendre Joueur et redéfinir isDonPossible() sans contrainte de voisinage
//   - spawner sur LaForetPourpre
//   - permettre les dons à distance entre aventuriers
//
// Utilisé par : Controleur (creerJoueur)
// ==================================================================

package Modèle;

import Controleur.Controleur;
import Vue.VueDonDeCartes;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Représente le rôle du Messager dans le jeu L'Île Interdite.
 *
 * Le Messager peut donner des cartes trésor à n'importe quel joueur de l'île,
 * sans restriction de position (pas besoin d'être sur la même tuile).
 * Cette capacité lui confère une grande flexibilité pour coordonner les échanges.
 *
 * Point de départ : {Zone#LaPorteDArgent} — Couleur : Blanc.
 * 
 * Le Messager est un aventurier spécialisé dans la communication et la coopération entre les joueurs. Sa capacité à donner des cartes à distance lui permet de faciliter les échanges de ressources, même lorsque les aventuriers sont séparés par des zones inondées ou des obstacles. 
 * En tant que tel, il est souvent considéré comme un rôle clé pour maintenir une bonne dynamique d'équipe et assurer une gestion efficace des cartes trésor tout au long de la partie. 
 */
public class Messager extends Joueur {

    // ==================================================================
    // Constructeur
    // ==================================================================

    /**
     * Initialise le Messager avec son point de départ, sa couleur et son image.
     *
     * @param nom        le nom du joueur
     * @param controleur le contrôleur central du jeu
     */
    public Messager(String nom, Controleur controleur) {
        super(nom, controleur);
        this.spawnPoint = Zone.LaPorteDArgent;
        this.setCouleur(Color.WHITE);
        this.setImage(new ImageIcon(this.getClass().getResource("/ImagesAventuriers/Messager.png")));
    }

    // ==================================================================
    // Capacités spéciales
    // ==================================================================

    /**
     * Ouvre l'interface de don de cartes en proposant tous les joueurs de la partie
     * comme destinataires possibles, quelle que soit leur position sur le plateau.
     */
    @Override
    public void donnerCarte() {
        ArrayList<Joueur> joueursPossibles = new ArrayList<>(this.getControleur().getJoueurs());
        new VueDonDeCartes(this, joueursPossibles);
    }

    /**
     * Vérifie si le Messager peut donner une carte.
     *
     * Le don est possible dès qu'il possède au moins une carte trésor non spéciale
     * (Hélicoptère et Sac de Sable exclus), indépendamment de sa position.
     *
     * @return {true} si au moins une carte trésor normale est en main
     */
    @Override
    public boolean isDonPossible() {
        for (CarteTresor carte : this.getMainJoueur()) {
            if (carte.getType() != TypeCarte.SpécialHélicoptère
                    && carte.getType() != TypeCarte.SpécialSacDeSable) {
                return true;
            }
        }
        return false;
    }
}
