import React from 'react';
import {node} from "prop-types";

function AppInitialized({children}) {
    const spinner = document.getElementById('initLoader');
    if(spinner) spinner.remove();
    return (<>{children}</>);
}
AppInitialized.propTypes = {
    children: node
};
export default AppInitialized;