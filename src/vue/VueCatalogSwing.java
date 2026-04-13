package vue;

import modele.Artiste;
import modele.Morceau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VueCatalogSwing implements IVueCatalog {

    private JFrame frame;

    private JButton btnRechercheMorceau;
    private JButton btnRechercheArtiste;
    private JButton btnAfficherTous;
    private JButton btnRetour;
    private JButton btnEcouter;

    private JLabel labelMessage;
    private DefaultListModel<String> modeleListe;
    private JList<String> listeVisuelle;

    // --- COMPOSANTS DU LECTEUR ---
    private JPanel panneauLecture;
    private JLabel labelTitreLecture;
    private JProgressBar barreProgression;
    private JButton btnPause;
    private JButton btnStop;

    private volatile int choix = -1;
    private volatile int indexSelection = -2;
    private volatile boolean enPause = false;
    private volatile boolean arrete = false;

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

        JButton[] boutons = {btnRechercheMorceau, btnRechercheArtiste, btnAfficherTous};
        for (JButton bouton : boutons) {
            bouton.setBackground(couleurBouton);
            bouton.setFocusPainted(false);
            bouton.setFont(new Font("Arial", Font.PLAIN, 16));
            panneauGauche.add(bouton);
        }

        btnRetour.setBackground(new Color(235, 235, 235));
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

        // --- ZONE DU BAS (Bouton valider + Lecteur) ---
        JPanel panneauBasGlobal = new JPanel(new BorderLayout());
        panneauBasGlobal.setBackground(fond);

        JPanel panneauSud = new JPanel(new BorderLayout());
        panneauSud.setBackground(fond);
        labelMessage = new JLabel("Bienvenue dans le catalogue");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        panneauSud.add(labelMessage, BorderLayout.WEST);

        btnEcouter = new JButton("Écouter la sélection");
        btnEcouter.setBackground(new Color(180, 230, 180));
        btnEcouter.setFont(new Font("Arial", Font.BOLD, 14));
        btnEcouter.setFocusPainted(false);
        btnEcouter.setVisible(false);
        panneauSud.add(btnEcouter, BorderLayout.EAST);

        panneauBasGlobal.add(panneauSud, BorderLayout.NORTH);

        // --- LECTEUR AUDIO GRAPHIQUE ---
        panneauLecture = new JPanel(new BorderLayout(15, 0));
        panneauLecture.setBackground(new Color(50, 50, 50));
        panneauLecture.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        labelTitreLecture = new JLabel("En écoute : ");
        labelTitreLecture.setForeground(Color.WHITE);
        labelTitreLecture.setFont(new Font("Arial", Font.BOLD, 14));
        panneauLecture.add(labelTitreLecture, BorderLayout.WEST);

        barreProgression = new JProgressBar(0, 100);
        barreProgression.setStringPainted(true);
        barreProgression.setForeground(new Color(100, 200, 100)); // Vert Spotify
        panneauLecture.add(barreProgression, BorderLayout.CENTER);

        JPanel panneauBoutonsLecture = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panneauBoutonsLecture.setBackground(new Color(50, 50, 50));

        btnPause = new JButton("⏸ Pause");
        btnPause.setFocusPainted(false);
        btnStop = new JButton("Arrêter");
        btnStop.setFocusPainted(false);

        panneauBoutonsLecture.add(btnPause);
        panneauBoutonsLecture.add(btnStop);
        panneauLecture.add(panneauBoutonsLecture, BorderLayout.EAST);

        panneauLecture.setVisible(false); // Caché tant qu'il n'y a pas de musique
        panneauBasGlobal.add(panneauLecture, BorderLayout.SOUTH);

        principal.add(panneauBasGlobal, BorderLayout.SOUTH);
        frame.setContentPane(principal);

        // --- EVENEMENTS ---
        btnRechercheMorceau.addActionListener(e -> { choix = 1; indexSelection = -1; });
        btnRechercheArtiste.addActionListener(e -> { choix = 2; indexSelection = -1; });
        btnAfficherTous.addActionListener(e -> { choix = 3; indexSelection = -1; });
        btnRetour.addActionListener(e -> { choix = 4; indexSelection = -1; });

        btnEcouter.addActionListener(e -> {
            if (listeVisuelle.getSelectedIndex() != -1) indexSelection = listeVisuelle.getSelectedIndex();
            else afficherErreur("Veuillez d'abord cliquer sur un morceau.");
        });

        listeVisuelle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && btnEcouter.isVisible() && listeVisuelle.getSelectedIndex() != -1) {
                    indexSelection = listeVisuelle.getSelectedIndex();
                }
            }
        });

        btnPause.addActionListener(e -> {
            enPause = !enPause;
            btnPause.setText(enPause ? "Reprendre" : "Pause");
        });

        btnStop.addActionListener(e -> arrete = true);
    }

    @Override
    public int afficherMenuCatalogue() {
        if (choix != -1) { int c = choix; choix = -1; return c; }
        frame.setVisible(true);
        while (choix == -1) { try { Thread.sleep(100); } catch (InterruptedException e) { return -1; } }
        int c = choix; choix = -1; return c;
    }

    @Override
    public String demanderRecherche() {
        String saisie = JOptionPane.showInputDialog(frame, "Entrez votre recherche :", "Recherche", JOptionPane.QUESTION_MESSAGE);
        return saisie == null ? "" : saisie;
    }

    @Override
    public Morceau selectionnerMorceau(ArrayList<Morceau> liste) {
        if (liste == null || liste.isEmpty()) { afficherMessage("Aucun morceau trouvé."); return null; }
        mettreAJourListeMorceaux(liste);
        btnEcouter.setVisible(true);
        indexSelection = -2;
        while (indexSelection == -2) { try { Thread.sleep(100); } catch (InterruptedException e) { return null; } }
        btnEcouter.setVisible(false);
        if (indexSelection == -1) return null;
        return liste.get(indexSelection);
    }

    public void mettreAJourListeMorceaux(ArrayList<Morceau> liste) {
        modeleListe.clear();
        if (liste == null || liste.isEmpty()) { modeleListe.addElement("Aucun morceau à afficher"); return; }
        for (Morceau morceau : liste) modeleListe.addElement(morceau.toString());
        labelMessage.setText(liste.size() + " morceau(x) affiché(s)");
    }

    @Override
    public void afficherInfosArtiste(Artiste artiste) {
        if (artiste == null) return;
        JOptionPane.showMessageDialog(frame, "Nom : " + artiste.getNom(), "Fiche", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void afficherMessage(String msg) { labelMessage.setText(msg); }

    @Override
    public void afficherErreur(String erreur) { JOptionPane.showMessageDialog(frame, erreur, "Erreur", JOptionPane.ERROR_MESSAGE); }

    // --- METHODES DU LECTEUR ---
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
            afficherMessage(arrete ? "Lecture interrompue." : "Fin de la lecture.");
        });
    }
}