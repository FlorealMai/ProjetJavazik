package controleur;

import modele.Catalogue;
import modele.Abonne;
import modele.Morceau;
import modele.Utilisateur;
import modele.Admin;
import vue.VueMenuPrincipal;
import vue.VueCatalog;

import java.util.ArrayList;
import java.util.Scanner;
import utilitaire.GestionnaireFichiers;

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
    private VueMenuPrincipal menuPrincipal;
    private VueCatalog vueCatalog;


    public ControleurMain() {
// 1. On initialise d'abord l'objet catalogue (vide au départ)
        this.catalogue = new Catalogue();

        // 2. APPEL DE CHARGEMENT : On récupère les morceaux du fichier .txt
        // (C'est ici que tu appelles ta méthode statique)
        ArrayList<Morceau> morceauxDuFichier = utilitaire.GestionnaireFichiers.chargerCatalogue();

        // 3. STOCKAGE : On remplit le catalogue avec ce qu'on vient de lire
        if (morceauxDuFichier != null && !morceauxDuFichier.isEmpty()) {
            for (Morceau m : morceauxDuFichier) {
                this.catalogue.ajouterMorceau(m);
            }
            System.out.println("[INFO] Catalogue chargé depuis le fichier (" + morceauxDuFichier.size() + " morceaux).");
        } else {
            // Si le fichier est vide ou n'existe pas, on met tes morceaux par défaut
            creerDonneesTest();
            System.out.println("[INFO] Fichier vide, utilisation des données de test.");
        }

        // 4. On fait la même chose pour les abonnés et les admins
        this.listeAbonnes = utilitaire.GestionnaireFichiers.chargerAbonnes();
        this.listeAdmin = utilitaire.GestionnaireFichiers.chargerAdmins();

        // Reste de tes initialisations (vues, contrôleurs...)
        this.abonneConnecte = null;
        this.adminConnecte = null;
        this.controleUtilisateur = new ControleUtilisateur();
        this.controleAdmin = new ControleAdmin();
        this.controleurCatalogue = new ControleurCatalogue();
        this.menuPrincipal = new VueMenuPrincipal();
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
                    utilitaire.GestionnaireFichiers.sauvegarderTout(listeAbonnes, listeAdmin, catalogue.getMorceaux());
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
                    // On donne la liste des morceaux du catalogue (chargée au démarrage) à la vue
                    ArrayList<Morceau> tousLesMorceaux = catalogue.getMorceaux();

                    // La vue va parcourir cette liste et l'afficher
                    Morceau choixTout = vueCatalog.selectionnerMorceau(tousLesMorceaux);

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
        ajouterSiInexistant("Baby Doll", 2.15f, "Ari Abdul");
        ajouterSiInexistant("Sigma Boy", 1.45f, "Skibidi Toilet");


        listeAbonnes.add(new Abonne("user", "123", "Utilisateur Test"));
        listeAdmin.add(new Admin("admin", "345", "Admin Test"));
    }

    // Petite méthode utilitaire pour éviter les doublons
    private void ajouterSiInexistant(String titre, float duree, String artiste) {
        boolean existe = false;
        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().equalsIgnoreCase(titre)) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            catalogue.ajouterMorceau(new Morceau(titre, duree, artiste));
        }
    }
}