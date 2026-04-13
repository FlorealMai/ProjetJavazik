package controleur;

import modele.Abonne;
import java.util.ArrayList;

public class ControleUtilisateur {
    private int compteurEcoutes = 0;
    private final int LIMITE = 5;



    /**
     * Vérifie si un login est déjà utilisé par un autre abonné.
     */
    public boolean loginExiste(String login, ArrayList<Abonne> liste) {
        for (Abonne a : liste) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crée un nouvel abonné et l'ajoute à la liste globale.
     */
    public void inscrireNouvelAbonne(String login, String mdp, String nom, ArrayList<Abonne> liste) {
        Abonne nouvelAbonne = new Abonne(login, mdp, nom);
        liste.add(nouvelAbonne);
    }

    /**
     * Tente de connecter un utilisateur.
     * Retourne l'objet Abonne si trouvé, sinon null.
     */
    public Abonne authentifier(String login, String mdp, ArrayList<Abonne> liste) {
        for (Abonne a : liste) {
            if (a.getLogin().equals(login) && a.getMotDePasse().equals(mdp)) {
                return a;
            }
        }
        return null;
    }
    

}