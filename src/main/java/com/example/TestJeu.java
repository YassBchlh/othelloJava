package com.example;

public class TestJeu {
    public static void main(String[] args) {
        GestionPlateau plateau = new GestionPlateau();

        System.out.println("=== TEST INITIALISATION ===");
        System.out.println("Position initiale:");
        afficherPlateau(plateau);

        System.out.println("\n=== TEST COUPS VALIDES NOIRS ===");
        var coupsNoirs = plateau.getCoupsValides("B");
        System.out.println("Nombre de coups valides pour les noirs: " + coupsNoirs.size());
        for (Case c : coupsNoirs) {
            System.out.println("  - (" + c.getI() + ", " + c.getJ() + ")");
        }

        System.out.println("\n=== TEST COUPS VALIDES BLANCS ===");
        var coupsBlancs = plateau.getCoupsValides("W");
        System.out.println("Nombre de coups valides pour les blancs: " + coupsBlancs.size());
        for (Case c : coupsBlancs) {
            System.out.println("  - (" + c.getI() + ", " + c.getJ() + ")");
        }

        // Jouer un coup noir
        if (!coupsNoirs.isEmpty()) {
            Case premierCoup = coupsNoirs.get(0);
            System.out.println("\n=== JOUER COUP NOIR (" + premierCoup.getI() + ", " + premierCoup.getJ() + ") ===");
            plateau.jouerCoup(plateau.getPlateau(), premierCoup.getI(), premierCoup.getJ(), "B");
            afficherPlateau(plateau);

            System.out.println("\nJetons noirs: " + plateau.compterJetons(plateau.getPlateau(), "B"));
            System.out.println("Jetons blancs: " + plateau.compterJetons(plateau.getPlateau(), "W"));
        }
    }

    private static void afficherPlateau(GestionPlateau gestion) {
        String[][] plateau = gestion.getPlateau();
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 8; j++) {
                String cell = plateau[i][j];
                if (cell == null || cell.isEmpty()) {
                    System.out.print(". ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }
}
