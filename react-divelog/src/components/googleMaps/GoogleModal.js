import React from 'react';
import $ from 'jquery';
import { fakeAuth } from '../../util/fakeAuth';
import swal from 'sweetalert';
import { withTranslation } from 'react-i18next';

class GoogleModal extends React.Component {
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

        const googleMarker = {
            name: name,
            latitude: this.props.latitude,
            longitude: this.props.longitude
        }

        fetch(`/add/marker/${fakeAuth.userID}`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            },
            body: JSON.stringify(googleMarker)
        });

        this.props.setFinishMarker();
        $("#modalCenter").modal('hide');

        swal(
            this.props.t("googleMap.modal.swal.title"), 
            this.props.t("googleMap.modal.swal.text"), 
            "success"
        );
    }

    render() {
        return (
            <div className="modal fade" id="modalCenter" tabindex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered" role="document">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="modalCenterTitle">
                            {this.props.t("googleMap.modal.title")}
                        </h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        <div className="form-group">
                            <label htmlFor="name">
                                {this.props.t("googleMap.modal.text")}
                            </label>
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
                        <button type="button" className="btn btn-danger" data-dismiss="modal">
                            {this.props.t("googleMap.modal.close")}
                        </button>
                        <button 
                            type="button" 
                            className="btn btn-success"
                            onClick={this.onSubmit}
                        >
                            {this.props.t("googleMap.modal.save")}
                        </button>
                    </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withTranslation("common")(GoogleModal);