import React from 'react';
import { withTranslation } from 'react-i18next';
import swal from 'sweetalert';

class DeleteButton extends React.Component {
    constructor(props) {
        super(props);

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit() {
        let markerID = this.props.id;
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/delete/marker/${jwtToken}/${markerID}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        })
        this.props.setIsDeletedMarker();

        swal(
            this.props.t("googleMap.modal.swal.title"), 
            this.props.t("googleMap.modal.swal.text"), 
            "success"
        );
    }

    render() {
        return (
            <button
                className="btn btn-danger"
                onClick={this.onSubmit}
            >
                {this.props.t("googleMap.table.delete")}
            </button>
        );
    }
}

export default withTranslation("common")(DeleteButton);