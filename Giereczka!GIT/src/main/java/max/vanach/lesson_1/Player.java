package max.vanach.lesson_1;

public class Player {
    private String nickname;
    private int bestScore;
    private int bestScoreEasy;
    private int bestScoreNormal;
    private int bestScoreHard;
    private int bestScoreAdvanced;
    
    private int gamesWonMixed;
    private int gamesLostMixed;

    public Player(String nickname) {
        this.nickname = nickname;
        this.bestScore = 0;
        this.bestScoreEasy = 0;
        this.bestScoreNormal = 0;
        this.bestScoreHard = 0;
    }

    // Dodaj gettery i settery dla wszystkich pól

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getBestScoreEasy() {
        return bestScoreEasy;
    }

    public void setBestScoreEasy(int bestScoreEasy) {
        this.bestScoreEasy = bestScoreEasy;
    }

    public int getBestScoreNormal() {
        return bestScoreNormal;
    }

    public void setBestScoreNormal(int bestScoreNormal) {
        this.bestScoreNormal = bestScoreNormal;
    }

    public int getBestScoreHard() {
        return bestScoreHard;
    }

    public void setBestScoreHard(int bestScoreHard) {
        this.bestScoreHard = bestScoreHard;
    }
    
    public int getGamesWonMixed() {
        return gamesWonMixed;
    }

    public void setGamesWonMixed(int gamesWonMixed) {
        this.gamesWonMixed = gamesWonMixed;
    }

    public int getGamesLostMixed() {
        return gamesLostMixed;
    }

    public void setGamesLostMixed(int gamesLostMixed) {
        this.gamesLostMixed = gamesLostMixed;
    }
    public int getBestScoreAdvanced() {
        return bestScoreAdvanced;
    }

    public void setBestScoreAdvanced(int bestScoreAdvanced) {
        this.bestScoreAdvanced = bestScoreAdvanced;
    }
}