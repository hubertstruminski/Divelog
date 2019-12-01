import React from 'react';
import AuthService from './AuthService';

export default function withTwitterAuth(AuthComponent) {
    const Auth =  new AuthService();

    class AuthWrapped extends React.Component {
        componentWillMount() {
            
            if(!Auth.loggedIn()) {
                this.props.history.replace("/login")
            } else {
                try {
                    this.props.history.replace("/dashboard");
                } catch(err) {
                    Auth.logout();
                    this.props.history.replace("/login");
                }
            }
        }

        render() {
            if(Auth.loggedIn()) {
                return (
                    <AuthComponent history={this.props.history} />
                );
            } else {
                return null;
            }
        }
    }
    return AuthWrapped;
}