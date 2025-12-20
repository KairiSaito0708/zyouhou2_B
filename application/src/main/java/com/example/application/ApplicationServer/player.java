package com.example.application.ApplicationServer;

import java.util.UUID;
import lombok.Data;

@Data

public class Player {
    private String id;
    private String name;
    private String color;
    private int currentPosition;
    private int earnedUnits;
    private int expectedUnits;

    public Player(String name) {
        this.id = UUID.randomUUID().toString(); // 重複しないIDを生成
        this.name = name;
        this.currentPosition = 0;
        this.earnedUnits = 0;
        this.expectedUnits = 25;
    }
}

   