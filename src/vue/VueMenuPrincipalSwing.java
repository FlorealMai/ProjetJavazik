package vue;

import javax.swing.*;
import java.awt.*;

public class VueMenuPrincipalSwing implements IVueMenuPrincipal {

    private JFrame frame;

    private JButton btnAdmin;
    private JButton btnClient;
    private JButton btnCreerCompte;
    private JButton btnVisiteur;
    private JButton btnQuitter;

    private JLabel labelMessage;

    private int choix = -1;

    public VueMenuPrincipalSwing() {
        frame = new JFrame("JAVAZIC - Menu principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setMinimumSize(new Dimension(2240, 1400));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        initialiserInterface();
    }

    private void initialiserInterface() {
        Color fond = new Color(245, 245, 245);
        Color couleurBouton = new Color(220, 230, 240);
        Color couleurQuitter = new Color(235, 235, 235);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fond);

        JLabel titre = new JLabel("Javazic", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        panelPrincipal.add(titre, BorderLayout.NORTH);

        JPanel panelCentreWrapper = new JPanel(new GridBagLayout());
        panelCentreWrapper.setBackground(fond);

        JPanel panelCentre = new JPanel(new GridLayout(4, 1, 15, 15));
        panelCentre.setBackground(fond);
        panelCentre.setPreferredSize(new Dimension(350, 250));

        btnAdmin = new JButton("Connexion administrateur");
        btnClient = new JButton("Connexion client");
        btnCreerCompte = new JButton("Créer un compte");
        btnVisiteur = new JButton("Continuer en visiteur");
        btnQuitter = new JButton("Quitter");

        JButton[] boutons = {btnAdmin, btnClient, btnCreerCompte, btnVisiteur};
        for (JButton bouton : boutons) {
            bouton.setFocusPainted(false);
            bouton.setBackground(couleurBouton);
            bouton.setFont(new Font("Arial", Font.PLAIN, 18));
        }

        btnQuitter.setFocusPainted(false);
        btnQuitter.setBackground(couleurQuitter);
        btnQuitter.setFont(new Font("Arial", Font.PLAIN, 15));

        panelCentre.add(btnAdmin);
        panelCentre.add(btnClient);
        panelCentre.add(btnCreerCompte);
        panelCentre.add(btnVisiteur);

        panelCentreWrapper.add(panelCentre);
        panelPrincipal.add(panelCentreWrapper, BorderLayout.CENTER);

        JPanel panelBas = new JPanel(new BorderLayout());
        panelBas.setBackground(fond);
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        labelMessage = new JLabel("Bienvenue sur Javazic");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panelQuitter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelQuitter.setBackground(fond);
        panelQuitter.add(btnQuitter);

        panelBas.add(labelMessage, BorderLayout.WEST);
        panelBas.add(panelQuitter, BorderLayout.EAST);

        panelPrincipal.add(panelBas, BorderLayout.SOUTH);

        frame.setContentPane(panelPrincipal);

        btnAdmin.addActionListener(e -> choix = 1);
        btnClient.addActionListener(e -> choix = 2);
        btnCreerCompte.addActionListener(e -> choix = 3);
        btnVisiteur.addActionListener(e -> choix = 4);
        btnQuitter.addActionListener(e -> choix = 5);
    }

    @Override
    public int afficherMenuInitial() {
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
    public String[] demanderIdentifiants() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField champLogin = new JTextField();
        JPasswordField champMdp = new JPasswordField();

        panel.add(new JLabel("Login :"));
        panel.add(champLogin);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMdp);

        int resultat = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Connexion",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultat == JOptionPane.OK_OPTION) {
            return new String[]{
                    champLogin.getText(),
                    new String(champMdp.getPassword())
            };
        }

        return new String[]{"", ""};
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
}