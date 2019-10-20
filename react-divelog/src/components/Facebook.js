import React from 'react';
import '../css/Facebook.css';
import LeftCard from './Layout/LeftCard';
import withAuth from '../util/withAuth';
import Cookies from 'universal-cookie';
import AuthService from '../util/AuthService';

class Facebook extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            userID: '',
            pictureUrl: ''
        }
        this.cookies = new Cookies();
        this.Auth = new AuthService();
    }

    componentDidMount() {
        window.twttr.widgets.load();
        
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
                pictureUrl: jsonData.pictureUrl
            }, () => {
                // fetch(`https://graph.facebook.com/${this.state.userID}/posts?fields=id,name,full_picture,message,place,created_time&access_token=${this.state.accessToken}`, {
                //     method: 'GET'
                // }).then(response => response.json())
                // .then(jsonData => {
                   
                // });
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
                            <LeftCard />
                        </div>
                    </div>
                    <div className="feed-container">
                        <div className="feed-add-container">
                            <i className="fas fa-plus fa-3x plus-shadow"></i>
                        </div>
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

export default withAuth(Facebook);
// export default Facebook;