import React from 'react';
import withAuth from '../util/withAuth';
import AuthService from '../util/AuthService';

class Twitter extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            userID: '',
            pictureUrl: ''
        }
        this.Auth = new AuthService();
    }

    componentDidMount() {
        window.twttr.widgets.load(document.getElementsByClassName("feed-container")[0]);
        
        let jwtToken = null;

        if(this.Auth.getTwitterToken() !== null) {
            jwtToken = this.Auth.getTwitterToken();
        }
        if(this.Auth.getToken() !== null) {
            jwtToken = this.Auth.getToken();
        }

        fetch(`/getuserdata/${jwtToken}`, {
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
                userID: jsonData.userID,
                pictureUrl: jsonData.pictureUrl,
                providerId: jsonData.providerId,
                screenName: jsonData.screenName,
                tokenSecret: jsonData.tokenSecret
            }, () => {
                window.twttr.widgets.createTimeline(
                {
                    sourceType: 'profile',
                    screenName: this.state.screenName
                },
                document.getElementsByClassName("feed-container")[0],
                {
                    width: '100%',
                    height: '100%',
                    related: 'twitterdev,twitterapi'
                }).then(function (el) {
                    console.log('Embedded a timeline.')
                });
            });
        }); 
    }

    render() {
        return (
            <div className="facebook-container">
                <div className="fb-grid-container">
                    <div className="fb-grid-item-1">
                        <div className="categories-container">
                            <div className="profil-container">
                                <div className="ins-profil-container">
                                    <img 
                                        src={this.state.pictureUrl} 
                                        alt="Profil"
                                        className="fb-profil-picture"
                                    />
                                    { this.state.name }
                                </div>
                            </div>
                            <hr className="hr-margin" />
                            {/* <LeftCard /> */}
                        </div>
                    </div>
                    <div className="feed-container">
                        {/* Hubert Strumiński - twitter account */}
                    </div>
                    <div className="fb-grid-item-3">
                        <div className="rl-container">
                            <div className="groups-container"></div>
                        </div>
                        <div className="rr-container">
                            <div className="friends-container"></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withAuth(Twitter);