package vue;

public interface IVueMenuPrincipal {
    int afficherMenuInitial();
    String[] demanderIdentifiants();
    void afficherMessage(String message);
    void afficherErreur(String erreur);
}