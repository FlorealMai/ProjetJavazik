package vue;

import modele.Artiste;
import modele.Morceau;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VueCatalogSwing implements IVueCatalog {

    private JFrame frame;

    private JButton btnRechercheMorceau;
    private JButton btnRechercheArtiste;
    private JButton btnAfficherTous;
    private JButton btnRetour;

    private JLabel labelMessage;
    private DefaultListModel<String> modeleListe;
    private JList<String> listeVisuelle;

    private int choix = -1;

    public VueCatalogSwing() {
        frame = new JFrame("JAVAZIC - Catalogue");
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
        Color couleurRetour = new Color(235, 235, 235);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(fond);
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titre = new JLabel("Catalogue musical", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        principal.add(titre, BorderLayout.NORTH);

        JPanel panneauGauche = new JPanel(new GridLayout(4, 1, 12, 12));
        panneauGauche.setBackground(fond);
        panneauGauche.setPreferredSize(new Dimension(260, 0));

        btnRechercheMorceau = new JButton("Rechercher un morceau");
        btnRechercheArtiste = new JButton("Rechercher un artiste");
        btnAfficherTous = new JButton("Afficher tous les morceaux");
        btnRetour = new JButton("Retour au menu principal");

        JButton[] boutons = {
                btnRechercheMorceau,
                btnRechercheArtiste,
                btnAfficherTous
        };

        for (JButton bouton : boutons) {
            bouton.setBackground(couleurBouton);
            bouton.setFocusPainted(false);
            bouton.setFont(new Font("Arial", Font.PLAIN, 16));
            panneauGauche.add(bouton);
        }

        btnRetour.setBackground(couleurRetour);
        btnRetour.setFocusPainted(false);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 16));
        panneauGauche.add(btnRetour);

        principal.add(panneauGauche, BorderLayout.WEST);

        modeleListe = new DefaultListModel<>();
        listeVisuelle = new JList<>(modeleListe);
        listeVisuelle.setFont(new Font("Arial", Font.PLAIN, 16));
        listeVisuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listeVisuelle);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Morceaux"));
        principal.add(scrollPane, BorderLayout.CENTER);

        labelMessage = new JLabel("Bienvenue dans le catalogue");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        principal.add(labelMessage, BorderLayout.SOUTH);

        frame.setContentPane(principal);

        // Compatibles avec ton switch
        btnRechercheMorceau.addActionListener(e -> choix = 1);
        btnRechercheArtiste.addActionListener(e -> choix = 2);
        btnAfficherTous.addActionListener(e -> choix = 3);
        btnRetour.addActionListener(e -> choix = 4);
    }

    @Override
    public int afficherMenuCatalogue() {
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
    public String demanderRecherche() {
        String saisie = JOptionPane.showInputDialog(
                frame,
                "Entrez votre recherche :",
                "Recherche",
                JOptionPane.QUESTION_MESSAGE
        );
        return saisie == null ? "" : saisie;
    }

    @Override
    public Morceau selectionnerMorceau(ArrayList<Morceau> liste) {
        if (liste == null || liste.isEmpty()) {
            afficherMessage("Aucun morceau trouvé.");
            return null;
        }

        mettreAJourListeMorceaux(liste);

        JOptionPane.showMessageDialog(
                frame,
                "Sélectionnez un morceau dans la liste puis validez.",
                "Choix du morceau",
                JOptionPane.INFORMATION_MESSAGE
        );

        int index = listeVisuelle.getSelectedIndex();

        if (index >= 0 && index < liste.size()) {
            return liste.get(index);
        }

        afficherErreur("Aucun morceau sélectionné.");
        return null;
    }

    public void mettreAJourListeMorceaux(ArrayList<Morceau> liste) {
        modeleListe.clear();

        if (liste == null || liste.isEmpty()) {
            modeleListe.addElement("Aucun morceau à afficher");
            return;
        }

        for (Morceau morceau : liste) {
            modeleListe.addElement(morceau.toString());
        }

        labelMessage.setText(liste.size() + " morceau(x) affiché(s)");
    }

    @Override
    public void afficherInfosArtiste(Artiste artiste) {
        if (artiste == null) {
            afficherErreur("Artiste non trouvé.");
            return;
        }

        String message = "Nom : " + artiste.getNom()
                + "\nNombre d'albums : " + artiste.getAlbums().size()
                + "\nNombre de morceaux : " + artiste.getMorceaux().size();

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Fiche artiste",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherMessage(String msg) {
        labelMessage.setText(msg);
        JOptionPane.showMessageDialog(
                frame,
                msg,
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