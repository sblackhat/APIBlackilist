/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
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
    private static final String JSONKEY = "fullip";
    private final String charset = "UTF-8";
    private final static String SIMPLECHECKER = "https://signals.api.auth0.com/badip/";
    private final static String FULLCHECKER = "https://signals.api.auth0.com/v2.0/ip/";
    private final String apiKey = "b98bc2ef-94a3-4a59-b9bf-669b796d820a";
    private Collection<String> ips = new ArrayList<String>();
    private final Collection<String> malicious = new ArrayList<String>();

    public void setIps(Collection<String> ips) {
        this.ips = ips;
    }

    public Collection<String> getMalicious() {
        return Collections.unmodifiableCollection(malicious);
    }
    
    private String parseJSON(JSONObject input){
        //Build the JSONObject 
        JSONObject json = input.getJSONObject("fullip");
         //Build the String reponse
        StringBuilder sb = new StringBuilder();
        
        //Geo characteristics of the ip
        JSONObject geo = json.getJSONObject("geo");
        
        //Address
        sb.append("IP : ").append(geo.getString("address"))
          .append("\n");
        
        //Hostname of the ip
        sb.append("Hostname : ").append(geo.getString("hostname"))
          .append("\n")
        //Geographical location of the ip
          .append("GEO : ").append(geo.getString("country"))
          .append(" -> ").append(geo.getJSONObject("country_names").getString("en"))
          .append("\n")
         //Latitude coordinate
          .append("Latitude : ").append(geo.getString("latitude"))
          .append("\n")
         //Longitude coordinate
          .append("Longitude : ").append(geo.getString("longitude"))
          .append("\n")
         //Time zone
          .append("Time zone : ").append(geo.getString("time_zone"))
          .append("\n");
        //IP score characteristics 
             
          JSONObject badip = json.getJSONObject("badip");    
                
        //Score of the IP
          sb.append("Score : ").append(badip.getString("score"))
          .append("\n")
        //Blacklists in which the ip is included
          .append("Blacklists : ").append(badip.get("blacklists"));
          
    return sb.toString();}
    
    private void fullResponse(String ip) throws UnsupportedEncodingException, IOException, InterruptedException{
            //Builder for the URI
            StringBuilder sb = new StringBuilder(FULLCHECKER);
            
            //Build the query with the parameters
            sb.append(ip).append("?")
              .append(String.format("token=%s",
            URLEncoder.encode(apiKey, charset)));
            
            System.out.println("Checking IP : " + ip);
            System.out.println("....");
            
            //Set up the connection
            HttpClient client = HttpClient.newHttpClient();
            
            //Build the request
            HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Accept", "application/json")
                .uri(URI.create(sb.toString()))
                .build();
            
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
            
            //Check the response of the server
            if(response.statusCode() == 200){
                try{
                String result = parseJSON(new JSONObject(response.body())); //Parse the response
                malicious.add(result);  //Add the parsed response to the list of responses
                }catch (Exception e ){
                    System.out.println("Error while parsing " + e.getCause());
                } 
            }else{
                System.out.println("Error while handling the request!");
            }
         }
    
    
    /**
     * Returns true if the Collection contained in the class
     * contains any malicious IP
     * */
    public boolean handle() throws Exception{
        String result  = "";
        //Traverse all the ips in the list
         for(String ip : ips){
             
            StringBuilder URL = new StringBuilder(SIMPLECHECKER);
            
            URL.append(ip).append("?")
              .append(String.format("token=%s",
            URLEncoder.encode(apiKey, charset)));
            
            //Console output
            System.out.println("Checking IP : " + ip);
            System.out.println("....");
            
            //Create the connection
            HttpClient client = HttpClient.newHttpClient();
            //Build the uri for the simple request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL.toString()))
                .build();
            //Response
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
            //Check the response of the server
            if(response.statusCode() == 200){ //200 if malicious
                System.out.println("IP "+ip+" was found to be malicious");
                //Go for the full report if the ip is malicious
                try{
                fullResponse(ip);
                }catch (Exception e){
                    System.out.println("Error while building the full response " + e.getMessage());
                }
            }else if(response.statusCode() == 429){ //409 if daily quota exceeded
                System.out.println("Daily quota exceeded!");
                break;
            }else{
                System.out.println("OK!"); //404 for clean ip
            }
            
        }
    return !malicious.isEmpty(); }
    /*
    public static void main(String args[]) throws Exception{
        RequestHandler request = new RequestHandler();
        request.ips.add("95.101.34.19");
        request.handle();
    }*/
    
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
}
