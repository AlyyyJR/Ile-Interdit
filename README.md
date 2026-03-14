# L'Île Interdite — Jeu coopératif en Java

> Projet POGL — Aly KONATE & Julien DENIS | L2 Informatique Groupe 1  
> Adaptation complète du jeu de société *Forbidden Island* en Java Swing, architecture MVC stricte avec pattern Observer et Template Method

---

## Présentation du projet

Ce projet est une **adaptation fidèle du jeu de société coopératif *L'Île Interdite*** (Forbidden Island), développé en Java Swing dans le cadre du cours de Programmation Orientée Objet et Génie Logiciel (POGL) en Licence 2.

L'objectif pédagogique était de concevoir une application **multi-joueurs, temps réel, entièrement graphique**, en appliquant les grands patterns de conception orientée objet. La communication entre la Vue et le Contrôleur repose sur un système de messages typés (pattern Observer), tandis que chaque rôle d'aventurier surcharge une méthode abstraite de la classe `Joueur` pour exprimer ses capacités spéciales (pattern Template Method). Toute la boucle de jeu est synchronisée avec les interactions utilisateur via `ReentrantLock` et `Condition`, ce qui permet de mettre en pause le thread principal en attendant l'action du joueur sans bloquer l'interface Swing.

Le jeu supporte **2 à 4 joueurs**, 6 rôles distincts, 5 niveaux de difficulté, un mode debug, et une interface graphique complète avec écran titre, formulaire d'inscription, plateau de jeu en calques superposés, panneaux aventurier, et plusieurs fenêtres contextuelles (défausse, don de cartes, cartes spéciales, montée des eaux, fin de partie).

---

## Objectifs du projet

- Implémenter un **jeu de plateau complet** avec règles fidèles à l'original (Forbidden Island)
- Appliquer le **pattern Observer** pour découpler totalement Vue et Contrôleur
- Utiliser le **pattern Template Method** pour factoriser les comportements communs des 6 rôles
- Concevoir une **interface graphique Swing** multi-panneaux avec calques superposés (`JLayeredPane`)
- Synchroniser la boucle de jeu avec les inputs utilisateur via **`ReentrantLock` / `Condition`**
- Produire un code **propre, documenté en Javadoc L2** et maintenable (aucun `System.out.println` en production)

---

## Stack technique

| Composant | Technologie |
|---|---|
| **Langage** | Java 17 |
| **Interface graphique** | Java Swing (`JFrame`, `JLayeredPane`, `GridLayout`, `BorderLayout`, `JDialog`) |
| **Synchronisation threads** | `ReentrantLock` + `Condition` (attente bloquante sur l'input utilisateur) |
| **Collections** | `ArrayList`, `Stack`, `Vector` (pioche, défausse, locataires des tuiles) |
| **Ressources graphiques** | `ImageIcon` chargées depuis le classpath (`src/`) |
| **Patterns OO** | Observer, Template Method |
| **Diagramme UML** | Généré en Python avec `reportlab` (format A3 paysage, 33 classes) |

---

## Règles du jeu

### But

Les joueurs coopèrent ensemble pour **récupérer les 4 reliques sacrées** (Statue de Vent, Calice des Océans, Cristal du Feu, Pierre de la Terre) cachées dans les ruines de l'île, puis **rejoindre l'héliport** et s'enfuir avant que l'île ne soit entièrement submergée.

### Le plateau

L'île est composée de **24 tuiles nommées** disposées sur une grille 6×6. Les cases aux quatre coins et en bordure extrême sont vides (eau, infranchissables). Chaque tuile porte un nom de lieu et peut se trouver dans trois états au fil de la partie :

| État | Signification | Conséquence |
|---|---|---|
| **Sec** | Tuile normale, praticable | Aucune |
| **Inondé** | Partiellement submergée | Peut être asséchée (1 action) |
| **Sombré** | Disparue, retirée du plateau | Irréversible — ne peut plus être traversée |

```
         Col 0   Col 1   Col 2   Col 3   Col 4   Col 5
Ligne 0   ~~~     ~~~    [T01]   [T02]    ~~~     ~~~
Ligne 1   ~~~    [T03]   [T04]  [T05]   [T06]    ~~~
Ligne 2  [T07]   [T08]   [T09]  [T10]   [T11]   [T12]
Ligne 3  [T13]   [T14]   [T15]  [T16]   [T17]   [T18]
Ligne 4   ~~~    [T19]   [T20]  [T21]   [T22]    ~~~
Ligne 5   ~~~     ~~~    [T23]  [T24]    ~~~     ~~~

~~~  = eau (case vide, jamais accessible)
[Txx] = tuile nommée (Sec / Inondé / Sombré)
```

### Déroulement d'un tour

```
1. ACTIONS (3 par défaut, 4 pour le Pilote)
       ├─ Déplacer       → vers une tuile adjacente non Sombré
       ├─ Assécher       → une tuile Inondée adjacente ou sous le joueur
       ├─ Donner carte   → à un joueur sur la même tuile (Messager : à distance)
       └─ Prendre relique → 4 cartes du même type + être sur la bonne tuile

2. PIOCHER 2 cartes Trésor
       → Si "Montée des Eaux" piochée : appliquer immédiatement puis continuer

3. PIOCHER N cartes Inondation (N = niveau d'eau actuel, entre 2 et 6)
       → Chaque carte inonde (Sec→Inondé) ou coule (Inondé→Sombré) la tuile tirée
```

### Conditions de victoire et de défaite

La partie se termine immédiatement dans l'un de ces cas :

```
VICTOIRE
  ✔  4 reliques récupérées
  ✔  Tous les joueurs sur la tuile Héliport
  ✔  Au moins 1 carte Hélicoptère en main

DÉFAITE — l'une de ces conditions suffit
  ✘  La tuile Héliport sombre → impossible de s'enfuir
  ✘  Une tuile relique sombre avant sa capture → relique perdue à jamais
  ✘  Un aventurier se retrouve sur une tuile qui sombre sans tuile adjacente accessible → noyé
  ✘  La pioche de cartes Inondation est épuisée → l'île s'est trop inondée
```

### Niveaux de difficulté

| Niveau | Valeur interne | Comportement |
|---|---|---|
| Novice | 1 | Montée des eaux lente |
| Normal | 2 | Montée normale (défaut) |
| Élite | 3 | Montée soutenue |
| Légendaire | 4 | Très agressive |
| Mortel | 10 | Niveau d'eau maximal dès le départ |

### Rôles des aventuriers

| Rôle | Couleur | Capacité spéciale |
|---|---|---|
| **Explorateur** | Vert | Se déplace et assèche en diagonale (8 directions) |
| **Navigateur** | Jaune | Peut déplacer un autre joueur de 2 cases par action |
| **Pilote** | Blanc | Se téléporte sur n'importe quelle tuile 1×/tour — 4 actions/tour |
| **Plongeur** | Noir | Traverse les tuiles Sombré pour rejoindre la première tuile accessible |
| **Ingénieur** | Rouge | Assèche 2 tuiles pour le coût d'une seule action |
| **Messager** | Bleu clair | Donne des cartes sans être sur la même tuile que le receveur |

### Types de cartes

**Pioche Trésor — 28 cartes :**

| Type | Qté | Effet |
|---|---|---|
| `TresorGray` — Statue de Vent | 5 | Réunir 4× → capturer la relique grise |
| `TresorCyan` — Calice des Océans | 5 | Réunir 4× → capturer la relique cyan |
| `TresorMagenta` — Cristal du Feu | 5 | Réunir 4× → capturer la relique magenta |
| `TresorOrange` — Pierre de la Terre | 5 | Réunir 4× → capturer la relique orange |
| `SpécialHélicoptère` | 3 | Déplacer tous les joueurs d'une tuile vers une autre (ou victoire finale) |
| `SpécialSacDeSable` | 3 | Assécher immédiatement n'importe quelle tuile, même Sombré → Inondé |
| `MontéeEaux` | 3 | Montée des eaux immédiate : défausse Inondation réinsérée dans la pioche |

**Pioche Inondation — 24 cartes (une par tuile nommée) :**
Tirées en fin de tour. Chaque carte inonde (Sec→Inondé) ou coule (Inondé→Sombré) la tuile correspondante. Quand la pioche est vide, la défausse est remélangée et replacée au-dessus.

---

## Structure du projet

```
PJ-Ile-Interdite-POGL-KONATE-DENIS-G1/
├── src/
│   ├── Controleur/
│   │   ├── Controleur.java               # Boucle de jeu, dispatch des messages, logique de tour
│   │   ├── Main.java                     # Point d'entrée — instancie Controleur et démarre la Vue
│   │   ├── Observateur.java              # Classe abstraite Observer (ReentrantLock + Condition)
│   │   ├── Message.java                  # Message générique portant un TypeMessage
│   │   ├── MessageAventurier.java        # Sous-classe : actions joueur + référence Joueur
│   │   ├── MessagePlateau.java           # Sous-classe : clic plateau + objet Coordonnees
│   │   └── TypeMessage.java              # Enum — 12 types d'événements (Valider, Jouer, Déplacer…)
│   │
│   ├── Modèle/
│   │   ├── Joueur.java                   # Classe abstraite — Template Method (listerCasesDispo)
│   │   ├── Explorateur.java              # Déplacement + assèchement en 8 directions
│   │   ├── Navigateur.java               # Déplacement d'un autre joueur sur 2 cases
│   │   ├── Pilote.java                   # Téléportation libre 1×/tour, 4 actions par tour
│   │   ├── Plongeur.java                 # BFS à travers les tuiles Sombré
│   │   ├── Ingénieur.java                # Assèche 2 tuiles pour 1 action
│   │   ├── Messager.java                 # Don de cartes sans contrainte de distance
│   │   ├── Grille.java                   # Plateau 6×6, initialisation et accès aux Tuile
│   │   ├── Tuile.java                    # Case : état (Etat), locataires, coordonnées, image
│   │   ├── Coordonnees.java              # Paire (x, y) immuable
│   │   ├── CarteInondation.java          # Carte liée à une tuile nommée (Zone)
│   │   ├── CarteTresor.java              # Carte avec TypeCarte et ImageIcon
│   │   ├── Etat.java                     # Enum : Sec | Inondé | Sombré
│   │   ├── Zone.java                     # Enum : 24 noms de tuiles + nomEspace()
│   │   └── TypeCarte.java                # Enum : 7 types de cartes Trésor + Montée Eaux
│   │
│   └── Vue/
│       ├── VuePlateau.java               # Plateau principal — JLayeredPane 3 calques
│       ├── VueAventurier.java            # Panneau joueur actif (cartes en main + 6 boutons)
│       ├── VueInscription.java           # Formulaire démarrage (noms, rôles, difficulté, debug)
│       ├── VueMonteeEaux.java            # Fenêtre alerte visuelle montée des eaux
│       ├── VueReliques.java              # Suivi des 4 reliques (icônes gris → couleur)
│       ├── VueEcranTitre.java            # Écran d'accueil avec fond et bouton Jouer
│       ├── VueFinDePartie.java           # Écran victoire / défaite selon le code de fin
│       ├── VueDefausse.java              # Sélection d'une carte à défausser (main > 5 cartes)
│       ├── VueCarteSpe.java              # Utilisation Hélicoptère ou Sac de sable
│       ├── VueDonDeCartes.java           # Sélection carte + joueur cible pour un don
│       └── AfficheImage.java             # JPanel fond d'image avec redimensionnement
│
├── src/ImagesAventuriers/                # Portraits et cartes de rôle des 6 aventuriers
├── src/ImagesCartesTresor/               # Illustrations des 7 types de cartes Trésor
├── src/ImagesTuiles/                     # Visuels des tuiles dans leurs 3 états
├── src/resources/                        # Fonds d'écran, boutons, PDF des règles
│   └── Trésors/                          # Illustrations des 4 reliques récupérables
│
├── DiagrammeDeClasses.pdf                # Diagramme UML complet — 33 classes, A3 paysage
├── Diagramme_de_Classe.pdf               # Diagramme UML v1 initial
└── README.md                             # Ce fichier
```

---

## Architecture technique

```
┌──────────────────────────────┐    Message (TypeMessage)    ┌───────────────────────────┐
│             VUE              │ ──────────────────────────▶ │        CONTRÔLEUR         │
│                              │                             │                           │
│  VuePlateau                  │ ◀────────────────────────── │  Controleur.java          │
│  VueAventurier               │     notifier() → update()   │  (extends Observateur)    │
│  VueInscription              │                             │                           │
│  VueFinDePartie              │                             └──────────────┬────────────┘
│  VueMonteeEaux ...           │                                            │
└──────────────────────────────┘                                            │ lit / modifie
                                                                            ▼
                                                               ┌────────────────────────────┐
                                                               │           MODÈLE           │
                                                               │                            │
                                                               │  Grille   Tuile   Joueur   │
                                                               │  CarteTresor   CarteInond. │
                                                               │  Etat   Zone   TypeCarte   │
                                                               └────────────────────────────┘
```

La Vue ne modifie jamais le Modèle directement. Elle envoie un `Message` (ou ses sous-classes) au Contrôleur, qui traite l'action, met à jour le Modèle, puis appelle `notifier()` pour que la Vue se rafraîchisse via `update()`.

### Pattern Observer — synchronisation inter-thread

`Observateur` est une classe abstraite qui encapsule un `ReentrantLock` et une `Condition`. La boucle principale de `Controleur.play()` appelle `waitForInput()` qui suspend le thread de jeu jusqu'à ce que la Vue envoie un événement :

```java
// Observateur.java
public void waitForInput() {
    lock.lock();
    try {
        condition.await();          // suspend le thread de jeu
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // bonne pratique L2
    } finally {
        lock.unlock();
    }
}

public void notifier() {
    lock.lock();
    try {
        condition.signal();         // réveille waitForInput()
    } finally {
        lock.unlock();
    }
}
```

Le Contrôleur implémente deux méthodes de dispatch distinctes selon la source du message :

```java
// Message venant des boutons de VueAventurier
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

// Clic sur une tuile du plateau → coordonnées de la case
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
```

### Pattern Template Method — hiérarchie des rôles

`Joueur` est abstraite et définit `listerCasesDispo()` comme méthode abstraite. Chaque sous-classe la surcharge pour exprimer sa logique propre, sans dupliquer `déplacer()`, `assecher()`, `donnerCarte()` ou `prendreRelique()` qui restent dans `Joueur` :

```
Joueur (abstract)
├── listerCasesDispo()    ← ABSTRACT — chaque rôle implémente ses cases accessibles
├── déplacer()            ← commun à tous
├── assecher()            ← commun (Ingénieur : décrémente 1 action pour 2 assèchements)
├── donnerCarte()         ← commun (Messager : supprime la contrainte de distance)
├── prendreRelique()      ← commun
├── utiliserHelico()      ← commun
└── utiliserSac()         ← commun

    ├── Explorateur  → listerCasesDispo() : 8 directions (N, S, E, O + diagonales)
    ├── Navigateur   → listerCasesDispo() : cases accessibles pour déplacer un autre joueur
    ├── Pilote       → listerCasesDispo() : toutes les tuiles non Sombré (1 usage/tour)
    ├── Plongeur     → listerCasesDispo() : BFS traversant les Sombré jusqu'à une tuile sèche
    ├── Ingénieur    → listerCasesDispo() : standard, assecher() gère le bonus ×2
    └── Messager     → listerCasesDispo() : standard, donnerCarte() sans contrainte positionnelle
```

---

## Pipeline d'un tour complet

```
Controleur.play()
  │
  └─ débutTour()
       ├─ Surligner les tuiles disponibles (bordure dorée sur VuePlateau)
       ├─ Mettre à jour VueAventurier (actions restantes, cartes en main)
       └─ waitForInput()  ◄───────────────────────────────────────────────────────┐
                                                                                  │
            Joueur clique sur un bouton dans VueAventurier                        │
            → VueAventurier.actionListener → new MessageAventurier(type, joueur)  │
            → controleur.traiterMessageAventurier(msg) → set actionChoisie        │
            → this.notifier() réveille la boucle ─────────────────────────────────┘
  │
  ├─ switch (actionChoisie)
  │
  │     case 1 — DÉPLACER
  │     │   surligner(joueurActif.listerCasesDispo())
  │     │   waitForInput()  ← attente clic plateau
  │     │   MessagePlateau(coo) → joueurActif.déplacer(tuile)
  │     │   nbActions--
  │     │
  │     case 2 — ASSÉCHER
  │     │   surligner(tuiles inondées adjacentes)
  │     │   waitForInput()  ← attente clic plateau
  │     │   joueurActif.assecher(tuile)   [Ingénieur : 2 tuiles pour 1 action]
  │     │   nbActions--
  │     │
  │     case 3 — DONNER CARTE
  │     │   ouvrir VueDonDeCartes(joueurActif, cibles)
  │     │   waitForInput()  ← sélection carte + joueur cible
  │     │   joueurActif.donnerCarte(carte, cible)
  │     │   nbActions--
  │     │
  │     case 4 — PRENDRE RELIQUE
  │     │   vérifier : 4 cartes du bon type + bonne tuile
  │     │   addRelique(i) → signalerPriseRelique(type) → VueReliques.update()
  │     │   défausser les 4 cartes utilisées
  │     │   nbActions--
  │     │
  │     case 5 — CARTE SPÉCIALE
  │     │   ouvrir VueCarteSpe(joueurActif, cartesSpeciales)
  │     │   waitForInput()  ← sélection carte
  │     │   joueurActif.utiliserHelico() ou utiliserSac()
  │     │   [ne coûte pas d'action]
  │     │
  │     case 6 — TERMINER TOUR
  │         piocherCarteTresorFinTour() × 2
  │           └─ si MontéeEaux → monteeEau() immédiate
  │           └─ si main > 5 cartes → ouvrir VueDefausse → waitForInput()
  │         piocherCarteInondeFinTour(difficulte) × N
  │           └─ màj tuiles → VuePlateau.update()
  │           └─ si tuile héliport ou relique sombre → GAME OVER
  │         passer au joueur suivant → retour à débutTour()
  │
  └─ Vérification fin de partie à chaque action critique
       ├─ Victoire → VueFinDePartie(1)
       └─ Défaite  → VueFinDePartie(2..6) selon la cause
```

---

## Interface graphique

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                             VueEcranTitre                                      │
│                                                                                │
│                    [ Logo L'Île Interdite + fond illustré ]                    │
│                                                                                │
│                               [ JOUER ]                                        │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │ clic JOUER
                                    ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│                            VueInscription                                      │
│                                                                                │
│   Joueur 1 : [ Nom _________ ]  [ Rôle ▼ ]    Obligatoire                      │
│   Joueur 2 : [ Nom _________ ]  [ Rôle ▼ ]    Optionnel                        │
│   Joueur 3 : [ Nom _________ ]  [ Rôle ▼ ]    Optionnel                        │
│   Joueur 4 : [ Nom _________ ]  [ Rôle ▼ ]    Optionnel                        │
│                                                                                │
│   Difficulté : [ Normal ▼ ]            [ ] Mode debug      [ VALIDER ]         │
└────────────────────────────────────────────────────────────────────────────────┘
                                    │ validation
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  VueReliques :  [ ◇ gris ]  [ ◇ gris ]  [ ◇ gris ]  [ ◇ gris ]                  │
├─────────────────────────────────────────────┬───────────────────────────────────┤
│                                             │  VueAventurier                    │
│                VuePlateau                   │                                   │
│                                             │  Explorateur — Actions : ●●●      │
│  ┌────┬────┬────┬────┬────┬─────┐           │                                   │
│  │    │    │[T1]│[T2]│    │     │           │  [ C1 ] [ C2 ] [ C3 ] [ C4 ]      │
│  ├────┼────┼────┼────┼────┼─────┤           │                                   │
│  │    │[T3]│[T4]│[T5]│[T6]│     │  Calque 0 │  [ Déplacer  ]  [ Assécher  ]     │
│  ├────┼────┼────┼────┼────┼─────┤  tuiles   │  [ Donner   ]   [ Relique   ]     │
│  │[T7]│[T8]│....│....│....│[T12]│  Calque 1 │  [ Carte Spe ]  [ Fin Tour  ]     │
│  ├────┼────┼────┼────┼────┼─────┤  pions    │                                   │
│  │....│....│....│....│....│...  │  Calque 2 └───────────────────────────────────┤
│  └────┴────┴────┴────┴────┴─────┘  boutons                                      │
│                                             Tuiles surlignées = or (cliquables) │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**VuePlateau — 3 calques `JLayeredPane` superposés :**

| Calque | Contenu | Z-Order |
|---|---|---|
| Calque 0 (bas) | Images des tuiles selon leur état (sec / inondé / sombré) | Fond |
| Calque 1 (milieu) | Pions des aventuriers (`JLabel` + `ImageIcon`) | Au-dessus des tuiles |
| Calque 2 (haut) | Boutons transparents capturant les clics | Devant tout |

**Fenêtres contextuelles :**

| Fenêtre | Déclencheur | Rôle |
|---|---|---|
| `VueMonteeEaux` | Carte MontéeEaux piochée | Affiche le niveau d'eau, bloque temporairement |
| `VueDefausse` | Main du joueur > 5 cartes | Forcer la défausse d'une carte avant de continuer |
| `VueCarteSpe` | Bouton « Carte Spe » | Sélectionner et activer Hélico ou Sac de sable |
| `VueDonDeCartes` | Bouton « Donner » | Choisir une carte et un joueur cible |
| `VueFinDePartie` | Victoire ou défaite détectée | Afficher le résultat avec titre et sous-titre |

---

## Installation et lancement

### Prérequis

```bash
java -version      # Java 17 minimum requis
```

---

### Option 1 — IntelliJ IDEA (recommandé)

```
1. File → Open
   → Sélectionner le dossier PJ-Ile-Interdite-POGL-KONATE-DENIS-G1/

2. File → Project Structure → Modules → Sources
   → Marquer src/ comme "Sources Root"

3. Run → Edit Configurations → Add → Application
   → Main class : Controleur.Main
   → Working directory : <racine du projet>

4. Run → Run 'Controleur.Main'
```

---

### Option 2 — Ligne de commande (macOS / Linux)

```bash
# Se placer à la racine du projet
cd PJ-Ile-Interdite-POGL-KONATE-DENIS-G1/

# Compiler toutes les classes Java
javac -sourcepath src -d out $(find src -name "*.java")

# Lancer (les ressources sont chargées depuis src/)
java -cp out:src Controleur.Main
```

> **Important** : toujours lancer depuis la racine du projet. Les chemins vers les images (`ImagesAventuriers/`, `ImagesTuiles/`, etc.) sont relatifs à `src/`.

---

### Option 3 — Windows (PowerShell)

```powershell
# Depuis la racine du projet
Get-ChildItem -Recurse -Filter "*.java" src | Select-Object -ExpandProperty FullName | Out-File sources.txt
javac -sourcepath src -d out "@sources.txt"
java -cp "out;src" Controleur.Main
```

---

## Fonctionnalités

### Écran titre et inscription
- Écran d'accueil illustré avec fond de l'île
- Formulaire d'inscription pour 1 à 4 joueurs (le joueur 1 est obligatoire)
- Sélection du rôle par combo-box pour chaque joueur (Aléatoire disponible)
- Sélection du niveau de difficulté (Novice, Normal, Élite, Légendaire, Mortel)
- Mode debug activable par case à cocher

### Plateau de jeu
- Grille 6×6 avec les 24 tuiles nommées, positions fixes ou aléatoires
- Affichage dynamique des 3 états (sec / inondé / sombré) avec images distinctes
- Pions des aventuriers repositionnés en temps réel à chaque déplacement
- Surlinaige doré des tuiles accessibles selon le rôle et l'action en cours
- Mise à jour automatique à chaque action (déplacement, assèchement, noyade de tuile)

### Panneau aventurier
- Affichage du nom, du rôle et du nombre d'actions restantes du joueur actif
- Main de cartes illustrée (jusqu'à 5 cartes, forçage de défausse au-delà)
- Activation/désactivation des boutons selon les actions légales à l'instant T
- Passage automatique au joueur suivant après la fin du tour

### Gestion des cartes
- Pioche de cartes Trésor en fin de tour (2 cartes par joueur)
- Déclenchement immédiat de la montée des eaux si une carte `MontéeEaux` est piochée
- Pioche de cartes Inondation en nombre variable selon le niveau d'eau
- Remontée automatique de la défausse d'inondation dans la pioche quand elle est épuisée
- Fenêtre de défausse forcée si le joueur dépasse 5 cartes en main

### Cartes spéciales
- `SpécialHélicoptère` : téléporter tous les joueurs d'une tuile vers n'importe quelle autre
- `SpécialSacDeSable` : assécher immédiatement n'importe quelle tuile (même en cours de sombrage)
- Utilisation possible en dehors du tour du joueur (exception au tour de jeu)

### Reliques et victoire
- Capture d'une relique quand un joueur cumule 4 cartes du bon type sur la bonne tuile
- Affichage des reliques collectées dans `VueReliques` (passage de gris à couleur)
- Détection de la victoire dès que les 4 reliques sont récupérées + héliport + carte Hélico
- Détection des 4 conditions de défaite à chaque action critique

### Fin de partie
- Écran de fin avec titre et message adapté à la cause de la victoire ou défaite
- Arrêt propre du programme après affichage du résultat

---

## Mode debug

L'application inclut un **mode debug** activable depuis l'écran d'inscription (case à cocher). En mode debug, la grille est initialisée avec un placement de tuiles fixe et connu, et certaines tuiles sont pré-inondées, ce qui permet de tester rapidement les conditions de fin de partie sans attendre que le hasard les provoque.

---

## Diagramme de classes UML

Le fichier `DiagrammeDeClasses.pdf` (format A3 paysage) présente les 33 classes du projet réparties en 3 packages colorés. Il a été généré avec un script Python `reportlab` et intègre les flèches d'héritage entre `Joueur` et ses 6 sous-classes, entre `Observateur` et `Controleur`, et entre `Message` et ses deux spécialisations.

| Package | Couleur | Classes |
|---|---|---|
| `Modèle` | Vert clair | 15 (dont 3 enums : `Etat`, `Zone`, `TypeCarte`) |
| `Controleur` | Bleu clair | 7 (dont 1 enum : `TypeMessage`, 1 abstraite : `Observateur`) |
| `Vue` | Orange clair | 11 |

---

## Conventions de code (L2)

| Convention | Application dans le projet |
|---|---|
| **Javadoc** | Toutes les classes et méthodes publiques documentées (`@param`, `@return`, `@throws`, `@author`, `@version`) |
| **Comparaison String** | `.equals()` systématique — jamais `==` sur des `String` ou valeurs issues de `getSelectedItem()` |
| **Switch-arrow** | `case X -> ...` sur tous les branchements multi-cas |
| **Champs immuables** | `final` sur tous les champs qui ne changent pas après construction |
| **Chaînes vides** | `.isBlank()` plutôt que `.isEmpty()` (couvre les chaînes contenant uniquement des espaces) |
| **Logs** | Aucun `System.out.println` ni `System.err.println` en production |
| **InterruptedException** | `Thread.currentThread().interrupt()` après chaque `catch (InterruptedException e)` |
| **DRY** | Méthodes extraites : `traiterValider()` et `creerJoueur()` dans `Controleur` pour éviter la duplication |

---

## Auteurs

**Aly KONATE** & **Julien DENIS** — L2 Informatique Groupe 1  
Projet POGL — Programmation Orientée Objet et Génie Logiciel  
GitHub : [@AlyyyJR](https://github.com/AlyyyJR)
