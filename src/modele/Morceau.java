package modele;

import java.util.ArrayList;

public class Morceau {
    private String titre;
    private float duree;
    private String artiste;
    private ArrayList<Album> albums;

    public Morceau(String titre, float duree, String artiste) {
        this.titre = titre;
        this.duree = duree;
        this.artiste = artiste;
        this.albums = new ArrayList<>();
    }

    public String getTitre() {
        return titre;
    }
    public float getDuree() {
        return duree;
    }
    public String getArtiste() {
        return artiste;
    }
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    //  ajouter un album
    public void ajouterAlbum(Album album) {
        albums.add(album);
    }
}