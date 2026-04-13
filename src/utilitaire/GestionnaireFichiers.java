package utilitaire;

import modele.Abonne;
import modele.Admin;
import modele.Morceau;
import java.io.*;
import java.util.ArrayList;
import modele.Playlist;

public class GestionnaireFichiers {
    // Noms des fichiers de stockage
    private static final String FILE_ABONNES = "abonnes.txt";
    private static final String FILE_ADMINS = "admins.txt";
    private static final String FILE_CATALOGUE = "catalogue.txt";
    private static final String FILE_PLAYLISTS = "playlists.txt";
    private static final String FILE_HISTORIQUE = "historique.txt";
    // ==========================================================
    // MÉTHODES DE SAUVEGARDE (Écriture)
    // ==========================================================

    public static void sauvegarderTout(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, ArrayList<Morceau> catalogue) {
        sauvegarderAbonnes(abonnes);
        sauvegarderAdmins(admins);
        sauvegarderCatalogue(catalogue);
        sauvegarderPlaylists(abonnes);
    }

    public static void sauvegarderAbonnes(ArrayList<Abonne> liste) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_ABONNES))) {
            for (Abonne a : liste) {
                // Format : login;motdepasse;nom
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

    public static void sauvegarderCatalogue(ArrayList<Morceau> liste) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_CATALOGUE))) {
            for (Morceau m : liste) {
                // Format : titre;duree;artiste
                writer.println(m.getTitre() + ";" + m.getDuree() + ";" + m.getArtiste());
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde catalogue : " + e.getMessage());
        }
    }

    // ==========================================================
    // MÉTHODES DE CHARGEMENT (Lecture)
    // ==========================================================

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
                if (d.length == 3) {
                    // Attention au Float.parseFloat pour la durée
                    liste.add(new Morceau(d[0], Float.parseFloat(d[1]), d[2]));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement catalogue : " + e.getMessage());
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
                // Format du fichier : login;titre
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(loginUtilisateur)) {
                    titres.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur technique de lecture : " + e.getMessage());
        }
        return titres;
    }

//-----------------------------------------------
//          SAUVEGARDE PLAY LISTS
//-----------------------------------------------
    public static void sauvegarderPlaylists(ArrayList<Abonne> abonnes) {
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
    public static void chargerPlaylists(ArrayList<Abonne> abonnes, ArrayList<Morceau> catalogue) {
        File file = new File(FILE_PLAYLISTS);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ligne;

            while ((ligne = reader.readLine()) != null) {
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
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement playlists : " + e.getMessage());
        }
    }
    private static Abonne trouverAbonneParLogin(String login, ArrayList<Abonne> abonnes) {
        for (Abonne a : abonnes) {
            if (a.getLogin().equalsIgnoreCase(login)) {
                return a;
            }
        }
        return null;
    }

    public static void enregistrerEcoute(String login, String titreMorceau) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_HISTORIQUE, true))) {
            writer.println(login + ";" + titreMorceau);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement de l'historique : " + e.getMessage());
        }
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