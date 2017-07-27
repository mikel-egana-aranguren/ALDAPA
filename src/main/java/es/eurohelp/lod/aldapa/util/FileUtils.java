package es.eurohelp.lod.aldapa.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;


/**
 * Contains utilities for workings with files.
 *
 * @author acarbajo
 *         Created at 14 de oct. de 2016
 */
public class FileUtils {

	private static FileUtils INSTANCE = null;

	/**
	 * Private constructor for FileUtils
	 * 
	 */
	private FileUtils() {
	}

	/**
	 * Retrieves the only instance of this Singleton class.
	 * 
	 * @return the FileUtils instance.
	 *
	 * @author acarbajo
	 */
	public synchronized static FileUtils getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new FileUtils();
		}
		return INSTANCE;
	}

	/**
	 * Returns an input stream for reading the specified resource.
	 * 
	 * @param name
	 *            the resource name, e.g. configuration/aldapa-default-configuration.yml
	 *            
	 * @return an input stream for reading the resource
	 *
	 * @author acarbajo
	 */
	
	public InputStream getInputStream(String name) {
		return FileUtils.class.getClassLoader().getResourceAsStream(name);
	}
	
	/**
	 * Returns a FileOutputStream stream for writing to a File. If the file does not exist, it creates it.
	 * 
	 * @param name
	 *            the file name, including path
	 *            
	 * @return a FileOutputStream stream for writing to the specified file
	 * @throws IOException 
	 *
	 */
	
	public FileOutputStream getFileOutputStream (String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}
	
	/**
	 * 
	 * Resolves the tokens of a file with the replacement URIs
	 * 
	 * @param in
	 *            the InputStream with the file
	 * @param token
	 *            the token to search for in the file
	 * @param replacement
	 *            the value to replace the token with
	 * @return the new file content, resolved
	 * @throws IOException
	 * 
	 */

	public static String fileTokenResolver(InputStream in, String token, String replacement) throws IOException {
		String unresolved_string = IOUtils.toString(in, StandardCharsets.UTF_8);
		String resolved_string = unresolved_string.replaceAll(token, "<" + replacement + ">");
		return resolved_string;
	}
}
