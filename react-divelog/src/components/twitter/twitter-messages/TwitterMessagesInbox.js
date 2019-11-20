import React from 'react';
import AuthService from '../../../util/AuthService';
import TwitterConversationContact from './TwitterConversationContact';
import $ from 'jquery';

class TwitterMessagesInbox extends React.Component {
    constructor() {
        super();

        this.state = {
            conversations: [],
            isConversationsRetrieved: false
        }
        this.Auth = new AuthService();
        this.renderConversations = this.renderConversations.bind(this);
    }

    componentDidMount() {
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`/twitter/direct/messages/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json())
        .then(json => {
            json.map((item, index) => {
                const element = {
                    recipientId: item.recipientId,
                    senderId: item.senderId,
                    name: item.name,
                    screenName: item.screenName,
                    createdAt: item.createdAt,
                    text: item.text,
                    pictureUrl: item.pictureUrl
                }
                this.setState({ conversations: this.state.conversations.concat(element) });
            });
            this.setState({ isConversationsRetrieved: true }, () => {
                $(".twitter-messages-list-persons-spinner").css({ display: "block" });
            });
        })
    }

    renderConversations() {
        return this.state.conversations
            .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
            .map((conversation, index) => {
            return (
                <TwitterConversationContact 
                    recipientId={conversation.recipientId}
                    senderId={conversation.senderId}
                    name={conversation.name}
                    screenName={conversation.screenName}
                    createdAt={conversation.createdAt}
                    text={conversation.text}
                    pictureUrl={conversation.pictureUrl}
                />
            );
        });
    }

    render() {
        let isConversationsRetrieved = this.state.isConversationsRetrieved;
        return (
            <div className="twitter-messages-list-persons-spinner">
                <ul className="list-group">
                    { isConversationsRetrieved && this.renderConversations() }
                </ul>
                { !isConversationsRetrieved &&
                    <div 
                        className='spinner-border text-primary' 
                        role='status'
                    >
                        <span class='sr-only'>
                            Loading...
                        </span>
                    </div>
                }
            </div>
        );
    }
}

export default TwitterMessagesInbox;