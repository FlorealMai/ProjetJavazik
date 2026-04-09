package modele;

import java.util.ArrayList;

public class Morceau {
    private String titre;
    private float duree;
    private String artiste;
    private ArrayList<Album> albums;


    private int nombreEcoutes;
    private long derniereEcoute; // timestamp

    public Morceau(String titre, float duree, String artiste) {
        this.titre = titre;
        this.duree = duree;
        this.artiste = artiste;
        this.albums = new ArrayList<>();
        this.nombreEcoutes = 0;
        this.derniereEcoute = 0;
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

    public int getNombreEcoutes() {
        return nombreEcoutes;
    }

    public long getDerniereEcoute() {
        return derniereEcoute;
    }

    // 🔗 liaison avec album
    public void ajouterAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }

    // 🎧 écouter un morceau
    public void ecouter() {
        nombreEcoutes++;
        derniereEcoute = System.currentTimeMillis();
    }
    public int getNbEcoutes() {
        return nombreEcoutes;
    }

    @Override
    public String toString() {
        return titre + " - " + artiste;
    }
}