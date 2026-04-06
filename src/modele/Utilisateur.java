package modele;

public class Utilisateur {

    protected int nombreEcoutes;
    protected final int LIMITE_ECOUTES = 5;

    public Utilisateur() {
        this.nombreEcoutes = 0;
    }

    public boolean peutEcouter() {
        return nombreEcoutes < LIMITE_ECOUTES;
    }

    public void ecouter() {
        if (peutEcouter()) {
            nombreEcoutes++;
        } else {
            System.out.println("Limite atteinte");
        }
    }
}