import React from 'react';
import '../../css/Forum.css';
import poland from '../../img/flags/poland.jpg';
import germany from '../../img/flags/germany.png';
import england from '../../img/flags/england.jpg';
import $ from 'jquery';
import { Link } from 'react-router-dom';
import swal from 'sweetalert';

class Forum extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedForum: '',
            isEnglishForum: false,
            isPolishForum: false,
            isGermanyForum: false
        }
        this.onFlagClick = this.onFlagClick.bind(this);
        this.onCreateTopicClick = this.onCreateTopicClick.bind(this);
    }

    onFlagClick(e) {
        e.preventDefault();

        $("#polandFlag").click(() => {
            this.setState({ 
                selectedForum: 'POLISH'
            }, () => {
                $("#polandFlag").addClass("isActiveFlag");
                $("#germanyFlag").removeClass("isActiveFlag");
                $("#englandFlag").removeClass("isActiveFlag");
            });
        });

        $("#germanyFlag").click(() => {
            this.setState({ 
                selectedForum: 'GERMANY'
            }, () => {
                $("#germanyFlag").addClass("isActiveFlag");
                $("#polandFlag").removeClass("isActiveFlag");
                $("#englandFlag").removeClass("isActiveFlag");
            });
        });

        $("#englandFlag").click(() => {
            this.setState({ 
                selectedForum: 'ENGLISH'
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

    render() {
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
            </div>
        );
    }
}

export default Forum;