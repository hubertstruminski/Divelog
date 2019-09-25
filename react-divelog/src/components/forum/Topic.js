import React from 'react';
import '../../css/Topic.css';

class Topic extends React.Component {
    constructor(props) {
        super(props);
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
                        <div className="title-topic">
                            { this.props.title }
                        </div>
                        <div className="owner-topic">
                            {/* Created at { this.props.createdAt } by { this.props.owner } */}
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

export default Topic;