package controleur;

import modele.Abonne;
import modele.Catalogue;
import modele.Morceau;
import modele.Playlist;
import vue.IVueAbonne;

import java.util.ArrayList;

public class ControleurAbonne {

    private IVueAbonne vueAbonne;
    private ControleurCatalogue controleurCatalogue;

    public ControleurAbonne(IVueAbonne vueAbonne, ControleurCatalogue controleurCatalogue) {
        this.vueAbonne = vueAbonne;
        this.controleurCatalogue = controleurCatalogue;
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
                    afficherPlaylists(abonne);
                    break;

                case 6:
                    ajouterMorceauAPlaylist(abonne, catalogue);
                    break;

                case 7:
                    quitter = true;
                    break;

                default:
                    vueAbonne.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void afficherHistorique(Abonne abonne) {
        ArrayList<Morceau> historique = abonne.getHistorique();

        if (historique == null || historique.isEmpty()) {
            vueAbonne.afficherMessage("Aucun morceau écouté.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historique.size(); i++) {
            Morceau m = historique.get(i);
            sb.append(i + 1)
                    .append(". ")
                    .append(m.getTitre())
                    .append(" - ")
                    .append(m.getArtiste())
                    .append("\n");
        }

        vueAbonne.afficherHistorique(sb.toString());
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

        vueAbonne.afficherMessage("Playlist créée avec succès.");
    }

    private void afficherPlaylists(Abonne abonne) {
        ArrayList<Playlist> playlists = abonne.getPlaylists();

        if (playlists == null || playlists.isEmpty()) {
            vueAbonne.afficherMessage("Aucune playlist créée.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            sb.append(i + 1)
                    .append(". ")
                    .append(p.getNom())
                    .append(" (")
                    .append(p.getMorceaux().size())
                    .append(" morceaux)")
                    .append("\n");
        }

        vueAbonne.afficherPlaylists(sb.toString());
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

        vueAbonne.afficherMessage("Morceau ajouté à la playlist.");
    }
}