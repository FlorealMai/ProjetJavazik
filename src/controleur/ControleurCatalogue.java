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
            System.out.println("\n[LECTURE] " + m.getTitre() + " - " + m.getArtiste());

            //calcul de durée
            float dureeBrute = m.getDuree();
            int minutes = (int) dureeBrute;
            // recuperer juste apres la virgule et la multip par 100
            int secondes = Math.round((dureeBrute - minutes) * 100);
            int tempsRestantEnSecondes = (minutes * 60) + secondes;

            int tempsEcoule = 0;
            boolean enPause = false;
            boolean arrete = false;
            java.util.Scanner sc = new java.util.Scanner(System.in);

            System.out.println("Durée totale : " + minutes + "m " + secondes + "s");
            System.out.println("Commandes : [p] Pause/Reprise | [q] Arrêter");

            // boucle de ecoute
            while (tempsEcoule < tempsRestantEnSecondes && !arrete) {
                try {
                    if (!enPause) {
                        // on simule le temps
                        Thread.sleep(1000);
                        tempsEcoule++;

                        // barre progretion
                        System.out.print("\rProgression : " + tempsEcoule + "s / " + tempsRestantEnSecondes + "s ");
                    } else {
                        // att pour pause
                        Thread.sleep(200);
                    }

                    // dtection clavier
                    if (System.in.available() > 0) {
                        String commande = sc.nextLine().toLowerCase();
                        if (commande.equals("p")) {
                            enPause = !enPause;
                            System.out.println(enPause ? "\n[PAUSE]" : "\n[REPRISE]");
                        } else if (commande.equals("q")) {
                            arrete = true;
                        }
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }


            if (arrete) {
                System.out.println("\n[LECTURE INTERROMPUE]");
            } else {
                System.out.println("\n[FIN DE LA LECTURE]");
            }

            // updtade stat et historique
            u.ecouter();
            m.ecouter();

            if (u instanceof Abonne) {
                Abonne abonne = (Abonne) u;
                abonne.ajouterHistorique(m);
                utilitaire.GestionnaireFichiers.enregistrerEcoute(abonne.getLogin(), m.getTitre());
            }

            return true;
        } else {
            System.out.println("\nErreur : Limite d'écoutes atteinte pour votre profil.");
            return false;
        }
    }

}



