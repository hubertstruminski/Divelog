import React from 'react';
import '../../../css/twitter-home/TwitterHomeAdd.css';
import * as filestack from 'filestack-js';
import ReactFilestack from 'filestack-react';
import $ from 'jquery';

class TwitterHomeAdd extends React.Component {
    constructor() {
        super();

        this.state = {
            files: [],
            failedFiles: [],
            isSuccessUploaded: false
        }
        this.removeRenderedFile = this.removeRenderedFile.bind(this);
        this.renderFiles = this.renderFiles.bind(this);
        this.filestackCallback = this.filestackCallback.bind(this);
    }

    componentDidMount() {

    }

    removeRenderedFile(handle) {
        console.log(handle);
    }

    renderFiles(isSuccessUploaded) {
        if(isSuccessUploaded) {
            $("tweet-add-uploaded-files").html("");
            return this.state.files.map((file, index) => {
                if(file.mimetype.includes("image")) {
                    return (
                        <React.Fragment>
                            <img
                                className="tweet-add-rendered-file-image"
                                src={file.url}
                                alt={file.filename}
                            />
                            <br />
                            <button className="upload-file-delete" onClick={() => this.removeRenderedFile(file.handle)}>Delete - {file.filename}</button>
                        </React.Fragment>
                    );
                } else {
                    return (
                        <React.Fragment>
                            <img
                                className="tweet-add-rendered-file-image"
                                src={file.url}
                                alt={file.filename}
                            />
                            <br />
                            <button className="upload-file-delete" onClick={() => this.removeRenderedFile(file.handle)}>Delete - {file.filename}</button>
                        </React.Fragment>
                    );
                }
            });
        }
        return null;
    }

    filestackCallback(response) {
        console.log(response);

        response.filesUploaded.map((fileUploaded, index) => {
            this.setState({ 
                files: this.state.files.concat(fileUploaded),
                isSuccessUploaded: true
            });
        });

        response.filesFailed.map((fileFailed, index) => {
            this.setState({ failedFiles: this.state.failedFiles.concat(fileFailed) });
        });
    }

    onPick() {
        const clientOptions = {
            security: {
                policy: "",
                signature: ""
            }
        }
        const client = filestack.init("Abn3RoxlVQeWNtMpk2Gflz");
        client.picker({}).open();
    }

    render() {
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
                            placeholder="What's happening?"
                        >  
                        </textarea>
                        
                    
                    </div>
                </div>
                <div className="tweet-add-uploaded-files">
                    { this.renderFiles(this.state.isSuccessUploaded) }
                </div>
                <div className="add-tweet-buttons">
                    <ReactFilestack
                        apikey="Abn3RoxlVQeWNtMpk2Gflz"
                        // action="pick"
                        actionOptions={{
                            maxSize: 5 * 1024 * 1024,
                            // uploadInBackground: false,
                            maxFiles: 4
                        }}
                        customRender={({ onPick }) => (
                            <i 
                                className="far fa-images" 
                                style={{ color: '#00A4EF', fontSize: "1.5vw", width: "15%", marginTop: "3%"}}
                                onClick={onPick}
                            ></i>
                        )}
                        onSuccess={this.filestackCallback}
                    />
                    <button 
                        className="add-tweet-button-home-timeline"
                    >
                        Tweet
                    </button>
                </div>
            </div>
        );
    }
}

export default TwitterHomeAdd;