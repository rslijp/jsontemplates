/* eslint-disable no-unused-vars */

export function groupBy(xs, key) {
    return xs.reduce(function(rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
    }, {});
}

export function sortOn(property) {
    let sortOrder = 1;
    if(property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function (a,b) {
        const result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
        return result * sortOrder;
    };
}

export function head(xs){
    const [head, ...tail] = xs;
    return head;
}

export function tail(xs){
    const [head, ...tail] = xs;
    return tail;
}


export function last(xs) {
    return xs[xs.length-1];
}