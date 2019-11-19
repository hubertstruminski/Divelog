import React from 'react';
import '../../../css/twitter-messages/TwitterMessagesSearch.css';

class TwitterMessagesSearch extends React.Component {
    render() {
        return (
            <div className="twitter-messages-search-input-container">
                <input 
                    className="twitter-messages-search-input" 
                    type="text" 
                    placeholder="Search for people and groups"
                />
            </div>
        );
    }
}

export default TwitterMessagesSearch;