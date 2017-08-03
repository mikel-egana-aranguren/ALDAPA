package es.eurohelp.lod.aldapa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;

import org.apache.commons.io.IOUtils;

import es.eurohelp.lod.aldapa.core.MethodFileToken;

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
	 * @param fileName
	 *            the file name including path
	 * 
	 * @return a FileOutputStream stream for writing to the specified file
	 * @throws IOException input/output problem with the file to write to
	 *
	 */

	public FileOutputStream getFileOutputStream(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}

	/**
	 * 
	 * Resolves the occurrences of a single token of a file with the replacement URIs
	 * 
	 * @param file_name
	 *            the name of the file
	 * @param token
	 *            the token to search for in the file
	 * @param replacement
	 *            the value to replace the token with
	 * @return the new file content, resolved
	 * @throws IOException input/output exception regarding the target file
	 * 
	 */

	public String fileTokenResolver(String file_name, String token, String replacement) throws IOException {
		InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(file_name);
		String unresolved_string = IOUtils.toString(in, StandardCharsets.UTF_8);
		String resolved_string = unresolved_string.replaceAll(token, "<" + replacement + ">");
		return resolved_string;
	}

	/**
	 *  Resolves the occurrences of multiple tokens of a file with the replacement URIs
	 * 
	 * @param file_name the name of the file with the actual content
	 * @param token_replacement_map a map containing pairs of tokens and replacements
	 * @return the new file content, resolved
	 * @throws IOException input/output exception regarding the target file
	 */
	public String fileMultipleTokenResolver(String file_name, EnumMap<MethodFileToken, String> token_replacement_map) throws IOException { 
		InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(file_name);
		String unresolved_string = IOUtils.toString(in, StandardCharsets.UTF_8);
		String resolved_string = unresolved_string;
		for (MethodFileToken token : token_replacement_map.keySet()) {
			String token_value = token.getValue();
			String replacement_value = token_replacement_map.get(token);
			resolved_string = resolved_string.replaceAll(token_value, "<" + replacement_value + ">");
		}
		return resolved_string;
	}
}
