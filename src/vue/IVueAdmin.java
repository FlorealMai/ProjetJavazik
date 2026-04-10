package vue;

public interface IVueAdmin {
    int afficherMenuAdmin();
    int menuCatalogueAdmin();
    int menuAbonnesAdmin();
    String demanderTexte(String message);
    float demanderFloat(String message);
    void afficherMessage(String msg);
}