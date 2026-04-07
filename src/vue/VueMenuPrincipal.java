package vue;

import java.util.Scanner;

public class VueMenuPrincipal {
    private Scanner scanner;

    public VueMenuPrincipal() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu de bienvenue et récupère le choix de l'utilisateur.
     * Correspond au lancement du programme.
     */
    public int afficherMenuInitial() {
        System.out.println("\n--- BIENVENUE SUR JAVAZIC ---");
        System.out.println("1. Se connecter en tant qu'administrateur");
        System.out.println("2. Se connecter en tant que client");
        System.out.println("3. Créer un compte client");
        System.out.println("4. Continuer en tant que simple visiteur");
        System.out.println("5. Quitter");
        System.out.print("Votre choix : ");

        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide : ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Formulaire générique pour la connexion ou l'inscription.
     */
    public String[] demanderIdentifiants() {
        scanner.nextLine(); // Nettoyage du tampon
        System.out.print("identifiant : ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();
        return new String[]{login, mdp};
    }

    public void afficherMessage(String message) {
        System.out.println("[INFO] " + message);
    }

    public void afficherErreur(String message) {
        System.err.println("[ERREUR] " + message);
    }




}