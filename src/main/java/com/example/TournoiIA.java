package com.example;

import java.util.ArrayList;

/**
 * Classe pour faire jouer un tournoi entre deux IA sans interface graphique
 */
public class TournoiIA {
    private GestionPlateau gestionPlateau;
    private int victoires_IA_Facile = 0;
    private int victoires_IA_Difficile = 0;
    private int matchsNuls = 0;

    public TournoiIA() {
        gestionPlateau = new GestionPlateau();
    }

    /**
     * Joue une partie complète entre les deux IA
     * 
     * @param iaNoirDifficile true si l'IA difficile joue les noirs, false si elle
     *                        joue les blancs
     * @return "B" si les noirs gagnent, "W" si les blancs gagnent, "N" si match nul
     */
    public String jouerPartie(boolean iaNoirDifficile) {
        gestionPlateau.initialiserPlateau();
        String joueurActuel = "B"; // Les noirs commencent
        int coups = 0;
        int maxCoups = 100; // Limite de sécurité pour éviter les boucles infinies
        int toursPassesConsecutifs = 0;
        boolean premierepartie = false; // Set to true for debugging

        while (!gestionPlateau.estPartieTerminee() && coups < maxCoups) {
            ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(joueurActuel);

            if (!coupsValides.isEmpty()) {
                toursPassesConsecutifs = 0;

                if (premierepartie && coups < 3) {
                    System.out.println("\n=== Coup " + (coups + 1) + " - Joueur: " + joueurActuel + " ===");
                    System.out.println("Coups valides: " + coupsValides.size());
                    for (Case c : coupsValides) {
                        System.out.println("  (" + c.getI() + ", " + c.getJ() + ")");
                    }
                }

                // Déterminer quelle IA joue
                boolean iaFacileJoue = (joueurActuel.equals("B") && !iaNoirDifficile) ||
                        (joueurActuel.equals("W") && iaNoirDifficile);

                if (iaFacileJoue) {
                    // IA Facile joue
                    jouerCoupIAFacile(joueurActuel);
                } else {
                    // IA Difficile joue
                    jouerCoupIADifficile(joueurActuel);
                }

                if (premierepartie && coups < 3) {
                    System.out.println("Jetons B: " + gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "B") +
                            ", W: " + gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "W"));
                }

                coups++;
            } else {
                // Pas de coups valides, passer le tour
                toursPassesConsecutifs++;
                if (toursPassesConsecutifs >= 2) {
                    // Les deux joueurs ont passé, fin de partie
                    break;
                }
            }

            // Changer de joueur
            joueurActuel = joueurActuel.equals("B") ? "W" : "B";
        }

        // Déterminer le gagnant
        int jetonsNoirs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "B");
        int jetonsBlancs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "W");

        if (jetonsNoirs > jetonsBlancs) {
            return "B";
        } else if (jetonsBlancs > jetonsNoirs) {
            return "W";
        } else {
            return "N"; // Match nul
        }
    }

    /**
     * Joue un coup avec l'IA facile pour une couleur donnée
     */
    private void jouerCoupIAFacile(String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        for (Case c : gestionPlateau.getCoupsValides(couleur)) {
            String[][] copie = gestionPlateau.copiePlateau(gestionPlateau.getPlateau());
            gestionPlateau.jouerCoup(copie, c.getI(), c.getJ(), couleur);

            // Évaluation simple : nombre de pions de notre couleur - nombre de pions
            // adverses
            int jetonsIA = gestionPlateau.compterJetons(copie, couleur);
            String adversaire = couleur.equals("B") ? "W" : "B";
            int jetonsAdversaire = gestionPlateau.compterJetons(copie, adversaire);
            int score = jetonsIA - jetonsAdversaire;

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }

        if (meilleurCoup != null) {
            gestionPlateau.jouerCoup(gestionPlateau.getPlateau(), meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    /**
     * Joue un coup avec l'IA difficile pour une couleur donnée
     */
    private void jouerCoupIADifficile(String couleur) {
        Case meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(couleur);

        for (Case c : coupsValides) {
            String[][] copie = gestionPlateau.copiePlateau(gestionPlateau.getPlateau());
            gestionPlateau.jouerCoup(copie, c.getI(), c.getJ(), couleur);

            // Profondeur augmentée à 5 pour une IA plus forte
            // Après que l'IA joue, c'est au tour de l'adversaire (minimisant)
            int score = minMaxDifficile(copie, Integer.MIN_VALUE, Integer.MAX_VALUE, 5, false, couleur);

            // L'IA cherche toujours à maximiser son score, quelle que soit sa couleur
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = c;
            }
        }

        if (meilleurCoup != null) {
            gestionPlateau.jouerCoup(gestionPlateau.getPlateau(), meilleurCoup.getI(), meilleurCoup.getJ(), couleur);
        }
    }

    /**
     * Algorithme MinMax adapté pour fonctionner avec les deux couleurs
     */
    private int minMaxDifficile(String[][] plateauTemp, int alpha, int beta, int profondeur, boolean estMaximisant,
            String couleurIA) {
        if (profondeur == 0) {
            return evaluationPosition(plateauTemp, couleurIA);
        }

        String joueur = estMaximisant ? couleurIA : (couleurIA.equals("B") ? "W" : "B");
        String[][] plateauSauvegarde = gestionPlateau.getPlateau();

        ArrayList<Case> coupsValides = getCoupsValidesTemp(plateauTemp, joueur);

        if (coupsValides.isEmpty()) {
            String adversaire = joueur.equals("B") ? "W" : "B";
            ArrayList<Case> coupsAdversaire = getCoupsValidesTemp(plateauTemp, adversaire);

            if (coupsAdversaire.isEmpty()) {
                return gestionPlateau.fonctionEvaluationDifficile(plateauTemp, couleurIA);
            }

            return minMaxDifficile(plateauTemp, alpha, beta, profondeur - 1, !estMaximisant, couleurIA);
        }

        if (estMaximisant) {
            int maxScore = Integer.MIN_VALUE;
            for (Case c : coupsValides) {
                String[][] copie = copierPlateau(plateauTemp);
                jouerCoupTemp(copie, c.getI(), c.getJ(), joueur);
                int score = minMaxDifficile(copie, alpha, beta, profondeur - 1, false, couleurIA);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha)
                    break;
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Case c : coupsValides) {
                String[][] copie = copierPlateau(plateauTemp);
                jouerCoupTemp(copie, c.getI(), c.getJ(), joueur);
                int score = minMaxDifficile(copie, alpha, beta, profondeur - 1, true, couleurIA);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha)
                    break;
            }
            return minScore;
        }
    }

    private int evaluationPosition(String[][] plateau, String couleurIA) {
        int score = 0;
        int[][] POIDS_POSITIONS = {
                { 100, -20, 10, 5, 5, 10, -20, 100 },
                { -20, -50, -2, -2, -2, -2, -50, -20 },
                { 10, -2, 5, 1, 1, 5, -2, 10 },
                { 5, -2, 1, 0, 0, 1, -2, 5 },
                { 5, -2, 1, 0, 0, 1, -2, 5 },
                { 10, -2, 5, 1, 1, 5, -2, 10 },
                { -20, -50, -2, -2, -2, -2, -50, -20 },
                { 100, -20, 10, 5, 5, 10, -20, 100 }
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] != null && plateau[i][j].equals(couleurIA)) {
                    score += POIDS_POSITIONS[i][j];
                } else if (plateau[i][j] != null && !plateau[i][j].equals(couleurIA)) {
                    score -= POIDS_POSITIONS[i][j];
                }
            }
        }
        return score;
    }

    private ArrayList<Case> getCoupsValidesTemp(String[][] plateau, String couleur) {
        ArrayList<Case> coupsValides = new ArrayList<>();
        String[][] plateauSauvegarde = gestionPlateau.getPlateau();

        // Temporairement utiliser le plateau fourni
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (estCoupValideTemp(plateau, i, j, couleur)) {
                    coupsValides.add(new Case(i, j));
                }
            }
        }

        return coupsValides;
    }

    private boolean estCoupValideTemp(String[][] plateau, int i, int j, String couleur) {
        if (plateau[i][j] != null && !plateau[i][j].isEmpty()) {
            return false;
        }

        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            if (peutCapturerDansDirection(plateau, i, j, dir[0], dir[1], couleur)) {
                return true;
            }
        }

        return false;
    }

    private boolean peutCapturerDansDirection(String[][] plateau, int i, int j, int di, int dj, String couleur) {
        String adversaire = couleur.equals("W") ? "B" : "W";
        int ligne = i + di;
        int colonne = j + dj;
        boolean aTrouveAdversaire = false;

        while (ligne >= 0 && ligne < 8 && colonne >= 0 && colonne < 8) {
            String caseActuelle = plateau[ligne][colonne];

            if (caseActuelle == null || caseActuelle.isEmpty()) {
                return false;
            }

            if (caseActuelle.equals(adversaire)) {
                aTrouveAdversaire = true;
                ligne += di;
                colonne += dj;
                continue;
            }

            if (caseActuelle.equals(couleur)) {
                return aTrouveAdversaire;
            }
        }

        return false;
    }

    private void jouerCoupTemp(String[][] plateau, int i, int j, String couleur) {
        plateau[i][j] = couleur;

        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            capturerDansDirection(plateau, i, j, dir[0], dir[1], couleur);
        }
    }

    private void capturerDansDirection(String[][] plateau, int i, int j, int di, int dj, String couleur) {
        if (!peutCapturerDansDirection(plateau, i, j, di, dj, couleur)) {
            return;
        }

        String adversaire = couleur.equals("W") ? "B" : "W";
        int ligne = i + di;
        int colonne = j + dj;

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

    private String[][] copierPlateau(String[][] plateau) {
        String[][] copie = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copie[i][j] = plateau[i][j];
            }
        }
        return copie;
    }

    /**
     * Lance un tournoi de 50 parties
     */
    public void lancerTournoi() {
        System.out.println("=== TOURNOI ENTRE IA FACILE ET IA DIFFICILE ===");
        System.out.println("50 parties vont être jouées...\n");

        long tempsDebut = System.currentTimeMillis();

        for (int i = 1; i <= 50; i++) {
            // Alterner qui joue les noirs (avantage du premier coup)
            boolean iaNoirDifficile = (i % 2 == 0);

            System.out.print("Partie " + i + "/50 - ");
            if (iaNoirDifficile) {
                System.out.print("IA Difficile (Noirs) vs IA Facile (Blancs)... ");
            } else {
                System.out.print("IA Facile (Noirs) vs IA Difficile (Blancs)... ");
            }

            String resultat = jouerPartie(iaNoirDifficile);

            // Debug pour la première partie
            if (i == 1) {
                System.out.println("\n=== DEBUG PREMIÈRE PARTIE ===");
                System.out.println("Plateau final:");
                afficherPlateauDebug();
                int noirsFinal = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "B");
                int blancsFinal = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "W");
                System.out.println("Jetons noirs: " + noirsFinal);
                System.out.println("Jetons blancs: " + blancsFinal);
                System.out.println("=== FIN DEBUG ===\n");
            }

            // Compter les victoires
            if (resultat.equals("N")) {
                matchsNuls++;
                System.out.println("Match nul");
            } else if ((resultat.equals("B") && !iaNoirDifficile) || (resultat.equals("W") && iaNoirDifficile)) {
                victoires_IA_Facile++;
                System.out.println("Victoire IA Facile");
            } else {
                victoires_IA_Difficile++;
                System.out.println("Victoire IA Difficile");
            }
        }

        long tempsFin = System.currentTimeMillis();
        long duree = (tempsFin - tempsDebut) / 1000; // en secondes

        // Afficher les résultats
        System.out.println("\n=== RÉSULTATS DU TOURNOI ===");
        System.out.println("Victoires IA Facile : " + victoires_IA_Facile);
        System.out.println("Victoires IA Difficile : " + victoires_IA_Difficile);
        System.out.println("Matchs nuls : " + matchsNuls);
        System.out.println("\nDurée totale : " + duree + " secondes");
        System.out.println("Temps moyen par partie : " + (duree / 50.0) + " secondes");
    }

    private void afficherPlateauDebug() {
        String[][] plateau = gestionPlateau.getPlateau();
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

    public static void main(String[] args) {
        TournoiIA tournoi = new TournoiIA();
        tournoi.lancerTournoi();
    }
}
