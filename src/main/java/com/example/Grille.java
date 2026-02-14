package com.example;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public class Grille extends VBox {
    private Button[][] cases;
    private GestionPlateau gestionPlateau;
    private String joueurActuel; // "B" pour noir, "W" pour blanc
    private Label labelJetonsNoirs;
    private Label labelJetonsBlancs;
    private GridPane plateauJeu;
    private int choixFinal2; // Stores the AI difficulty level choice
    private String couleurJoueur;
    private String couleurIA;

    public Grille() {
        gestionPlateau = new GestionPlateau();
        joueurActuel = "B"; // Les noirs commencent
        HBox modeJeu = new HBox(30);
        int choix = 0;
        Scanner sc = new Scanner(System.in);
        do {
            menu1();
            choix = sc.nextInt();
            switch (choix) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                default:
                    System.out.println("Veuillez entrer un nombre entre 1 et 4");
                    break;
            }
        } while (choix < 1 || choix > 4);
        final int choixFinal1 = choix; // Store in final variable for use in lambda
        if (choixFinal1 == 2) {
            do {
                menu2();
                choix = sc.nextInt();
            } while (choix < 1 || choix > 3);
            choixFinal2 = choix;
            System.out.println("Quelle est la couleur que vous voulez utiliser (B ou W): ");
            couleurJoueur = sc.next();
            couleurIA = couleurJoueur.equals("B") ? "W" : "B";
            joueurActuel = couleurJoueur.equals("B") ? couleurJoueur : couleurIA;
        } else {
            choixFinal2 = -1; // Default value when not playing against IA
        }

        // Créer le panneau de score
        HBox panneauScore = new HBox(30);
        panneauScore.setAlignment(Pos.CENTER);
        panneauScore.setPadding(new Insets(10));
        panneauScore.setStyle("-fx-background-color: #f0f0f0;");

        // Label pour les jetons noirs
        HBox scoreNoirs = new HBox(10);
        scoreNoirs.setAlignment(Pos.CENTER);
        Circle cercleNoir = new Circle(15, Color.BLACK);
        labelJetonsNoirs = new Label("2");
        labelJetonsNoirs.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        scoreNoirs.getChildren().addAll(cercleNoir, labelJetonsNoirs);

        // Label pour les jetons blancs
        HBox scoreBlancs = new HBox(10);
        scoreBlancs.setAlignment(Pos.CENTER);
        Circle cercleBlanc = new Circle(15, Color.WHITE);
        cercleBlanc.setStroke(Color.BLACK);
        cercleBlanc.setStrokeWidth(1);
        labelJetonsBlancs = new Label("2");
        labelJetonsBlancs.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        scoreBlancs.getChildren().addAll(cercleBlanc, labelJetonsBlancs);

        panneauScore.getChildren().addAll(scoreNoirs, scoreBlancs);

        // Créer le plateau de jeu
        plateauJeu = new GridPane();
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
                cases[i][j].setOnAction(e -> {
                    // Le joueur Blanc joue
                    if (choixFinal1 == 1) {
                        jouerCoup(ligne, colonne);
                    } else if (choixFinal1 == 2) {
                        if (joueurActuel.equals(couleurJoueur)) {
                            jouerCoup(ligne, colonne);
                            // Puis l'IA Noir joue
                            if (choixFinal2 == 1 || choixFinal2 == 2 || choixFinal2 == 3) {
                                jouerCoupIA();
                            }
                        }
                    }
                });

                plateauJeu.getChildren().add(cases[i][j]);
            }
        }

        // Ajouter les composants au VBox principal
        this.getChildren().addAll(modeJeu, panneauScore, plateauJeu);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        // Initialiser l'affichage
        mettreAJourAffichage();

        // L'IA Noir commence (si on joue contre l'IA)
        if (choixFinal2 == 1 || choixFinal2 == 2 || choixFinal2 == 3) {
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                if (couleurIA.equals("B")) {
                    jouerCoupIA();
                }
            });
            pause.play();

        }
    }

    public void menu1() {
        System.out.println("Bienvenue dans le jeu Othello. Voici les options qui s'offrent à vous : \n");
        System.out.println("1. Jouer contre un autre joueur\n");
        System.out.println("2. Jouer contre l'IA\n");
        System.out.println("3. Tournoi entre IA\n");
        System.out.println("4. Quitter\n");
    }

    public void menu2() {
        System.out.println("Bienvenue dans le mode IA, voici les options qui s'offrent à vous : \n");
        System.out.println("1. IA Facile\n");
        System.out.println("2. IA Moyenne\n");
        System.out.println("3. IA Difficile\n");
        System.out.println("4. Retour au menu principal\n");
    }

    private void jouerCoup(int i, int j) {
        // Vérifier si le coup est valide
        if (gestionPlateau.estCoupValide(i, j, joueurActuel)) {
            // Jouer le coup
            gestionPlateau.jouerCoup(gestionPlateau.getPlateau(), i, j, joueurActuel);

            // Changer de joueur
            joueurActuel = joueurActuel.equals("B") ? "W" : "B";

            // Mettre à jour l'affichage
            mettreAJourAffichage();

            // Vérifier si le joueur suivant a des coups valides
            ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(joueurActuel);
            if (coupsValides.isEmpty()) {
                // Passer le tour
                System.out.println("DEBUG jouerCoup: Passage de tour pour " + joueurActuel);
                joueurActuel = joueurActuel.equals("B") ? "W" : "B";
                mettreAJourAffichage();

                // Vérifier si la partie est terminée (aucun des deux joueurs ne peut jouer)
                if (gestionPlateau.estPartieTerminee()) {
                    System.out.println("DEBUG jouerCoup: Partie terminée après passage de tour!");
                    afficherFinDePartie();
                } else {
                    // Si la partie n'est pas terminée et que c'est le tour de l'IA, la faire jouer
                    System.out.println("DEBUG jouerCoup: Tour passé, vérification si l'IA doit jouer");
                    if (joueurActuel.equals(couleurIA)) {
                        System.out.println("DEBUG jouerCoup: C'est le tour de l'IA après passage");
                        jouerCoupIA();
                    }
                }
            }
        }
    }

    private void jouerCoupIA() {
        if (joueurActuel.equals(couleurIA)) {
            // Créer un délai d'une seconde avant que l'IA joue
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                if (choixFinal2 == 1) {
                    gestionPlateau.minMaxFacile(joueurActuel);
                } else if (choixFinal2 == 2) {
                    // IA Moyenne - utilise minMaxFacile pour l'instant
                    gestionPlateau.minMaxFacile(joueurActuel);
                } else if (choixFinal2 == 3) {
                    gestionPlateau.jouerCoupDifficile(joueurActuel);
                }
                joueurActuel = joueurActuel.equals("B") ? "W" : "B";
                mettreAJourAffichage();

                ArrayList<Case> coupsValides = gestionPlateau.getCoupsValides(joueurActuel);
                if (coupsValides.isEmpty()) {
                    // Passer le tour
                    System.out.println("DEBUG IA: Aucun coup valide pour " + joueurActuel + ", passage du tour");
                    joueurActuel = joueurActuel.equals("B") ? "W" : "B";
                    mettreAJourAffichage();

                    // Vérifier si la partie est terminée (les deux joueurs ne peuvent plus jouer)
                    System.out.println("DEBUG IA: Vérification fin de partie après passage de tour");
                    System.out.println("DEBUG IA: Coups valides B: " + gestionPlateau.getCoupsValides("B").size());
                    System.out.println("DEBUG IA: Coups valides W: " + gestionPlateau.getCoupsValides("W").size());
                    if (gestionPlateau.estPartieTerminee()) {
                        System.out.println("DEBUG IA: Affichage fin de partie!");
                        afficherFinDePartie();
                    } else {
                        // Si la partie n'est pas terminée et que c'est de nouveau le tour de l'IA,
                        // rejouer
                        System.out.println("DEBUG IA: Tour passé, vérification si l'IA doit rejouer");
                        if (joueurActuel.equals(couleurIA)) {
                            System.out.println("DEBUG IA: C'est de nouveau le tour de l'IA, relance du coup");
                            jouerCoupIA();
                        }
                    }
                }
            });
            pause.play();
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

        // Mettre à jour les compteurs
        int jetonsNoirs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "B");
        int jetonsBlancs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "W");
        labelJetonsNoirs.setText(String.valueOf(jetonsNoirs));
        labelJetonsBlancs.setText(String.valueOf(jetonsBlancs));
    }

    /**
     * Affiche un message de fin de partie avec le gagnant
     */
    private void afficherFinDePartie() {
        int jetonsNoirs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "B");
        int jetonsBlancs = gestionPlateau.compterJetons(gestionPlateau.getPlateau(), "W");

        // Utiliser Platform.runLater pour éviter l'erreur "showAndWait not allowed
        // during animation"
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fin de partie");
            alert.setHeaderText("La partie est terminée !");

            String message;
            if (jetonsNoirs > jetonsBlancs) {
                message = "Les NOIRS ont gagné !\n\n" +
                        "Score final :\n" +
                        "Noirs : " + jetonsNoirs + "\n" +
                        "Blancs : " + jetonsBlancs;
            } else if (jetonsBlancs > jetonsNoirs) {
                message = "Les BLANCS ont gagné !\n\n" +
                        "Score final :\n" +
                        "Noirs : " + jetonsNoirs + "\n" +
                        "Blancs : " + jetonsBlancs;
            } else {
                message = "Match nul !\n\n" +
                        "Score final :\n" +
                        "Noirs : " + jetonsNoirs + "\n" +
                        "Blancs : " + jetonsBlancs;
            }

            alert.setContentText(message);
            alert.showAndWait();
            gestionPlateau.initialiserPlateau();
            joueurActuel = "B";
            mettreAJourAffichage();
        });
    }
}
