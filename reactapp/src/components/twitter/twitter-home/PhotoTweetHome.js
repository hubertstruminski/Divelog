import React from 'react';
import '../../../css/twitter-home/PhotoTwitterHome.css';
import $ from 'jquery';
import axios from 'axios';
import {BACKEND_API_URL} from '../../../actions/types';
import swal from 'sweetalert';

class PhotoTweetHome extends React.Component {
    constructor(props) {
        super(props);
        this.onDeleteClick = this.onDeleteClick.bind(this);
    }

    componentDidMount() {
        $(`.tweet-add-rendered-file-image:eq(${this.props.count})`).css({
            "background-image": `url(${this.props.src})`,
            "background-size": "100% 100%"
        });
    }

    onDeleteClick() {
        this.props.removeRenderedFile(this.props.handle);

        axios({
            url: `${BACKEND_API_URL}/filestack/file/delete/${this.props.handle}`,
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).catch(error => {
            swal("Error", "Photo was not removed from database.", "error");
        }); 
    }

    render() {
        return (
            <div className="tweet-add-rendered-file-container">
                <div className="tweet-add-rendered-file-image">
                </div>
                <i 
                    className="fas fa-times-circle tweet-add-image-cancel-icon"
                    onClick={this.onDeleteClick}
                ></i>
            </div>
        );
    }
}

export default PhotoTweetHome;