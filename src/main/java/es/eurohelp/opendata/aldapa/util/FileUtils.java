package es.eurohelp.opendata.aldapa.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains utilities for workings with files.
 *
 * @author acarbajo
 *         Created at 14 de oct. de 2016
 */
public class FileUtils {

	private static FileUtils INSTANCE = null;
	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

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
}
