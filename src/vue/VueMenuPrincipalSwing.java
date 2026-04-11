package vue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VueMenuPrincipalSwing implements IVueMenuPrincipal {

    private final JFrame frame;

    public VueMenuPrincipalSwing() {
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public int afficherMenuInitial() {


        String saisie = JOptionPane.showInputDialog(
                frame,
                "1. Connexion administrateur\n"
                        + "2. Connexion client\n"
                        + "3. Créer un compte\n"
                        + "4. Continuer en visiteur\n"
                        + "5. Quitter\n\n"
                        + "Votre choix :",
                "JAVAZIC - Menu principal",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String[] demanderIdentifiants() {
        String login = JOptionPane.showInputDialog(frame, "Login :");
        String mdp = JOptionPane.showInputDialog(frame, "Mot de passe :");

        if (login == null) login = "";
        if (mdp == null) mdp = "";

        return new String[]{login, mdp};
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