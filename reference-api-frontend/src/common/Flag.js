import React from 'react';
import {string} from "prop-types";

const Flag = ({name}) => {
    return (
        <small><i>({name})</i></small>
    );
};
Flag.propTypes = {
    name: string
};
export default Flag;
