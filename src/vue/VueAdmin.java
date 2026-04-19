package vue;

import java.util.Scanner;


public class VueAdmin implements IVueAdmin{

    private Scanner sc = new Scanner(System.in);

    public int afficherMenuAdmin() {
        System.out.println("\n===== MENU ADMIN =====");
        System.out.println("1. Gérer le catalogue");
        System.out.println("2. Gérer les abonnés");
        System.out.println("3. Voir les statistiques");
        System.out.println("4. Déconnexion");
        System.out.print("Choix : ");

        int choix = sc.nextInt();
        sc.nextLine();
        return choix;
    }

    public int menuCatalogueAdmin() {
        System.out.println("\n=== Catalogue ===");
        System.out.println("1. Ajouter un morceau");
        System.out.println("2. Supprimer un morceau");
        System.out.println("3. Retour");
        System.out.print("Choix : ");

        int choix = sc.nextInt();
        sc.nextLine();
        return choix;
    }

    public int menuAbonnesAdmin() {
        System.out.println("\n=== Abonnés ===");
        System.out.println("1. Supprimer un abonné");
        System.out.println("2. Retour");
        System.out.print("Choix : ");

        int choix = sc.nextInt();
        sc.nextLine();
        return choix;
    }

    public String demanderTexte(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public float demanderFloat(String message) {
        System.out.print(message);
        float valeur = sc.nextFloat();
        sc.nextLine();
        return valeur;
    }

    public void afficherContenu(String texte) {
        System.out.println(texte);
    }

    public void afficherMessage(String msg) {
        System.out.println(msg);
    }


}