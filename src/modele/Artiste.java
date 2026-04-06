package modele;

import java.util.ArrayList;

public class Artiste {
    private String nom;
    private ArrayList<Morceau> morceaux;
    private ArrayList<Album> albums;

    public Artiste(String nom) {
        this.nom = nom;
        this.morceaux = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }

    public void ajouterAlbum(Album album) {
        albums.add(album);
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }
}