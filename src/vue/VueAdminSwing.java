package vue;

import javax.swing.*;
import java.awt.*;

public class VueAdminSwing implements IVueAdmin {

    private JFrame frame;

    private JButton btnGererCatalogue;
    private JButton btnGererAbonnes;
    private JButton btnVoirStats;
    private JButton btnDeconnexion;

    private JTextArea zoneContenu;
    private JLabel labelMessage;

    private int choix = -1;

    public VueAdminSwing() {
        frame = new JFrame("JAVAZIC - Espace administrateur");
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
        Color couleurSecondaire = new Color(235, 235, 235);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(fond);
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titre = new JLabel("Espace administrateur", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        principal.add(titre, BorderLayout.NORTH);

        JPanel panneauGauche = new JPanel(new GridLayout(4, 1, 12, 12));
        panneauGauche.setBackground(fond);
        panneauGauche.setPreferredSize(new Dimension(270, 0));

        btnGererCatalogue = new JButton("Gérer le catalogue");
        btnGererAbonnes = new JButton("Gérer les abonnés");
        btnVoirStats = new JButton("Voir les statistiques");
        btnDeconnexion = new JButton("Déconnexion");

        JButton[] boutons = {
                btnGererCatalogue,
                btnGererAbonnes,
                btnVoirStats
        };

        for (JButton bouton : boutons) {
            bouton.setBackground(couleurBouton);
            bouton.setFocusPainted(false);
            bouton.setFont(new Font("Arial", Font.PLAIN, 16));
            panneauGauche.add(bouton);
        }

        btnDeconnexion.setBackground(couleurSecondaire);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setFont(new Font("Arial", Font.PLAIN, 16));
        panneauGauche.add(btnDeconnexion);

        principal.add(panneauGauche, BorderLayout.WEST);

        zoneContenu = new JTextArea();
        zoneContenu.setEditable(false);
        zoneContenu.setLineWrap(true);
        zoneContenu.setWrapStyleWord(true);
        zoneContenu.setFont(new Font("Arial", Font.PLAIN, 16));
        zoneContenu.setText("Bienvenue dans l'espace administrateur.");

        JScrollPane scrollPane = new JScrollPane(zoneContenu);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contenu"));
        principal.add(scrollPane, BorderLayout.CENTER);

        labelMessage = new JLabel("Prêt");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        principal.add(labelMessage, BorderLayout.SOUTH);

        frame.setContentPane(principal);

        // action des bouton qui renvois une valeur de choix pour ce deplacer dans les menu
        btnGererCatalogue.addActionListener(e -> choix = 1);
        btnGererAbonnes.addActionListener(e -> choix = 2);
        btnVoirStats.addActionListener(e -> choix = 3);
        btnDeconnexion.addActionListener(e -> choix = 4);
    }

    @Override
    public int afficherMenuAdmin() {
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
    public int menuCatalogueAdmin() {
        String[] options = {
                "1. Ajouter un morceau",
                "2. Supprimer un morceau",
                "3. Retour"
        };

        String choixTexte = (String) JOptionPane.showInputDialog(
                frame,
                "Choisissez une action :",
                "Admin - Catalogue",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choixTexte == null) return 3;
        if (choixTexte.startsWith("1")) return 1;
        if (choixTexte.startsWith("2")) return 2;
        return 3;
    }

    @Override
    public int menuAbonnesAdmin() {
        String[] options = {
                "1. Supprimer un abonné",
                "2. Retour"
        };

        String choixTexte = (String) JOptionPane.showInputDialog(
                frame,
                "Choisissez une action :",
                "Admin - Abonnés",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choixTexte == null) return 2;
        if (choixTexte.startsWith("1")) return 1;
        return 2;
    }

    @Override
    public String demanderTexte(String message) {
        String saisie = JOptionPane.showInputDialog(
                frame,
                message,
                "Saisie",
                JOptionPane.QUESTION_MESSAGE
        );
        return saisie == null ? "" : saisie;
    }

    @Override
    public float demanderFloat(String message) {
        String saisie = JOptionPane.showInputDialog(
                frame,
                message,
                "Saisie",
                JOptionPane.QUESTION_MESSAGE
        );

        if (saisie == null || saisie.isEmpty()) {
            return 0f;
        }

        try {
            return Float.parseFloat(saisie.replace(',', '.'));
        } catch (Exception e) {
            afficherErreur("Valeur invalide.");
            return 0f;
        }
    }

    @Override
    public void afficherMessage(String msg) {
        labelMessage.setText(msg);
        zoneContenu.setText(msg);

        JOptionPane.showMessageDialog(
                frame,
                msg,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public void afficherErreur(String erreur) {
        labelMessage.setText(erreur);

        JOptionPane.showMessageDialog(
                frame,
                erreur,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void afficherContenu(String texte) {
        zoneContenu.setText(texte);
    }
}