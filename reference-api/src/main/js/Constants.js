export const ItemTypes = {
    NODE: 'node',
};


export const NodeTypes = {
    BRACKETS: 'BRACKETS',
    CONSTANT: 'CONSTANT',
    VARIABLE: 'VARIABLE',
    TERNARY: 'TERNARY',
    UNARY: 'UNARY'
};

export const ReturnTypes = {
    BOOLEAN: 'BOOLEAN',
    INTEGER: 'INTEGER',
    DECIMAL: 'DECIMAL',
    TEXT: 'TEXT',
    GENERIC: 'GENERIC',

    OPTIONAL: 'OPTIONAL' //WRONG
};

export const OperatorPrecendence = {
    FUNCTION:1,
    BRACKETS: 12,
    TERNARY: 3,
    AND: 4,
    OR: 5,
    COMPARISON: 6,
    EQUALITY: 7,
    ADD: 8,
    MINUS: 8,
    NOT: 8,
    MULTIPLY: 9,
    DIVIDE: 9,
    MODULO: 9,
    POWER: 10,
    VARIABLE: null,
    CONSTANTS: null
};
