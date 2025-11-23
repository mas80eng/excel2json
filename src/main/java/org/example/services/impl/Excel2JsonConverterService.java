package org.example.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.services.ConverterService;

import com.github.wnameless.json.flattener.PrintMode;
import com.github.wnameless.json.unflattener.JsonUnflattener;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class Excel2JsonConverterService implements ConverterService {

    private static final Logger logger = LogManager.getLogger(Excel2JsonConverterService.class);
    private final JsonType jsonType;

    public enum JsonType {
        JSON_F, // Flat
        JSON_U // Unflat
    }

    public Excel2JsonConverterService(JsonType jsonType) {
        this.jsonType = jsonType;
    }

    @Override
    public void convert(String file) {
        var name = file.split("\\.")[0];

        Map<String, String> itMap = new TreeMap<>();
        Map<String, String> enMap = new TreeMap<>();
        Map<String, String> deMap = new TreeMap<>();
        Map<String, String> frMap = new TreeMap<>();

        load(file, itMap, enMap, deMap, frMap); // Load in map data from Excel

        // Write languages in separated files
        writeFile(name, "IT", itMap);
        writeFile(name, "EN", enMap);
        writeFile(name, "DE", deMap);
        writeFile(name, "FR", frMap);

        logger.info("File {} converted with success", file);
    }

    private void load(String file,
                      Map<String, String> itMap,
                      Map<String, String> enMap,
                      Map<String, String> deMap,
                      Map<String, String> frMap) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip first line

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getCell(0) == null) {
                    continue;
                }

                var key = row.getCell(0).getStringCellValue();

                itMap.put(key, getCellValue(row, 1));
                enMap.put(key, getCellValue(row, 2));
                deMap.put(key, getCellValue(row, 3));
                frMap.put(key, getCellValue(row, 4));
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Error during reading file", ex);
        }
    }

    private String getCellValue(Row row, int i) {
        if (row.getCell(i) == null) {
            return null;
        }

        return row.getCell(i).getStringCellValue();
    }

    private void writeFile(String name, String lang, Map<String, String> data) {
        var filename = name + "_" + lang + ".json";
        var content = buildJson(data);

        Path path = Paths.get(filename);
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred during writing file " + filename);
        }
    }

    private String buildJson(Map<String, String> data) {
        String json;
        try {
            json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch(JacksonException e) {
            json = "{}";
        }

        if(jsonType == JsonType.JSON_U) {
            return new JsonUnflattener(json).withPrintMode(PrintMode.PRETTY).unflatten();
        }

        return json;
    }
}
