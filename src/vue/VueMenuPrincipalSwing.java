/// //////////////////////////////A Modiffier//////////////////////////////////////////
package vue;

import javax.swing.JOptionPane;

public class VueMenuPrincipalSwing implements IVueMenuPrincipal {

    @Override
    public int afficherMenuInitial() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "===== MENU PRINCIPAL =====\n"
                        + "1. Connexion administrateur\n"
                        + "2. Connexion client\n"
                        + "3. Créer un compte client\n"
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
        String login = JOptionPane.showInputDialog(null, "Login :");
        String mdp = JOptionPane.showInputDialog(null, "Mot de passe :");

        if (login == null) login = "";
        if (mdp == null) mdp = "";

        return new String[]{login, mdp};
    }

    @Override
    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void afficherErreur(String erreur) {
        JOptionPane.showMessageDialog(
                null,
                erreur,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }
}