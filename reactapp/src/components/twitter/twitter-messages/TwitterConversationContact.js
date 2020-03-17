import React from 'react';
import '../../../css/twitter-messages/TwitterConversationContact.css';
import ConvertDMTime from '../../../util/ConvertDMTime';
import $ from 'jquery';

class TwitterConversationContact extends React.Component {
    constructor(props) {
        super(props);
        this.convertTime = new ConvertDMTime();
    }

    componentDidMount() {
        // don't remove this jQuery code, with pure Javascript doesn't work
        $(".conversation-contact-avatar").css({
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
        })
    }

    onSubmit(e) {
        e.preventDefault();
        this.props.setIsLoadingConversation(
            true,
            this.props.userId,
            this.props.name,
            this.props.screenName,
            this.props.pictureUrl
        );
        // this.props.reRenderSingleMessages();
    }

    render() {
        let text = "";
        if(this.props.text.length > 40) {
            text = this.props.text.substring(0, 40);
        } else {
            text = this.props.text.substring(0);
        }
        // document.getElementsByClassName("conversation-contact-avatar").style.display = "flex;"
        // document.getElementsByClassName("conversation-contact-avatar").style.alignItems = "center";
        // document.getElementsByClassName("conversation-contact-avatar").style.justifyContent = "center";

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