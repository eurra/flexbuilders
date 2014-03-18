
package flexbuilders.scripting;

import flexbuilders.core.BuildException;
import flexbuilders.tree.TreeHandler;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class PropertiesConfigScript extends BuildScript
{
    private String file;
    private Properties config;

    public PropertiesConfigScript(TreeHandler input, String propertiesFile) throws BuildException
    {
        super(input);
        
        config = new Properties();
        
        try
        {
            config.load(new FileReader(propertiesFile));
        }
        catch(FileNotFoundException ex)
        {
            throw new BuildException("The properties file '" + propertiesFile + "' does not exists");
        }
        catch(IOException ex)
        {
            throw new BuildException("Cannot read the properties file '" + propertiesFile + "'", ex);
        }
        
        file = propertiesFile;
    }

    public final String checkEntry(String entry)
    {
        try
        {
            return getEntry(entry);
        }
        catch(BuildException ex)
        {
            return null;
        }
    }
    
    public final String getEntry(String entry) throws BuildException
    {
        String value = config.getProperty(entry);
        
        if(value == null)
            throw new BuildException("The entry '" + entry + "' was not found in the properties file '" + file + "'");
        
        return value;
    }
    
    public final boolean entryExists(String entry)
    {
        return config.getProperty(entry) != null;
    }
}
