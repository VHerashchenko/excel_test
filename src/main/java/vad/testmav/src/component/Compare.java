package vad.testmav.src.component;

import vad.testmav.src.controls.ApachePOIExcelRead;
import vad.testmav.src.controls.ApachePOIExcelWrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Compare {
    private String firstFile;
    private String secondFile;
    private List<String> firstList = new ArrayList<>();
    private List<String> secondList = new ArrayList<>();
    private List<String> finalList = new ArrayList<>();
    private HashSet<Integer> setForI = new HashSet<>();
    private HashSet<Integer> setForJ = new HashSet<>();
    private int sizeOfDataTypeLength = 6;

    private void readFiles(){
        ApachePOIExcelRead reader = new ApachePOIExcelRead();

        reader.setFilename(firstFile);
        reader.readExcel();
        firstList = reader.getStringList();

        reader.setFilename(secondFile);
        reader.readExcel();
        secondList = reader.getStringList();

        String test = firstList.get(0);
    }

    private void createNewFile(){

        Object[][] dataTypes = new Object[finalList.size() + 1][sizeOfDataTypeLength + 1];

            dataTypes[0][0] = "№";
            dataTypes[0][1] = "Зареєстровано";
            dataTypes[0][2] = "Дата терміну";
            dataTypes[0][3] = "Термін розгляду";
            dataTypes[0][4] = "Стан";
            dataTypes[0][5] = "Тип";

        for(int i = 0; i < finalList.size(); ++i){
            String[] tokens = finalList.get(i).split("--");

            if (tokens.length < 6)
                sizeOfDataTypeLength = tokens.length;

            for(int j = 0; j < sizeOfDataTypeLength; ++j){
                if(j != 0)
                    dataTypes[i + 1][j] = tokens[j];
                else {
                    tokens[j] = tokens[j].substring(0, tokens[j].length() - 1);
                    tokens[j] = tokens[j].replace(".", "");
                    tokens[j] = tokens[j].replace("E", "");
                    dataTypes[i + 1][j] = tokens[j];
                }
            }
        }

        System.out.println("done");
        ApachePOIExcelWrite apachePOIExcelWrite = new ApachePOIExcelWrite("file1.xlsx", dataTypes);
        apachePOIExcelWrite.writeExcel();
    }

    private void comparing(){

        for(int i = 0; i < firstList.size(); ++i)
            for(int j = 0; j < secondList.size(); ++j){
                if(firstList.get(i).equals(secondList.get(j))){
                    setForI.add(i);
                    setForJ.add(j);
                }
            }
    }

    private void createNewList(){
        for (int i = 0; i < firstList.size(); ++i){
            if(!setForI.contains(i))
                finalList.add(firstList.get(i));
        }
        for (int j = 0; j < secondList.size(); ++j){
            if (!setForJ.contains(j))
                finalList.add(secondList.get(j));
        }
    }

    private void process(){
        readFiles();
        comparing();
        createNewList();
        createNewFile();
    }

    public Compare(List<String> fileNames){
        this.firstFile = fileNames.get(0);
        this.secondFile = fileNames.get(1);
        process();
    }
}
