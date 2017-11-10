package es.eurohelp.lod.aldapa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;

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
     * Returns a FileOutputStream stream for writing to a File. If the file does not exist, it creates it.
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
     * @param fileName
     *            (file system)
     * @return true if file is empty
     * @throws IOException
     */
    public boolean isFileEmpty(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        int b = fis.read();
        fis.close();
        return (b == -1);
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
     * @throws IOException
     *             input/output exception regarding the target file
     * 
     */

    public String fileTokenResolver(String fileName, String token, String replacement) throws IOException {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        String unresolvedString = IOUtils.toString(in, StandardCharsets.UTF_8);
        return unresolvedString.replaceAll(token, "<" + replacement + ">");
    }

    /**
     * Resolves the occurrences of multiple tokens of a file with the replacement URIs
     * 
     * @param file_name
     *            the name of the file with the actual content
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
            resolvedString = resolvedString.replaceAll(tokenValue, "<" + replacementValue + ">");
        }
        return resolvedString;
    }
}
