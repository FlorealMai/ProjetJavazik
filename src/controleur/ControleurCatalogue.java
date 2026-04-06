package controleur;

import modele.Catalogue;
import modele.Morceau;
import modele.Album;
import modele.Artiste;
import modele.Utilisateur;
import java.util.ArrayList;

public class ControleurCatalogue {

    public ControleurCatalogue() {
    }

    /**
     * Recherche un morceau par son titre dans le catalogue.
     */
    public ArrayList<Morceau> rechercherMorceau(String titre, Catalogue catalogue) {
        ArrayList<Morceau> resultats = new ArrayList<>();
        for (Morceau m : catalogue.getMorceaux()) {
            if (m.getTitre().toLowerCase().contains(titre.toLowerCase())) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    /**
     * Recherche un artiste par son nom.
     */
    public Artiste rechercherArtiste(String nom, Catalogue catalogue) {
        for (Artiste a : catalogue.getArtistes()) {
            if (a.getNom().equalsIgnoreCase(nom)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Gère la simulation d'écoute d'un morceau.
     * Vérifie les droits de l'utilisateur avant de lancer la pause.
     */
    public boolean lireMorceau(Morceau m, Utilisateur u) {
        if (u.peutEcouter()) {
            System.out.println("\n[LECTURE] " + m.getTitre() + " par " + m.getArtiste());

            // Simulation par un temps de pause comme demandé
            try {
                Thread.sleep(2000); // Pause de 2 secondes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            u.ecouter(); // Incrémente le compteur ou ajoute à l'historique
            m.ecouter(); // Met à jour les stats du morceau
            return true;
        } else {
            return false; // Limite atteinte pour les visiteurs
        }
    }

    /**
     * Retourne tous les morceaux d'un album spécifique[cite: 98].
     */
    public ArrayList<Morceau> obtenirMorceauxAlbum(Album album) {
        return album.getMorceaux();
    }
}