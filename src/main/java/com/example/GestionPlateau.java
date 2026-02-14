package com.example;

import java.util.ArrayList;

public class GestionPlateau {
    private String[][] plateau;
    private ArrayList<Case> cases;

    // Matrice des poids pour l'évaluation des positions
    private static final int[][] POIDS_POSITIONS = {
            { 100, -20, 10, 5, 5, 10, -20, 100 },
            { -20, -50, -2, -2, -2, -2, -50, -20 },
            { 10, -2, 5, 1, 1, 5, -2, 10 },
            { 5, -2, 1, 0, 0, 1, -2, 5 },
            { 5, -2, 1, 0, 0, 1, -2, 5 },
            { 10, -2, 5, 1, 1, 5, -2, 10 },
            { -20, -50, -2, -2, -2, -2, -50, -20 },
            { 100, -20, 10, 5, 5, 10, -20, 100 }
    };

    public GestionPlateau() {
        plateau = new String[8][8];
        cases = new ArrayList<>();
        initialiserPlateau();

    }

    public void initialiserPlateau() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plateau[i][j] = null;
            }
        }
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
    public boolean estCoupValide(String[][] plateau, int i, int j, String couleur) {
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
            if (peutCapturerDansDirection(plateau, i, j, dir[0], dir[1], couleur)) {
                return true;
            }
        }

        return false;
    }

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
            if (peutCapturerDansDirection(plateau, i, j, dir[0], dir[1], couleur)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si on peut capturer des pièces dans une direction donnée
     */
    private boolean peutCapturerDansDirection(String[][] plateau, int i, int j, int di, int dj, String couleur) {
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
    public ArrayList<Case> getCoupsValides(String[][] plateau, String couleur) {
        ArrayList<Case> coupsValides = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (estCoupValide(plateau, i, j, couleur)) {
                    coupsValides.add(new Case(i, j));
                }
            }
        }

        return coupsValides;
    }

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
    public void jouerCoup(String[][] plateau, int i, int j, String couleur) {
        plateau[i][j] = couleur;

        // Capturer dans toutes les directions
        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            capturerDansDirection(plateau, i, j, dir[0], dir[1], couleur);
        }
    }

    /**
     * Capture les pièces dans une direction donnée
     */
    private void capturerDansDirection(String[][] plateau, int i, int j, int di, int dj, String couleur) {
        if (!peutCapturerDansDirection(plateau, i, j, di, dj, couleur)) {
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
    public int compterJetons(String[][] plateau, String couleur) {
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

    /**
     * Vérifie si la partie est terminée (aucun coup valide pour les deux joueurs)
     * 
     * @return true si la partie est terminée
     */
    public boolean estPartieTerminee() {
        return getCoupsValides(plateau, "B").isEmpty() && getCoupsValides(plateau, "W").isEmpty();
    }

    private int evaluationPosition(String[][] plateau, String couleur) {
        String adversaire = couleur.equals("B") ? "W" : "B";
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] != null && plateau[i][j].equals(couleur)) {
                    score += POIDS_POSITIONS[i][j];
                } else if (plateau[i][j] != null && plateau[i][j].equals(adversaire)) {
                    score -= POIDS_POSITIONS[i][j];
                }
            }
        }
        return score;
    }

    private int parite(String[][] plateau, String couleur) {
        return compterJetons(plateau, couleur) - compterJetons(plateau, couleur.equals("B") ? "W" : "B");
    }

    private int mobilite(String[][] plateau, String couleur) {
        int mobiliteCouleur = getCoupsValides(plateau, couleur).size();
        String adversaire = couleur.equals("B") ? "W" : "B";
        int mobiliteAdversaire = getCoupsValides(plateau, adversaire).size();

        return mobiliteCouleur - mobiliteAdversaire;
    }

    // Compter le nombre de coins de MAX par rapport à ceux de min.
    private int nbCoins(String[][] plateau, String couleur) {
        int nbCoins = 0;
        if (plateau[0][0] != null && plateau[0][0].equals(couleur)) {
            nbCoins++;
        }
        if (plateau[0][7] != null && plateau[0][7].equals(couleur)) {
            nbCoins++;
        }
        if (plateau[7][0] != null && plateau[7][0].equals(couleur)) {
            nbCoins++;
        }
        if (plateau[7][7] != null && plateau[7][7].equals(couleur)) {
            nbCoins++;
        }
        return nbCoins;
    }

    private int coins(String[][] plateau, String couleur) {
        return nbCoins(plateau, couleur) - nbCoins(plateau, couleur.equals("B") ? "W" : "B");
    }

    public int evaluation(String[][] plateau, String couleur) {
        return compterJetons(plateau, couleur) - compterJetons(plateau, couleur.equals("B") ? "W" : "B");
    }

    private int nbPionsTotal(String[][] plateau) {
        return compterJetons(plateau, "B") + compterJetons(plateau, "W");
    }

    /**
     * Fonction d'évaluation optimisée pour l'IA difficile
     * Combine : position, mobilité, parité, coins, stabilité, frontière
     */
    public int fonctionEvaluationDifficile(String[][] plateau, String couleur) {
        int nbPionsTotal = nbPionsTotal(plateau);
        int w1 = 0;
        int w2 = 0;
        int w3 = 0;
        int w4 = 0;

        // Phase de début (0-20 pions) : Mobilité et position prioritaires
        if (nbPionsTotal < 20) {
            w1 += 10 * evaluationPosition(plateau, couleur); // Position importante
            w2 += 50 * mobilite(plateau, couleur); // Mobilité cruciale
            w3 += -10 * parite(plateau, couleur); // Parité modérée
            w4 += 1000 * coins(plateau, couleur); // Coins pas encore décisifs
        }
        // Phase de milieu (20-50 pions) : Équilibre entre tous les critères
        else if (nbPionsTotal < 50) {
            w1 += 30 * evaluationPosition(plateau, couleur);
            w2 += 20 * mobilite(plateau, couleur);
            w3 += 10 * parite(plateau, couleur);
            w4 += 1000 * coins(plateau, couleur); // Coins deviennent importants

        }
        // Phase de fin (50+ pions) : Parité, coins et stabilité dominants
        else {
            w1 += 5 * evaluationPosition(plateau, couleur);
            w2 += 20 * mobilite(plateau, couleur); // Toujours important !
            w3 += 500 * parite(plateau, couleur); // Parité cruciale
            w4 += 1000 * coins(plateau, couleur); // Coins très importants

        }

        return w1 + w2 + w3 + w4;
    }

    private ArrayList<Case> trier(ArrayList<Case> coupsValides) {
        ArrayList<Case> copie = new ArrayList<>(coupsValides);
        // Tri par ordre décroissant de poids
        for (int i = 0; i < copie.size() - 1; i++) {
            for (int j = 0; j < copie.size() - 1 - i; j++) {
                Case c1 = copie.get(j);
                Case c2 = copie.get(j + 1);
                int poids1 = POIDS_POSITIONS[c1.getI()][c1.getJ()];
                int poids2 = POIDS_POSITIONS[c2.getI()][c2.getJ()];
                if (poids1 < poids2) {
                    copie.set(j, c2);
                    copie.set(j + 1, c1);
                }
            }
        }
        return copie;
    }

    /**
     * Algorithme MinMax avec élagage alpha-beta pour l'IA difficile
     * 
     * @param plateauTemp   Le plateau à évaluer
     * @param alpha         Meilleure valeur pour le joueur maximisant
     * @param beta          Meilleure valeur pour le joueur minimisant
     * @param profondeur    Profondeur de recherche restante
     * @param estMaximisant true si c'est le tour de l'IA (B), false si c'est le
     *                      tour de l'adversaire (W)
     * @return Le score évalué
     */
    public int minMaxDifficile(String[][] plateauTemp, int alpha, int beta, int profondeur, boolean estMaximisant,
            String couleur) {
        // Condition d'arrêt : profondeur 0 ou partie terminée
        if (profondeur == 0) {
            return fonctionEvaluationDifficile(plateauTemp, couleur);
        }
        String adv = couleur.equals("B") ? "W" : "B";
        String joueur = estMaximisant ? couleur : adv;

        ArrayList<Case> coupsValides = getCoupsValides(plateauTemp, joueur);

        // Si aucun coup valide, passer le tour
        if (coupsValides.isEmpty()) {
            String adversaire = estMaximisant ? adv : couleur;
            ArrayList<Case> coupsAdversaire = getCoupsValides(plateauTemp, adversaire);

            // Si l'adversaire n'a pas de coups non plus, la partie est terminée
            if (coupsAdversaire.isEmpty()) {
                return fonctionEvaluationDifficile(plateauTemp, couleur);
            }

            // Sinon, passer le tour
            return minMaxDifficile(plateauTemp, alpha, beta, profondeur - 1, !estMaximisant, couleur);
        }

        if (estMaximisant) {
            // Phase de maximisation (IA joue)
            int maxScore = Integer.MIN_VALUE;
            for (Case c : trier(coupsValides)) {
                // Créer une copie du plateau
                String[][] copie = copiePlateau(plateauTemp);

                // Jouer le coup sur la copie
                jouerCoup(copie, c.getI(), c.getJ(), joueur);

                // Évaluer récursivement
                int score = minMaxDifficile(copie, alpha, beta, profondeur - 1, false, couleur);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);

                // Élagage alpha-beta
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            // Phase de minimisation (adversaire joue)
            int minScore = Integer.MAX_VALUE;
            for (Case c : trier(coupsValides)) {
                // Créer une copie du plateau
                String[][] copie = copiePlateau(plateauTemp);

                // Jouer le coup sur la copie
                jouerCoup(copie, c.getI(), c.getJ(), joueur);

                // Évaluer récursivement
                int score = minMaxDifficile(copie, alpha, beta, profondeur - 1, true, couleur);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);

                // Élagage alpha-beta
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }

    public void jouerCoupDifficile(String[][] plateauTemp, String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        ArrayList<Case> coupsValides = getCoupsValides(plateauTemp, couleur);

        for (Case c : coupsValides) {
            // Créer une copie du plateau
            String[][] copie = copiePlateau(plateau);

            // Jouer le coup sur la copie
            jouerCoup(copie, c.getI(), c.getJ(), couleur);

            // Évaluer le coup avec MinMax (profondeur 5 pour IA difficile)
            // false car après le coup de l'IA, c'est au tour de l'adversaire (minimisant)
            int score = minMaxDifficile(copie, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, false, couleur);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }

        if (meilleurCoup != null) {
            System.out.println("Un meilleur coup a été trouvé : " + meilleurCoup.getI() + ", " + meilleurCoup.getJ()
                    + " (score: " + meilleurScore + ")");
            jouerCoup(plateau, meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    public void jouerCoupDifficile(String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        ArrayList<Case> coupsValides = getCoupsValides(couleur);

        for (Case c : coupsValides) {
            // Créer une copie du plateau
            String[][] copie = copiePlateau(plateau);

            // Jouer le coup sur la copie
            jouerCoup(copie, c.getI(), c.getJ(), couleur);

            // Évaluer le coup avec MinMax (profondeur 5 pour IA difficile)
            // false car après le coup de l'IA, c'est au tour de l'adversaire (minimisant)
            int score = minMaxDifficile(copie, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, false, couleur);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }

        if (meilleurCoup != null) {
            System.out.println("Un meilleur coup a été trouvé : " + meilleurCoup.getI() + ", " + meilleurCoup.getJ()
                    + " (score: " + meilleurScore + ")");
            jouerCoup(plateau, meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    public void minMaxFacile(String[][] plateauTemp, String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;
        for (Case c : getCoupsValides(plateauTemp, couleur)) {
            String[][] copie = copiePlateau(plateauTemp);
            jouerCoup(copie, c.getI(), c.getJ(), couleur);
            int score = evaluation(copie, couleur);
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }
        if (meilleurCoup != null) {
            System.out.println("Un meilleur coup a été trouvé : " + meilleurCoup.getI() + ", " + meilleurCoup.getJ());
            jouerCoup(plateau, meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    public void minMaxFacile(String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;
        for (Case c : getCoupsValides(couleur)) {
            String[][] copie = copiePlateau(plateau);
            jouerCoup(copie, c.getI(), c.getJ(), couleur);
            int score = evaluation(copie, couleur);
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }
        if (meilleurCoup != null) {
            System.out.println("Un meilleur coup a été trouvé : " + meilleurCoup.getI() + ", " + meilleurCoup.getJ());
            jouerCoup(plateau, meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    public String[][] copiePlateau(String[][] plateau) {
        String[][] copie = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copie[i][j] = plateau[i][j];
            }
        }
        return copie;
    }

    public String[][] getPlateau() {
        return plateau;
    }
}
