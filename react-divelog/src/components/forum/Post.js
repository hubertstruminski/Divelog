import React from 'react';
import '../../css/TopicWithPosts.css';
import ReactPlayer from 'react-player';
import swal from 'sweetalert';

class Post extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            email: '',
            isOwner: false
        }
        this.addImages = this.addImages.bind(this);
        this.addVideos = this.addVideos.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
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
        return this.props.files.map((file, index) => {
            if(file.type.includes('image')) {
                return (
                    <div>
                        <br />
                        <img 
                            src={file.url} 
                            alt={file.name} 
                            className="attachment-images"
                        />
                        <br />
                        { file.name }
                        <br />
                    </div>
                );
            }
        });
    }

    addVideos() {
        return this.props.files.map((file, index) => {
            if(file.type.includes('video')) {
                return (
                    <div>
                        <br />
                        <ReactPlayer 
                            url={file.url} 
                            playing
                            controls="true"
                        />
                        <br />
                        { file.name }
                        <br />
                    </div>
                );
            }
        })
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
                swal("Success", "Post has been removed successfully.", "success");
            }
        });
    }

    render() {
        let isOwner = this.state.isOwner;

        return (
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
                    <div className="main-post-message">
                        { this.props.message }
                    </div>
                    <div className="main-post-attachments">
                        { this.addImages() }
                        { this.addVideos() }
                    </div>
            
                    <div>
                        { isOwner &&
                            <>
                                <hr />
                                <button className="btn btn-warning">
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
                    </div>
                </div>
            </div>
        );
    }
}

export default Post;