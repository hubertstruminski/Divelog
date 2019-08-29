import React from 'react';
import { withTranslation } from 'react-i18next';
import $ from 'jquery';
import swal from 'sweetalert';

class GoogleLogbookModal extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    onSubmit() {
        try {
            let name = $("#name").val();

            const googleMarker = {
                name: name,
                latitude: this.props.latitude,
                longitude: this.props.longitude
            }
            this.props.updateMarker(googleMarker);

            this.props.setFinishMarker(true);
            $("#modalLogbookCenter").modal('hide');
        } catch(error) {
            swal("Error", "Something goes wrong.", "error");
        }
    }

    render() {
        return(
            <div className="modal fade" id="modalLogbookCenter" tabIndex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
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

export default withTranslation("common")(GoogleLogbookModal);