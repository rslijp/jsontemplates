
import React, {useContext} from 'react';
import {any, arrayOf} from "prop-types";
import { groupBy, last , sortOn} from '../utils/ArrayUtil';
import { AllowNodesContext } from '../available/AllowNodesProvider';
import Node from './Node';

function NodeList({allNodes}) {
    const [nodes] = useContext(AllowNodesContext);

    const bundles = groupBy(nodes.sort(sortOn('packageName')), 'packageName');
    const groupsList = Object.keys(bundles).map((bundle) => {
        const nodes = bundles[bundle];
        const nodesList = nodes.map(node =>
            <Node key={node.id} node={node} allNodes={allNodes}/>
        );
        const names = bundle.split(".");
        const name = last(names);
        return  <div key={bundle}>
            <h5>{name}</h5>
            {nodesList}
        </div>;
    }
    );

    return (
       <>
                {groupsList}
        </>
    );
}
NodeList.propTypes = {
    // nodes: arrayOf(any),
    allNodes: arrayOf(any)
};
export default NodeList;