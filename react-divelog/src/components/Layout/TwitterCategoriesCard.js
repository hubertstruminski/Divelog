import React from 'react';
import '../../css/TwitterCategoriesCard.css';

class TwitterCategoriesCard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            searchPeople: ''
        }

        this.onTwitterSearchChange = this.onTwitterSearchChange.bind(this);
    }

    onTwitterSearchChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    componentDidMount() {

    }

    render() {
        return (
            <div>
                <ul className="list-group">
                    <li className="list-group-item">
                        <input 
                            type="text"
                            value={this.state.searchPeople}
                            placeholder="Search twitter people"
                            className="form-control input-search-twitter"
                            onChange={this.onTwitterSearchChange}
                        />
                    </li>
                    <li className="list-group-item">
                        Notifications
                    </li>
                    <li className="list-group-item">
                        Messages
                    </li>
                    <li className="list-group-item">
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