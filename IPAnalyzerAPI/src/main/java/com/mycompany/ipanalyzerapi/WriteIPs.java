/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sertv
 */
public class WriteIPs {
    private Collection<String> malicious = new ArrayList<String>();
    private String path;

    public WriteIPs() {
    }
    
    public void setMalicious(Collection<String> malicious) {
        this.malicious = malicious;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
    public void write(){
      try {
        File myFile = new File(path + "\\malicious.txt");
        if (myFile.createNewFile()) {
            //Write all the ips
            try (FileWriter myWriter = new FileWriter(myFile)) {
                for (String str : malicious) {
                    myWriter.write(str+"\n");
                }
                //Close the writer
                myWriter.close();
            }
        } else {
            System.out.println("File already exists.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred.");
    }
    }
    
}
