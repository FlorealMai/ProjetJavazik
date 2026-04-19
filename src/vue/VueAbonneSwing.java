package vue;

import modele.Morceau;
import modele.Playlist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VueAbonneSwing implements IVueAbonne {

    private JFrame frame;

    private JButton btnCatalogue;
    private JButton btnHistorique;
    private JButton btnInfos;
    private JButton btnCreerPlaylist;
    private JButton btnVoirPlaylists;
    private JButton btnAjouterPlaylist;
    private JButton btnRecommandations;
    private JButton btnDeconnexion;

    private JPanel panelCentre;
    private CardLayout cardLayout;
    private JTextArea zoneContenu;
    private DefaultListModel<String> modeleListe;
    private JList<String> listeVisuelle;

    private JButton btnActionListe;
    private JLabel labelMessage;
    private JLabel labelTitreListe;

    private JPanel panneauLecture;
    private JLabel labelTitreLecture;
    private JProgressBar barreProgression;
    private JButton btnPause;
    private JButton btnStop;

    private volatile int choix = -1;
    private volatile int indexSelection = -2;
    private volatile boolean enPause = false;
    private volatile boolean arrete = false;

    public VueAbonneSwing() {
        frame = new JFrame("JAVAZIC - Espace abonné");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setMinimumSize(new Dimension(850, 550));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        initialiserInterface();
    }

    private void initialiserInterface() {
        Color fond = new Color(245, 245, 245);
        Color couleurBouton = new Color(220, 230, 240);
        Color couleurDeconnexion = new Color(235, 235, 235);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(fond);
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titre = new JLabel("Espace Abonné", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        principal.add(titre, BorderLayout.NORTH);

        //menu de gauche
        JPanel panneauGauche = new JPanel(new GridLayout(8, 1, 10, 10));
        panneauGauche.setBackground(fond);
        panneauGauche.setPreferredSize(new Dimension(280, 0));

        btnCatalogue = new JButton("Accéder au catalogue");
        btnHistorique = new JButton("Voir l'historique");
        btnInfos = new JButton("Mes informations");
        btnCreerPlaylist = new JButton("Créer une playlist");
        btnVoirPlaylists = new JButton("Mes playlists");
        btnAjouterPlaylist = new JButton("Ajouter à une playlist");
        btnRecommandations = new JButton("Mes Recommandations");
        btnDeconnexion = new JButton("Déconnexion");

        JButton[] boutons = {btnCatalogue, btnHistorique, btnInfos, btnCreerPlaylist, btnVoirPlaylists, btnAjouterPlaylist, btnRecommandations};
        for (JButton b : boutons) {
            b.setBackground(couleurBouton);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial", Font.PLAIN, 15));
            panneauGauche.add(b);
        }

        btnDeconnexion.setBackground(couleurDeconnexion);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setFont(new Font("Arial", Font.PLAIN, 15));
        panneauGauche.add(btnDeconnexion);

        principal.add(panneauGauche, BorderLayout.WEST);

        // texte ou liste au centre
        cardLayout = new CardLayout();
        panelCentre = new JPanel(cardLayout);

        // texte
        zoneContenu = new JTextArea();
        zoneContenu.setEditable(false);
        zoneContenu.setFont(new Font("Arial", Font.PLAIN, 16));
        zoneContenu.setMargin(new Insets(10, 15, 10, 15));
        panelCentre.add(new JScrollPane(zoneContenu), "TEXTE");


        // liste des morceau
        JPanel contenuListeBlanc = new JPanel(new BorderLayout());
        contenuListeBlanc.setBackground(Color.WHITE);

        labelTitreListe = new JLabel("=== TITRE ===");
        labelTitreListe.setFont(new Font("Arial", Font.PLAIN, 16));
        labelTitreListe.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        contenuListeBlanc.add(labelTitreListe, BorderLayout.NORTH);

        modeleListe = new DefaultListModel<>();
        listeVisuelle = new JList<>(modeleListe);
        listeVisuelle.setFont(new Font("Arial", Font.PLAIN, 16));
        listeVisuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeVisuelle.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        contenuListeBlanc.add(listeVisuelle, BorderLayout.CENTER);

        JScrollPane scrollPaneListe = new JScrollPane(contenuListeBlanc);
        scrollPaneListe.getViewport().setBackground(Color.WHITE);

        panelCentre.add(scrollPaneListe, "LISTE");



        principal.add(panelCentre, BorderLayout.CENTER);

        // bouton
        JPanel panneauBasGlobal = new JPanel(new BorderLayout());
        panneauBasGlobal.setBackground(fond);

        JPanel panneauSud = new JPanel(new BorderLayout());
        panneauSud.setBackground(fond);
        labelMessage = new JLabel("Bienvenue dans votre espace");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        panneauSud.add(labelMessage, BorderLayout.WEST);

        btnActionListe = new JButton("Sélectionner");
        btnActionListe.setBackground(new Color(180, 230, 180));
        btnActionListe.setFont(new Font("Arial", Font.BOLD, 14));
        btnActionListe.setFocusPainted(false);
        btnActionListe.setVisible(false);
        panneauSud.add(btnActionListe, BorderLayout.EAST);

        panneauBasGlobal.add(panneauSud, BorderLayout.NORTH);

        //affichage de la musique qui play
        panneauLecture = new JPanel(new BorderLayout(15, 0));
        panneauLecture.setBackground(new Color(50, 50, 50));
        panneauLecture.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        labelTitreLecture = new JLabel("En écoute : ");
        labelTitreLecture.setForeground(Color.WHITE);
        labelTitreLecture.setFont(new Font("Arial", Font.BOLD, 14));
        panneauLecture.add(labelTitreLecture, BorderLayout.WEST);

        barreProgression = new JProgressBar(0, 100);
        barreProgression.setStringPainted(true);
        barreProgression.setForeground(new Color(100, 200, 100));
        panneauLecture.add(barreProgression, BorderLayout.CENTER);

        JPanel panneauBoutonsLecture = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panneauBoutonsLecture.setBackground(new Color(50, 50, 50));

        btnPause = new JButton("Pause");
        btnPause.setFocusPainted(false);
        btnStop = new JButton("Arrêter");
        btnStop.setFocusPainted(false);

        panneauBoutonsLecture.add(btnPause);
        panneauBoutonsLecture.add(btnStop);
        panneauLecture.add(panneauBoutonsLecture, BorderLayout.EAST);

        panneauLecture.setVisible(false);
        panneauBasGlobal.add(panneauLecture, BorderLayout.SOUTH);

        principal.add(panneauBasGlobal, BorderLayout.SOUTH);
        frame.setContentPane(principal);

        // action des boutons
        btnCatalogue.addActionListener(e -> { choix = 1; indexSelection = -1; });
        btnHistorique.addActionListener(e -> { choix = 2; indexSelection = -1; });
        btnInfos.addActionListener(e -> { choix = 3; indexSelection = -1; });
        btnCreerPlaylist.addActionListener(e -> { choix = 4; indexSelection = -1; });
        btnVoirPlaylists.addActionListener(e -> { choix = 5; indexSelection = -1; });
        btnAjouterPlaylist.addActionListener(e -> { choix = 6; indexSelection = -1; });
        btnRecommandations.addActionListener(e -> { choix = 7; indexSelection = -1; });
        btnDeconnexion.addActionListener(e -> { choix = 8; indexSelection = -1; });

        btnActionListe.addActionListener(e -> {
            if (listeVisuelle.getSelectedIndex() != -1) indexSelection = listeVisuelle.getSelectedIndex();
            else afficherErreur("Veuillez d'abord cliquer sur un élément.");
        });

        listeVisuelle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && btnActionListe.isVisible() && listeVisuelle.getSelectedIndex() != -1) {
                    indexSelection = listeVisuelle.getSelectedIndex();
                }
            }
        });

        btnPause.addActionListener(e -> {
            enPause = !enPause;
            btnPause.setText(enPause ? "Reprendre" : "⏸ Pause");
        });

        btnStop.addActionListener(e -> arrete = true);
    }

    @Override
    public int afficherMenuAbonne() {
        if (choix != -1) { int c = choix; choix = -1; return c; }
        frame.setVisible(true);
        while (choix == -1) { try { Thread.sleep(100); } catch (InterruptedException e) { return -1; } }
        int c = choix; choix = -1; return c;
    }

    @Override
    public void afficherInfosAbonne(String login, String nom) {
        cardLayout.show(panelCentre, "TEXTE");
        zoneContenu.setText("=== MES INFORMATIONS ===\nLogin : " + login + "\nNom : " + nom);
        labelMessage.setText("Informations affichées");
    }

    @Override
    public void afficherHistorique(String historiqueTexte) {
        cardLayout.show(panelCentre, "TEXTE");
        String titre = "=== MON HISTORIQUE D'ÉCOUTES ===\n\n";

        if (historiqueTexte == null || historiqueTexte.isEmpty()) {
            zoneContenu.setText(titre + "Vous n'avez encore écouté aucun morceau.");
        } else {
            zoneContenu.setText(titre + historiqueTexte);
        }

        labelMessage.setText("Historique affiché");
    }

    @Override
    public void afficherPlaylists(String playlistsTexte) {
        cardLayout.show(panelCentre, "TEXTE");
        String titre = "=== MES PLAYLISTS ===\n\n";

        if (playlistsTexte == null || playlistsTexte.isEmpty()) {
            zoneContenu.setText(titre + "Vous n'avez encore créé aucune playlist.");
        } else {
            zoneContenu.setText(titre + playlistsTexte);
        }

        labelMessage.setText("Playlists affichées");
    }

    @Override
    public void afficherRecommandations(String recommandationsTexte) {
        cardLayout.show(panelCentre, "TEXTE");
        String titre = "=== MES RECOMMANDATIONS ===\n\n";

        if (recommandationsTexte == null || recommandationsTexte.isEmpty()) {
            zoneContenu.setText(titre + "Aucune recommandation disponible pour le moment.");
        } else {
            String textePropre = recommandationsTexte.replace("=== VOS RECOMMANDATIONS ===", "").trim();
            zoneContenu.setText(titre + textePropre);
        }

        // On met à jour le texte
        labelMessage.setText("Recommandations affichées");
    }

    @Override
    public String demanderTexte(String message) {
        String saisie = JOptionPane.showInputDialog(frame, message);
        return saisie == null ? "" : saisie;
    }

    @Override
    public int demanderChoix(String message) {
        String saisie = JOptionPane.showInputDialog(frame, message);
        try { return Integer.parseInt(saisie); } catch (Exception e) { return -1; }
    }

    @Override
    public void afficherMessage(String message) {
        labelMessage.setText(message);
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void afficherErreur(String erreur) {
        labelMessage.setText(erreur);
        JOptionPane.showMessageDialog(frame, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public Playlist selectionnerPlaylist(ArrayList<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            afficherErreur("Vous n'avez aucune playlist.");
            return null;
        }

        labelTitreListe.setText("=== MES PLAYLISTS ===");
        cardLayout.show(panelCentre, "LISTE");

        modeleListe.clear();
        for (Playlist p : playlists) modeleListe.addElement(p.getNom());

        labelMessage.setText("Sélectionnez une playlist (double-clic)");
        btnActionListe.setText("Ouvrir la playlist");
        btnActionListe.setVisible(true);
        indexSelection = -2;

        while (indexSelection == -2) { try { Thread.sleep(100); } catch (InterruptedException e) { return null; } }
        btnActionListe.setVisible(false);
        if (indexSelection == -1) return null;
        return playlists.get(indexSelection);
    }

    @Override
    public Morceau selectionnerMorceauDansPlaylist(Playlist playlist) {
        if (playlist.getMorceaux() == null || playlist.getMorceaux().isEmpty()) {
            afficherErreur("Cette playlist est vide.");
            return null;
        }

        labelTitreListe.setText("=== PLAYLIST : " + playlist.getNom().toUpperCase() + " ===");
        cardLayout.show(panelCentre, "LISTE");

        modeleListe.clear();
        for (Morceau m : playlist.getMorceaux()) modeleListe.addElement(m.getTitre() + " - " + m.getArtiste());

        labelMessage.setText("Sélectionnez un morceau à écouter (double-clic)");
        btnActionListe.setText("Écouter");
        btnActionListe.setVisible(true);
        indexSelection = -2;

        while (indexSelection == -2) { try { Thread.sleep(100); } catch (InterruptedException e) { return null; } }
        btnActionListe.setVisible(false);
        if (indexSelection == -1) return null;
        return playlist.getMorceaux().get(indexSelection);
    }

    // partie qui ecoute la musique les fonction
    @Override
    public void afficherEcoute(Morceau m, int dureeTotale) {
        enPause = false;
        arrete = false;
        btnPause.setText("Pause");
        labelTitreLecture.setText(m.getTitre() + " - " + m.getArtiste());
        barreProgression.setMaximum(dureeTotale);
        barreProgression.setValue(0);
        barreProgression.setString("0s / " + dureeTotale + "s");
        panneauLecture.setVisible(true);
    }

    @Override
    public void majProgression(int tempsEcoule, int dureeTotale) {
        SwingUtilities.invokeLater(() -> {
            barreProgression.setValue(tempsEcoule);
            barreProgression.setString(tempsEcoule + "s / " + dureeTotale + "s");
        });
    }

    @Override
    public boolean isEnPause() { return enPause; }
    @Override
    public boolean isArrete() { return arrete; }

    @Override
    public void arreterEcoute() {
        SwingUtilities.invokeLater(() -> {
            panneauLecture.setVisible(false);
            labelMessage.setText(arrete ? "Lecture interrompue." : "Fin de la lecture.");
        });
    }


}