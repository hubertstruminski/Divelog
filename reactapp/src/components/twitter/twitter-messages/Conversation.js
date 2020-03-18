import React from 'react';
import '../../../css/twitter-messages/Conversation.css';
import axios from 'axios';
import AuthService from '../../../util/AuthService';
import SingleMessage from './SingleMessage';
import { BACKEND_API_URL } from '../../../actions/types';
import $ from 'jquery';

class Conversation extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoadingConversation: this.props.isLoadingConversation,
            directMessages: [],
            isSingleMessageRetrieved: false,
            isFirstTimeRendered: true,
            isRateLimitExceeded: false
        }
        this.isError = false;
        this.Auth = new AuthService();
        this.renderSingleMessages = this.renderSingleMessages.bind(this);
    }

    componentDidMount() {
        let jwtToken = this.Auth.getRightSocialToken();

        if(this.state.isFirstTimeRendered) {
            axios({
                url: `${BACKEND_API_URL}/twitter/direct/messages/specified/person/${jwtToken}`,
                method: 'POST',
                data: JSON.stringify(this.props.userId),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if(response.status === 429) {
                    this.setState({ isRateLimitExceeded: true });
                    this.isError = true;
                    return;
                }
                if(!this.isError) {
                    response.data.map((message, index) => {
                        let urlEntities = [];
                        let mediaEntities = [];

                        message.urlEntities.map((urlEntity) => {
                            let element = urlEntity.expandedURL;
                            urlEntities.push(element);
                        });
        
                        message.mediaEntities.map((mediaEntity) => {
                            let element = {
                                mediaUrl: mediaEntity.mediaURL,
                                type: mediaEntity.type
                            }
                            mediaEntities.push(element);
                        });
                        const singleMessage = {
                            id: message.id,
                            createdAt: message.createdAt,
                            recipientId: message.recipientId,
                            senderId: message.senderId,
                            text: message.text,
                            mediaEntities: mediaEntities,
                            urlEntities: urlEntities,
                            twitterOwnerId: message.twitterOwnerId
                        }
                        this.setState({ directMessages: this.state.directMessages.concat(singleMessage) });
                    });
                }
                this.setState({ 
                    isLoadingConversation: false,
                    isSingleMessageRetrieved: true,
                    isFirstTimeRendered: false
                });
            }).catch(err => {
                console.log(err);
            });
        }
        
    }

    retrieveSingleMessages() {
        if(!this.state.isFirstTimeRendered) {
            this.setState({ 
                isSingleMessageRetrieved: false,
                directMessages: []
            }, () => {
                let jwtToken = this.Auth.getRightSocialToken();
                axios({
                    url: `${BACKEND_API_URL}/twitter/direct/messages/specified/person/${jwtToken}`,
                    method: 'POST',
                    data: JSON.stringify(this.props.userId),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if(response.status === 429) {
                        this.setState({ isRateLimitExceeded: true });
                        this.isError = true;
                        return;
                    }
                    if(!this.isError) {
                        response.data.map((message, index) => {
                            let urlEntities = [];
                            let mediaEntities = [];
            
                            message.urlEntities.map((urlEntity) => {
                                let element = urlEntity.expandedURL;
                                urlEntities.push(element);
                            });
            
                            message.mediaEntities.map((mediaEntity) => {
                                let element = {
                                    mediaUrl: mediaEntity.mediaURL,
                                    type: mediaEntity.type
                                }
                                mediaEntities.push(element);
                            });
                            const singleMessage = {
                                id: message.id,
                                createdAt: message.createdAt,
                                recipientId: message.recipientId,
                                senderId: message.senderId,
                                text: message.text,
                                mediaEntities: mediaEntities,
                                urlEntities: urlEntities,
                                twitterOwnerId: message.twitterOwnerId
                            }
                            this.setState({ directMessages: this.state.directMessages.concat(singleMessage) });
                        });
                    }
                    this.setState({ isSingleMessageRetrieved: true })
                }).catch(err => {
                    console.log(err);
                });
            });
        }
    }

    renderSingleMessages() {
        return this.state.directMessages
        .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
        .map((singleMessage, index) => {
            return (
                <SingleMessage 
                    index={index}
                    id={singleMessage.id}
                    createdAt={singleMessage.createdAt}
                    recipientId={singleMessage.recipientId}
                    senderId={singleMessage.senderId}
                    text={singleMessage.text}
                    mediaEntities={singleMessage.mediaEntities}
                    urlEntities={singleMessage.urlEntities}
                    twitterOwnerId={singleMessage.twitterOwnerId}
                    pictureUrl={this.props.pictureUrl}
                />
            );
        });
    }

    render() {
        let isLoadingConversation = this.state.isLoadingConversation;
        let isSingleMessageRetrieved = this.state.isSingleMessageRetrieved;
        let isRateLimitExceeded = this.state.isRateLimitExceeded;
        return (
            <>
                { isLoadingConversation &&
                    <div 
                        className='spinner-border text-primary' 
                        role='status'
                    >
                        <span class='sr-only'>
                            Loading...
                        </span>
                    </div>
                }
                {
                    !isLoadingConversation &&
                    <div className="twitter-messages-direct-message-person-conversation-wrapper">
                        <div className="twitter-messages-direct-message-person-title-info">
                            <div className="twitter-direct-message-screen-name-container">
                                <span style={{fontWeight: 700, fontSize: '1vw'}}>{ this.props.name }</span>
                                <br />
                                @{ this.props.screenName }
                            </div>
                            <div className="twitter-direct-message-info-icon">
                                <i class="fas fa-info-circle"></i>
                            </div>
                        </div>
                        <div className="twitter-messages-direct-message-container">
                            {
                                isSingleMessageRetrieved && this.renderSingleMessages()
                            }
                            {
                                isRateLimitExceeded &&
                                <span style={{ color: "red", fontSize: "0.65vw" }}>Twitter rate limit exceeded.</span>
                            }
                        </div>
                        <div className="twitter-messages-direct-message-send-input-container">
                            <i class="fas fa-image"></i>
                                <textarea
                                    placeholder="Start a new message"
                                ></textarea>
                            <i class="fas fa-check"></i>
                        </div>
                    </div>
                }
            </>
        );
    }
}

export default Conversation;