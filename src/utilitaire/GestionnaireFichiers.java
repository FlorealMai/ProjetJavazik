package utilitaire;

import modele.Abonne;
import modele.Admin;
import modele.Morceau;
import java.io.*;
import java.util.ArrayList;

public class GestionnaireFichiers {
    // Noms des fichiers de stockage
    private static final String FILE_ABONNES = "abonnes.txt";
    private static final String FILE_ADMINS = "admins.txt";
    private static final String FILE_CATALOGUE = "catalogue.txt";

    // ==========================================================
    // MÉTHODES DE SAUVEGARDE (Écriture)
    // ==========================================================

    public static void sauvegarderTout(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, ArrayList<Morceau> catalogue) {
        sauvegarderAbonnes(abonnes);
        sauvegarderAdmins(admins);
        sauvegarderCatalogue(catalogue);
    }

    private static void sauvegarderAbonnes(ArrayList<Abonne> liste) {
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

    private static void sauvegarderCatalogue(ArrayList<Morceau> liste) {
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
}