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

export const HighlightTypes = {
    'CONSTANT': {color: '#006A4E', font: 'bold', type:'CONSTANT'},
    'VARIABLE': {color: '#AB274F', font: 'bold', type:'VARIABLE'},
    'FUNCTION': {color: '#FFBF00', font: 'italic', type:'FUNCTION'},
    'FUNCTION_SYMBOLS': {color: '#FFBF00', font: 'italic-bold', type:'FUNCTION'},
    'BRACKETS': {color: '#FFBF00', font: 'bold', type:'BRACKETS'},
    'INFIX':    {color: '#CC5500', font: 'regular', type:'INFIX'},
    'TERNARY':    {color: '#CC5500', font: 'regular', type:'TERNARY'},
    'UNARY':    {color: '#CC5500', font: 'regular', type:'UNARY'},
    'UNKNOWN' : {color: 'lightgrey', font: 'regular', type:'UNKNOWN'},
};


export const ReturnTypes = {
    GENERIC: 'GENERIC',
    GENERICOPTIONAL: 'GENERIC?',
    LIST_GENERIC: 'GENERIC[]',
    MAP_GENERIC: 'GENERIC{}',
    BOOLEAN: 'BOOLEAN',
    BOOLEANOPTIONAL: 'BOOLEAN?',
    LIST_BOOLEAN: 'BOOLEAN[]',
    MAP_BOOLEAN: 'BOOLEAN{}',
    INTEGER: 'INTEGER',
    INTEGEROPTIONAL: 'INTEGER?',
    LIST_INTEGER: 'INTEGER[]',
    MAP_INTEGER: 'INTEGER{}',
    DECIMAL: 'DECIMAL',
    DECIMALOPTIONAL: 'DECIMAL?',
    LIST_DECIMAL: 'DECIMAL[]',
    MAP_DECIMAL: 'DECIMAL{}',
    TEXT: 'TEXT',
    TEXTOPTIONAL: 'TEXT?',
    LIST_TEXT: 'TEXT[]',
    MAP_TEXT: 'TEXT{}',
    DATETIME: 'DATETIME',
    DATETIMEOPTIONAL: 'DATETIME?',
    LIST_DATETIME: 'DATETIME[]',
    MAP_DATETIME: 'DATETIME{}',
    NULL : 'NULL'
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
