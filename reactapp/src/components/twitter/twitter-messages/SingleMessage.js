import React from 'react';
import '../../../css/twitter-messages/SingleMessage.css';
import $ from 'jquery';
import ConvertDMTime from '../../../util/ConvertDMTime';
import axios from 'axios';
import AuthService from '../../../util/AuthService';
import { BACKEND_API_URL } from '../../../actions/types';

class SingleMessage extends React.Component {
    constructor() {
        super();
        this.state = {
            isRecipient: false,
            isPhotoMessage: false,
            mediaUrls: [],
            mediaUrl: '',
            isRateLimitExceeded: false
        }
        this.isError = false;
        this.Auth = new AuthService();
        this.convertTime = new ConvertDMTime();
    }

    componentDidMount() {
        let jwtToken = this.Auth.getRightSocialToken();
        this.props.mediaEntities.map((media, index) => {
            if(media.type === "photo") {
                axios({
                    url: `${BACKEND_API_URL}/twitter/direct/message/person/photo/retrieve/${jwtToken}`,
                    method: 'POST',
                    data: media.mediaUrl,
                    headers: {
                        'Accept': 'application/json, text/plain, */*',
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if(response.status === 429) {
                        this.setState({ isRateLimitExceeded: true });
                        this.isError = true;
                        return;
                    }
                    if(!this.isError) {
                        this.setState({
                            mediaUrl: response.data,
                            isPhotoMessage: true
                        });
                    }
                });
            }
        })

        if(this.props.senderId === this.props.twitterOwnerId) {
            $(`.twitter-single-message-wrapper:eq(${this.props.index})`).css({ 
                "justify-content": "flex-end",
                "display": "flex",
                "align-items": "center"
            });
            $(`.twitter-single-message-container:eq(${this.props.index})`).css({ "text-align": "right" });
            $(`.twitter-single-message-text:eq(${this.props.index})`).css({ "background-color": "#00A4EF", "border-radius": "25px 25px 0 25px" });
            $(`.twitter-single-message-text:eq(${this.props.index})`).hover(function() {
                $(this).css({"background-color": "#0094D8"});
            }, function() {
                $(this).css({"background-color": "#00A4EF"});
            });
        } else {
            this.setState({ isRecipient: true }, () => {
                $(`.twitter-single-message-wrapper:eq(${this.props.index})`).css({ "justify-content": "flex-start" });
                $(`.twitter-single-message-container:eq(${this.props.index})`).css({ "text-align": "left" });
                $(`.twitter-single-message-text:eq(${this.props.index})`).css({ "background-color": "#E5ECF0", "border-radius": "25px 25px 25px 0" });
            })
        }
    }
    render() {
        let isPhotoMessage = this.state.isPhotoMessage;
        let isRecipient = this.state.isRecipient;
        let createdAt = this.convertTime.formatDate(this.props.createdAt, true);
        let isRateLimitExceeded = this.state.isRateLimitExceeded;
        return (
            <>
                {
                    isPhotoMessage && <img className="twitter-dm-photo-message" src={this.state.mediaUrl} />
                    && <img src={`data:image/jpeg;base64,${this.state.mediaUrl}`} />
                }
                {
                    !isPhotoMessage && isRateLimitExceeded &&
                    <span style={{ color: "red", fontSize: "0.65vw"}}>Twitter rate limit exceeded for photo download.</span>
                }
                <div className="twitter-single-message-wrapper">
                    { isRecipient &&
                        <div className="twitter-single-message-avatar">
                            <img src={this.props.pictureUrl} alt="Avatar" />
                        </div>
                    }
                    <div className="twitter-single-message-container">
                        <div className="twitter-single-message-text">
                            { this.props.text }
                        </div>
                            { createdAt }
                    </div>
                </div>
            </>
        );
    }
}

export default SingleMessage;