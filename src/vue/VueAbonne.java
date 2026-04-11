package vue;

import java.util.Scanner;

public class VueAbonne implements IVueAbonne {

    private Scanner scanner;

    public VueAbonne() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int afficherMenuAbonne() {
        System.out.println("\n===== MENU ABONNÉ =====");
        System.out.println("1. Accéder au catalogue");
        System.out.println("2. Voir l'historique");
        System.out.println("3. Voir mes informations");
        System.out.println("4. Créer une playlist");
        System.out.println("5. Voir mes playlists");
        System.out.println("6. Ajouter un morceau à une playlist");
        System.out.println("7. Déconnexion");
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
    public void afficherInfosAbonne(String login, String nom) {
        System.out.println("\n--- MES INFORMATIONS ---");
        System.out.println("Login : " + login);
        System.out.println("Nom : " + nom);
    }

    @Override
    public void afficherHistorique(String historiqueTexte) {
        System.out.println("\n--- HISTORIQUE ---");
        System.out.println(historiqueTexte);
    }

    @Override
    public void afficherPlaylists(String playlistsTexte) {
        System.out.println("\n--- PLAYLISTS ---");
        System.out.println(playlistsTexte);
    }

    @Override
    public String demanderTexte(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    @Override
    public int demanderChoix(String message) {
        System.out.println(message);

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Entrez un nombre valide : ");
        }

        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void afficherErreur(String erreur) {
        System.out.println("Erreur : " + erreur);
    }
}