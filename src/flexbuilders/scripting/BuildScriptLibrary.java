
package flexbuilders.scripting;

import flexbuilders.tree.TreeHandler;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 * @author Enrique Urra C.
 */
public final class BuildScriptLibrary
{
    public static BuildScriptLibrary getFromClassLoader(ClassLoader loader) throws ScriptLibraryException
    {
        List<ScriptBundleInfo> bundles = new ArrayList<>();
        
        try
        {
            Enumeration<URL> resources = loader.getResources("META-INF/MANIFEST.MF");
            
            while(resources.hasMoreElements())
            {
                URL resource = resources.nextElement();
                Manifest manifest = new Manifest(resource.openStream());
                loadBundlesFromManifest(manifest, new File(resource.getFile()), bundles);
            }
        }
        catch(IOException ex)
        {
            throw new ScriptLibraryException("Cannot load scripts bundles from classpath: " + ex.getLocalizedMessage(), ex);
        }
        
        return new BuildScriptLibrary(BuildScriptLibrary.class.getClassLoader(), bundles.toArray(new ScriptBundleInfo[0]));
    }
    
    public static BuildScriptLibrary getFromPaths(String... paths) throws ScriptLibraryException
    {
        return getFromPaths(true, paths);
    }
    
    public static BuildScriptLibrary getFromPaths(boolean searchRecursively, String... paths) throws ScriptLibraryException
    {
        Set<URL> filesURLs = new HashSet<>();
        List<ScriptBundleInfo> bundles = new ArrayList<>();

        for(int i = 0; i < paths.length; i++)
            loadBundlesFromPath(paths[i], bundles, filesURLs, new HashSet<String>(), searchRecursively);

        ClassLoader loader = URLClassLoader.newInstance(filesURLs.toArray(new URL[0]));
        return new BuildScriptLibrary(loader, bundles.toArray(new ScriptBundleInfo[0]));
    }
    
    private static void loadBundlesFromPath(String path, List<ScriptBundleInfo> bundles, Set<URL> filesURLs, Set<String> checkedPaths, boolean searchRecursively) throws ScriptLibraryException
    {
        File folder = new File(path);
        
        if(checkedPaths.contains(path))
            return;
        
        if(!folder.exists() || !folder.isDirectory())
           throw new ScriptLibraryException("The provided path (" + path + ") does not exists or is not a directory");
        
        File[] filesInFolder = folder.listFiles();
        List<String> subFolders = new ArrayList<>();
        
        for(int i = 0; i < filesInFolder.length; i++)
        {
            File toCheck = filesInFolder[i];
            
            if(fileIsJar(toCheck))
            {   
                Manifest manifest;

                try
                {
                    JarFile currJarFile = new JarFile(toCheck);
                    manifest = currJarFile.getManifest();
                }
                catch(IOException ex)
                {
                    throw new ScriptLibraryException("Cannot load jar file '" + toCheck + "': " + ex.getLocalizedMessage(), ex);
                }

                if(manifest != null && loadBundlesFromManifest(manifest, toCheck, bundles))
                {
                    try
                    {
                        filesURLs.add(toCheck.toURI().toURL());
                    }
                    catch(MalformedURLException ex)
                    {
                        throw new ScriptLibraryException("Cannot load script bundles from jar: " + ex.getLocalizedMessage(), ex);
                    }
                }
            }
            else if(toCheck.isDirectory())
            {
                subFolders.add(toCheck.getPath());
            }
        }
        
        checkedPaths.add(path);
        
        if(searchRecursively)
        {
            int subFoldersCount = subFolders.size();
        
            for(int i = 0; i < subFoldersCount; i++)
                loadBundlesFromPath(subFolders.get(i), bundles, filesURLs, checkedPaths, true);
        }
    }
    
    private static boolean fileIsJar(File file)
    {
        if(!file.isFile())
            return false;
        
        String name = file.getName().toLowerCase();
        
        if(name.endsWith(".jar") || name.endsWith(".zip"))
            return true;
        
        return false;
    }
    
    private static boolean loadBundlesFromManifest(Manifest manifest, File jarFile, List<ScriptBundleInfo> bundles) throws ScriptLibraryException
    {
        Map<String, Attributes> jarBundles = manifest.getEntries();
        boolean bundleLoaded = false;

        for(String name : jarBundles.keySet())
        {
            Attributes attrs = jarBundles.get(name);
            String isBundleBool = attrs.getValue("Script-Bundle");

            if(isBundleBool == null || !Boolean.parseBoolean(isBundleBool))
                continue;

            String title = attrs.getValue("Script-Bundle-Title");
            String version = attrs.getValue("Script-Bundle-Version");
            String description = attrs.getValue("Script-Bundle-Description");

            ScriptBundleInfo bInfo = new ScriptBundleInfo(
                name.replaceAll("/", ".").substring(0, name.length() - 1),
                title,
                version,
                description,
                jarFile.getPath(),
                attrs
            );

            bundles.add(bInfo);
            
            if(!bundleLoaded)
                bundleLoaded = true;
        }
        
        return bundleLoaded;
    }
    
    private ClassLoader loader;
    private ScriptBundleInfo[] bundles;

    private BuildScriptLibrary(ClassLoader loader, ScriptBundleInfo[] bundles) throws ScriptLibraryException
    {        
        this.loader = loader;
        this.bundles = bundles;
    }
    
    /*public String[] getPackages()
    {
        Set<String> packages = new HashSet<>();
        int bundlesCount = bundles.size();
        
        for(int i = 0; i < bundlesCount; i++)
        {
            ScriptBundleInfo bInfo = bundles.get(i);
            String pkg = bInfo.getPackage();
            
            if(!packages.contains(pkg))
                packages.add(pkg);
        }
        
        return packages.toArray(new String[0]);
    }*/
    
    /*private void searchBundlesInPackage(String pkgName, List<ScriptBundleInfo> toSearch, List<ScriptBundleInfo> toStore)
    {
        int bundlesCount = toSearch.size();
        
        for(int i = 0; i < bundlesCount; i++)
        {
            ScriptBundleInfo bInfo = toSearch.get(i);
            String bundlePkg = bInfo.getPackage();
            
            if(bundlePkg.startsWith(pkgName))
                toStore.add(bInfo);
        }
    }*/
    
    public ScriptBundleInfo[] getBundles()
    {
        return Arrays.copyOf(bundles, bundles.length);
    }
    
    /*public ScriptBundleInfo[] searchBundlesInPackage(String pkgName, boolean includeClassPath) throws ScriptLibraryException
    {
        List<ScriptBundleInfo> retBundles = new ArrayList<>();
        
        if(includeClassPath)
            searchBundlesInPackage(pkgName, getBundlesInClassPath(), retBundles);
        
        searchBundlesInPackage(pkgName, bundles, retBundles);
        return retBundles.toArray(new ScriptBundleInfo[0]);
    }*/
    
    public final BuildScript createScript(String className, TreeHandler delegate, Object... args) throws ScriptLibraryException
    {
        Class<? extends BuildScript> scriptClass;
        
        try
        {
            scriptClass = (Class<? extends BuildScript>)loader.loadClass(className);
        }
        catch(ClassCastException ex)
        {
            throw new ScriptLibraryException("The class '" + className + "' is not a valid script type", ex);
        }
        catch(ClassNotFoundException ex)
        {
            throw new ScriptLibraryException("The script class '" + className + "' was not found", ex);
        }
        
        Constructor[] scriptConstructors = scriptClass.getConstructors();
        Object[] finalArgs = new Object[args.length + 1];
        finalArgs[0] = delegate;
        System.arraycopy(args, 0, finalArgs, 1, args.length);
        
        for(int i = 0; i < scriptConstructors.length; i++)
        {
            Constructor currConstructor = scriptConstructors[i];
            
            try
            {
                BuildScript script = (BuildScript)currConstructor.newInstance(finalArgs);
                return script;
            }
            catch(InvocationTargetException ex)
            {
                throw new ScriptLibraryException("Error while instantiating the script class '" + className + "': " + ex.getCause().getMessage(), ex);
            }
            catch(IllegalAccessException | IllegalArgumentException | InstantiationException | ClassCastException ex)
            {
            }
        }
        
        String exMsg = "No suitable script class was found with name '" + className + "'";
        
        if(args.length > 0)
        {
            exMsg += " and constructable with the following arguments: ";
            
            for(int i = 0; i < args.length; i++)
                exMsg += args[i].getClass() + (i < args.length - 1 ? ", " : "");
        }
        
        throw new ScriptLibraryException(exMsg);
    }
}