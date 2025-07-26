package org.example.factory;

import org.example.services.ConverterService;
import org.example.services.impl.Excel2JsonConverterService;
import org.example.services.impl.Json2ExcelConverterService;

public class ConverterFactory {

    private static final String EXCEL_CONVERTER_TYPE = "excel";
    private static final String JSON_F_CONVERTER_TYPE = "json-f";
    private static final String JSON_U_CONVERTER_TYPE = "json-u";


    private ConverterFactory() {}

    public static ConverterService createFromType(String converterType) {
        if(EXCEL_CONVERTER_TYPE.equalsIgnoreCase(converterType)) {
            return new Json2ExcelConverterService();
        }

        if(JSON_F_CONVERTER_TYPE.equalsIgnoreCase(converterType)
                || JSON_U_CONVERTER_TYPE.equalsIgnoreCase(converterType)) {
            return new Excel2JsonConverterService(toJsonType(converterType));
        }

        throw new IllegalArgumentException("Converter type " + converterType + " is not supported. Use one of: excel, json-f, json-u.");
    }

    private static Excel2JsonConverterService.JsonType toJsonType(String input) {
        switch(input) {
            case JSON_F_CONVERTER_TYPE:
                return Excel2JsonConverterService.JsonType.JSON_F;
            case JSON_U_CONVERTER_TYPE:
                return Excel2JsonConverterService.JsonType.JSON_U;
            default:
                throw new IllegalArgumentException("Value " + input + " can't be mapped to JsonType");
        }
    }
}
