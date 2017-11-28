package es.eurohelp.lod.aldapa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.core.MethodFileToken;

/**
 * Contains utilities for workings with files.
 *
 * @author acarbajo
 *         Created at 14 de oct. de 2016
 * 
 * @author megana
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
    public static synchronized FileUtils getInstance() {
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
     * Returns a FileOutputStream stream for writing to a File. If the file does not exist, it creates it. Client should
     * close the FileOutputStream
     * 
     * @param fileName
     *            the file name including path
     * 
     * @return a FileOutputStream stream for writing to the specified file
     * @throws IOException
     *             input/output problem with the file to write to
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
     * @param fileName
     *            (Project resource)
     * @return a String containing the content of the file
     * @throws IOException
     */
    public String fileToString(String fileName) throws IOException {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        return IOUtils.toString(in, StandardCharsets.UTF_8);
    }

    /**
     * 
     * Creates a File if it doesn't exist, retrieving FileInputStream
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public FileInputStream getFileInputStream(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return new FileInputStream(file);
    }

    public void createFile(String filePath) throws IOException {
        LOGGER.info("Create file: " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * @param fileName
     *            (file system)
     * @return true if file is empty
     * @throws IOException
     */
    public boolean fileIsEmpty(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        int b = fis.read();
        fis.close();
        return b == -1;
    }

    /**
     * 
     * Resolves the occurrences of a single token of a file with the replacement URIs
     * 
     * @param file_name
     *            the name of the file (resource)
     * @param token
     *            the token to search for in the file
     * @param replacement
     *            the value to replace the token with
     * @return the new file content, resolved
     * @throws IOException
     *             input/output exception regarding the target file
     * 
     */

    public String fileTokenResolver(String fileName, String token, String replacement) throws IOException {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        String unresolvedString = IOUtils.toString(in, StandardCharsets.UTF_8);
        return unresolvedString.replaceAll(token, replacement);
    }

    /**
     * Resolves the occurrences of multiple tokens of a file with the replacement URIs
     * 
     * @param file_name
     *            the name of the file with the actual content (resource)
     * @param token_replacement_map
     *            a map containing pairs of tokens and replacements
     * @return the new file content, resolved
     * @throws IOException
     *             input/output exception regarding the target file
     */
    public String fileMultipleTokenResolver(String fileName, Map<MethodFileToken, String> tokenReplacementMap) throws IOException {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        String unresolvedString = IOUtils.toString(in, StandardCharsets.UTF_8);
        String resolvedString = unresolvedString;
        for (MethodFileToken token : tokenReplacementMap.keySet()) {
            String tokenValue = token.getValue();
            String replacementValue = tokenReplacementMap.get(token);
            resolvedString = resolvedString.replaceAll(tokenValue, replacementValue);
        }
        return resolvedString;
    }

    /**
     * 
     * Creates a directory. If the directory already exists, returns false
     * 
     * @param dirPath
     * @return
     */
    public boolean createDir(String dirPath) {
        File directory = new File(dirPath);
        return directory.mkdir();
    }

    public void appendContentToFile(String filename, String lineToAdd) throws IOException {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(lineToAdd + "\n");
        }
    }

    /**
     * @param outputpath
     */
    public void deleteElement(String elementPath) {
        try {
            deleteElement(new File(elementPath));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private static void deleteElement(File element) throws IOException {
        if (element.exists()) {
            if (element.isDirectory()) {
                for (File sub : element.listFiles()) {
                    deleteElement(sub);
                }
            }
            element.delete();
            LOGGER.info("Deleting element: " + element);
        }else {
            throw new IOException("Element " + element + " does not exist");
        }
    }
}
