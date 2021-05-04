package wetalk_client.utils;

import wetalk_client.App;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * A helper class for saving and reading local files
 * Singleton design pattern
 */
public class LocalStorage {
    private static final LocalStorage instance = new LocalStorage();
    File tempDir;

    private LocalStorage() {
        try {
            this.tempDir = new File(App.class.getResource("").toURI().getPath(), Global.getInstance().getProperty("tempPath"));
        } catch (URISyntaxException e) {
            System.out.println("Invalid path for temp foler");
            System.exit(-1);
        }
        if(!this.tempDir.exists()) {
            this.tempDir.mkdirs();
        }
    }

    /**
     * Saves string data to a file named fileName
     * @param fileName target file's name
     * @param data string data that user wants to store
     * @throws IOException when can not access the file
     */
    public void saveDataToFile(String fileName, String data) throws IOException {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        if(!tempFile.exists()) {
            tempFile.createNewFile();
        }
        OutputStream tempFileStream = new FileOutputStream(tempFile);
        tempFileStream.write(data.getBytes(StandardCharsets.UTF_8));
        tempFileStream.close();
    }

    /**
     * Reads string data from a file named fileName
     * @param fileName target file's name
     * @return string data read from the file
     * @throws IOException when can not access the file
     */
    public String readDataFromFile(String fileName) throws IOException {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        if(!tempFile.exists()) {
            return null;
        }
        InputStream tempFileStream = new FileInputStream(tempFile);
        InputStreamReader tempFileStreamReader = new InputStreamReader(tempFileStream, StandardCharsets.UTF_8);
        BufferedReader tempFileBufferedReader = new BufferedReader(tempFileStreamReader);
        String data = "";
        while(tempFileBufferedReader.ready()) {
            String temp = tempFileBufferedReader.readLine();
            if(temp != null) {
                data += temp;
            } else {
                break;
            }
        }
        tempFileBufferedReader.close();
        tempFileStreamReader.close();
        tempFileStream.close();
        return data;
    }

    /**
     * Deletes file named fileName
     * @param fileName the file's name that wants to delete
     */
    public void deleteFile(String fileName) {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        if(!tempFile.exists()) {
            return;
        }
        tempFile.delete();
    }

    /**
     * Returns the only one instance of LocalStorage
     * @return an instance of LocalStorage
     */
    public static LocalStorage getInstance() {
        return LocalStorage.instance;
    }
}
