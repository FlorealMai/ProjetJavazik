package controleur;

import modele.Catalogue;
import modele.Abonne;
import modele.Morceau;
import modele.Utilisateur;
import modele.Admin;
import vue.MenuPrincipalView;
import vue.VueCatalog;

import java.util.ArrayList;
import java.util.Scanner;

public class ControleurMain {
    // Données du programme
    private Catalogue catalogue;
    private ArrayList<Abonne> listeAbonnes;
    private ArrayList<Admin> listeAdmin;

    // Session actuelle
    private Abonne abonneConnecte;
    private Admin adminConnecte;

    // Composants MVC
    private ControleUtilisateur controleUtilisateur;
    private ControleAdmin controleAdmin;
    private ControleurCatalogue controleurCatalogue;
    private MenuPrincipalView menuPrincipal;
    private VueCatalog vueCatalog;

    public ControleurMain() {
        // Initialisation du modèle
        this.catalogue = new Catalogue();
        this.listeAbonnes = new ArrayList<>();
        this.listeAdmin = new ArrayList<>();

        this.abonneConnecte = null;
        this.adminConnecte = null;

        // Initialisation des contrôleurs et vues
        this.controleUtilisateur = new ControleUtilisateur();
        this.controleAdmin = new ControleAdmin();
        this.controleurCatalogue = new ControleurCatalogue();
        this.menuPrincipal = new MenuPrincipalView();
        this.vueCatalog = new VueCatalog();
    }

    public void lancer() {
        creerDonneesTest();
        boolean continuer = true;

        while (continuer) {
            int choix = menuPrincipal.afficherMenuInitial();

            switch (choix) {
                case 1: // Connexion administrateur
                    seConnecterAdmin();
                    if (adminConnecte != null) {
                        menuPrincipal.afficherMessage("Connexion admin réussie !");
                        gererCatalogue();
                        deconnexion();
                    }
                    break;

                case 2: // Connexion client
                    seConnecterAbonne();
                    if (abonneConnecte != null) {
                        menuPrincipal.afficherMessage("Connexion client réussie !");
                        gererCatalogue();
                        deconnexion();
                    }
                    break;

                case 3: // Créer un compte client
                    creerCompte();
                    break;

                case 4: // Continuer en visiteur
                    menuPrincipal.afficherMessage("Navigation en tant que visiteur (limite de 5 écoutes).");
                    gererCatalogue();
                    break;

                case 5: // Quitter
                    menuPrincipal.afficherMessage("Fermeture de JAVAZIC. Sauvegarde en cours...");
                    continuer = false;
                    break;

                default:
                    menuPrincipal.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void seConnecterAbonne() {
        String[] ids = menuPrincipal.demanderIdentifiants();

        Abonne resultat = controleUtilisateur.authentifier(ids[0], ids[1], listeAbonnes);

        if (resultat != null) {
            this.abonneConnecte = resultat;
            this.adminConnecte = null;
            menuPrincipal.afficherMessage("Bonjour " + resultat.getNom());
        } else {
            menuPrincipal.afficherErreur("Identifiants client incorrects.");
        }
    }

    private void seConnecterAdmin() {
        String[] ids = menuPrincipal.demanderIdentifiants();

        Admin resultat = controleAdmin.authentifier(ids[0], ids[1], listeAdmin);

        if (resultat != null) {
            this.adminConnecte = resultat;
            this.abonneConnecte = null;
            menuPrincipal.afficherMessage("Bonjour admin " + resultat.getNom());
        } else {
            menuPrincipal.afficherErreur("Identifiants admin incorrects.");
        }
    }

    private void creerCompte() {
        String[] ids = menuPrincipal.demanderIdentifiants();

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

        // Si aucun abonné n'est connecté, on navigue en simple utilisateur/visiteur
        Utilisateur utilisateurActuel = (abonneConnecte != null) ? abonneConnecte : new Utilisateur();

        while (!retour) {
            int choix = vueCatalog.afficherMenuCatalogue();

            switch (choix) {
                case 1: // Recherche par titre
                    String recherche = vueCatalog.demanderRecherche();
                    ArrayList<Morceau> resultats = controleurCatalogue.rechercherMorceau(recherche, catalogue);
                    Morceau selection = vueCatalog.selectionnerMorceau(resultats);

                    if (selection != null) {
                        ecouter(selection, utilisateurActuel);
                    }
                    break;

                case 2:
                    // à compléter plus tard : recherche album / artiste / groupe
                    menuPrincipal.afficherMessage("Fonction pas encore implémentée.");
                    break;

                case 3: // Tout afficher
                    Morceau choixTout = vueCatalog.selectionnerMorceau(catalogue.getMorceaux());
                    if (choixTout != null) {
                        ecouter(choixTout, utilisateurActuel);
                    }
                    break;

                case 4: // Retour
                    retour = true;
                    break;

                default:
                    menuPrincipal.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void ecouter(Morceau m, Utilisateur u) {
        boolean succes = controleurCatalogue.lireMorceau(m, u);

        if (!succes) {
            menuPrincipal.afficherErreur("Limite de 5 écoutes atteinte. Veuillez créer un compte.");
        }
    }

    private void deconnexion() {
        this.abonneConnecte = null;
        this.adminConnecte = null;
    }

    private void creerDonneesTest() {
        catalogue.ajouterMorceau(new Morceau("Оладушки", 2.28f, "Gorilla Glue"));
        catalogue.ajouterMorceau(new Morceau("Baby Doll", 2.15f, "Ari Abdul"));
        catalogue.ajouterMorceau(new Morceau("Sigma Boy", 1.45f, "Skibidi Toilet"));

        listeAbonnes.add(new Abonne("user", "123", "Utilisateur Test"));
        listeAdmin.add(new Admin("admin", "345", "Admin Test"));
    }
}