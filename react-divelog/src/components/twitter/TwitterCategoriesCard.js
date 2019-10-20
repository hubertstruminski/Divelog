import React from 'react';
import '../../css/TwitterCategoriesCard.css';
import { withRouter } from 'react-router';
import withAuth from '../../util/withAuth';
import { Link } from 'react-router-dom';

class TwitterCategoriesCard extends React.Component {
    constructor(props) {
        super(props);
        
    }

    render() {
        return (
            <div className="twitter-categories-container">
                <ul className="list-group twitter-categories-list">
                    <li className="list-group-item list-group-item-hover">

                        <div className="twitter-categories-icons-box">
                            <i className="fas fa-home"></i> Home
                        </div>
                    </li>
                    <Link to="/twitter/explore" className="list-group-item list-group-item-hover">
                        <div className="twitter-categories-icons-box">
                            <span style={{ fontWeight: '900' }}>#</span> Explore
                        </div>
                    </Link>
                    <li className="list-group-item list-group-item-hover">
                        <div className="twitter-categories-icons-box">
                            <i className="fas fa-clock"></i> Timeline likes
                        </div>
                    </li>
                    <li className="list-group-item list-group-item-hover">
                        <div className="twitter-categories-icons-box">
                            <i className="fas fa-bell"></i> Notifications
                        </div>
                    </li>
                    <li className="list-group-item list-group-item-hover">
                        <div className="twitter-categories-icons-box">
                            <i className="far fa-envelope"></i> Messages
                        </div>
                    </li>
                    <li 
                        className="list-group-item list-group-item-hover"
                    >
                        <div className="twitter-categories-icons-box">
                            <img 
                            src={this.props.pictureUrl} 
                            alt="Avatar" 
                            className="twitter-categories-avatar"
                            /> Profile
                        </div>
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

export default withRouter(withAuth(TwitterCategoriesCard, { twitterExploreForCategories: true }));