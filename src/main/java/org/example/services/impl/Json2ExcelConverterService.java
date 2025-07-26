package org.example.services.impl;

import static org.example.constants.Constants.DE;
import static org.example.constants.Constants.EN;
import static org.example.constants.Constants.FR;
import static org.example.constants.Constants.IT;
import static org.example.constants.Constants.KEY;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.services.ConverterService;

import com.github.wnameless.json.flattener.JsonFlattener;

public class Json2ExcelConverterService implements ConverterService {

    private static final Logger logger = LogManager.getLogger(Json2ExcelConverterService.class);
    @Override
    public void convert(String file) {
        String json = load(file);
        Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);

        createExcelAndWriteFs(flattenJson, file);

        logger.info("File {} converted with success", file);
    }

    private String load(String file) {
        Path path = Paths.get(file);
        try(Stream<String> stream = Files.lines(path)) {
            return stream.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred during reading file " + file);
        }
    }

    private void createExcelAndWriteFs(Map<String, Object> data, String name) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Traduzioni");

        createHeaders(sheet);

        int numRow = 1;
        for(var entry : data.entrySet()) {
            Row row = sheet.createRow(numRow);
            row.createCell(0, CellType.STRING).setCellValue(entry.getKey());
            row.createCell(1, CellType.STRING).setCellValue(valueToString(entry.getValue()));
            numRow++;
        }

        var fileName = name.split("\\.")[0] + ".xlsx";
        writeFs(fileName, workbook);
    }

    private String valueToString(Object value) {
        var out = value.toString();
        if("{}".equals(out)) {
            return "";
        }
        return out;
    }

    private void createHeaders(XSSFSheet sheet) {
        Row row = sheet.createRow(0);
        row.createCell(0, CellType.STRING).setCellValue(KEY);
        row.createCell(1, CellType.STRING).setCellValue(IT);
        row.createCell(2, CellType.STRING).setCellValue(EN);
        row.createCell(3, CellType.STRING).setCellValue(DE);
        row.createCell(4, CellType.STRING).setCellValue(FR);
    }

    private void writeFs(String name, XSSFWorkbook  workbook) {
        try {
            FileOutputStream outputStream = new FileOutputStream(name);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            throw new IllegalStateException("An error occurred during writing file " + name);
        }
    }
}
