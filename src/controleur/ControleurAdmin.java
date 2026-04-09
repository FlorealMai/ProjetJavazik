package controleur;

import modele.*;
import vue.VueAdmin;

import java.util.ArrayList;

public class ControleurAdmin {

    private VueAdmin vueAdmin;

    public ControleurAdmin(VueAdmin vueAdmin) {
        this.vueAdmin = vueAdmin;
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
                    vueAdmin.afficherMessage("Choix invalide");
            }
        }
    }

    private void gererCatalogue(Catalogue catalogue) {
        boolean retour = false;

        while (!retour) {
            int choix = vueAdmin.menuCatalogueAdmin();

            switch (choix) {

                case 1:
                    String titre = vueAdmin.demanderTexte("Titre : ");
                    String artiste = vueAdmin.demanderTexte("Artiste : ");
                    float duree = vueAdmin.demanderFloat("Durée : ");

                    catalogue.ajouterMorceau(new Morceau(titre, duree, artiste));
                    vueAdmin.afficherMessage("Morceau ajouté !");
                    break;

                case 2:
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
                        vueAdmin.afficherMessage("Supprimé !");
                    } else {
                        vueAdmin.afficherMessage("Introuvable");
                    }
                    break;

                case 3:
                    retour = true;
                    break;
            }
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
                        vueAdmin.afficherMessage("Utilisateur introuvable");
                    }
                    break;

                case 2:
                    retour = true;
                    break;
            }
        }
    }
    public Admin authentifier(String login, String mdp, ArrayList<Admin> liste) {
        for (Admin a : liste) {
            if (a.getLogin().equals(login) && a.getMotDePasse().equals(mdp)) {
                return a;
            }
        }
        return null;
    }
    private void afficherStats(Catalogue catalogue, ArrayList<Abonne> abonnes) {
        int nbMorceaux = catalogue.getMorceaux().size();
        int nbUsers = abonnes.size();

        int totalEcoutes = 0;
        for (Morceau m : catalogue.getMorceaux()) {
            totalEcoutes += m.getNbEcoutes(); // suppose que tu as ça
        }

        vueAdmin.afficherMessage("\n--- STATS ---");
        vueAdmin.afficherMessage("Utilisateurs : " + nbUsers);
        vueAdmin.afficherMessage("Morceaux : " + nbMorceaux);
        vueAdmin.afficherMessage("Écoutes totales : " + totalEcoutes);
    }
}