package spiglet.spiglet2kanga;

import java.io.ByteArrayInputStream;

import Mainclass.Mainclass;
import spiglet.ParseException;
import spiglet.SpigletParser;
import spiglet.TokenMgrError;
import spiglet.syntaxtree.Node;

public class SpigletMain { 
 
    public static String Start() {
    	
    	AllFlowGraphs graphs = new AllFlowGraphs();
    	
    	try {
    		Node root = new SpigletParser(new ByteArrayInputStream(Mainclass.source.getBytes())).Goal();
    		
    		root.accept(new BuildFlowGraphVisitor(), graphs);
    		
    		root.accept(new TranslateVisitor(), graphs);
    		
    		return Console.ResultOutput();
    	}
    	catch(TokenMgrError e){
    		//Handle Lexical Errors
    		e.printStackTrace();
    	}
    	catch (ParseException e){
    		//Handle Grammar Errors
    		e.printStackTrace();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return null;	
    }
}