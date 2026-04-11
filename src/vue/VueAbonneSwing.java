package vue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VueAbonneSwing implements IVueAbonne {

    private final JFrame frame;

    public VueAbonneSwing() {
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public int afficherMenuAbonne() {
        String saisie = JOptionPane.showInputDialog(
                frame,
                "===== MENU ABONNÉ =====\n"
                        + "1. Accéder au catalogue\n"
                        + "2. Voir l'historique\n"
                        + "3. Voir mes informations\n"
                        + "4. Déconnexion\n\n"
                        + "Votre choix :",
                "Menu abonné",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void afficherInfosAbonne(String login, String nom) {
        JOptionPane.showMessageDialog(
                frame,
                "Login : " + login + "\nNom : " + nom,
                "Mes informations",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherHistorique(String historiqueTexte) {
        JOptionPane.showMessageDialog(
                frame,
                historiqueTexte,
                "Historique",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherErreur(String erreur) {
        JOptionPane.showMessageDialog(
                frame,
                erreur,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }
}