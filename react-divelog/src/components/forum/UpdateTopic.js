import React from 'react';
import '../../css/AddTopic.css';
import ReactFilestack from 'filestack-react';
import swal from 'sweetalert';
import axios from 'axios';
import { withRouter } from 'react-router';

class UpdateTopic extends React.Component {
    constructor(props) {
        super(props);
    
        this.state = {
            title: '',
            message: '',
            isInvalidTitle: false,
            isInvalidMessage: false,
            successFiles: [],
            successNameFiles: [],
            isSuccessUploaded: false,
            isFailureUploaded: false,
            failureNameFiles: [],
            languageForum: ''
        }
        this.errors = [];
        this.files = []

        this.onChange = this.onChange.bind(this);
        this.onSubmitForm = this.onSubmitForm.bind(this);
        this.validateForm = this.validateForm.bind(this);
        this.responseFilestack = this.responseFilestack.bind(this);
        this.onErrorFilestack = this.onErrorFilestack.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    componentDidMount() {
        let topicId = this.props.match.params.id;

        fetch(`/get/topic/${topicId}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            jsonData.files.map((file, index) => {
                const element = {
                    id: file.id,
                    objectId: file.objectId,
                    url: file.url,
                    size: file.size,
                    name: file.name,
                    type: file.type
                }
                this.setState({ 
                    successFiles: this.state.successFiles.concat(element),
                    successNameFiles: this.state.successNameFiles.concat(file.name)
                });
            });
            this.setState({ 
                title: jsonData.title,
                message: jsonData.message,
                languageForum: jsonData.languageForum
            });
            
        });
    }

    showInvalidTitle(isInvalidTitle) {
        if(isInvalidTitle) {
            return (
                <div className="alert alert-danger">
                    Title length must be between 10 and 160 characters.
                </div>
            );
        }
        return null;
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
        if(this.state.title.length < 10 || this.state.title.length > 160) {
            e.preventDefault();
            this.setState({ isInvalidTitle: true });
            this.errors.push(true);
        }

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
            let topicId = this.props.match.params.id;
            
            const updatedTopic = {
                title: this.state.title,
                message: this.state.message,
                files: this.state.successFiles
            }

            axios({
                method: 'PUT',
                url: `/update/topic/${topicId}`,
                data: updatedTopic,
                headers: {
                    "Accept": "application/json",
                    "Content-type": "application/json"
                }
            }).then(response => {
                if(response.status !== 200) {
                    swal("Error", "Something goes wrong.", "error");
                } else {
                    // this.props.history.push(`topic/${topicId}/${this.state.languageForum}/posts`);
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
            this.setState({ 
                failureNameFiles: this.state.failureNameFiles.concat(file.originalFile.name),
                isFailureUploaded: true
            });
        });
    }

    onErrorFilestack() {
        swal("Error", "Something goes wrong.", "error");
    }

    render() {
        return (
            <div className="add-topic-container add-topic-center">
                <div className="new-topic-box">
                    <div className="new-topic-title">
                        Update topic
                    </div>

                    <form onSubmit={this.onSubmitForm}>
                        <div className="form-group">
                            <label for="title">Title</label>
                            <input 
                                className="form-control form-control-lg new-update-topic-inputs"
                                type="text"
                                id="title"
                                name="title"
                                placeholder="Enter title"
                                value={this.state.title}
                                onChange={this.onChange}
                                style={{ color: 'white' }}
                            />
                        </div>
                        { this.showInvalidTitle(this.state.isInvalidTitle) }

                        <div className="form-group">
                            <label for="message">Message</label>
                            <textarea
                                className="form-control form-control-lg new-update-topic-inputs"
                                id="message"
                                name="message"
                                value={this.state.message}
                                onChange={this.onChange}
                                rows="17"
                                style={{ color: 'white' }}
                            >
                            </textarea>
                        </div>
                        { this.showInvalidMessage(this.state.isInvalidMessage) }

                        <div className="form-group">
                            <label>Upload data</label>
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

export default withRouter(UpdateTopic);