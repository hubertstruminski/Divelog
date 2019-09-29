import React from 'react';
import '../../css/Forum.css';
import poland from '../../img/flags/poland.jpg';
import germany from '../../img/flags/germany.png';
import england from '../../img/flags/england.jpg';
import $ from 'jquery';
import { Link } from 'react-router-dom';
import swal from 'sweetalert';
import Topic from './Topic';
import ConvertTime from '../../util/ConvertTime';

class Forum extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedForum: '',
            isEnglishForum: false,
            isPolishForum: false,
            isGermanyForum: false,
            englishTopics: [],
            polishTopics: [],
            germanyTopics: []
        }
        this.ConvertTime = new ConvertTime();

        this.onFlagClick = this.onFlagClick.bind(this);
        this.onCreateTopicClick = this.onCreateTopicClick.bind(this);
        this.generatePolishTopics = this.generatePolishTopics.bind(this);
    }

    componentDidMount() {
        fetch("/get/topics/all", {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            console.log(jsonData);
            jsonData.map((topic, index) => {
                if(topic.languageForum === 'polish') {
                    let time = this.ConvertTime.convertTime(topic.createdAt, null, false);

                    const element = {
                        id: topic.id,
                        likes: topic.likes,
                        title: topic.title,
                        createdAt: time[0],
                        owner: topic.user.name
                    }
                    this.setState({ polishTopics: this.state.polishTopics.concat(element) });
                }

                if(topic.languageForum === 'english') {
                    let time = this.ConvertTime.convertTime(topic.createdAt, null, false);
                    
                    const element = {
                        id: topic.id,
                        likes: topic.likes,
                        title: topic.title,
                        createdAt: time[0],
                        owner: topic.user.name
                    }
                    this.setState({ englishTopics: this.state.englishTopics.concat(element) });
                }

                if(topic.languageForum === 'germany') {
                    let time = this.ConvertTime.convertTime(topic.createdAt, null, false);
                    
                    const element = {
                        id: topic.id,
                        likes: topic.likes,
                        title: topic.title,
                        createdAt: time[0],
                        owner: topic.user.name
                    }
                    this.setState({ germanyTopics: this.state.germanyTopics.concat(element) });
                }
            });
        });
    }

    onFlagClick(e) {
        e.preventDefault();

        $("#polandFlag").click(() => {
            this.setState({ 
                selectedForum: 'polish',
                isPolishForum: true
            }, () => {
                $("#polandFlag").addClass("isActiveFlag");
                $("#germanyFlag").removeClass("isActiveFlag");
                $("#englandFlag").removeClass("isActiveFlag");
            });
        });

        $("#germanyFlag").click(() => {
            this.setState({ 
                selectedForum: 'germany',
                isGermanyForum: true
            }, () => {
                $("#germanyFlag").addClass("isActiveFlag");
                $("#polandFlag").removeClass("isActiveFlag");
                $("#englandFlag").removeClass("isActiveFlag");
            });
        });

        $("#englandFlag").click(() => {
            this.setState({ 
                selectedForum: 'english',
                isEnglishForum: true
            }, () => {
                $("#englandFlag").addClass("isActiveFlag");
                $("#germanyFlag").removeClass("isActiveFlag");
                $("#polandFlag").removeClass("isActiveFlag");
            });
        });
    }

    onCreateTopicClick(e) {
        if(this.state.selectedForum === '') {
            e.preventDefault();
            swal("Warning", "You have to select language for forum", "warning");
        }
    }

    generatePolishTopics() {
        return this.state.polishTopics.map((topic, index) => {
             return (
                <Topic 
                    id={topic.id}
                    owner={topic.owner}
                    likes={topic.likes}
                    title={topic.title}
                    createdAt={topic.createdAt}
                    languageForum={this.state.selectedForum}
                />
            );
        })
    }

    render() {
        let isPolishForum = this.state.isPolishForum;
        let isGermanyForum = this.state.isGermanyForum;
        let isEnglishForum = this.state.isEnglishForum;

        return (
            <div className="forum-container">
                <div className="forum-title">
                    Select forum
                </div>
                <div className="language-forum-box language-forum-box-center language-shadow">
                    <div className="flag-item">
                        <img 
                            id="polandFlag"
                            src={poland} 
                            alt="Polish flag"
                            onClick={this.onFlagClick}
                        />
                    </div>
                    <div className="flag-item">
                        <img 
                            id="germanyFlag"
                            src={germany} 
                            alt="Germany flag" 
                            onClick={this.onFlagClick}
                        />
                    </div>
                    <div className="flag-item">
                        <img 
                            id="englandFlag"
                            src={england} 
                            alt="English flag"
                            onClick={this.onFlagClick} 
                        />
                    </div>
                </div>
                <Link to={`/create/topic/${this.state.selectedForum}`}>
                    <button 
                        className="btn btn-primary btn-padding"
                        onClick={this.onCreateTopicClick}
                    >
                        CREATE TOPIC
                    </button>
                </Link>
                <div className="wrapper-forum-box">
                    <div className="forum-topics-box">
                        { isPolishForum && this.generatePolishTopics() }
                    </div>
                    <div className="forum-top-topics"></div>
                </div>
            </div>
        );
    }
}

export default Forum;