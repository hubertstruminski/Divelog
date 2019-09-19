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
        }).then(response => {
            if(response.status === 400) {
                swal("No access", "You can not remove marker assigned to dive from logbook.", "error");
            } else {
                swal("Success", "Record has been removed successfully.", "success");
                this.props.setDeletedMarkerId(markerID);
                this.props.fetchMarkers();
            }
        });
        this.props.setIsDeletedMarker();
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