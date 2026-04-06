package controleur;

import modele.Catalogue;
import modele.Abonne;
import modele.Morceau;
import modele.Utilisateur;
import vue.MenuPrincipalView;
import vue.VueCatalog;
import java.util.ArrayList;
import java.util.Scanner;

public class ControleurMain {
    // Données du programme
    private Catalogue catalogue;
    private ArrayList<Abonne> listeAbonnes;
    private Abonne utilisateurConnecte; // Session actuelle

    // Composants MVC
    private ControleUtilisateur controleUtilisateur;
    private ControleurCatalogue controleurCatalogue;
    private MenuPrincipalView menuPrincipal;
    private VueCatalog vueCatalog;

    public ControleurMain() {
        // Initialisation du modèle
        this.catalogue = new Catalogue();
        this.listeAbonnes = new ArrayList<>();
        this.utilisateurConnecte = null;

        // Initialisation des contrôleurs et vues [cite: 156]
        this.controleUtilisateur = new ControleUtilisateur();
        this.controleurCatalogue = new ControleurCatalogue();
        this.menuPrincipal = new MenuPrincipalView();
        this.vueCatalog = new VueCatalog();
    }

    public void lancer() {
        creerDonneesTest();
        boolean continuer = true;

        while (continuer) {
            // Affichage du menu principal [cite: 71, 78]
            int choix = menuPrincipal.afficherMenuInitial();

            switch (choix) {
                case 1: // Administrateur [cite: 72]
                    menuPrincipal.afficherMessage("Mode Administrateur non encore implémenté.");
                    break;

                case 2: // Connexion Client [cite: 73]
                    seConnecter();
                    if (utilisateurConnecte != null) {
                        gererCatalogue();
                    }
                    break;

                case 3: // Créer un compte [cite: 74]
                    creerCompte();
                    break;

                case 4: // Visiteur [cite: 76]
                    menuPrincipal.afficherMessage("Navigation en tant que visiteur (limite de 5 écoutes).");
                    gererCatalogue();
                    break;

                case 5: // Quitter [cite: 77]
                    menuPrincipal.afficherMessage("Fermeture de JAVAZIC. Sauvegarde en cours...");
                    continuer = false;
                    break;

                default:
                    menuPrincipal.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void seConnecter() {
        String[] ids = menuPrincipal.demanderIdentifiants();
        // Vérification via le contrôleur utilisateur
        Abonne resultat = controleUtilisateur.authentifier(ids[0], ids[1], listeAbonnes);

        if (resultat != null) {
            this.utilisateurConnecte = resultat;
            menuPrincipal.afficherMessage("Connexion réussie ! Bonjour " + resultat.getNom());
        } else {
            menuPrincipal.afficherErreur("Identifiants incorrects.");
        }
    }

    private void creerCompte() {
        String[] ids = menuPrincipal.demanderIdentifiants();

        // On demande le nom spécifique pour l'inscription
        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez votre nom complet : ");
        String nom = sc.nextLine();

        if (controleUtilisateur.loginExiste(ids[0], listeAbonnes)) {
            menuPrincipal.afficherErreur("Ce login est déjà utilisé.");
        } else {
            controleUtilisateur.inscrireNouvelAbonne(ids[0], ids[1], nom, listeAbonnes);
            menuPrincipal.afficherMessage("Compte créé avec succès !");
        }
    }

    private void gererCatalogue() {
        boolean retour = false;
        // Si personne n'est connecté, on crée un Utilisateur temporaire (visiteur) [cite: 79, 101]
        Utilisateur actuel = (utilisateurConnecte != null) ? utilisateurConnecte : new Utilisateur();

        while (!retour) {
            int choix = vueCatalog.afficherMenuCatalogue();

            switch (choix) {
                case 1: // Recherche par titre [cite: 92]
                    String recherche = vueCatalog.demanderRecherche();
                    ArrayList<Morceau> resultats = controleurCatalogue.rechercherMorceau(recherche, catalogue);
                    Morceau selection = vueCatalog.selectionnerMorceau(resultats);

                    if (selection != null) {
                        ecouter(selection, actuel);
                    }
                    break;

                case 3: // Tout afficher [cite: 91]
                    Morceau choixTout = vueCatalog.selectionnerMorceau(catalogue.getMorceaux());
                    if (choixTout != null) {
                        ecouter(choixTout, actuel);
                    }
                    break;

                case 4: // Retour
                    retour = true;
                    break;
            }
        }
    }

    private void ecouter(Morceau m, Utilisateur u) {
        // Le contrôleur catalogue gère la simulation (pause) et la vérification des droits [cite: 101, 102]
        boolean succes = controleurCatalogue.lireMorceau(m, u);
        if (!succes) {
            menuPrincipal.afficherErreur("Limite de 5 écoutes atteinte. Veuillez créer un compte !");
        }
    }

    private void creerDonneesTest() {
        // Ajout de quelques données pour pouvoir tester le catalogue immédiatement
        catalogue.ajouterMorceau(new Morceau("Оладушки", 2.28f, "Gorilla Glue"));
        catalogue.ajouterMorceau(new Morceau("Baby Doll", 2.15f, "Ari Abdul"));
        catalogue.ajouterMorceau(new Morceau("Sigma Boy", 1.45f, "Skibidi Toilet"));

        // Un compte de test : login "admin" / mdp "admin"
        listeAbonnes.add(new Abonne("user", "123", "Utilisateur Test"));
    }
}