import React from 'react';
import '../../css/AddDive.css';
import GoogleLogbookMap from './GoogleLogbookMap';
import AuthService from '../../util/AuthService';
import axios from 'axios';

class AddDive extends React.Component {
    constructor() {
        super();

        this.state = {
            partnerName: '',
            partnerSurname: '',
            marker: {},
            // date: '',
            entryTime: '',
            exitTime: '',
            averageDepth: 0.0,
            maxDepth: 0.0,
            visibility: 0.0,
            cylinderCapacity: '',
            divingSuit: '',
            waterType: '',
            waterEntryType: '',
            ballast: 0.0,
            glovesType: '',
            divingType: '',
            comment: ''
        }
        this.Auth = new AuthService();

        this.onChange = this.onChange.bind(this);
        this.setMarker = this.setMarker.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    setMarker(newMarker) {
        this.setState({ marker: newMarker });
    }

    onSubmit(e) {
        try {
            e.preventDefault();
            let jwtToken = this.Auth.getToken();
            
            const logbookObject = {
                partnerName: this.state.partnerName,
                partnerSurname: this.state.partnerSurname,
                marker: this.state.marker,
                // date: this.state.date,
                entryTime: this.state.entryTime,
                exitTime: this.state.exitTime,
                averageDepth: this.state.averageDepth,
                maxDepth: this.state.maxDepth,
                visibility: this.state.visibility,
                cylinderCapacity: this.state.cylinderCapacity,
                divingSuit: this.state.divingSuit,
                waterType: this.state.waterType,
                waterEntryType: this.state.waterEntryType,
                ballast: this.state.ballast,
                glovesType: this.state.glovesType,
                divingType: this.state.divingType,
                comment: this.state.comment
            }

            axios({
                method: 'POST',
                url: `/add/logbook/${jwtToken}`,
                data: logbookObject,
                headers: {
                    "Accept": "application/json",
                    "Content-type": "application/json"
                }
            }).then(response => {
                console.log(response);
            });
        } catch(error) {

        }
    }

    render() {
        return (
            <div className="add-dive-container">
                <div className="add-dive-title">
                    Add dive to logbook
                </div>
                <div className="add-dive-center">
                    <div className="add-dive-box dive-shadow">
                        <form onSubmit={this.onSubmit}>
                            <div className="form-group">
                                <label htmlFor="partnerName">Partner's name</label>
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

                            <div className="form-group">
                                <label htmlFor="partnerSurname">Partner's surname</label>
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

                            {/* <div className="form-group row">
                                <label htmlFor="date" className="col-sm-2 col-form-label">Date</label>
                                <div className="col-sm-10">
                                    <input 
                                        type="date" 
                                        className="form-control" 
                                        id="date" 
                                        placeholder="Enter date"
                                        name="date"
                                        value={this.state.date}
                                        onChange={this.onChange} 
                                    />
                                </div>
                            </div> */}

                            <div className="form-group row">
                                <label htmlFor="entryTime" className="col-sm-2 col-form-label">Entry time</label>
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
                                <label htmlFor="exitTime" className="col-sm-2 col-form-label">Exit time</label>
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
                                <label htmlFor="avgDepth">Average depth</label>
                                <input 
                                    type="number" 
                                    step="0.1" 
                                    min="1" 
                                    className="form-control" 
                                    id="avgDepth" 
                                    name="averageDepth"
                                    value={this.state.averageDepth}
                                    onChange={this.onChange}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="maxDepth">Maximum depth</label>
                                <input 
                                    type="number" 
                                    step="0.1" min="1"
                                    className="form-control" 
                                    id="maxDepth" 
                                    name="maxDepth"
                                    value={this.state.maxDepth}
                                    onChange={this.onChange}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="visibility">Visibility</label>
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

                            <div className="form-row">
                                <div className="form-group col-md-5">
                                    <label htmlFor="cylinder">Cylinder capacity</label>
                                    <select 
                                        className="custom-select mr-sm-2" 
                                        id="cylinder"
                                        name="cylinderCapacity"
                                        value={this.state.cylinderCapacity}
                                        onChange={this.onChange}
                                    >
                                        <option defaultValue>Choose cylinder...</option>
                                        <option value="10L">10L</option>
                                        <option value="12L">12L</option>
                                        <option value="15L">15L</option>
                                        <option value="18L">18L</option>
                                        <option value="TWINSET">Twinset</option>
                                        <option value="REBREATHER">Rebreather</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>

                                <div className="form-group col-md-3">
                                    <label htmlFor="suit">Suit</label>
                                    <select 
                                        className="custom-select mr-sm-2" 
                                        id="suit"
                                        name="divingSuit"
                                        value={this.state.divingSuit}
                                        onChange={this.onChange}
                                    >
                                        <option defaultValue>Choose suit...</option>
                                        <option value="DRY">Dry suit</option>
                                        <option value="SEMIARID">Semiarid suit</option>
                                        <option value="WET">Wet suit</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>

                                <div className="form-group col-md-4">
                                    <label htmlFor="waterType">Water type</label>
                                    <select 
                                        className="custom-select mr-sm-2" 
                                        id="waterType"
                                        name="waterType"
                                        value={this.state.waterType}
                                        onChange={this.onChange}
                                    >
                                        <option defaultValue>Choose water type...</option>
                                        <option value="SWEET">Sweet water</option>
                                        <option value="SALT">Salt water</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="waterEntryType">Entry type to water</label>
                                    <select 
                                        className="custom-select mr-sm-2" 
                                        id="waterEntryType"
                                        name="waterEntryType"
                                        value={this.state.waterEntryType}
                                        onChange={this.onChange}
                                    >
                                        <option defaultValue>Choose entry type...</option>
                                        <option value="COAST">COAST</option>
                                        <option value="BOAT">BOAT</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>

                                <div className="form-group col-md-6">
                                    <label htmlFor="glovesType">Gloves type</label>
                                    <select 
                                        className="custom-select mr-sm-2" 
                                        id="glovesType"
                                        name="glovesType"
                                        value={this.state.glovesType}
                                        onChange={this.onChange}
                                    >
                                        <option defaultValue>Choose gloves type...</option>
                                        <option value="WET">WET</option>
                                        <option value="DRY">DRY</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="ballast">Ballast</label>
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
                                <label htmlFor="divingType">Diving type</label>
                                <select 
                                    className="custom-select mr-sm-2" 
                                    id="divingType"
                                    name="divingType"
                                    value={this.state.divingType}
                                    onChange={this.onChange}
                                >
                                    <option defaultValue>Choose diving type...</option>
                                    <option value="RECREATIONAL">Recreational diving</option>
                                    <option value="TECHNICAL">Technical diving</option>
                                    <option value="CAVE">Cave diving</option>
                                    <option value="WRECK">Wreck diving</option>
                                    <option value="NONE">NONE</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="googleLogbookMap">Location</label>
                                <GoogleLogbookMap 
                                    setMarker={this.setMarker}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="comment">Comment</label>
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
                                    Confirm
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

export default AddDive;