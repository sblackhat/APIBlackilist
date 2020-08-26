/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.util.Scanner;

/**
 *
 * @author sertv
 */
public class IPAnalyzerAPI {
    
    
    public static void main(String args[]) throws Exception{
        RequestHandler rq = new RequestHandler();
        Scanner sc=new Scanner(System.in);
        ReadCaptures rd = new ReadCaptures();
        WriteIPs wc = new WriteIPs();
        
        System.out.println("Introduce the path where the txt file is : ");
        
        //Get user input (path) and set it in the reader   
        rd.setPath(sc.nextLine());
        
        //Read the capture
        rd.readFile();
        
        //Set the IPs in the handler
        rq.setIps(rd.getIps());
        
        //Handle the request
        
        if (rq.handle().isEmpty() || rq.handle().isBlank()){
        //Introduce the output path file
        System.out.println("Introduce the path where the txt file is : ");
        //Get user input
        wc.setPath(sc.nextLine());
        //Get the collection of malicious IPS
        wc.setMalicious(rq.getMalicious());
        //Write the malicious ips
        wc.write();
        }
        
        
    }
}
