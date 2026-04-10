package vue;

import modele.Artiste;
import modele.Morceau;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class VueCatalogSwing implements IVueCatalog {

    @Override
    public int afficherMenuCatalogue() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "--- NAVIGATION CATALOGUE ---\n"
                        + "1. Rechercher un morceau\n"
                        + "2. Rechercher un artiste ou un groupe\n"
                        + "3. Afficher tous les morceaux\n"
                        + "4. Retour au menu principal\n\n"
                        + "Votre choix :",
                "Catalogue",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            return Integer.parseInt(saisie);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String demanderRecherche() {
        String saisie = JOptionPane.showInputDialog(
                null,
                "Entrez votre recherche :",
                "Recherche",
                JOptionPane.QUESTION_MESSAGE
        );
        return saisie == null ? "" : saisie;
    }

    @Override
    public Morceau selectionnerMorceau(ArrayList<Morceau> liste) {
        if (liste == null || liste.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Aucun morceau trouvé.",
                    "Catalogue",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- RESULTATS ---\n");
        for (int i = 0; i < liste.size(); i++) {
            sb.append(i + 1).append(". ").append(liste.get(i).toString()).append("\n");
        }
        sb.append("\n0. Annuler\n");
        sb.append("Choisissez un morceau :");

        String saisie = JOptionPane.showInputDialog(
                null,
                sb.toString(),
                "Choix du morceau",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            int choix = Integer.parseInt(saisie);
            if (choix > 0 && choix <= liste.size()) {
                return liste.get(choix - 1);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public void afficherInfosArtiste(Artiste artiste) {
        if (artiste == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Artiste non trouvé.",
                    "Artiste",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String message = "--- FICHE ARTISTE ---\n"
                + "Nom : " + artiste.getNom() + "\n"
                + "Nombre d'albums : " + artiste.getAlbums().size() + "\n"
                + "Nombre de morceaux : " + artiste.getMorceaux().size();

        JOptionPane.showMessageDialog(
                null,
                message,
                "Artiste",
                JOptionPane.INFORMATION_MESSAGE
        );
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