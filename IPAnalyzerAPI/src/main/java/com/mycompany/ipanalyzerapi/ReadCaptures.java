/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author sertv
 */
public class ReadCaptures {

    private String path;
    private final Map<String, String> ips = new HashMap<>();

    private boolean checkIfPublicIP(String str) {
        try {
            InetAddress add = InetAddress.getByName(str);

            return !(!IpValidator.isValidInet4Address(str) || add.isSiteLocalAddress()
                    || add.isLoopbackAddress() || add.isMulticastAddress());
        } catch (Exception ex) {
            System.out.println("Invalid IP " + str + ex.getMessage());
        }
        return false;
    }

    private void parser(String s) {
        StringTokenizer str = new StringTokenizer(s);
        while (str.hasMoreElements()) {
            String ip = str.nextToken().trim().replaceAll("[^.a-zA-Z0-9]", "");
            if (!ip.isBlank() && checkIfPublicIP(ip) && !ips.containsValue(ip)) {
                ips.put(ip, ip);
            }
        }
    }

    private void parseCapture(BufferedReader rd) throws IOException {
        String s;
        while ((s = rd.readLine()) != null) {
            parser(s);
        }
    }

    private void parseCapture(String s) {
        parser(s);
    }
    
    void readFile(File capture) throws FileNotFoundException, IOException{
        
            FileReader reader = new FileReader(capture);
            BufferedReader rd = new BufferedReader(reader);
            parseCapture(rd);
    }
    
    void readFile(String str){
        parseCapture(str);
    }

    public Collection<String> getIps() {
        return Collections.unmodifiableCollection(ips.values());
    }
/*
    public static void main(String args[]) {
        ReadCaptures rd = new ReadCaptures();
         System.out.println(System.getProperty("user.dir"));
    rd.setPath("C:\\Users\\sertv\\Desktop\\output.txt");
    rd.readFile();
    rd.ips.forEach((k, v) -> System.out.println("IP : " + v)); 
         
        String s = ExecutePowerShell.command();
        System.out.println("Initial " + s + "\n");
        rd.parseCapture(s);
        rd.ips.forEach((k, v) -> System.out.println("IP : " + v));
    }*/
}
