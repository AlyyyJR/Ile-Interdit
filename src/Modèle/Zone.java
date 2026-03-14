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
 * <p>Chaque constante correspond à un lieu unique dont le nom est encodé en CamelCase sans espaces.
 * La méthode {@link #nomEspace()} permet d'obtenir un affichage lisible avec espaces.</p>
 *
 * <p>Les zones sont réparties en quatre familles de reliques, identifiables par leur préfixe :</p>
 * <ul>
 *   <li>LAC → relique Magenta (Cristal Ardent)</li>
 *   <li>LET → relique Grise (Pierre Sacrée)</li>
 *   <li>LEP → relique Cyan (Calice de l'Onde)</li>
 *   <li>LEJ → relique Orange (Statue du Zéphyr)</li>
 * </ul>
 *
 * @author Aly KONATE &amp; Julien DENIS
 * @version 1.0
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
     * <p>Exemple : {@code LePontDesAbimes} → {@code "Le Pont Des Abimes"}</p>
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
