package com.example.project;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Excel {
    public final String filename;

    public Excel(String filename) {
        this.filename = filename;
    }
    public ArrayList<Student> getStudentsList(){
        ArrayList<Student> students = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new File(filename));
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowCount = sheet.getPhysicalNumberOfRows();
            System.out.println(rowCount);
            int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println(columnCount);
            DecimalFormat df = new DecimalFormat("0");
            for (int i = 1; i < rowCount; i++) { // Start from index 1 to skip header row
                int id = 0;
                String name = null;
                String pinCode = null;
                for (int j = 0; j < columnCount; j++) {
                    if (sheet.getRow(i).getCell(j) != null) { // Check if cell is not empty
                        if (sheet.getRow(i).getCell(j).getCellType() == CellType.NUMERIC) {
                            if(j==0){
                            id = (int) sheet.getRow(i).getCell(j).getNumericCellValue();
                            } else if (j==2) {
                                int temp = (int) sheet.getRow(i).getCell(j).getNumericCellValue();
                                pinCode = String.valueOf(df.format(sheet.getRow(i).getCell(j).getNumericCellValue()));
                            }
                        } else if (sheet.getRow(i).getCell(j).getCellType() == CellType.STRING) {
                            // Handle string value
                            if (j == 1) {
                                name = sheet.getRow(i).getCell(j).getStringCellValue();
                            } else if (j == 2) {
                                pinCode = sheet.getRow(i).getCell(j).getStringCellValue();
                            }
                        }
                    }
                }
                if (!(id == 0 || name == null ||pinCode == null )){
                    // Create student object and add to list
                    students.add(new Student(id, name, pinCode));
                }
            }

            // Print student information
            for (Student student : students) {
                System.out.println("Student ID: " + student.getId());
                System.out.println("Student Name: " + student.getName());
                System.out.println("Student PinCode: " + student.getPincode());
                System.out.println("--------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}
