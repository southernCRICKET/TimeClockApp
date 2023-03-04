package com.timeclockapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileMethods {
    public static int CreateFile(String name, String outputToFile)
    {
        try{
            File file = new File(name);
            if(!file.createNewFile())
                return 0;
            FileOutputStream fos = new FileOutputStream(name);
            fos.write(outputToFile.getBytes());
            fos.close();
        } catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

    public static String ReadFile(String name){
        try{
            return Files.readString(Paths.get(name));
        } catch (IOException e){
            System.out.println("Error while reading JSON");
            e.printStackTrace();
        }
        return "0";
    }
}
