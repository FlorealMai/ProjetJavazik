package controleur;

import modele.Abonne;
import modele.Morceau;
import vue.IVueAbonne;

public class ControleurAbonne {

    private IVueAbonne vueAbonne;
    private ControleurCatalogue controleurCatalogue;

    public ControleurAbonne(IVueAbonne vueAbonne, ControleurCatalogue controleurCatalogue) {
        this.vueAbonne = vueAbonne;
        this.controleurCatalogue = controleurCatalogue;
    }

    public void menuAbonne(Abonne abonne, modele.Catalogue catalogue) {
        boolean quitter = false;

        while (!quitter) {
            int choix = vueAbonne.afficherMenuAbonne();

            switch (choix) {
                case 1:
                    controleurCatalogue.gererCatalogue(catalogue, abonne);
                    break;

                case 2:
                    afficherHistorique(abonne);
                    break;

                case 3:
                    vueAbonne.afficherInfosAbonne(abonne.getLogin(), abonne.getNom());
                    break;

                case 4:
                    quitter = true;
                    break;

                default:
                    vueAbonne.afficherErreur("Choix invalide.");
                    break;
            }
        }
    }

    private void afficherHistorique(Abonne abonne) {
        if (abonne.getHistorique().isEmpty()) {
            vueAbonne.afficherMessage("Aucun morceau écouté.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Morceau m : abonne.getHistorique()) {
            sb.append("- ")
                    .append(m.getTitre())
                    .append(" - ")
                    .append(m.getArtiste())
                    .append("\n");
        }

        vueAbonne.afficherHistorique(sb.toString());
    }
}