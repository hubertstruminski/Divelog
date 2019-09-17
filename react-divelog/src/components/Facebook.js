import React from 'react';
import '../css/Facebook.css';
import LeftCard from './Layout/LeftCard';
import withAuth from '../util/withAuth';

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
    }

    componentDidMount() {
        let jwtToken = localStorage.getItem("JwtToken");

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