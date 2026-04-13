package vue;

import modele.Morceau;
import modele.Artiste;
import java.util.ArrayList;

public interface IVueCatalog {
    int afficherMenuCatalogue();

    String demanderRecherche();

    Morceau selectionnerMorceau(ArrayList<Morceau> liste);

    void afficherInfosArtiste(Artiste artiste);

    void afficherMessage(String msg);

    void afficherErreur(String erreur);

    void afficherEcoute(Morceau m, int dureeTotale);
    void majProgression(int tempsEcoule, int dureeTotale);
    boolean isEnPause();
    boolean isArrete();
    void arreterEcoute();

}