import React from 'react';
import { withTranslation } from 'react-i18next';
import { AuthObject } from '../../util/AuthObject';

class DeleteButton extends React.Component {
    constructor(props) {
        super(props);

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit() {
        let markerID = this.props.id;

        fetch(`/delete/marker/${AuthObject.userID}/${markerID}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        })
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