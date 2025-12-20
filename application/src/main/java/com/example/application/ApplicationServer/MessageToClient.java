package com.example.application.ApplicationServer;
import lombok.Data;
import java.util.List;

@Data
public class MessageToClient {
    private int roomId;
    private int gameStatus;
    private int userId;
    private int expectedCredits;
    private int earnedCredits;
    private int currentPlace;
    private boolean isSkipped;
    private List<Integer> resultList;
}
