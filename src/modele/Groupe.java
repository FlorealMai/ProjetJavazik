package modele;

import java.util.ArrayList;

public class Groupe {
    private String nom;
    private ArrayList<Artiste> membres;
    private ArrayList<Album> albums;
    private ArrayList<Morceau> morceaux;

    public Groupe(String nom) {
        this.nom = nom;
        this.membres = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.morceaux = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void ajouterMembre(Artiste artiste) {
        if (!membres.contains(artiste)) {
            membres.add(artiste);
        }
    }

    public void ajouterAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }

    public void ajouterMorceau(Morceau morceau) {
        if (!morceaux.contains(morceau)) {
            morceaux.add(morceau);
        }
    }

    public ArrayList<Artiste> getMembres() {
        return membres;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }
}