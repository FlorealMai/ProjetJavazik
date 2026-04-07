package controleur;

import modele.Admin;
import java.util.ArrayList;

public class ControleAdmin {

    /**
     * Vérifie si un login est déjà utilisé par un autre Admin.
     */
    public boolean loginExiste(String login, ArrayList<Admin> liste) {
        for (Admin a : liste) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crée un nouvel admin et l'ajoute à la liste globale.
     */
    public void inscrireNouvelAdmin(String login, String mdp, String nom, ArrayList<Admin> liste) {
        Admin nouvelAdmin = new Admin(login, mdp, nom);
        liste.add(nouvelAdmin);
    }

    /**
     * Tente de connecter un utilisateur.
     * Retourne l'objet Admin si trouvé, sinon null.
     */
    public Admin authentifier(String login, String mdp, ArrayList<Admin> liste) {
        for (Admin a : liste) {
            if (a.getLogin().equals(login) && a.getMotDePasse().equals(mdp)) {
                return a;
            }
        }
        return null;
    }
}