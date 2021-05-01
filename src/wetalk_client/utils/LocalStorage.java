package wetalk_client.utils;

import wetalk_client.App;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    public void saveDataToFile(String fileName, String data) throws IOException {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        if(!tempFile.exists()) {
            tempFile.createNewFile();
        }
        OutputStream tempFileStream = new FileOutputStream(tempFile);
        tempFileStream.write(data.getBytes(StandardCharsets.UTF_8));
        tempFileStream.close();
    }

    public void appendDataToFile(String fileName, String data) throws IOException {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        System.out.println(tempFile.getPath());
        if(!tempFile.exists()) {
            tempFile.createNewFile();
        }
        OutputStream tempFileStream = new FileOutputStream(tempFile, true);
        tempFileStream.write(data.getBytes(StandardCharsets.UTF_8));
        tempFileStream.close();
    }

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

    public void deleteFile(String fileName) {
        File tempFile = new File(this.tempDir.getPath(), fileName);
        System.out.println(tempFile.getPath());
        System.out.println(tempFile.exists());
        if(!tempFile.exists()) {
            return;
        }
        tempFile.delete();
    }

    public static LocalStorage getInstance() {
        return LocalStorage.instance;
    }
}
