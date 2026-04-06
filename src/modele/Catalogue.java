package modele;

import java.util.ArrayList;

public class Catalogue {
    private ArrayList<Morceau> morceaux;
    private ArrayList<Album> albums;
    private ArrayList<Artiste> artistes;
    private ArrayList<Groupe> groupes;

    public Catalogue() {
        this.morceaux = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.artistes = new ArrayList<>();
        this.groupes = new ArrayList<>();
    }

    public void ajouterMorceau(Morceau morceau) {
        if (!morceaux.contains(morceau)) {
            morceaux.add(morceau);
        }
    }
    public void supprimerMorceau(Morceau morceau) {
        morceaux.remove(morceau);
    }

    public void ajouterAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }
    public void supprimerAlbum(Album album) {
        albums.remove(album);
    }

    public void ajouterArtiste(Artiste artiste) {
        if (!artistes.contains(artiste)) {
            artistes.add(artiste);
        }
    }
    public void ajouterGroupe(Groupe groupe) {
        if (!groupes.contains(groupe)) {
            groupes.add(groupe);
        }
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }
    public ArrayList<Album> getAlbums() {
        return albums;
    }
    public ArrayList<Artiste> getArtistes() {
        return artistes;
    }
    public ArrayList<Groupe> getGroupes() {
        return groupes;
    }
}