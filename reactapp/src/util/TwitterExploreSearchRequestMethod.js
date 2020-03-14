import { BACKEND_API_URL } from '../actions/types';
import AuthService from '../util/AuthService';
import axios from 'axios';
import swal from 'sweetalert';
import $ from 'jquery';

export default class TwitterExploreSearchRequestMethod {
    constructor() {
        this.Auth = new AuthService();
    }

    searchTweets(props, searchInputString) {
        let jwtToken = this.Auth.getRightSocialToken();

        axios({
            url: `${BACKEND_API_URL}/twitter/search/tweets/${jwtToken}`,
            method: 'POST',
            data: searchInputString,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if(response.status !== 200) {
                swal("Error", "Internal server error", "error");
            } else {
                $(".twitter-explore-search-tweets-container").html(response.data);
                $(".twitter-tweet").attr("data-width", "520px");
            }
        }).catch(err => {
            swal("Error", "Internal server error", "error");
        });
    }
}