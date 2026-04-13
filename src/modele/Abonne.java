package modele;

import java.util.ArrayList;

public class Abonne extends Utilisateur {
    private String login;
    private String motDePasse;
    private String nom;

    private ArrayList<Morceau> historique;
    private ArrayList<Playlist> playlists;

    public Abonne(String login, String motDePasse, String nom) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.historique = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    // ===== GETTERS =====

    public String getLogin() {
        return login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Morceau> getHistorique() {
        return historique;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    // ===== HISTORIQUE =====

    public void ajouterHistorique(Morceau morceau) {
        if (morceau != null) {
            historique.add(morceau);
        }
    }

    // ===== PLAYLISTS =====

    public void ajouterPlaylist(Playlist p) {
        if (p != null) {
            playlists.add(p);
        }
    }

    public void supprimerPlaylist(Playlist p) {
        playlists.remove(p);
    }

    public Playlist getPlaylistParNom(String nom) {
        for (Playlist p : playlists) {
            if (p.getNom().equalsIgnoreCase(nom)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean peutEcouter() {
        // abonné peu tjr ecouté
        return true;
    }

}