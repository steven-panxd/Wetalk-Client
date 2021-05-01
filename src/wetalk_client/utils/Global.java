package wetalk_client.utils;

import wetalk_client.App;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

/*
Singleton Pattern
Global Constance for the program
 */
public class Global extends Properties {
    private static final Global instance = new Global();

    private Global() {
        Properties props = new Properties();

        try {
            InputStream stream = App.class.getResourceAsStream("config.properties");
            props.load(stream);
            stream.close();
        } catch (IOException exception) {
            System.out.println("Can not read the config.properties");
            System.exit(-1);
        }

        for(String propertyName: props.stringPropertyNames()) {
            this.setProperty(propertyName, props.getProperty(propertyName));
        }
    }

    public static Global getInstance() { return Global.instance; }
}
