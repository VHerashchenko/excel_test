package vad.testmav.src.component;

import vad.testmav.src.controls.ApachePOIExcelRead;
import vad.testmav.src.controls.ApachePOIExcelWrite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Compare {
    private String firstFile;
    private String secondFile;
    private String outputFileName;
    private List<String> firstList = new ArrayList<>();
    private List<String> secondList = new ArrayList<>();
    private List<String> finalList = new ArrayList<>();
    private int sizeOfDataTypeLength = 6;

    private void readFiles() {
        ApachePOIExcelRead reader = new ApachePOIExcelRead();

        reader.setFilename(firstFile);
        reader.readExcel();
        firstList = reader.getStringList();

        reader.setFilename(secondFile);
        reader.readExcel();
        secondList = reader.getStringList();
    }

    private void createNewFile() {

        Object[][] dataTypes = new Object[finalList.size() + 1][sizeOfDataTypeLength + 1];

        dataTypes[0][0] = "№";
        dataTypes[0][1] = "Зареєстровано";
        dataTypes[0][2] = "Дата терміну";
        dataTypes[0][3] = "Термін розгляду";
        dataTypes[0][4] = "Стан";
        dataTypes[0][5] = "Тип";

        for (int i = 0; i < finalList.size(); ++i) {
            String[] tokens = finalList.get(i).split("--");

            if (tokens.length < 6)
                sizeOfDataTypeLength = tokens.length;

            for (int j = 0; j < sizeOfDataTypeLength; ++j) {
                if (j != 0)
                    dataTypes[i + 1][j] = tokens[j];
                else {
                    dataTypes[i + 1][j] = new BigDecimal(tokens[j]).toPlainString();
                }
            }
        }

        System.out.println("done");
        ApachePOIExcelWrite apachePOIExcelWrite = new ApachePOIExcelWrite(outputFileName, dataTypes);
        apachePOIExcelWrite.writeExcel();
    }

    private void excludeDuplications() {
        List<String> duplicateList = firstList.stream()
                .filter(secondList::contains)
                .collect(Collectors.toList());

        finalList.addAll(firstList);
        finalList.addAll(secondList);

        finalList = finalList.stream()
                .filter(value -> !duplicateList.contains(value))
                .collect(Collectors.toList());
    }

    public void process() {
        readFiles();
        excludeDuplications();
        createNewFile();
    }

    public Compare(List<String> fileNames, String outputFileName) {
        this.firstFile = fileNames.get(0);
        this.secondFile = fileNames.get(1);
        this.outputFileName = outputFileName;
    }
}
