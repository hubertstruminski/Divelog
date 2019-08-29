import React from 'react';
import { Map, Marker, GoogleApiWrapper, InfoWindow } from 'google-maps-react';
import GoogleLogbookModal from '../logbook/GoogleLogbookModal';
import $ from 'jquery';
import { withTranslation } from 'react-i18next';
import { compose } from 'redux';
import DeleteLogbookButton from '../logbook/DeleteLogbookButton';
import swal from 'sweetalert';

class GoogleLogbookMap extends React.Component {
    constructor() {
        super();

        this.state = {
            latitude: '',
            longitude: '',
            marker: {},
            activeMarker: {},
            showingInfoWIndow: false,
            selectedPlace: {},
            isFinishMarker: false,
            isAccessible: true
        }
        this.onMapClick = this.onMapClick.bind(this);
        this.updateMarker = this.updateMarker.bind(this);
        this.setFinishMarker = this.setFinishMarker.bind(this);
        this.setIsAccessible = this.setIsAccessible.bind(this);
    }

    componentDidMount() {
        $(".add-dive-google-container div:first").css({
            "height": "350px",
            "position": "static"
        });
    }

    onMapClick(t, map, coord) {
        if(this.state.isAccessible) {
            const { latLng } = coord;
            const lat = latLng.lat();
            const lng = latLng.lng();

            this.setState({ 
                latitude: lat,
                longitude: lng,
                isAccessible: false
            });

            $(document).on('show.bs.modal', "#modalLogbookCenter", function (event) {
                $('#name').trigger('focus');
            });
            $("#modalLogbookCenter").modal('show');
        } else {
            swal("No access", "You can not mark 2nd location", "error");
        }
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
            });if(this.tbodyRef.innerHTML !== "") {
                this.tbodyRef.innerHTML = "";
            }
        }
    };

    showMarker() {
        return (
            <Marker 
                name={this.state.marker.name}
                position={{ lat: this.state.marker.latitude, lng: this.state.marker.longitude }}
                onClick={this.onMarkerClick}
            />
        );
    }

    showTableRow = () => {
        return (
            <tr>
                <th scope="row">
                    <b>1</b>
                </th>
                <td>{this.state.marker.name}</td>
                <td>{this.state.marker.latitude}</td>
                <td>{this.state.marker.longitude}</td>
                <td>
                    <DeleteLogbookButton 
                        id={this.state.marker.id}
                        updateMarker={this.updateMarker}
                        setFinishMarker={this.setFinishMarker}
                        setRef={this.setRef}
                        setIsAccessible={this.setIsAccessible}
                    />
                </td>
            </tr>
        );
    }

    updateMarker(markerObject) {
        this.setState({ marker: markerObject });
    }

    setFinishMarker(value) {
        this.setState({ isFinishMarker: value });
    }

    setIsAccessible() {
        this.setState({ isAccessible: true });
    }

    render() {
        let isFinishMarker = this.state.isFinishMarker;

        let tableRow;
        if(isFinishMarker) {
            tableRow = this.showTableRow();
        } else {
            tableRow = "";
        }
         
        const mapStyle = {
            position: 'static',
            width: '100%',
            height: '350px',
        }

        let loadingScreen = (
            <div class="d-flex justify-content-center">
                <div class="spinner-grow" role="status">
                    <span class="sr-only">
                        {this.props.t("loading")}
                    </span>
                </div>
            </div>
        )

        let map = (
            <div className="add-dive-google-container">
                <Map
                    google={this.props.google}
                    zoom={5}
                    style={mapStyle}
                    initialCenter={{ lat: 48.023, lng: 14.426}}
                    onClick={this.onMapClick}
                >
                    { isFinishMarker && this.showMarker() }
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
                <GoogleLogbookModal 
                    latitude={this.state.latitude}
                    longitude={this.state.longitude}
                    updateMarker={this.updateMarker}
                    setFinishMarker={this.setFinishMarker}
                    setMarker={this.props.setMarker}
                    setIsAccessible={this.setIsAccessible}
                />
                <div className="add-dive-table">
                    <table className="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th scope="col">
                                    <b>#</b>
                                </th>
                                <th scope="col">
                                    {this.props.t("googleMap.table.name")}
                                </th>
                                <th scope="col">
                                    {this.props.t("googleMap.table.latitude")}
                                </th>
                                <th scope="col">
                                    {this.props.t("googleMap.table.longitude")}
                                </th>
                                <th scope="col">
                                    {this.props.t("googleMap.table.delete")}
                                </th>
                            </tr>
                        </thead>
                        <tbody ref={(el) => this.tbodyRef = el}>
                            { isFinishMarker && tableRow }
                        </tbody>
                    </table>
                </div>
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

export default compose(
    GoogleApiWrapper(
    (props) => ({
      apiKey: 'AIzaSyBgb4kpatKEjsOGsxplxFyRfw1K_wGhLTo',
      language: props.language,
    }
  )),
    withTranslation("common")
  )(GoogleLogbookMap);