package com.mir.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


public class Topo_import {
	FileReader topo_reader;
	public JSONObject JSONMSG;

	public Topo_import(FileReader reader) throws JSONException{
		topo_reader = reader;
		try {
        	BufferedReader b_reader = new BufferedReader(reader);
        	
        	String T_string = b_reader.readLine(); 
        	String J_string = "{";
        	while(T_string != null){
	        	T_string = b_reader.readLine();
	        	if(T_string != null)
	        		J_string = J_string + T_string;
        	}

        	J_string = J_string.replaceAll("\\s+","");
        	JSONMSG = new JSONObject(J_string);       

		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
	}
	
}
