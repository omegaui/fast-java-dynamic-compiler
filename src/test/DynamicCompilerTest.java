package test;
import java.io.File;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import java.util.LinkedList;

import omegaui.dynamic.compiler.JavaDynamicCompiler;

public class DynamicCompilerTest {
	
	public static void main(String[] args){
		String projectPath = "/home/ubuntu/Documents/Omega Projects/Test Dynamic Compiler";

		LinkedList<File> files = new LinkedList<>();
		files.add(new File("/home/ubuntu/Documents/Omega Projects/Test Dynamic Compiler/src/test/TestFile.java"));
		
		DiagnosticCollector<JavaFileObject> diagnostics = JavaDynamicCompiler.fastCompile(projectPath, new String[]{"/home/ubuntu/Documents/Omega IDE/build/Omega IDE.jar"}, files);
		diagnostics.getDiagnostics().forEach(System.out::println);
	}
	
}
