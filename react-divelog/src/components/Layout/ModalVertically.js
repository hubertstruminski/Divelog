import React from 'react';
import $ from 'jquery';

class ModalVertically extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            name: ''
        }

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    onSubmit() {
        let name = $("#name").val();

        console.log(this.props.latitude);
        console.log(this.props.longitude);
        console.log(name);

        this.props.setFinishMarker();
        $("#modalCenter").modal('hide');
    }


    render() {
        return (
            <div className="modal fade" id="modalCenter" tabindex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered" role="document">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="modalCenterTitle">Add marker to story</h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        <div className="form-group">
                            <label htmlFor="name">Name for marker:</label>
                            <input 
                                type="text" 
                                id="name" 
                                name="name"
                                className="form-control" 
                                value={this.state.name}
                                onChange={this.onChange}
                            />
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-danger" data-dismiss="modal">Close</button>
                        <button 
                            type="button" 
                            className="btn btn-success"
                            onClick={this.onSubmit}
                        >
                            Save marker
                        </button>
                    </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ModalVertically;