/*
 * The dynamic java compiler.
 * Copyright (C) 2022 Omega UI

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package omegaui.dynamic.compiler;

import java.util.LinkedList;

import java.nio.charset.StandardCharsets;

import java.io.File;

import javax.tools.DiagnosticCollector;
import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject;

/**
 * JavaDynamicCompiler
 * This class is capable of compiling java projects dynamically that don't use any build systems like gradle.
 */ 

public final class JavaDynamicCompiler {

	/**
	 * The Compiler Object
	 */
	public static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	/**
	 * An Internal Method to load all .class files that belong to a specific class file
	 */
	private synchronized static void loadByteCodes(LinkedList<File> fileList, File dir, LinkedList<File> clazzList){
		File[] F = dir.listFiles();
		if(F == null || F.length == 0)
			return;
		String fileName;
		String path;
		main:
		for(File file : F){
			if(file.isDirectory())
				loadByteCodes(fileList, file, clazzList);
			else if((fileName = file.getName()).endsWith(".class")){
				for(File fx : clazzList){
					path = fx.getAbsolutePath();
					path = path.substring(path.lastIndexOf(File.separatorChar) + 1, path.lastIndexOf('.'));
					if(fileName.startsWith(fileName) || fileName.startsWith(fileName + "$")){
						fileList.add(file);
						continue main;
					}
				}
			}
		}
	}

	/**
	 * An Internal Method to refresh the bin directory's class cache!
	 */
	private synchronized static void clearBinDir(String binDir, LinkedList<File> clazzList){
		LinkedList<File> files = new LinkedList<>();
		loadByteCodes(files, new File(binDir), clazzList);
		files.forEach(file->file.delete());
	}

	/**
	 * The actual method which performs the compilation!
	 * @param projectPath - The Path of the Project
	 * @param classpath - The Projects' classpath
	 * @param clazzList - The list of files to be recompiled (its better to provide only changed files for a faster compilation)
	 */ 
	public synchronized static DiagnosticCollector<JavaFileObject> fastCompile(String projectPath, String[] classpath, LinkedList<File> clazzList){
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

		String precompiledByteCodesDir = projectPath + File.separator + "bin";
		clearBinDir(precompiledByteCodesDir, clazzList);

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);

		LinkedList<String> options = new LinkedList<>();
		options.add("-d");
		options.add(precompiledByteCodesDir);
		options.add("-cp");

		//Putting already compiled byte codes on classpath as we need them for selective compilation!
		String clzPath = precompiledByteCodesDir + File.pathSeparator;
		for(String px : classpath){
			clzPath += px + File.pathSeparator;
		}

		options.add(clzPath + ".");

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(clazzList);
		compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call();
		try{
			fileManager.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.gc();
		return diagnostics;
	}
}
