package vue;

import modele.Morceau;
import modele.Artiste;
import java.util.ArrayList;
import java.util.Scanner;

public class VueCatalog implements IVueCatalog {
    private Scanner scanner;

    public VueCatalog() {
        this.scanner = new Scanner(System.in);
    }

    @Override
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
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }

    @Override
    public String demanderRecherche() {
        System.out.print("Entrez votre recherche : ");
        return scanner.nextLine();
    }

    @Override
    public Morceau selectionnerMorceau(ArrayList<Morceau> liste) {
        if (liste.isEmpty()) {
            System.out.println("Aucun morceau trouvé.");
            return null;
        }

        System.out.println("\n--- RESULTATS ---");
        for (int i = 0; i < liste.size(); i++) {
            System.out.println((i + 1) + ". " + liste.get(i));
        }
        System.out.println("0. Annuler");
        System.out.print("Choisissez un morceau à écouter (numéro) : ");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Entrez un nombre valide : ");
        }

        int choix = scanner.nextInt();
        scanner.nextLine();

        if (choix > 0 && choix <= liste.size()) {
            return liste.get(choix - 1);
        }
        return null;
    }

    @Override
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

    @Override
    public void afficherMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void afficherErreur(String erreur) {
        System.out.println("Erreur : " + erreur);
    }

    private boolean enPause = false;
    private boolean arrete = false;

    @Override
    public void afficherEcoute(Morceau m, int dureeTotale) {
        System.out.println("\n[LECTURE] " + m.getTitre() + " - " + m.getArtiste());
        System.out.println("Durée totale : " + (dureeTotale / 60) + "m " + (dureeTotale % 60) + "s");
        System.out.println("Commandes : Tapez 'p' + Entrée (Pause) | 'q' + Entrée (Arrêter)");
        enPause = false;
        arrete = false;
    }

    @Override
    public void majProgression(int tempsEcoule, int dureeTotale) {
        System.out.print("\rProgression : " + tempsEcoule + "s / " + dureeTotale + "s ");
    }

    private void lireSaisieConsole() {
        try {
            if (System.in.available() > 0) {
                byte[] bytes = new byte[System.in.available()];
                System.in.read(bytes);
                String saisie = new String(bytes).trim().toLowerCase();

                if (saisie.contains("p")) {
                    enPause = !enPause;
                    System.out.println(enPause ? "\n[PAUSE]" : "\n[REPRISE]");
                } else if (saisie.contains("q")) {
                    arrete = true;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public boolean isEnPause() { lireSaisieConsole(); return enPause; }

    @Override
    public boolean isArrete() { lireSaisieConsole(); return arrete; }

    @Override
    public void arreterEcoute() {
        System.out.println(arrete ? "\n[LECTURE INTERROMPUE]" : "\n[FIN DE LA LECTURE]");
    }
}