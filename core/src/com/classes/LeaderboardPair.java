package com.classes;

/**
 * A Pair Structure. Used by the leaderboard to read in a pair.
 */
public class LeaderboardPair {
    public String name;
    public int score;

    /**
     * Constructor creates a new pair that stores the name and score.
     * 
     * @param name The name of the user.
     * @param score The score of the user.
     */
    public LeaderboardPair(String name, int score){
        this.name = name;
        this.score = score;
    }
}
