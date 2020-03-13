import React from 'react';
import '../../../css/twitter-home/TwitterHomeAdd.css';
import * as filestack from 'filestack-js';
import ReactFilestack from 'filestack-react';
import $ from 'jquery';
import swal from 'sweetalert';
import axios from 'axios';
import AuthService from '../../../util/AuthService';
import { BACKEND_API_URL } from '../../../actions/types';
import PhotoTweetHome from './PhotoTweetHome';

class TwitterHomeAdd extends React.Component {
    constructor() {
        super();

        this.state = {
            files: [],
            failedFiles: [],
            isSuccessUploaded: false,
            isFailedUploaded: false,
            message: ''
        }
        this.Auth = new AuthService();

        this.onChange = this.onChange.bind(this);
        this.removeRenderedFile = this.removeRenderedFile.bind(this);
        this.renderFiles = this.renderFiles.bind(this);
        this.filestackCallback = this.filestackCallback.bind(this);
        this.onErrorFilestack = this.onErrorFilestack.bind(this);
        this.onSubmitTweet = this.onSubmitTweet.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value }, () => {
            if(this.state.message.length > 0) {
                $(".add-tweet-button-home-timeline").css({ "background-color": "#00A4EF" });
                $(".add-tweet-button-home-timeline").hover(function() {
                    $(".add-tweet-button-home-timeline").css({ "background-color": "#0094D8" });
                }, function() {
                    $(".add-tweet-button-home-timeline").css({ "background-color": "#00A4EF" });
                });
            } else {
                $(".add-tweet-button-home-timeline").css({ "background-color": "#7FD1F7" });
            }
        });
    }

    removeRenderedFile(handle) {
        this.state.files.map((file, index) => {
            if(file.handle === handle) {
                this.setState({ 
                    files: this.state.files.filter((item, i) => i !== index) 
                }, () => {
                    this.renderFiles(this.state.isSuccessUploaded);
                });
            }
        });
    }

    renderFiles(isSuccessUploaded) {
        if(isSuccessUploaded) {
            let count = 0;
            $("tweet-add-uploaded-files").html("");
            return this.state.files.map((file, index) => {
                return (
                    <PhotoTweetHome 
                        count={count++}
                        key={index}
                        src={file.url}
                        name={file.name}
                        handle={file.handle}
                        removeRenderedFile={this.removeRenderedFile}
                    />
                );
            });
        }
        return null;
    }

    renderFailedFiles(isFailedUploaded) {
        if(isFailedUploaded) {
            return this.state.failedFiles.map((file, index) => {
                return (
                    <div>
                        {file.name}
                        <br />
                    </div>

                );
            });
        }
    }

    filestackCallback(response) {
        response.filesUploaded.map((fileUploaded, index) => {
            const element = {
                objectId: fileUploaded.uploadId,
                url: fileUploaded.url,
                size: fileUploaded.size,
                name: fileUploaded.originalFile.name,
                type: fileUploaded.originalFile.type,
                handle: fileUploaded.handle
            };
            this.setState({
                files: this.state.files.concat(element),
                isSuccessUploaded: true
            });
        });

        response.filesFailed.map((fileFailed) => {
            const element = {
                name: fileFailed.originalFile.name,
            }
            this.setState({ 
                failedFiles: this.state.failedFiles.concat(element),
                isFailedUploaded: true
            });
        });
    }

    onErrorFilestack() {
        swal(this.props.t("error-500.title"), this.props.t("error-500.message"), "error");
    }

    onPick() {
        const client = filestack.init("Abn3RoxlVQeWNtMpk2Gflz");
        client.picker({}).open();
    }

    onSubmitTweet(e) {
        e.preventDefault();

        const tweet = {
            message: this.state.message,
            files: this.state.files
        }
        let jwtToken = this.Auth.getRightSocialToken();

        axios({
            url: `${BACKEND_API_URL}/twitter/create/tweet/${jwtToken}`,
            method: 'POST',
            data: JSON.stringify(tweet),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if(response.status !== 200) {
                swal(this.props.t("error-500.title"), this.props.t("error-500.message"), "error");
                e.preventDefault();
            } else {
                this.props.addNewTweet(response.data);
            }
        }).catch(err => {
            console.log(err);
        });
    }

    render() {
        let isFailedUploaded = this.state.isFailedUploaded;
        return (
            <div className="add-tweet-container">
                <div className="avatar-tweet-textarea-container">
                    <div className="tweet-add-avatar-container">
                        <img 
                            src={this.props.pictureUrl} 
                            alt="Avatar" 
                            className="tweet-add-avatar"
                        />
                    </div>
                    <div className="tweet-add-textarea-container">
                        <textarea
                            name="message"
                            placeholder="What's happening?"
                            value={this.state.message}
                            onChange={this.onChange}
                            className="tweet-message-textarea"
                        >  
                        </textarea>
                    </div>
                </div>
                <div className="tweet-add-uploaded-files">
                    { this.renderFiles(this.state.isSuccessUploaded) }
                    {
                        isFailedUploaded &&
                        <div className="alert alert-danger">
                            Failed upload files:
                            <br />
                            { this.renderFailedFiles(this.state.isFailedUploaded) }
                        </div>
                    }
                </div>
                <div className="add-tweet-buttons">
                    <ReactFilestack
                        apikey="Abn3RoxlVQeWNtMpk2Gflz"
                        actionOptions={{
                            maxSize: 5 * 1024 * 1024,
                            maxFiles: 4,
                            accept: ["image/png", "image/jpg"]
                        }}
                        customRender={({ onPick }) => (
                            <i 
                                className="far fa-images" 
                                style={{ color: '#00A4EF', fontSize: "1.5vw", width: "15%", marginTop: "3%"}}
                                onClick={onPick}
                            ></i>
                        )}
                        onSuccess={this.filestackCallback}
                        onError={this.onErrorFilestack}
                    />
                    <button 
                        className="add-tweet-button-home-timeline"
                        onClick={this.onSubmitTweet}
                    >
                        Tweet
                    </button>
                </div>
            </div>
        );
    }
}

export default TwitterHomeAdd;