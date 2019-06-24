package com.wing.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 夏飞
 */
public class ExcelUtils {

    public static void setFileName(String fileName, HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpServletResponse.setHeader("Content-Disposition", String.format("attachment;fileName=%s.xlsx", fileName));
    }

    public static <T> void export(Collection<T> data, OutputStream outputStream) {
        export(data, null, null, outputStream);
    }
    public static <T> void export(Collection<T> data,  LinkedHashMap<String, String> header, OutputStream outputStream){
        export(data, null, header, outputStream);
    }

    public static <T> void export(Collection<T> data, BeanHandler<T> beanHandler, LinkedHashMap<String, String> header, OutputStream outputStream) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("sheet");
            boolean first = true;
            int i = 0;
            Collection<String> fieldSort = null;
            for (T rowData : data) {
                if (beanHandler != null)
                    beanHandler.handler(rowData);
                Map<String, Object> rowMap = convertMap(rowData);
                if (first) {
                    first = false;
                    fieldSort = getFieldSort(rowMap, header);
                    Row firsRow = sheet.createRow(i++);
                    setRowHeader(firsRow, fieldSort, header);
                }
                Row row = sheet.createRow(i++);
                setRowData(row, rowMap, fieldSort);
            }
            wb.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void setRowData(Row row, Map map, Collection<String> fieldSort) {
        int i = 0;
        for (String field : fieldSort) {
            Cell cell = row.createCell(i++);
            Object value = map.get(field);
            if (Number.class.isInstance(value)) {
                cell.setCellValue(Double.parseDouble(value.toString()));
            } else {
                cell.setCellValue(String.valueOf(value));
            }
        }
    }

    /**
     * 根据header获取字段排序
     */
    private static Collection<String> getFieldSort(Map<String, Object> map, Map<String, String> header) {
        if (header != null) {
            return header.keySet();
        } else {
            return map.keySet();
        }
    }

    /**
     * 根据header设置第一行
     */
    private static void setRowHeader(Row row, Collection<String> fieldSort, Map<String, String> header) {
        int i = 0;
        for (String key : fieldSort) {
            Cell cell = row.createCell(i++);
            //先从header里面取
            if (header != null) {
                cell.setCellValue(header.get(String.valueOf(key)));
            } else {
                cell.setCellValue(key);
            }
        }
    }

    private static Map<String, Object> convertMap(Object data) {
        if (Map.class.isInstance(data)) {
            return (Map<String, Object>) data;
        } else {
            return BeanMap.create(data);
        }
    }
}
