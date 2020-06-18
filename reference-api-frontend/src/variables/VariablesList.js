import React, {useContext} from 'react';
import VariableModel from "./VariableModel";
import {VariablesContext} from "../variables/VariableProvider";

function VariablesList() {
    const [variables] = useContext(VariablesContext);
    const models = variables.map(model => <VariableModel key={model.type} model={ model} />);
    return <>
                <h5>Available</h5>
                {models}
            </>;
}
export default VariablesList;