import React from 'react';
import '../../css/AddDive.css';
import GoogleLogbookMap from './GoogleLogbookMap';

class AddDive extends React.Component {
    constructor() {
        super();

        this.state = {

        }
        this.onChange = this.onChange.bind(this);
    }
    componentDidMount() {

    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    render() {
        return (
            <div className="add-dive-container">
                <div className="add-dive-title">
                    Add dive to logbook
                </div>
                <div className="add-dive-center">
                    <div className="add-dive-box dive-shadow">
                        <form>
                            <div className="form-group">
                                <label htmlFor="partnerName">Partner's name</label>
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="partnerName" 
                                    placeholder="Enter partner's name" 
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="partnerSurname">Partner's surname</label>
                                <input type="text" className="form-control" id="partnerSurname" placeholder="Enter partner's surname" />
                            </div>

                            <div className="form-group row">
                                <label htmlFor="date" className="col-sm-2 col-form-label">Date</label>
                                <div className="col-sm-10">
                                    <input type="date" className="form-control" id="date" placeholder="Enter date" />
                                </div>
                            </div>

                            <div className="form-group row">
                                <label htmlFor="entryTime" className="col-sm-2 col-form-label">Entry time</label>
                                <div className="col-sm-10">
                                    <input type="time" className="form-control" id="entryTime" />
                                </div>
                            </div>

                            <div className="form-group row">
                                <label htmlFor="exitTime" className="col-sm-2 col-form-label">Exit time</label>
                                <div className="col-sm-10">
                                    <input type="time" className="form-control" id="exitTime" />
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="avgDepth">Average depth</label>
                                <input type="number" step="0.1" min="1" className="form-control" id="avgDepth" />
                            </div>

                            <div className="form-group">
                                <label htmlFor="maxDepth">Maximum depth</label>
                                <input type="number" step="0.1" min="1" className="form-control" id="maxDepth" />
                            </div>

                            <div className="form-group">
                                <label htmlFor="visibility">Visibility</label>
                                <input type="number" step="0.1" min="0" className="form-control" id="visibility" />
                            </div>

                            <div className="form-row">
                                <div className="form-group col-md-5">
                                    <label htmlFor="cylinder">Cylinder capacity</label>
                                    <select className="custom-select mr-sm-2" id="cylinder">
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
                                    <select className="custom-select mr-sm-2" id="suit">
                                        <option defaultValue>Choose suit...</option>
                                        <option value="DRY">Dry suit</option>
                                        <option value="SEMIARID">Semiarid suit</option>
                                        <option value="WET">Wet suit</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>

                                <div className="form-group col-md-4">
                                    <label htmlFor="waterType">Water type</label>
                                    <select className="custom-select mr-sm-2" id="waterType">
                                        <option defaultValue>Choose water type...</option>
                                        <option value="SWEET">Sweet water</option>
                                        <option value="SALT">Salt water</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="entryTypeWater">Entry type to water</label>
                                    <select className="custom-select mr-sm-2" id="entryTypeWater">
                                        <option defaultValue>Choose entry type...</option>
                                        <option value="COAST">COAST</option>
                                        <option value="BOAT">BOAT</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>

                                <div className="form-group col-md-6">
                                    <label htmlFor="glovesType">Gloves type</label>
                                    <select className="custom-select mr-sm-2" id="glovesType">
                                        <option defaultValue>Choose gloves type...</option>
                                        <option value="WET">WET</option>
                                        <option value="DRY">DRY</option>
                                        <option value="NONE">NONE</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="ballast">Ballast</label>
                                <input type="number" step="0.5" min="0" className="form-control" id="ballast" />
                            </div>

                            <div className="form-group">
                                <label htmlFor="divingType">Diving type</label>
                                <select className="custom-select mr-sm-2" id="divingType">
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
                                <GoogleLogbookMap />
                            </div>

                            <div className="form-group">
                                <label htmlFor="comment">Comment</label>
                                <textarea className="form-control" id="comment" rows="7"></textarea>
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