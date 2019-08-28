import React from 'react';
import '../../css/Logbook.css';
import { Link } from 'react-router-dom';

class Logbook extends React.Component {
    constructor() {
        super();
    }

    componentDidMount() {

    }

    render() {
        return (
            <div className="logbook-container">
                <div className="logbook-center">
                    <div className="logbook-add-link">
                        <Link 
                            to="/add/dive"
                            className="add-link"
                        >
                            Add dive
                        </Link>
                    </div>
                </div>
            </div>
        );
    }
}

export default Logbook;