
package flexbuilders.scripting;

/**
 *
 * @author Enrique Urra C.
 */
public class ScriptLibraryException extends Exception
{
    public ScriptLibraryException(String string)
    {
        super(string);
    }

    public ScriptLibraryException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    public ScriptLibraryException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}
