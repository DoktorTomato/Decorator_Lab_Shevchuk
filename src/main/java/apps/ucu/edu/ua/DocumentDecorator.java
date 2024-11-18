package apps.ucu.edu.ua;

public class DocumentDecorator implements Document{
    private  Document document;
    
    public DocumentDecorator(Document document) {
        this.document = document;
    }

    @Override
    public String parse(String path) {
        return document.parse(path);
    }
    
}
