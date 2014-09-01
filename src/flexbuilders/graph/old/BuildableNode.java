
package flexbuilders.graph.old;

import flexbuilders.core.NestedBuilder;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Enrique Urra C.
 */
@Target(ElementType.FIELD)
@Documented
public @interface BuildableNode
{
    Class<? extends NestedBuilder> value();
}
