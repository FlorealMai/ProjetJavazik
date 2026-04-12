package vue;

import javax.swing.*;
import java.awt.*;

public class VueAbonneSwing implements IVueAbonne {

    private JFrame frame;

    private JButton btnCatalogue;
    private JButton btnHistorique;
    private JButton btnInfos;
    private JButton btnCreerPlaylist;
    private JButton btnVoirPlaylists;
    private JButton btnAjouterPlaylist;
    private JButton btnDeconnexion;

    private JTextArea zoneContenu;
    private JLabel labelMessage;

    private int choix = -1;

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

        JLabel titre = new JLabel("Espace abonné", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        principal.add(titre, BorderLayout.NORTH);

        JPanel panneauGauche = new JPanel(new GridLayout(7, 1, 12, 12));
        panneauGauche.setBackground(fond);
        panneauGauche.setPreferredSize(new Dimension(270, 0));

        btnCatalogue = new JButton("Accéder au catalogue");
        btnHistorique = new JButton("Voir l'historique");
        btnInfos = new JButton("Voir mes informations");
        btnCreerPlaylist = new JButton("Créer une playlist");
        btnVoirPlaylists = new JButton("Voir mes playlists");
        btnAjouterPlaylist = new JButton("Ajouter à une playlist");
        btnDeconnexion = new JButton("Déconnexion");

        JButton[] boutons = {
                btnCatalogue,
                btnHistorique,
                btnInfos,
                btnCreerPlaylist,
                btnVoirPlaylists,
                btnAjouterPlaylist
        };

        for (JButton bouton : boutons) {
            bouton.setBackground(couleurBouton);
            bouton.setFocusPainted(false);
            bouton.setFont(new Font("Arial", Font.PLAIN, 16));
            panneauGauche.add(bouton);
        }

        btnDeconnexion.setBackground(couleurDeconnexion);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setFont(new Font("Arial", Font.PLAIN, 16));
        panneauGauche.add(btnDeconnexion);

        principal.add(panneauGauche, BorderLayout.WEST);

        zoneContenu = new JTextArea();
        zoneContenu.setEditable(false);
        zoneContenu.setLineWrap(true);
        zoneContenu.setWrapStyleWord(true);
        zoneContenu.setFont(new Font("Arial", Font.PLAIN, 16));
        zoneContenu.setText("Bienvenue dans votre espace abonné.");

        JScrollPane scrollPane = new JScrollPane(zoneContenu);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contenu"));
        principal.add(scrollPane, BorderLayout.CENTER);

        labelMessage = new JLabel("Bienvenue");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        principal.add(labelMessage, BorderLayout.SOUTH);

        frame.setContentPane(principal);

        // Compatibles avec ton switch
        btnCatalogue.addActionListener(e -> choix = 1);
        btnHistorique.addActionListener(e -> choix = 2);
        btnInfos.addActionListener(e -> choix = 3);
        btnCreerPlaylist.addActionListener(e -> choix = 4);
        btnVoirPlaylists.addActionListener(e -> choix = 5);
        btnAjouterPlaylist.addActionListener(e -> choix = 6);
        btnDeconnexion.addActionListener(e -> choix = 7);
    }

    @Override
    public int afficherMenuAbonne() {
        choix = -1;
        frame.setVisible(true);

        while (choix == -1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }

        return choix;
    }

    @Override
    public void afficherInfosAbonne(String login, String nom) {
        zoneContenu.setText("Mes informations\n\nLogin : " + login + "\nNom : " + nom);
        labelMessage.setText("Informations affichées");
    }

    @Override
    public void afficherHistorique(String historiqueTexte) {
        if (historiqueTexte == null || historiqueTexte.isEmpty()) {
            historiqueTexte = "Aucun historique.";
        }
        zoneContenu.setText(historiqueTexte);
        labelMessage.setText("Historique affiché");
    }

    @Override
    public void afficherPlaylists(String playlistsTexte) {
        if (playlistsTexte == null || playlistsTexte.isEmpty()) {
            playlistsTexte = "Aucune playlist.";
        }
        zoneContenu.setText(playlistsTexte);
        labelMessage.setText("Playlists affichées");
    }

    @Override
    public String demanderTexte(String message) {
        String saisie = JOptionPane.showInputDialog(frame, message);
        return saisie == null ? "" : saisie;
    }

    @Override
    public int demanderChoix(String message) {
        String saisie = JOptionPane.showInputDialog(frame, message);

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void afficherMessage(String message) {
        labelMessage.setText(message);
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherErreur(String erreur) {
        labelMessage.setText(erreur);
        JOptionPane.showMessageDialog(
                frame,
                erreur,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void dispose() {
        frame.dispose();
    }
}