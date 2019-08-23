import React from 'react';
import '../../css/GoogleMap.css';
import { Map, Marker, GoogleApiWrapper} from 'google-maps-react';

class GoogleMap extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoading: true
        }
    }

    componentDidMount() {
        this.setState({ isLoading: false });
    }

    render() {
        const mapStyle = {
            width: '70%',
            height: '60%',
            position: 'absolute',
            left: '50%',
            top: '50%',
            '-webkit-transform': 'translate(-50%, -50%)',
            transform: 'translate(-50%, -50%)'
        }

        let loadingScreen = (
            <div class="d-flex justify-content-center">
                <div class="spinner-grow" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
        )

        let map = (
            <div className="google-container">
                <Map
                    google={this.props.google}
                    zoom={2}
                    style={mapStyle}
                    initialCenter={{ lat: 47.444, lng: -122.176}}
                >
                    <Marker position={{ lat: 48.00, lng: -122.00}} />
                </Map>
            </div>
        )
        let content = this.state.isLoading ? loadingScreen : map;

        return (
            <div>
                { content }
            </div>
        );
    }
}

export default GoogleApiWrapper(
    (props) => ({
      apiKey: 'AIzaSyBgb4kpatKEjsOGsxplxFyRfw1K_wGhLTo',
      language: props.language,
    }
  ))(GoogleMap);