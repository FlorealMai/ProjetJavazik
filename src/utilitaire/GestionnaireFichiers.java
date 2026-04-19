package utilitaire;

import modele.Abonne;
import modele.Admin;
import modele.Morceau;
import java.io.*;
import java.util.ArrayList;
import modele.Playlist;
import java.io.IOException;

public class GestionnaireFichiers {
    // fichier de stockage
    private static final String FILE_ABONNES = "abonnes.txt";
    private static final String FILE_ADMINS = "admins.txt";
    private static final String FILE_CATALOGUE = "catalogue.txt";
    private static final String FILE_PLAYLISTS = "playlists.txt";
    private static final String FILE_HISTORIQUE = "historique.txt";


    // toute les fonction pour sauvegarder dans un fichier txt


    public static void sauvegarderTout(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, ArrayList<Morceau> catalogue) throws IOException {
        sauvegarderAbonnes(abonnes);
        sauvegarderAdmins(admins);
        sauvegarderCatalogue(catalogue);
        sauvegarderPlaylists(abonnes);
    }

    public static void sauvegarderAbonnes(ArrayList<Abonne> liste) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_ABONNES))) {
            for (Abonne a : liste) {
                writer.println(a.getLogin() + ";" + a.getMotDePasse() + ";" + a.getNom());
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde abonnés : " + e.getMessage());
        }
    }

    private static void sauvegarderAdmins(ArrayList<Admin> liste) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_ADMINS))) {
            for (Admin a : liste) {
                writer.println(a.getLogin() + ";" + a.getMotDePasse() + ";" + a.getNom());
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde admins : " + e.getMessage());
        }
    }

    public static void sauvegarderCatalogue(ArrayList<Morceau> liste) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_CATALOGUE))) {
            for (Morceau m : liste) {
                writer.println(m.getTitre() + ";" + m.getDuree() + ";" + m.getAlbum() + ";" + m.getArtiste());
            }
        }
    }

    public static void sauvegarderPlaylists(ArrayList<Abonne> abonnes) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PLAYLISTS))) {
            for (Abonne a : abonnes) {
                for (Playlist p : a.getPlaylists()) {
                    StringBuilder ligne = new StringBuilder();
                    ligne.append(a.getLogin()).append(";")
                            .append(p.getNom()).append(";");

                    ArrayList<Morceau> morceaux = p.getMorceaux();
                    for (int i = 0; i < morceaux.size(); i++) {
                        ligne.append(morceaux.get(i).getTitre());
                        if (i < morceaux.size() - 1) {
                            ligne.append("|");
                        }
                    }

                    writer.println(ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde playlists : " + e.getMessage());
        }
    }

    public static void enregistrerEcoute(String login, String titreMorceau) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_HISTORIQUE, true))) {
            writer.println(login + ";" + titreMorceau);
        } catch (IOException e) {
            System.err.println("Impossible d'écrire dans l'historique : " + e.getMessage());
        }
    }

    // toutes les fonctions de chargement a partir d'un fichier txt


    public static ArrayList<Abonne> chargerAbonnes() {
        ArrayList<Abonne> liste = new ArrayList<>();
        File file = new File(FILE_ABONNES);
        if (!file.exists()) return liste;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] d = ligne.split(";");
                if (d.length == 3) {
                    liste.add(new Abonne(d[0], d[1], d[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement abonnés : " + e.getMessage());
        }
        return liste;
    }

    public static ArrayList<Admin> chargerAdmins() {
        ArrayList<Admin> liste = new ArrayList<>();
        File file = new File(FILE_ADMINS);
        if (!file.exists()) return liste;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] d = ligne.split(";");
                if (d.length == 3) {
                    liste.add(new Admin(d[0], d[1], d[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement admins : " + e.getMessage());
        }
        return liste;
    }

    public static ArrayList<Morceau> chargerCatalogue() {
        ArrayList<Morceau> liste = new ArrayList<>();
        File file = new File(FILE_CATALOGUE);
        if (!file.exists()) return liste;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] d = ligne.split(";");
                if (d.length == 4) {
                    try {
                        String titre = d[0];
                        float duree = Float.parseFloat(d[1]); // Risque de NumberFormatException [cite: 416]
                        String album = d[2];
                        String artiste = d[3];
                        liste.add(new Morceau(titre, duree, album, artiste));
                    } catch (NumberFormatException e) {
                        // pour ignorer les lignes avec des problemes
                        System.err.println("Erreur de format numérique dans le catalogue : " + d[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur technique de lecture du catalogue : " + e.getMessage());
        }
        return liste;
    }

    public static ArrayList<String> chargerHistorique(String loginUtilisateur) {
        ArrayList<String> titres = new ArrayList<>();
        File fichier = new File("historique.txt");

        if (!fichier.exists()) return titres;

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(loginUtilisateur)) {
                    titres.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur technique de lecture : " + e.getMessage());
        }
        return titres;
    }

    public static void chargerPlaylists(ArrayList<Abonne> abonnes, ArrayList<Morceau> catalogue) {
        File file = new File(FILE_PLAYLISTS);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                try {
                    String[] d = ligne.split(";");
                    if (d.length >= 2) {
                        String login = d[0];
                        String nomPlaylist = d[1];
                        Playlist playlist = new Playlist(nomPlaylist);

                        if (d.length == 3 && !d[2].isEmpty()) {
                            String[] titres = d[2].split("\\|");
                            for (String titre : titres) {
                                Morceau morceauTrouve = trouverMorceauParTitre(titre, catalogue);
                                if (morceauTrouve != null) {
                                    playlist.ajouterMorceau(morceauTrouve);
                                }
                            }
                        }

                        Abonne abonneTrouve = trouverAbonneParLogin(login, abonnes);
                        if (abonneTrouve != null) {
                            abonneTrouve.ajouterPlaylist(playlist);
                        }
                    }
                } catch (Exception e) {
                    // Capture toute erreur spécifique à une ligne (ex: index out of bounds) [cite: 405]
                    System.err.println("Ligne de playlist malformée : " + ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur globale lors du chargement des playlists : " + e.getMessage());
        }
    }


    // les fonction pour retrouver un truc précis dans un fichier txt

    private static Abonne trouverAbonneParLogin(String login, ArrayList<Abonne> abonnes) {
        for (Abonne a : abonnes) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return a;
            }
        }
        return null;
    }


    private static Morceau trouverMorceauParTitre(String titre, ArrayList<Morceau> catalogue) {
        for (Morceau m : catalogue) {
            if (m.getTitre().equalsIgnoreCase(titre)) {
                return m;
            }
        }
        return null;
    }

}