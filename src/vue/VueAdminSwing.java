package vue;

import javax.swing.JOptionPane;

public class VueAdminSwing implements IVueAdmin {

    @Override
    public int afficherMenuAdmin() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "===== MENU ADMIN =====\n"
                        + "1. Gérer le catalogue\n"
                        + "2. Gérer les abonnés\n"
                        + "3. Voir les statistiques\n"
                        + "4. Déconnexion\n\n"
                        + "Choix :",
                "Admin",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public int menuCatalogueAdmin() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "--- Catalogue ---\n"
                        + "1. Ajouter un morceau\n"
                        + "2. Supprimer un morceau\n"
                        + "3. Retour\n\n"
                        + "Choix :",
                "Admin - Catalogue",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public int menuAbonnesAdmin() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "--- Abonnés ---\n"
                        + "1. Supprimer un abonné\n"
                        + "2. Retour\n\n"
                        + "Choix :",
                "Admin - Abonnés",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String demanderTexte(String message) {
        String saisie = JOptionPane.showInputDialog(
                null,
                message,
                "Saisie",
                JOptionPane.QUESTION_MESSAGE
        );
        return saisie == null ? "" : saisie;
    }

    @Override
    public float demanderFloat(String message) {
        String saisie = JOptionPane.showInputDialog(
                null,
                message,
                "Saisie",
                JOptionPane.QUESTION_MESSAGE
        );

        if (saisie == null) return 0f;

        try {
            return Float.parseFloat(saisie.replace(',', '.'));
        } catch (Exception e) {
            return 0f;
        }
    }

    @Override
    public void afficherMessage(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public void afficherErreur(String erreur) {
        JOptionPane.showMessageDialog(
                null,
                erreur,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }
}