import React from 'react';
import '../../css/AddPosts.css';
import ReactFilestack from 'filestack-react';
import swal from 'sweetalert';
import axios from 'axios';
import { withRouter } from 'react-router';

class AddPosts extends React.Component {
    constructor(props) {
        super(props);
    
        this.state = {
            message: '',
            isInvalidMessage: false,
            successFiles: [],
            successNameFiles: [],
            isSuccessUploaded: false,
            isFailureUploaded: false,
            failureNameFiles: []
        }
        this.errors = [];

        this.onChange = this.onChange.bind(this);
        this.onSubmitForm = this.onSubmitForm.bind(this);
        this.validateForm = this.validateForm.bind(this);
        this.responseFilestack = this.responseFilestack.bind(this);
        this.onErrorFilestack = this.onErrorFilestack.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    showInvalidMessage(isInvalidMessage) {
        if(isInvalidMessage) {
            return (
                <div className="alert alert-danger">
                    Message length must be minimum 10 characters.
                </div>
            );
        }
        return null;
    }

    showSuccessUploadedFiles(isSuccessUploaded) {
        if(isSuccessUploaded) {
            return (
                <div className="alert alert-warning">
                    Success uploaded files:
                    { this.state.successNameFiles.map((name, index) => {
                        return <p>{name}</p>
                    })}
                </div>
            );
        }
        return null;
    }

    showFailureUploadedFiles(isFailureUploaded) {
        if(isFailureUploaded) {
            return (
                <div className="alert alert-danger">
                    Failure uploaded files:
                    { this.state.failureNameFiles.map((name, index) => {
                        return <p>{name}</p>
                    })}
                </div>
            );
        }
        return null;
    }

    validateForm(e) {
        if(this.state.message.length < 10) {
            e.preventDefault();
            this.setState({ isInvalidMessage: true });
            this.errors.push(true);
        }
    }

    onSubmitForm(e) {
        e.preventDefault();

        this.validateForm(e);

        if(this.errors.length === 0) {
            let jwtToken = localStorage.getItem("JwtToken");
            let topicId = this.props.topicId;
            let languageForum = this.props.languageForum;

            const post = {
                message: this.state.message,
                languageForum: languageForum,
                topicId: topicId,
                jwtToken: jwtToken,
                files: this.state.successFiles,
                isPostOwner: true
            }

            axios({
                url: "/add/post",
                method: 'POST',
                data: JSON.stringify(post),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if(response.status !== 200) {
                    swal("Error", "Something goes wrong.", "error");
                } else {
                    this.props.history.push("/forum");
                }
            });
        }
    }

    responseFilestack(response) {
        response.filesUploaded.map((file, index) => {
            const element = {
                objectId: file.uploadId,
                url: file.url,
                size: file.size,
                name: file.originalFile.name,
                type: file.originalFile.type
            };
            this.setState({ 
                successFiles: this.state.successFiles.concat(element),
                successNameFiles: this.state.successNameFiles.concat(file.originalFile.name),
                isSuccessUploaded: true
            });
        });

        response.filesFailed.map((file, index) => {
            this.setState({ failureNameFiles: this.state.failureNameFiles.concat(file.originalFile.name) });
        });
    }

    onErrorFilestack() {
        swal("Error", "Something goes wrong.", "error");
    }

    render() {
        return (
            <div className="add-topic-center">
                <div className="new-topic-box new-topic-box-shadow">
                    <div className="new-topic-title">
                        Add new post
                    </div>

                    <form onSubmit={this.onSubmitForm}>
                        <div className="form-group">
                            <label className="new-post-label" for="message">Message</label>
                            <textarea
                                className="form-control form-control-lg new-topic-textarea"
                                id="message"
                                name="message"
                                value={this.state.message}
                                onChange={this.onChange}
                                rows="5"
                                style={{ color: 'white' }}
                            >
                            </textarea>
                        </div>
                        { this.showInvalidMessage(this.state.isInvalidMessage) }

                        <div className="form-group">
                            <label className="new-post-label">Upload data</label>
                            <br />
                            <ReactFilestack
                                apikey="Abn3RoxlVQeWNtMpk2Gflz"
                                onSuccess={this.responseFilestack}
                                onError={this.onErrorFilestack}
                                componentDisplayMode={{
                                    type: 'button',
                                    customText: 'Upload files',
                                    customClass: 'btn btn-warning btn-upload'
                                }}
                            />
                            <div style={{ clear: "both" }}></div>
                            { this.showSuccessUploadedFiles(this.state.isSuccessUploaded) }
                            { this.showFailureUploadedFiles(this.state.isFailureUploaded) }
                        </div>

                        <button 
                            type="submit" 
                            className="btn btn-primary new-topic-btn"
                        >
                            Publicate
                        </button>
                    </form>
                </div>
            </div>
        );
    }
}

export default withRouter(AddPosts);