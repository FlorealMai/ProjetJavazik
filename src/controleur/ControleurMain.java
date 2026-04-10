package controleur;

import modele.Catalogue;
import modele.Abonne;
import modele.Morceau;
import modele.Utilisateur;
import modele.Admin;
import vue.IVueMenuPrincipal;
import vue.IVueCatalog;
import vue.IVueAdmin;

import java.util.ArrayList;

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
    private ControleurAdmin controleurAdmin;
    private ControleurCatalogue controleurCatalogue;
    private IVueMenuPrincipal menuPrincipal;
    private IVueCatalog vueCatalog;
    private IVueAdmin vueAdmin;

    public ControleurMain(IVueMenuPrincipal menuPrincipal, IVueCatalog vueCatalog, IVueAdmin vueAdmin) {
        this.menuPrincipal = menuPrincipal;
        this.vueCatalog = vueCatalog;
        this.vueAdmin = vueAdmin;

        this.catalogue = new Catalogue();

        // Chargement catalogue
        ArrayList<Morceau> morceauxDuFichier = utilitaire.GestionnaireFichiers.chargerCatalogue();

        if (morceauxDuFichier != null && !morceauxDuFichier.isEmpty()) {
            for (Morceau m : morceauxDuFichier) {
                this.catalogue.ajouterMorceau(m);
            }
            System.out.println("[INFO] Catalogue chargé (" + morceauxDuFichier.size() + " morceaux).");
        }

        // Chargement utilisateurs
        this.listeAbonnes = utilitaire.GestionnaireFichiers.chargerAbonnes();
        this.listeAdmin = utilitaire.GestionnaireFichiers.chargerAdmins();

        if (listeAbonnes == null) {
            listeAbonnes = new ArrayList<>();
        }
        if (listeAdmin == null) {
            listeAdmin = new ArrayList<>();
        }

        // Données test si vide
        if (catalogue.getMorceaux().isEmpty()) {
            creerDonneesTest();
            System.out.println("[INFO] Données de test utilisées.");
        }

        this.abonneConnecte = null;
        this.adminConnecte = null;

        // Contrôleurs
        this.controleUtilisateur = new ControleUtilisateur();
        this.controleurAdmin = new ControleurAdmin(vueAdmin);
        this.controleurCatalogue = new ControleurCatalogue(vueCatalog, menuPrincipal);
    }

    public void lancer() {
        boolean continuer = true;

        while (continuer) {
            int choix = menuPrincipal.afficherMenuInitial();

            switch (choix) {
                case 1:
                    seConnecterAdmin();
                    if (adminConnecte != null) {
                        menuPrincipal.afficherMessage("Connexion admin réussie !");
                        controleurAdmin.menuAdmin(catalogue, listeAbonnes);
                        deconnexion();
                    }
                    break;

                case 2:
                    seConnecterAbonne();
                    if (abonneConnecte != null) {
                        menuPrincipal.afficherMessage("Connexion client réussie !");
                        gererCatalogue();
                        deconnexion();
                    }
                    break;

                case 3:
                    creerCompte();
                    break;

                case 4:
                    menuPrincipal.afficherMessage("Mode visiteur (limite de 5 écoutes).");
                    gererCatalogue();
                    break;

                case 5:
                    menuPrincipal.afficherMessage("Sauvegarde...");
                    utilitaire.GestionnaireFichiers.sauvegarderTout(
                            listeAbonnes,
                            listeAdmin,
                            catalogue.getMorceaux()
                    );
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
            menuPrincipal.afficherErreur("Identifiants incorrects.");
        }
    }

    private void seConnecterAdmin() {
        String[] ids = menuPrincipal.demanderIdentifiants();

        Admin resultat = controleurAdmin.authentifier(ids[0], ids[1], listeAdmin);

        if (resultat != null) {
            this.adminConnecte = resultat;
            this.abonneConnecte = null;
            menuPrincipal.afficherMessage("Bonjour admin " + resultat.getNom());
        } else {
            menuPrincipal.afficherErreur("Identifiants incorrects.");
        }
    }

    private void creerCompte() {
        String[] ids = menuPrincipal.demanderIdentifiants();
        String nom = vueAdmin.demanderTexte("Nom complet : ");

        if (controleUtilisateur.loginExiste(ids[0], listeAbonnes)) {
            menuPrincipal.afficherErreur("Login déjà utilisé.");
        } else {
            controleUtilisateur.inscrireNouvelAbonne(ids[0], ids[1], nom, listeAbonnes);
            menuPrincipal.afficherMessage("Compte créé !");
        }
    }

    private void gererCatalogue() {
        Utilisateur utilisateurActuel = (abonneConnecte != null)
                ? abonneConnecte
                : new Utilisateur();

        controleurCatalogue.gererCatalogue(catalogue, utilisateurActuel);
    }

    private void deconnexion() {
        abonneConnecte = null;
        adminConnecte = null;
    }

    private void creerDonneesTest() {
        ajouterSiInexistant("Baby Doll", 2.15f, "Ari Abdul");
        ajouterSiInexistant("Sigma Boy", 1.45f, "Skibidi Toilet");

        if (listeAbonnes.isEmpty()) {
            listeAbonnes.add(new Abonne("user", "123", "Utilisateur Test"));
        }

        if (listeAdmin.isEmpty()) {
            listeAdmin.add(new Admin("admin", "345", "Admin Test"));
        }
    }

    private void ajouterSiInexistant(String titre, float duree, String artiste) {
        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().equalsIgnoreCase(titre)) {
                return;
            }
        }
        catalogue.ajouterMorceau(new Morceau(titre, duree, artiste));
    }
}