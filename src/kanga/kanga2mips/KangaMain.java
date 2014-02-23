package kanga.kanga2mips;

import java.io.ByteArrayInputStream;

import Mainclass.Mainclass;
import kanga.KangaParser;
import kanga.ParseException;
import kanga.TokenMgrError;
import kanga.syntaxtree.Node;
import kanga.visitor.GJDepthFirst;

public class KangaMain {

	public static void Start() {
		try {
			Node root = new KangaParser(new ByteArrayInputStream(Mainclass.source.getBytes())).Goal();
	
			root.accept(new TranslateVisitor(), new KType());
			
			Console.ResultOutput();
		} catch (TokenMgrError e) {
			// Handle Lexical Errors
			e.printStackTrace();
		} catch (ParseException e) {
			// Handle Grammar Errors
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}