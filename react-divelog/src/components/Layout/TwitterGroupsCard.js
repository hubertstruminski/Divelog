import React from 'react';
import '../../css/TwitterGroupsCard.css';
import AuthService from '../../util/AuthService';
import axios from 'axios';
import twitterVerified from '../../img/twitter-verified.png';

class TwitterGroupsCard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            searchPeople: '',
            timeInterval: 0,
            searchPeopleList: [],
            isSearched: false
        }
        this.Auth = new AuthService();
        this.twitterJwtToken = this.Auth.getTwitterToken();

        this.onTwitterSearchChange = this.onTwitterSearchChange.bind(this);
        this.getUsers = this.getUsers.bind(this);
        this.getPeopleList = this.getPeopleList.bind(this);
        this.onBlur = this.onBlur.bind(this);
    }

    onTwitterSearchChange(e) {
        this.setState({
            searchPeople: e.target.value
        }, () => {
            if(this.state.searchPeople && this.state.searchPeople.length > 1) {
                this.getUsers();
            } else if(!this.state.searchPeople) {
                this.setState({ searchPeopleList: [] });
            }
        });
    }

    onBlur(e) {
        if(e.keyCode === 8) {
            if(this.state.searchPeople.length === 0) {
                this.setState({ searchPeopleList: [] });
            }
        }
    }

    getUsers() {
        let searchPeople = this.state.searchPeople;
        axios({ 
            url: `/twitter/users/search/${this.state.searchPeople}/${this.twitterJwtToken}`,
            method: 'POST',
            body: searchPeople,
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => {
            this.setState({ searchPeopleList: [] });
            let counter = 0;
            response.data.map((person, index) => {
                if(counter === 10) {
                    return;
                }
                const element = {
                    id: person.id,
                    name: person.name,
                    pictureUrl: person['400x400ProfileImageURL'],
                    screenName: person.screenName,
                    verified: person.verified
                }
                this.setState({ searchPeopleList: this.state.searchPeopleList.concat(element) });
                counter++;
            }); 
            this.setState({ isSearched: true });
        });
    }

    getPeopleList() {
        return this.state.searchPeopleList.map((person, index) => {
            let verified = '';
            if(person.verified) {
                verified = "- Verified";
            } else {
                verified = "";
            }
            return (
                <div>
                <li className="list-group-item list-group-search-person">
                    <div className="search-li-item-float search-1-div">
                        <img src={person.pictureUrl} className="search-picture-person" alt="Person" />
                    </div>
                    <div className="search-li-item-float search-2-div">
                        {person.verified && <img src={twitterVerified} className="twitter-verified" alt="Twitter user verified" /> }
                        {person.name} 
                        <br />
                        @{person.screenName}
                    </div>
                    <div style={{ clear: 'both' }}></div>
                </li>
                </div>
            );
        });
    }

    render() {
        let isSearched = this.state.isSearched;

        return (
            <div>
                <ul className="list-group">
                    <li id="search-twitter-people" className="list-group-item">
                        <input 
                            type="text"
                            value={this.state.searchPeople}
                            placeholder="Search twitter people"
                            className="form-control input-search-twitter"
                            onChange={this.onTwitterSearchChange}
                            onBlur={this.onBlur}
                        />
                    </li>
                    <div className="list-searched-people">
                        { isSearched && this.getPeopleList() }
                    </div>
                </ul>
            </div>
        );
    }
}

export default TwitterGroupsCard;