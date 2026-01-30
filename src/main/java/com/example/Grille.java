package com.example;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Grille extends GridPane {
    private Button[][] cases;
    private GestionPlateau gestionPlateau;
    private String joueurActuel; // "B" pour noir, "W" pour blanc

    public Grille() {
        gestionPlateau = new GestionPlateau();
        joueurActuel = "B"; // Les noirs commencent

        cases = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int ligne = i;
                final int colonne = j;

                cases[i][j] = new Button();
                GridPane.setRowIndex(cases[i][j], i);
                GridPane.setColumnIndex(cases[i][j], j);
                cases[i][j].setPrefSize(50, 50);
                cases[i][j].setStyle("-fx-background-color: green;-fx-border-color: black; -fx-border-width: 1px;");

                // Ajouter un gestionnaire de clic
                cases[i][j].setOnAction(e -> jouerCoup(ligne, colonne));

                this.getChildren().add(cases[i][j]);
            }
        }

        // Initialiser l'affichage
        mettreAJourAffichage();
    }

    private void jouerCoup(int i, int j) {
        // Vérifier si le coup est valide
        if (gestionPlateau.estCoupValide(i, j, joueurActuel)) {
            // Jouer le coup
            gestionPlateau.jouerCoup(i, j, joueurActuel);

            // Changer de joueur
            joueurActuel = joueurActuel.equals("B") ? "W" : "B";

            // Mettre à jour l'affichage
            mettreAJourAffichage();

            // Vérifier si le joueur suivant a des coups valides
            ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(joueurActuel);
            if (coupsValides.isEmpty()) {
                // Passer le tour
                joueurActuel = joueurActuel.equals("B") ? "W" : "B";
                mettreAJourAffichage();
            }
        }
    }

    private void mettreAJourAffichage() {
        // Obtenir les coups valides pour le joueur actuel
        ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(joueurActuel);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String contenu = gestionPlateau.getCase(i, j);

                // Afficher les pièces
                if (contenu != null && !contenu.isEmpty()) {
                    Color couleur = contenu.equals("B") ? Color.BLACK : Color.WHITE;
                    cases[i][j].setGraphic(new Circle(15, couleur));
                } else {
                    // Vérifier si c'est un coup valide
                    boolean estValide = false;
                    for (Case c : coupsValides) {
                        if (c.getI() == i && c.getJ() == j) {
                            estValide = true;
                            break;
                        }
                    }

                    if (estValide) {
                        // Afficher un cercle semi-transparent pour indiquer un coup valide
                        Color couleurJoueur = joueurActuel.equals("B") ? Color.BLACK : Color.WHITE;
                        Circle hint = new Circle(15, couleurJoueur);
                        hint.setOpacity(0.3);
                        cases[i][j].setGraphic(hint);
                    } else {
                        cases[i][j].setGraphic(null);
                    }
                }
            }
        }
    }
}
