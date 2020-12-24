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

    private void parsePath(String s) {

    }

    public void setMalicious(Collection<String> malicious) {
        this.malicious = malicious;
    }

    public void setPath(String path) {
        //parsePath();
        this.path = path;
    }

    public void write() {
        boolean done = false;
        int counter = 0;
        String c = "";

        do {

            try {
                //Keep adding number until an available name is found
                c = (counter == 0) ? "" : String.valueOf(counter);

                File myFile = new File(path + c + EXTENSION);
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
                    if (counter == 0) {
                        System.out.println("File already exists.");
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
            }
            counter++;
        } while (!done);
        System.out.println("File stored : " + path + c + EXTENSION);
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
/**
 * Banned filenames in Windows OS
 */
    private enum BannedNames {
        CON("CON"),
        PRN("PRN"),
        AUX("PRN"),
        NUL("NUL"),
        COM1("COM1"),
        COM2("COM2"),
        COM3("COM3"),
        COM4("COM4"),
        COM5("COM5"),
        COM6("COM6"),
        COM7("COM7"),
        COM8("COM8"),
        COM9("COM9"),
        LPT1("LPT1"),
        LPT2("LPT2"),
        LPT3("LPT3"),
        LPT4("LPT4"),
        LPT5("LPT5"),
        LPT6("LPT6"),
        LPT7("LPT7"),
        LPT8("LPT8"),
        LPT9("LPT9");

        private final String name;

        /**
         * @param text
         */
        BannedNames(final String name) {
            this.name = name;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return name;
        }
    }

}
