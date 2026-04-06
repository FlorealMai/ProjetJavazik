package modele;

import java.util.ArrayList;

public class Historique {
    private ArrayList<Morceau> morceauxEcoutes;

    public Historique() {
        this.morceauxEcoutes = new ArrayList<>();
    }

    public void ajouterMorceau(Morceau morceau) {
        morceauxEcoutes.add(morceau);
    }

    public ArrayList<Morceau> getMorceauxEcoutes() {
        return morceauxEcoutes;
    }
}