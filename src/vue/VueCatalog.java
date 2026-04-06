package vue;

import modele.Morceau;
import modele.Album;
import modele.Artiste;
import java.util.ArrayList;
import java.util.Scanner;

public class VueCatalog {
    private Scanner scanner;

    public VueCatalog() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu de navigation du catalogue.
     */
    public int afficherMenuCatalogue() {
        System.out.println("\n--- NAVIGATION CATALOGUE ---");
        System.out.println("1. Rechercher un morceau");
        System.out.println("2. Rechercher un artiste ou un groupe");
        System.out.println("3. Afficher tous les morceaux");
        System.out.println("4. Retour au menu principal");
        System.out.print("Votre choix : ");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Entrez un nombre valide : ");
        }
        return scanner.nextInt();
    }

    /**
     * Demande un mot-clé pour la recherche.
     */
    public String demanderRecherche() {
        scanner.nextLine(); // Nettoyage
        System.out.print("Entrez votre recherche : ");
        return scanner.nextLine();
    }

    /**
     * Affiche une liste de morceaux et permet d'en choisir un pour l'écouter.
     */
    public Morceau selectionnerMorceau(ArrayList<Morceau> liste) {
        if (liste.isEmpty()) {
            System.out.println("Aucun morceau trouvé.");
            return null;
        }

        System.out.println("\n--- RESULTATS ---");
        for (int i = 0; i < liste.size(); i++) {
            System.out.println((i + 1) + ". " + liste.get(i).toString());
        }
        System.out.println("0. Annuler");
        System.out.print("Choisissez un morceau à écouter (numéro) : ");

        int choix = scanner.nextInt();
        if (choix > 0 && choix <= liste.size()) {
            return liste.get(choix - 1);
        }
        return null;
    }

    /**
     * Affiche les détails d'un artiste ou d'un groupe.
     */
    public void afficherInfosArtiste(Artiste artiste) {
        if (artiste == null) {
            System.out.println("Artiste non trouvé.");
            return;
        }
        System.out.println("\n--- FICHE ARTISTE ---");
        System.out.println("Nom : " + artiste.getNom());
        System.out.println("Nombre d'albums : " + artiste.getAlbums().size());
        System.out.println("Nombre de morceaux : " + artiste.getMorceaux().size());
    }

    public void afficherMessage(String msg) {
        System.out.println(msg);
    }
}