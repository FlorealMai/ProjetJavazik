package modele;

import modele.Utilisateur;

import java.util.ArrayList;

public class Abonne extends Utilisateur {

    private String login;
    private String motDePasse;
    private String nom;

    private ArrayList<Playlist> playlists;
    private ArrayList<Morceau> historique;

    public Abonne(String login, String motDePasse, String nom) {
        super();
        this.login = login;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.playlists = new ArrayList<>();
        this.historique = new ArrayList<>();
    }

    // 🔥 override → pas de limite pour abonné
    @Override
    public boolean peutEcouter() {
        return true;
    }

    // écouter un morceau
    public void ecouter(Morceau morceau) {
        morceau.ecouter();      // incrémente le compteur du morceau
        historique.add(morceau);
    }

    // historique
    public ArrayList<Morceau> getHistorique() {
        return historique;
    }

    // playlists
    public void creerPlaylist(String nom) {
        playlists.add(new Playlist(nom));
    }
    public void supprimerPlaylist(Playlist p) {
        playlists.remove(p);
    }
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    // login
    public String getLogin() {
        return login;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public String getNom() {
        return nom;
    }
}