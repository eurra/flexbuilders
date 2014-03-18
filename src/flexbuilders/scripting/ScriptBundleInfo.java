
package flexbuilders.scripting;

import java.util.jar.Attributes;

/**
 *
 * @author Enrique Urra C.
 */
public final class ScriptBundleInfo
{
    private String pkg;
    private String title;
    private String version;
    private String description;
    private String jarPath;
    private Attributes manifestData;

    public ScriptBundleInfo(String pkg, String title, String version, String description, String jarPath, Attributes manifestData)
    {
        this.pkg = pkg;
        this.title = title;
        this.version = version;
        this.description = description;
        this.jarPath = jarPath;
        this.manifestData = manifestData;
    }

    public String getPackage()
    {
        return pkg;
    }
    
    public String getTitle()
    {
        return title;
    }

    public String getVersion()
    {
        return version;
    }

    public String getDescription()
    {
        return description;
    }

    public String getJARPath()
    {
        return jarPath;
    }

    public Attributes getManifestData()
    {
        return manifestData;
    }
}
