package controleur;

import modele.Admin;
import modele.Abonne;
import modele.Catalogue;
import modele.Morceau;
import vue.VueAdmin;
import utilitaire.GestionnaireFichiers;

import java.util.ArrayList;

public class ControleurAdmin {

    private VueAdmin vueAdmin;

    public ControleurAdmin(VueAdmin vueAdmin) {
        this.vueAdmin = vueAdmin;
    }

    public boolean loginExiste(String login, ArrayList<Admin> liste) {
        for (Admin a : liste) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    public void inscrireNouvelAdmin(String login, String mdp, String nom, ArrayList<Admin> liste) {
        Admin nouvelAdmin = new Admin(login, mdp, nom);
        liste.add(nouvelAdmin);
    }

    public Admin authentifier(String login, String mdp, ArrayList<Admin> liste) {
        for (Admin a : liste) {
            if (a.getLogin().equals(login) && a.getMotDePasse().equals(mdp)) {
                return a;
            }
        }
        return null;
    }

    public void menuAdmin(Catalogue catalogue, ArrayList<Abonne> abonnes) {
        boolean quitter = false;

        while (!quitter) {
            int choix = vueAdmin.afficherMenuAdmin();

            switch (choix) {
                case 1:
                    gererCatalogue(catalogue);
                    break;

                case 2:
                    gererAbonnes(abonnes);
                    break;

                case 3:
                    afficherStats(catalogue, abonnes);
                    break;

                case 4:
                    quitter = true;
                    break;

                default:
                    vueAdmin.afficherMessage("Choix invalide.");
                    break;
            }
        }
    }

    private void gererCatalogue(Catalogue catalogue) {
        boolean retour = false;

        while (!retour) {
            int choix = vueAdmin.menuCatalogueAdmin();

            switch (choix) {
                case 1:
                    ajouterMorceau(catalogue);
                    break;

                case 2:
                    supprimerMorceau(catalogue);
                    break;

                case 3:
                    retour = true;
                    break;

                default:
                    vueAdmin.afficherMessage("Choix invalide.");
                    break;
            }
        }
    }

    private void ajouterMorceau(Catalogue catalogue) {
        String titre = vueAdmin.demanderTexte("Titre : ");
        String artiste = vueAdmin.demanderTexte("Artiste : ");
        float duree = vueAdmin.demanderFloat("Durée : ");

        Morceau nouveau = new Morceau(titre, duree, artiste);
        catalogue.ajouterMorceau(nouveau);

        utilitaire.GestionnaireFichiers.sauvegarderCatalogue(catalogue.getMorceaux());

        vueAdmin.afficherMessage("Morceau ajouté avec succès !");
    }

    private void supprimerMorceau(Catalogue catalogue) {
        String titreSuppr = vueAdmin.demanderTexte("Titre à supprimer : ");

        Morceau aSuppr = null;
        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().equalsIgnoreCase(titreSuppr)) {
                aSuppr = m;
                break;
            }
        }

        if (aSuppr != null) {
            catalogue.getMorceaux().remove(aSuppr);
            utilitaire.GestionnaireFichiers.sauvegarderCatalogue(catalogue.getMorceaux());
            vueAdmin.afficherMessage("Morceau supprimé avec succès !");
        } else {
            vueAdmin.afficherMessage("Morceau introuvable.");
        }
    }

    private void gererAbonnes(ArrayList<Abonne> abonnes) {
        boolean retour = false;

        while (!retour) {
            int choix = vueAdmin.menuAbonnesAdmin();

            switch (choix) {
                case 1:
                    String login = vueAdmin.demanderTexte("Login à supprimer : ");

                    Abonne aSuppr = null;
                    for (Abonne a : abonnes) {
                        if (a.getLogin().equalsIgnoreCase(login)) {
                            aSuppr = a;
                            break;
                        }
                    }

                    if (aSuppr != null) {
                        abonnes.remove(aSuppr);
                        vueAdmin.afficherMessage("Compte supprimé !");
                    } else {
                        vueAdmin.afficherMessage("Utilisateur introuvable.");
                    }
                    break;

                case 2:
                    retour = true;
                    break;

                default:
                    vueAdmin.afficherMessage("Choix invalide.");
                    break;
            }
        }
    }

    private void afficherStats(Catalogue catalogue, ArrayList<Abonne> abonnes) {
        int nbMorceaux = catalogue.getMorceaux().size();
        int nbUsers = abonnes.size();

        int totalEcoutes = 0;
        for (Morceau m : catalogue.getMorceaux()) {
            totalEcoutes += m.getNbEcoutes();
        }

        vueAdmin.afficherMessage("\n--- STATS ---");
        vueAdmin.afficherMessage("Utilisateurs : " + nbUsers);
        vueAdmin.afficherMessage("Morceaux : " + nbMorceaux);
        vueAdmin.afficherMessage("Écoutes totales : " + totalEcoutes);
    }
}