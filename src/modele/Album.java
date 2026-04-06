package modele;

import java.util.ArrayList;

public class Album {
    private String titre;
    private String nomAuteur;
    private ArrayList<Morceau> morceaux;
    private int annee;

    public Album(String titre) {
        this.titre = titre;
        this.nomAuteur = nomAuteur ;
        this.morceaux = new ArrayList<>();
        this.annee = annee;

    }

    public String getTitre() {
        return titre;
    }
    public int getAnnee() {
        return annee;
    }
    public String getAuteur() {
        return nomAuteur;
    }
    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }


    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }
}
