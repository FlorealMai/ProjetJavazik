package modele;

import java.util.ArrayList;

public class Abonne extends Utilisateur {
    private String login;
    private String motDePasse;
    private String nom;
    private ArrayList<Morceau> historique;

    public Abonne(String login, String motDePasse, String nom) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.historique = new ArrayList<>();
    }

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

    public void ajouterHistorique(Morceau morceau) {
        historique.add(morceau);
    }
}