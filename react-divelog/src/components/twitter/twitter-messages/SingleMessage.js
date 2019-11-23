import React from 'react';
import '../../../css/twitter-messages/SingleMessage.css';

class SingleMessage extends React.Component {
    render() {
        return (
            <div className="twitter-single-message-container">
                <div className="twitter-single-message-text">
                    { this.props.text }
                </div>
                    { this.props.createdAt }
            </div>
        );
    }
}

export default SingleMessage;