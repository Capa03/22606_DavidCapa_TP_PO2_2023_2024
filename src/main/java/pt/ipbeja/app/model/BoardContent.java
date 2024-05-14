package pt.ipbeja.app.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BoardContent {

    private String boardContent;
    private List<String> easy ;
    private Map<String,List<String>> solutions;


    public BoardContent() {
        this.solutions = new HashMap<>();
        setSolutions();
    }

    public Map<String,List<String>> getSolutions(){
        return this.solutions;
    }

    public String getBoardContent() {
        //TODO Read File
        System.out.println(this.readFile());
        return this.readFile().toString();
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


    public static String readFile() {
        StringBuilder formattedContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/capa/Desktop/PO2/Projeto/PO2/src/main/resources/words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Trim the line and add it to the formatted content with a newline character
                formattedContent.append(line.trim()).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Remove the trailing newline character if it exists
        if (formattedContent.length() > 0) {
            formattedContent.setLength(formattedContent.length() - 1);
        }
        return formattedContent.toString();
    }
}
