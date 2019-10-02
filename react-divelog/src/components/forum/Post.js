import React from 'react';
import '../../css/TopicWithPosts.css';
import '../../css/AddPosts.css';
import ReactPlayer from 'react-player';
import swal from 'sweetalert';
import $ from 'jquery';
import ReactFilestack from 'filestack-react';
import axios from 'axios';
import DeleteAttachmentButton from './DeleteAttachmentButton';

class Post extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            email: '',
            isOwner: false,
            isUpdating: false,
            successFiles: [],
            successNameFiles: [],
            isSuccessUploaded: false,
            isFailureUploaded: false,
            failureNameFiles: [],
            isError: false,
            wasUpdated: false
        }
        this.addImages = this.addImages.bind(this);
        this.addVideos = this.addVideos.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
        this.onEditClick = this.onEditClick.bind(this);
        this.responseFilestack = this.responseFilestack.bind(this);
        this.onErrorFilestack = this.onErrorFilestack.bind(this);
        this.renderDeleteButtonsForAttachments = this.renderDeleteButtonsForAttachments.bind(this);
    }

    componentDidMount() {
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/getuserdata/${jwtToken}`, {
            method: 'GET',
            headers: {
              'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            this.setState({ email: jsonData.email }, () => {
                if(this.props.user.email === this.state.email) {
                    this.setState({ isOwner: true });
                }
            });
        }); 
    }

    addImages() {
        let isUpdating = this.state.isUpdating;

        return this.props.files.map((file, index) => {
            if(file.type.includes('image')) {
                return (
                    <div id={`attachment${file.id}`}>
                        <br />
                        <img 
                            src={file.url} 
                            alt={file.name} 
                            className="attachment-images"
                        />
                        <br />
                        { !isUpdating && file.name }
                        <br />
                    </div>
                );
            }
        });
    }

    addVideos() {
        let isUpdating = this.state.isUpdating;

        return this.props.files.map((file, index) => {
            if(file.type.includes('video')) {
                return (
                    <div id={`attachment${file.id}`}>
                        <br />
                        <ReactPlayer 
                            url={file.url} 
                            playing
                            controls="true"
                        />
                        <br />
                        { !isUpdating && file.name }
                        <br />
                    </div>
                );
            }
        })
    }

    renderDeleteButtonsForAttachments() {
        return this.props.files.map((file, index) => {
            return (
                <DeleteAttachmentButton
                    key={index}
                    id={file.id}
                    name={file.name}
                    type={file.type}
                    counter={this.props.counter}
                    postId={this.props.id}
                    setDeletedFileForPost={this.props.setDeletedFileForPost}
                />
            );
        });
    }

    onDeleteClick() {
        let postId = this.props.id;
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/delete/post/${postId}/${jwtToken}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => {
            if(response.status !== 200) {
                swal("Error", "Something goes wrong.", "error");
            } else {
                this.props.fetchTopicAndPosts();
                swal("Success", "Post has been removed successfully.", "success");
            }
        });
    }

    onEditClick(e) {
        e.preventDefault();
        if(!this.state.isUpdating) {
            this.setState({ isUpdating: true }, () => {
                let counter = this.props.counter;
                let message = $(`.post-message:eq(${counter})`).html();

                $(`.post-message:eq(${counter})`).html("").append($("<div></div>").addClass("form-group edit-post-div"))
                $(`.post-message:eq(${counter}) > .edit-post-div`).append($("<textarea></textarea").attr("rows", "7").addClass("new-post-textarea edit-textarea"));
                $(`.post-buttons:eq(${counter})`).append($("<button>Confirm</button>").attr("id", "btnConfirm").addClass("btn btn-success btn-success-float"));
        
                $(".post-buttons").not(`.post-buttons:eq(${counter})`).css({ "display": "none"});

                $(`.post-message:eq(${counter}) > .edit-post-div > .edit-textarea`).val(message);

                $("#btnConfirm").click(() => {
                    if($(".edit-textarea").val().length < 5) {
                        $(`.post-message:eq(${counter})`).append(
                            $("<div></div>").addClass("alert alert-danger").text("Message length must be minimum 5 characters")
                        );
                    } else {
                        let postId = this.props.id;
                        let jwtToken = localStorage.getItem("JwtToken");

                        let message = $(".edit-textarea").val();
                        let files = this.state.successFiles;

                        const updatedPost = {
                            message: message,
                            files: this.state.successFiles
                        }
                        
                        axios({
                            method: 'PUT',
                            url: `/post/${postId}/${jwtToken}`,
                            data: updatedPost,
                            headers: {
                                "Accept": "application/json",
                                "Content-type": "application/json"
                            }
                        }).then(response => {
                            if(response.status !== 200) {
                                swal("Error", "Something goes wrong.", "error");
                            } else {
                                this.setState({ isUpdating: false }, () => {
                                    $(`.post-message:eq(${counter})`).html("");
                                    $(`.post-message:eq(${counter})`).text(message);
                                    $(".post-buttons").css({ "display": "block"}); 
                                    $(`.post-buttons:eq(${counter}) > #btnConfirm`).css({ "display": "none" });
                                    this.props.fetchTopicAndPosts();
                                });
                            }
                        });
                    }
                });
            });
        }
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
        let isOwner = this.state.isOwner;
        let isUpdating = this.state.isUpdating;

        let updatedAt = 'post updated at ' + this.props.updatedAt;
        updatedAt = updatedAt.substr(0, updatedAt.length - 1);

        return (
            <div className="main-post-center">
                <div className="main-post-container post-margin">
                    <div className="main-post-grid-one">
                        <div className="main-post-header">
                            { this.props.createdAt }
                        </div>
                        <div className="main-post-avatar">
                            <img src={this.props.user.pictureUrl} alt="Avatar" />
                        </div>
                        <div className="main-post-footer">
                            { this.props.user.name }
                        </div>
                    </div>
                    <div className="main-post-grid-two">
                        <div className="post-message">
                            { this.props.message }
                        </div>  
                        <div className="main-post-attachments">
                            { this.addImages() }
                            { this.addVideos() }
                        </div>
                
                        <div className="post-buttons">
                            { isOwner && !isUpdating &&
                                <>
                                    <hr />
                                    <button 
                                        className="btn btn-warning"
                                        onClick={this.onEditClick}
                                    >
                                        EDIT
                                    </button>
                                
                                    <button 
                                        className="btn btn-danger"
                                        onClick={this.onDeleteClick}
                                    >
                                        DELETE
                                    </button>
                                </>
                            }
                            { this.props.wasUpdatedPost && <div className="post-updated-box">{updatedAt}</div> }
                            { isUpdating && this.renderDeleteButtonsForAttachments() }
                            {
                                isUpdating && (
                                <div className="form-group edit-post-upload-files-div">
                                    <label>Upload data</label>
                                    <br />
                                    <ReactFilestack
                                        apikey="Abn3RoxlVQeWNtMpk2Gflz"
                                        onSuccess={this.responseFilestack}
                                        onError={this.onErrorFilestack}
                                        componentDisplayMode={{
                                            type: 'button',
                                            customText: 'Upload files',
                                            customClass: 'btn btn-warning edit-post-upload-file'
                                        }}
                                    />
                                    <div style={{ clear: "both" }}></div>
                                    { this.showSuccessUploadedFiles(this.state.isSuccessUploaded) }
                                    { this.showFailureUploadedFiles(this.state.isFailureUploaded) }
                                </div>
                                )
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Post;