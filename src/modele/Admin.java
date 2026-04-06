package modele;

public class Admin {
    private String login;
    private String motDePasse;
    private String nom;

    public Admin(String login, String motDePasse, String nom) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.nom = nom;
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
}