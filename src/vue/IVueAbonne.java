package vue;

public interface IVueAbonne {
    int afficherMenuAbonne();
    void afficherInfosAbonne(String login, String nom);
    void afficherHistorique(String historiqueTexte);
    void afficherPlaylists(String playlistsTexte);
    String demanderTexte(String message);
    int demanderChoix(String message);
    void afficherMessage(String message);
    void afficherErreur(String erreur);
}