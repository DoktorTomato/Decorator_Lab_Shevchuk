package apps.ucu.edu.ua;

public class Main {
    public static void main(String[] args) {
        Document doc = new SmartDocument();
        doc.parse("parsing");
        TimedDocument tDoc = new TimedDocument(doc);
        tDoc.parse("path");
        CachedDocument cDoc = new CachedDocument(doc);
        cDoc.parse("test.png");
    }
}