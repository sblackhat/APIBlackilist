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
import java.net.UnknownHostException;
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
    private final Map<String,String> ips = new HashMap<>();
    
   boolean checkIfPublicIP(String str){
        try {
            InetAddress add = InetAddress.getByName(str);
            if(add.isSiteLocalAddress() || add.isLoopbackAddress() || add.isMulticastAddress())
                return false;
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("Invalid IP " + str);
        }
        return false;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
    void readFile(){
    File capture;
    //Check if the path is null
    if(path.isBlank()){
    //Obtain the working directory
    StringBuilder workingDirectory = new StringBuilder(System.getProperty("user.dir"));
    capture = new File(workingDirectory.toString()); 
    }else{
        capture = new File(path);
    }
    try{
     FileReader reader = new FileReader(capture);
     BufferedReader rd = new BufferedReader(reader);
     String s;
     while((s = rd.readLine()) != null){
         StringTokenizer str = new StringTokenizer(s);
         while(str.hasMoreElements()){
             String ip = str.nextToken().trim().replaceAll("[^.a-zA-Z0-9]", "");
             if(!ip.isBlank() && checkIfPublicIP(ip) && !ips.containsValue(ip)){
                 ips.put(ip,ip);
             }
         }
     }
    
    }catch(FileNotFoundException  e ){
        System.out.println("File not found ");
    }
    catch(IOException e){
        System.out.println("Error while reading the file");
    }
    }

    public Collection<String> getIps() {
        return Collections.unmodifiableCollection(ips.values());
    }
    
    /*
public static void main(String args[]){
    ReadCaptures rd = new ReadCaptures();
    System.out.println(System.getProperty("user.dir"));
    rd.setPath("C:\\Users\\sertv\\Desktop\\output.txt");
    rd.readFile();
    //rd.ips.forEach((k, v) -> System.out.println("IP : " + v)); 

    
}*/
}
