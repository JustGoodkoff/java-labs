package ru.nsu.ccfit.gudkov.minesweeper.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.ccfit.gudkov.minesweeper.StringConstants;

import java.io.File;
import java.io.IOException;

public class StatModel {

    private int easyRecord;
    private int mediumRecord;
    private int hardRecord;

    public StatModel() {
        File file = new File(StringConstants.STATISTIC_ABSOLUTE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        easyRecord = rootNode.get(StringConstants.EASY_MODE).asInt();
        mediumRecord = rootNode.get(StringConstants.MEDIUM_MODE).asInt();
        hardRecord = rootNode.get(StringConstants.HARD_MODE).asInt();
    }

    public int getEasyRecord() {
        return easyRecord;
    }

    public int getMediumRecord() {
        return mediumRecord;
    }

    public int getHardRecord() {
        return hardRecord;
    }
}
