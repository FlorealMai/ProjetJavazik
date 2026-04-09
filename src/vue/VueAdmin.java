package vue;

import java.util.Scanner;

public class VueAdmin {

    private Scanner sc = new Scanner(System.in);

    public int afficherMenuAdmin() {
        System.out.println("\n===== MENU ADMIN =====");
        System.out.println("1. Gérer le catalogue");
        System.out.println("2. Gérer les abonnés");
        System.out.println("3. Voir les statistiques");
        System.out.println("4. Déconnexion");
        System.out.print("Choix : ");

        return sc.nextInt();
    }

    public int menuCatalogueAdmin() {
        System.out.println("\n--- Catalogue ---");
        System.out.println("1. Ajouter un morceau");
        System.out.println("2. Supprimer un morceau");
        System.out.println("3. Retour");
        System.out.print("Choix : ");
        return sc.nextInt();
    }

    public int menuAbonnesAdmin() {
        System.out.println("\n--- Abonnés ---");
        System.out.println("1. Supprimer un abonné");
        System.out.println("2. Retour");
        System.out.print("Choix : ");
        return sc.nextInt();
    }

    public String demanderTexte(String message) {
        sc.nextLine();
        System.out.print(message);
        return sc.nextLine();
    }

    public float demanderFloat(String message) {
        System.out.print(message);
        return sc.nextFloat();
    }

    public void afficherMessage(String msg) {
        System.out.println(msg);
    }
}