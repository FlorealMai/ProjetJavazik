import controleur.ControleurMain;
import vue.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choisissez le mode d'affichage :");
        System.out.println("1. Terminal");
        System.out.println("2. Interface graphique");
        System.out.print("Choix : ");

        int choix = Integer.parseInt(sc.nextLine());

        IVueMenuPrincipal vueMenu;
        IVueCatalog vueCatalog;
        IVueAdmin vueAdmin;

        if (choix == 2) {
            vueMenu = new VueMenuPrincipalSwing();
            vueCatalog = new VueCatalogSwing();
            vueAdmin = new VueAdminSwing();
        } else {
            vueMenu = new VueMenuPrincipal();
            vueCatalog = new VueCatalog();
            vueAdmin = new VueAdmin();
        }

        ControleurMain controleur = new ControleurMain(vueMenu, vueCatalog, vueAdmin);
        controleur.lancer();
    }
}