import React from 'react';
import '../../../css/twitter-messages/TwitterMessagesInbox.css';
import AuthService from '../../../util/AuthService';
import TwitterConversationContact from './TwitterConversationContact';
import $ from 'jquery';
import TwitterMessagesSearch from './TwitterMessagesSearch';
import Conversation from './Conversation';
import { BACKEND_API_URL } from '../../../actions/types';

class TwitterMessagesInbox extends React.Component {
    constructor() {
        super();

        this.state = {
            conversations: [],
            isConversationsRetrieved: false,
            copyOfConversations: [],
            isLoading: true,
            isLoadingConversation: false,
            userId: '',
            isConversationClicked: false,
            name: '',
            screenName: '',
            pictureUrl: '',
            isChangedConversationContext: false,
            directMessages: [],
            isRateLimitExceeded: false
        }
        this.isError = false;
        this.child = React.createRef();

        this.Auth = new AuthService();
        this.copyOfConversations = [];
        this.renderConversations = this.renderConversations.bind(this);
        this.searchInList = this.searchInList.bind(this);
        this.retrieveConversations = this.retrieveConversations.bind(this);
        this.setIsConversationRetrieved = this.setIsConversationRetrieved.bind(this);
        this.searchPeopleToConversation = this.searchPeopleToConversation.bind(this);
        this.setIsLoadingConversation = this.setIsLoadingConversation.bind(this);
        this.setIsChangedConversationContext = this.setIsChangedConversationContext.bind(this);
    }

    componentDidMount() {
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`${BACKEND_API_URL}/twitter/direct/messages/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => { 
            if(response.status === 429) {
                this.setState({ 
                    isRateLimitExceeded: true,
                    isLoading: false
                });
                this.isError = true;
                return;
            }
            return response.json()
        }).then(json => {
            if(!this.isError) {
                json.map((item, index) => {
                    const element = {
                        userId: item.userId,
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
            }
        }).catch(err => {
            console.log(err);
        });
    }

    renderConversations() {
        return this.state.conversations
            .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
            .map((conversation, index) => {
            return (
                <TwitterConversationContact 
                    userId={conversation.userId}
                    name={conversation.name}
                    screenName={conversation.screenName}
                    createdAt={conversation.createdAt}
                    text={conversation.text}
                    pictureUrl={conversation.pictureUrl}
                    setIsLoadingConversation={this.setIsLoadingConversation}
                    reRenderSingleMessages={this.reRenderSingleMessages}
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
                    userId={conversation.userId}
                    name={conversation.name}
                    screenName={conversation.screenName}
                    createdAt={conversation.createdAt}
                    text={conversation.text}
                    pictureUrl={conversation.pictureUrl}
                    setIsLoadingConversation={this.setIsLoadingConversation}
                />
            );
        });
    }

    searchPeopleToConversation(e) {
        e.preventDefault();
        this.props.searchPeopleToConversation();
    }

    setIsConversationRetrieved(value) {
        this.setState({ isConversationsRetrieved: value });
    }

    setIsLoadingConversation(value, userId, name, screenName, pictureUrl) {
        this.setState({ 
            isConversationClicked: true,
            isLoadingConversation: value,
            userId: userId,
            name: name,
            screenName: screenName,
            pictureUrl: pictureUrl
        }, () => {
            this.child.current.retrieveSingleMessages();
        });
    }

    setIsChangedConversationContext(value) {
        this.setState({ isChangedConversationContext: value });
    }

    render() {
        let isConversationsRetrieved = this.state.isConversationsRetrieved;
        let isLoading = this.state.isLoading;
        let isConversationClicked = this.state.isConversationClicked;
        let isRateLimitExceeded = this.state.isRateLimitExceeded;
        return (
            <>
                <div className="twitter-messages-list-inboxfalse">
                    <div className="twitter-messages-title-box">
                        <div className="twitter-messages-title">Messages</div>
                        <i 
                            className="far fa-envelope twitter-messages-add-icon" 
                            onClick={this.searchPeopleToConversation}
                        ></i>
                    </div>
                    <TwitterMessagesSearch 
                        searchInList={this.searchInList}
                        retrieveConversations={this.retrieveConversations}
                        setIsConversationRetrieved={this.setIsConversationRetrieved}
                    />
                    <div className="twitter-messages-list-persons-spinner">
                        <ul className="list-group">
                            { isConversationsRetrieved && this.renderConversations() }
                            {/* { !isConversationsRetrieved && this.retrieveConversations() } */}
                        </ul>
                        { isLoading &&
                            <div 
                                className='spinner-border tfalsext-primary' 
                                role='status'
                            >
                                <span class='sr-only'>
                                    Loading...
                                </span>
                            </div>
                        }
                        {
                            isRateLimitExceeded &&
                            <span style={{ color: "red", fontSize: "0.65vw" }}>Twitter rate limit exceeded.</span>
                        }
                    </div>
                </div>
                <div className="twitter-messages-person-invite-wrapper">
                    { !isConversationClicked &&
                        <div className="twitter-messages-person-invite">
                            <span style={{ fontWeight: 700, fontSize: '1.1vw' }}>You don't have a message selected</span>
                            <br />
                            Choose one from your existing messages, or start a new one.
                            <br />
                            <button 
                                className="twitter-message-person-btn-new-message"
                                onClick={this.searchPeopleToConversation}
                            >
                                New message
                            </button>
                        </div>
                    }
                    { isConversationClicked &&
                        <Conversation 
                            userId={this.state.userId}
                            pictureUrl={this.state.pictureUrl}
                            isLoadingConversation={this.state.isLoadingConversation}
                            name={this.state.name}
                            screenName={this.state.screenName}
                            ref={this.child}
                        />
                    }
                </div>
            </>
        );
    }
}

export default TwitterMessagesInbox;