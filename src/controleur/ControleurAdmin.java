package controleur;

import modele.Admin;
import modele.Abonne;
import modele.Catalogue;
import modele.Morceau;
import vue.IVueAdmin;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import utilitaire.GestionnaireFichiers;

import java.util.ArrayList;

public class ControleurAdmin {

    private IVueAdmin vueAdmin;

    public ControleurAdmin(IVueAdmin vueAdmin) {
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
            try {
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
            } catch (NumberFormatException e) {
                vueAdmin.afficherMessage("Erreur : Veuillez saisir un nombre valide.");
            }
        }
    }

    private void gererCatalogue(Catalogue catalogue) {
        boolean retour = false;

        while (!retour) {
            try {
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
        } catch (NumberFormatException e) {
            vueAdmin.afficherMessage("Erreur : Veuillez saisir un nombre valide.");
        }
    }
}

    private void ajouterMorceau(Catalogue catalogue) {
        String titre = vueAdmin.demanderTexte("Titre : ");
        String artiste = vueAdmin.demanderTexte("Artiste : ");
        String album = vueAdmin.demanderTexte("Album : ");
        try {
            float duree = vueAdmin.demanderFloat("Durée : ");
            Morceau nouveau = new Morceau(titre, duree, album, artiste);
            catalogue.ajouterMorceau(nouveau);

            utilitaire.GestionnaireFichiers.sauvegarderCatalogue(catalogue.getMorceaux());
            vueAdmin.afficherMessage("Morceau ajouté avec succès !");
        } catch (NumberFormatException e) {
            vueAdmin.afficherMessage("Erreur : La durée doit être un nombre.");
        } catch (java.io.IOException e) {
            vueAdmin.afficherMessage("Erreur technique : Impossible de mettre à jour le fichier catalogue.");
        }
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
            try {
                catalogue.getMorceaux().remove(aSuppr);
                utilitaire.GestionnaireFichiers.sauvegarderCatalogue(catalogue.getMorceaux());
                vueAdmin.afficherMessage("Morceau supprimé avec succès");
            } catch (java.io.IOException e) {
                vueAdmin.afficherMessage("Erreur : Échec de la mise à jour du fichier.");
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
                        vueAdmin.afficherMessage("Compte supprimé");
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
        int nbUsers = abonnes.size();
        int nbMorceaux = catalogue.getMorceaux().size();

        ArrayList<String> artistesUniques = new ArrayList<>();
        ArrayList<String> albumsUniques = new ArrayList<>();

        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getArtiste() != null && !artistesUniques.contains(m.getArtiste())) {
                artistesUniques.add(m.getArtiste());
            }
            if (m.getAlbum() != null && !albumsUniques.contains(m.getAlbum())) {
                albumsUniques.add(m.getAlbum());
            }
        }

        // pour les stat evoluer pour compter
        Map<String, Integer> ecoutesMorceaux = new HashMap<>();
        Map<String, Integer> ecoutesArtistes = new HashMap<>();
        Map<String, Integer> ajoutsPlaylists = new HashMap<>();

        int totalEcoutes = 0;

        for (Abonne a : abonnes) {
            ArrayList<String> historiqueTitres = utilitaire.GestionnaireFichiers.chargerHistorique(a.getLogin());
            totalEcoutes += historiqueTitres.size();

            for (String titre : historiqueTitres) {
                // compteur de morceau
                ecoutesMorceaux.put(titre, ecoutesMorceaux.getOrDefault(titre, 0) + 1);

                // On cherche le morceau dans le catalogue pour trouver son artiste
                Morceau m = trouverMorceau(titre, catalogue);
                if (m != null && m.getArtiste() != null) {
                    ecoutesArtistes.put(m.getArtiste(), ecoutesArtistes.getOrDefault(m.getArtiste(), 0) + 1);
                }
            }

            // compteur des morveau dans playlist
            for (modele.Playlist p : a.getPlaylists()) {
                for (Morceau m : p.getMorceaux()) {
                    ajoutsPlaylists.put(m.getTitre(), ajoutsPlaylists.getOrDefault(m.getTitre(), 0) + 1);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== STATISTIQUES JAVAZIC ===\n\n");
        sb.append("Abonnés inscrits : ").append(nbUsers).append("\n");
        sb.append("Morceaux au catalogue : ").append(nbMorceaux).append("\n");
        sb.append("Albums enregistrés : ").append(albumsUniques.size()).append("\n");
        sb.append("Artistes : ").append(artistesUniques.size()).append("\n");
        sb.append("Nombre total d'écoutes : ").append(totalEcoutes).append("\n\n");

        sb.append("=== STATISTIQUES ÉVOLUÉES ===\n");

        sb.append("\nMorceaux les plus écoutés :\n");
        sb.append(obtenirTop(ecoutesMorceaux, 3));

        sb.append("\nArtistes les plus écoutés :\n");
        sb.append(obtenirTop(ecoutesArtistes, 3));

        sb.append("\nMorceaux les plus ajoutés en playlist :\n");
        sb.append(obtenirTop(ajoutsPlaylists, 3));

        vueAdmin.afficherContenu(sb.toString());
    }

     // permet de trouver un morceau avec le titre
    private Morceau trouverMorceau(String titre, Catalogue catalogue) {
        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().equalsIgnoreCase(titre)) {
                return m;
            }
        }
        return null;
    }

    // trie les Map
    private String obtenirTop(Map<String, Integer> map, int limite) {
        if (map.isEmpty()) {
            return "Aucune donnée suffisante.\n";
        }
        // pour que la map devienne une list
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        // tri decroissant
        list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        StringBuilder sb = new StringBuilder();
        int max = Math.min(limite, list.size());

        for (int i = 0; i < max; i++) {
            sb.append("  ").append(i + 1).append(". ")
                    .append(list.get(i).getKey())
                    .append(" (").append(list.get(i).getValue()).append(" fois)\n");
        }
        return sb.toString();
    }

}