import React from 'react';
import '../../../css/twitter-messages/TwitterMessagesBox.css';
import AuthService from '../../../util/AuthService';
import TwitterCategoriesCard from '../TwitterCategoriesCard';
import TwitterFriendsList from '../TwitterFriendsList';
import $ from 'jquery';
import TwitterMessagesSearch from './TwitterMessagesSearch';
import TwitterMessagesInbox from './TwitterMessagesInbox';

class TwitterMessagesBox extends React.Component {
    isMountedTwitterMessagesBox = false;
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            twitterUserID: '',
            pictureUrl: '',
            providerId: '',
            screenName: '',
            tokenSecret: '',
        }
        this.Auth = new AuthService();
    }

    componentDidMount() {
        this.isMountedTwitterMessagesBox = true;
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`/getuserdata/${jwtToken}`, {
            method: 'GET',
            headers: {
              'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            if(this.isMountedTwitterMessagesBox) {
                this.setState({
                    accessToken: jsonData.accessToken,
                    email: jsonData.email,
                    name: jsonData.name,
                    twitterUserID: jsonData.twitterUserID,
                    pictureUrl: jsonData.pictureUrl,
                    providerId: jsonData.providerId,
                    screenName: jsonData.screenName,
                    tokenSecret: jsonData.tokenSecret
                });
            }
        });
    }

    componentWillUnmount() {
        this.isMountedTwitterMessagesBox = false;
    }

    render() {
        return (
            <div className="twitter-messages-container">
                <div className="twitter-messages-grid-container">
                    <div className="twitter-messages-grid-item-1">
                        <div className="twitter-messages-left-categories-container">
                            <div className="twitter-messages-profil-container">
                                { this.state.name }
                            </div>
                            <TwitterCategoriesCard
                                pictureUrl={this.state.pictureUrl}
                                screenName={this.state.screenName}
                            />    
                        </div>
                    </div>
                    <div className="twitter-messages-container-feed">
                        <div className="twitter-messages-list-inbox">
                            <div className="twitter-messages-title-box">
                                <div className="twitter-messages-title">Messages</div>
                                <i className="far fa-envelope twitter-messages-add-icon"></i>
                            </div>
                            <TwitterMessagesSearch />
                            {/* <div className="twitter-messages-list-persons-spinner">
                                <TwitterMessagesInbox />
                            </div> */}
                            <TwitterMessagesInbox />
                        </div>
                        <div className="twitter-messages-person-box">
                            <div className="twitter-messages-person-invite-wrapper">
                                <div className="twitter-messages-person-invite">
                                    <span style={{ fontWeight: 700, fontSize: '1.1vw' }}>You don't have a message selected</span>
                                    <br />
                                    Choose one from your existing messages, or start a new one.
                                    <br />
                                    <button className="twitter-message-person-btn-new-message">New message</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="twitter-messages-grid-item-3">
                        {/* <div className="twitter-messages-rl-container">
                            <div className="twitter-messages-groups-container">
                            </div>
                        </div> */}
                        <div className="twitter-messages-rr-container">
                            <div className="twitter-messages-friends-container">
                                {/* <TwitterFriendsList /> */}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default TwitterMessagesBox;