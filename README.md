# fast-java-dynamic-compiler
A library for IDEs and Code Editors to compile java projects faster dynamically.

# Usage
Note that the project, that is to be compiled dynamically, must have already been compiled at least once!

All you need to do is call `JavaDynamicCompiler.fastCompile`.

```java
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
		files.add(new File("/home/ubuntu/Documents/Omega Projects/Test Dynamic Compiler/src/test/TestDependentFile.java"));

		String[] classpath = new String[]{"/home/ubuntu/Downloads/jediterm.jar", "/home/ubuntu/Downloads/dependency.jar"};
		
		DiagnosticCollector<JavaFileObject> diagnostics = JavaDynamicCompiler.fastCompile(projectPath, classpath, files);
		diagnostics.getDiagnostics().forEach(System.out::println);
	}
	
}
```

`files` is the list of files that have changed contents since last compilation, thus, recompiling only the changed files.

The above code will compile and generate the `.class` files in `bin` directory of the project.

