package vue;

public interface IVueAbonne {
    int afficherMenuAbonne();
    void afficherInfosAbonne(String login, String nom);
    void afficherHistorique(String historiqueTexte);
    void afficherMessage(String message);
    void afficherErreur(String erreur);
}