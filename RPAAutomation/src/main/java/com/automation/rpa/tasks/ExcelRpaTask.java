package com.automation.rpa.tasks;

import com.automation.rpa.core.RpaBaseTask;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * RPA task for reading and writing Excel (.xlsx) files using Apache POI.
 *
 * Supports WRITE (create a new workbook from data) and READ (log sheet content).
 *
 * Example - write:
 * <pre>
 *   ExcelRpaTask task = new ExcelRpaTask.Builder()
 *       .operation(ExcelOperation.WRITE)
 *       .filePath("output/results.xlsx")
 *       .sheetName("RPA Results")
 *       .data(List.of(
 *           List.of("Task", "Status", "Duration"),
 *           List.of("FileTask", "COMPLETED", "23ms")
 *       ))
 *       .build();
 * </pre>
 */
public class ExcelRpaTask extends RpaBaseTask {

    public enum ExcelOperation { READ, WRITE }

    private final ExcelOperation    operation;
    private final String            filePath;
    private final String            sheetName;
    private final List<List<String>> data;

    private ExcelRpaTask(Builder builder) {
        super("ExcelTask[" + builder.operation + "] → " + builder.filePath);
        this.operation  = builder.operation;
        this.filePath   = builder.filePath;
        this.sheetName  = builder.sheetName != null ? builder.sheetName : "Sheet1";
        this.data       = builder.data;
    }

    @Override
    protected void execute() throws Exception {
        switch (operation) {
            case WRITE -> writeExcel();
            case READ  -> readExcel();
        }
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    private void writeExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = buildHeaderStyle(workbook);

            for (int rowIdx = 0; rowIdx < data.size(); rowIdx++) {
                Row row = sheet.createRow(rowIdx);
                List<String> rowData = data.get(rowIdx);
                for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
                    Cell cell = row.createCell(colIdx);
                    cell.setCellValue(rowData.get(colIdx));
                    if (rowIdx == 0) {
                        cell.setCellStyle(headerStyle);
                    }
                }
            }

            autoSizeColumns(sheet, data);

            Files.createDirectories(Path.of(filePath).getParent());
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            log.Info("Excel written: " + filePath + " — " + Math.max(0, data.size() - 1) + " data row(s)");
        }
    }

    private CellStyle buildHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private void autoSizeColumns(Sheet sheet, List<List<String>> data) {
        int colCount = data.isEmpty() ? 0 : data.get(0).size();
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    private void readExcel() throws IOException {
        try (InputStream is = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) sheet = workbook.getSheetAt(0);

            log.Info("Reading sheet: '" + sheet.getSheetName() + "' — " + sheet.getPhysicalNumberOfRows() + " row(s)");

            for (Row row : sheet) {
                StringBuilder sb = new StringBuilder();
                for (Cell cell : row) {
                    sb.append(cell.toString()).append("\t");
                }
                log.Info("Row " + (row.getRowNum() + 1) + " | " + sb.toString().trim());
            }
        }
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private ExcelOperation      operation;
        private String              filePath;
        private String              sheetName;
        private List<List<String>>  data = new ArrayList<>();

        public Builder operation(ExcelOperation op)         { this.operation  = op;   return this; }
        public Builder filePath(String path)                { this.filePath   = path; return this; }
        public Builder sheetName(String name)               { this.sheetName  = name; return this; }
        public Builder data(List<List<String>> rows)        { this.data       = rows; return this; }

        public ExcelRpaTask build() {
            if (operation == null || filePath == null) {
                throw new IllegalStateException("ExcelRpaTask requires 'operation' and 'filePath'.");
            }
            return new ExcelRpaTask(this);
        }
    }
}
