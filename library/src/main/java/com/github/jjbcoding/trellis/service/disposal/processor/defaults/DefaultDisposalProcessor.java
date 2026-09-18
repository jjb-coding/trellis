package com.github.jjbcoding.trellis.service.disposal.processor.defaults;

import com.github.jjbcoding.trellis.exceptions.ImplementerException;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.disposal.object.DisposalObject;
import com.github.jjbcoding.trellis.service.disposal.object.NodeDescription;
import com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor;

import java.util.List;

import static com.github.jjbcoding.trellis.util.internal.DataHelper.flatten;

/**
 * The DefaultDisposalProcessor visits Nodes and Injectables in the
 * configured order, and closes them if they implement AutoCloseable.
 */
public class DefaultDisposalProcessor
    implements IDisposalProcessor {
    // ----- DYNAMIC
    // *** FIELDS
    TreeOrder order;
    TraversalType traversalType;
    DisposalOrder disposalOrder;

    // *** CONSTRUCTORS
    public DefaultDisposalProcessor(TreeOrder order, TraversalType traversalType, DisposalOrder disposalOrder) {
        this.order = order;
        this.traversalType = traversalType;
        this.disposalOrder = disposalOrder;
    }

    // ----- INTERFACE IMPLEMENTATIONS
    // * [ IDisposalProcessor ]
    @Override
    public void consume(DisposalObject disposalObject) {
        List<NodeDescription> nodeDescriptions;
        if (traversalType == TraversalType.Breadth) {
            if (order == TreeOrder.Ascending)
                nodeDescriptions = flatten(disposalObject.byRankAscending());
            else
                nodeDescriptions = flatten(disposalObject.byRankDescending());
        }
        else {
            if (order == TreeOrder.Ascending)
                nodeDescriptions = disposalObject.byLineageAscending();
            else
                nodeDescriptions = disposalObject.byLineageDescending();
        }

        int j = (disposalOrder == DisposalOrder.InjectablesThenNode) ? 0 : 1;
        for (NodeDescription nodeDescription : nodeDescriptions) {
            for (int i = 0; i < 2; i++) {
                if (i == j) {
                    for (Injectable injectable : nodeDescription.getInjectables())
                        if (injectable instanceof AutoCloseable) {
                            try {
                                ((AutoCloseable)injectable).close();
                            } catch (Exception e) {
                                throw new ImplementerException("Injectable extends AutoCloseable::close at " + injectable.getClass().getSimpleName(), e);
                            }
                        }
                }
                else {
                    Node node = nodeDescription.getNode();
                    if (node instanceof AutoCloseable) {
                        try {
                            ((AutoCloseable) node).close();
                        } catch (Exception e) {
                            throw new ImplementerException("Node extends AutoCloseable::close at " + node.getClass().getSimpleName(), e);
                        }
                    }
                }
            }
        }
    }
}
