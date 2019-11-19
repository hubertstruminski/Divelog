import React from 'react';
import AuthService from '../../../util/AuthService';

class TwitterMessagesInbox extends React.Component {
    constructor() {
        super();

        this.state = {

        }
        this.Auth = new AuthService();
    }

    componentDidMount() {
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`/twitter/direct/messages/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response);
        })
    }

    render() {
        return (
            <ul>

            </ul>
        );
    }
}

export default TwitterMessagesInbox;