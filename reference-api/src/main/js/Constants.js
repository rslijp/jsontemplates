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
    'UNKNOWN' : {color: 'black', font: 'regular', type:'UNKNOWN'},
};


export const ReturnTypes = {
    GENERIC: 'generic',
    GENERICOPTIONAL: 'generic?',
    GENERICLIST: 'generic*',
    GENERICMAP: 'generic[]',
    BOOLEAN: 'boolean',
    BOOLEANOPTIONAL: 'boolean?',
    BOOLEANLIST: 'boolean*',
    BOOLEANMAP: 'boolean[]',
    INTEGER: 'integer',
    INTEGEROPTIONAL: 'integer?',
    INTEGERLIST: 'integer*',
    INTEGERMAP: 'integer[]',
    DECIMAL: 'decimal',
    DECIMALOPTIONAL: 'decimal?',
    DECIMALLIST: 'decimal*',
    DECIMALMAP: 'decimal[]',
    TEXT: 'text',
    TEXTOPTIONAL: 'text?',
    TEXTLIST: 'text*',
    TEXTMAP: 'text[]',
    DATETIME: 'datetime',
    DATETIMEOPTIONAL: 'datetime?',
    DATETIMELIST: 'datetime*',
    DATETIMEMAP: 'datetime[]',
    OBJECT: 'object',
    OBJECTLIST: 'object*',
    OBJECTOPTIONAL: 'object?',
    NULL : 'null'
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
