
package flexbuilders.scripting;

import flexbuilders.tree.BranchBuilder;
import flexbuilders.tree.BranchBuilderSet;
import flexbuilders.tree.TreeHandler;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Defines an algorithm createBuilder script. The classes that implement this interface
 * should define building constructs to assemble algorithm components within the
 * {@link #process(hMod.parser.algorithmBuild.OLD_AlgorithmBuilder)} method.
 * 
 * @author Enrique Urra C.
 */
public abstract class BuildScript implements Script, TreeHandler
{
    private TreeHandler input;
    private Set<BranchBuilderSet> enabledSets;
    private BranchBuilderSet lastEnabledSet;

    public BuildScript(TreeHandler input)
    {
        if(input == null)
            throw new NullPointerException("Null input");
        
        this.input = input;
        this.enabledSets = new HashSet<>();
    }
    
    private void checkEnabledSets(BranchBuilder branch) throws BuildException
    {
        if(enabledSets.size() > 0)
        {
            Iterator<BranchBuilderSet> it = enabledSets.iterator();
        
            while(it.hasNext())
                branch.addTo(it.next());
        }
    }
    
    @Override
    public final BranchBuilder branch(String id) throws BuildException
    {
        BranchBuilder branch = input.branch(id);
        checkEnabledSets(branch);
        
        return branch;
    }

    @Override
    public final BranchBuilder branch() throws BuildException
    {
        BranchBuilder branch = input.branch();
        checkEnabledSets(branch);
        
        return branch;
    }

    @Override
    public final BranchBuilder root() throws BuildException
    {
        return input.root();
    }
    
    @Override
    public final BranchBuilderSet branchSet() throws BuildException
    {
        return input.branchSet();
    }

    @Override
    public final Buildable ref(String id) throws BuildException
    {
        return input.ref(id);
    }
    
    public final BranchBuilderSet enableNewSet() throws BuildException
    {
        BranchBuilderSet set = branchSet();
        enableSet(set);
        
        return set;
    }
    
    public final void enableSet(BranchBuilderSet set)
    {
        if(set == null)
            return;
        
        if(!enabledSets.contains(set))
            enabledSets.add(set);
        
        lastEnabledSet = set;
    }
    
    public final void disableLastSet()
    {
        disableSet(lastEnabledSet);
    }
    
    public final void disableSet(BranchBuilderSet set)
    {
        if(set == null)
            return;
        
        if(enabledSets.contains(set))
            enabledSets.remove(set);
        
        if(lastEnabledSet == set)
            lastEnabledSet = null;
    }
}