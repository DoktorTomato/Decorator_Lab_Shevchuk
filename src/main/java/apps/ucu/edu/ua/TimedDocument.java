package apps.ucu.edu.ua;

public class TimedDocument extends DocumentDecorator {
    public TimedDocument(Document document){
        super(document);
    }

    public String parse(String path){
        long start = System.nanoTime();
        String result = super.parse(path);
        long end = System.nanoTime();
        double durationInSec = (end - start) / 1_000_000_000.0;
        System.out.println("Time taken:" + durationInSec);
        return result;
    }
}
