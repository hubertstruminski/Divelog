import React from 'react';
import '../css/Twitter.css';
import withAuth from '../util/withAuth';
import AuthService from '../util/AuthService';
import TwitterCategoriesCard from './twitter/TwitterCategoriesCard';
import SearchTwitterPeople from './twitter/SearchTwitterPeople';
import AvailableTrends from './twitter/AvailableTrends';


class Twitter extends React.Component {
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
                            <TwitterCategoriesCard
                                pictureUrl={this.state.pictureUrl}
                                screenName={this.state.screenName}
                            />
                            
                        </div>
                    </div>
                    <div className="feed-container">
                        <div className="twitter-user-profil">
                            <div className="twitter-header-profile">

                            </div>
                            <div className="tweets-profile-container">
                                <a class="twitter-timeline" href={`https://twitter.com/${this.state.screenName}?ref_src=twsrc%5Etfw`}>Tweets by {this.state.screenName}</a>
                            </div>
                        </div>
                        <div className="tweets-likes-container">

                        </div>
                    </div>
                    <div className="fb-grid-item-3">
                        <div className="rl-container">
                            <div className="groups-container">
                                <SearchTwitterPeople />
                                <AvailableTrends />
                            </div>
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