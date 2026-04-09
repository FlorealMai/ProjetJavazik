package controleur;

import modele.*;
import vue.VueCatalog;
import vue.VueMenuPrincipal;

import java.util.ArrayList;

public class ControleurCatalogue {

    private VueCatalog vueCatalog;
    private VueMenuPrincipal menuPrincipal;

    public ControleurCatalogue(VueCatalog vueCatalog, VueMenuPrincipal menuPrincipal) {
        this.vueCatalog = vueCatalog;
        this.menuPrincipal = menuPrincipal;
    }

    public void gererCatalogue(Catalogue catalogue, Utilisateur utilisateurActuel) {
        boolean retour = false;

        while (!retour) {
            int choix = vueCatalog.afficherMenuCatalogue();

            switch (choix) {

                case 1: // Recherche titre
                    String recherche = vueCatalog.demanderRecherche();
                    ArrayList<Morceau> resultats = rechercherMorceau(recherche, catalogue);

                    Morceau selection = vueCatalog.selectionnerMorceau(resultats);

                    if (selection != null) {
                        ecouter(selection, utilisateurActuel);
                    }
                    break;

                case 2: // Recherche artiste
                    String nomArtiste = vueCatalog.demanderRecherche();
                    Artiste artiste = rechercherArtiste(nomArtiste, catalogue);

                    if (artiste != null) {
                        ArrayList<Morceau> morceaux = artiste.getMorceaux();
                        Morceau choixArtiste = vueCatalog.selectionnerMorceau(morceaux);

                        if (choixArtiste != null) {
                            ecouter(choixArtiste, utilisateurActuel);
                        }
                    } else {
                        menuPrincipal.afficherErreur("Artiste introuvable.");
                    }
                    break;

                case 3: // Tout afficher
                    ArrayList<Morceau> tous = catalogue.getMorceaux();
                    Morceau choixTout = vueCatalog.selectionnerMorceau(tous);

                    if (choixTout != null) {
                        ecouter(choixTout, utilisateurActuel);
                    }
                    break;

                case 4:
                    retour = true;
                    break;

                default:
                    menuPrincipal.afficherErreur("Choix invalide.");
            }
        }
    }

    public ArrayList<Morceau> rechercherMorceau(String titre, Catalogue catalogue) {
        ArrayList<Morceau> resultats = new ArrayList<>();

        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().toLowerCase().contains(titre.toLowerCase())) {
                resultats.add(m);
            }
        }

        return resultats;
    }

    public Artiste rechercherArtiste(String nom, Catalogue catalogue) {
        for (Artiste a : catalogue.getArtistes()) {
            if (a.getNom().equalsIgnoreCase(nom)) {
                return a;
            }
        }
        return null;
    }

    private void ecouter(Morceau m, Utilisateur u) {
        if (u.peutEcouter()) {
            System.out.println("\n[LECTURE] " + m.getTitre() + " - " + m.getArtiste());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            u.ecouter();
            m.ecouter();

        } else {
            menuPrincipal.afficherErreur("Limite de 5 écoutes atteinte.");
        }
    }
}
