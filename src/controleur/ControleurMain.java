package controleur;

import modele.Catalogue;
import modele.Abonne;
import modele.Morceau;
import modele.Utilisateur;
import modele.Admin;
import vue.IVueMenuPrincipal;
import vue.IVueCatalog;
import vue.IVueAdmin;
import vue.IVueAbonne;
import java.io.IOException;

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
    private IVueMenuPrincipal menuPrincipal;

    private ControleurCatalogue controleurCatalogue;
    private IVueCatalog vueCatalog;

    private ControleurAdmin controleurAdmin;
    private IVueAdmin vueAdmin;

    private ControleurAbonne controleurAbonne;
    private IVueAbonne vueAbonne;

    public ControleurMain(IVueMenuPrincipal menuPrincipal, IVueCatalog vueCatalog, IVueAdmin vueAdmin, IVueAbonne vueAbonne) {
        this.menuPrincipal = menuPrincipal;
        this.vueCatalog = vueCatalog;
        this.vueAdmin = vueAdmin;
        this.vueAbonne = vueAbonne;

        this.catalogue = new Catalogue();

        ArrayList<Morceau> morceauxDuFichier = utilitaire.GestionnaireFichiers.chargerCatalogue();

        if (morceauxDuFichier != null && !morceauxDuFichier.isEmpty()) {
            for (Morceau m : morceauxDuFichier) {
                this.catalogue.ajouterMorceau(m);
            }
        }

        this.listeAbonnes = utilitaire.GestionnaireFichiers.chargerAbonnes();
        this.listeAdmin = utilitaire.GestionnaireFichiers.chargerAdmins();

        if (listeAbonnes == null) {
            listeAbonnes = new ArrayList<>();
        }
        if (listeAdmin == null) {
            listeAdmin = new ArrayList<>();
        }


        utilitaire.GestionnaireFichiers.chargerPlaylists(listeAbonnes, catalogue.getMorceaux());

        this.abonneConnecte = null;
        this.adminConnecte = null;

        this.controleUtilisateur = new ControleUtilisateur();
        this.controleurAdmin = new ControleurAdmin(vueAdmin);
        this.controleurCatalogue = new ControleurCatalogue(vueCatalog, menuPrincipal);
        this.controleurAbonne = new ControleurAbonne(
                vueAbonne,
                controleurCatalogue,
                listeAbonnes
        );
    }

    private void controleur() {
    }

    public void lancer()  {
        boolean continuer = true;

        while (continuer) {
            try {
            int choix = menuPrincipal.afficherMenuInitial();

            switch (choix) {
                case 1:
                    //choix de se connecter
                    seConnecterAdmin();
                    if (adminConnecte != null) {
                        controleurAdmin.menuAdmin(catalogue, listeAbonnes);
                        deconnexion();
                    }
                    break;

                case 2:
                    //choix de se connecter en tant qu'abboné
                    seConnecterAbonne();
                    if (abonneConnecte != null) {

                        controleurAbonne.menuAbonne(abonneConnecte, catalogue);
                        deconnexion();
                    }
                    break;

                case 3:
                    //option se creer un compte
                    creerCompte();
                    break;

                case 4:
                    //aller en tant que visiteur
                    gererCatalogue();
                    break;

                case 5:
                    //quitter
                    try {
                        utilitaire.GestionnaireFichiers.sauvegarderTout(
                                listeAbonnes,
                                listeAdmin,
                                catalogue.getMorceaux()
                        );
                        menuPrincipal.afficherMessage("Données sauvegardées avec succès.");
                        continuer = false;
                    } catch (java.io.IOException e) {
                        menuPrincipal.afficherErreur("Erreur lors de la sauvegarde finale : " + e.getMessage());
                        continuer = false;
                    }
                    break;

                default:
                    menuPrincipal.afficherErreur("Choix invalide.");
                    break;
            }
        } catch (NumberFormatException e) {
            menuPrincipal.afficherErreur("Erreur : Veuillez saisir un chiffre entre 1 et 5.");
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
            menuPrincipal.afficherErreur("Identifiants incorrects");
        }
    }

    private void creerCompte() {
        String[] ids = menuPrincipal.demanderIdentifiants();
        String nom = vueAdmin.demanderTexte("Nom complet : ");

        if (controleUtilisateur.loginExiste(ids[0], listeAbonnes)) {
            menuPrincipal.afficherErreur("Login déjà utilisé.");
        } else {
            controleUtilisateur.inscrireNouvelAbonne(ids[0], ids[1], nom, listeAbonnes);
            try {
                utilitaire.GestionnaireFichiers.sauvegarderAbonnes(listeAbonnes);
                menuPrincipal.afficherMessage("Compte créé et enregistré !");
            } catch (java.io.IOException e) {
                menuPrincipal.afficherErreur("Compte créé localement mais erreur d'écriture disque : " + e.getMessage());
            }
        }
    }


    private void gererCatalogue() {
        Utilisateur utilisateurActuel;

        if (abonneConnecte != null) {
            utilisateurActuel = abonneConnecte;
        } else {
            // creation du visiteur qui a 5 ecoute
            utilisateurActuel = new Utilisateur();
        }
        controleurCatalogue.gererCatalogue(catalogue, utilisateurActuel);
    }

    private void deconnexion() {
        abonneConnecte = null;
        adminConnecte = null;
    }

}