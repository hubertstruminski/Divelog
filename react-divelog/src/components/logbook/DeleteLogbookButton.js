import React from 'react';
import { withTranslation } from 'react-i18next';
import swal from 'sweetalert';

class DeleteLogbookButton extends React.Component {
    constructor(props) {
        super(props);

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();

        const markerObject = {
            name: '',
            latitude: '',
            longitude: ''
        }
        this.props.updateMarker(markerObject);
        this.props.setIsAccessible();
        this.props.setFinishMarker(false);
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

export default withTranslation("common")(DeleteLogbookButton);