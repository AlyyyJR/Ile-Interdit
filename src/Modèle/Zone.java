// ==================================================================
// FICHIER : Zone.java
// Projet  : L'Île Interdite
// Auteur  : Aly KONATE & Julien DENIS - L2 Informatique
// ==================================================================
// Énumération des 24 zones nommées présentes sur le plateau de jeu.
//
// Rôle de Zone :
//   - nommer chacune des 24 tuiles avec un identifiant unique CamelCase
//   - fournir nomEspace() pour l'affichage lisible avec espaces
//   - identifier les tuiles spéciales (Heliport) et les paires de reliques
//
// Utilisé par : Tuile, Grille, Joueur (spawnPoint), Controleur (isPartiePerdue)
// ==================================================================

package Modèle;

/**
 * Énumération des 24 zones (tuiles nommées) présentes sur le plateau de L'Île Interdite.
 *
 * Chaque constante correspond à un lieu unique dont le nom est encodé en CamelCase sans espaces.
 * La méthode {#nomEspace()} permet d'obtenir un affichage lisible avec espaces.
 *
 * Les zones sont réparties en quatre familles de reliques, identifiables par leur préfixe :
 * 
 *   LAC → relique Magenta (Cristal Ardent)
 *   LET → relique Grise (Pierre Sacrée)
 *   LEP → relique Cyan (Calice de l'Onde)
 *   LEJ → relique Orange (Statue du Zéphyr)
 * 
 * Certaines zones sont également spéciales, comme l'Heliport, point de départ du Pilote.
 * L'énumération {Zone} est un élément central du modèle du jeu, fournissant une identification claire et structurée des différentes tuiles du plateau, ce qui facilite la gestion des déplacements, des actions et des conditions de victoire ou de défaite liées à ces zones. 
 * Par exemple, le contrôleur peut utiliser les constantes de {Zone} pour vérifier si une tuile spécifique a été inondée ou asséchée, ou pour déterminer si un joueur se trouve sur une zone critique comme l'Heliport. 
 * En somme, {Zone} joue un rôle clé dans l'organisation et la logique du jeu.
 */
public enum Zone {

    // ==================================================================
    // Zones standard
    // ==================================================================
    LePontDesAbimes,
    LaPorteDeBronze,
    LaCaverneDesOmbres,
    LaPorteDeFer,
    LaPorteDOr,
    LesFalaisesDeLOubli,
    LePalaisDeCorail,
    LaPorteDArgent,
    LesDunesDeLIllusion,
    Heliport,
    LaPorteDeCuivre,
    LeJardinDesHurlements,
    LaForetPourpre,
    LeLagonPerdu,
    LeMaraisBrumeux,
    Observatoire,
    LeRocherFantome,
    LaCaverneDuBrasier,
    LeTempleDuSoleil,
    LeTempleDeLaLune,
    LePalaisDesMarees,
    LeValDuCrepuscule,
    LaTourDuGuet,
    LeJardinDesMurmures;

    /**
     * Retourne une représentation lisible du nom de la zone, avec des espaces
     * insérés avant chaque lettre majuscule non précédée d'une autre majuscule.
     *
     * Exemple : {LePontDesAbimes} → {"Le Pont Des Abimes"}
     *
     * @return le nom de la zone avec espaces
     */
    public String nomEspace() {
        String nomOriginal = this.name();
        StringBuilder resultat = new StringBuilder();
        resultat.append(nomOriginal.charAt(0));

        for (int i = 1; i < nomOriginal.length(); i++) {
            char actuel   = nomOriginal.charAt(i);
            char precedent = nomOriginal.charAt(i - 1);

            if (Character.isUpperCase(actuel) && !Character.isUpperCase(precedent)) {
                resultat.append(' ');
            }
            resultat.append(actuel);
        }

        return resultat.toString();
    }
}
