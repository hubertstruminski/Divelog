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
        let bool = false;

        axios({
            url: `${BACKEND_API_URL}/twitter/search/tweets/${jwtToken}`,
            method: 'POST',
            data: searchInputString,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response);
            if(response.status === 429) {
                $(".twitter-explore-search-tweets-container").text("Twitter rate limit exceeded.");
                $(".twitter-explore-search-tweets-container").css({
                    color: "red",
                    fontSize: "0.65vw"
                });
                bool = true;
                return;
            }
            if(!bool) {
                if(response.status !== 200) {
                    // swal("Error", "Internal server error", "error");
                    return;
                } else {
                    document.getElementsByClassName("twitter-explore-search-tweets-container")[0].style.display = "block";
                    $(".twitter-explore-search-tweets-container").html(response.data);
                    $(".twitter-tweet").attr("data-width", "520px");
                }
            }
        }).catch(err => {
            swal("Error", "Internal server error", "error");
        });
    }
}