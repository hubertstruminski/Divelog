import React from 'react';
import '../../css/AvailableTrends.css';
import AuthService from '../../util/AuthService';
import { geolocated } from 'react-geolocated';
import GeoLocation from './GeoLocation';

class AvailableTrends extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            longitude: 0.0,
            latitude: 0.0
        }
        this.Auth = new AuthService();
        this.twitterJwtToken = this.Auth.getTwitterToken();

        this.setGeolocation = this.setGeolocation.bind(this);
    }

    componentDidMount() {
        let latitude = this.state.latitude;
        let longitude = this.state.longitude
        fetch(`/twitter/available/trends/${this.state.latitude}/${this.state.longitude}/${this.twitterJwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(json => {
            console.log(json);
        });
    }

    componentDidUpdate() {
        let latitude = this.state.latitude;
        let longitude = this.state.longitude
        fetch(`/twitter/available/trends/${this.state.latitude}/${this.state.longitude}/${this.twitterJwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(json => {
            console.log(json);
        });
    }

    setGeolocation(longitude, latitude) {
        this.setState({
            longitude: longitude,
            latitude: latitude
        });
    }

    render() {
        return (
            <div>
                <ul className="list-group">
                    <div className="geolocation-twitter-trends-box">
                        <GeoLocation 
                            {...this.props}
                            setGeolocation={this.setGeolocation} 
                        />
                    </div>
                </ul>
            </div>
        );
    }
}

export default geolocated({
    positionOptions: {
        enableHighAccuracy: false,
    },
    userDecisionTimeout: 25000,
})(AvailableTrends);