package com.github.jjbcoding.trellis.util.internal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Helps with tasks related to locating classes.
 */
public class ReflectionHelper {
    // ----- STATIC
    /**
     * Gets all classes under a package name.
     * @param packageName               The name of the package
     * @return                          The list of classes
     * @throws IOException              On filesystem error
     * @throws ClassNotFoundException   On reflection error
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
    	ArrayList<Class<?>> classes = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files == null)
                    throw new IOException("Failed to list files in " + directory);
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        
        return classes;
    }

    /**
     * Gets all named classes under a package name.
     * @param packageName               The name of the package
     * @return                          The list of named classes
     * @throws IOException              On filesystem error
     * @throws ClassNotFoundException   On reflection error
     */
    @SuppressWarnings("unused")
    public static List<Class<?>> getNamedClasses(String packageName) throws ClassNotFoundException, IOException {
    	List<Class<?>> in = getClasses(packageName);
        List<Class<?>> out = new ArrayList<>();
    	for (int i = 0; i < in.size(); i++)
    		if (!in.get(i).isAnonymousClass())
    			out.add(in.get(i));
    	return out;
    }
}
