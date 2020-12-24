/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import com.mycompany.ipanalyzerapi.GUI.Output;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
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
    private final List<String> keys = new ArrayList<String>();
    private Collection<String> ips = new ArrayList<String>();
    private final Collection<String> malicious = new ArrayList<String>();

    public void setIps(Collection<String> ips) {
        this.ips = ips;
        setKeys();
    }

    public Collection<String> getMalicious() {
        return Collections.unmodifiableCollection(malicious);
    }
    
    private void setKeys(){
        final String key1 = "b98bc2ef-94a3-4a59-b9bf-669b796d820a";
        final String key2 = "7c1ee8ae-b0c1-4daf-98a5-57531be921c9";
        keys.add(key1);
        keys.add(key2);
    }
    
    private String parseJSON(JSONObject input){
        //Build the JSONObject 
        JSONObject json = input.getJSONObject(JSONKEY);
         //Build the String reponse
        StringBuilder sb = new StringBuilder();
        
        //Geo characteristics of the ip
        JSONObject geo = json.getJSONObject("geo");
        
        //Address
        sb.append("IP : ").append(geo.getString("address"))
          .append("\n");
        
        //Hostname of the ip
        sb.append("Hostname : ");
                
        try{
        sb.append(geo.getString("hostname"));
        }catch(Exception e){
            sb.append("");
        }
        sb.append("\n");
        
        //Geographical location of the ip
        try{
          sb.append("GEO : ").append(geo.getString("country"))
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
        }catch(Exception e){
            sb.append("Could not get the information about the location \n");
        }
        //IP score characteristics 
             
          JSONObject badip = json.getJSONObject("badip");    
          try{      
        //Score of the IP
          sb.append("Score : ").append(badip.getString("score"))
          .append("\n")
        //Blacklists in which the ip is included
          .append("Blacklists : ").append(badip.get("blacklists"))
          .append("\n\n");
          }catch(Exception e){
              sb.append("Could not get the information about the score and blacklists");
          }
          
    return sb.toString();}
    
    private void fullResponse(String ip) throws Exception{
            //Builder for the URI
            StringBuilder URL = new StringBuilder(FULLCHECKER);
            
            //Build the query with the parameters
            URL.append(ip).append("?")
              .append(String.format("token=%s",
            URLEncoder.encode(keys.get(1), charset)));
            
            //System.out.println("Checking IP : " + ip);
            //System.out.println("....");
            
           HttpResponse<String> response = Unirest.get(URL.toString())
            .header("accept", "application/json")
            .asString();
            
            //Check the response of the server
            if(response.getStatus() == 200){
               // try{
                    malicious.add(parseJSON(new JSONObject(response.getBody())));  //Add the parsed response to the list of responses
               // }catch (Exception e ){
                  //  System.out.println("Error while parsing ");
                //} 
            }else{
                System.out.println("Error while handling the request!");
            }
            
         }
    
    
    /**
     * Returns true if the Collection contained in the class
     * contains any malicious IP
     * */
    public boolean handle(Output out) throws Exception{
        int tries = 0;
        //Set the progress bar increments of each iteration
        int increment = (int) Math.floor(80/ips.size());
        if (increment == 0 ) increment++;
        
        out.setMessage("");
        //Traverse all the ips in the list
         for(String ip : ips){
             
            StringBuilder URL = new StringBuilder(SIMPLECHECKER);
            
            URL.append(ip).append("?")
              .append(String.format("token=%s",
            URLEncoder.encode(keys.get(tries), charset)));
            
            //Console output
            StringBuilder str = new StringBuilder(out.getMessage());
            str.append("Checking IP : ").append(ip).append("\n...");
            
            out.addMessage(str.toString());
            
            HttpResponse<String> response = Unirest.get(URL.toString())
                .header("accept", "application/json")
                .asString();
            
            //Check the response of the server
            if(response.isSuccess()){ //200 if malicious
                //Set the output
                out.addProgressBar(increment);
                str.append("\nIP ").append(ip).append(" was found to be malicious \n");
                out.setMessage(str.toString());
                //Go for the full report if the ip is malicious
                fullResponse(ip);
                tries = 0;
            }else if(response.getStatus() == 429){ //409 if daily quota exceeded
                str.append("\nDaily quota exceeded!\n");
                out.setMessage(str.toString());
                //Get the next key and wait for 60 seconds
                tries++;
                if(tries >= keys.size()) break;
            }else{
                tries = 0;
                str.append("\nOK!\n");
                out.setMessage(str.toString());
                //System.out.println("OK!"); //404 for clean ip
            }
        }
         Unirest.shutDown();
    return !malicious.isEmpty(); }
    /*
    public static void main(String args[]) throws Exception{
        RequestHandler request = new RequestHandler();
        request.ips.add("95.101.34.19");
        request.handle();
    }*/
}
