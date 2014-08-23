
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.NestedBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultScannableGraph implements ScannableGraph
{
    private NestableGraph graph;
    
    public DefaultScannableGraph(NestableGraph... dependencies)
    {
        this.graph = new DefaultNestableGraph(dependencies);
    }
    
    @Override
    public void findNodes(String input) throws BuildException
    {
        findNodes(input, DefaultScannableGraph.class.getClassLoader());
    }
    
    @Override
    public void findNodes(String input, ClassLoader classLoader) throws BuildException
    {
        if(input == null)
            throw new NullPointerException("Null package name");
        
        Class targetClass;
        
        try
        {
            targetClass = classLoader.loadClass(input);
        }
        catch(ClassNotFoundException ex)
        {
            throw new BuilderInputException("Cannot find the class '" + input + "'", ex);
        }
        
        Field[] fields = targetClass.getFields();
            
        for(int i = 0; i < fields.length; i++)
        {
            Field currField = fields[i];
            Class fieldType = currField.getType();

            if(NodeLoader.class.isAssignableFrom(fieldType) && Modifier.isStatic(currField.getModifiers()))
            {
                try
                {
                    loadNode((NodeLoader)currField.get(null));
                }
                catch(IllegalAccessException | IllegalArgumentException ex)
                {
                    throw new BuildException("Cannot retrieve the node loader of field '" + currField.toGenericString() + "': " + ex.getLocalizedMessage(), ex);
                }
            }
        }
    }

    @Override
    public <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value)
    {
        return graph.setValue(id, value);
    }

    @Override
    public <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value, NodePriority priority)
    {
        return graph.setValue(id, value, priority);
    }

    @Override
    public <T> NodeBuilder<T> forNode(NodeId<T> id)
    {
        return graph.forNode(id);
    }

    @Override
    public <T> NestedBuilder<T> loadNode(NodeLoader<T> id)
    {
        return graph.loadNode(id);
    }

    @Override
    public void loadSubGraph(SubGraphLoader loader) throws BuildException
    {
        graph.loadSubGraph(loader);
    }
    
    @Override
    public <T> NestedBuilder<T> getNode(NodeId<T> id)
    {
        return graph.getNode(id);
    }

    @Override
    public <T> NestedBuilder<T[]> getAllFromNode(NodeId<T> id, Class<T> arrayType)
    {
        return graph.getAllFromNode(id, arrayType);
    }

    @Override
    public <T> NestedBuilder<T> getValue(NodeId<T> id)
    {
        return graph.getValue(id);
    }
    
    @Override
    public DefaultScannableGraph dispose()
    {
        graph.dispose();
        graph = null;
        
        return this;
    }
}
