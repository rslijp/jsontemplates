import React from 'react';
import {Alert, Button} from "react-bootstrap";

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }

    static getDerivedStateFromError(error) {
        // Update state so the next render will show the fallback UI.
        return { hasError: true };
    }

    componentDidCatch(error, errorInfo) {
        // You can also log the error to an error reporting service
        console.log(error, errorInfo);
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
export default ErrorBoundary;