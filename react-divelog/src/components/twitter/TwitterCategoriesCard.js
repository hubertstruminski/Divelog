import React from 'react';
import '../../css/TwitterCategoriesCard.css';
import $ from 'jquery';

class TwitterCategoriesCard extends React.Component {
    constructor(props) {
        super(props);

        this.onProfileClick = this.onProfileClick.bind(this);
        this.onRenderProfil = this.onRenderProfil.bind(this);
    }

    onProfileClick() {
        $(".tweets-likes-container").css({ "display": "none" });
        $(".twitter-user-profil").css({ "display": "block" })
        
    }

    onRenderProfil() {
        return (
            <div className="twitter-user-profil">
                <div className="twitter-header-profile">

                </div>
                <a class="twitter-timeline" href={`https://twitter.com/${this.props.screenName}?ref_src=twsrc%5Etfw`}>Tweets by {this.props.screenName}</a>
            </div>
        );
    }

    render() {
        return (
            <div>
                <ul className="list-group">
                    <li className="list-group-item list-group-item-hover">
                        <i className="fas fa-bell"></i> Notifications
                    </li>
                    <li className="list-group-item list-group-item-hover">
                        <i className="far fa-envelope"></i> Messages
                    </li>
                    <li 
                        className="list-group-item list-group-item-hover"
                        onClick={this.onProfileClick}
                    >
                        <img 
                            src={this.props.pictureUrl} 
                            alt="Avatar" 
                            className="twitter-categories-avatar"
                        /> Profile
                    </li>
                    <li className="list-group-item list-group-item-hover add-tweet-button">
                        <a 
                            className="twitter-share-button"
                            href="https://twitter.com/intent/tweet"
                            data-size="large"
                        >
                            Tweet
                        </a>
                    </li>
                </ul>
            </div>
        );
    }
}

export default TwitterCategoriesCard;