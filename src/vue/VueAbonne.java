package vue;

import java.util.Scanner;
import modele.Playlist;

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
        System.out.println("7. Mes Recommandations");
        System.out.println("8. Déconnexion");
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

    @Override
    public Playlist selectionnerPlaylist(java.util.ArrayList<modele.Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) return null;
        System.out.println("\n--- VOS PLAYLISTS ---");
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println((i + 1) + ". " + playlists.get(i).getNom());
        }
        System.out.println("0. Retour");
        int c = demanderChoix("Choisissez une playlist : ");
        if (c > 0 && c <= playlists.size()) return playlists.get(c - 1);
        return null;
    }

    @Override
    public modele.Morceau selectionnerMorceauDansPlaylist(modele.Playlist playlist) {
        if (playlist.getMorceaux() == null || playlist.getMorceaux().isEmpty()) return null;
        System.out.println("\n--- " + playlist.getNom() + " ---");
        for (int i = 0; i < playlist.getMorceaux().size(); i++) {
            System.out.println((i + 1) + ". " + playlist.getMorceaux().get(i).getTitre());
        }
        System.out.println("0. Retour");
        int c = demanderChoix("Choisissez un morceau à écouter : ");
        if (c > 0 && c <= playlist.getMorceaux().size()) return playlist.getMorceaux().get(c - 1);
        return null;
    }

    private boolean enPause = false;
    private boolean arrete = false;

    @Override
    public void afficherEcoute(modele.Morceau m, int dureeTotale) {
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

    @Override
    public void afficherRecommandations(String recommandationsTexte) {
        System.out.println("\n=== MES RECOMMANDATIONS ===");
        System.out.println(recommandationsTexte);
    }

}