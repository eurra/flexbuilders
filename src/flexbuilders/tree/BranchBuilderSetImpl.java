
package flexbuilders.tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
class BranchBuilderSetImpl implements BranchBuilderSet
{
    private Set<BranchBuilder> branches;

    public BranchBuilderSetImpl()
    {
        this.branches = new HashSet<>();
    }
    
    @Override
    public void addBranch(BranchBuilder branch)
    {
        if(branch != null && !branches.contains(branch))
            branches.add(branch);
    }
    
    @Override
    public BranchBuilderSetImpl open() throws BuildException
    {
        Iterator<BranchBuilder> it = branches.iterator();
        
        while(it.hasNext())
            it.next().open();
        
        return this;
    }
    
    @Override
    public BranchBuilderSetImpl readOnly() throws BuildException
    {
        Iterator<BranchBuilder> it = branches.iterator();
        
        while(it.hasNext())
            it.next().readOnly();
        
        return this;
    }
    
    @Override
    public BranchBuilderSetImpl seal() throws BuildException
    {
        Iterator<BranchBuilder> it = branches.iterator();
        
        while(it.hasNext())
            it.next().seal();
        
        return this;
    }
}
