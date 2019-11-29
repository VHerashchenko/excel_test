package vad.testmav.src.controls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApachePOIExcelRead {

    private String filename;
    private StringBuilder stringBuilder = new StringBuilder();
    private List<String> stringList = new ArrayList<>();


    public void readExcel() {

        try {
            FileInputStream excelFile = new FileInputStream(new File(filename));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            stringList = new ArrayList<>();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        stringBuilder.append(currentCell.getStringCellValue());
                        stringBuilder.append("--");
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        stringBuilder.append(currentCell.getNumericCellValue());
                        stringBuilder.append("--");
                    }
                }
                stringList.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApachePOIExcelRead() {
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public List<String> getStringList() {
        return stringList;
    }
}
