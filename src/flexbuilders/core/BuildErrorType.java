
package flexbuilders.core;

/**
 * Defines different types of errors that can be generated during the building
 * process performed by multiple mutable builders.
 * @author Enrique Urra C.
 */
public enum BuildErrorType
{
    /**
     * Represent a warning, which only provide information regarding potential
     * problems. This type should be used when the continuity of the build 
     * process is not compromised.
     */
    WARNING,    
    /**
     * Represent a common build error. This This type should be used when the
     * continuity of the build process may be stopped, but the consistency of
     * the build state is not compromised.
     */
    NONFATAL,
    /**
     * Represent a fatal build error. This type should be used when the 
     * build state is compromised and could present inconsistencies, therefore,
     * a restart or rollback should be performed.
     */
    FATAL
}
