package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class FilesParsingTest {

    private ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void xlsxTest() throws Exception{
        try (InputStream is = zipFileParsingTest(".xlsx")) {
            assertNotNull(is, "xlsx-файл не найден в архиве");
            XLS xls = new XLS(is);
            String actualValue = xls.excel.getSheetAt(0)
                    .getRow(2)
                    .getCell(0)
                    .getStringCellValue();
            assertTrue(actualValue.contains("Информация о мероприятии"));
        }
    }

    @Test
    void pdfTest() throws Exception{
        try (InputStream is = zipFileParsingTest(".pdf")) {
            assertNotNull(is, "pdf-файл не найден в архиве");
            PDF pdf = new PDF(is);
            assertTrue(pdf.text.contains("ПЛАН ДЕМОНТАЖА И МОНТАЖА ПЕРЕГОРОДОК И ОБОРУДОВАНИЯ"), "pdf-файл не содержит нужной строки");
        }

    }

    @Test
    void csvTest() throws Exception{
        try (InputStream is = zipFileParsingTest(".csv")) {
            assertNotNull(is, "csv-файл не найден в архиве");
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .withCSVParser(parser)
                    .build();

            List<String[]> data = csvReader.readAll();
            String[] lineSearch = {"6RU 845 025","СТЕКЛО З Л","1", "2540.00"};

            boolean ifFound = data.stream()
                    .anyMatch(line -> Arrays.equals(line, lineSearch));
            Assertions.assertTrue(ifFound, "csv-файл не содержит нужной строки");
        }

    }

    private InputStream zipFileParsingTest(String fileName) throws Exception {
        try (InputStream is = cl.getResourceAsStream("4.zip")) {
            assertNotNull(is, "Архив не найден");
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(fileName)) {
                    return new ByteArrayInputStream(zis.readAllBytes());
                }
            }
        }
        return null;
    }
}