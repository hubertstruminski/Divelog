import React from 'react';
import { withTranslation } from 'react-i18next';

class PDFTableButton extends React.Component {
    render() {
        return (
            <button className="btn btn-primary">
                PDF
            </button>
        );
    }
}

export default withTranslation("common")(PDFTableButton);