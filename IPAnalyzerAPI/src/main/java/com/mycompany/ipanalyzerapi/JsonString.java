/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import kong.unirest.json.JSONObject;

/**
 *
 * @author sertv
 */
public class JsonString {
    private static final String JSON = "{\"fullip\": {\"geo\": {\"address\": \"51.121.5.250\", \"hostname\": \"\", \"country\": \"US\", \"country_names\": {\"de\": \"USA\", \"en\": \"United States\", \"es\": \"Estados Unidos\", \"fr\": \"\\u00c9tats-Unis\", \"ja\": \"\\u30a2\\u30e1\\u30ea\\u30ab\\u5408\\u8846\\u56fd\", \"pt-BR\": \"Estados Unidos\", \"ru\": \"\\u0421\\u0428\\u0410\", \"zh-CN\": \"\\u7f8e\\u56fd\"}, \"country_geoname_id\": 6252001, \"continent\": \"NA\", \"continent_names\": {\"de\": \"Nordamerika\", \"en\": \"North America\", \"es\": \"Norteam\\u00e9rica\", \"fr\": \"Am\\u00e9rique du Nord\", \"ja\": \"\\u5317\\u30a2\\u30e1\\u30ea\\u30ab\", \"pt-BR\": \"Am\\u00e9rica do Norte\", \"ru\": \"\\u0421\\u0435\\u0432\\u0435\\u0440\\u043d\\u0430\\u044f \\u0410\\u043c\\u0435\\u0440\\u0438\\u043a\\u0430\", \"zh-CN\": \"\\u5317\\u7f8e\\u6d32\"}, \"continent_geoname_id\": 6255149, \"latitude\": 0, \"longitude\": 0, \"time_zone\": \"America/Chicago\", \"region\": \"\", \"region_names\": {}, \"region_geoname_id\": -1, \"city\": \"\", \"city_names\": {}, \"city_geoname_id\": -1, \"accuracy_radius\": 1000, \"postal\": \"\", \"as\": \"\"}, \"hostname\": \"\", \"baddomain\": {\"domain\": {}, \"source_ip\": {}, \"ip\": {}, \"score\": 0}, \"badip\": {\"score\": 0, \"blacklists\": []}, \"history\": {\"score\": 0, \"activity\": [], \"score_1day\": false, \"score_7days\": false, \"score_30days\": false, \"score_90days\": false, \"score_180days\": false, \"score_1year\": false}, \"score\": 0, \"whois\": {}}}";
    
    
    public static void main(String args[]){
        JSONObject json = new JSONObject(JSON).getJSONObject("fullip");
        
        //Build the String reponse
        StringBuilder sb = new StringBuilder("Hostname : ");
        
        //Geo characteristics of the ip
        JSONObject geo = json.getJSONObject("geo");
        
        //Address
        sb.append("IP :").append(geo.getString("address"))
          .append("\n");
        
        //Hostname of the ip
        sb.append(geo.getString("hostname"))
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
          
        System.out.println(sb.toString());
        
    }
}
