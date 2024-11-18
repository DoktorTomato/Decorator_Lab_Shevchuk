package apps.ucu.edu.ua;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class CachedDocument extends DocumentDecorator{
    public CachedDocument(Document doc){
        super(doc);
    }
    public String parse(String path){
        String databaseUrl = "jdbc:sqlite:db.sqlite3";
        String tableName = "pathes";
        String parsedString = "cached documetn";

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");

        try {
            // Read text from the image
            parsedString = tesseract.doOCR(new File(path));
            System.out.println("Extracted text:");
            System.out.println(parsedString);
            try (Connection conn = DriverManager.getConnection(databaseUrl)) {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                            + "\"path\" TEXT PRIMARY KEY,"
                            + "parsed_string TEXT"
                            + ");";
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(createTableSQL);
                    }
    
                    // Check if path exists
                    String checkPathSQL = "SELECT COUNT(*) FROM " + tableName + " WHERE path = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(checkPathSQL)) {
                        pstmt.setString(1, path);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next() && rs.getInt(1) == 0) {
                            // Path does not exist, insert it
                            String insertSQL = "INSERT INTO " + tableName + " (path, parsed_string) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                                insertStmt.setString(1, path);
                                insertStmt.setString(2, parsedString);
                                insertStmt.executeUpdate();
                                System.out.println("Path added to the table.");
                            }
                        } else {
                            System.out.println("Path already exists in the table.");
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (TesseractException e) {
            System.err.println("Error occurred while processing the image: " + e.getMessage());
        }
        String result = super.parse(path);
        return result;
    }
}