package com.example;

import java.util.ArrayList;

public class GestionPlateau {
    private String[][] plateau;
    private ArrayList<Case> cases;

    public GestionPlateau() {
        plateau = new String[8][8];
        cases = new ArrayList<>();
        initialiserPlateau();

    }

    public void initialiserPlateau() {
        plateau[3][3] = "W";
        plateau[3][4] = "B";
        plateau[4][3] = "B";
        plateau[4][4] = "W";
    }

    public void setCase(int i, int j, String couleur) {
        plateau[i][j] = couleur;
    }

    public String getCase(int i, int j) {
        return plateau[i][j];
    }

    public boolean checkEmpty(int i, int j) {
        return plateau[i][j] == null || plateau[i][j].isEmpty();
    }

    /**
     * Vérifie si un coup est valide pour un joueur donné
     * 
     * @param i       ligne
     * @param j       colonne
     * @param couleur "W" ou "B"
     * @return true si le coup est valide
     */
    public boolean estCoupValide(int i, int j, String couleur) {
        // La case doit être vide
        if (plateau[i][j] != null && !plateau[i][j].isEmpty()) {
            return false;
        }

        // Vérifier dans les 8 directions
        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 }, // haut-gauche, haut, haut-droite
                { 0, -1 }, { 0, 1 }, // gauche, droite
                { 1, -1 }, { 1, 0 }, { 1, 1 } // bas-gauche, bas, bas-droite
        };

        for (int[] dir : directions) {
            if (peutCapturerDansDirection(i, j, dir[0], dir[1], couleur)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si on peut capturer des pièces dans une direction donnée
     */
    private boolean peutCapturerDansDirection(int i, int j, int di, int dj, String couleur) {
        String adversaire = couleur.equals("W") ? "B" : "W";
        int ligne = i + di;
        int colonne = j + dj;
        boolean aTrouveAdversaire = false;

        // Parcourir dans la direction donnée
        while (ligne >= 0 && ligne < 8 && colonne >= 0 && colonne < 8) {
            String caseActuelle = plateau[ligne][colonne];

            // Case vide : pas de capture possible
            if (caseActuelle == null || caseActuelle.isEmpty()) {
                return false;
            }

            // Pièce adversaire : continuer
            if (caseActuelle.equals(adversaire)) {
                aTrouveAdversaire = true;
                ligne += di;
                colonne += dj;
                continue;
            }

            // Pièce de notre couleur
            if (caseActuelle.equals(couleur)) {
                // On peut capturer seulement si on a trouvé au moins une pièce adversaire
                return aTrouveAdversaire;
            }
        }

        return false;
    }

    /**
     * Retourne la liste de tous les coups valides pour un joueur
     */
    public ArrayList<Case> getCoupsValides(String couleur) {
        ArrayList<Case> coupsValides = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (estCoupValide(i, j, couleur)) {
                    coupsValides.add(new Case(i, j));
                }
            }
        }

        return coupsValides;
    }

    /**
     * Joue un coup et retourne les pièces capturées
     */
    public void jouerCoup(int i, int j, String couleur) {
        plateau[i][j] = couleur;

        // Capturer dans toutes les directions
        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            capturerDansDirection(i, j, dir[0], dir[1], couleur);
        }
    }

    /**
     * Capture les pièces dans une direction donnée
     */
    private void capturerDansDirection(int i, int j, int di, int dj, String couleur) {
        if (!peutCapturerDansDirection(i, j, di, dj, couleur)) {
            return;
        }

        String adversaire = couleur.equals("W") ? "B" : "W";
        int ligne = i + di;
        int colonne = j + dj;

        // Retourner toutes les pièces adversaires jusqu'à notre couleur
        while (ligne >= 0 && ligne < 8 && colonne >= 0 && colonne < 8) {
            if (plateau[ligne][colonne].equals(adversaire)) {
                plateau[ligne][colonne] = couleur;
                ligne += di;
                colonne += dj;
            } else {
                break;
            }
        }
    }

    /**
     * Compte le nombre de jetons d'une couleur donnée
     * 
     * @param couleur "W" ou "B"
     * @return le nombre de jetons de cette couleur
     */
    public int compterJetons(String couleur) {
        int compteur = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] != null && plateau[i][j].equals(couleur)) {
                    compteur++;
                }
            }
        }
        return compteur;
    }

}
