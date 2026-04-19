JAVAZIC - Plateforme de Streaming Musical
JAVAZIC est une application Java robuste conçue pour la gestion et l'écoute de musique. Ce projet pédagogique illustre une séparation stricte des responsabilités grâce à l'architecture MVC (Modèle-Vue-Contrôleur) et offre une expérience utilisateur flexible via une interface Console ou Graphique (Swing).

Fonctionnalités principales :


Profils Utilisateurs :

Visiteur : Accès au catalogue avec une limite de 5 écoutes gratuites. Un compteur dynamique affiche la progression (ex: 1 / 5).
Abonné : Écoutes illimitées, gestion de playlists personnalisées, historique d'écoute et système de recommandations basées sur les préférences.
Administrateur : Gestion complète du catalogue (ajout/suppression de morceaux), gestion des utilisateurs et consultation de statistiques avancées (Top morceaux, Top artistes).

Gestion Musicale

Catalogue complet : Recherche par titre ou par artiste.
Lecteur Intégré : Simulation de lecture temps réel avec gestion de la Pause et de l'Arrêt.
Persistance : Sauvegarde automatique de toutes les données (utilisateurs, catalogue, playlists, historiques) dans des fichiers texte (.txt).

Architecture du Projet : 

Le projet est découpé en quatre packages principaux pour garantir la maintenabilité du code :
modele : Contient la logique métier et les structures de données (Morceau, Abonne, Utilisateur, etc.). Utilise l'héritage pour gérer les droits d'écoute.
vue : Implémente le pattern Strategy avec des interfaces (IVueAbonne, IVueCatalog) déclinées en versions Console et Swing.
controleur : Orchestre les interactions. Le ControleurMain injecte les dépendances et gère le cycle de vie de l'application.
utilitaire : Gère l'accès aux données via le GestionnaireFichiers.

Installation et Lancement
Prérequis
Java JDK 8 ou supérieur.

Un IDE (IntelliJ IDEA, Eclipse) ou un terminal.

Lancement
Clonez ou téléchargez le dépôt.

Compilez les fichiers source depuis la racine.

Lancez la classe principale :

Bash
java Main
Au démarrage, choisissez votre mode d'affichage :

1 : Mode Terminal (Console)

2 : Mode Interface Graphique (Swing)

Aperçu de l'Interface :

L'interface Swing a été conçue pour être intuitive et s'adapte automatiquement à votre écran grâce au mode plein écran maximisé :
Navigation : Menu latéral pour un accès rapide aux fonctionnalités.
Lecteur : Barre de progression dynamique en bas de l'écran pendant l'écoute.
Contrôle : En mode console, des menus de pause (p) et d'arrêt (q) sont disponibles, tandis qu'en Swing, des boutons dédiés gèrent le flux.

