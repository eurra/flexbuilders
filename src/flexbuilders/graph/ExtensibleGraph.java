
package flexbuilders.graph;

import optefx.util.metadata.Metadata;
import optefx.util.metadata.MutableMetadataProvider;

/**
 *
 * @author Enrique Urra C.
 */
public interface ExtensibleGraph extends NestableGraph, DisposableGraph, MutableMetadataProvider<Metadata>
{
    ExtensibleGraph addResolver(NodeResolver resolver);
    @Override ExtensibleGraph addData(Metadata... data);
    @Override ExtensibleGraph addDependencies(NestableGraph... dependencies);
}
