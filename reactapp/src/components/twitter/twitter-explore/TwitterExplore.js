import React from 'react';
import '../../../css/twitter-explore/TwitterExplore.css';
import withAuth from '../../../util/withAuth';
import AuthService from '../../../util/AuthService';
import TwitterCategoriesCard from '../TwitterCategoriesCard';
import SearchTwitterPeople from '../SearchTwitterPeople';
import AvailableTrends from '../AvailableTrends';
import TwitterExploreSearch from './TwitterExploreSearch';
import $ from 'jquery';
import { BACKEND_API_URL } from '../../../actions/types';
import swal from 'sweetalert';
import TwitterExploreSearchRequestMethod from '../../../util/TwitterExploreSearchRequestMethod';

class TwitterExplore extends React.Component {
    isMountedTwitterExplore = false;
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
            searchTweets: '',
            isTrendClickedAgain: false,
            searchTweetInputAgain: '',
        }
        this.isLoading = true;
        this.Auth = new AuthService();
        this.SearchTweetObject = new TwitterExploreSearchRequestMethod();

        this.addNewTweet = this.addNewTweet.bind(this);
    }

    componentDidMount() {
        this.isMountedTwitterExplore = true;

        if(this.props.match.params.trendName === undefined) {
            this.isLoading = false;
        }

        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`${BACKEND_API_URL}/getuserdata/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(response => { return response.json() })
        .then(jsonData => {
            if(this.isMountedTwitterExplore) {
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
        }).catch(err => {
            swal("Error", "Can not retrieve user data.", "error");
        });   
    }

    componentDidUpdate(prevProps, prevState) {
        if(this.state.isTrendClickedAgain) {
            this.SearchTweetObject.searchTweets(this, this.state.searchTweetInputAgain);
        } else {
            if(prevProps.match.params.trendName === undefined) {
                if(prevProps.location.hash !== "") {
                    this.SearchTweetObject.searchTweets(this, prevProps.location.hash);
                }
            } else {
                if(prevProps.location.hash === "") {
                    this.SearchTweetObject.searchTweets(this, prevProps.match.params.trendName);
                }
            }
        }
        this.isLoading = false;
    }

    componentWillReceiveProps(nextProps) {
        if(this.isMountedTwitterExplore) {
            if(nextProps.match.params.trendName === undefined) {
                if(nextProps.location.hash !== "") {
                    this.setState({
                        isTrendClickedAgain: true,
                        searchTweetInputAgain: nextProps.location.hash
                    })
                }
            } else {
                if(nextProps.location.hash === "") {
                    this.setState({
                        isTrendClickedAgain: true,
                        searchTweetInputAgain: nextProps.match.params.trendName
                    })
                }
            }
        }
    }

    addNewTweet(newTweets) {
        this.setState({ searchTweets: newTweets }, () => {
            $(".twitter-explore-search-tweets-container").html(this.state.searchTweets);
            $(".twitter-tweet").attr("data-width", "520px");
        });
    }

    render() {
        let isLoading = this.isLoading;
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
                        <TwitterExploreSearch 
                            addNewTweet={this.addNewTweet}
                        />
                        <div className="twitter-explore-search-tweets-container">
                            {
                                isLoading ?
                                (
                                    <div 
                                        className='spinner-border text-primary' 
                                        role='status'
                                    >
                                        <span class='sr-only'>
                                            Loading...
                                        </span>
                                    </div>
                                )
                                :
                                (
                                    <div></div>
                                )
                            }
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
                            <div className="twitter-friends-container"></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

// export default withAuth(TwitterExplore, {  twitterExplore: true });
export default TwitterExplore;