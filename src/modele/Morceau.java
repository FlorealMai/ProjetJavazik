package modele;

public class Morceau {
    private String titre;
    private float duree;
    private String artiste;
    private String album;


    public Morceau(String titre, float duree, String artiste, String album){
        this.titre = titre;
        this.duree = duree;
        this.artiste = artiste;
        this.album = album;

    }
    public String getTitre() {
        return titre;
    }

    public double getDuree() {
        return duree;
    }

    public String getArtiste() {
        return artiste;
    }
    public String getAlbum() {
        return album ;
    }

}
