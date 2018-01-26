import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader implements AsynchronousStringReader, Runnable {

    String[] resources;
    int id;
    String filename;

    public FileReader(String[] resources, int id, String filename) {
        this.resources = resources;
        this.id = id;
        this.filename = filename;
    }

    @Override
    public void read(String[] resources, int id, String filename) {
        try{
            byte[] bytes = Files.readAllBytes(Paths.get(filename));
            resources[id] = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Unable to read the files. Stopping the program");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        read(resources, id, filename);
    }
}
