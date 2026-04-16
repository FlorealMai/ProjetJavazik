package controleur;

import modele.Abonne;
import modele.Catalogue;
import modele.Morceau;
import modele.Playlist;
import vue.IVueAbonne;
import utilitaire.GestionnaireFichiers;
import java.util.ArrayList;

public class ControleurAbonne {

    private IVueAbonne vueAbonne;
    private ControleurCatalogue controleurCatalogue;
    private ArrayList<Abonne> listeAbonnes;

    public ControleurAbonne(IVueAbonne vueAbonne,
                            ControleurCatalogue controleurCatalogue,
                            ArrayList<Abonne> listeAbonnes) {

        this.vueAbonne = vueAbonne;
        this.controleurCatalogue = controleurCatalogue;
        this.listeAbonnes = listeAbonnes;
    }

    public void menuAbonne(Abonne abonne, Catalogue catalogue) {
        boolean quitter = false;

        while (!quitter) {
            int choix = vueAbonne.afficherMenuAbonne();

            switch (choix) {
                case 1:
                    controleurCatalogue.gererCatalogue(catalogue, abonne);
                    break;

                case 2:
                    afficherHistorique(abonne);
                    break;

                case 3:
                    afficherInfos(abonne);
                    break;

                case 4:
                    creerPlaylist(abonne);
                    break;

                case 5:
                    gererEcoutePlaylists(abonne);
                    break;

                case 6:
                    ajouterMorceauAPlaylist(abonne, catalogue);
                    break;
                case 7:
                    afficherRecommandations(abonne);
                    break;
                case 8:
                    quitter = true;
                    break;

                default:
                    vueAbonne.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void afficherHistorique(Abonne abonne) {
        ArrayList<String> historiqueBrut = utilitaire.GestionnaireFichiers.chargerHistorique(abonne.getLogin());

        if (historiqueBrut.isEmpty()) {
            vueAbonne.afficherHistorique("Aucune écoute enregistrée.");
        } else {
            java.util.Map<String, Integer> compteur = new java.util.HashMap<>();

            for (String titre : historiqueBrut) {
                compteur.put(titre, compteur.getOrDefault(titre, 0) + 1);
            }

            StringBuilder sb = new StringBuilder();
            for (java.util.Map.Entry<String, Integer> entree : compteur.entrySet()) {
                sb.append("- ").append(entree.getKey())
                        .append(" - ").append(entree.getValue())
                        .append(" écoute(s)\n");
            }

            vueAbonne.afficherHistorique(sb.toString());
        }

        if (vueAbonne instanceof vue.VueAbonne) {
            boolean rester = true;
            while (rester) {
                int choix = vueAbonne.demanderChoix("\nTapez 0 pour revenir au menu précédent : ");
                if (choix == 0) rester = false;
            }
        }
    }

    private void afficherRecommandations(Abonne abonne) {
        ArrayList<String> historiqueBrut = utilitaire.GestionnaireFichiers.chargerHistorique(abonne.getLogin());

        if (historiqueBrut == null || historiqueBrut.isEmpty()) {
            vueAbonne.afficherMessage("Écoutez quelques morceaux pour recevoir des recommandations !");
            return;
        }

        // on compte le nb d écoute
        java.util.Map<String, Integer> compteur = new java.util.HashMap<>();
        for (String titre : historiqueBrut) {
            compteur.put(titre, compteur.getOrDefault(titre, 0) + 1);
        }

        // tri par ecoute
        java.util.List<java.util.Map.Entry<String, Integer>> listeTriee = new java.util.ArrayList<>(compteur.entrySet());
        listeTriee.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        StringBuilder sb = new StringBuilder();

        int limite = Math.min(listeTriee.size(), 5);
        for (int i = 0; i < limite; i++) {
            java.util.Map.Entry<String, Integer> entree = listeTriee.get(i);
            sb.append(i + 1).append(". ")
                    .append(entree.getKey())
                    .append("\n");
        }
        vueAbonne.afficherRecommandations(sb.toString());

        if (vueAbonne instanceof vue.VueAbonne) {
            boolean rester = true;
            while (rester) {
                int retour = vueAbonne.demanderChoix("\nTapez 0 pour revenir au menu précédent : ");
                if (retour == 0) rester = false;
            }
        }
    }

    private void afficherInfos(Abonne abonne) {
        vueAbonne.afficherInfosAbonne(abonne.getLogin(), abonne.getNom());
    }

    private void creerPlaylist(Abonne abonne) {
        String nomPlaylist = vueAbonne.demanderTexte("Nom de la playlist : ");

        if (nomPlaylist == null || nomPlaylist.trim().isEmpty()) {
            vueAbonne.afficherErreur("Nom de playlist invalide.");
            return;
        }

        Playlist nouvellePlaylist = new Playlist(nomPlaylist.trim());
        abonne.ajouterPlaylist(nouvellePlaylist);
        utilitaire.GestionnaireFichiers.sauvegarderPlaylists(listeAbonnes);
        vueAbonne.afficherMessage("Playlist créée avec succès.");

    }

    private void afficherPlaylists(Abonne abonne) {
        ArrayList<Playlist> playlists = abonne.getPlaylists();

        if (playlists == null || playlists.isEmpty()) {
            vueAbonne.afficherMessage("Aucune playlist créée.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- VOS PLAYLISTS ---\n");
        for (int i = 0; i < playlists.size(); i++) {
            sb.append(i + 1).append(". ").append(playlists.get(i).getNom()).append("\n");
        }
        sb.append("0. Retour");

        int choixP = vueAbonne.demanderChoix(sb.toString() + "\nQuelle playlist voulez-vous ouvrir ?");

        if (choixP > 0 && choixP <= playlists.size()) {
            Playlist pSelectionnee = playlists.get(choixP - 1);
            boolean resterDansPlaylist = true;

            while (resterDansPlaylist) {
                ArrayList<Morceau> morceaux = pSelectionnee.getMorceaux();
                StringBuilder sbDetail = new StringBuilder();
                sbDetail.append("\n--- CONTENU : ").append(pSelectionnee.getNom()).append(" ---\n");

                if (morceaux.isEmpty()) {
                    sbDetail.append("(Playlist vide)\n0. Retour");
                    vueAbonne.afficherMessage(sbDetail.toString());
                    resterDansPlaylist = false;
                } else {
                    for (int i = 0; i < morceaux.size(); i++) {
                        sbDetail.append(i + 1).append(". ").append(morceaux.get(i).getTitre())
                                .append(" - ").append(morceaux.get(i).getArtiste()).append("\n");
                    }
                    sbDetail.append("0. Quitter vers le menu abonnés");

                    int choixM = vueAbonne.demanderChoix(sbDetail.toString() + "\nChoisissez un numéro pour écouter ou 0 pour quitter :");

                    if (choixM > 0 && choixM <= morceaux.size()) {
                        controleurCatalogue.ecouter(morceaux.get(choixM - 1), abonne);
                    } else {
                        resterDansPlaylist = false;
                    }
                }
            }
        }
    }

    private void ajouterMorceauAPlaylist(Abonne abonne, Catalogue catalogue) {
        ArrayList<Playlist> playlists = abonne.getPlaylists();

        if (playlists == null || playlists.isEmpty()) {
            vueAbonne.afficherErreur("Vous devez d'abord créer une playlist.");
            return;
        }

        StringBuilder sbPlaylists = new StringBuilder();
        for (int i = 0; i < playlists.size(); i++) {
            sbPlaylists.append(i + 1)
                    .append(". ")
                    .append(playlists.get(i).getNom())
                    .append("\n");
        }

        int choixPlaylist = vueAbonne.demanderChoix(
                "Choisissez une playlist :\n" + sbPlaylists
        );

        if (choixPlaylist < 1 || choixPlaylist > playlists.size()) {
            vueAbonne.afficherErreur("Choix de playlist invalide.");
            return;
        }

        Playlist playlistChoisie = playlists.get(choixPlaylist - 1);

        ArrayList<Morceau> morceauxCatalogue = catalogue.getMorceaux();

        if (morceauxCatalogue == null || morceauxCatalogue.isEmpty()) {
            vueAbonne.afficherErreur("Le catalogue est vide.");
            return;
        }

        StringBuilder sbMorceaux = new StringBuilder();
        for (int i = 0; i < morceauxCatalogue.size(); i++) {
            Morceau m = morceauxCatalogue.get(i);
            sbMorceaux.append(i + 1)
                    .append(". ")
                    .append(m.getTitre())
                    .append(" - ")
                    .append(m.getArtiste())
                    .append("\n");
        }

        int choixMorceau = vueAbonne.demanderChoix(
                "Choisissez un morceau à ajouter :\n" + sbMorceaux
        );

        if (choixMorceau < 1 || choixMorceau > morceauxCatalogue.size()) {
            vueAbonne.afficherErreur("Choix de morceau invalide.");
            return;
        }

        Morceau morceauChoisi = morceauxCatalogue.get(choixMorceau - 1);
        playlistChoisie.ajouterMorceau(morceauChoisi);

        utilitaire.GestionnaireFichiers.sauvegarderPlaylists(listeAbonnes);
        vueAbonne.afficherMessage("Morceau ajouté à la playlist.");


    }
    private void gererEcoutePlaylists(Abonne abonne) {
        boolean resterDansPlaylists = true;

        while (resterDansPlaylists) {
            // L'utilisateur choisit visuellement une de ses playlists
            Playlist p = vueAbonne.selectionnerPlaylist(abonne.getPlaylists());

            if (p != null) {
                boolean resterDansMorceaux = true;
                while (resterDansMorceaux) {
                    // L'utilisateur choisit visuellement un morceau dedans
                    Morceau m = vueAbonne.selectionnerMorceauDansPlaylist(p);

                    if (m != null) {
                        ecouterDepuisAbonne(m, abonne); // La musique se lance !
                    } else {
                        resterDansMorceaux = false; // Il retourne en arrière (aux playlists)
                    }
                }
            } else {
                resterDansPlaylists = false; // L'utilisateur a cliqué sur le menu de gauche
            }
        }
    }

    // ecouter musique pour abonne
    private boolean ecouterDepuisAbonne(Morceau m, Abonne abonne) {
        if (abonne.peutEcouter()) {
            float dureeBrute = m.getDuree();
            int minutes = (int) dureeBrute;
            int secondes = Math.round((dureeBrute - minutes) * 100);
            int tempsRestantEnSecondes = (minutes * 60) + secondes;

            vueAbonne.afficherEcoute(m, tempsRestantEnSecondes);

            int tempsEcoule = 0;
            while (tempsEcoule < tempsRestantEnSecondes && !vueAbonne.isArrete()) {
                try {
                    if (!vueAbonne.isEnPause()) {
                        Thread.sleep(1000);
                        tempsEcoule++;
                        vueAbonne.majProgression(tempsEcoule, tempsRestantEnSecondes);
                    } else {
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            vueAbonne.arreterEcoute();

            abonne.ecouter();
            m.ecouter();
            abonne.ajouterHistorique(m);
            utilitaire.GestionnaireFichiers.enregistrerEcoute(abonne.getLogin(), m.getTitre());

            return true;
        } else {
            vueAbonne.afficherErreur("Limite d'écoutes atteinte.");
            return false;
        }
    }
}