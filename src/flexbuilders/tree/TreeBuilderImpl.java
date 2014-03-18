
package flexbuilders.tree;

import java.util.HashSet;
import java.util.Set;
import flexbuilders.core.BuildException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.StackBuildable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
class TreeBuilderImpl<T> extends StackBuildable<T> implements TreeBuilder<T>
{
    private T result;
    private DefaultBranchBuilder root;
    private Map<String, DefaultBranchBuilder> branches;
    private Set<ReferenceBuilderImpl> references;
    private boolean disposed;
    
    public TreeBuilderImpl() throws BuildException
    {
        this.root = new DefaultBranchBuilder().setRoot();
        this.branches = new HashMap<>();
        this.references = new HashSet<>();
    }

    private void checkDisposed() throws BuildException
    {
        if(disposed)
            throw new BuildException("This tree builder is disposed and cannot be modified");
    }

    @Override
    public BranchBuilder root() throws BuildException
    {
        checkDisposed();
        return root;
    }
    
    @Override
    public BranchBuilder branch(String id) throws BuildException
    {
        checkDisposed();
        
        if(id == null)
            throw new BuildException("The provided id cannot be null");

        DefaultBranchBuilder branch = branches.get(id);

        if(branch == null)
        {
            branch = branch().setId(id);
            branches.put(id, branch);
        }
        
        return branch;
    }

    @Override
    public DefaultBranchBuilder branch() throws BuildException
    {
        return new DefaultBranchBuilder();
    }

    @Override
    public BranchBuilderSet branchSet() throws BuildException
    {
        checkDisposed();            
        return new BranchBuilderSetImpl();
    }

    @Override
    public Buildable ref(String destId) throws BuildException
    {
        checkDisposed();

        if(destId == null)
            throw new BuildException("The destination id cannot be null");

        ReferenceBuilderImpl ref = new ReferenceBuilderImpl().setId(destId);
        references.add(ref);

        return ref;
    }

    @Override
    public TreeBuilderImpl<T> dispose() throws BuildException
    {
        if(!disposed)
        {
            disposed = true;

            root = null;
            branches = null;
            references = null;
        }
        
        return this;
    }

    private void resolveReferences() throws BuildException
    {        
        if(references.isEmpty())
            return;

        for(ReferenceBuilderImpl ref : references)
        {
            if(!ref.isSolved())
            {
                String refId = ref.getId();
                DefaultBranchBuilder branch = branches.get(refId);
                
                if(branch != null)
                    ref.setReferenced(branch);
            }
        }
    }
    
    @Override
    public void buildInstance() throws BuildException
    {
        resolveReferences();
        Object objResult = root.build();

        try
        {
            result = (T)objResult;
        }
        catch(ClassCastException ex)
        {
            throw new BuildException("Cannot convert the root result (" + objResult.getClass() + ") to the tree builder type", ex);
        }
    }

    @Override
    public T getInstance() throws BuildException
    {
        return result;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Tree");
    }
}
