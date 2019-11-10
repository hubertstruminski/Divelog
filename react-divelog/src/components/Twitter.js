import React from 'react';
import '../css/TimelineLikes.css';
import withAuth from '../util/withAuth';
import AuthService from '../util/AuthService';
import TwitterCategoriesCard from './twitter/TwitterCategoriesCard';
import SearchTwitterPeople from './twitter/SearchTwitterPeople';
import AvailableTrends from './twitter/AvailableTrends';
import TwitterFriendsList from './twitter/TwitterFriendsList';

class Twitter extends React.Component {
    isMountedTwitter = false;
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            userID: '',
            pictureUrl: '',
            providerId: '',
            screenName: '',
            tokenSecret: ''
        }
        this.Auth = new AuthService();
    }

    componentDidMount() {
        this.isMountedTwitter = true;

        this.isMountedTwitter && window.twttr.widgets.load(document.getElementsByClassName("feed-container")[0]);
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`/getuserdata/${jwtToken}`, {
            method: 'GET',
            headers: {
              'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            if(this.isMountedTwitter) {
                this.setState({
                    accessToken: jsonData.accessToken,
                    email: jsonData.email,
                    name: jsonData.name,
                    twitterUserID: jsonData.twitterUserID,
                    pictureUrl: jsonData.pictureUrl,
                    providerId: jsonData.providerId,
                    screenName: jsonData.screenName,
                    tokenSecret: jsonData.tokenSecret
                }, () => {
                    window.twttr.widgets.createTimeline(
                    {
                        sourceType: 'likes',
                        screenName: this.state.screenName
                    },
                    document.getElementsByClassName("tweets-likes-container")[0],
                    {
                        width: '100%',
                        height: '100%',
                        related: 'twitterdev,twitterapi'
                    });
                });
            }
        }); 
    }

    componentWillUnmount() {
        this.isMountedTwitter = false;
    }

    render() {
        return (
            <div className="twitter-container">
                <div className="twitter-grid-container">
                    <div className="twitter-grid-item-1">
                        <div className="twitter-left-categories-container">
                            <div className="twitter-profil-container">
                                { this.state.name }
                            </div>
                            <TwitterCategoriesCard
                                pictureUrl={this.state.pictureUrl}
                                screenName={this.state.screenName}
                            />    
                        </div>
                    </div>
                    <div className="feed-container">
                        <div className="tweets-likes-container">

                        </div>
                    </div>
                    <div className="twitter-grid-item-3">
                        <div className="twitter-rl-container">
                            <div className="twitter-groups-container">
                                <SearchTwitterPeople />
                                <AvailableTrends />
                            </div>
                        </div>
                        <div className="twitter-rr-container">
                            <div className="twitter-friends-container">
                                {/* <TwitterFriendsList /> */}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withAuth(Twitter);