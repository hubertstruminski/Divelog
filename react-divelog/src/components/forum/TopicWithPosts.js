import React from 'react';
import '../../css/TopicWithPosts.css';
import ConvertTime from '../../util/ConvertTime';
import AddPosts from '../forum/AddPosts';

class TopicWithPosts extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            mainPost: {},
            isRetrieved: false
        }

        this.files = []
        this.ConvertTime = new ConvertTime();

        this.addImages = this.addImages.bind(this);
    }

    componentDidMount() {
        let id = this.props.match.params.id;

        fetch(`/get/topic/posts/${id}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            this.files = [];

            jsonData.files.map((file, index) => {
                const element = {
                    id: file.id,
                    objectId: file.objectId,
                    url: file.url,
                    size: file.size,
                    name: file.name,
                    type: file.type
                }
                this.files.push(element);
            });

            const element = {
                title: jsonData.title,
                message: jsonData.message,
                createdAt: this.ConvertTime.convertTime(jsonData.createdAt, null, false)[0],
                owner: jsonData.user.name,
                pictureUrl: jsonData.user.pictureUrl,
                files: this.files
            }
            this.setState({ 
                mainPost: element, 
                isRetrieved: true
            });
            console.log(this.state.mainPost);
        });
    }

    addImages() {
        return this.state.mainPost.files.map((file, index) => {
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

    render() {
        let isRetrieved = this.state.isRetrieved;

        return (
            <div className="topic-posts-container">
                <div className="main-post-center">
                    <div className="main-post-container">
                        <div className="main-post-grid-one">
                            <div className="main-post-header">
                                { this.state.mainPost.createdAt }
                            </div>
                            <div className="main-post-avatar">
                                <img src={this.state.mainPost.pictureUrl} alt="Avatar" />
                            </div>
                            <div className="main-post-footer">
                                { this.state.mainPost.owner }
                            </div>
                        </div>
                        <div className="main-post-grid-two">
                            <div className="main-post-title">
                                { this.state.mainPost.title }
                            </div>
                            <div className="main-post-message">
                                { this.state.mainPost.message }
                            </div>
                            <div className="main-post-attachments">
                                { isRetrieved && this.addImages() }
                            </div>
                        </div>
                    </div>
                </div>
                <hr className="line-break-posts"/>

                <div className="main-post-center">
                    {/* User posts */}
                </div>

                <div className="">
                    <AddPosts 
                        topicId={this.props.match.params.id}
                        languageForum={this.props.match.params.languageForum}
                    />
                </div>
            </div>
        );
    }
}

export default TopicWithPosts;