package max.vanach.lesson_1;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Random;
import java.util.Scanner;

public class Lesson_1 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);

        while (true) {
            List<Player> players = new ArrayList<>();

            System.out.println("Wybierz ilość graczy (1-8):");
            int numberOfPlayers = readIntInput();

            for (int i = 1; i <= numberOfPlayers; i++) {
                System.out.print("Podaj nick gracza " + i + ": ");
                String nickname = scan.nextLine();
                Player player = loadPlayer(nickname);
                players.add(player);
            }

            System.out.println("Witajcie!");

            System.out.println("Wybierz tryb gry:");
            System.out.println("1. Rozgrywka standardowa(1gracz)");
            System.out.println("2. Rozgrywka odwrotna(1 gracz)");
            System.out.println("3. Rozgrywka sekwencyjna(2-8 graczy)");
            System.out.println("4. Sprawdź najlepsze wyniki!)");
            int gameMode = readIntInput();

        switch (gameMode) {
            case 1:
                System.out.println("Wybierz poziom trudności:");
                System.out.println("1. Łatwy (0-100)");
                System.out.println("2. Normalny (0-10000)");
                System.out.println("3. Trudny (0-1000000)");
                System.out.println("4. Zaawansowany (Gracz sam wybiera liczbę)");
                int difficultyLevel = readIntInput();
                switch (difficultyLevel) {
                    case 1:
                        playGuessingGame(players.get(0), 0, 100, 1);
                        savePlayers(players, 1); // 1 reprezentuje poziom trudności łatwy
                        break;
                    case 2:
                        playGuessingGame(players.get(0), 0, 10000, 2);
                        savePlayers(players, 2); // 2 reprezentuje poziom trudności normalny
                        break;
                    case 3:
                        playGuessingGame(players.get(0), 0, 1000000, 3);
                        savePlayers(players, 3); // 3 reprezentuje poziom trudności trudny
                        break;
                    case 4:
                        int customMinRange, customMaxRange;

                        System.out.println("Podaj minimum przedziału: ");
                        customMinRange = readIntInput();

                        System.out.println("Podaj maksimum przedziału: ");
                        customMaxRange = readIntInput();

                        if (customMinRange < customMaxRange) {
                            playGuessingGame(players.get(0), customMinRange, customMaxRange, 4);
                            savePlayers(players, 4); // 4 reprezentuje tryb zaawansowany
                        } else {
                            System.out.println("Nieprawidłowy wybór przedziału. Gra łatwa zostanie uruchomiona.");
                            playGuessingGame(players.get(0), 0, 100, 1);
                            savePlayers(players, 1);
                        }
                        break;
                    default:
                        System.out.println("Nieprawidłowy wybór. Gra łatwa zostanie uruchomiona.");
                        playGuessingGame(players.get(0), 0, 100, 1);
                        savePlayers(players, 1);
                }
                break;
            case 2:
                reverseGuessingGame(players.get(0));
                break;
            case 3:
                if (players.size() > 1) {
                    mixedGuessingGame(players);
                } else {
                    System.out.println("Nieprawidłowy wybór. Gra standardowa zostanie uruchomiona.");
                    playGuessingGame(players.get(0), 0, 100, 1);
                    savePlayers(players, 1);
                }
                break;
            case 4:
                // Wyświetl najlepsze wyniki przed wyborem trybu gry
                displayBestScores(players);
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Gra standardowa zostanie uruchomiona.");
                playGuessingGame(players.get(0), 0, 100, 1);
                savePlayers(players, 1);
        }

            System.out.println("Czy chcesz zagrać ponownie? Wpisz tak, by rozpocząć nową rozgrywkę");
    String playAgain = scan.nextLine().toLowerCase();
    if (!playAgain.equals("tak")) {
        // Wyświetl najlepsze wyniki przed zakończeniem programu
        displayBestScores(players);
        break;
    }
}
    }

    private static void displayBestScores(List<Player> players) {
    System.out.println("Najlepsze wyniki:");
    for (Player player : players) {
        System.out.println(player.getNickname() + ":");
        System.out.println("Łatwy: " + player.getBestScoreEasy());
        System.out.println("Normalny: " + player.getBestScoreNormal());
        System.out.println("Trudny: " + player.getBestScoreHard());
        System.out.println("Zaawansowany: " + player.getBestScoreAdvanced());
        System.out.println("Mieszany - Wygrane: " + player.getGamesWonMixed() + " Przegrane: " + player.getGamesLostMixed());
        System.out.println();
    }
}
    
    private static void savePlayers(List<Player> players, int difficultyLevel) {
        for (Player player : players) {
            savePlayer(player, difficultyLevel);
        }
    }

    private static void savePlayer(Player player, int difficultyLevel) {
        Path playerFilePath = Paths.get("./gracze/");
        if (!Files.exists(playerFilePath)) {
            try {
                Files.createDirectories(playerFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        Path playerFile = Paths.get("./gracze/" + player.getNickname() + ".txt");

        try (PrintWriter fileWriter = new PrintWriter(playerFile.toFile())) {
            fileWriter.println(player.getBestScore());

            fileWriter.println("Łatwy: " + player.getBestScoreEasy());
            fileWriter.println("Normalny: " + player.getBestScoreNormal());
            fileWriter.println("Trudny: " + player.getBestScoreHard());
            fileWriter.println("Zaawansowany: " + player.getBestScoreAdvanced());

            fileWriter.println("Wygrane w grze mieszanej: " + player.getGamesWonMixed());
            fileWriter.println("Przegrane w grze mieszanej: " + player.getGamesLostMixed());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Player loadPlayer(String nickname) {
        Path playerFile = Paths.get("./gracze/" + nickname + ".txt");
        Player player;

        if (Files.exists(playerFile)) {
            try (Scanner fileReader = new Scanner(playerFile)) {
                int bestScore = fileReader.nextInt();
                int bestScoreEasy = 0;
                int bestScoreNormal = 0;
                int bestScoreHard = 0;
                int bestScoreAdvanced = 0;
                int gamesWonMixed = 0;
                int gamesLostMixed = 0;

                while (fileReader.hasNextLine()) {
                    String line = fileReader.nextLine();
                    if (line.startsWith("Łatwy:")) {
                        bestScoreEasy = Integer.parseInt(line.substring(7).trim());
                    } else if (line.startsWith("Normalny:")) {
                        bestScoreNormal = Integer.parseInt(line.substring(9).trim());
                    } else if (line.startsWith("Trudny:")) {
                        bestScoreHard = Integer.parseInt(line.substring(7).trim());
                    } else if (line.startsWith("Zaawansowany:")) {
                        bestScoreAdvanced = Integer.parseInt(line.substring(13).trim());
                    }
                    else if (line.startsWith("Wygrane w grze mieszanej:")) {
                        gamesWonMixed = Integer.parseInt(line.substring(26).trim());
                    } else if (line.startsWith("Przegrane w grze mieszanej:")) {
                        gamesLostMixed = Integer.parseInt(line.substring(28).trim());
                    }
                }

                player = new Player(nickname);
                player.setBestScore(bestScore);
                player.setBestScoreEasy(bestScoreEasy);
                player.setBestScoreNormal(bestScoreNormal);
                player.setBestScoreHard(bestScoreHard);
                player.setBestScoreAdvanced (bestScoreAdvanced);
                player.setGamesWonMixed(gamesWonMixed);
                player.setGamesLostMixed(gamesLostMixed);
            } catch (IOException e) {
                e.printStackTrace();
                player = new Player(nickname);
            }
        } else {
            player = new Player(nickname);
        }

        return player;
    }
    
    private static void playGuessingGame(Player player, int minRange, int maxRange, int difficultyLevel) {
        Random random = new Random();
        int targetNumber = random.nextInt(maxRange - minRange + 1) + minRange;
        int attempts = 0;

        System.out.println("Spróbuj odgadnąć liczbę w przedziale od " + minRange + " do " + maxRange);

        while (true) {
            System.out.print("Podaj liczbę: ");
            int guess = readIntInput();

            attempts++;

            if (guess < targetNumber) {
                System.out.println("Spróbuj podać wyższą liczbę!");
            } else if (guess > targetNumber) {
                System.out.println("Spróbuj wybrać mniejszą liczbę!");
            } else {
                System.out.println("Brrrawo! Odgadłeś liczbę w " + attempts + " podejściach.");
                //switch dotyczacy powiadomienia nowego najlepszego wyniku
                switch (difficultyLevel) {
                    case 1:
                        if (attempts < player.getBestScoreEasy() || player.getBestScoreEasy() == 0) {
                            player.setBestScoreEasy(attempts);
                            System.out.println("Nowy najlepszy wynik dla poziomu łatwego!");
                        }
                        break;
                    case 2:
                        if (attempts < player.getBestScoreNormal() || player.getBestScoreNormal() == 0) {
                            player.setBestScoreNormal(attempts);
                            System.out.println("Nowy najlepszy wynik dla poziomu normalnego!");
                        }
                        break;
                    case 3:
                        if (attempts < player.getBestScoreHard() || player.getBestScoreHard() == 0) {
                            player.setBestScoreHard(attempts);
                            System.out.println("Nowy najlepszy wynik dla poziomu trudnego!");
                        }
                        break;
                    case 4:
                        if (attempts < player.getBestScoreAdvanced() || player.getBestScoreAdvanced() == 0) {
                            player.setBestScoreAdvanced(attempts);
                            System.out.println("Nowy njlepszy wynik dla poziomu zaawansowanego!");
                        }
                        break;
                    default:
                        System.out.println("Nieprawidłowy poziom trudności.");
                }

                break;
            }
        }
    }

    private static void reverseGuessingGame(Player player) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wybierz poziom trudności:");
        System.out.println("1. Łatwy (0-100)");
        System.out.println("2. Normalny (0-1000)");
        System.out.println("3. Trudny (0-10000)");
        System.out.println("4. Zaawansowany (Gracz sam wybiera przedział)");
        int difficultyLevel = readIntInput();

        int minZakres;
        int maxZakres;

        switch (difficultyLevel) {
            case 1:
                minZakres = 0;
                maxZakres = 100;
                break;
            case 2:
                minZakres = 0;
                maxZakres = 1000;
                break;
            case 3:
                minZakres = 0;
                maxZakres = 10000;
                break;
            case 4:
                System.out.println("Podaj minimum przedziału: ");
                minZakres = readIntInput();
                System.out.println("Podaj maksimum przedziału: ");
                maxZakres = readIntInput();
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Gra łatwa zostanie uruchomiona.");
                minZakres = 0;
                maxZakres = 100;
        }

        Random losowaLiczba = new Random();
        int wylosowanaLiczba;

        System.out.print("Podaj liczbę, a program będzie próbował ją odgadnąć: ");
        wylosowanaLiczba = readIntInput();

        int próby = 0;

        System.out.println("Program próbuje odgadnąć liczbę " + wylosowanaLiczba + ".");

        while (true) {
            int zgadnięcie = losowaLiczba.nextInt(maxZakres - minZakres + 1) + minZakres;

            próby++;

            System.out.println("Program próbuje: " + zgadnięcie);

            if (zgadnięcie < wylosowanaLiczba) {
                System.out.println("Program próbuje wyższej liczby!");
                minZakres = zgadnięcie + 1;
            } else if (zgadnięcie > wylosowanaLiczba) {
                System.out.println("Program próbuje mniejszej liczby!");
                maxZakres = zgadnięcie - 1;
            } else {
                System.out.println("Brawo programie!!! Program odgadł liczbę " + wylosowanaLiczba + " w " + próby + " podejściach.");
                break;
            }
            try {
                Thread.sleep(500); // opóźnienie dodałem, by rozgrywka była przyjemniejsza dla oka, 20 linijek tekstu w konsoli na raz nie wygląda ładnie
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mixedGuessingGame(List<Player> players) {
    Scanner scanner = new Scanner(System.in);

    Map<Player, Integer> targetNumbers = new HashMap<>();
    Map<Player, Integer> attempts = new HashMap<>();
    Map<Player, Boolean> hasWon = new HashMap<>();

    for (Player player : players) {
        System.out.println(player.getNickname() + ", wybierz liczbę do odgadnięcia: ");
        int targetNumber = readIntInput();
        targetNumbers.put(player, targetNumber);
        attempts.put(player, 0);
        hasWon.put(player, false);
    }

    while (true) {
    for (int i = 0; i < players.size(); i++) {
        int nextPlayerIndex = (i + 1) % players.size();
        Player currentPlayer = players.get(i);
        Player nextPlayer = players.get(nextPlayerIndex);

        if (hasWon.get(currentPlayer)) {
            continue;
        }

        System.out.println(currentPlayer.getNickname() + ", odgadnij liczbę " + nextPlayer.getNickname() + ": ");
        int guess = readIntInput();
        attempts.put(currentPlayer, attempts.get(currentPlayer) + 1);

        int targetNumberNextPlayer = targetNumbers.get(nextPlayer);

        if (guess < targetNumberNextPlayer) {
            System.out.println("Spróbuj podać wyższą liczbę, " + currentPlayer.getNickname());
        } else if (guess > targetNumberNextPlayer) {
            System.out.println("Spróbuj wybrać mniejszą liczbę, " + currentPlayer.getNickname());
        } else {
            System.out.println("Brawo " + currentPlayer.getNickname() + "!!! Odgadłeś liczbę " + nextPlayer.getNickname() + "!");
            currentPlayer.setGamesWonMixed(currentPlayer.getGamesWonMixed() + 1);
            hasWon.put(currentPlayer, true);

            if (currentPlayer != nextPlayer) {
                nextPlayer.setGamesLostMixed(nextPlayer.getGamesLostMixed() + 1);
                hasWon.put(nextPlayer, true);
            }

            for (Player otherPlayer : players) {
                if (!hasWon.get(otherPlayer) && otherPlayer != currentPlayer && otherPlayer != nextPlayer) {
                    otherPlayer.setGamesLostMixed(otherPlayer.getGamesLostMixed() + 1);
                }
            }

            for (Player p : players) {
                savePlayer(p, 1);
            }

            return;
        }
    }
}
}
    
    private static int readIntInput() {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("Podaj właściwą liczbę.");
            scanner.next(); // w przypadku podania złej liczby(żeby uniknąć błędów)
        }
        return scanner.nextInt();
    }
}