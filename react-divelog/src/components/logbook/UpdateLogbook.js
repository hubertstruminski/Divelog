import React from 'react';
import '../../css/AddDive.css';
import GoogleLogbookMap from './GoogleLogbookMap';
import AuthService from '../../util/AuthService';
import axios from 'axios';
import { withRouter } from 'react-router';
import swal from 'sweetalert'; 
import $ from 'jquery';  
import { withTranslation } from 'react-i18next';
import moment from 'moment';

class UpdateLogbook extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            partnerName: '',
            partnerSurname: '',
            marker: {},
            entryTime: '',
            exitTime: '',
            averageDepth: 0.0,
            maxDepth: 0.0,
            visibility: 0.0,
            waterTemperature: 0.0,
            airTemperature: 0.0,
            cylinderCapacity: '',
            divingSuit: 'NONE',
            waterType: 'NONE',
            waterEntryType: 'NONE',
            ballast: 0.0,
            glovesType: 'NONE',
            divingType: 'NONE',
            comment: '',
            partnerNameValidator: false,
            partnerSurnameValidator: false,
            visibilityValidator: false,
            markerValidator: false,
            maxDepthValidator: false
        }
        this.Auth = new AuthService();

        this.validator = [];

        this.onChange = this.onChange.bind(this);
        this.setMarker = this.setMarker.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.validateForm = this.validateForm.bind(this);
    }

    componentDidMount() {
        let logbookId = this.props.match.params.id;
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/get/logbook/${jwtToken}/${logbookId}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            // console.log(jsonData);
            // console.log(jsonData.entryTime);
            // console.log(jsonData.exitTime);
            // var entry = moment(jsonData.entryTime).format("DD-MM-YYYYTHH:mm");
            console.log(jsonData);
            let year = jsonData.entryTime.substr(0, 4);
            let month = jsonData.entryTime.substr(5, 2);
            let day = jsonData.entryTime.substr(8, 2);
            
            let hours = jsonData.entryTime.substr(11, 2);
            let minutes = jsonData.entryTime.substr(14, 2);

            let result = day + "-" + month + "-" + year + "T" + hours + ":" + minutes;
            console.log(result);

            const markerElement = {
                id: jsonData.marker.id,
                name: jsonData.marker.name,
                latitude: jsonData.marker.latitude,
                longitude: jsonData.marker.longitude
            }

            this.setState({
                partnerName: jsonData.partnerName,
                partnerSurname: jsonData.partnerSurname,
                marker: markerElement,
                entryTime: result,
                exitTime: jsonData.exitTime,
                averageDepth: jsonData.averageDepth,
                maxDepth: jsonData.maxDepth,
                visibility: jsonData.visibility,
                waterTemperature: jsonData.waterTemperature,
                airTemperature: jsonData.airTemperature,
                cylinderCapacity: jsonData.cylinderCapacity,
                divingSuit: jsonData.divingSuit,
                waterType: jsonData.waterType,
                waterEntryType: jsonData.waterEntryType,
                ballast: jsonData.ballast,
                glovesType: jsonData.glovesType,
                divingType: jsonData.divingType,
                comment: jsonData.comment
            });
        }); 
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    setMarker(newMarker) {
        this.setState({ marker: newMarker });
    }

    validateForm(e) {
        if(this.state.partnerName.length > 60) {
            this.setState({ partnerNameValidator: true });
            this.validator.push(true);
            e.preventDefault();
        } else {
            if(this.state.partnerNameValidator === true) {
                this.setState({ partnerNameValidator: false });
            }
        }

        if(this.state.partnerSurname.length > 100) {
            this.setState({ partnerSurnameValidator: true });
            this.validator.push(true);
            e.preventDefault();
        } else {
            if(this.state.partnerSurnameValidator === true) {
                this.setState({ partnerSurameValidator: false });
            }
        }

        if(this.state.maxDepth === 0.0) {
            this.setState({ maxDepthValidator: true });
            this.validator.push(true);
            e.preventDefault();
        } else {
            if(this.state.maxDepthValidator === true) {
                this.setState({ maxDepthValidator: false });
            }
        }

        if(this.state.visibility === 0.0) {
            this.setState({ visibilityValidator: true });
            this.validator.push(true);
            e.preventDefault();
        } else {
            if(this.state.visibilityValidator === true) {
                this.setState({ visibilityValidator: false });
            }
        }

        if($.isEmptyObject(this.state.marker)) {
            this.setState({ markerValidator: true });
            this.validator.push(true);
            e.preventDefault();
        } else {
            if(this.state.markerValidator === true) {
                this.setState({ markerValidator: false });
            }
        }
        console.log(this.state.marker);

        for(let property in this.state.marker) {
            console.log(this.state.marker[property]);
            if(this.state.marker[property] === '') {
                this.setState({ markerValidator: true });
                this.validator.push(true);
                e.preventDefault();
            } else {
                if(this.state.markerValidator === true) {
                    this.setState({ markerValidator: false });
                }
            }
        }
    }

    onSubmit(e) {
        e.preventDefault();

        this.validator = [];

        this.validateForm(e);
        
        if(this.validator.length === 0) {
            let jwtToken = this.Auth.getToken();
            
            const logbookObject = {
                partnerName: this.state.partnerName,
                partnerSurname: this.state.partnerSurname,
                marker: this.state.marker,
                entryTime: this.state.entryTime,
                exitTime: this.state.exitTime,
                averageDepth: this.state.averageDepth,
                maxDepth: this.state.maxDepth,
                visibility: this.state.visibility,
                waterTemperature: this.state.waterTemperature,
                airTemperature: this.state.airTemperature,    
                cylinderCapacity: this.state.cylinderCapacity,
                divingSuit: this.state.divingSuit,
                waterType: this.state.waterType,
                waterEntryType: this.state.waterEntryType,
                ballast: this.state.ballast,
                glovesType: this.state.glovesType,
                divingType: this.state.divingType,
                comment: this.state.comment
            }
            // ----------------------------------- ZMIENIĆ AXIOS NA METODĘ PUT --------------------------------------------------------
            axios({
                method: 'POST',
                url: `/add/logbook/${jwtToken}`,
                data: logbookObject,
                headers: {
                    "Accept": "application/json",
                    "Content-type": "application/json"
                }
            }).then(response => {
                this.props.history.push("/logbook");
            }).catch(function(error) {
                swal(this.props.t("googleMap.modal.swalError.title"), this.props.t("googleMap.modal.swalError.text"), "error");
            })
            //--------------------------------------------------------------------------------------------------------------------------
        }   
    }

    render() {
        return (
            <div className="add-dive-container">
                <div className="add-dive-center">
                    <div className="add-dive-box dive-shadow">
                        <div className="add-dive-title">
                            {this.props.t("addDive.form.title")}
                        </div>

                        <form onSubmit={this.onSubmit}>
                            <div className="form-group">
                                <label htmlFor="partnerName">
                                    {this.props.t("addDive.form.partnerName")}
                                </label>
                                <input 
                                    type="text" 
                                    className="form-control"
                                    id="partnerName" 
                                    placeholder="Enter partner's name"
                                    name="partnerName"
                                    value={this.state.partnerName}
                                    onChange={this.onChange}
                                />
                            </div>
                            <ShowInvalidPartnerName partnerNameValidator={this.state.partnerNameValidator} />

                            <div className="form-group">
                                <label htmlFor="partnerSurname">
                                    {this.props.t("addDive.form.partnerSurname")}
                                </label>
                                <input 
                                    type="text" 
                                    className="form-control"
                                    id="partnerSurname" 
                                    placeholder="Enter partner's surname" 
                                    name="partnerSurname"
                                    value={this.state.partnerSurname}
                                    onChange={this.onChange}
                                />
                            </div>
                            <ShowInvalidPartnerSurname partnerSurameValidator={this.state.partnerSurnameValidator} />

                            <div className="form-group row">
                                <label htmlFor="entryTime" className="col-sm-2 col-form-label">
                                    {this.props.t("addDive.form.entryTime")}
                                </label>
                                <div className="col-sm-10">
                                    <input 
                                        type="datetime-local" 
                                        className="form-control" 
                                        id="entryTime"
                                        name="entryTime"
                                        value={this.state.entryTime}
                                        onChange={this.onChange} 
                                    />
                                </div>
                            </div>

                            <div className="form-group row">
                                <label htmlFor="exitTime" className="col-sm-2 col-form-label">
                                    {this.props.t("addDive.form.exitTime")}
                                </label>
                                <div className="col-sm-10">
                                    <input 
                                        type="datetime-local" 
                                        className="form-control" 
                                        id="exitTime" 
                                        name="exitTime"
                                        value={this.state.exitTime}
                                        onChange={this.onChange}
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="avgDepth">
                                    {this.props.t("addDive.form.avgDepth")} [m]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.1" 
                                    className="form-control" 
                                    id="avgDepth" 
                                    name="averageDepth"
                                    value={this.state.averageDepth}
                                    onChange={this.onChange}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="maxDepth">
                                    {this.props.t("addDive.form.maxDepth")} [m]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.1"
                                    className="form-control" 
                                    id="maxDepth" 
                                    name="maxDepth"
                                    value={this.state.maxDepth}
                                    onChange={this.onChange}
                                />
                            </div>
                            <ShowInvalidMaxDepth maxDepthValidator={this.state.maxDepthValidator} />

                            <div className="form-group">
                                <label htmlFor="visibility">
                                    {this.props.t("addDive.form.visibility")} [m]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.1" 
                                    min="0" 
                                    className="form-control" 
                                    id="visibility" 
                                    name="visibility"
                                    value={this.state.visibility}
                                    onChange={this.onChange}
                                />
                            </div>
                            <ShowInvalidVisibility visibilityValidator={this.state.visibilityValidator} />
                            
                            <div className="form-group">
                                <label htmlFor="waterTemperature">
                                    {this.props.t("addDive.form.waterTemperature")} [<sup>o</sup>C]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.1" 
                                    min="-5" 
                                    className="form-control" 
                                    id="waterTemperature" 
                                    name="waterTemperature"
                                    value={this.state.waterTemperature}
                                    onChange={this.onChange}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="airTemperature">
                                    {this.props.t("addDive.form.airTemperature")} [<sup>o</sup>C]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.1" 
                                    min="-100" 
                                    className="form-control" 
                                    id="airTemperature" 
                                    name="airTemperature"
                                    value={this.state.airTemperature}
                                    onChange={this.onChange}
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group col-md-5">
                                    <label htmlFor="cylinder">
                                        {this.props.t("addDive.form.cylinderCapacity.title")}
                                    </label>
                                    <select 
                                        id="cylinder"
                                        className="custom-select mr-sm-2"
                                        name="cylinderCapacity"
                                        value={this.state.cylinderCapacity}
                                        onChange={this.onChange}
                                    >
                                        <option value="NONE">
                                        {this.props.t("addDive.form.cylinderCapacity.options.NONE")}
                                        </option>
                                        <option value="10L">10L</option>
                                        <option value="12L">12L</option>
                                        <option value="15L">15L</option>
                                        <option value="18L">18L</option>
                                        <option value="TWINSET">
                                            {this.props.t("addDive.form.cylinderCapacity.options.TWINSET")}
                                        </option>
                                        <option value="REBREATHER">
                                            {this.props.t("addDive.form.cylinderCapacity.options.REBREATHER")}
                                        </option>
                                    </select>
                                </div>

                                <div className="form-group col-md-3">
                                    <label htmlFor="suit">
                                        {this.props.t("addDive.form.suit.title")}
                                    </label>
                                    <select 
                                        id="suit"
                                        name="divingSuit"
                                        className="custom-select mr-sm-2"
                                        value={this.state.divingSuit}
                                        onChange={this.onChange}
                                    >
                                        <option value="NONE">
                                            {this.props.t("addDive.form.suit.options.NONE")}
                                        </option>
                                        <option value="DRY">
                                            {this.props.t("addDive.form.suit.options.DRY")}
                                        </option>
                                        <option value="SEMIARID">
                                            {this.props.t("addDive.form.suit.options.SEMIARID")}
                                        </option>
                                        <option value="WET">
                                            {this.props.t("addDive.form.suit.options.WET")}
                                        </option>
                                    </select>
                                </div>

                                <div className="form-group col-md-4">
                                    <label htmlFor="waterType">
                                        {this.props.t("addDive.form.waterType.title")}
                                    </label>
                                    <select 
                                        id="waterType"
                                        className="custom-select mr-sm-2"
                                        name="waterType"
                                        value={this.state.waterType}
                                        onChange={this.onChange}
                                    >
                                        <option value="NONE">
                                            {this.props.t("addDive.form.waterType.options.NONE")}
                                        </option>
                                        <option value="SWEET">
                                            {this.props.t("addDive.form.waterType.options.SWEET")}
                                        </option>
                                        <option value="SALT">
                                            {this.props.t("addDive.form.waterType.options.SALT")}
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="waterEntryType">
                                        {this.props.t("addDive.form.waterEntryType.title")}
                                    </label>
                                    <select  
                                        id="waterEntryType"
                                        name="waterEntryType"
                                        className="custom-select mr-sm-2"
                                        value={this.state.waterEntryType}
                                        onChange={this.onChange}
                                    >
                                        <option value="NONE">
                                            {this.props.t("addDive.form.waterEntryType.options.NONE")}
                                        </option>
                                        <option value="COAST">
                                            {this.props.t("addDive.form.waterEntryType.options.COAST")}
                                        </option>
                                        <option value="BOAT">
                                            {this.props.t("addDive.form.waterEntryType.options.BOAT")}
                                        </option>
                                    </select>
                                </div>

                                <div className="form-group col-md-6">
                                    <label htmlFor="glovesType">
                                        {this.props.t("addDive.form.glovesType.title")}
                                    </label>
                                    <select 
                                        id="glovesType"
                                        name="glovesType"
                                        className="custom-select mr-sm-2"
                                        value={this.state.glovesType}
                                        onChange={this.onChange}
                                    >
                                        <option value="NONE">
                                            {this.props.t("addDive.form.glovesType.options.NONE")}
                                        </option>
                                        <option value="WET">
                                            {this.props.t("addDive.form.glovesType.options.WET")}
                                        </option>
                                        <option value="DRY">
                                            {this.props.t("addDive.form.glovesType.options.DRY")}
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="ballast">
                                    {this.props.t("addDive.form.ballast")} [kg]
                                </label>
                                <input 
                                    type="number" 
                                    step="0.5" 
                                    min="0" 
                                    className="form-control" 
                                    id="ballast"
                                    name="ballast"
                                    value={this.state.ballast}
                                    onChange={this.onChange} 
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="divingType">
                                    {this.props.t("addDive.form.divingType.title")}
                                </label>
                                <select 
                                    id="divingType"
                                    name="divingType"
                                    className="custom-select mr-sm-2"
                                    value={this.state.divingType}
                                    onChange={this.onChange}
                                >
                                    <option value="NONE">
                                        {this.props.t("addDive.form.divingType.options.NONE")}
                                    </option>
                                    <option value="RECREATIONAL">
                                        {this.props.t("addDive.form.divingType.options.Recreational")}
                                    </option>
                                    <option value="TECHNICAL">
                                        {this.props.t("addDive.form.divingType.options.Technical")}
                                    </option>
                                    <option value="CAVE">
                                        {this.props.t("addDive.form.divingType.options.Cave")}
                                    </option>
                                    <option value="WRECK">
                                        {this.props.t("addDive.form.divingType.options.Wreck")}
                                    </option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="googleLogbookMap">
                                    {this.props.t("addDive.form.location")}
                                </label>
                                <GoogleLogbookMap 
                                    setMarker={this.setMarker}
                                />
                            </div>
                            <ShowInvalidMarker markerValidator={this.state.markerValidator} />

                            <div className="form-group">
                                <label htmlFor="comment">
                                    {this.props.t("addDive.form.comment")}
                                </label>
                                <textarea 
                                    className="form-control" 
                                    id="comment" 
                                    rows="7"
                                    name="comment"
                                    value={this.state.comment}
                                    onChange={this.onChange}
                                >
                                </textarea>
                            </div>

                            <div className="btn-add-dive-center">
                                <button type="submit" className="btn btn-primary btn-lg btn-add-dive">
                                    {this.props.t("addDive.form.button")}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

function ShowInvalidPartnerName(props) {
    if(props.partnerNameValidator) {
        return (
            <div className="alert alert-danger">
                {this.props.t("addDive.invalidPartnerName")}
            </div>
        );
    }
    return null;
}

function ShowInvalidPartnerSurname(props) {
    if(props.partnerSurameValidator) {
        return (
            <div className="alert alert-danger">
                {this.props.t("addDive.invalidPartnerSurname")}
            </div>
        );
    }
    return null;
}

function ShowInvalidMaxDepth(props) {
    if(props.maxDepthValidator) {
        return (
            <div className="alert alert-danger">
                {this.props.t("addDive.invalidMaxDepth")}
            </div>
        );
    }
    return null;
}

function ShowInvalidVisibility(props) {
    if(props.visibilityValidator) {
        return (
            <div className="alert alert-danger">
                {this.props.t("addDive.invalidVisibility")}
            </div>
        );
    }
    return null;
}

function ShowInvalidMarker(props) {
    if(props.markerValidator) {
        return (
            <div className="alert alert-danger">
                {this.props.t("addDive.invalidMarker")}
            </div>
        );
    }
    return null;
}

export default withTranslation("common")(withRouter(UpdateLogbook));