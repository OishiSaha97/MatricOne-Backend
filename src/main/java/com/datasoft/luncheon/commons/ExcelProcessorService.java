package com.datasoft.luncheon.commons;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static com.datasoft.luncheon.commons.model.Strings.*;

@Slf4j
@Service
public class ExcelProcessorService {

    public List<Map<String, Object>> readExcel(MultipartFile file, List<HashMap<String, String>> mappings) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row row = rowIterator.next();
        boolean isHeaderInvalid = false;
        try{
            for(int cellNumber = 0; cellNumber < mappings.size(); cellNumber++){
                if(!Objects.equals(row.getCell(cellNumber).getStringCellValue(), mappings.get(cellNumber).get(KEY_EXCEL))){
                    isHeaderInvalid = true;
                    break;
                }
            }
        } catch (Exception e){
            isHeaderInvalid = true;
        }
        if(isHeaderInvalid){
            throw new RuntimeException("Invalid Template");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> rowData = new HashMap<>();
        Cell cell = null;

        String regex = "^\\d{2}-\\d{2}-\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        while(rowIterator.hasNext()){
            rowData = new HashMap<>();
            row = rowIterator.next();
            for(int cellNumber = 0; cellNumber < mappings.size(); cellNumber++){
                try{
                    cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    switch (mappings.get(cellNumber).get(KEY_DATATYPE).toUpperCase()) {
                        case "STRING" : rowData.put(mappings.get(cellNumber).get(KEY_DB), cell.getStringCellValue()); break;
                        case "DATE" : {
                            if(Objects.isNull(cell) || Objects.isNull(cell.getStringCellValue())){
                                throw new DataFormatException("Date is mandatory");

                            }else if(pattern.matcher(cell.getStringCellValue()).matches()){
                                rowData.put(mappings.get(cellNumber).get(KEY_DB), cell.getStringCellValue());
                            } else{
                                throw new DataFormatException("Invalid Date Format. Accepted Format is dd-MM-yyyy");
                            }

                        } break;
                        case "DOUBLE" : rowData.put(mappings.get(cellNumber).get(KEY_DB), cell.getNumericCellValue()); break;
                        case "BOOLEAN" : rowData.put(mappings.get(cellNumber).get(KEY_DB), cell.getBooleanCellValue()); break;
                        default: rowData.put(mappings.get(cellNumber).get(KEY_DB), null);
//                        rowData.put("created_by",user)
                    }
                } catch (DataFormatException e){
                    workbook.close();
                    throw e;
                } catch (Exception e){
                    rowData.put(mappings.get(cellNumber).get(KEY_DB), null);
                }
            }
            result.add(rowData);
        }
        workbook.close();
        return result;
    }


    public void writeExcel(List<Map<String, Object>> dataList, List<HashMap<String, String>> mappings, HttpServletResponse response){
        Workbook workbook = null;
        ServletOutputStream outputStream = null;
        try{
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Table Data");
            // Header
            Row row = sheet.createRow(0);
            Cell cell = null;
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int colIdx = 0; colIdx < mappings.size(); colIdx ++) {
                cell = row.createCell(colIdx);
                cell.setCellValue(mappings.get(colIdx).get(KEY_EXCEL));
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowIndex = 1;
            for (Map<String, Object> data : dataList) {
                row = sheet.createRow(rowIndex++);
                for (int colIdx = 0; colIdx < mappings.size(); colIdx ++) {
                    cell = row.createCell(colIdx);
                    cell.setCellValue(Objects.isNull(data.get(mappings.get(colIdx).get(KEY_DB))) ? null : data.get(mappings.get(colIdx).get(KEY_DB)).toString() );
                }
            }
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (Exception e){
            log.error(e.getMessage(), e.getCause());

        } finally {
            if(Objects.nonNull(workbook)){
                try {
                    workbook.close();
                    outputStream.close();
                } catch (Exception e) {
                    log.error("Unable to close workbook", e.getCause());
                }
            }


        }

    }
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);

        return style;
    }


}
