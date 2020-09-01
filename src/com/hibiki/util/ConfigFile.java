package com.hibiki.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigFile {
    private static JSONObject config_settings;

    public static void load_config_file() {
        try {
            String config = new Scanner(new File("config.json")).next();
            config_settings = new JSONObject(config);
            System.out.println(config_settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getConfigSettings(String key){
        return (int) config_settings.get(key);
    }

    public static void updateConfigSetting(String key, int value){
        config_settings.put(key, value);
        String updated_json = String.valueOf(config_settings);
        try{
           FileWriter write = new FileWriter("config.json");
           write.write(updated_json);
           write.close();
           System.out.println("Successfully updated the json file");

           //After write reload the configfile
           load_config_file();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
