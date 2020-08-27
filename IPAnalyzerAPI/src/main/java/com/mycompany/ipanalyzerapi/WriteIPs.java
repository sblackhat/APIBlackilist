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
    private static final String EXTENSION = ".txt";
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

    public void write() {
        boolean done = false;
        String fileName = "\\malicious";
        int counter = 0;
        do {
            
            try {
                //Keep adding number until an available name is found
                String c = (counter == 0) ? "" : String.valueOf(counter);
                
                File myFile = new File(path + fileName + c + EXTENSION);
                if (myFile.createNewFile()) {
                    //Write all the ips
                    try ( FileWriter myWriter = new FileWriter(myFile)) {
                        for (String str : malicious) {
                            myWriter.write(str + "\n");
                        }
                        //Close the writer
                        myWriter.close();
                        done = true;
                    }
                } else {
                    if(counter == 0) System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
            }
        counter++;
        } while (!done);
    }
    /*
    public static void main(String args[]){
        WriteIPs wc = new WriteIPs();
        wc.setPath("C:\\Users\\sertv\\Desktop");
        Collection<String> lista = new ArrayList<>();
        lista.add("adddd");
        lista.add("holaaaa");
        wc.setMalicious(lista);
        wc.write();
    }*/
}
