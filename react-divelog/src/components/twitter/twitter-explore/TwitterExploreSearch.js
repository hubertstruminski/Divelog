import React from 'react';
import '../../../css/TwitterExploreSearch.css';
import AuthService from '../../../util/AuthService';
import axios from 'axios';
import swal from 'sweetalert';

class TwitterExploreSearch extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            search: ''
        }
        this.Auth = new AuthService();

        this.onChange = this.onChange.bind(this);
        this.onEnterClick = this.onEnterClick.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    onEnterClick(e) {
        if(e.keyCode === 13) {
            this.props.addNewTweet("<div class='spinner-border text-primary twitter-explore-search-spinner' role='status'><span class='sr-only'>Loading...</span></div>");
            let jwtToken = this.Auth.getRightSocialToken();

            axios({
                url: `/twitter/search/tweets/${jwtToken}`,
                method: 'POST',
                data: this.state.search,
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'content-type': 'application/json'
                }
            }).then(response => {
                if(response.status !== 200) {
                    swal(this.props.t("error-500.title"), this.props.t("error-500.message"), "error");
                } else {
                    this.props.addNewTweet(response.data);
                }
            });
        }
    }

    render() {
        return (
            <div className="twitter-explore-search-container">
                <input 
                    type="text" 
                    name="search"
                    className="twitter-explore-search-input" 
                    placeholder="Search Twitter"
                    value={this.state.search}
                    onChange={this.onChange}
                    onKeyDown={this.onEnterClick}
                />
            </div>
        );        
    }
}

export default TwitterExploreSearch;