package pt.ipbeja.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BoardContent {

    private String boardContent;
    private List<String> easy ;

    private Map<String,List<String>> solutions;
    private String gameLevel;

    public BoardContent() {
        this.solutions = new HashMap<>();
        setSolutions();
    }

    public Map<String,List<String>> getSolutions(){
        return this.solutions;
    }

    public String getBoardContent() {
        return this.boardContent;
    }

    public String gameLevel(){
        return this.gameLevel;
    }

    private void setValues(){
        this.easy = new ArrayList<>();
        this.easy.add("CASA");
        this.easy.add("GALINHA");
        this.easy.add("GATO");
    }

    private void setSolutions() {
        this.setValues();
        this.solutions.put("easy",easy);
    }

}
