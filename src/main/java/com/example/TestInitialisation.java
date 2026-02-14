package com.example;

public class TestInitialisation {
    public static void main(String[] args) {
        GestionPlateau plateau = new GestionPlateau();

        System.out.println("=== VÉRIFICATION INITIALISATION ===");
        System.out.println("Position initiale standard Othello:");
        System.out.println("  0 1 2 3 4 5 6 7");
        System.out.println("0 . . . . . . . .");
        System.out.println("1 . . . . . . . .");
        System.out.println("2 . . . . . . . .");
        System.out.println("3 . . . W B . . .");
        System.out.println("4 . . . B W . . .");
        System.out.println("5 . . . . . . . .");
        System.out.println("6 . . . . . . . .");
        System.out.println("7 . . . . . . . .");

        System.out.println("\nVotre initialisation:");
        afficherPlateau(plateau);

        System.out.println("\nCode d'initialisation:");
        System.out.println("plateau[3][3] = \"W\";");
        System.out.println("plateau[3][4] = \"B\";");
        System.out.println("plateau[4][3] = \"B\";");
        System.out.println("plateau[4][4] = \"W\";");
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
