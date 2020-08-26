/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import kong.unirest.json.JSONObject;

/**
 *
 * @author sertv
 */
public class RequestHandler {
    private final String charset = "UTF-8";
    private final String badIP = "https://signals.api.auth0.com/badip/";
    private final String badFullIP = "https://signals.api.auth0.com/v2.0/ip/";
    private final String apiKey = "b98bc2ef-94a3-4a59-b9bf-669b796d820a";
    private Collection<String> ips = new ArrayList<String>();
    private final Collection<String> malicious = new ArrayList<String>();

    public void setIps(Collection<String> ips) {
        this.ips = ips;
    }

    public Collection<String> getMalicious() {
        return Collections.unmodifiableCollection(malicious);
    }
    
    private String parseJSON(JSONObject json){
        
        
    }
    
    /**
     * Returns true if the Collection contained in the class
     * contains any malicious IP
     * */
    public String handle() throws Exception{
        String result  = "";
        /* Simple badIP Checker
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
        }*/
         for(String ip : ips){
            
             StringBuilder sb = new StringBuilder(badFullIP);
            
            //Build the query with the parameters
            sb.append(ip).append("?")
              .append(String.format("token=%s",
            URLEncoder.encode(apiKey, charset)));
            
            System.out.println("Checking IP : " + ip);
            System.out.println("....");
            //System.out.println("URL : " + str);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                //.setHeader("X-Auth-Token", apiKey)
                .setHeader("Accept", "application/json")
                .uri(URI.create(sb.toString()))
                .build();
            
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
            /*
             //Print the request
             System.out.println("Request -> " +request.toString());
              
             HttpHeaders hd = request.headers();
             System.out.println(hd.toString());
             
             //Print the response
             System.out.println("Response -> " + response.toString());
            //Print Status Code
             System.out.println("Status Code -> " + response.statusCode());
            */
            if(response.statusCode() == 200){
                result = parseJSON(new JSONObject(response.toString()));
                
            }else{
                System.out.println("Error while handling the request!");
            }
         }
        
    return result; }
    
    public static void main(String args[]) throws Exception{
        RequestHandler request = new RequestHandler();
        request.ips.add("8.8.8.8");
        request.handle();
    }
}
