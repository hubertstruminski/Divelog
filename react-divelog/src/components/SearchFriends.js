import React from 'react';
import { withTranslation } from 'react-i18next';

class SearchFriends extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <input
                    type="text"
                    className="form-control"
                    placeholder={this.props.t("searchFriends.placeholder")}
                />
            </div>
        );
    }
}

export default withTranslation('common')(SearchFriends);