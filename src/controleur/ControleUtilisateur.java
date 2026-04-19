package controleur;

import modele.Abonne;
import java.util.ArrayList;

public class ControleUtilisateur {


    public boolean loginExiste(String login, ArrayList<Abonne> liste) {
        for (Abonne a : liste) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    public void inscrireNouvelAbonne(String login, String mdp, String nom, ArrayList<Abonne> liste) {
        Abonne nouvelAbonne = new Abonne(login, mdp, nom);
        liste.add(nouvelAbonne);
    }

    public Abonne authentifier(String login, String mdp, ArrayList<Abonne> liste) {
        for (Abonne a : liste) {
            if (a.getLogin().equals(login) && a.getMotDePasse().equals(mdp)) {
                return a;
            }
        }
        return null;
    }
    

}