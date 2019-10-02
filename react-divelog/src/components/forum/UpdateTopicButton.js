import React from 'react';
import { withRouter } from 'react-router';

class UpdateTopicButton extends React.Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        let topicId = this.props.topicId;
        this.props.history.push(`/update/topic/${topicId}`);
    }

    render() {
        return (
            <button 
                className="btn btn-warning"
                onClick={this.onClick}
            >
                EDIT
            </button>
        );
    }
}

export default withRouter(UpdateTopicButton);