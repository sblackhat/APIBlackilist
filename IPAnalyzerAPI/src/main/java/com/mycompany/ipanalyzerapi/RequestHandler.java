/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author sertv
 */
public class RequestHandler {
    private final String charset = "UTF-8";
    private final String badIP = "https://signals.api.auth0.com/badip/";
    private final String apiKey = "b98bc2ef-94a3-4a59-b9bf-669b796d820a";
    private Collection<String> ips = new ArrayList<String>();
    private final Collection<String> malicious = new ArrayList<String>();

    public void setIps(Collection<String> ips) {
        this.ips = ips;
    }

    public Collection<String> getMalicious() {
        return Collections.unmodifiableCollection(malicious);
    }
    
    /**
     * Returns true if the Collection contained in the class
     * contains any malicious IP
     * */
    public boolean handle() throws Exception{
        
        for(String ip : ips){
            String str = badIP + ip;
            System.out.println("Checking IP : " + ip);
            System.out.println("....");
            //System.out.println("URL : " + str);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .setHeader("X-Auth-Token", apiKey)
                .uri(URI.create(str))
                .build();
            
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
            
            if(response.statusCode() == 200){
                System.out.println("IP "+ip+" was found to be malicious");
                malicious.add(ip);
            }else if(response.statusCode() == 429){
                System.out.println("Daily quota exceeded!");
            }else{
                System.out.println("OK!");
            }
        }
    return !malicious.isEmpty();}
    /*
    public static void main(String args[]) throws Exception{
        RequestHandler request = new RequestHandler();
        request.handle();
    }*/
}
