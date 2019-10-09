import React from 'react';

class GeoLocation extends React.Component {

    // componentDidMount() {
    //     console.log("GeoLocation - componentDidMount()");

    //     if(this.props.isGeolocationEnabled) {
    //         this.props.setGeolocation(this.props.coords.longitude, this.props.coords.latitude);
    //     }
    // }

    // componentDidUpdate() {
    //     console.log("GeoLocation - componentDidUpdate()");

    //     if(this.props.isGeolocationEnabled) {
    //         this.props.setGeolocation(this.props.coords.longitude, this.props.coords.latitude);
    //     }
    // }

    render() {
        return !this.props.isGeolocationAvailable
        ? <div>Your browser does not support Geolocation</div>
        : !this.props.isGeolocationEnabled
            ? <div>Geolocation is not enabled. <br />Turn on to display twitter trends.</div>
            : <div></div>
            ? <div></div>
            : <div>Please give us allow for your geolocation to display twitter trends...</div>;
    }
}

export default GeoLocation;