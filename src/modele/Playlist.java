package modele;

import java.util.ArrayList;

public class Playlist {
    private String nom;
    private ArrayList<Morceau> morceaux;

    public Playlist(String nom) {
        this.nom = nom;
        this.morceaux = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void renommer(String nouveauNom) {
        this.nom = nouveauNom;
    }

    public void ajouterMorceau(Morceau morceau) {
        if (!morceaux.contains(morceau)) {
            morceaux.add(morceau);
        }
    }
    public void retirerMorceau(Morceau morceau) {
        morceaux.remove(morceau);
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }
}