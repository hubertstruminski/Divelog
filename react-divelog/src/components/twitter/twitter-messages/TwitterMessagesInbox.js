import React from 'react';
import AuthService from '../../../util/AuthService';
import TwitterConversationContact from './TwitterConversationContact';
import $ from 'jquery';
import TwitterMessagesSearch from './TwitterMessagesSearch';

class TwitterMessagesInbox extends React.Component {
    constructor() {
        super();

        this.state = {
            conversations: [],
            isConversationsRetrieved: false,
            copyOfConversations: [],
            isLoading: true
        }
        this.Auth = new AuthService();
        this.copyOfConversations = [];
        this.renderConversations = this.renderConversations.bind(this);
        this.searchInList = this.searchInList.bind(this);
        this.retrieveConversations = this.retrieveConversations.bind(this);
        this.setIsConversationRetrieved = this.setIsConversationRetrieved.bind(this);
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
                this.setState({ isLoading: false });
                this.copyOfConversations = this.state.conversations.map((x) => x);
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

    searchInList(searchInput) {
        this.setState({ 
            isConversationsRetrieved: true,
            conversations: this.copyOfConversations.map((x) => x)
        
        }, () => {
            return this.state.conversations.map((conversation, index) => {
                if(conversation.name.includes(searchInput) || conversation.screenName.includes(searchInput)) {
                    this.setState({ conversations: this.state.conversations.filter((item, i) => i === index)});
                }
            });
        });
        return null;
    }

    retrieveConversations() {
        return this.copyOfConversations
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

    setIsConversationRetrieved(value) {
        this.setState({ isConversationsRetrieved: value });
    }

    render() {
        let isConversationsRetrieved = this.state.isConversationsRetrieved;
        let isLoading = this.state.isLoading;
        return (
            <React.Fragment>
                <TwitterMessagesSearch 
                    searchInList={this.searchInList}
                    retrieveConversations={this.retrieveConversations}
                    setIsConversationRetrieved={this.setIsConversationRetrieved}
                />
                <div className="twitter-messages-list-persons-spinner">
                    <ul className="list-group">
                        { isConversationsRetrieved && this.renderConversations() }
                        { !isConversationsRetrieved && this.retrieveConversations() }
                    </ul>
                    { isLoading &&
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
            </React.Fragment>
        );
    }
}

export default TwitterMessagesInbox;