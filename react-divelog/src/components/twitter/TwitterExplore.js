import React from 'react';
import withAuth from '../../util/withAuth';
import AuthService from '../../util/AuthService';
import TwitterCategoriesCard from './TwitterCategoriesCard';
import SearchTwitterPeople from './SearchTwitterPeople';
import AvailableTrends from './AvailableTrends';
import { withRouter } from 'react-router';

class TwitterExplore extends React.Component {
    isMountedTwitterExplore = false;
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
        this.isMountedTwitterExplore = true;
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

                    </div>
                    <div className="twitter-grid-item-3">
                        <div className="twitter-rl-container">
                            <div className="twitter-groups-container">
                                    <SearchTwitterPeople />
                                    <AvailableTrends />
                            </div>
                        </div>
                        <div className="twitter-rr-container">
                            <div className="twitter-friends-container"></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withAuth(TwitterExplore, {  twitterExplore: true });