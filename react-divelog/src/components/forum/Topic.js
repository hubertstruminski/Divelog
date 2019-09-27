import React from 'react';
import '../../css/Topic.css';
import { Redirect } from 'react-router-dom';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';

class Topic extends React.Component {
    constructor(props) {
        super(props);

        this.onTopicClick = this.onTopicClick.bind(this);
    }

    onTopicClick(e) {
        e.preventDefault();
        this.props.history.push(`/topic/${this.props.id}/${this.props.languageForum}/posts`);
    }

    render() {
        return(
            <div className="topic-container">
                <div className="topic-body">
                    <div className="grid-topic-one">
                        <div>
                            <i className="fas fa-chevron-circle-up like-icon"></i>
                        </div>
                        <div className="counter">
                            { this.props.likes }
                        </div>
                        <div>
                            <i className="fas fa-chevron-circle-down like-icon"></i>
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
                            <span>0</span> answers
                        </div>
                        <div>
                            <span>0</span> display
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Topic);