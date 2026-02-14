package com.example;

import java.util.Scanner;

public class TournoiIA2 {

    private GestionPlateau gestionPlateau;
    private int victoiresIAFacile = 0;
    private int victoiresIADifficile = 0;
    private int victoiresIANoirs = 0;
    private int victoiresIABlancs = 0;
    private int matchsNuls = 0;

    public TournoiIA2() {
        gestionPlateau = new GestionPlateau();
    }

    /*
     * public void jouer() {
     * int nbParties = 0;
     * 
     * while (nbParties < 50) {
     * System.out.println("\n=== PARTIE " + (nbParties + 1) + " ===");
     * String joueurActuel = "B";
     * String[][] plateau = gestionPlateau.getPlateau();
     * while (!gestionPlateau.estPartieTerminee()) {
     * if (!gestionPlateau.getCoupsValides(plateau, joueurActuel).isEmpty()) {
     * // Les DEUX IA utilisent minMaxFacile
     * System.out.println("IA Facile joue (" + joueurActuel + ")");
     * gestionPlateau.minMaxFacile(plateau, joueurActuel);
     * }
     * joueurActuel = joueurActuel.equals("B") ? "W" : "B";
     * }
     * 
     * // Compter les résultats
     * int scoreNoirs = gestionPlateau.compterJetons(plateau, "B");
     * int scoreBlancs = gestionPlateau.compterJetons(plateau, "W");
     * 
     * System.out.println("Score : Noirs = " + scoreNoirs + ", Blancs = " +
     * scoreBlancs);
     * 
     * if (scoreNoirs > scoreBlancs) {
     * victoiresIANoirs++;
     * System.out.println("✓ IA Noirs gagne");
     * } else if (scoreBlancs > scoreNoirs) {
     * victoiresIABlancs++;
     * System.out.println("✓ IA Blancs gagne");
     * } else {
     * matchsNuls++;
     * System.out.println("= Match nul");
     * }
     * 
     * nbParties++;
     * gestionPlateau.initialiserPlateau(); // Réinitialiser le plateau pour la
     * prochaine partie
     * plateau = gestionPlateau.getPlateau();
     * }
     * 
     * System.out.println("\n=== RÉSULTATS FINAUX (50 parties) ===");
     * System.out.println("Victoires IA Noirs : " + victoiresIANoirs);
     * System.out.println("Victoires IA Blancs : " + victoiresIABlancs);
     * System.out.println("Matchs Nuls : " + matchsNuls);
     * }
     */
    public void jouer() {
        int nbParties = 0;
        String couleurIAFacile = "B";
        String couleurIADifficile = "W";

        while (nbParties < 50) {
            System.out.println("\n=== PARTIE " + (nbParties + 1) + " ===");
            System.out.println("IA Facile joue : " + couleurIAFacile);
            System.out.println("IA Difficile joue : " + couleurIADifficile);

            String joueurActuel = "B";
            int coups = 0; // ✅ Compter les coups
            String[][] plateau = gestionPlateau.getPlateau();
            while (!gestionPlateau.estPartieTerminee()) {
                if (!gestionPlateau.getCoupsValides(plateau, joueurActuel).isEmpty()) {
                    if (joueurActuel.equals(couleurIAFacile)) {
                        System.out.println("Coup " + (++coups) + " - IA Facile (" + joueurActuel +
                                ")");
                        gestionPlateau.minMaxFacile(plateau, joueurActuel);
                    } else {
                        System.out.println("Coup " + (++coups) + " - IA Difficile (" + joueurActuel +
                                ")");
                        gestionPlateau.jouerCoupDifficile(plateau, joueurActuel);
                    }
                }
                joueurActuel = joueurActuel.equals("B") ? "W" : "B";
            }
            // Compter les résultats
            int scoreIAFacile = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), couleurIAFacile);
            int scoreIADifficile = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), couleurIADifficile);

            System.out.println("Score final : IA Facile = " + scoreIAFacile + ", IA Difficile = " + scoreIADifficile);

            if (scoreIAFacile > scoreIADifficile) {
                victoiresIAFacile++;
                System.out.println("✓ IA Facile gagne");
            } else if (scoreIAFacile < scoreIADifficile) {
                victoiresIADifficile++;
                System.out.println("✓ IA Difficile gagne");
            } else {
                matchsNuls++;
                System.out.println("= Match nul");
            }

            nbParties++;
            gestionPlateau.initialiserPlateau();

            couleurIAFacile = couleurIAFacile.equals("B") ? "W" : "B";
            couleurIADifficile = couleurIADifficile.equals("B") ? "W" : "B";
        }

        System.out.println("\n=== RÉSULTATS FINAUX ===");
        System.out.println("Victoires IA Facile : " + victoiresIAFacile);
        System.out.println("Victoires IA Difficile : " + victoiresIADifficile);
        System.out.println("Matchs Nuls : " + matchsNuls);
    }

    public static void main(String[] args) {
        TournoiIA2 tournoi = new TournoiIA2();
        tournoi.jouer();
    }
}
