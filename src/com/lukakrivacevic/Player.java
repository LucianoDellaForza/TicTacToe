package com.lukakrivacevic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Integer playerNumber;
    private boolean resigned;

    public List<Integer> markedPositions;

    public Player(String name) {
        this.name = name;
        this.markedPositions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public boolean hasResigned() {
        return resigned;
    }

    public void setResigned(boolean resigned) {
        this.resigned = resigned;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }
}
