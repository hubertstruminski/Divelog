import React from 'react';
import AuthService from './AuthService';

// export default function withAuth(AuthComponent) {
export default function withAuth(AuthComponent, props) {
    const Auth =  new AuthService();
    let customAuthComponent = false;

    class AuthWrapped extends React.Component {
        constructor(props) {
            super(props);
        }

        setRedirectForTwitterExplore(props, url) {
            if(props.hasOwnProperty("twitterExploreForCategories")) {
                if(props.twitterExploreForCategories) {
                    this.props.history.replace(url);
                }
            }

            if(props.hasOwnProperty("twitterExplore")) {
                if(props.twitterExplore) {
                    this.props.history.replace(url);
                }
            }
        }

        componentWillMount() {
            console.log("Hubert Strumiński");
            console.log(props);
            customAuthComponent = true;

            if(!Auth.loggedIn()) {
                console.log("!Auth.loggedIn()");
                this.props.history.replace("/login");
            } else {
                console.log("before retrieving JwtToken");
                let twitterJwtToken = Auth.getTwitterToken();
                let facebookJwtToken = Auth.getToken();
                console.log(twitterJwtToken);
                console.log(facebookJwtToken);
                try {
                    if(twitterJwtToken) {
                        if(props !== undefined) {
                            if(this.props.location.pathname === "/twitter/explore") {
                                customAuthComponent && this.setRedirectForTwitterExplore(props, "/twitter/explore");
                            }
                            if(this.props.location.pathname === "/twitter/home") {
                                customAuthComponent && this.props.history.replace("/twitter/home");
                            }
                        } else {
                            console.log("/twitter");
                            customAuthComponent && this.props.history.replace("/twitter");
                        }
                    }

                    if(facebookJwtToken) {
                        customAuthComponent && this.props.history.replace("/dashboard");
                    }
                } catch(err) {
                    if(twitterJwtToken) {
                        Auth.logoutTwitter();
                    }
                    if(facebookJwtToken) {
                        Auth.logout();
                    }
                    console.log("/login");
                    this.props.history.replace("/login");
                }
            }
        }

        componentWillUnmount() {
            customAuthComponent = false;
        }

        render() {
            if(Auth.loggedIn()) {
                return (
                    customAuthComponent && <AuthComponent history={this.props.history} />
                );
            } else {
                return null;
            }
        }
    }
    return AuthWrapped;
}