package vue;
import modele.Morceau;
import modele.Playlist;
import java.util.ArrayList;

public interface IVueAbonne {
    int afficherMenuAbonne();
    void afficherInfosAbonne(String login, String nom);
    void afficherHistorique(String historiqueTexte);
    void afficherPlaylists(String playlistsTexte);
    String demanderTexte(String message);
    int demanderChoix(String message);
    void afficherMessage(String message);
    void afficherErreur(String erreur);

    Playlist selectionnerPlaylist(ArrayList<Playlist> playlists);
    Morceau selectionnerMorceauDansPlaylist(Playlist playlist);

    void afficherEcoute(Morceau m, int dureeTotale);
    void majProgression(int tempsEcoule, int dureeTotale);
    boolean isEnPause();
    boolean isArrete();
    void arreterEcoute();
    void afficherRecommandations(String recommandationsTexte);
}