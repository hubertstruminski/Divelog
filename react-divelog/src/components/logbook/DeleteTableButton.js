import React from 'react';
import { withTranslation } from 'react-i18next';
import swal from 'sweetalert';

class DeleteTableButton extends React.Component {
    constructor(props) {
        super(props);

        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        let jwtToken = localStorage.getItem("JwtToken");
        let id = this.props.id;

        fetch(`/logbook/${id}/${jwtToken}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => {
            if(response.status === 200) {
                this.props.setIsDeletedRow(true);
                this.props.setDeletedLogbookId(id);
                swal("Success", "Record has been removed successfully.", "success");
            } else {
                swal("Error", "Bad Request. Something goes wrong.", "error");
            }
        });
    }

    render() {
        return (
            <button 
                className="btn btn-danger"
                onClick={this.onClick}
            >
                {this.props.t("logbook.table.DELETE")}
            </button>
        );
    }
}

export default withTranslation("common")(DeleteTableButton);