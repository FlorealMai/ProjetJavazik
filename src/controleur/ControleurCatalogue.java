package controleur;

import modele.*;
import vue.IVueCatalog;
import vue.IVueMenuPrincipal;

import java.util.ArrayList;

public class ControleurCatalogue {

    private IVueCatalog vueCatalog;
    private IVueMenuPrincipal menuPrincipal;

    public ControleurCatalogue(IVueCatalog vueCatalog, IVueMenuPrincipal menuPrincipal) {
        this.vueCatalog = vueCatalog;
        this.menuPrincipal = menuPrincipal;
    }

    public void gererCatalogue(Catalogue catalogue, Utilisateur utilisateurActuel) {
        boolean retour = false;

        while (!retour) {
            int choix = vueCatalog.afficherMenuCatalogue();

            switch (choix) {

                case 1: // Recherche par titre
                    String recherche = vueCatalog.demanderRecherche();
                    ArrayList<Morceau> resultats = rechercherMorceau(recherche, catalogue);

                    boolean resterDansRecherche = true;
                    while (resterDansRecherche) {
                        Morceau selection = vueCatalog.selectionnerMorceau(resultats);
                        if (selection != null) {
                            ecouter(selection, utilisateurActuel);
                        } else {
                            resterDansRecherche = false;
                        }
                    }
                    break;

                case 2: // Recherche par artiste
                    String nomArtiste = vueCatalog.demanderRecherche();
                    Artiste artiste = rechercherArtiste(nomArtiste, catalogue);

                    if (artiste != null) {
                        ArrayList<Morceau> morceaux = artiste.getMorceaux();

                        boolean resterDansArtiste = true;
                        while (resterDansArtiste) {
                            Morceau selectionArtiste = vueCatalog.selectionnerMorceau(morceaux);
                            if (selectionArtiste != null) {
                                ecouter(selectionArtiste, utilisateurActuel);
                            } else {
                                resterDansArtiste = false;
                            }
                        }
                    } else {
                        menuPrincipal.afficherMessage("Artiste introuvable.");
                    }
                    break;

                case 3: // tous afficher
                    ArrayList<Morceau> tous = catalogue.getMorceaux();

                    boolean resterDansTous = true;
                    while (resterDansTous) {
                        Morceau choixTous = vueCatalog.selectionnerMorceau(tous);

                        if (choixTous != null) {
                            ecouter(choixTous, utilisateurActuel);
                        } else {
                            resterDansTous = false;
                        }
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
        Artiste resultat = new Artiste(nom);
        boolean trouve = false;

        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getArtiste().equalsIgnoreCase(nom)) {
                resultat.ajouterMorceau(m);
                trouve = true;
            }
        }
        return trouve ? resultat : null;
    }

    public boolean ecouter(Morceau m, Utilisateur u) {
        if (u.peutEcouter()) {
            float dureeBrute = m.getDuree();
            int minutes = (int) dureeBrute;
            int secondes = Math.round((dureeBrute - minutes) * 100);
            int tempsRestantEnSecondes = (minutes * 60) + secondes;

            // On demande à la vue d'afficher le lecteur (Console ou Graphique)
            vueCatalog.afficherEcoute(m, tempsRestantEnSecondes);

            int tempsEcoule = 0;

            // La boucle tourne tant qu'on a pas fini et que l'utilisateur n'a pas cliqué sur Arrêter
            while (tempsEcoule < tempsRestantEnSecondes && !vueCatalog.isArrete()) {
                try {
                    if (!vueCatalog.isEnPause()) {
                        Thread.sleep(1000);
                        tempsEcoule++;
                        vueCatalog.majProgression(tempsEcoule, tempsRestantEnSecondes);
                    } else {
                        Thread.sleep(200); // En pause, on attend juste
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // On cache le lecteur
            vueCatalog.arreterEcoute();

            u.ecouter();
            m.ecouter();

            if (u instanceof Abonne) {
                Abonne abonne = (Abonne) u;
                abonne.ajouterHistorique(m);
                utilitaire.GestionnaireFichiers.enregistrerEcoute(abonne.getLogin(), m.getTitre());
            }

            return true;
        } else {
            menuPrincipal.afficherErreur("Limite d'écoutes atteinte pour votre profil.");
            return false;
        }
    }

}



