import {Alert, Button} from "react-bootstrap";
import React from 'react';
import {any} from "prop-types";

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }

    static getDerivedStateFromError() {
        // Update state so the next render will show the fallback UI.
        return { hasError: true };
    }

    componentDidCatch() {
        // You can also log the error to an error reporting service
        // console.log(error, errorInfo);
    }

    render() {
        if (this.state.hasError) {
            return <Alert variant="danger">
                <Alert.Heading>Something serious went wrong</Alert.Heading>
                <p>
                    Please contact the developer for support.
                </p>
                <hr />
                <div className="d-flex justify-content-end">
                    <Button onClick={() => this.setState({hasError:false})} variant="outline-danger">
                        Close
                    </Button>
                </div>
            </Alert>;
        }

        return this.props.children;
    }
}
ErrorBoundary.propTypes = {
    children: any
};
export default ErrorBoundary;