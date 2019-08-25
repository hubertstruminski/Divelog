import React from 'react';
import '../../css/GoogleMap.css';
import { Map, Marker, GoogleApiWrapper, InfoWindow } from 'google-maps-react';
import ModalVertically from '../Layout/ModalVertically';
import $ from 'jquery';

class GoogleMap extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoading: true,
            showingInfoWindow: false,
            activeMarker: {},
            selectedPlace: {},
            markers: [],
            isFinishMarker: false,
            latitude: '',
            longitude: '',
            markerName: ''
        }
        this.onMapClick = this.onMapClick.bind(this);
        this.setFinishMarker = this.setFinishMarker.bind(this);
    }

    componentDidMount() {
        this.setState({ isLoading: false });

        this.setState({
            markers: [
                {name: 'Paryż', lat: 48.887, lng: 2.343 },
                {name: 'Hubert Strumiński', lat: 49.748, lng: 20.731 },
                {name: 'Berlin', lat: 52.518, lng: 13.373  }
            ]
        })
    }

    showMarkers = () => {
        return this.state.markers.map((marker, index) => {
            return (
                <Marker 
                    key={index} 
                    position={{ lat: marker.lat, lng: marker.lng }}
                    onClick={this.onMarkerClick}
                />
            );
        })
    }

    onMarkerClick = (props, marker, e) => {
        this.setState({
            selectedPlace: props,
            activeMarker: marker,
            showingInfoWindow: true
        });
    }

    onClose = props => {
        if(this.state.showingInfoWindow) {
            this.setState({
                showingInfoWindow: false,
                activeMarker: null
            });
        }
    };

    onMapClick(t, map, coord) {
        const { latLng } = coord;
        const lat = latLng.lat();
        const lng = latLng.lng();

        this.setState({ 
            latitude: lat,
            longitude: lng
        });

        $(document).on('show.bs.modal', "#modalCenter", function (event) {
            $('#name').trigger('focus');
        });
        $("#modalCenter").modal('show');
    }

    setFinishMarker() {
        this.setState({ isFinishMarker: true });
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
                    zoom={5}
                    style={mapStyle}
                    initialCenter={{ lat: 50.087, lng: 14.421}}
                    onClick={this.onMapClick}
                >
                    { this.showMarkers() }
                    <InfoWindow
                        marker={this.state.activeMarker}
                        visible={this.state.showingInfoWindow}
                        onClose={this.onClose}
                    >
                    <div className="alert alert-success" role="alert">
                        <h4>{this.state.selectedPlace.name}</h4>
                    </div>
                    </InfoWindow>
                </Map>
                <ModalVertically 
                    latitude={this.state.latitude}
                    longitude={this.state.longitude}
                    setFinishMarker={this.setFinishMarker}
                />
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