/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ipanalyzerapi;

import com.profesorfalken.jpowershell.PowerShell;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author sertv
 */
public class ExecutePowerShell {
    
    private static final String COMMAND1 = "tshark -r ";
    private static final String COMMAND2 = " -T fields -e ip.dst -e ip.src";
    
    public static String command(String file){
        StringBuilder sb = new StringBuilder(COMMAND1);
        sb.append(file);
        sb.append(COMMAND2);
        return PowerShell.executeSingleCommand(sb.toString()).getCommandOutput();
    }
    
    /*
    public static void main(String args[]){
       // System.out.println(ExecutePowerShell.command());
       /* ExecutePowerShell e = new ExecutePowerShell();
        System.out.println(e.getClass().getResource("icon.png"));
        System.out.println(System.getProperty("user.dir")+"\\files\\icon.png");
        
       StringBuilder sb = new StringBuilder(COMMAND1);
       sb.append("holaaa");
       sb.append(COMMAND2);
        System.out.println(sb.toString());
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WireShark","pncap");
        System.out.println(filter.getDescription());
    }*/
}
