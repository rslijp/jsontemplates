import React, {useState} from 'react';
import {any} from "prop-types";

let setVariablesHook = null;


function setVariables(variables) {
    setVariablesHook(variables);
}

const variablesContext = React.createContext([{authenticated: false, fullName: null}, () => {}]);
function VariablesProvider({ children }) {
    const [variables, _setVariables] = useState({});
    setVariablesHook=_setVariables.bind(this);
    return (
        <variablesContext.Provider value={[variables, _setVariables]}>
            {children}
        </variablesContext.Provider>
    );
}
VariablesProvider.propTypes = {
    children: any
};
export {
    variablesContext as VariablesContext,
    VariablesProvider,
    setVariables
};