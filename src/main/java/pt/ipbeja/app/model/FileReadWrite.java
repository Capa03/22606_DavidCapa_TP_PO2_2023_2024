package pt.ipbeja.app.model;

import java.io.*;

/**
 * FileReadWrite class
 * @author David Capa
 * @version 2024/04/14
 */

public class FileReadWrite {

    public FileReadWrite() {}

    public String readFile(String fileName) {
        StringBuilder formattedContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/"+fileName+".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                formattedContent.append(line.trim()).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!formattedContent.isEmpty()) {
            formattedContent.setLength(formattedContent.length() - 1);
        }
        return formattedContent.toString();
    }

    public void writeFile(String content,String fileName,boolean append) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/"+fileName+".txt",append))) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
