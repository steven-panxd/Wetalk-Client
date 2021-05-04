package wetalk_client.utils;

import wetalk_client.App;

import java.io.*;
import java.util.Properties;

/**
 * Global variables of current running client
 * Contains properties read from config.properties
 * Can store temporary variables
 * Singleton Design Pattern, only one instance for current client
 */
public class Global extends Properties {
    private static final Global instance = new Global();

    private Global() {
        Properties props = new Properties();

        // read data from config.properties
        try {
            InputStream stream = App.class.getResourceAsStream("config.properties");
            props.load(stream);
            stream.close();
        } catch (IOException exception) {
            System.out.println("Can not read file config.properties");
            System.exit(-1);
        }

        // load data from config.properties to Global
        for(String propertyName: props.stringPropertyNames()) {
            this.setProperty(propertyName, props.getProperty(propertyName));
        }
    }

    /**
     * Get the only one instance of Global for current client
     * @return an instance of Global
     */
    public static Global getInstance() { return Global.instance; }
}
