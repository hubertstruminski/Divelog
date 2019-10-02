import React from 'react';
import '../../css/Topic.css';
import { withRouter } from 'react-router';
import axios from 'axios';
import swal from 'sweetalert';
import $ from 'jquery';

class Topic extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            numberDisplays: 0,
            numberComments: 0,
            likes: 0,
            vote: 0,
            isUpVoted: false,
            isDownVoted: false
        }
        this.onTopicClick = this.onTopicClick.bind(this);
        this.onUpVote = this.onUpVote.bind(this);
        this.onDownVote = this.onDownVote.bind(this);
        this.fetchTopicData = this.fetchTopicData.bind(this);
    }

    componentDidMount() {
        let jwtToken = localStorage.getItem("JwtToken"); 
        let topicId = this.props.id;

        fetch(`/get/topic/number/comments/${topicId}/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            console.log(jsonData);
            this.setState({
                numberDisplays: jsonData.numberDisplay,
                numberComments: jsonData.numberComments,
                likes: jsonData.likes,
                vote: jsonData.vote
            }, () => {
                if(this.state.vote === 1) {
                    console.log(this.props.count);
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "red" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "white" });
                    this.setState({ 
                        isUpVoted: true,
                        isDownVoted: false
                    });
                } else if(this.state.vote === -1) {
                    console.log(this.props.count);
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "red" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "white" });
                    this.setState({ 
                        isDownVoted: true,
                        isUpVoted: false 
                    });
                } else {
                    console.log(this.props.count);
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "white" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "white" });
                    this.setState({ 
                        isDownVoted: false,
                        isUpVoted: false
                    });
                }
            });
        });
    }

    onTopicClick(e) {
        e.preventDefault();
        this.props.history.push(`/topic/${this.props.id}/${this.props.languageForum}/posts`);
    }

    onUpVote() {
        let jwtToken = localStorage.getItem("JwtToken");
        if(!this.state.isUpVoted) {
            let isUpVoted = true;    
            axios({
                method: 'PUT',
                url: `/topic/likes/vote/${this.props.id}/${jwtToken}`,
                data: isUpVoted,
                headers: {
                    "Accept": "application/json",
                    "Content-type": "application/json"
                }
            }).then(response => {
                if(response.status !== 200) {
                    swal("Error", "Something goes wrong.", "error");
                } else {
                    this.setState({
                        numberDisplays: 0,
                        numberComments: 0,
                        likes: 0,
                        isUpVoted: true,
                        isDownVoted: false
                    }, () => {
                        $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "red" });
                        $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "white"});
                        this.fetchTopicData();
                    });
                }
            });
        } 
    }

    onDownVote() {
        let jwtToken = localStorage.getItem("JwtToken");
        if(!this.state.isDownVoted) {
            let isUpVoted = false;
            axios({
                method: 'PUT',
                url: `/topic/likes/vote/${this.props.id}/${jwtToken}`,
                data: isUpVoted,
                headers: {
                    "Accept": "application/json",
                    "Content-type": "application/json"
                }
            }).then(response => {
                if(response.status !== 200) {
                    swal("Error", "Something goes wrong.", "error");
                } else {
                    this.setState({
                        numberDisplays: 0,
                        numberComments: 0,
                        likes: 0,
                        isDownVoted: true,
                        isUpVoted: false
                    }, () => {
                        $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "white" });
                        $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "red"});
                        this.fetchTopicData();
                    });
                }
            });
        }
    }

    fetchTopicData() {
        let jwtToken = localStorage.getItem("JwtToken"); 
        let topicId = this.props.id;
        
        fetch(`/get/topic/number/comments/${topicId}/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            console.log(jsonData);
            this.setState({
                numberDisplays: jsonData.numberDisplay,
                numberComments: jsonData.numberComments,
                likes: jsonData.likes,
                vote: jsonData.vote
            }, () => {
                if(this.state.vote === 1) {
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "red" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "white" });
                    this.setState({ 
                        isUpVoted: true,
                        isDownVoted: false
                    });
                } else if(this.state.vote === -1) {
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "red" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "white" });
                    this.setState({ 
                        isDownVoted: true,
                        isUpVoted: false
                    });
                } else {
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(0) > i`).css({ "color": "white" });
                    $(`.grid-topic-one:eq(${this.props.count}) > div:eq(2) > i`).css({ "color": "white" });
                    this.setState({ 
                        isDownVoted: false,
                        isUpVoted: false
                    });
                }
            });
        });
    }

    render() {
        return(
            <div className="topic-container">
                <div className="topic-body">
                    <div className="grid-topic-one">
                        <div>
                            <i 
                                className="fas fa-chevron-circle-up like-icon"
                                onClick={this.onUpVote}
                            >
                            </i>
                        </div>
                        <div className="counter">
                            { this.state.likes }
                        </div>
                        <div>
                            <i 
                                className="fas fa-chevron-circle-down like-icon"
                                onClick={this.onDownVote}
                            >
                            </i>
                        </div>
                    </div>
                    <div className="grid-topic-two">
                        <div className="title-topic" onClick={this.onTopicClick}>
                            { this.props.title }
                        </div>
                        <div className="owner-topic">
                            <span className="floating-div">
                                { this.props.owner }
                            </span>

                            <span className="floating-div float-two-right">
                                {this.props.createdAt }
                            </span>

                            <div style={{ clear: 'both' }}></div>
                        </div>
                    </div>
                    <div className="grid-topic-three">
                        <div>
                            { this.state.numberComments } comments
                            <br />
                            { this.state.numberDisplays } display
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Topic);