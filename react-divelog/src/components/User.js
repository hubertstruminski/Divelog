import React from 'react';
import { withRouter } from 'react-router-dom';

class User extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            userID: ''
        }
    }

    componentDidMount() {
        let userID = localStorage.getItem("KEY");

        fetch(`/getuserdata/${userID}`, {
            method: 'GET',
            headers: {
              'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            this.setState({
                accessToken: jsonData.accessToken,
                email: jsonData.email,
                name: jsonData.name,
                userID: jsonData.userID
            });
        }); 
        localStorage.removeItem("KEY");  
    }

    render() {
        return (
            <div>Codecool</div>
        );
    }
}

export default withRouter(User);