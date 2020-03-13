import React from 'react';
import '../../../css/twitter-messages/TwitterConversationContact.css';
import ConvertDMTime from '../../../util/ConvertDMTime';

class TwitterConversationContact extends React.Component {
    constructor(props) {
        super(props);
        this.convertTime = new ConvertDMTime();
    }

    onSubmit(e) {
        e.preventDefault();
        this.props.setIsLoadingConversation(
            true,
            this.props.recipientId,
            this.props.senderId,
            this.props.name,
            this.props.screenName,
            this.props.pictureUrl 
        );
    }

    render() {
        let text = "";
        if(this.props.text.length > 40) {
            text = this.props.text.substring(0, 40);
        } else {
            text = this.props.text.substring(0);
        }

        let date = this.convertTime.formatDate(this.props.createdAt, false);

        return (
            <li className="conversation-contact-wrapper" onClick={this.onSubmit.bind(this)}>
                <div className="conversation-contact-avatar">
                    <img src={this.props.pictureUrl} alt="Avatar" />
                </div>
                <div className="conversation-contact-info">
                    <div className="conversation-contact-name">
                        {this.props.name}
                    </div>
                    <div className="conversation-contact-date">{date}</div>
                    <br />
                    @{ this.props.screenName }
                    <br />
                    { text }
                </div>
                    
            </li>
        );
    }
}

export default TwitterConversationContact;